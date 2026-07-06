/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpcserver.collab;

import io.grpc.MethodDescriptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Collaborator: identity marshaller — hands raw gRPC message bytes
 * through untouched. This is what makes the RA schema-agnostic: protobuf
 * encoding/decoding stays in the application layer.
 */
public final class BytesMarshaller implements MethodDescriptor.Marshaller<byte[]> {

    public static final BytesMarshaller INSTANCE = new BytesMarshaller();

    private BytesMarshaller() {
    }

    @Override
    public InputStream stream(byte[] value) {
        return new ByteArrayInputStream(value == null ? new byte[0] : value);
    }

    @Override
    public byte[] parse(InputStream stream) {
        try {
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read gRPC message", e);
        }
    }
}
