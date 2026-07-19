/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.ports;

/**
 * Blob storage abstraction — the functional face of the {@code storage/} RA.
 * The RA also registers itself with the container as a 3-port resource
 * adaptor, but SBBs and routers use this synchronous port directly.
 */
public interface StoragePort {

    /**
     * Store bytes under a filename and return a stored blob.
     *
     * @param filename original filename (used to derive extension)
     * @param bytes    payload
     * @return the stored blob's locator + public URL
     */
    Stored store(String filename, byte[] bytes);

    /** Read bytes back by storage path, or {@code null} if absent. */
    byte[] read(String storagePath);

    /** Delete by storage path; returns true if something was removed. */
    boolean delete(String storagePath);

    /**
     * Result of a store operation.
     *
     * @param storagePath opaque locator (e.g. relative path or S3 key)
     * @param publicUrl   URL the public site serves the blob from
     */
    record Stored(String storagePath, String publicUrl) {
    }
}
