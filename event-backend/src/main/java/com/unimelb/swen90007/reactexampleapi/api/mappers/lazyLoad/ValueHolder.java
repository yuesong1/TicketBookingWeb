package com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad;

import com.unimelb.swen90007.reactexampleapi.api.objects.Section;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class ValueHolder {
    private Object value;
    private ValueLoader loader;
    public ValueHolder(ValueLoader loader) {
        this.loader = loader;
    }
    public Object getValue() throws SQLException {
        Connection conn = DBUtil.connection();
        if (value == null) {
            value = loader.load(conn);
        }
        conn.close();
        return value;
    }

}
