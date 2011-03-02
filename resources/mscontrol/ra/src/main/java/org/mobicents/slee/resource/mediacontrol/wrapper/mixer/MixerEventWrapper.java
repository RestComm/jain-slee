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

import javax.media.mscontrol.join.Joinable;
import javax.media.mscontrol.mixer.MediaMixer;
import javax.media.mscontrol.mixer.MixerEvent;
import javax.media.mscontrol.resource.ResourceEvent;

import org.mobicents.slee.resource.mediacontrol.wrapper.ResourceEventWrapper;

/**
 * @author baranowb
 *
 */
public class MixerEventWrapper extends ResourceEventWrapper implements MixerEvent{
	public static final String ACTIVE_INPUTS_CHANED = "javax.media.mscontrol.mixer.MediaMixer.ACTIVE_INPUTS_CHANGED";
	public static final String MOST_ACTIVE_INPUTS_CHANED = "javax.media.mscontrol.mixer.MediaMixer.MOST_ACTIVE_INPUT_CHANGED";
	protected final MixerEvent wrappedEvent;
	protected final MediaMixerWrapper source;
	protected final Joinable[] activeInputs; //to be calculated.
	/**
	 * @param resourceEvent
	 */
	public MixerEventWrapper(ResourceEvent resourceEvent,MediaMixerWrapper source,Joinable[] activeInputs) {
		super(resourceEvent);
		this.wrappedEvent = (MixerEvent) resourceEvent;
		this.activeInputs = activeInputs;
		this.source = source;
		
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#getSource()
	 */
	
	public MediaMixer getSource() {
		return this.source;
	}

	
	/* (non-Javadoc)
	 * @see javax.media.mscontrol.mixer.MixerEvent#getActiveInputs()
	 */
	
	public Joinable[] getActiveInputs() {
		return this.activeInputs;
	}

}
