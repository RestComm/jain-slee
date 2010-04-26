/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import javax.management.NotificationBroadcaster;
import javax.slee.management.TraceMBean;

import org.jboss.system.ServiceMBean;

/**
 * @author Ivelin Ivanov
 */
public interface TraceMBeanImplMBean extends ServiceMBean, NotificationBroadcaster, TraceMBean {

}
