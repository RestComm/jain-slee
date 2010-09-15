package org.mobicents.slee.resources.smpp.util;

import net.java.slee.resources.smpp.util.RelativeSMPPDate;

public class RelativeSMPPDateImpl extends SMPPDateImpl implements RelativeSMPPDate {
	
	public RelativeSMPPDateImpl(org.mobicents.protocols.smpp.util.SMPPDate protoSMPPDate){
		this.protoSMPPDate = protoSMPPDate;
	}
	
	public RelativeSMPPDateImpl(int years, int months, int days, int hours, int minutes, int seconds) {
		protoSMPPDate = org.mobicents.protocols.smpp.util.SMPPDate.getRelativeInstance(years, months, days, hours,
				minutes, seconds);
	}
	
	public boolean isAbsolute() {
		return false;
	}

	public boolean isRelative() {
		return true;
	}
}
