/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.events.article;

import com.example.cmr.events.CmrEvent;
import com.microjainslee.api.annotations.EventType;

import java.time.Instant;

/**
 * Fired by {@code ArticleSbb} once an article is rendered and persisted.
 * A downstream event: {@code NotificationSbb} reacts (notify subscribers,
 * warm caches, bump metrics). Carries only a preview to stay light on the
 * ring buffer.
 */
@EventType(name = "ArticlePublished", vendor = "cmr", version = "1.0")
public record ArticlePublishedEvent(String articleId, String slug, String title,
                                    String htmlPreview, String initiator, Instant publishedAt)
        implements CmrEvent {
}
