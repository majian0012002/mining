package com.miller.mining.exception;

public class TransforVoFaildedException extends Exception {

    public TransforVoFaildedException() {
    }

    public TransforVoFaildedException(String message) {
        super(message);
    }

    public TransforVoFaildedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransforVoFaildedException(Throwable cause) {
        super(cause);
    }

    public TransforVoFaildedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
