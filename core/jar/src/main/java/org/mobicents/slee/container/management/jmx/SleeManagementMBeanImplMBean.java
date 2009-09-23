/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
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
import javax.slee.management.SleeManagementMBean;

/**
 * @author Ivelin Ivanov
 * 
 */
public interface SleeManagementMBeanImplMBean extends SleeManagementMBean,
		MBeanRegistration, NotificationBroadcaster {
	
}