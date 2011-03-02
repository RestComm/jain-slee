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

import javax.media.mscontrol.Value;
import javax.media.mscontrol.mediagroup.signals.SignalDetector;
import javax.media.mscontrol.mediagroup.signals.SignalDetectorEvent;
import javax.media.mscontrol.resource.ResourceEvent;

import org.mobicents.slee.resource.mediacontrol.wrapper.ResourceEventWrapper;

/**
 * @author baranowb
 * 
 */
public class SignalDetectorEventWrapper extends ResourceEventWrapper implements SignalDetectorEvent {

	public static final String FLUSH_BUFFER_COMPLETED = "javax.media.mscontrol.mediagroup.signals.SignalDetectorEvent.FLUSH_BUFFER_COMPLETED";
	public static final String OVERFLOWED = "javax.media.mscontrol.mediagroup.signals.SignalDetectorEvent.OVERFLOWED";
	public static final String PATTERN_MATCHED = "javax.media.mscontrol.mediagroup.signals.SignalDetectorEvent.PATTERN_MATCHED";
	public static final String RECEIVE_SIGNALS_COMPLETED = "javax.media.mscontrol.mediagroup.signals.SignalDetectorEvent.RECEIVE_SIGNALS_COMPLETED";
	public static final String SIGNAL_DETECTED = "javax.media.mscontrol.mediagroup.signals.SignalDetectorEvent.SIGNAL_DETECTED";

	private final SignalDetectorEvent wrappedEvent;
	private final SignalDetectorWrapper source;

	/**
	 * @param resourceEvent
	 */
	public SignalDetectorEventWrapper(ResourceEvent resourceEvent, SignalDetectorWrapper source) {
		super(resourceEvent);
		this.wrappedEvent = (SignalDetectorEvent) resourceEvent;
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#getSource()
	 */
	
	public SignalDetector getSource() {
		return this.source;
	}

	public int getPatternIndex() {
		return wrappedEvent.getPatternIndex();
	}

	public Value[] getSignalBuffer() {
		return wrappedEvent.getSignalBuffer();
	}

	public String getSignalString() {
		return wrappedEvent.getSignalString();
	}

}
