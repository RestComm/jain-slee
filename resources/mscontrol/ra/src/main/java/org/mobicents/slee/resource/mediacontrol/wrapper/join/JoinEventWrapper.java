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
package org.mobicents.slee.resource.mediacontrol.wrapper.join;

import java.io.Serializable;

import javax.media.mscontrol.EventType;
import javax.media.mscontrol.MediaErr;
import javax.media.mscontrol.join.JoinEvent;
import javax.media.mscontrol.join.Joinable;

/**
 * @author baranowb
 * 
 */
public class JoinEventWrapper implements JoinEvent {
	public static final String SLEE_EVENT_JOINED = "javax.media.mscontrol.join.JoinEvent.JOINED";
	public static final String SLEE_EVENT_UNJOINED = "javax.media.mscontrol.join.JoinEvent.UNJOINED";

	private JoinEvent wrappedEvent;
	private Joinable source;
	private Joinable thisJoinable;
	private Joinable otherJoinable;

	/**
	 * @param wrappedEvent
	 * @param source
	 * @param thisJoinable
	 * @param otherJoinable
	 */
	public JoinEventWrapper(JoinEvent wrappedEvent, Joinable source, Joinable thisJoinable, Joinable otherJoinable) {
		super();
		this.wrappedEvent = wrappedEvent;
		this.source = source;
		this.otherJoinable = otherJoinable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#getError()
	 */
	
	public MediaErr getError() {
		return this.wrappedEvent.getError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#getErrorText()
	 */
	
	public String getErrorText() {
		return this.wrappedEvent.getErrorText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#getEventType()
	 */
	
	public EventType getEventType() {
		return this.wrappedEvent.getEventType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#getSource()
	 */
	
	public Joinable getSource() {
		return this.source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#isSuccessful()
	 */
	
	public boolean isSuccessful() {
		return this.wrappedEvent.isSuccessful();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.join.JoinEvent#getContext()
	 */
	
	public Serializable getContext() {
		return this.wrappedEvent.getContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.join.JoinEvent#getOtherJoinable()
	 */
	
	public Joinable getOtherJoinable() {
		return this.otherJoinable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.join.JoinEvent#getThisJoinable()
	 */
	
	public Joinable getThisJoinable() {
		return this.thisJoinable;
	}

}
