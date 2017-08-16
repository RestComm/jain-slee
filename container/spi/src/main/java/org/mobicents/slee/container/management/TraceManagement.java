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
package org.mobicents.slee.container.management;

import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.facilities.Tracer;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.facilities.TraceFacility;


/**
 * @author martins
 *
 */
public interface TraceManagement extends SleeContainerModule {

	/**
	 * 
	 * @return
	 */
	public TraceFacility getTraceFacility();

	/**
	 * 
	 * @param src
	 */
	public void registerNotificationSource(NotificationSource src);
	
	/**
	 * This method shoudl be called on on removal of notification source from
	 * slee
	 * 
	 * @param src
	 */
	public void deregisterNotificationSource(NotificationSource src);
	
	/**
	 * 
	 * @param src
	 * @return
	 */
	public boolean isNotificationSourceDefined(NotificationSource src);
	
	/**
	 * 
	 * @param src
	 * @param tracerName
	 * @return
	 * @throws ManagementException
	 */
	public boolean isTracerDefined(NotificationSource src, String tracerName) throws ManagementException;

	/**
	 * This method creates tracer for specified source, with specified name.
	 * boolean flag indicates that tracer has been requested by src, else, its
	 * just management operation. This method can me invoked multiple times.
	 * Only difference is boolean flag.
	 * 
	 * @param src
	 *            - notification source
	 * @param tracerName
	 *            - tracer name
	 * @param createdBySource
	 *            - flag indicating that src requested this tracer. In case
	 *            tracer already exists (created by mgmt operation) and this
	 *            method is invoked from NotificationSource this flag is set to
	 *            true. This alters state of tracer. It can not be changed back
	 * @return Tracer object. Either newly created or one that previously
	 *         existed.
	 * @throws InvalidArgumentException 
	 * 
	 */
	public Tracer createTracer(NotificationSource src, String tracerName, boolean createdBySource) throws InvalidArgumentException;

	/**
	 * @return
	 */
	public ObjectName getTraceMBeanObjectName(); 

}
