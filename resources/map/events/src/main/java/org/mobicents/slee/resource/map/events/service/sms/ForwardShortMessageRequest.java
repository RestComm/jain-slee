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

import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.slee.resource.map.events.MAPEvent;

/**
 * @author baranowb
 * 
 */
public class ForwardShortMessageRequest extends MAPEvent<MAPDialogSms> {

	private final ForwardShortMessageRequestIndication wrapped;

	/**
	 * @param mAPDialog
	 * @param forwardShortMessageRequest
	 */
	public ForwardShortMessageRequest(MAPDialogSms mAPDialog, ForwardShortMessageRequestIndication forwardShortMessageRequest) {
		super(mAPDialog);
		this.wrapped = forwardShortMessageRequest;
	}

	public long getInvokeId() {
		return wrapped.getInvokeId();
	}

	public boolean getMoreMessagesToSend() {
		return wrapped.getMoreMessagesToSend();
	}

	public SM_RP_DA getSM_RP_DA() {
		return wrapped.getSM_RP_DA();
	}

	public SM_RP_OA getSM_RP_OA() {
		return wrapped.getSM_RP_OA();
	}

	public SmsSignalInfo getSM_RP_UI() {
		return wrapped.getSM_RP_UI();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ForwardShortMessageRequest [wrapped=" + wrapped + "]";
	}
	
}
