/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-3-26                             *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.test.suite.tckwrapper;

import javax.management.MBeanRegistration;

import org.jboss.system.ServiceMBean;

/**
 * 
 * The Standard MBean interface, which makes visible to the MBeans server 
 * all methods that should be.
 * 
 * @author Ivelin Ivanov
 *
 */
public interface SleeTCKPluginWrapperMBean extends ServiceMBean, MBeanRegistration 
{
    public abstract void setTCKPluginClassName(String newClName);

    public abstract String getTCKPluginClassName();

    public abstract void setRMIRegistryPort(int port);

    public abstract int getRMIRegistryPort();

    public abstract void setSleeProviderImpl(String provider);

    public abstract String getSleeProviderImpl();

    public abstract void setTCKPluginMBeanObjectName(String newMBObjectName);

    public abstract String getTCKPluginMBeanObjectName();
}