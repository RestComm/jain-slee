/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.ports;

import com.example.cmr.model.Article;

import java.util.List;
import java.util.Optional;

/**
 * Persistence port for articles. SBBs write through it on the event path;
 * the public/admin routers read through it directly (reads bypass the event
 * pipeline — a CMS is write-through-events, read-direct).
 */
public interface ArticleRepository {

    /** Insert or replace by id. */
    Article save(Article article);

    Optional<Article> findById(String id);

    Optional<Article> findBySlug(String slug);

    /** Remove by id; returns true if something was removed. */
    boolean delete(String id);

    /** All articles, newest first. */
    List<Article> findAll();

    /** Published articles in a category, newest first. */
    List<Article> findPublishedByCategory(String categorySlug);

    /** All published articles, newest first. */
    List<Article> findPublished();

    /** Total count (for dashboard metrics). */
    long count();
}
