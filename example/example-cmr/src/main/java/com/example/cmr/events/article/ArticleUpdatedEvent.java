/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.events.article;

import com.example.cmr.events.CmrEvent;
import com.example.cmr.model.Article;
import com.microjainslee.api.annotations.EventType;

import java.time.Instant;

/**
 * Fired when an admin edits an existing article's content or metadata.
 * {@code ArticleSbb} re-renders the Markdown and persists.
 */
@EventType(name = "ArticleUpdated", vendor = "cmr", version = "1.0")
public record ArticleUpdatedEvent(String articleId, Article updated,
                                  String initiator, Instant firedAt)
        implements CmrEvent {

    public ArticleUpdatedEvent(String articleId, Article updated, String initiator) {
        this(articleId, updated, initiator, Instant.now());
    }
}
