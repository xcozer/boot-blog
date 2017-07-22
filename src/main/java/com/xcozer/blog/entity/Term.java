package com.xcozer.blog.entity;

import com.xcozer.blog.constant.Taxonomy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created on 2017/6/20.
 */
@Getter @Setter
@EqualsAndHashCode(doNotUseGetters = true, exclude = {"slug", "count"})
public class Term implements Serializable {

    private static final long serialVersionUID = 3570768574898728180L;

    private Integer termId;

    private Integer taxonomyId;

    private String name;

    private String slug;

    private String taxonomy;

    private Integer count;

    public static Term withNameAndTaxonomy(String name, Taxonomy taxonomy) {
        Term term = new Term();
        term.setName(name);
        term.setTaxonomy(taxonomy.getName());

        return term;
    }
}
