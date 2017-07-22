package com.xcozer.blog.constant;

import lombok.Getter;

/**
 * Created on 2017/6/20.
 */
@Getter
public enum PostStatus {

    PUBLISH("publish"),
    DRAFT("draft");

    private String desc;

    PostStatus(String desc) {
        this.desc = desc;
    }
}
