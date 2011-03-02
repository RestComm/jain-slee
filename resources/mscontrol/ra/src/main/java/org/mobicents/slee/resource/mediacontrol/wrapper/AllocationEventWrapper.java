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
package org.mobicents.slee.resource.mediacontrol.wrapper;

import javax.media.mscontrol.EventType;
import javax.media.mscontrol.MediaErr;
import javax.media.mscontrol.resource.AllocationEvent;
import javax.media.mscontrol.resource.ResourceContainer;
import javax.media.mscontrol.resource.ResourceEvent;

/**
 * @author baranowb
 * 
 */
public class AllocationEventWrapper  implements AllocationEvent {

	public static final String IRRECOVERABLE_FAILURE = ">javax.media.mscontrol.resource.AllocationEvent.IRRECOVERABLE_FAILURE";
	// this event causes release.
	public static final String ALLOCATION_CONFIRMED = "javax.media.mscontrol.resource.AllocationEvent.ALLOCATION_CONFIRMED";

	protected final ResourceContainer source;
	protected final AllocationEvent wrappedEvent;
	/**
	 * @param resourceEvent
	 */
	public AllocationEventWrapper(AllocationEvent resourceEvent, ResourceContainer source) {
		this.wrappedEvent = resourceEvent;
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#getSource()
	 */
	
	public ResourceContainer getSource() {
		return this.source;
	}

	public MediaErr getError() {
		return wrappedEvent.getError();
	}

	public String getErrorText() {
		return wrappedEvent.getErrorText();
	}

	public EventType getEventType() {
		return wrappedEvent.getEventType();
	}

	public boolean isSuccessful() {
		return wrappedEvent.isSuccessful();
	}

}
