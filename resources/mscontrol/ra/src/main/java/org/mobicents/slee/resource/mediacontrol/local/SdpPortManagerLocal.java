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
package org.mobicents.slee.resource.mediacontrol.local;

import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.networkconnection.CodecPolicy;
import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.media.mscontrol.networkconnection.SdpPortManager;
import javax.media.mscontrol.networkconnection.SdpPortManagerEvent;
import javax.media.mscontrol.networkconnection.SdpPortManagerException;

import org.mobicents.slee.resource.mediacontrol.NetworkConnectionActivityHandle;

/**
 * @author baranowb
 * 
 */
public class SdpPortManagerLocal implements SdpPortManager {

	private SdpPortManager sdpPortManager;
	private NetworkConnectionLocal networkConnection;
	private MediaSessionLocal mediaSession;


	/**
	 * @param sdpPortManager
	 * @param networkConnection
	 * @param mediaSession
	 */
	public SdpPortManagerLocal(SdpPortManager sdpPortManager, NetworkConnectionLocal networkConnection, MediaSessionLocal mediaSession) {
		super();
		this.sdpPortManager = sdpPortManager;
		this.networkConnection = networkConnection;
		this.mediaSession = mediaSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.Resource#getContainer()
	 */
	@Override
	public NetworkConnection getContainer() {
		return networkConnection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#addListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	@Override
	public void addListener(MediaEventListener<SdpPortManagerEvent> arg0) {
		throw new SecurityException("SBB can't register listener");

	}
	
	public void addListenerLocal(NetworkConnectionActivityHandle listener)
	{
		sdpPortManager.addListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEventNotifier#getMediaSession()
	 */
	@Override
	public MediaSession getMediaSession() {
		return mediaSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#removeListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	@Override
	public void removeListener(MediaEventListener<SdpPortManagerEvent> arg0) {
		throw new SecurityException("SBB can't register listener");

	}
	
	public void removeListenerLocal(NetworkConnectionActivityHandle listener)
	{
		sdpPortManager.removeListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#generateSdpOffer()
	 */
	@Override
	public void generateSdpOffer() throws SdpPortManagerException {
		this.sdpPortManager.generateSdpOffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#getCodecPolicy()
	 */
	@Override
	public CodecPolicy getCodecPolicy() {
		return sdpPortManager.getCodecPolicy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.networkconnection.SdpPortManager#
	 * getMediaServerSessionDescription()
	 */
	@Override
	public byte[] getMediaServerSessionDescription() throws SdpPortManagerException {
		return sdpPortManager.getMediaServerSessionDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.networkconnection.SdpPortManager#
	 * getUserAgentSessionDescription()
	 */
	@Override
	public byte[] getUserAgentSessionDescription() throws SdpPortManagerException {
		return sdpPortManager.getUserAgentSessionDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#processSdpAnswer
	 * (byte[])
	 */
	@Override
	public void processSdpAnswer(byte[] arg0) throws SdpPortManagerException {
		sdpPortManager.processSdpAnswer(arg0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#processSdpOffer
	 * (byte[])
	 */
	@Override
	public void processSdpOffer(byte[] arg0) throws SdpPortManagerException {
		sdpPortManager.processSdpOffer(arg0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#rejectSdpOffer()
	 */
	@Override
	public void rejectSdpOffer() throws SdpPortManagerException {
		sdpPortManager.rejectSdpOffer();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.SdpPortManager#setCodecPolicy
	 * (javax.media.mscontrol.networkconnection.CodecPolicy)
	 */
	@Override
	public void setCodecPolicy(CodecPolicy arg0) throws SdpPortManagerException {
		sdpPortManager.setCodecPolicy(arg0);

	}

}
