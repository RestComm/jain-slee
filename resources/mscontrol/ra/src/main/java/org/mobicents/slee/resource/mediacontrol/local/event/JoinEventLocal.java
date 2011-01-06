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
package org.mobicents.slee.resource.mediacontrol.local.event;

import java.io.Serializable;

import javax.media.mscontrol.EventType;
import javax.media.mscontrol.MediaErr;
import javax.media.mscontrol.join.JoinEvent;
import javax.media.mscontrol.join.Joinable;

/**
 * @author baranowb
 *
 */
public class JoinEventLocal  implements JoinEvent {
	//TODO: how to handle this?
	private JoinEvent joinEvent;
	private Joinable joinable;
	/**
	 * @param resourceEvent
	 */
	public JoinEventLocal(JoinEvent resourceEvent,Joinable joinable){
		this.joinEvent  = resourceEvent;
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#getSource()
	 */
	@Override
	public Joinable getSource() {
		return joinable;
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.join.JoinEvent#getContext()
	 */
	@Override
	public Serializable getContext() {
		return joinEvent.getContext();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.join.JoinEvent#getOtherJoinable()
	 */
	@Override
	public Joinable getOtherJoinable() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.join.JoinEvent#getThisJoinable()
	 */
	@Override
	public Joinable getThisJoinable() {
		
		return joinable;
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#getError()
	 */
	@Override
	public MediaErr getError() {
		return joinEvent.getError();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#getErrorText()
	 */
	@Override
	public String getErrorText() {
		return joinEvent.getErrorText();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#getEventType()
	 */
	@Override
	public EventType getEventType() {
		return joinEvent.getEventType();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#isSuccessful()
	 */
	@Override
	public boolean isSuccessful() {
		return joinEvent.isSuccessful();
	}

	
}
