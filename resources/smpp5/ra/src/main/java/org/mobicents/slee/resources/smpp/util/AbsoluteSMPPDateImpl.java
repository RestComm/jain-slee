package org.mobicents.slee.resources.smpp.util;

import java.util.Calendar;
import java.util.TimeZone;

public class AbsoluteSMPPDateImpl extends SMPPDateImpl implements net.java.slee.resources.smpp.util.AbsoluteSMPPDate {
	
	public AbsoluteSMPPDateImpl(org.mobicents.protocols.smpp.util.SMPPDate protoSMPPDate){
		this.protoSMPPDate = protoSMPPDate;
	}

	public AbsoluteSMPPDateImpl(Calendar calendar, boolean hasTz) {
		this.protoSMPPDate = org.mobicents.protocols.smpp.util.SMPPDate.getAbsoluteInstance(calendar, hasTz);
	}

	public Calendar getCalendar() {
		return protoSMPPDate.getCalendar();
	}

	public TimeZone getTimeZone() {
		return protoSMPPDate.getTimeZone();
	}
	
	public boolean isAbsolute() {
		return true;
	}

	public boolean isRelative() {
		return false;
	}

}
