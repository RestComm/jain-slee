/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-5-5                            *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import javax.management.ObjectName;

import org.jboss.mx.util.MBeanProxy;
import org.jboss.system.ServiceMBeanSupport;

/**
 * @author Ivelin Ivanov
 *
 */
public class SleeLifecycleMonitor extends ServiceMBeanSupport implements SleeLifecycleMonitorMBean {
    
    private ObjectName sleeManagementMBeanName;
    private SleeManagementMBeanImplMBean mgmt;
  
    public void setSleeManagementMBean(ObjectName newSleeManagementMBeanName) {
        sleeManagementMBeanName = newSleeManagementMBeanName;
    }

    public ObjectName getSleeManagementMBean() {
        return sleeManagementMBeanName;
    }
    
    public void stopService() throws Exception {
        // enable full slee stop, assuming that this method is called due to undeployment of mobicents.sar 
        mgmt.setFullSleeStop(true);
    }
    
    public void startService() throws Exception {
        mgmt = (SleeManagementMBeanImplMBean)MBeanProxy.get(SleeManagementMBeanImplMBean.class, sleeManagementMBeanName, server);
    }
}
