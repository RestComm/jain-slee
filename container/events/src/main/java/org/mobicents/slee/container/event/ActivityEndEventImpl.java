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

package org.mobicents.slee.container.event;

import java.io.ObjectStreamException;

import javax.slee.ActivityEndEvent;
import javax.slee.EventTypeID;

/**
 * 
 * Activity End Event implementation, as a singleton, also useful to retrieve
 * related objects such {@link EventTypeID} or {@link ComponentKey}.
 * 
 * @author Eduardo Martins
 * @author F.Moggia
 */
public class ActivityEndEventImpl implements ActivityEndEvent {
	
	/**
	 *	the event type id for this event
	 *  NOTE: do not build other instances of this event type id since 
	 *  the event router depends on that, i.e., uses == in some conditional choices
	 */
	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.ActivityEndEvent",
			"javax.slee", "1.0");
		
	/**
	 * singleton for this class
	 */
	public static final ActivityEndEventImpl SINGLETON = new ActivityEndEventImpl();
	
	/**
	 * private contsructor to ensure singleton
	 */
	private ActivityEndEventImpl() {
		
	}
		
	// solves serialization of singleton
	private Object readResolve() throws ObjectStreamException {
        return SINGLETON;
    }

    // solves cloning of singleton
    protected Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
    }
}
