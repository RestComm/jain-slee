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

package org.mobicents.slee.container.profile;

/**
 * 
 * Start time:16:44:48 2009-03-13<br>
 * Project: restcomm-jainslee-server-core<br>
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
