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
