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

package org.mobicents.slee.resource.map.events.service.lsm;

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
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.mobicents.slee.resource.map.events.MAPEvent;

/**
 * @author baranowb
 * 
 */
public class ProvideSubscriberLocationRequest extends MAPEvent<MAPDialogLsm> {

	private final ProvideSubscriberLocationRequestIndication wrapped;

	/**
	 * @param mAPDialog
	 * @param provideSubscriberLocationRequest
	 */
	public ProvideSubscriberLocationRequest(MAPDialogLsm mAPDialog, ProvideSubscriberLocationRequestIndication provideSubscriberLocationRequest) {
		super(mAPDialog);
		this.wrapped = provideSubscriberLocationRequest;
	}

	public AreaEventInfo getAreaEventInfo() {
		return wrapped.getAreaEventInfo();
	}

	public MAPExtensionContainer getExtensionContainer() {
		return wrapped.getExtensionContainer();
	}

	public byte[] getHGMLCAddress() {
		return wrapped.getHGMLCAddress();
	}

	public IMEI getIMEI() {
		return wrapped.getIMEI();
	}

	public IMSI getIMSI() {
		return wrapped.getIMSI();
	}

	public long getInvokeId() {
		return wrapped.getInvokeId();
	}

	public LCSClientID getLCSClientID() {
		return wrapped.getLCSClientID();
	}

	public LCSCodeword getLCSCodeword() {
		return wrapped.getLCSCodeword();
	}

	public Integer getLCSPriority() {
		return wrapped.getLCSPriority();
	}

	public LCSPrivacyCheck getLCSPrivacyCheck() {
		return wrapped.getLCSPrivacyCheck();
	}

	public LCSQoS getLCSQoS() {
		return wrapped.getLCSQoS();
	}

	public Byte getLCSReferenceNumber() {
		return wrapped.getLCSReferenceNumber();
	}

	public Integer getLCSServiceTypeID() {
		return wrapped.getLCSServiceTypeID();
	}

	public LMSI getLMSI() {
		return wrapped.getLMSI();
	}

	public LocationType getLocationType() {
		return wrapped.getLocationType();
	}

	public ISDNAddressString getMSISDN() {
		return wrapped.getMSISDN();
	}

	public ISDNAddressString getMlcNumber() {
		return wrapped.getMlcNumber();
	}

	public Boolean getPrivacyOverride() {
		return wrapped.getPrivacyOverride();
	}

	public SupportedGADShapes getSupportedGADShapes() {
		return wrapped.getSupportedGADShapes();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProvideSubscriberLocationRequest [wrapped=" + wrapped + "]";
	}

}
