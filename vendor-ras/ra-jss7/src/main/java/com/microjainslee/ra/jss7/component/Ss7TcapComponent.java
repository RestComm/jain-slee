/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.component;

/**
 * Generic TCAP component hierarchy — protocol-agnostic.
 * Carries raw ASN.1-encoded parameter bytes and operation metadata.
 * SBBs may further decode the payload using jSS7 MAP/CAP/INAP codecs.
 */
public sealed interface Ss7TcapComponent {

    Long invokeId();

    /** TCAP invoke — request an operation from the remote peer. */
    record Invoke(
            Long invokeId, int operationCode, byte[] parameter,
            boolean isLastComponent, int timeout
    ) implements Ss7TcapComponent {}

    /** TCAP return result — successful response to a prior invoke. */
    record ReturnResult(
            Long invokeId, int operationCode, byte[] parameter,
            boolean isLastComponent
    ) implements Ss7TcapComponent {}

    /** TCAP return error — error response to a prior invoke. */
    record ReturnError(
            Long invokeId, int errorCode, byte[] parameter
    ) implements Ss7TcapComponent {}

    /** TCAP reject — protocol-level rejection. */
    record Reject(
            Long invokeId, int problemCode, boolean isLocalOriginated
    ) implements Ss7TcapComponent {}
}
