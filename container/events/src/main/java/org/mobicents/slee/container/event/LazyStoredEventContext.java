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

/**
 * 
 */
package org.mobicents.slee.container.event;

/**
 * Abstract event context class, that only puts itself on the datasource if the handle is requested, i.e., is stored in a cmp 
 * @author martins
 *
 */
public abstract class LazyStoredEventContext implements EventContext {

	protected final EventContextFactoryImpl factory;
	
	private EventContextHandle handle;
	
	/**
	 * 
	 */
	public LazyStoredEventContext(EventContextFactoryImpl factory) {
		this.factory = factory;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContext#getEventContextHandle()
	 */
	public EventContextHandle getEventContextHandle() {
		if (handle == null) {
			handle = new EventContextHandleImpl(factory.getSleeContainer().getUuidGenerator().createUUID());
			factory.getDataSource().addEventContext(handle, this);
		}
		return handle;
	}
	
	/**
	 * 
	 */
	public void remove() {
		if (handle != null) {
			factory.getDataSource().removeEventContext(handle);		
			handle = null;
		}
	}
}
