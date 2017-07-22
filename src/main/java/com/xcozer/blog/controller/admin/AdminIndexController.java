package com.xcozer.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.xcozer.blog.entity.Post;
import com.xcozer.blog.service.AdminService;
import com.xcozer.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created on 2017/6/19.
 */
@Controller
@RequestMapping("admin")
public class AdminIndexController {

    private final AdminService adminService;

    private final PostService postService;

    @Autowired
    public AdminIndexController(AdminService adminService, PostService postService) {
        this.adminService = adminService;
        this.postService = postService;
    }

    @RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login() {
        return "admin/login";
    }

    @RequestMapping("unauthorized")
    public String unauthorized() {
        return "comm/error_403";
    }

    @GetMapping({"", "index"})
    public String index(Model model) {
        PageInfo<Post> posts = postService.getPosts(1, 5);
        model.addAttribute("statistic", adminService.getStatistic());
        model.addAttribute("posts", posts.getList());

        return "admin/index";
    }

    @GetMapping("setting")
    public String setting() {

        return "admin/setting";
    }
}
