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

import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.networkconnection.CodecPolicy;
import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.media.mscontrol.networkconnection.SdpPortManager;
import javax.media.mscontrol.networkconnection.SdpPortManagerEvent;
import javax.media.mscontrol.networkconnection.SdpPortManagerException;

import org.mobicents.slee.resource.mediacontrol.MsResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;

/**
 * @author baranowb
 * 
 */
public class SdpPortManagerWrapper implements SdpPortManager {

	private final SdpPortManager wrappedSDPPortManager;
	private final NetworkConnectionWrapper networkConnection;
	private final MediaSessionWrapper mediaSession;
	private final MsResourceAdaptor ra;

	/**
	 * @param sdpPortManager
	 * @param networkConnection
	 * @param mediaSession
	 */
	public SdpPortManagerWrapper(SdpPortManager sdpPortManager, NetworkConnectionWrapper networkConnection, MediaSessionWrapper mediaSession,
			MsResourceAdaptor ra) {
		super();
		this.wrappedSDPPortManager = sdpPortManager;
		this.networkConnection = networkConnection;
		this.mediaSession = mediaSession;
		this.ra = ra;
		this.wrappedSDPPortManager.addListener(new SdpPortManagerListener(this)); // possibly
																					// can
																					// be
																					// localy
																					// implemented,
																					// but...
																					// does
																					// not
																					// matter
																					// really?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.Resource#getContainer()
	 */
	
	public NetworkConnection getContainer() {
		return this.networkConnection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEventNotifier#getMediaSession()
	 */
	
	public MediaSession getMediaSession() {
		return this.mediaSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#generateSdpOffer()
	 */
	
	public void generateSdpOffer() throws SdpPortManagerException {
		this.wrappedSDPPortManager.generateSdpOffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#getCodecPolicy()
	 */
	
	public CodecPolicy getCodecPolicy() {
		return wrappedSDPPortManager.getCodecPolicy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.networkconnection.SdpPortManager#
	 * getMediaServerSessionDescription()
	 */
	
	public byte[] getMediaServerSessionDescription() throws SdpPortManagerException {
		return wrappedSDPPortManager.getMediaServerSessionDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.networkconnection.SdpPortManager#
	 * getUserAgentSessionDescription()
	 */
	
	public byte[] getUserAgentSessionDescription() throws SdpPortManagerException {
		return wrappedSDPPortManager.getUserAgentSessionDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#processSdpAnswer
	 * (byte[])
	 */
	
	public void processSdpAnswer(byte[] arg0) throws SdpPortManagerException {
		wrappedSDPPortManager.processSdpAnswer(arg0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#processSdpOffer
	 * (byte[])
	 */
	
	public void processSdpOffer(byte[] arg0) throws SdpPortManagerException {
		wrappedSDPPortManager.processSdpOffer(arg0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#rejectSdpOffer()
	 */
	
	public void rejectSdpOffer() throws SdpPortManagerException {
		wrappedSDPPortManager.rejectSdpOffer();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#setCodecPolicy
	 * (javax.media.mscontrol.networkconnection.CodecPolicy)
	 */
	
	public void setCodecPolicy(CodecPolicy arg0) throws SdpPortManagerException {
		wrappedSDPPortManager.setCodecPolicy(arg0);

	}

	// --------------------------- not allowed ---------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#removeListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	
	public void removeListener(MediaEventListener<SdpPortManagerEvent> arg0) {
		throw new SecurityException("SBB can't register listener");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#addListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	
	public void addListener(MediaEventListener<SdpPortManagerEvent> arg0) {
		throw new SecurityException("SBB can't register listener");

	}

	private class SdpPortManagerListener implements MediaEventListener<SdpPortManagerEvent> {
		private final SdpPortManagerWrapper wrapper;

		/**
		 * @param wrapper
		 */
		public SdpPortManagerListener(SdpPortManagerWrapper wrapper) {
			super();
			this.wrapper = wrapper;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.media.mscontrol.MediaEventListener#onEvent(javax.media.mscontrol
		 * .MediaEvent)
		 */
		
		public void onEvent(SdpPortManagerEvent event) {
			SdpPortManagerEventWrapper localEvent = new SdpPortManagerEventWrapper(event, wrapper);
			if (event.getEventType().equals(SdpPortManagerEvent.OFFER_GENERATED)) {
				ra.fireEvent(localEvent.OFFER_GENERATED, networkConnection.getActivityHandle(), localEvent);
			} else if (event.getEventType().equals(SdpPortManagerEvent.ANSWER_GENERATED)) {
				ra.fireEvent(localEvent.ANSWER_GENERATED, networkConnection.getActivityHandle(), localEvent);
			} else if (event.getEventType().equals(SdpPortManagerEvent.ANSWER_PROCESSED)) {
				ra.fireEvent(localEvent.ANSWER_PROCESSED, networkConnection.getActivityHandle(), localEvent);
			} else if (event.getEventType().equals(SdpPortManagerEvent.NETWORK_STREAM_FAILURE)) {
				try{
					ra.fireEvent(localEvent.NETWORK_STREAM_FAILURE, networkConnection.getActivityHandle(), localEvent);
				}finally
				{
					ra.endActivity(networkConnection.getActivityHandle());// this is
																		// actually
																		// bad?
				}
			}

		}

	}

}
