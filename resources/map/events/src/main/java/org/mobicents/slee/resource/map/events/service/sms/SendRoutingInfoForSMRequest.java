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

package org.mobicents.slee.resource.map.events.service.sms;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequestIndication;
import org.mobicents.slee.resource.map.events.MAPEvent;

/**
 * @author baranowb
 * 
 */
public class SendRoutingInfoForSMRequest extends MAPEvent<MAPDialogSms> {
	
	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.SEND_ROUTING_INFO_FOR_SM_REQUEST";

	private final SendRoutingInfoForSMRequestIndication wrapped;

	/**
	 * @param mAPDialog
	 * @param sendRoutingInfoForSmRequest
	 */
	public SendRoutingInfoForSMRequest(MAPDialogSms mAPDialog, SendRoutingInfoForSMRequestIndication sendRoutingInfoForSmRequest) {
		super(mAPDialog, EVENT_TYPE_NAME);
		this.wrapped = sendRoutingInfoForSmRequest;
	}

	public MAPExtensionContainer getExtensionContainer() {
		return wrapped.getExtensionContainer();
	}

	public boolean getGprsSupportIndicator() {
		return wrapped.getGprsSupportIndicator();
	}

	public long getInvokeId() {
		return wrapped.getInvokeId();
	}

	public ISDNAddressString getMsisdn() {
		return wrapped.getMsisdn();
	}

	public SM_RP_MTI getSM_RP_MTI() {
		return wrapped.getSM_RP_MTI();
	}

	public SM_RP_SMEA getSM_RP_SMEA() {
		return wrapped.getSM_RP_SMEA();
	}

	public AddressString getServiceCentreAddress() {
		return wrapped.getServiceCentreAddress();
	}

	public boolean getSm_RP_PRI() {
		return wrapped.getSm_RP_PRI();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SendRoutingInfoForSMRequest [wrapped=" + wrapped + "]";
	}

}
