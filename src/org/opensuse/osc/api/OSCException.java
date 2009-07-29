package org.opensuse.osc.api;

public class OSCException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OSCException(Exception cause) {
		super(cause);
	}

	public OSCException(String message) {
		super(message);
	}

}
