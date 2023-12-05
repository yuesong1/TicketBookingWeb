//package com.unimelb.swen90007.reactexampleapi.api.application;
//
//import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
//import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.sql.SQLException;
//
//public class TransactionCommand implements Command{
//    Command cmd;
//
//    public TransactionCommand(Command cmd) {
//        this.cmd = cmd;
//    }
//
//    @Override
//    public void init(HttpServletRequest req, HttpServletResponse rsp) {
//    }
//
//    public void process() throws Exception {
//        UnitOfWork.newCurrent();
//        try {
//            cmd.process();
//            UnitOfWork.getCurrent().commit();
//        } catch (SQLException e) {
//            DBUtil.getConnection().rollback();
//            throw e;
//        }finally {
//            DBUtil.closeConnection();
//        }
//    }
//}
