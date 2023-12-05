package com.unimelb.swen90007.reactexampleapi.api.application;

import com.unimelb.swen90007.reactexampleapi.api.objects.DomainObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {
    public void init(HttpServletRequest req, HttpServletResponse rsp);
    public void process() throws Exception;
}
