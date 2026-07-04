/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 */

package com.example.ussddemo.commands;

import com.microjainslee.api.OutboundCommand;

/**
 * Outbound command sent from an SBB to the gRPC menu RA via
 * {@link com.microjainslee.api.RaCommandPort#sendCommand(OutboundCommand)}.
 * This is a self-contained local command type for the embedded example;
 * production deployments use {@code com.microjainslee.ra.grpc.GrpcMenuCommand}
 * from the vendor RA module.
 */
public record GrpcMenuCommand(String menuRequest) implements OutboundCommand {
}
