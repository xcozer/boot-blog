package com.xcozer.blog.entity;

import com.xcozer.blog.constant.PostStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created on 2017/6/15.
 */
@Data
public class Post implements Serializable {

    private static final long serialVersionUID = -1733598075605530342L;

    private Integer postId;

    private String title;

    private String slug;

    private String content;

    private Boolean allowComment;

    private Integer commentCount;

    private Integer hits;

    private PostStatus status = PostStatus.PUBLISH;

    private Date created;

    private Date modified;
}
