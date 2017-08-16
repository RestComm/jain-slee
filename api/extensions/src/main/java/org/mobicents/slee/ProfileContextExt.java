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

package org.mobicents.slee;

import javax.slee.facilities.AlarmFacility;
import javax.slee.profile.Profile;
import javax.slee.profile.ProfileContext;

/**
 * Extends {@link ProfileContext}, to expose facilities, allowing the
 * {@link Profile} developer to avoid JNDI usage.
 * 
 * @author martins
 * 
 */
public interface ProfileContextExt extends ProfileContext {

	/**
	 * Retrieves the Alarm Facility.
	 * 
	 * @return
	 */
	public AlarmFacility getAlarmFacility();

}
