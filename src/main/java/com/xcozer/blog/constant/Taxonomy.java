package com.xcozer.blog.constant;

import lombok.Getter;

/**
 * Created on 2017/6/21.
 */
@Getter
public enum Taxonomy {

    TAG("tag"), CATEGORY("category");

    private String name;

    Taxonomy(String name) {
        this.name = name;
    }
}
