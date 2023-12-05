package com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions;

public class InvalidUpdateException extends Exception{
    public InvalidUpdateException() { super(); }
    public InvalidUpdateException(String errorMessage) { super(errorMessage); }
}
