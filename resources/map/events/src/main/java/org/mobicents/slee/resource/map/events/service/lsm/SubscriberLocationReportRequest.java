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
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequestIndication;
import org.mobicents.slee.resource.map.events.MAPEvent;

/**
 * @author baranowb
 * 
 */
public class SubscriberLocationReportRequest extends MAPEvent<MAPDialogLsm> {

	private final SubscriberLocationReportRequestIndication wrapped;

	/**
	 * @param mAPDialog
	 * @param subscriberLocationReportRequest
	 */
	public SubscriberLocationReportRequest(MAPDialogLsm mAPDialog, SubscriberLocationReportRequestIndication subscriberLocationReportRequest) {
		super(mAPDialog);
		this.wrapped = subscriberLocationReportRequest;
	}

	public AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator() {
		return wrapped.getAccuracyFulfilmentIndicator();
	}

	public byte[] getAdditionalLocationEstimate() {
		return wrapped.getAdditionalLocationEstimate();
	}

	public Integer getAgeOfLocationEstimate() {
		return wrapped.getAgeOfLocationEstimate();
	}

	public DeferredmtlrData getDeferredmtlrData() {
		return wrapped.getDeferredmtlrData();
	}

	public byte[] getGeranPositioningData() {
		return wrapped.getGeranPositioningData();
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

	public LCSEvent getLCSEvent() {
		return wrapped.getLCSEvent();
	}

	public LCSLocationInfo getLCSLocationInfo() {
		return wrapped.getLCSLocationInfo();
	}

	public Byte getLCSReferenceNumber() {
		return wrapped.getLCSReferenceNumber();
	}

	public Integer getLCSServiceTypeID() {
		return wrapped.getLCSServiceTypeID();
	}

	public byte[] getLocationEstimate() {
		return wrapped.getLocationEstimate();
	}

	public ISDNAddressString getMSISDN() {
		return wrapped.getMSISDN();
	}

	public ISDNAddressString getNaESRD() {
		return wrapped.getNaESRD();
	}

	public ISDNAddressString getNaESRK() {
		return wrapped.getNaESRK();
	}

	public Boolean getPseudonymIndicator() {
		return wrapped.getPseudonymIndicator();
	}

	public SLRArgExtensionContainer getSLRArgExtensionContainer() {
		return wrapped.getSLRArgExtensionContainer();
	}

	public Boolean getSaiPresent() {
		return wrapped.getSaiPresent();
	}

	public byte[] getUtranPositioningData() {
		return wrapped.getUtranPositioningData();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubscriberLocationReportRequest [wrapped=" + wrapped + "]";
	}

}
