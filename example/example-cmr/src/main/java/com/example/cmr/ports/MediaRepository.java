/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.ports;

import com.example.cmr.model.MediaFile;

import java.util.List;
import java.util.Optional;

/** Metadata store for uploaded media. Bytes live behind {@link StoragePort}. */
public interface MediaRepository {

    MediaFile save(MediaFile media);

    Optional<MediaFile> findById(String id);

    boolean delete(String id);

    /** All media, newest first. */
    List<MediaFile> findAll();

    long count();
}
