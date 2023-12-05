package com.unimelb.swen90007.reactexampleapi.api.application;

import com.unimelb.swen90007.reactexampleapi.api.util.ReadWriteLockManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.IdentityHashMap;

public abstract class BusinessTransactionCommand implements Command{

    public String APP_SESSION = "appSession";
    public String LOCK_REMOVER = "removeLock";
    protected HttpServletRequest req;
    protected HttpServletResponse rsp;

//    protected static HttpSession httpSession;
//
//    public HttpSession getHttpSession() {
//        return httpSession;
//    }
//
//    public void setHttpSession(HttpSession httpSession) {
//        this.httpSession = httpSession;
//    }

    public void init(HttpServletRequest req, HttpServletResponse rsp) {
        this.req = req;
        this.rsp = rsp;
        System.out.println("---------test init req = " + req + "--- rsp = " + rsp);
    }
    protected void startNewBusinessTransaction() {
        HttpSession httpSession = getReq().getSession(true);
        System.out.println("-----start-----" + httpSession.getId());
        ApplicationSession appSession = (ApplicationSession) httpSession.getAttribute(APP_SESSION);
        if (appSession  != null) {
            ReadWriteLockManager.getInstance().releaseAllLock(appSession.getId());
        }
        appSession = new ApplicationSession(
                httpSession.getId(), getReq().getRemoteUser(), new IdentityHashMap()); // what is this???
        AppSessionManager.setSession(appSession);
        httpSession.setAttribute(APP_SESSION, appSession);
        httpSession.setAttribute(LOCK_REMOVER,
                new LockRemover(appSession.getId()));
    }
    protected void continueBusinessTransaction() {
        HttpSession httpSession = getReq().getSession(true);
        System.out.println("-----continue-----" + httpSession.getId());
        ApplicationSession appSession = (ApplicationSession) httpSession.getAttribute(APP_SESSION);
        System.out.println("-----continue- app----" + appSession);
        AppSessionManager.setSession(appSession);
    }
    protected HttpServletRequest getReq() {
        return req;
    }
    protected HttpServletResponse getRsp() {
        return rsp;
    }
}
