package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.GenericNack;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public class GenericNackImpl extends PDUImpl implements GenericNack, ExtSmppResponse {

	public GenericNackImpl(org.mobicents.protocols.smpp.message.GenericNack genericNack) {
		this.smppPacket = genericNack;
	}

	public GenericNackImpl(int commandStatus) {
		this.smppPacket = new org.mobicents.protocols.smpp.message.GenericNack();
		this.smppPacket.setCommandStatus(commandStatus);
	}

	public boolean isTLVPermitted(Tag tag) {
		return false;
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
