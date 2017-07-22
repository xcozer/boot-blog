package com.xcozer.blog.utils;

import org.modelmapper.ModelMapper;

/**
 * Created on 2017/6/21.
 */
public class EntityUtil {

    private static ModelMapper mapper = new ModelMapper();

    public static <T> T convert(Object src, Class<T> dst) {
        return mapper.map(src, dst);
    }
}
