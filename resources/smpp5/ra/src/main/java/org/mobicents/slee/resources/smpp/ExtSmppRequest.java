package org.mobicents.slee.resources.smpp;

import net.java.slee.resources.smpp.pdu.SmppRequest;

public interface ExtSmppRequest extends SmppRequest {
	
	org.mobicents.protocols.smpp.message.SMPPPacket getSMPPPacket();
	
}
