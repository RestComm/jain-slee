/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
