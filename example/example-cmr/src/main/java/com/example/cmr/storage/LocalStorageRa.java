/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.storage;

import com.example.cmr.ports.StoragePort;

import com.microjainslee.api.OutboundCommand;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Local-filesystem storage resource adaptor. Doubles as a {@link StoragePort}
 * for synchronous use by SBBs/routers and as a 3-port SLEE RA
 * ({@link RaEndpointPort} + {@link RaCommandPort}) so it registers with the
 * container and shows up in telemetry like any other resource.
 *
 * <p>Swap this class for an {@code S3StorageRa} to move blobs off-box — the
 * {@link StoragePort} contract is all the rest of the app depends on.</p>
 */
public final class LocalStorageRa implements StoragePort, RaEndpointPort, RaCommandPort {

    private static final Logger LOG = LogManager.getLogger(LocalStorageRa.class);

    private final Path baseDir;
    private final String publicPrefix;

    /**
     * @param baseDir      directory blobs are written under (created on activate)
     * @param publicPrefix URL prefix the HTTP RA serves the directory from
     */
    public LocalStorageRa(Path baseDir, String publicPrefix) {
        this.baseDir = baseDir;
        this.publicPrefix = publicPrefix.endsWith("/")
                ? publicPrefix.substring(0, publicPrefix.length() - 1) : publicPrefix;
    }

    // ── StoragePort ──

    @Override
    public Stored store(String filename, byte[] bytes) {
        String ext = extensionOf(filename);
        String stored = UUID.randomUUID() + ext;
        try {
            Files.createDirectories(baseDir);
            Files.write(baseDir.resolve(stored), bytes);
        } catch (IOException e) {
            throw new UncheckedIOException("store failed: " + filename, e);
        }
        LOG.debug("[storage] stored {} ({} bytes) as {}", filename, bytes.length, stored);
        return new Stored(stored, publicPrefix + "/" + stored);
    }

    @Override
    public byte[] read(String storagePath) {
        try {
            Path p = baseDir.resolve(storagePath);
            return Files.exists(p) ? Files.readAllBytes(p) : null;
        } catch (IOException e) {
            throw new UncheckedIOException("read failed: " + storagePath, e);
        }
    }

    @Override
    public boolean delete(String storagePath) {
        try {
            return Files.deleteIfExists(baseDir.resolve(storagePath));
        } catch (IOException e) {
            LOG.warn("[storage] delete failed {}: {}", storagePath, e.getMessage());
            return false;
        }
    }

    /** Directory blobs are stored under — the HTTP RA serves it statically. */
    public Path baseDir() {
        return baseDir;
    }

    public String publicPrefix() {
        return publicPrefix;
    }

    // ── RaEndpointPort ──

    @Override
    public String getRaName() {
        return "cmr-storage-ra";
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new UncheckedIOException("cannot create storage dir " + baseDir, e);
        }
        LOG.info("[storage] RA activated — base dir {}", baseDir.toAbsolutePath());
    }

    @Override
    public void deactivate() {
        LOG.info("[storage] RA deactivated");
    }

    // ── RaCommandPort (no outbound commands defined yet) ──

    @Override
    public void sendCommand(OutboundCommand command) {
        LOG.debug("[storage] ignoring command {}",
                command == null ? "null" : command.getClass().getSimpleName());
    }

    private static String extensionOf(String filename) {
        if (filename == null) {
            return "";
        }
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot).toLowerCase() : "";
    }
}
