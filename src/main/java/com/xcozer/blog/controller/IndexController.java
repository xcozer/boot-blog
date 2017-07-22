package com.xcozer.blog.controller;

import com.github.pagehelper.PageInfo;
import com.xcozer.blog.constant.Taxonomy;
import com.xcozer.blog.entity.Post;
import com.xcozer.blog.entity.Term;
import com.xcozer.blog.entity.vo.PostVO;
import com.xcozer.blog.service.PostService;
import com.xcozer.blog.service.TermService;
import com.xcozer.blog.utils.EntityUtil;
import com.xcozer.blog.utils.TermUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/6/15.
 */
@Controller
public class IndexController {

    private final PostService postService;

    private final TermService termService;

    @Autowired
    public IndexController(PostService postService, TermService termService) {
        this.postService = postService;
        this.termService = termService;
    }

    @GetMapping("")
    public String index(Model model) {
        PageInfo<Post> posts = postService.getPosts(1, 15);
        List<PostVO> postVOS = posts.getList()
                .stream()
                .map(post -> EntityUtil.convert(post, PostVO.class))
                .collect(Collectors.toList());
        postVOS.forEach(postVO -> {
            List<Term> categories = termService.findAllTermsWithPost(postVO.getPostId(), Taxonomy.CATEGORY);
            List<Term> tags = termService.findAllTermsWithPost(postVO.getPostId(), Taxonomy.TAG);
            TermUtil.termsToString(tags).ifPresent(postVO::setTags);

            try {
                postVO.setCategories(TermUtil.renderTerms(categories));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        model.addAttribute("posts", new PageInfo<>(postVOS, 15));

        return "themes/default/index";
    }
}
