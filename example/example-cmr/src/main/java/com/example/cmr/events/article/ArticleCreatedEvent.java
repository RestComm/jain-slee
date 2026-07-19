/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.events.article;

import com.example.cmr.events.CmrEvent;
import com.example.cmr.model.Article;
import com.microjainslee.api.annotations.EventType;

import java.time.Instant;

/**
 * Fired when an admin creates a new article (raw Markdown, not yet rendered).
 * Handled by {@code ArticleSbb}: slug generation, Markdown render, persist,
 * then fires {@link ArticlePublishedEvent} downstream.
 */
@EventType(name = "ArticleCreated", vendor = "cmr", version = "1.0")
public record ArticleCreatedEvent(Article article, String initiator, Instant firedAt)
        implements CmrEvent {

    public ArticleCreatedEvent(Article article, String initiator) {
        this(article, initiator, Instant.now());
    }
}
