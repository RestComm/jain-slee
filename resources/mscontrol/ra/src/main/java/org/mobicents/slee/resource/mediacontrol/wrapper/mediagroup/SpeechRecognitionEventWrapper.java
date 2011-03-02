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

import java.net.URL;

import javax.media.mscontrol.mediagroup.signals.SpeechRecognitionEvent;
import javax.media.mscontrol.resource.ResourceEvent;

/**
 * @author baranowb
 * 
 */
public class SpeechRecognitionEventWrapper extends SignalDetectorEventWrapper implements SpeechRecognitionEvent {

	public static final String SPEECH = "javax.media.mscontrol.mediagroup.signals.SpeechRecognitionEvent.SPEECH";
	private final SpeechRecognitionEvent wrappedEvent;

	/**
	 * @param resourceEvent
	 * @param source
	 */
	public SpeechRecognitionEventWrapper(ResourceEvent resourceEvent, SignalDetectorWrapper source) {
		super(resourceEvent, source);
		this.wrappedEvent = (SpeechRecognitionEvent) resourceEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.signals.SpeechRecognitionEvent#
	 * getSemanticResult()
	 */
	
	public URL getSemanticResult() {

		return this.wrappedEvent.getSemanticResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.mediagroup.signals.SpeechRecognitionEvent#getTag()
	 */
	
	public String getTag() {

		return this.wrappedEvent.getTag();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.mediagroup.signals.SpeechRecognitionEvent#getUserInput
	 * ()
	 */
	
	public String getUserInput() {
		return this.wrappedEvent.getUserInput();
	}

}
