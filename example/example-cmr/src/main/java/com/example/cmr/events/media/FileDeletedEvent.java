/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.events.media;

import com.example.cmr.events.CmrEvent;
import com.microjainslee.api.annotations.EventType;

import java.time.Instant;

/**
 * Fired when an admin removes a media file. {@code MediaSbb} deletes the bytes
 * via the {@code StoragePort} and drops the metadata.
 */
@EventType(name = "FileDeleted", vendor = "cmr", version = "1.0")
public record FileDeletedEvent(String mediaId, String storagePath,
                               String initiator, Instant firedAt)
        implements CmrEvent {

    public FileDeletedEvent(String mediaId, String storagePath, String initiator) {
        this(mediaId, storagePath, initiator, Instant.now());
    }
}
