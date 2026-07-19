/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.render;

import com.example.cmr.ports.MarkdownRenderer;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

/**
 * {@link MarkdownRenderer} backed by flexmark. Thread-safe: the {@code Parser}
 * and {@code HtmlRenderer} are immutable after {@code build()} and shared.
 */
public final class FlexmarkRenderer implements MarkdownRenderer {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public FlexmarkRenderer() {
        MutableDataSet options = new MutableDataSet();
        this.parser = Parser.builder(options).build();
        this.renderer = HtmlRenderer.builder(options).build();
    }

    @Override
    public String render(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }
        return renderer.render(parser.parse(markdown));
    }
}
