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
import javax.media.mscontrol.Qualifier;
import javax.media.mscontrol.resource.ResourceEvent;
import javax.media.mscontrol.resource.Trigger;

/**
 * @author baranowb
 * 
 */
public abstract class ResourceEventWrapper {
	//NOTE: dont implement interface here. it will not allow to subclass this class and define specific getSource....
	protected ResourceEvent resourceEvent;

	/**
	 * @param resourceEvent
	 */
	public ResourceEventWrapper(ResourceEvent resourceEvent) {
		super();
		this.resourceEvent = resourceEvent;
	}

	public MediaErr getError() {
		return resourceEvent.getError();
	}

	public String getErrorText() {
		return resourceEvent.getErrorText();
	}

	public EventType getEventType() {
		return resourceEvent.getEventType();
	}

	public boolean isSuccessful() {
		return resourceEvent.isSuccessful();
	}

	public Qualifier getQualifier() {
		return resourceEvent.getQualifier();
	}

	public Trigger getRTCTrigger() {
		return resourceEvent.getRTCTrigger();
	}



}
