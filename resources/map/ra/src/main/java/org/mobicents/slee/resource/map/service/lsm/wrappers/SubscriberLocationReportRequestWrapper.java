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

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;

/**
 * @author baranowb
 * @author amit bhayani
 */
public class SubscriberLocationReportRequestWrapper extends LsmMessageWrapper<SubscriberLocationReportRequest>
		implements SubscriberLocationReportRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.lsm.SUBSCRIBER_LOCATION_REPORT_REQUEST";

	/**
	 * @param mapDialogWrapper
	 * @param subscriberLocationReportRequest
	 */
	public SubscriberLocationReportRequestWrapper(MAPDialogLsmWrapper mAPDialog,
			SubscriberLocationReportRequest subscriberLocationReportRequest) {
		super(mAPDialog, EVENT_TYPE_NAME, subscriberLocationReportRequest);
	}

	public AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator() {
		return this.wrappedEvent.getAccuracyFulfilmentIndicator();
	}

	public byte[] getAdditionalLocationEstimate() {
		return this.wrappedEvent.getAdditionalLocationEstimate();
	}

	public Integer getAgeOfLocationEstimate() {
		return this.wrappedEvent.getAgeOfLocationEstimate();
	}

	public DeferredmtlrData getDeferredmtlrData() {
		return this.wrappedEvent.getDeferredmtlrData();
	}

	public byte[] getGeranPositioningData() {
		return this.wrappedEvent.getGeranPositioningData();
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

	public long getInvokeId() {
		return this.wrappedEvent.getInvokeId();
	}

	public LCSClientID getLCSClientID() {
		return this.wrappedEvent.getLCSClientID();
	}

	public LCSEvent getLCSEvent() {
		return this.wrappedEvent.getLCSEvent();
	}

	public LCSLocationInfo getLCSLocationInfo() {
		return this.wrappedEvent.getLCSLocationInfo();
	}

	public Byte getLCSReferenceNumber() {
		return this.wrappedEvent.getLCSReferenceNumber();
	}

	public Integer getLCSServiceTypeID() {
		return this.wrappedEvent.getLCSServiceTypeID();
	}

	public byte[] getLocationEstimate() {
		return this.wrappedEvent.getLocationEstimate();
	}

	public ISDNAddressString getMSISDN() {
		return this.wrappedEvent.getMSISDN();
	}

	public ISDNAddressString getNaESRD() {
		return this.wrappedEvent.getNaESRD();
	}

	public ISDNAddressString getNaESRK() {
		return this.wrappedEvent.getNaESRK();
	}

	public Boolean getPseudonymIndicator() {
		return this.wrappedEvent.getPseudonymIndicator();
	}

	public SLRArgExtensionContainer getSLRArgExtensionContainer() {
		return this.wrappedEvent.getSLRArgExtensionContainer();
	}

	public Boolean getSaiPresent() {
		return this.wrappedEvent.getSaiPresent();
	}

	public byte[] getUtranPositioningData() {
		return this.wrappedEvent.getUtranPositioningData();
	}

	@Override
	public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
		return this.wrappedEvent.getCellGlobalIdOrServiceAreaIdOrLAI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubscriberLocationReportRequest [wrapped=" + this.wrappedEvent + "]";
	}

}
