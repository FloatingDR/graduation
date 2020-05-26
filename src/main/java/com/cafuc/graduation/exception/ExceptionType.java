package com.cafuc.graduation.exception;

/**
 * <p>
 * 异常类型枚举类
 * </p>
 *
 * @author taylor
 * @date 2020/2/18 14:02
 */
public enum ExceptionType {

    /**
     * 业务流程中的异常
     */
    BIZ_EXCEPTION(0, "业务异常"),

    /**
     * 数据库相关的异常
     */
    DB_EXCEPTION(1, "数据库异常"),

    /**
     * redis相关的异常
     */
    REDIS_EXCEPTION(2, "Redis异常"),

    /**
     * netty相关的异常
     */
    NETTY_EXCEPTION(3, "Netty异常"),

    /**
     * 系统抛出的不可预知的异常
     */
    SYSTEM_EXCEPTION(4, "系统异常");

    int type;

    String describe;

    ExceptionType(int type, String describe) {
        this.type = type;
        this.describe = describe;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
