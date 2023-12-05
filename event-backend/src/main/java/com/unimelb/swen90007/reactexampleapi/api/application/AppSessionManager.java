package com.unimelb.swen90007.reactexampleapi.api.application;

public class AppSessionManager {
    private static ThreadLocal current = new ThreadLocal();

    public static ApplicationSession getSession() {
        return (ApplicationSession) current.get();
    }
    public static void setSession(ApplicationSession session) {
        current.set(session);
    }
}
