package com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions;

public class DuplicateFieldException extends Exception {
    public DuplicateFieldException() { super(); }
    public DuplicateFieldException(String errorMessage) { super(errorMessage); }
}
