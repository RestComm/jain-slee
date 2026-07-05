package com.example.helloworld.quarkus.command;

import com.microjainslee.api.OutboundCommand;

/**
 * Sealed hierarchy of outbound commands sent from HelloWorldSbb to RA.
 */
public sealed interface HelloWorldCommand extends OutboundCommand
        permits HelloWorldCommand.HttpResponseCommand {

    String sessionId();

    /**
     * Instruct RA to send an HTTP response to the client.
     */
    record HttpResponseCommand(String sessionId, int statusCode,
                               String contentType, String body)
            implements HelloWorldCommand {
    }
}
