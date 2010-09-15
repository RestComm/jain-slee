package org.mobicents.slee.resources.smpp.util;

import net.java.slee.resources.smpp.util.SMPPDate;

public abstract class SMPPDateImpl implements SMPPDate {

	protected org.mobicents.protocols.smpp.util.SMPPDate protoSMPPDate;

	public int getDay() {
		return protoSMPPDate.getDay();
	}

	public int getHour() {
		return protoSMPPDate.getHour();
	}

	public int getMinute() {
		return protoSMPPDate.getMinute();
	}

	public int getMonth() {
		return protoSMPPDate.getMonth();
	}

	public int getSecond() {
		return protoSMPPDate.getSecond();
	}

	public char getSign() {
		return protoSMPPDate.getSign();
	}

	public int getTenth() {
		return protoSMPPDate.getTenth();
	}

	public int getUtcOffset() {
		return protoSMPPDate.getUtcOffset();
	}

	public int getYear() {
		return protoSMPPDate.getYear();
	}

	public org.mobicents.protocols.smpp.util.SMPPDate getSMPPDate() {
		return this.protoSMPPDate;
	}

}
