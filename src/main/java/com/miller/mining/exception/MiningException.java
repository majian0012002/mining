package com.miller.mining.exception;

public class MiningException extends RuntimeException {

    public MiningException() {
    }

    public MiningException(String message) {
        super(message);
    }

    public MiningException(String message, Throwable cause) {
        super(message, cause);
    }

    public MiningException(Throwable cause) {
        super(cause);
    }

    public MiningException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
