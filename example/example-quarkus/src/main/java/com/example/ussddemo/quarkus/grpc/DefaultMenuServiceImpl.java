/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.quarkus.grpc;

import com.example.ussddemo.quarkus.grpc.proto.MenuRequest;
import com.example.ussddemo.quarkus.grpc.proto.MenuResponse;
import com.example.ussddemo.quarkus.grpc.proto.UssdMenuServiceGrpc;

import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** In-process gRPC USSD AS used by smoke tests. */
public final class DefaultMenuServiceImpl extends UssdMenuServiceGrpc.UssdMenuServiceImplBase {

    private static final Logger LOG = LogManager.getLogger(DefaultMenuServiceImpl.class);

    private static final String BALANCE_MENU =
            "Welcome to micro-jainslee demo\n1. Balance\n2. Buy bundle\n0. Exit";

    @Override
    public void resolveMenu(MenuRequest request, StreamObserver<MenuResponse> responseObserver) {
        LOG.infof("[grpc] ResolveMenu msisdn=%s ussdString=%s",
                request.getMsisdn(), request.getUssdString());
        String sessionId = request.getSessionId().isEmpty()
                ? java.util.UUID.randomUUID().toString()
                : request.getSessionId();
        MenuResponse.Builder b = MenuResponse.newBuilder().setSessionId(sessionId);
        if ("*123#".equals(request.getUssdString())) {
            responseObserver.onNext(b.setStatus("OK").setMenuText(BALANCE_MENU).build());
        } else {
            responseObserver.onNext(b.setStatus("ERR")
                    .setError("Unknown short code " + request.getUssdString()
                            + " for " + request.getMsisdn())
                    .build());
        }
        responseObserver.onCompleted();
    }
}
