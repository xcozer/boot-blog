package com.xcozer.blog.repo;

import com.xcozer.blog.entity.Post;
import com.xcozer.blog.entity.Statistic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created on 2017/6/16.
 */
@Mapper
public interface PostRepository {

    List<Post> findAllPosts(boolean withContent);

    Post findPostById(Integer id);

    Statistic countPosts();

    int save(Post post);

    int delete(int postId);
}
