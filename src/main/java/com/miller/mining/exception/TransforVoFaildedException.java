package com.miller.mining.exception;

public class TransforVoFaildedException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8541051331499050334L;

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
