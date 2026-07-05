/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * Frames SIP messages on stream transports (TCP/TLS) per RFC 3261 §18.3:
 * a message ends after the double-CRLF header terminator plus
 * {@code Content-Length} body octets. Without this decoder, Netty hands the
 * parser arbitrary stream chunks and pipelined or fragmented messages are
 * corrupted.
 *
 * <p>CRLF keep-alive pings ({@code \r\n\r\n}, RFC 5626) are consumed
 * silently.
 */
public final class SipTcpFrameDecoder extends ByteToMessageDecoder {

    /** Upper bound for a single SIP message — protects against malformed
     *  or hostile Content-Length values. */
    private static final int MAX_MESSAGE_BYTES = 1 << 20; // 1 MiB

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while (in.isReadable()) {
            in.markReaderIndex();

            // Swallow leading CRLF keep-alives.
            while (in.readableBytes() >= 2
                    && in.getByte(in.readerIndex()) == '\r'
                    && in.getByte(in.readerIndex() + 1) == '\n') {
                in.skipBytes(2);
            }
            if (!in.isReadable()) {
                return;
            }

            int headerEnd = indexOfDoubleCrlf(in);
            if (headerEnd < 0) {
                if (in.readableBytes() > MAX_MESSAGE_BYTES) {
                    ctx.close();
                    return;
                }
                in.resetReaderIndex();
                return; // wait for more bytes
            }

            int headerLen = headerEnd - in.readerIndex() + 4; // include CRLFCRLF
            int contentLength = parseContentLength(in, in.readerIndex(), headerEnd);
            if (contentLength < 0 || headerLen + contentLength > MAX_MESSAGE_BYTES) {
                ctx.close();
                return;
            }
            if (in.readableBytes() < headerLen + contentLength) {
                in.resetReaderIndex();
                return; // body not complete yet
            }

            ByteBuf frame = in.readRetainedSlice(headerLen + contentLength);
            out.add(frame);
        }
    }

    private static int indexOfDoubleCrlf(ByteBuf buf) {
        int from = buf.readerIndex();
        int to = buf.writerIndex() - 3;
        for (int i = from; i <= to; i++) {
            if (buf.getByte(i) == '\r' && buf.getByte(i + 1) == '\n'
                    && buf.getByte(i + 2) == '\r' && buf.getByte(i + 3) == '\n') {
                return i;
            }
        }
        return -1;
    }

    /** Returns 0 when the header is absent (RFC 3261 §20.14 default for
     *  stream transports is "until connection close", but in practice a
     *  missing Content-Length on TCP means no body for requests). */
    private static int parseContentLength(ByteBuf buf, int start, int headerEnd) {
        String headers = buf.toString(start, headerEnd - start, StandardCharsets.US_ASCII)
                .toLowerCase(Locale.ROOT);
        int idx = headers.indexOf("\r\ncontent-length");
        String compactName = null;
        if (idx < 0) {
            idx = headers.indexOf("\r\nl:");
            compactName = "l:";
        }
        if (idx < 0) {
            return 0;
        }
        int lineStart = idx + 2;
        int colon = headers.indexOf(':', compactName == null ? lineStart : lineStart + 1);
        if (colon < 0) {
            return 0;
        }
        int lineEnd = headers.indexOf("\r\n", colon);
        if (lineEnd < 0) {
            lineEnd = headers.length();
        }
        try {
            return Integer.parseInt(headers.substring(colon + 1, lineEnd).trim());
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }
}
