package com.xcozer.blog.utils;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collections;
import java.util.List;

/**
 * Created on 2017/6/18.
 */
public final class Markdown {

    public static String render(String rawContent) {
        List<Extension> extensions = Collections.singletonList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        Node node = parser.parse(rawContent);
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();

        return renderer.render(node);
    }
}
