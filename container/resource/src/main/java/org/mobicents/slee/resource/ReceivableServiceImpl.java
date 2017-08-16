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

import javax.slee.ServiceID;
import javax.slee.resource.ReceivableService;

/**
 * Implementation of the SLEE 1.1 specs {@link ReceivableService} class.
 * @author martins
 *
 */
public class ReceivableServiceImpl implements ReceivableService {

	private final ServiceID service;
	private final ReceivableEvent[] receivableEvents;
	
	public ReceivableServiceImpl(ServiceID service,ReceivableEvent[] receivableEvents) {
		this.service = service;
		this.receivableEvents = receivableEvents;
	}
	
	public ReceivableEvent[] getReceivableEvents() {
		return receivableEvents;
	}

	public ServiceID getService() {
		return service;
	}

	@Override
	public int hashCode() {
		return service.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((ReceivableServiceImpl)obj).service.equals(this.service);
		}
		else {
			return false;
		}
	}
}
