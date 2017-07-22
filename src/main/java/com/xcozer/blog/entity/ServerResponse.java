package com.xcozer.blog.entity;

import lombok.Data;

/**
 * Created on 2017/6/20.
 */
@Data
public class ServerResponse {

    private boolean success;

    private String msg;

    private ServerResponse(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public static ServerResponse ok() {
        return new ServerResponse(true, "成功");
    }

    public static ServerResponse failed(String message) {
        return new ServerResponse(false, message);
    }
}
