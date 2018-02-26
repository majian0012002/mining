package com.miller.mining.exception;

public class MiningException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -9166857537998495810L;

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
