package com.xcozer.blog.utils;

import com.xcozer.blog.entity.Term;
import org.apache.commons.collections4.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

/**
 * Created on 2017/6/23.
 */
public class TermUtil {

    private static final Random random = new SecureRandom();

    public static String renderTerms(Collection<Term> terms) throws UnsupportedEncodingException {
        if (CollectionUtils.isNotEmpty(terms)) {
            StringBuilder sb = new StringBuilder();
            for (Term term : terms) {
                sb.append("<a href=\"/")
                        .append(term.getTaxonomy()).append("/")
                        .append(URLEncoder.encode(term.getName(), "UTF-8"))
                        .append("\">")
                        .append(term.getName())
                        .append("</a>");
            }
            return sb.toString();
        }

        return "";
    }

    public static Optional<String> termsToString(Collection<Term> terms) {
        return terms.stream()
                .map(Term::getName)
                .reduce((s, s2) -> s + "," + s2);
    }

    private static final String[] COLORS = {"default", "primary", "success", "info", "warning", "danger", "inverse", "purple", "pink"};

    public static String rand_color() {
        int max = COLORS.length - 1, min = 0;
        int r = random.nextInt(max) % (max - min + 1) + min;

        return COLORS[r];
    }
}
