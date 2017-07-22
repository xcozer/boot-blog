package com.xcozer.blog.controller;

import com.xcozer.blog.constant.Taxonomy;
import com.xcozer.blog.entity.Post;
import com.xcozer.blog.entity.Term;
import com.xcozer.blog.service.PostService;
import com.xcozer.blog.service.TermService;
import com.xcozer.blog.utils.Markdown;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.xcozer.blog.utils.TermUtil.renderTerms;

/**
 * Created on 2017/6/18.
 */
@Slf4j
@Controller
@RequestMapping("post")
public class PostController {

    private final PostService postService;

    private final TermService termService;

    @Autowired
    public PostController(PostService postService, TermService termService) {
        this.postService = postService;
        this.termService = termService;
    }

    @GetMapping("{id:\\d+}")
    public String getPost(Model model, @PathVariable Integer id) throws UnsupportedEncodingException {
        Post post = postService.findPostById(id);
        post.setHits(postService.incAndGetHits(id));
        List<Term> categories = termService.findAllTermsWithPost(post.getPostId(), Taxonomy.CATEGORY);
        List<Term> tags = termService.findAllTermsWithPost(post.getPostId(), Taxonomy.TAG);
        post.setContent(Markdown.render(post.getContent()));

        model.addAttribute("post", post);
        model.addAttribute("is_post", true);
        model.addAttribute("renderedTags", renderTerms(tags));
        model.addAttribute("renderedCategories", renderTerms(categories));

        return "themes/default/post";
    }
}
