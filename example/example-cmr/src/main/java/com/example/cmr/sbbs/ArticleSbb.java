/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.sbbs;

import com.example.cmr.events.article.ArticleCreatedEvent;
import com.example.cmr.events.article.ArticleDeletedEvent;
import com.example.cmr.events.article.ArticlePublishedEvent;
import com.example.cmr.events.article.ArticleUpdatedEvent;
import com.example.cmr.model.Article;
import com.example.cmr.model.ArticleStatus;
import com.example.cmr.ports.ArticleRepository;
import com.example.cmr.ports.MarkdownRenderer;
import com.example.cmr.render.SlugUtils;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.SbbAnnotation;
import com.microjainslee.core.InMemoryActivityContext;
import com.microjainslee.core.MicroSleeContainer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

/**
 * The article lifecycle handler — the CMR equivalent of a telecom service SBB.
 *
 * <ul>
 *   <li>{@code ArticleCreated}: assign slug, render Markdown → HTML, persist,
 *       then fire {@link ArticlePublishedEvent} downstream when published.</li>
 *   <li>{@code ArticleUpdated}: re-render and persist.</li>
 *   <li>{@code ArticleDeleted}: remove and release the activity context.</li>
 * </ul>
 */
@SbbAnnotation(name = "ArticleSbb", vendor = "cmr", version = "1.0")
public final class ArticleSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(ArticleSbb.class);

    private final ArticleRepository repo;
    private final MarkdownRenderer renderer;
    private final MicroSleeContainer container;

    public ArticleSbb(ArticleRepository repo, MarkdownRenderer renderer,
                      MicroSleeContainer container) {
        this.repo = repo;
        this.renderer = renderer;
        this.container = container;
    }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof ArticleCreatedEvent e) {
            onCreated(e);
        } else if (event instanceof ArticleUpdatedEvent e) {
            onUpdated(e);
        } else if (event instanceof ArticleDeletedEvent e) {
            onDeleted(e);
        }
    }

    private void onCreated(ArticleCreatedEvent e) {
        Article incoming = e.article();
        String slug = incoming.slug() == null || incoming.slug().isBlank()
                ? SlugUtils.generate(incoming.title()) : incoming.slug();
        String html = renderer.render(incoming.rawMarkdown());
        Article stored = incoming.withSlug(uniqueSlug(slug, incoming.id()))
                .withRendered(html, incoming.status(), Instant.now());
        repo.save(stored);
        LOG.info("[article] created id={} slug={} status={}",
                stored.id(), stored.slug(), stored.status());
        if (stored.status() == ArticleStatus.PUBLISHED) {
            firePublished(stored, e.initiator());
        }
    }

    private void onUpdated(ArticleUpdatedEvent e) {
        Article incoming = e.updated();
        String html = renderer.render(incoming.rawMarkdown());
        Article stored = incoming.withRendered(html, incoming.status(), Instant.now());
        repo.save(stored);
        LOG.info("[article] updated id={} status={}", stored.id(), stored.status());
        if (stored.status() == ArticleStatus.PUBLISHED) {
            firePublished(stored, e.initiator());
        }
    }

    private void onDeleted(ArticleDeletedEvent e) {
        boolean removed = repo.delete(e.articleId());
        LOG.info("[article] deleted id={} removed={}", e.articleId(), removed);
    }

    /** Fire the downstream published event onto a fresh mapped activity context. */
    private void firePublished(Article a, String initiator) {
        String preview = a.renderedHtml().length() > 200
                ? a.renderedHtml().substring(0, 200) : a.renderedHtml();
        ArticlePublishedEvent pub = new ArticlePublishedEvent(
                a.id(), a.slug(), a.title(), preview, initiator, Instant.now());
        InMemoryActivityContext pubAci =
                container.createActivityContext("cmr-pub-" + a.id() + "-" + System.nanoTime());
        container.routeEvent(pub, pubAci);
    }

    /** Guard against slug collisions by suffixing the article id fragment. */
    private String uniqueSlug(String slug, String id) {
        return repo.findBySlug(slug)
                .filter(existing -> !existing.id().equals(id))
                .map(existing -> slug + "-" + id.substring(0, Math.min(6, id.length())))
                .orElse(slug);
    }
}
