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

package org.mobicents.slee.container.profile;

/**
 * 
 * Start time:16:44:48 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * Enum representing state of Profile Object - 10.13.2 (or Profile Management
 * Object - 10.9 Profile Management objects)
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public enum ProfileObjectState {

	/**
	 * The Profile object does not exist. It may not have been created or it may
	 * have been deleted.
	 */
	DOES_NOT_EXIST,

	/**
	 * The Profile object exists but is not assigned to any particular Profile.
	 */
	POOLED,

	/**
	 * The Profile object is making the transaction from POOLED to READY through
	 * a call of profileInitialize().
	 */
	PROFILE_INITIALIZATION,

	/**
	 * The Profile object is assigned to a Profile. It is ready to receive
	 * method invocations through its Profile Local interface or Profile
	 * Management interface, and various life-cycle callback method invocations.
	 */
	READY;

}
