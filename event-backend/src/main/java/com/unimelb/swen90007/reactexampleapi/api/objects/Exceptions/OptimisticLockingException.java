package com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions;

public class OptimisticLockingException extends RuntimeException {
    public OptimisticLockingException(String message) {
        super(message);
    }
}
