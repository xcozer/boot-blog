package com.xcozer.blog.service;

import com.xcozer.blog.constant.Taxonomy;
import com.xcozer.blog.entity.Term;

import java.util.Collection;
import java.util.List;

/**
 * Created on 2017/6/20.
 *
 * 分类、tag等相关的服务
 */
public interface TermService {

    List<Term> findAllTerms(Taxonomy taxonomy);

    List<Term> findAllTermsWithPost(int postId, Taxonomy taxonomy);

    void addTerm(Term term);

    void addTerms(Collection<Term> terms);

    void addTermRelationship(int postId, int termTaxonomyId);

    void updateTerm(Term term);

    void delTerm(int termId);
}
