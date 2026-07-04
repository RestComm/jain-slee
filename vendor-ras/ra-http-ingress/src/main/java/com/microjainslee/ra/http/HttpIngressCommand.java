/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.http;

import com.microjainslee.api.OutboundCommand;

/**
 * Base type for outbound commands targeting the HTTP ingress RA.
 *
 * <p>Currently a marker; concrete sub-types will carry callback
 * response payloads (e.g. USSD final response to push to an
 * external callback URL) when the ingress RA gains outbound
 * capabilities.</p>
 */
public abstract class HttpIngressCommand implements OutboundCommand {
    protected HttpIngressCommand() {
    }
}
