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

/***************************************************
 *                                                 *
 *  Restcomm: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on Feb 9, 2005                             *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import javax.management.MBeanRegistration;
import javax.management.NotificationBroadcaster;
import javax.slee.InvalidStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.SleeManagementMBean;

/**
 * @author Ivelin Ivanov
 * @author <a href="mailto:info@pro-ids.com">ProIDS sp. z o.o.</a>
 * 
 */
public interface SleeManagementMBeanImplMBean extends SleeManagementMBean,
		MBeanRegistration, NotificationBroadcaster {

	public String gracefulStop(Integer ast, Long time) throws InvalidStateException, ManagementException;

	public int getDefaultActiveSessionsThreshold();
	public void setDefaultActiveSessionsThreshold(int defaultActiveSessionsThreshold);

	public long getDefaultGracefulShutdownWaitTime();
	public void setDefaultGracefulShutdownWaitTime(long defaultGracefulShutdownWaitTime);

}