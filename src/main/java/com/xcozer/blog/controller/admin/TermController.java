package com.xcozer.blog.controller.admin;

import com.xcozer.blog.constant.Taxonomy;
import com.xcozer.blog.entity.ServerResponse;
import com.xcozer.blog.entity.Term;
import com.xcozer.blog.service.TermService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Created on 2017/6/20.
 */
@Slf4j
@Controller
@RequestMapping("admin/term")
public class TermController {

    private final TermService termService;

    @Autowired
    public TermController(TermService termService) {
        this.termService = termService;
    }

    @GetMapping
    public String category(Model model) {
        List<Term> categories = termService.findAllTerms(Taxonomy.CATEGORY);
        List<Term> tags = termService.findAllTerms(Taxonomy.TAG);
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);

        return "admin/category";
    }

    @PostMapping("save")
    @ResponseBody
    public ServerResponse save(@RequestBody Term term) {
        try {
            if (Objects.isNull(term.getTermId())) {
                termService.addTerm(term);
            } else {
                termService.updateTerm(term);
            }
        } catch (RuntimeException e) {
            log.error("save term failed: {}", e);
            return ServerResponse.failed(e.getMessage());
        }

        return ServerResponse.ok();
    }

    @PostMapping("delete")
    @ResponseBody
    public ServerResponse delete(@RequestBody Term term) {
        try {
            termService.delTerm(term.getTermId());
        } catch (RuntimeException e) {
            log.error("delete term failed: {}", e);
            return ServerResponse.failed(e.getMessage());
        }

        return ServerResponse.ok();
    }
}
