/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.events.media;

import com.example.cmr.events.CmrEvent;
import com.microjainslee.api.annotations.EventType;

import java.time.Instant;

/**
 * Fired when an admin uploads an image or a {@code .md} file. {@code MediaSbb}
 * hands the bytes to the {@code StoragePort}; for a Markdown upload it also
 * parses YAML front-matter and fires an
 * {@link com.example.cmr.events.article.ArticleCreatedEvent}.
 *
 * @param uploadId  correlation id for the upload
 * @param filename  original filename (extension drives the branch)
 * @param mimeType  detected content type
 * @param bytes     payload (this example keeps uploads in memory)
 * @param initiator admin username
 * @param firedAt   fire timestamp
 */
@EventType(name = "FileUploaded", vendor = "cmr", version = "1.0")
public record FileUploadedEvent(String uploadId, String filename, String mimeType,
                                byte[] bytes, String initiator, Instant firedAt)
        implements CmrEvent {

    public FileUploadedEvent(String uploadId, String filename, String mimeType,
                             byte[] bytes, String initiator) {
        this(uploadId, filename, mimeType, bytes, initiator, Instant.now());
    }

    /** True when the upload is a Markdown document rather than a binary asset. */
    public boolean isMarkdown() {
        return filename != null
                && (filename.endsWith(".md") || filename.endsWith(".markdown"));
    }
}
