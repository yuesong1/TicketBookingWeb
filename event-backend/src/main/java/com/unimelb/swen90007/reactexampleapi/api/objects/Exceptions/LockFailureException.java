package com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions;

public class LockFailureException extends Exception{
    public LockFailureException() { super(); }
    public LockFailureException(String errorMessage) { super(errorMessage); }
}
