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
package org.mobicents.slee.runtime.event;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.event.EventUnreferencedCallback;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;

/**
 * @author martins
 *
 */
public class ActivityEndEventUnreferencedCallback implements EventUnreferencedCallback {

	private final ActivityContextHandle ach;
	private final ActivityContextFactoryImpl factory;
	
	/**
	 * @param ach
	 * @param factory
	 */
	public ActivityEndEventUnreferencedCallback(ActivityContextHandle ach,
			ActivityContextFactoryImpl factory) {
		this.ach = ach;
		this.factory = factory;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventUnreferencedCallback#eventUnreferenced()
	 */
	public void eventUnreferenced() {
		factory.getActivityContext(ach).activityEnded();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventUnreferencedCallback#requiresTransaction()
	 */
	public boolean requiresTransaction() {
		return true;
	}

}
