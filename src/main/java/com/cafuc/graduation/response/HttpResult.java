package com.cafuc.graduation.response;

import com.cafuc.graduation.exception.ExceptionType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * http 统一消息返回实体
 * </p>
 *
 * @author taylor
 * @date 2020/2/18 13:51
 */
@Data
public class HttpResult<T>  implements Serializable {

    private static final long serialVersionUID = -5240707943138473551L;

    private boolean status;

    private String message;

    private Integer errorType;

    private T data;

    public HttpResult() {

    }

    public HttpResult(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public HttpResult(boolean status, String message, Integer errorType, T data) {
        this.status = status;
        this.message = message;
        this.errorType = errorType;
        this.data = data;
    }

    public static <T> HttpResult<T> success() {
        return new HttpResult<>(true, null, null);
    }

    public static <T> HttpResult<T> success(T data) {
        return new HttpResult<>(true, null, data);
    }

    public static <T> HttpResult<T> success(T data, String message) {
        return new HttpResult<>(true, message, data);
    }

    public static <T> HttpResult<T> success(T data, String message, Object... args) {
        return new HttpResult<>(true, String.format(message, args), data);
    }

    public static <T> HttpResult<T> error(String message) {
        return new HttpResult<>(false, message, ExceptionType.BIZ_EXCEPTION.getType(), null);
    }

    public static <T> HttpResult<T> error(String message, Object... args) {
        return new HttpResult<>(false, String.format(message, args), ExceptionType.BIZ_EXCEPTION.getType(), null);
    }

    public static <T> HttpResult<T> error(int errorType, String message) {
        return new HttpResult<>(false, message, errorType, null);
    }

    public static <T> HttpResult<T> error(int errorType, String message, Object... args) {
        return new HttpResult<>(false, String.format(message, args), errorType, null);
    }

}
