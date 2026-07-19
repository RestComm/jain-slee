/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpserver.command;

import com.microjainslee.api.OutboundCommand;

import java.util.Map;

/**
 * Sealed hierarchy of outbound commands targeting the HTTP server RA.
 */
public sealed interface HttpServerCommand extends OutboundCommand
        permits HttpServerCommand.HttpResponseCommand,
                HttpServerCommand.HttpResponseExCommand {

    /**
     * Command to send a text HTTP response back to a pending request identified
     * by its {@code sessionId}.
     */
    record HttpResponseCommand(String sessionId, int statusCode, String contentType,
                               String body) implements HttpServerCommand, OutboundCommand {
    }

    /**
     * Extended response command carrying arbitrary response headers (e.g.
     * {@code Set-Cookie}, {@code Location}, {@code Cache-Control}) and an
     * optional <b>binary</b> body — everything a full web app needs (redirects,
     * sessions, static assets, images) without leaving the RA contract.
     *
     * <p>Exactly one of {@code textBody} / {@code binaryBody} should be set;
     * when both are null the response is empty.</p>
     *
     * @param sessionId   session id from the inbound event
     * @param statusCode  HTTP status
     * @param contentType Content-Type header value (may be null)
     * @param textBody    UTF-8 text body, or null
     * @param binaryBody  raw bytes body, or null
     * @param headers     extra response headers (may be null/empty)
     */
    record HttpResponseExCommand(String sessionId, int statusCode, String contentType,
                                 String textBody, byte[] binaryBody,
                                 Map<String, String> headers)
            implements HttpServerCommand, OutboundCommand {
    }
}
