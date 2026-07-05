/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.stun;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Minimal STUN Binding client (RFC 5389).
 *
 * <p>Sends a Binding Request to a configured STUN server and parses
 * the XOR-MAPPED-ADDRESS attribute from the response to discover
 * the public IP:port (server-reflexive candidate).</p>
 *
 * <p>Automatic keep-alive periodically refreshes the binding to
 * prevent NAT timeout.</p>
 */
public final class StunClient implements AutoCloseable {

    /** Result of a successful STUN binding. */
    public record StunResult(String publicAddress, int publicPort) {}

    // RFC 5389 constants
    private static final int BINDING_REQUEST   = 0x0001;
    private static final int MAGIC_COOKIE      = 0x2112A442;
    private static final int ATTR_XOR_MAPPED   = 0x0020;
    private static final int HEADER_SIZE       = 20;
    private static final int DEFAULT_STUN_PORT = 3478;
    private static final int RECEIVE_TIMEOUT_MS = 3000;

    private final String stunServer;
    private final int stunPort;
    private final ScheduledExecutorService scheduler;
    private final Random random = new Random();

    private DatagramSocket socket;
    private volatile StunResult lastResult;

    /**
     * @param stunServer  STUN server hostname or IP
     * @param stunPort    STUN server port (0 or negative = default 3478)
     */
    public StunClient(String stunServer, int stunPort) {
        this.stunServer = stunServer;
        this.stunPort = stunPort > 0 ? stunPort : DEFAULT_STUN_PORT;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = Thread.ofVirtual()
                    .name("stun-keepalive-", 1)
                    .factory()
                    .newThread(r);
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Send a STUN Binding Request and return the discovered public address.
     *
     * @return future with the XOR-MAPPED-ADDRESS result
     */
    public CompletableFuture<StunResult> sendBindingRequest() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ensureSocket();
                byte[] request = createBindingRequest();
                DatagramPacket packet = new DatagramPacket(
                        request, request.length,
                        InetAddress.getByName(stunServer), stunPort);
                socket.send(packet);

                byte[] buffer = new byte[512];
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socket.receive(response);

                StunResult result = parseXorMappedAddress(buffer, response.getLength());
                this.lastResult = result;
                return result;
            } catch (Exception e) {
                throw new CompletionException("STUN binding failed", e);
            }
        });
    }


    /**
     * Start periodic keep-alive to maintain the NAT binding.
     *
     * @param intervalSeconds  interval between binding requests
     */
    public void startKeepAlive(long intervalSeconds) {
        if (intervalSeconds <= 0) return;
        scheduler.scheduleAtFixedRate(() -> {
            try {
                sendBindingRequest().get(3, TimeUnit.SECONDS);
            } catch (Exception ignored) {
                // Keep-alive failure is non-fatal; will retry next interval
            }
        }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
    }

    /** Return the most recent STUN binding result, or null. */
    public StunResult lastResult() {
        return lastResult;
    }

    @Override
    public void close() {
        scheduler.shutdownNow();
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        socket = null;
    }

    // ---- wire format (RFC 5389 §6) ----

    private void ensureSocket() throws SocketException {
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket();
            socket.setSoTimeout(RECEIVE_TIMEOUT_MS);
        }
    }

    private byte[] createBindingRequest() {
        byte[] transactionId = new byte[12];
        random.nextBytes(transactionId);

        ByteBuffer buf = ByteBuffer.allocate(HEADER_SIZE);
        buf.putShort((short) BINDING_REQUEST);  // Message Type
        buf.putShort((short) 0);                 // Message Length (no attributes)
        buf.putInt(MAGIC_COOKIE);                // Magic Cookie
        buf.put(transactionId);                  // Transaction ID
        return buf.array();
    }

    /**
     * Walk attributes looking for XOR-MAPPED-ADDRESS (0x0020).
     * Returns a zero-address result if parsing fails.
     */
    private static StunResult parseXorMappedAddress(byte[] data, int len) {
        int offset = HEADER_SIZE;
        while (offset + 4 <= len) {
            int type   = ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
            int attLen = ((data[offset + 2] & 0xFF) << 8) | (data[offset + 3] & 0xFF);
            int valueStart = offset + 4;

            if (type == ATTR_XOR_MAPPED && attLen >= 8 && valueStart + attLen <= len) {
                int family = data[valueStart + 1] & 0xFF;
                if (family == 0x01) { // IPv4
                    int port = ((data[valueStart + 2] & 0xFF) << 8)
                             | (data[valueStart + 3] & 0xFF);
                    port ^= (MAGIC_COOKIE >>> 16);
                    byte[] ip = new byte[4];
                    System.arraycopy(data, valueStart + 4, ip, 0, 4);
                    for (int i = 0; i < 4; i++) {
                        int shift = 24 - (8 * i);
                        ip[i] ^= (byte) ((MAGIC_COOKIE >>> shift) & 0xFF);
                    }
                    try {
                        return new StunResult(
                                InetAddress.getByAddress(ip).getHostAddress(), port);
                    } catch (java.net.UnknownHostException e) {
                        return new StunResult("0.0.0.0", 0);
                    }
                }
            }
            offset = valueStart + attLen;
            if (attLen % 4 != 0) {
                offset += 4 - (attLen % 4);
            }
        }
        return new StunResult("0.0.0.0", 0);
    }
}
