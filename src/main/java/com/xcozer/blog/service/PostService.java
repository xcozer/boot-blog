package com.xcozer.blog.service;

import com.github.pagehelper.PageInfo;
import com.xcozer.blog.entity.Post;

/**
 * Created on 2017/6/15.
 */
public interface PostService {

    PageInfo<Post> getPosts(Integer page, Integer num);

    Post findPostById(Integer id);

    void save(Post post, String tags, String categories);

    void deletePost(int postId);

    int getHits(int postId);

    int incAndGetHits(int postId);
}
