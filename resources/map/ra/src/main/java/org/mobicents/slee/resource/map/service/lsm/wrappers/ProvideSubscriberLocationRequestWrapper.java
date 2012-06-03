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

package org.mobicents.slee.resource.map.service.lsm.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;

/**
 * @author baranowb
 * @author amit bhayani
 */
public class ProvideSubscriberLocationRequestWrapper extends LsmMessageWrapper<ProvideSubscriberLocationRequest>
		implements ProvideSubscriberLocationRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.lsm.PROVIDE_SUBSCRIBER_LOCATION_REQUEST";

	/**
	 * @param mapDialogWrapper
	 * @param provideSubscriberLocationRequest
	 */
	public ProvideSubscriberLocationRequestWrapper(MAPDialogLsmWrapper mAPDialog,
			ProvideSubscriberLocationRequest provideSubscriberLocationRequest) {
		super(mAPDialog, EVENT_TYPE_NAME, provideSubscriberLocationRequest);
	}

	public AreaEventInfo getAreaEventInfo() {
		return this.wrappedEvent.getAreaEventInfo();
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	public byte[] getHGMLCAddress() {
		return this.wrappedEvent.getHGMLCAddress();
	}

	public IMEI getIMEI() {
		return this.wrappedEvent.getIMEI();
	}

	public IMSI getIMSI() {
		return this.wrappedEvent.getIMSI();
	}

	public LCSClientID getLCSClientID() {
		return this.wrappedEvent.getLCSClientID();
	}

	public LCSCodeword getLCSCodeword() {
		return this.wrappedEvent.getLCSCodeword();
	}

	public Integer getLCSPriority() {
		return this.wrappedEvent.getLCSPriority();
	}

	public LCSPrivacyCheck getLCSPrivacyCheck() {
		return this.wrappedEvent.getLCSPrivacyCheck();
	}

	public LCSQoS getLCSQoS() {
		return this.wrappedEvent.getLCSQoS();
	}

	public Byte getLCSReferenceNumber() {
		return this.wrappedEvent.getLCSReferenceNumber();
	}

	public Integer getLCSServiceTypeID() {
		return this.wrappedEvent.getLCSServiceTypeID();
	}

	public LMSI getLMSI() {
		return this.wrappedEvent.getLMSI();
	}

	public LocationType getLocationType() {
		return this.wrappedEvent.getLocationType();
	}

	public ISDNAddressString getMSISDN() {
		return this.wrappedEvent.getMSISDN();
	}

	public ISDNAddressString getMlcNumber() {
		return this.wrappedEvent.getMlcNumber();
	}

	public Boolean getPrivacyOverride() {
		return this.wrappedEvent.getPrivacyOverride();
	}

	public SupportedGADShapes getSupportedGADShapes() {
		return this.wrappedEvent.getSupportedGADShapes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProvideSubscriberLocationRequestWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
