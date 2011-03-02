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
package org.mobicents.slee.resource.mediacontrol.wrapper.mediagroup;

import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.Value;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mediagroup.signals.SignalGenerator;
import javax.media.mscontrol.mediagroup.signals.SignalGeneratorEvent;
import javax.media.mscontrol.resource.RTC;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.Wrapper;

/**
 * @author baranowb
 * 
 */
public class SignalGeneratorWrapper implements SignalGenerator, Wrapper {

	protected final MediaSessionWrapper mediaSession;
	protected final MediaGroupWrapper mediaGroup;
	protected final SignalGenerator wrappedSignalGenerator;
	protected final Tracer logger;
	protected final SignalGeneratorEventListener sgEventListener = new SignalGeneratorEventListener(this);
	/**
	 * @param mediaSession
	 * @param mediaGroup
	 * @param wrappedSignalGenerator
	 */
	public SignalGeneratorWrapper(SignalGenerator wrappedSignalGenerator, MediaGroupWrapper mediaGroup, MediaSessionWrapper mediaSession) {
		super();
		if (wrappedSignalGenerator == null) {
			throw new IllegalArgumentException("SignalGenerator must not be null.");
		}
		if (mediaGroup == null) {
			throw new IllegalArgumentException("MediaGroup must not be null.");
		}
		if (mediaSession == null) {
			throw new IllegalArgumentException("MediaSession must not be null.");
		}
		this.mediaSession = mediaSession;
		this.mediaGroup = mediaGroup;
		this.wrappedSignalGenerator = wrappedSignalGenerator;
		this.logger = mediaGroup.getRA().getTracer(this);
		//TODO: or add on emit, and in completed remove ourselves?
		
		this.wrappedSignalGenerator.addListener(this.sgEventListener);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.Resource#getContainer()
	 */
	
	public MediaGroup getContainer() {
		return this.mediaGroup;
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
	 * javax.media.mscontrol.mediagroup.signals.SignalGenerator#emitSignals(
	 * javax.media.mscontrol.Value[], javax.media.mscontrol.resource.RTC[],
	 * javax.media.mscontrol.Parameters)
	 */
	
	public void emitSignals(Value[] signals, RTC[] rtc, Parameters optargs) throws MsControlException {
		this.wrappedSignalGenerator.emitSignals(signals, rtc, optargs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.mediagroup.signals.SignalGenerator#emitSignals(
	 * java.lang.String, javax.media.mscontrol.resource.RTC[],
	 * javax.media.mscontrol.Parameters)
	 */
	
	public void emitSignals(String signals, RTC[] rtc, Parameters optargs) throws MsControlException {
		this.wrappedSignalGenerator.emitSignals(signals, rtc, optargs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.signals.SignalGenerator#stop()
	 */
	
	public void stop() {
		this.wrappedSignalGenerator.stop();

	}

	// --------------------------- private -------------------------
	
	protected class SignalGeneratorEventListener implements MediaEventListener<SignalGeneratorEvent>
	{
		private final SignalGeneratorWrapper wrapper;
		
		
		
		/**
		 * @param wrapper
		 */
		public SignalGeneratorEventListener(SignalGeneratorWrapper wrapper) {
			super();
			this.wrapper = wrapper;
		}



		/* (non-Javadoc)
		 * @see javax.media.mscontrol.MediaEventListener#onEvent(javax.media.mscontrol.MediaEvent)
		 */
		
		public void onEvent(SignalGeneratorEvent realEvent) {
			if(logger.isFineEnabled())
			{
				logger.fine("Signal, on: " + wrapper);
			}
			if(realEvent.getEventType().equals(SignalGeneratorEvent.EMIT_SIGNALS_COMPLETED))
			{
				SignalGeneratorEventWrapper localEvent = new SignalGeneratorEventWrapper(realEvent, wrapper);
				mediaGroup.getRA().fireEvent(SignalGeneratorEventWrapper.EMIT_SIGNALS_COMPLETED, mediaGroup.getEventHandle(),localEvent);
			}else
			{
				if(logger.isWarningEnabled())
				{
					logger.warning("SignalGenerated(unknown!): " + realEvent.getEventType() + ", on: " + wrapper);
				}
				
			}
		}
		
	}
	
	// --------------------------- not allowed ---------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#addListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	
	public void addListener(MediaEventListener<SignalGeneratorEvent> arg0) {
		throw new SecurityException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#removeListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	
	public void removeListener(MediaEventListener<SignalGeneratorEvent> arg0) {
		throw new SecurityException();
	}

	// ------------------------ private ------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.mediacontrol.wrapper.Wrapper#getWrappedObject
	 * ()
	 */
	
	public Object getWrappedObject() {
		return this.wrappedSignalGenerator;
	}
	

}
