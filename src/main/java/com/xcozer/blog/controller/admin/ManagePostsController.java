package com.xcozer.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.xcozer.blog.constant.Taxonomy;
import com.xcozer.blog.entity.Post;
import com.xcozer.blog.entity.ServerResponse;
import com.xcozer.blog.entity.Term;
import com.xcozer.blog.entity.vo.PostVO;
import com.xcozer.blog.service.PostService;
import com.xcozer.blog.service.TermService;
import com.xcozer.blog.utils.EntityUtil;
import com.xcozer.blog.utils.TermUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/6/20.
 */
@Slf4j
@Controller
@RequestMapping("admin/posts")
public class ManagePostsController {

    private final PostService postService;

    private final TermService termService;

    @Autowired
    public ManagePostsController(PostService postService, TermService termService) {
        this.postService = postService;
        this.termService = termService;
    }

    @GetMapping("")
    public String posts(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit) {
        PageInfo<Post> posts = postService.getPosts(page, limit);
        List<PostVO> postVOS = posts.getList()
                .stream()
                .map(post -> {
                    PostVO postVO = EntityUtil.convert(post, PostVO.class);
                    TermUtil.termsToString(getTermsWithPost(post.getPostId(), Taxonomy.CATEGORY))
                            .ifPresent(postVO::setCategories);

                    return postVO;
                }).collect(Collectors.toList());
        model.addAttribute("posts", new PageInfo<>(postVOS, limit));

        return "admin/article_list";
    }

    @GetMapping("{postId:[\\d]+}")
    public String editPost(@PathVariable int postId, Model model) {
        Post post = postService.findPostById(postId);
        PostVO postVO = EntityUtil.convert(post, PostVO.class);
        List<Term> categories = termService.findAllTerms(Taxonomy.CATEGORY);
        List<Term> postCategories = getTermsWithPost(postId, Taxonomy.CATEGORY);
        TermUtil.termsToString(getTermsWithPost(postId, Taxonomy.TAG))
                .ifPresent(postVO::setTags);

        model.addAttribute("post", postVO);
        model.addAttribute("categories", categories);
        model.addAttribute("postCategories", postCategories);

        return "admin/article_edit";
    }

    @PostMapping(value = {"publish", "modify"})
    @ResponseBody
    public ServerResponse publish(PostVO postVO) {
        try {
            Post post = EntityUtil.convert(postVO, Post.class);
            if (StringUtils.isBlank(postVO.getCategories()))
                return ServerResponse.failed("请添加分类");
            postService.save(post, postVO.getTags(), postVO.getCategories());
        } catch (Exception e) {
            log.error("publish failed: {}", e);
            return ServerResponse.failed(e.getMessage());
        }

        return ServerResponse.ok();
    }

    @GetMapping(value = "publish")
    public String publish(Model model) {
        List<Term> categories = termService.findAllTerms(Taxonomy.CATEGORY);
        model.addAttribute("categories", categories);

        return "admin/article_edit";
    }

    @PostMapping("delete")
    @ResponseBody
    public ServerResponse delete(Integer postId) {
        try {
            postService.deletePost(postId);
        } catch (Exception e) {
            log.error("delete post failed: {}", e);
            return ServerResponse.failed(e.getMessage());
        }

        return ServerResponse.ok();
    }

    private List<Term> getTermsWithPost(int postId, Taxonomy taxonomy) {
        return termService.findAllTermsWithPost(postId, taxonomy);
    }
}
