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
package org.mobicents.slee.resource.mediacontrol.wrapper.mixer;

import javax.media.mscontrol.MediaConfig;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.mixer.MixerAdapter;
import javax.media.mscontrol.resource.Action;
import javax.media.mscontrol.resource.AllocationEventListener;

import org.mobicents.slee.resource.mediacontrol.MsActivityHandle;
import org.mobicents.slee.resource.mediacontrol.MsResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.join.JoinableContainerWrapper;

/**
 * @author baranowb
 * 
 */
public class MixerAdapterWrapper extends JoinableContainerWrapper implements MixerAdapter {

	private final MixerAdapter wrappedMixerAdapter;
	private final MediaMixerWrapper mediaMixer;
	private final WrapperAllocationEventListener mmaAllocationEventListener;
	/**
	 * @param wrappedObject
	 * @param mediaSession
	 * @param ra
	 */
	public MixerAdapterWrapper(MixerAdapter wrappedObject, MediaMixerWrapper mediaMixer, MediaSessionWrapper mediaSession, MsResourceAdaptor ra) {
		super(wrappedObject, mediaSession, ra);// media session wrapper passed
												// as arg to avoid cast...
		if (mediaMixer == null) {
			throw new IllegalArgumentException("MediaMixer must not be null.");
		}
		this.mediaMixer = mediaMixer;
		this.wrappedMixerAdapter = wrappedObject;
		this.mmaAllocationEventListener = new WrapperAllocationEventListener(this, this.mediaMixer.getEventHandle())
		{
			public void onFailure()
			{
				//release mixer adapter, rather than terminate activity ?
				release();
			}
		};
		this.wrappedMixerAdapter.addListener(this.mmaAllocationEventListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.ResourceContainer#confirm()
	 */
	
	public void confirm() throws MsControlException {
		this.wrappedMixerAdapter.confirm();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.ResourceContainer#getConfig()
	 */
	
	public MediaConfig getConfig() {
		return this.wrappedMixerAdapter.getConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.ResourceContainer#getResource(java.lang
	 * .Class)
	 */
	
	public <R> R getResource(Class<R> filter) throws MsControlException {
		// once again this method...
		return this.wrappedMixerAdapter.getResource(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.ResourceContainer#triggerAction(javax.
	 * media.mscontrol.resource.Action)
	 */
	
	public void triggerAction(Action action) {
		this.wrappedMixerAdapter.triggerAction(action);

	}

	// --------------------------- not allowed ---------------------

	
	public void addListener(AllocationEventListener arg0) {
		throw new SecurityException();

	}

	
	public void removeListener(AllocationEventListener arg0) {
		throw new SecurityException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.resource.mediacontrol.wrapper.join.
	 * JoinableContainerWrapper#getEventHandle()
	 */
	
	protected MsActivityHandle getEventHandle() {
		return this.mediaMixer.getEventHandle();
	}

}
