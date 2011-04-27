/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resources.smpp.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 
 * @author amit bhayani
 *
 */
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
