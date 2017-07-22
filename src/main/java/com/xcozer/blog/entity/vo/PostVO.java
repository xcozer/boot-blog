package com.xcozer.blog.entity.vo;

import com.xcozer.blog.constant.PostStatus;
import lombok.Data;

import java.util.Date;

/**
 * Created on 2017/6/21.
 */
@Data
public class PostVO {

    private Integer postId;

    private String title;

    private String slug;

    private String content;

    private Integer hits;

    private Boolean allowComment;

    private Integer commentCount;

    private String tags;

    private String categories;

    private PostStatus status = PostStatus.PUBLISH;

    private Date created;
}
