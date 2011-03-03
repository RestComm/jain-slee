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

import javax.media.mscontrol.MediaConfig;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.media.mscontrol.networkconnection.SdpPortManager;
import javax.media.mscontrol.resource.Action;
import javax.media.mscontrol.resource.AllocationEventListener;

import org.mobicents.slee.resource.mediacontrol.MsActivity;
import org.mobicents.slee.resource.mediacontrol.MsActivityHandle;
import org.mobicents.slee.resource.mediacontrol.MsResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.join.JoinableContainerWrapper;

/**
 * @author baranowb
 * 
 */
public class NetworkConnectionWrapper extends JoinableContainerWrapper implements NetworkConnection, MsActivity {

	protected final MsActivityHandle handle = new MsActivityHandle(this);
	protected final NetworkConnection wrappedNetworkConnection;
	protected final WrapperAllocationEventListener ncwAllocationEventListener = new WrapperAllocationEventListener(this,this.handle);
	protected SdpPortManagerWrapper sdpPortManager; // not final, since it may
													// not awlays be available.

	/**
	 * @param wrappedObject
	 * @param ra
	 */
	public NetworkConnectionWrapper(NetworkConnection wrappedObject, MediaSessionWrapper mediaSession, MsResourceAdaptor ra) {
		super(wrappedObject, mediaSession, ra);
		if (mediaSession == null) {
			throw new IllegalArgumentException("MediaSession must not be null.");
		}

		this.wrappedNetworkConnection = wrappedObject;
		this.wrappedNetworkConnection.addListener(this.ncwAllocationEventListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.ResourceContainer#confirm()
	 */
	
	public void confirm() throws MsControlException {
		this.wrappedNetworkConnection.confirm();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.ResourceContainer#getConfig()
	 */
	
	public MediaConfig getConfig() {
		return this.wrappedNetworkConnection.getConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.ResourceContainer#getResource(java.lang
	 * .Class)
	 */
	
	public <R> R getResource(Class<R> filter) throws MsControlException {
		R filteredResource;
		try {
			filteredResource = filter.cast(sdpPortManager);
			return filteredResource;
		} catch (ClassCastException cce) {

		}
		// TODO?
		return this.wrappedNetworkConnection.getResource(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.ResourceContainer#triggerAction(javax.
	 * media.mscontrol.resource.Action)
	 */
	
	public void triggerAction(Action action) {
		this.wrappedNetworkConnection.triggerAction(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaObject#release()
	 */
	
	public void release() {
		// remove lst.
		this.wrappedNetworkConnection.removeListener(this.ncwAllocationEventListener);
		// call super, to release joined,inform MediaSessionWrapper, ...etc
		super.release();
		// issue term
		super.ra.endActivity(getActivityHandle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.networkconnection.NetworkConnection#getSdpPortManager
	 * ()
	 */
	
	public SdpPortManager getSdpPortManager() throws MsControlException {
		if (this.sdpPortManager == null) {
			SdpPortManager realSdpPortManager = this.wrappedNetworkConnection.getSdpPortManager();
			this.sdpPortManager = new SdpPortManagerWrapper(realSdpPortManager, this, super.mediaSession, super.ra);
		}
		return this.sdpPortManager;
	}

	// --------------------------- JSLEE specific ------------------

	/**
	 * @return
	 */
	public MsActivityHandle getActivityHandle() {
		return this.handle;
	}

	
	protected MsActivityHandle getEventHandle() {
		return this.getActivityHandle();
	}

	// --------------------------- not allowed ---------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.AllocationEventNotifier#addListener(javax
	 * .media.mscontrol.resource.AllocationEventListener)
	 */
	
	public void addListener(AllocationEventListener arg0) {
		throw new SecurityException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.AllocationEventNotifier#removeListener
	 * (javax.media.mscontrol.resource.AllocationEventListener)
	 */
	
	public void removeListener(AllocationEventListener arg0) {
		throw new SecurityException();
	}

}
