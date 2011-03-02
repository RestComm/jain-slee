/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.mediacontrol.wrapper.networkconnection;

import javax.media.mscontrol.networkconnection.SdpPortManager;
import javax.media.mscontrol.networkconnection.SdpPortManagerEvent;

import org.mobicents.slee.resource.mediacontrol.wrapper.ResourceEventWrapper;

/**
 * @author baranowb
 * 
 */
public class SdpPortManagerEventWrapper extends ResourceEventWrapper implements SdpPortManagerEvent {
	public static final String OFFER_GENERATED = "javax.media.mscontrol.networkconnection.SdpPortManagerEvent.OFFER_GENERATED";
	public static final String ANSWER_GENERATED = "javax.media.mscontrol.networkconnection.SdpPortManagerEvent.ANSWER_GENERATED";
	public static final String ANSWER_PROCESSED = "javax.media.mscontrol.networkconnection.SdpPortManagerEvent.ANSWER_PROCESSED";
	public static final String NETWORK_STREAM_FAILURE = "javax.media.mscontrol.networkconnection.SdpPortManagerEvent.NETWORK_STREAM_FAILURE";
	private SdpPortManagerWrapper sdpPortManagerWrapper;

	/**
	 * @param resourceEvent
	 */
	public SdpPortManagerEventWrapper(SdpPortManagerEvent resourceEvent, SdpPortManagerWrapper sdpPortManagerWrapper) {
		super(resourceEvent);
		this.sdpPortManagerWrapper = sdpPortManagerWrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManagerEvent#getMediaServerSdp
	 * ()
	 */
	
	public byte[] getMediaServerSdp() {

		return ((SdpPortManagerEvent) super.resourceEvent).getMediaServerSdp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#getSource()
	 */
	
	public SdpPortManager getSource() {
		return this.sdpPortManagerWrapper;
	}
}
