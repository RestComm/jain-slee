/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.sbbs;

import com.example.cmr.events.article.ArticleCreatedEvent;
import com.example.cmr.events.media.FileDeletedEvent;
import com.example.cmr.events.media.FileUploadedEvent;
import com.example.cmr.model.Article;
import com.example.cmr.model.ArticleStatus;
import com.example.cmr.model.MediaFile;
import com.example.cmr.ports.MediaRepository;
import com.example.cmr.ports.StoragePort;
import com.example.cmr.render.Frontmatter;
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
import java.util.List;
import java.util.UUID;

/**
 * Handles uploads. A binary asset (image/attachment) is streamed to the
 * {@link StoragePort} and recorded as a {@link MediaFile}. A Markdown upload is
 * parsed for YAML front-matter and turned into an
 * {@link ArticleCreatedEvent} — so "upload a .md file" and "type in the editor"
 * converge on the same {@code ArticleSbb} pipeline.
 */
@SbbAnnotation(name = "MediaSbb", vendor = "cmr", version = "1.0")
public final class MediaSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(MediaSbb.class);

    private final StoragePort storage;
    private final MediaRepository mediaRepo;
    private final MicroSleeContainer container;

    public MediaSbb(StoragePort storage, MediaRepository mediaRepo,
                    MicroSleeContainer container) {
        this.storage = storage;
        this.mediaRepo = mediaRepo;
        this.container = container;
    }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof FileUploadedEvent e) {
            onUploaded(e);
        } else if (event instanceof FileDeletedEvent e) {
            onDeleted(e);
        }
    }

    private void onUploaded(FileUploadedEvent e) {
        if (e.isMarkdown()) {
            ingestMarkdown(e);
        } else {
            ingestBinary(e);
        }
    }

    private void ingestBinary(FileUploadedEvent e) {
        StoragePort.Stored stored = storage.store(e.filename(), e.bytes());
        MediaFile media = new MediaFile(UUID.randomUUID().toString(), e.filename(),
                e.mimeType(), e.bytes().length, stored.storagePath(), stored.publicUrl(),
                e.initiator(), Instant.now());
        mediaRepo.save(media);
        LOG.info("[media] stored {} -> {}", e.filename(), stored.publicUrl());
    }

    private void ingestMarkdown(FileUploadedEvent e) {
        String raw = new String(e.bytes(), java.nio.charset.StandardCharsets.UTF_8);
        Frontmatter fm = Frontmatter.parse(raw);
        String title = fm.get("title", stripExtension(e.filename()));
        String category = fm.get("category", "news");
        List<String> tags = fm.list("tags");
        Instant now = Instant.now();
        Article article = new Article(
                UUID.randomUUID().toString(),
                SlugUtils.generate(title),
                title,
                category,
                tags,
                fm.body(),
                "",                        // rendered by ArticleSbb
                null,
                e.initiator(),
                ArticleStatus.PUBLISHED,
                now, now, now);
        ArticleCreatedEvent created = new ArticleCreatedEvent(article, e.initiator());
        InMemoryActivityContext aci =
                container.createActivityContext("cmr-md-" + article.id());
        container.routeEvent(created, aci);
        LOG.info("[media] markdown upload {} -> article '{}'", e.filename(), title);
    }

    private void onDeleted(FileDeletedEvent e) {
        boolean blob = storage.delete(e.storagePath());
        boolean meta = mediaRepo.delete(e.mediaId());
        LOG.info("[media] deleted id={} blob={} meta={}", e.mediaId(), blob, meta);
    }

    private static String stripExtension(String filename) {
        if (filename == null) {
            return "untitled";
        }
        int dot = filename.lastIndexOf('.');
        return dot > 0 ? filename.substring(0, dot) : filename;
    }
}
