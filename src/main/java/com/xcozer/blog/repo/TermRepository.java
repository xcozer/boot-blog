package com.xcozer.blog.repo;

import com.xcozer.blog.entity.Term;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created on 2017/6/21.
 */
@Mapper
public interface TermRepository {

    List<Term> findAllTerms(String taxonomy);

    Term findTermById(int termId);

    int addTerm(Term term);

    int updateTerm(Term term);

    int addTermForTaxonomy(Term term);

    int addTermRelationship(@Param("postId") int postId, @Param("taxonomyId") int termTaxonomyId);

    int delTermRelationShip(int postId);

    int delTerm(@Param("termId") int termId, @Param("cascade") boolean cascade);

    List<Term> findAllTermsWithPost(@Param("postId") int postId, @Param("taxonomy") String taxonomy);

    Integer findTermByName(String name);

    Integer findTaxonomyByTermName(String name);

    int updateTermCount(@Param("taxonomyId") Integer taxonomyId, @Param("num") int num);

    int reduceTermCount(int postId);
}
