package com.unimelb.swen90007.reactexampleapi.api.mappers;

import com.unimelb.swen90007.reactexampleapi.api.objects.DomainObject;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DoesNotExistException;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.StatementSource;
import org.postgresql.jdbc.PgConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractMapper {
    static final Connection conn = DBUtil.connection();

    public Connection getConn() {
        return conn;
    }

    protected Map loadedMap = new HashMap();

//    Connection conn = DBUtil.connection();

    /** Find one data from the database with key.
     * @param key Find data by primary key of each object.
     * @return Data of domain object found in database
     * @throws SQLException
     * @throws DoesNotExistException
     */
    public DomainObject find(Key key){
//        setConn(conn);
        // Use map to reduce loading time.
        DomainObject result = (DomainObject)loadedMap.get(key);
        if(result != null) return result;

        ResultSet rs = null;
        PreparedStatement findStatement = null;
        try{
            findStatement = getConn().prepareStatement(findStatement());

            doFind(key, findStatement);
            rs = findStatement.executeQuery();
            if(rs.next()){
                result = load(rs);
            }else throw new DoesNotExistException();

//            conn.close();
            return result;
        } catch (DoesNotExistException e) {
            throw new RuntimeException("Object to find does not exist.");
        } catch (SQLException e){
            throw new RuntimeException("Failed to find object in database.");
        } finally {
            DBUtil.cleanUp(findStatement, rs);
        }
    }

    /** Find more than one data from the database.
     * @param source An interface wrapping both SQL script and statement's parameters.
     * @return List of domain objects found in database.
     * @throws SQLException
     */
    public List<DomainObject> findMany(StatementSource source) {
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = conn.prepareStatement(source.sqlQuery());
            if(source.parameters() != null) {
                for (int i = 0; i < source.parameters().length; i++) {
                    assert statement != null;
                    statement.setObject(i + 1, source.parameters()[i]);
                }
            }
            rs = statement.executeQuery();
//            conn.close();
            return loadAll(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find objects in database.");
        } finally {
            DBUtil.cleanUp(statement, rs);
        }
    }

    /** Complete searching query. Default to use UUID as the unique primary key.
     * @param key Primary key used for searching.
     * @param finder PreparedStatement that will be executed for searching.
     * @throws SQLException
     */
    protected void doFind(Key key, PreparedStatement finder) throws SQLException {
        finder.setString(1, key.getId().toString());
    }

    /** Load one data found from database.
     * @param rs Result found in finding query.
     * @return Domain object should be loaded from database.
     * @throws SQLException
     */
    protected DomainObject load(ResultSet rs) throws SQLException{
        Key pk = addKey(rs);

        if(loadedMap.get(pk) != null) {
            return (DomainObject) loadedMap.get(pk);
        }else {
            DomainObject result = doLoad(pk, rs);
            return result;
        }
    }

    protected DomainObject load(Key key, ResultSet rs) throws SQLException{
        DomainObject result = doLoad(key, rs);
        loadedMap.put(key, result);
        return result;
    }

    /** Load many data from database.
     * @param rs Cursor to the result found in query.
     * @return List of domain object should be loaded from database
     * @throws SQLException
     */
    protected List<DomainObject> loadAll(ResultSet rs) throws SQLException {
        List<DomainObject> results = new ArrayList<>();
        while (rs.next()) {
//            System.out.println("+1");
            results.add(load(rs));
        }
        return results;
    }

    /** Implement load method of each mapper.
     *
     * @param key Primary key of domain object to be loaded from database.
     * @param rs Cursor to the result found in query.
     * @return Domain object that should be retrieved from database.
     * @throws SQLException
     */
    abstract protected DomainObject doLoad(Key key, ResultSet rs) throws SQLException;

    /** Create the key and for loading. Default to create UUID as the unique primary key.
     * @param rs Cursor to the result found in query.
     * @return Primary key of the loading object in rs.
     * @throws SQLException
     */
    protected Key addKey(ResultSet rs) throws SQLException{
        return new Key(rs.getObject("id", UUID.class));
    }



    /** Create a domain object and insert it into the database.
     * @param object The domain object with information in need of inserting into database.
     * @return The primary key of the inserted data.
     * @throws SQLException
     */
    public Key insert(DomainObject object, Connection conn) {
        PreparedStatement insertStatement = null;
        ResultSet rs = null;
        try {
            Key pk = null;
            insertStatement = conn.prepareStatement(insertStatement());
            insertKey(object, insertStatement);
            insertData(object, insertStatement);

            rs = insertStatement.executeQuery();
            if (rs.next()) {
                System.out.println("do insert rs " + rs);
                pk = addKey(rs);
            }
//            conn.commit();
            object.setPrimaryKey(pk);
            return pk;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to insert objects in database.");
        }finally {
            DBUtil.cleanUp(insertStatement, rs);
        }
    }

    /** Insert the information of each data to the statement.
     * @param object Domain object that need to be in
     * @param statement SQL statement that is used to insert information into database.
     */
    abstract protected void insertData(DomainObject object, PreparedStatement statement) throws SQLException;

    /** Insert the information of primary key to the statement. Default to insert UUID as primary key.
     * @param object Domain object that need to be in
     * @param statement SQL statement that is used to insert information into database.
     */
    protected void insertKey(DomainObject object, PreparedStatement statement) throws SQLException {
        if(object.getPrimaryKey()!= null) {
            UUID id = object.getPrimaryKey().getId();
            statement.setString(1, id.toString());
        }
    }

    /** Update the existing data in the database.
     * @param object Domain object that should be updated.
     * @param conn Connection link to the database.
     * @throws SQLException
     */
    public void update(DomainObject object, Connection conn){
        // Modify the loadMap
        if(loadedMap.get(object.getPrimaryKey()) != null)
            loadedMap.replace(object.getPrimaryKey(), object);

        PreparedStatement updateStatement = null;
        try {
            updateStatement = conn.prepareStatement(updateStatement());
            doUpdate(object, updateStatement);
            updateStatement.execute();

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update object in database.");
        } finally {
            DBUtil.cleanUp(updateStatement);
        }
    }

    /** Implement update method of each mapper.
     * @param object Domain object that should be updated.
     * @param statement SQL statement that is used to update object in the database.
     * @throws SQLException
     */
    abstract protected void doUpdate(DomainObject object, PreparedStatement statement) throws SQLException;

    /** Delete one existing data in the database.
     * @throws SQLException
     */

    public void delete(Key key, Connection conn) {
        System.out.println("mapper .delete");
        // Delete from the loadedmap
        if(loadedMap.get(key) != null)
            loadedMap.remove(key);

        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(deleteStatement());
            doDelete(key, statement);
            System.out.println(statement);
            statement.execute();
            System.out.println("??????????????????");

//            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete object" + key.toString()+ "in database");
        } finally {
            DBUtil.cleanUp(statement);
        }
    }

    /** Implement deletion of the given domain object. Default to delete according to the identical UUID.
     * @param statement SQL statement that is used to delete object in the database.
     * @throws SQLException
     */
    protected void doDelete(Key key, PreparedStatement statement) throws SQLException {
        statement.setString(1, key.getId().toString());
    }

    /** Return the SQL statements of each mapper. */
    abstract protected String findStatement();
    abstract protected String insertStatement();
    abstract protected String updateStatement();
    abstract protected String deleteStatement();

}
