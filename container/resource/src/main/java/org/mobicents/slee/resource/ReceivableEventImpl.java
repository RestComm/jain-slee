/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource;

import javax.slee.EventTypeID;
import javax.slee.resource.ReceivableService.ReceivableEvent;

/**
 * Implementation of the SLEE 1.1 specs {@link ReceivableEvent} class.
 * @author martins
 *
 */
public class ReceivableEventImpl implements ReceivableEvent {

	private final EventTypeID eventType;
	private final String resourceOption;
	private final boolean initialEvent;
		
	public ReceivableEventImpl(EventTypeID eventType, String resourceOption,
			boolean initialEvent) {
		this.eventType = eventType;
		this.resourceOption = resourceOption;
		this.initialEvent = initialEvent;
	}

	public EventTypeID getEventType() {
		return eventType;
	}

	public String getResourceOption() {
		return resourceOption;
	}

	public boolean isInitialEvent() {
		return initialEvent;
	}

	@Override
	public int hashCode() {
		return eventType.hashCode()*31 + (resourceOption != null ? resourceOption.hashCode() : 0);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			ReceivableEventImpl other = (ReceivableEventImpl) obj;
			if (this.resourceOption == null) {
				if (other.resourceOption != null) {
					return false;
				}
			}
			else {
				if (!this.resourceOption.equals(other.resourceOption)) {
					return false;
				}
			}
			return this.eventType.equals(other.eventType);
		}
		else {
			return false;
		}
	}
}
