/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpserver.events;

/**
 * One {@code multipart/form-data} file part, already read into memory by the
 * RA — so application SBBs never touch the wire or a temp file.
 *
 * @param name        the form field name
 * @param filename    the client-supplied file name (may be null)
 * @param contentType the part content type (may be null)
 * @param content     the raw bytes of the uploaded file
 */
public record HttpUpload(String name, String filename, String contentType, byte[] content) {

    /** Size of the uploaded content in bytes. */
    public long size() {
        return content == null ? 0 : content.length;
    }
}
