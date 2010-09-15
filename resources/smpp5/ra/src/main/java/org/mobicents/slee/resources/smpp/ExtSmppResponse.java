package org.mobicents.slee.resources.smpp;

import net.java.slee.resources.smpp.pdu.SmppResponse;

public interface ExtSmppResponse extends SmppResponse {

	org.mobicents.protocols.smpp.message.SMPPPacket getSMPPPacket();
}
