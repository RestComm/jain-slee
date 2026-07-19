/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.events.article;

import com.example.cmr.events.CmrEvent;
import com.microjainslee.api.annotations.EventType;

import java.time.Instant;

/**
 * Fired when an admin deletes an article. {@code ArticleSbb} removes it and
 * releases its per-article activity context.
 */
@EventType(name = "ArticleDeleted", vendor = "cmr", version = "1.0")
public record ArticleDeletedEvent(String articleId, String slug,
                                  String initiator, Instant firedAt)
        implements CmrEvent {

    public ArticleDeletedEvent(String articleId, String slug, String initiator) {
        this(articleId, slug, initiator, Instant.now());
    }
}
