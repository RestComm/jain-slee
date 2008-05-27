package org.mobicents.slee.resource.diameter.base.events;



import net.java.slee.resource.diameter.base.events.DiameterHeader;

public class DiameterHeaderImpl implements DiameterHeader {

    private long applicationId, hopByHopId, endToEndId;
    private int messageLength, commandCode;
    private boolean request, proxiable, error, potentiallyRetransmitted;

    public DiameterHeaderImpl(long applicationId, long hopByHopId, long endToEndId, int messageLength, int commandCode, boolean request, boolean proxiable, boolean error, boolean potentiallyRetransmitted) {
        this.applicationId = applicationId;
        this.hopByHopId = hopByHopId;
        this.endToEndId = endToEndId;
        this.messageLength = messageLength;
        this.commandCode = commandCode;
        this.request = request;
        this.proxiable = proxiable;
        this.error = error;
        this.potentiallyRetransmitted = potentiallyRetransmitted;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public long getHopByHopId() {
        return hopByHopId;
    }

    public long getEndToEndId() {
        return endToEndId;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public int getCommandCode() {
        return commandCode;
    }

    public boolean isRequest() {
        return request;
    }

    public boolean isProxiable() {
        return proxiable;
    }

    public boolean isError() {
        return error;
    }

    public boolean isPotentiallyRetransmitted() {
        return potentiallyRetransmitted;
    }

    public short getVersion() {
        return 1;
    }

    public Object clone() {
        return new DiameterHeaderImpl(applicationId, hopByHopId, endToEndId, messageLength, commandCode, request, proxiable, error, potentiallyRetransmitted);
    }
}
