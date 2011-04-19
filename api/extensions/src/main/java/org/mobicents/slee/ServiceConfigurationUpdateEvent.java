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
