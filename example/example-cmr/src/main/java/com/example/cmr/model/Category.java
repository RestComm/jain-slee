/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.model;

/**
 * A public-site tab. The home page renders one tab per category
 * (ordered by {@link #order()}).
 *
 * @param slug  URL segment, e.g. {@code "technology"}
 * @param name  display label, e.g. {@code "Công nghệ"}
 * @param order tab position, ascending
 */
public record Category(String slug, String name, int order) {
}
