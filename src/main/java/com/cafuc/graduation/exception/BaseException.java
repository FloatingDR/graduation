package com.cafuc.graduation.exception;

/**
 * <p>
 * 系统异常基类
 * </p>
 *
 * @author taylor
 * @date 2020/3/4 15:50
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -3246985731826768145L;

    protected int type = ExceptionType.BIZ_EXCEPTION.getType();

    public BaseException() {
        super();
    }

    public BaseException(int exceptionType, String message) {
        super(message);
        type = exceptionType;
    }

    public BaseException(ExceptionType exceptionType, String message) {
        this(exceptionType.getType(), message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(Throwable cause, String message) {
        super(message, cause);
    }

    public BaseException(Throwable cause, String message, Object... param) {
        super(String.format(message, param), cause);
    }

    public BaseException(String message, Object... param) {
        super(String.format(message, param));
    }

}
