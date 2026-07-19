/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.repo;

import com.example.cmr.model.MediaFile;
import com.example.cmr.ports.MediaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/** Thread-safe in-memory {@link MediaRepository}. */
public final class InMemoryMediaRepository implements MediaRepository {

    private final ConcurrentHashMap<String, MediaFile> byId = new ConcurrentHashMap<>();

    @Override
    public MediaFile save(MediaFile media) {
        byId.put(media.id(), media);
        return media;
    }

    @Override
    public Optional<MediaFile> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public boolean delete(String id) {
        return byId.remove(id) != null;
    }

    @Override
    public List<MediaFile> findAll() {
        return byId.values().stream()
                .sorted(Comparator.comparing(MediaFile::uploadedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return byId.size();
    }
}
