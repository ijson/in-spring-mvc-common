package com.ijson.platform.api.model;

import lombok.Data;

/**
 * description:  内容传输对象,给插件提供
 *
 * @author cuiyongxu 创建时间：2016年1月4日
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private T result;


    public Result(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }


    public Result() {

    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result ok(String message) {
        return new Result(0, message);
    }


    public static Result ok() {
        return new Result(0, "");
    }

    public static Result okObject(Object object) {
        return new Result(0, "", object);
    }


    public static Result ok(Object result) {
        return new Result(0, "", result);
    }


    public static Result okObject(Object result, String message) {
        return new Result(0, message, result);
    }


    public static Result error(String message) {
        return new Result(-1, message);
    }


    public static Result error(int code, String message) {
        return new Result(code, message);
    }


}
