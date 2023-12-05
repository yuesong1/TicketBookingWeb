package com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions;

public class DoesNotExistException extends Exception {
    public DoesNotExistException() { super(); }
    public DoesNotExistException(String errorMessage) { super(errorMessage); }
}
