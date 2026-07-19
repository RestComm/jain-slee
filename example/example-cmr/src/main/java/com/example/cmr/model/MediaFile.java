/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.model;

import java.time.Instant;

/**
 * A stored binary (image or attachment) tracked by {@code MediaSbb}. The
 * bytes live behind the {@code StoragePort}; this record is only metadata.
 *
 * @param id               stable identifier (UUID)
 * @param originalFilename filename as uploaded
 * @param mimeType         detected content type
 * @param sizeBytes        payload size
 * @param storagePath      opaque locator returned by the {@code StoragePort}
 * @param publicUrl        URL the public site serves it from
 * @param uploadedBy       admin username
 * @param uploadedAt       upload timestamp
 */
public record MediaFile(
        String id,
        String originalFilename,
        String mimeType,
        long sizeBytes,
        String storagePath,
        String publicUrl,
        String uploadedBy,
        Instant uploadedAt
) {
    /** True for {@code image/*} content — eligible for inline/thumbnail use. */
    public boolean isImage() {
        return mimeType != null && mimeType.startsWith("image/");
    }
}
