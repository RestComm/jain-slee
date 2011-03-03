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

import javax.media.mscontrol.Configuration;
import javax.media.mscontrol.MediaConfig;
import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.join.Joinable;
import javax.media.mscontrol.mixer.MediaMixer;
import javax.media.mscontrol.mixer.MixerAdapter;
import javax.media.mscontrol.mixer.MixerEvent;
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
public class MediaMixerWrapper extends JoinableContainerWrapper implements MediaMixer,MsActivity {
	
	protected MsActivityHandle handle = new MsActivityHandle(this);
	protected final MediaMixer wrappedMediaMixer;
	protected final WrapperAllocationEventListener mmwAllocationEventListener = new WrapperAllocationEventListener(this,handle);
	protected final MMWMediaEventListener nnwMediaEventListener = new MMWMediaEventListener(this);
	/**
	 * @param wrappedObject
	 * @param ra
	 */
	public MediaMixerWrapper(MediaMixer wrappedObject, MediaSessionWrapper mediaSession, MsResourceAdaptor ra) {
		super(wrappedObject, (MediaSessionWrapper) mediaSession, ra);
		this.wrappedMediaMixer = wrappedObject;
		this.wrappedMediaMixer.addListener(this.mmwAllocationEventListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.ResourceContainer#confirm()
	 */
	
	public void confirm() throws MsControlException {
		this.wrappedMediaMixer.confirm();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.ResourceContainer#getConfig()
	 */
	
	public MediaConfig getConfig() {
		return this.wrappedMediaMixer.getConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.ResourceContainer#getResource(java.lang
	 * .Class)
	 */
	
	public <R> R getResource(Class<R> resourceClass) throws MsControlException {
		// TODO: 1. add check for what??
		// TODO: 2. return MixerAdapter... ?
		return this.wrappedMediaMixer.getResource(resourceClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.ResourceContainer#triggerAction(javax.
	 * media.mscontrol.resource.Action)
	 */
	
	public void triggerAction(Action action) {
		this.wrappedMediaMixer.triggerAction(action);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaObject#release()
	 */
	
	public void release() {
		// call super, to release joined,inform MediaSessionWrapper, ...etc
		this.wrappedMediaMixer.removeListener(this.mmwAllocationEventListener);
		super.release();
		// dont issue term, since for now its not activity. Might be kind of
		// tricky,
		super.ra.endActivity(getActivityHandle());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.mixer.MediaMixer#createMixerAdapter(javax.media
	 * .mscontrol.Configuration)
	 */
	
	public MixerAdapter createMixerAdapter(Configuration<MixerAdapter> config) throws MsControlException {
		MixerAdapter realMixedAdapter = this.wrappedMediaMixer.createMixerAdapter(config);
		return createWrapper(realMixedAdapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.mixer.MediaMixer#createMixerAdapter(javax.media
	 * .mscontrol.Configuration, javax.media.mscontrol.Parameters)
	 */
	
	public MixerAdapter createMixerAdapter(Configuration<MixerAdapter> config, Parameters parms) throws MsControlException {
		MixerAdapter realMixedAdapter = this.wrappedMediaMixer.createMixerAdapter(config, parms);
		return createWrapper(realMixedAdapter);
	}

	
	protected MsActivityHandle getEventHandle() {
		//ql, we are no activity :)
		return this.getActivityHandle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.mixer.MediaMixer#createMixerAdapter(javax.media
	 * .mscontrol.MediaConfig, javax.media.mscontrol.Parameters)
	 */
	
	public MixerAdapter createMixerAdapter(MediaConfig config, Parameters parms) throws MsControlException {
		MixerAdapter realMixedAdapter = this.wrappedMediaMixer.createMixerAdapter(config, parms);
		return createWrapper(realMixedAdapter);
	}
	
	
	public MsActivityHandle getActivityHandle() {
		return this.handle;
	}
	// ----------------------- private -------------------

	/**
	 * @param realMixedAdapter
	 * @return
	 */
	private MixerAdapter createWrapper(MixerAdapter realMixedAdapter) {
		MixerAdapterWrapper wrapper = new MixerAdapterWrapper(realMixedAdapter, this, this.mediaSession, super.ra); // this
																									// will
																									// throw
																									// NPE
																									// :)
		super.realToWrapperMap.put(realMixedAdapter, wrapper);
		// add activty?
		return wrapper;
	}

	// --------------------------- not allowed ---------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#addListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	
	public void addListener(MediaEventListener<MixerEvent> arg0) {
		throw new SecurityException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#removeListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	
	public void removeListener(MediaEventListener<MixerEvent> arg0) {
		throw new SecurityException();

	}

	
	public void addListener(AllocationEventListener arg0) {
		throw new SecurityException();

	}

	
	public void removeListener(AllocationEventListener arg0) {
		throw new SecurityException();

	}

	private class MMWMediaEventListener implements MediaEventListener<MixerEvent>
	{

		private final MediaMixerWrapper mediaMixerWrapper;
		/**
		 * @param mediaMixerWrapper
		 */
		public MMWMediaEventListener(MediaMixerWrapper mediaMixerWrapper) {
			this.mediaMixerWrapper = mediaMixerWrapper;
		}

		/* (non-Javadoc)
		 * @see javax.media.mscontrol.MediaEventListener#onEvent(javax.media.mscontrol.MediaEvent)
		 */
		
		public void onEvent(MixerEvent mixerEvent) {
			//now we need to calculate.
			//get real joinables and cross check against map
			Joinable[] actievInputs = mixerEvent.getActiveInputs();
			//this may fail
			for(int index = 0; index< actievInputs.length;index++)
			{
				//FIXME: this may not be correct in case impl has some internal booby traps... 
				//FIXME: are JoinableStreams to be checked here as well?
				if(joinees.containsKey(actievInputs[index]))
				{
					actievInputs[index] = joinees.get(actievInputs[index]);
				}else
				{
					logger.warning("Did not find wrapper for active joinable: "+actievInputs[index]);
					actievInputs[index] = null;
				}
			}
			MixerEventWrapper localEvent = new MixerEventWrapper(mixerEvent, mediaMixerWrapper, actievInputs);
			
			if(mixerEvent.getEventType().equals(MixerEvent.MOST_ACTIVE_INPUT_CHANGED))
			{
				ra.fireEvent(MixerEventWrapper.MOST_ACTIVE_INPUTS_CHANED, getActivityHandle(), localEvent);
			}else if(mixerEvent.getEventType().equals(MixerEvent.ACTIVE_INPUTS_CHANGED))
			{
				ra.fireEvent(MixerEventWrapper.ACTIVE_INPUTS_CHANED, getActivityHandle(), localEvent);
			}else
			{
				logger.warning("Received wrong mixer event type: "+mixerEvent.getEventType());
			}
			
		}
		
	}
	
	
}
