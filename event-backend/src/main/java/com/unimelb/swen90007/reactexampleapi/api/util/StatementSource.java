package com.unimelb.swen90007.reactexampleapi.api.util;

public interface StatementSource {
    String sqlQuery();
    Object[] parameters();
}
