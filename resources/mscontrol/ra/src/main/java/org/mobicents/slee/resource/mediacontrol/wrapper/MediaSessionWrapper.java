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
package org.mobicents.slee.resource.mediacontrol.wrapper;

import java.util.Iterator;

import javax.media.mscontrol.Configuration;
import javax.media.mscontrol.MediaConfig;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mixer.MediaMixer;
import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.media.mscontrol.vxml.VxmlDialog;

import org.mobicents.slee.resource.mediacontrol.MsActivity;
import org.mobicents.slee.resource.mediacontrol.MsActivityHandle;
import org.mobicents.slee.resource.mediacontrol.MsResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.wrapper.mediagroup.MediaGroupWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.mixer.MediaMixerWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.networkconnection.NetworkConnectionWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.vxml.VXMLDialogWrapper;

/**
 * @author baranowb
 * 
 */
public class MediaSessionWrapper extends MediaObjectWrapper implements MediaSession, MsActivity {

	protected MsActivityHandle handle = new MsActivityHandle(this);
	protected MediaSession wrappedSession;

	/**
	 * @param wrappedObject
	 */
	public MediaSessionWrapper(MediaSession wrappedSession, MsResourceAdaptor ra) {
		super(wrappedSession, ra);
		this.wrappedSession = wrappedSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaObject#release()
	 */
	
	public void release() {
		super.release();
		super.ra.endActivity(getActivityHandle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaSession#createMediaGroup(javax.media.mscontrol
	 * .Configuration)
	 */
	
	public MediaGroup createMediaGroup(Configuration<MediaGroup> config) throws MsControlException {
		MediaGroup realMediaGroup = this.wrappedSession.createMediaGroup(config);
		return createWrapper(realMediaGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaSession#createMediaGroup(javax.media.mscontrol
	 * .Configuration, javax.media.mscontrol.Parameters)
	 */
	
	public MediaGroup createMediaGroup(Configuration<MediaGroup> config, Parameters parameters) throws MsControlException {
		MediaGroup realMediaGroup = this.wrappedSession.createMediaGroup(config, parameters);
		return createWrapper(realMediaGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaSession#createMediaGroup(javax.media.mscontrol
	 * .MediaConfig, javax.media.mscontrol.Parameters)
	 */
	
	public MediaGroup createMediaGroup(MediaConfig config, Parameters parameters) throws MsControlException {
		MediaGroup realMediaGroup = this.wrappedSession.createMediaGroup(config, parameters);
		return createWrapper(realMediaGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaSession#createMediaMixer(javax.media.mscontrol
	 * .Configuration)
	 */
	
	public MediaMixer createMediaMixer(Configuration<MediaMixer> config) throws MsControlException {
		MediaMixer realMixer = this.wrappedSession.createMediaMixer(config);
		return createWrapper(realMixer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaSession#createMediaMixer(javax.media.mscontrol
	 * .Configuration, javax.media.mscontrol.Parameters)
	 */
	
	public MediaMixer createMediaMixer(Configuration<MediaMixer> config, Parameters parameters) throws MsControlException {
		MediaMixer realMixer = this.wrappedSession.createMediaMixer(config, parameters);
		return createWrapper(realMixer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaSession#createMediaMixer(javax.media.mscontrol
	 * .MediaConfig, javax.media.mscontrol.Parameters)
	 */
	
	public MediaMixer createMediaMixer(MediaConfig config, Parameters parameters) throws MsControlException {
		MediaMixer realMixer = this.wrappedSession.createMediaMixer(config, parameters);
		return createWrapper(realMixer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaSession#createNetworkConnection(javax.media
	 * .mscontrol.Configuration)
	 */
	
	public NetworkConnection createNetworkConnection(Configuration<NetworkConnection> config) throws MsControlException {
		NetworkConnection realNetworkConnection = this.wrappedSession.createNetworkConnection(config);
		return createWrapper(realNetworkConnection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaSession#createNetworkConnection(javax.media
	 * .mscontrol.Configuration, javax.media.mscontrol.Parameters)
	 */
	
	public NetworkConnection createNetworkConnection(Configuration<NetworkConnection> config, Parameters parameters) throws MsControlException {
		NetworkConnection realNetworkConnection = this.wrappedSession.createNetworkConnection(config, parameters);
		return createWrapper(realNetworkConnection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaSession#createNetworkConnection(javax.media
	 * .mscontrol.MediaConfig, javax.media.mscontrol.Parameters)
	 */
	
	public NetworkConnection createNetworkConnection(MediaConfig config, Parameters parameters) throws MsControlException {
		NetworkConnection realNetworkConnection = this.wrappedSession.createNetworkConnection(config, parameters);
		return createWrapper(realNetworkConnection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaSession#createVxmlDialog(javax.media.mscontrol
	 * .Parameters)
	 */
	
	public VxmlDialog createVxmlDialog(Parameters arg0) throws MsControlException {
		
		VxmlDialog realDialog = this.wrappedSession.createVxmlDialog(arg0);
		return createWrapper(realDialog);
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaSession#getAttribute(java.lang.String)
	 */
	
	public Object getAttribute(String arg0) {
		return this.wrappedSession.getAttribute(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaSession#getAttributeNames()
	 */
	
	public Iterator<String> getAttributeNames() {
		return this.wrappedSession.getAttributeNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaSession#removeAttribute(java.lang.String)
	 */
	
	public void removeAttribute(String arg0) {
		this.wrappedSession.removeAttribute(arg0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaSession#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	
	public void setAttribute(String arg0, Object arg1) {
		this.wrappedSession.setAttribute(arg0, arg1);
	}

	// --------------------------- JSLEE specific ------------------

	/**
	 * @return
	 */
	public MsActivityHandle getActivityHandle() {
		return this.handle;
	}

	// - some helper methods to keep same code in one place.

	protected MediaGroupWrapper createWrapper(MediaGroup realMediaGroup) throws MsControlException {
		MediaGroupWrapper wrapper = new MediaGroupWrapper(realMediaGroup, this, super.ra);
		super.realToWrapperMap.put(realMediaGroup, wrapper);
		try {
			this.ra.startActivity(wrapper);
		} catch (Exception e) {
			wrapper.release();
			throw new MsControlException("Failed to create MsControl resource.", e);
		}
		return wrapper;
	}

	/**
	 * @param realNetworkConnection
	 * @return
	 * @throws MsControlException
	 */
	protected NetworkConnection createWrapper(NetworkConnection realNetworkConnection) throws MsControlException {
		NetworkConnectionWrapper wrapper = new NetworkConnectionWrapper(realNetworkConnection, this, super.ra);
		super.realToWrapperMap.put(realNetworkConnection, wrapper);
		try {
			this.ra.startActivity(wrapper);
		} catch (Exception e) {
			wrapper.release();
			throw new MsControlException("Failed to create MsControl resource.", e);
		}
		return wrapper;
	}

	/**
	 * @param realNetworkConnection
	 * @return
	 * @throws MsControlException 
	 */
	protected MediaMixerWrapper createWrapper(MediaMixer realMediaMixer) throws MsControlException {
		MediaMixerWrapper wrapper = new MediaMixerWrapper(realMediaMixer, this, super.ra);															
		super.realToWrapperMap.put(realMediaMixer, wrapper);
		try {
			this.ra.startActivity(wrapper);
		} catch (Exception e) {
			wrapper.release();
			throw new MsControlException("Failed to create MsControl resource.", e);
		}
		return wrapper;
	}
	/**
	 * @param realDialog
	 * @return
	 * @throws MsControlException 
	 */
	protected VxmlDialog createWrapper(VxmlDialog realDialog) throws MsControlException {
		VXMLDialogWrapper  wrapper = new VXMLDialogWrapper(realDialog, this, super.ra);															
		super.realToWrapperMap.put(realDialog, wrapper);
		try {
			this.ra.startActivity(wrapper);
		} catch (Exception e) {
			wrapper.release();
			throw new MsControlException("Failed to create MsControl resource.", e);
		}
		return wrapper;
	}
}
