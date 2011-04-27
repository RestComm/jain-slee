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

import net.java.slee.resources.smpp.util.RelativeSMPPDate;

/**
 * 
 * @author amit bhayani
 *
 */
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
