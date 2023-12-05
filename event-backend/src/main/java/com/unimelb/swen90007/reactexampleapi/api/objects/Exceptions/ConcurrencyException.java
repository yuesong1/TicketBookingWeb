package com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions;

public class ConcurrencyException extends Exception {
    public ConcurrencyException() { super(); }
    public ConcurrencyException(String errorMessage) { super(errorMessage); }
}
