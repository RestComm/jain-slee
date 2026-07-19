/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.model;

import java.time.Instant;
import java.util.List;

/**
 * An article — the central content aggregate. Immutable; the SBB pipeline
 * derives a new copy at each stage via the {@code with*} helpers (slug at
 * create, rendered HTML at publish).
 *
 * @param id           stable identifier (UUID)
 * @param slug         URL-safe title, unique; assigned by {@code ArticleSbb}
 * @param title        display title
 * @param categorySlug owning {@link Category}
 * @param tags         free-form tags
 * @param rawMarkdown  author-supplied Markdown source
 * @param renderedHtml Markdown rendered to HTML; empty until published
 * @param coverImageId optional {@link MediaFile} id for the cover image
 * @param authorId     admin username who created it
 * @param status       lifecycle state
 * @param createdAt    creation timestamp
 * @param updatedAt    last-modified timestamp
 * @param publishAt    scheduled publish time (== createdAt for immediate)
 */
public record Article(
        String id,
        String slug,
        String title,
        String categorySlug,
        List<String> tags,
        String rawMarkdown,
        String renderedHtml,
        String coverImageId,
        String authorId,
        ArticleStatus status,
        Instant createdAt,
        Instant updatedAt,
        Instant publishAt
) {

    public Article {
        tags = tags == null ? List.of() : List.copyOf(tags);
    }

    /** A copy with a freshly assigned slug. */
    public Article withSlug(String newSlug) {
        return new Article(id, newSlug, title, categorySlug, tags, rawMarkdown,
                renderedHtml, coverImageId, authorId, status, createdAt, updatedAt, publishAt);
    }

    /** A copy carrying rendered HTML and a new status/updatedAt. */
    public Article withRendered(String html, ArticleStatus newStatus, Instant when) {
        return new Article(id, slug, title, categorySlug, tags, rawMarkdown,
                html, coverImageId, authorId, newStatus, createdAt, when, publishAt);
    }

    /** A copy with a different status and updatedAt. */
    public Article withStatus(ArticleStatus newStatus, Instant when) {
        return new Article(id, slug, title, categorySlug, tags, rawMarkdown,
                renderedHtml, coverImageId, authorId, newStatus, createdAt, when, publishAt);
    }

    /** True when the article should be shown to the public. */
    public boolean isPublic() {
        return status == ArticleStatus.PUBLISHED;
    }
}
