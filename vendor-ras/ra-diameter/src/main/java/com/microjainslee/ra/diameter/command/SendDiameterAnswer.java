/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter.command;

import java.util.Map;

/** Send a Diameter answer back to the peer. */
public record SendDiameterAnswer(
    String sessionId,
    long applicationId,
    int commandCode,
    long resultCode,
    long hopByHopId,
    long endToEndId,
    Map<Integer, String> avps
) implements DiameterCommand {

    /** DIAMETER_SUCCESS (2001). */
    public static final long SUCCESS = 2001;

    public static SendDiameterAnswer ok(String sessionId, long appId, int cmdCode,
                                         long hbh, long ete, Map<Integer, String> avps) {
        return new SendDiameterAnswer(sessionId, appId, cmdCode, SUCCESS, hbh, ete, avps);
    }

    public static SendDiameterAnswer error(String sessionId, long appId, int cmdCode,
                                            long hbh, long ete, long resultCode) {
        return new SendDiameterAnswer(sessionId, appId, cmdCode, resultCode, hbh, ete, Map.of());
    }
}
