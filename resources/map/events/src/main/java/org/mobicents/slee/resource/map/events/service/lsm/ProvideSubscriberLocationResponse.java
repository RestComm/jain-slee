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

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponseIndication;
import org.mobicents.slee.resource.map.events.MAPEvent;

/**
 * @author baranowb
 * 
 */
public class ProvideSubscriberLocationResponse extends MAPEvent<MAPDialogLsm> {

	private final ProvideSubscriberLocationResponseIndication wrapped;

	/**
	 * @param mAPDialog
	 * @param provideSubscriberLocationResponse
	 */
	public ProvideSubscriberLocationResponse(MAPDialogLsm mAPDialog, ProvideSubscriberLocationResponseIndication provideSubscriberLocationResponse) {
		super(mAPDialog);
		this.wrapped = provideSubscriberLocationResponse;
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

	public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
		return wrapped.getCellGlobalIdOrServiceAreaIdOrLAI();
	}

	public Boolean getDeferredMTLRResponseIndicator() {
		return wrapped.getDeferredMTLRResponseIndicator();
	}

	public MAPExtensionContainer getExtensionContainer() {
		return wrapped.getExtensionContainer();
	}

	public byte[] getGeranPositioningData() {
		return wrapped.getGeranPositioningData();
	}

	public long getInvokeId() {
		return wrapped.getInvokeId();
	}

	public byte[] getLocationEstimate() {
		return wrapped.getLocationEstimate();
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
		return "ProvideSubscriberLocationResponse [wrapped=" + wrapped + "]";
	}

}
