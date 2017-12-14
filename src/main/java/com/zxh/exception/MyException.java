package com.zxh.exception;

/**
 * Created by admin on 2017/12/14.
 */
public class MyException extends RuntimeException {

    private static final long serialVersionUID = 1l;

    public MyException(String message) {
        super(message);
    }

}
