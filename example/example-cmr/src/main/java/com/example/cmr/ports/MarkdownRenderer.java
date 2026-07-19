/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.ports;

/** Renders Markdown source to sanitized HTML. Implemented in {@code render/}. */
public interface MarkdownRenderer {

    /**
     * @param markdown Markdown source (front-matter already stripped)
     * @return rendered HTML fragment
     */
    String render(String markdown);
}
