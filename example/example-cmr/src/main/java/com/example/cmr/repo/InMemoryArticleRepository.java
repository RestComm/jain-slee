/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.repo;

import com.example.cmr.model.Article;
import com.example.cmr.model.ArticleStatus;
import com.example.cmr.ports.ArticleRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe in-memory {@link ArticleRepository}. A real deployment would
 * swap this for Panache/JDBC — the SBBs and routers only see the interface.
 */
public final class InMemoryArticleRepository implements ArticleRepository {

    private final ConcurrentHashMap<String, Article> byId = new ConcurrentHashMap<>();

    private static final Comparator<Article> NEWEST_FIRST =
            Comparator.comparing(Article::createdAt).reversed();

    @Override
    public Article save(Article article) {
        byId.put(article.id(), article);
        return article;
    }

    @Override
    public Optional<Article> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public Optional<Article> findBySlug(String slug) {
        return byId.values().stream()
                .filter(a -> a.slug().equals(slug))
                .findFirst();
    }

    @Override
    public boolean delete(String id) {
        return byId.remove(id) != null;
    }

    @Override
    public List<Article> findAll() {
        return byId.values().stream().sorted(NEWEST_FIRST).collect(Collectors.toList());
    }

    @Override
    public List<Article> findPublishedByCategory(String categorySlug) {
        return byId.values().stream()
                .filter(Article::isPublic)
                .filter(a -> categorySlug.equals(a.categorySlug()))
                .sorted(NEWEST_FIRST)
                .collect(Collectors.toList());
    }

    @Override
    public List<Article> findPublished() {
        return byId.values().stream()
                .filter(a -> a.status() == ArticleStatus.PUBLISHED)
                .sorted(NEWEST_FIRST)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return byId.size();
    }
}
