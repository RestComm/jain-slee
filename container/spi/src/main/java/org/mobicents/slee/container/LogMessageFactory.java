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
package org.mobicents.slee.container;

import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * @author martins
 *
 */
public class LogMessageFactory {

	private static final String NOT_APPLICABLE = "n/a";
	
	public static String newLogMessaget(String sbbEntityId, String logMessage) {
		return newLogMessaget(null, sbbEntityId, logMessage);
	}
	
	public static String newLogMessaget(ActivityContextHandle ach, String logMessage) {
		return newLogMessaget(ach, null, logMessage);
	}
	
	public static String newLogMessaget(ActivityContextHandle ach, String sbbEntityId, String logMessage) {
		return new StringBuilder("[AC = ").append(ach != null ? ach : NOT_APPLICABLE ).append(" , SBBE = ").append(sbbEntityId != null ? sbbEntityId : NOT_APPLICABLE).append("]\n\t").append(logMessage).toString();
	}
}
