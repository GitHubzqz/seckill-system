package com.java.cn.coding.exception;

public class RepeatKillException extends SeckillException{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RepeatKillException(String message) {
	        super(message);
	    }

	    public RepeatKillException(String message, Throwable cause) {
	        super(message, cause);
	    }
}
