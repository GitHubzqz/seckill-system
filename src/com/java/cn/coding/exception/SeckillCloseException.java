package com.java.cn.coding.exception;

public class SeckillCloseException extends SeckillException{
	
	public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }

}
