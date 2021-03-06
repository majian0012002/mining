package com.miller.mining.exception;

public class VerifyException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7782614345892193342L;

	public VerifyException() {
    }

    public VerifyException(String message) {
        super(message);
    }

    public VerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerifyException(Throwable cause) {
        super(cause);
    }

    public VerifyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
