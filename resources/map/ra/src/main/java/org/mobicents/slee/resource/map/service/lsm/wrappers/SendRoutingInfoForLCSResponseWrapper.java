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

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;

/**
 * @author baranowb
 * @author amit bhayani
 */
public class SendRoutingInfoForLCSResponseWrapper extends LsmMessageWrapper<SendRoutingInfoForLCSResponse>
		implements SendRoutingInfoForLCSResponse {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.lsm.SEND_ROUTING_INFO_FOR_LCS_RESPONSE";

	/**
	 * @param mapDialogWrapper
	 * @param sendRoutingInfoForLCSResponse
	 */
	public SendRoutingInfoForLCSResponseWrapper(MAPDialogLsmWrapper mAPDialog,
			SendRoutingInfoForLCSResponse sendRoutingInfoForLCSResponse) {
		super(mAPDialog, EVENT_TYPE_NAME, sendRoutingInfoForLCSResponse);
	}

	public byte[] getAdditionalVGmlcAddress() {
		return this.wrappedEvent.getAdditionalVGmlcAddress();
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	public byte[] getHGmlcAddress() {
		return this.wrappedEvent.getHGmlcAddress();
	}

	public long getInvokeId() {
		return this.wrappedEvent.getInvokeId();
	}

	public LCSLocationInfo getLCSLocationInfo() {
		return this.wrappedEvent.getLCSLocationInfo();
	}

	public byte[] getPprAddress() {
		return this.wrappedEvent.getPprAddress();
	}

	public SubscriberIdentity getTargetMS() {
		return this.wrappedEvent.getTargetMS();
	}

	public byte[] getVgmlcAddress() {
		return this.wrappedEvent.getVgmlcAddress();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SendRoutingInfoForLCSResponseWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
