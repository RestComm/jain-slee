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

package org.mobicents.slee.container.eventrouter;

/**
 * 
 * This class represents the Invocation State of an sbb object
 * It is used in rollback handling to determine what to do since
 * this depends on whether an sbb object method invocation was happening
 * at the time the exception was thrown
 * 
 * @author Tim
 * @author martins
 *
 * 
 */
public enum SbbInvocationState {
	
	NOT_INVOKING, INVOKING_SBB_CREATE, INVOKING_SBB_POSTCREATE, INVOKING_SBB_LOAD, INVOKING_SBB_STORE, INVOKING_EVENT_HANDLER
}
