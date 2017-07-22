package com.xcozer.blog.service.impl;

import com.xcozer.blog.entity.Statistic;
import com.xcozer.blog.repo.PostRepository;
import com.xcozer.blog.service.AdminService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 2017/6/19.
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final PostRepository postRepository;

    @Autowired
    public AdminServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    @RequiresRoles("admin")
    public Statistic getStatistic() {

        return postRepository.countPosts();
    }
}
