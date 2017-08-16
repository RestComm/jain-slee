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

import javax.slee.SbbID;
import javax.slee.ServiceID;

/**
 * An event indicating an update in a Service's SBB configuration, fired in the service activity.
 * @author Eduardo Martins
 *
 */
public interface ServiceConfigurationUpdateEvent {

	/**
	 * the event type name
	 */
	public static final String EVENT_TYPE_NAME = "org.mobicents.slee.ServiceConfigurationUpdateEvent";
	
	/**
	 * the event type vendor
	 */
	public static final String EVENT_TYPE_VENDOR = "org.mobicents.slee";
	
	/**
	 * the event type version
	 */
	public static final String EVENT_TYPE_VERSION = "1.1";
	
	/**
	 * Retrieves the ID of the Sbb which configuration was updated.
	 * @return
	 */
	public ConfigProperties getConfigProperties();
	
	/**
	 * Retrieves the ID of the Service's Sbb which configuration was updated.
	 * @return
	 */
	public SbbID getSbbID();	
	
	/**
	 * Retrieves the ID of the Service which configuration was updated.
	 * @return
	 */
	public ServiceID getServiceID();
	
}
