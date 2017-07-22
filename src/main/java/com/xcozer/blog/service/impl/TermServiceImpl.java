package com.xcozer.blog.service.impl;

import com.xcozer.blog.constant.Taxonomy;
import com.xcozer.blog.entity.Term;
import com.xcozer.blog.repo.TermRepository;
import com.xcozer.blog.service.TermService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Created on 2017/6/21.
 */
@Service
public class TermServiceImpl implements TermService {

    private final TermRepository termRepository;

    @Autowired
    public TermServiceImpl(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    @Override
    public List<Term> findAllTerms(Taxonomy taxonomy) {
        return termRepository.findAllTerms(taxonomy.getName());
    }

    @Override
    public List<Term> findAllTermsWithPost(int postId, Taxonomy taxonomy) {
        return termRepository.findAllTermsWithPost(postId, taxonomy.getName());
    }

    @Override
    public void addTerm(Term term) {
        int count = termRepository.addTerm(term);
        if (count > 0) {
            termRepository.addTermForTaxonomy(term);
        }
    }

    @Override
    public void addTerms(Collection<Term> terms) {
        terms.forEach(this::addTerm);
    }

    @Override
    public void addTermRelationship(int postId, int taxonomyId) {
        termRepository.addTermRelationship(postId, taxonomyId);
    }

    @Override
    public void updateTerm(Term term) {
        termRepository.updateTerm(term);
    }

    @Override
    public void delTerm(int termId) {
        Term term = termRepository.findTermById(termId);
        if (StringUtils.equals(Taxonomy.CATEGORY.getName(), term.getTaxonomy())) {
            if (term.getCount() > 0)
                throw new RuntimeException("该分类下有相关文章，不可删除");

            termRepository.delTerm(termId, false);
        }

        termRepository.delTerm(termId, term.getCount() > 0);
    }
}
