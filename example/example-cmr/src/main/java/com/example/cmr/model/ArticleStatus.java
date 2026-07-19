/*
 * micro-jainslee example :: CMR
 * Content Management Runtime on the micro-jainslee event engine.
 */
package com.example.cmr.model;

/**
 * Lifecycle state of an {@link Article}. A CMR article walks
 * {@code DRAFT → (SCHEDULED) → PUBLISHED → ARCHIVED}.
 */
public enum ArticleStatus {
    /** Editable, not visible on the public site. */
    DRAFT,
    /** Approved but held until {@link Article#publishAt()}. */
    SCHEDULED,
    /** Rendered and live on the public site. */
    PUBLISHED,
    /** Retired — kept for history, hidden from listings. */
    ARCHIVED
}
