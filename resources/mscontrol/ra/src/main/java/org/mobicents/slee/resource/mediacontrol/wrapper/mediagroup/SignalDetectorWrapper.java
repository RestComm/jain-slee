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

import javax.media.mscontrol.EventType;
import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.Parameter;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mediagroup.signals.SignalDetector;
import javax.media.mscontrol.mediagroup.signals.SignalDetectorEvent;
import javax.media.mscontrol.resource.RTC;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.Wrapper;

/**
 * @author baranowb
 * 
 */
public class SignalDetectorWrapper implements SignalDetector, Wrapper {

	protected final MediaSessionWrapper mediaSession;
	protected final MediaGroupWrapper mediaGroup;
	protected final SignalDetector wrappedSignalDetector;
	protected final Tracer logger;
	protected final SignalDetectorEventListener sdEventListener = new SignalDetectorEventListener(this);

	/**
	 * @param mediaSession
	 * @param mediaGroup
	 * @param wrappedSignalDetector
	 */
	public SignalDetectorWrapper(SignalDetector wrappedSignalDetector, MediaGroupWrapper mediaGroup, MediaSessionWrapper mediaSession) {
		super();
		if (wrappedSignalDetector == null) {
			throw new IllegalArgumentException("SignalDetector must not be null.");
		}
		if (mediaGroup == null) {
			throw new IllegalArgumentException("MediaGroup must not be null.");
		}
		if (mediaSession == null) {
			throw new IllegalArgumentException("MediaSession must not be null.");
		}
		this.mediaSession = mediaSession;
		this.mediaGroup = mediaGroup;
		this.wrappedSignalDetector = wrappedSignalDetector;
		this.logger = this.mediaGroup.getRA().getTracer(this);
		// TODO: or add in detect and remove in stop?
		this.wrappedSignalDetector.addListener(this.sdEventListener);
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
	 * javax.media.mscontrol.mediagroup.signals.SignalDetector#flushBuffer()
	 */
	
	public void flushBuffer() throws MsControlException {
		wrappedSignalDetector.flushBuffer();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.mediagroup.signals.SignalDetector#receiveSignals
	 * (int, javax.media.mscontrol.Parameter[],
	 * javax.media.mscontrol.resource.RTC[], javax.media.mscontrol.Parameters)
	 */
	
	public void receiveSignals(int numSignals, Parameter[] patternLabels, RTC[] rtc, Parameters optargs) throws MsControlException {
		wrappedSignalDetector.receiveSignals(numSignals, patternLabels, rtc, optargs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.signals.SignalDetector#stop()
	 */
	
	public void stop() {
		wrappedSignalDetector.stop();

	}

	// --------------------------- not allowed ---------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#removeListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	
	public void removeListener(MediaEventListener<SignalDetectorEvent> arg0) {
		throw new SecurityException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#addListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	
	public void addListener(MediaEventListener<SignalDetectorEvent> arg0) {
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
		return this.wrappedSignalDetector;
	}

	private class SignalDetectorEventListener implements MediaEventListener<SignalDetectorEvent> {
		private final SignalDetectorWrapper wrapper;

		/**
		 * @param source
		 */
		public SignalDetectorEventListener(SignalDetectorWrapper wrapper) {
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
		
		public void onEvent(SignalDetectorEvent realEvent) {
			if (logger.isFineEnabled()) {
				logger.fine("Signal Deteted: " + realEvent.getEventType() + ", on: " + wrapper);
			}
			String eventName = null;
			EventType type = realEvent.getEventType();
			if (type.equals(SignalDetectorEvent.FLUSH_BUFFER_COMPLETED)) {
				eventName = SignalDetectorEventWrapper.FLUSH_BUFFER_COMPLETED;
			} else if (type.equals(SignalDetectorEvent.OVERFLOWED)) {
				eventName = SignalDetectorEventWrapper.OVERFLOWED;
			} else if (type.equals(SignalDetectorEvent.PATTERN_MATCHED)) {
				eventName = SignalDetectorEventWrapper.PATTERN_MATCHED;
			} else if (type.equals(SignalDetectorEvent.RECEIVE_SIGNALS_COMPLETED)) {
				eventName = SignalDetectorEventWrapper.RECEIVE_SIGNALS_COMPLETED;
			} else if (type.equals(SignalDetectorEvent.SIGNAL_DETECTED)) {
				eventName = SignalDetectorEventWrapper.SIGNAL_DETECTED;
			} else {
				if (logger.isWarningEnabled()) {
					logger.warning("SignalDetected(unknown!): " + type + ", on: " + wrapper);
				}
				return;
			}

			SignalDetectorEventWrapper localEvent = new SignalDetectorEventWrapper(realEvent, wrapper);
			mediaGroup.getRA().fireEvent(eventName, mediaGroup.getEventHandle(), localEvent);

		}

	}
}
