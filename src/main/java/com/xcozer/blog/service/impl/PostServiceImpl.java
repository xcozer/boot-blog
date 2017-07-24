package com.xcozer.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xcozer.blog.constant.Taxonomy;
import com.xcozer.blog.entity.Post;
import com.xcozer.blog.entity.Term;
import com.xcozer.blog.repo.PostRepository;
import com.xcozer.blog.repo.TermRepository;
import com.xcozer.blog.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created on 2017/6/16.
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "posts")
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final TermRepository termRepository;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, TermRepository termRepository, StringRedisTemplate redisTemplate) {
        this.postRepository = postRepository;
        this.termRepository = termRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public PageInfo<Post> getPosts(Integer page, Integer num) {
        PageHelper.startPage(page, num);
        List<Post> posts = postRepository.findAllPosts(false);

        return new PageInfo<>(posts, num);
    }

    @Override
    @Cacheable(key = "'post-' + #id")
    public Post findPostById(Integer id) {
        Post post = postRepository.findPostById(id);
        String hits = redisTemplate.opsForValue().get("post-" + id + "-hits");
        if (Objects.nonNull(hits)) {
            post.setHits(Integer.parseInt(hits));
        } else {
            redisTemplate.opsForValue().set("post-" + id + "-hits", post.getHits().toString());
        }

        return post;
    }

    @Override
    @Transactional
    @CacheEvict(key = "'post-' + #post.postId")
    public void save(Post post, String tags, String categories) {
        Integer temp = post.getPostId();
        int count = postRepository.save(post);
        final int postId = count > 1 ? temp : post.getPostId();

        termRepository.reduceTermCount(postId);
        termRepository.delTermRelationShip(postId);

        if (StringUtils.isNotBlank(tags))
            getSplitTerms(tags, Taxonomy.TAG).forEach(term -> {
                if (termRepository.addTerm(term) > 0) {
                    termRepository.addTermForTaxonomy(term);
                } else {
                    term.setTaxonomyId(termRepository.findTaxonomyByTermName(term.getName()));
                }

                if (term.getTaxonomyId() > 0)
                    if (termRepository.addTermRelationship(postId, term.getTaxonomyId()) > 0)
                        termRepository.updateTermCount(term.getTaxonomyId(), 1);
            });

        getSplitTerms(categories, Taxonomy.CATEGORY).forEach(term -> {
            Integer taxonomyId = termRepository.findTaxonomyByTermName(term.getName());
            if (taxonomyId > 0)
                if (termRepository.addTermRelationship(postId, taxonomyId) > 0)
                    termRepository.updateTermCount(taxonomyId, 1);
        });
    }

    @Override
    @Transactional
    @CacheEvict(key = "'post-' + #postId")
    public void deletePost(int postId) {
        termRepository.reduceTermCount(postId);
        postRepository.delete(postId);
        redisTemplate.delete("post-" + postId + "-hits");
    }

    @Override
    public int getHits(int postId) {
        return Integer.parseInt(redisTemplate.opsForValue().get("post-" + postId + "-hits"));
    }

    @Override
    public int incAndGetHits(int postId) {
        return Math.toIntExact(redisTemplate.opsForValue().increment("post-" + postId + "-hits", 1));
    }

    private List<Term> getSplitTerms(String termString, Taxonomy taxonomy) {
        return Arrays.stream(termString.split(","))
                .map(s -> Term.withNameAndTaxonomy(s, taxonomy))
                .collect(Collectors.toList());
    }
}
