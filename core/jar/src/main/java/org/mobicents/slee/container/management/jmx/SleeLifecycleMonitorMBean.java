/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-5-5                             *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import javax.management.ObjectName;

import org.jboss.system.ServiceMBean;

/**
 * Helper MBean for Slee Lifecycle control. Used by SleeManagementMBean to distinguish between 
 * external request for SLEE stop() vs. mobicents.sar redeployment.
 * 
 * @author Ivelin Ivanov
 *
 */
public interface SleeLifecycleMonitorMBean extends ServiceMBean {
    public abstract void setSleeManagementMBean(
            ObjectName newSleeManagementMBeanName);

    public abstract ObjectName getSleeManagementMBean();
}
