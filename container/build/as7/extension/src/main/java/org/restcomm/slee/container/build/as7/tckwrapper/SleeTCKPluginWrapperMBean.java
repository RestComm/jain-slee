/***************************************************
 *                                                 *
 *  Restcomm: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-3-26                             *
 *                                                 *
 ***************************************************/

package org.restcomm.slee.container.build.as7.tckwrapper;

/**
 * 
 * The Standard MBean interface, which makes visible to the MBeans server 
 * all methods that should be.
 * 
 * @author Ivelin Ivanov
 *
 */
public interface SleeTCKPluginWrapperMBean
{
    void setTCKPluginClassName(String newClName);

    String getTCKPluginClassName();

    void setRMIRegistryPort(int port);

    int getRMIRegistryPort();

    void setSleeProviderImpl(String provider);

    String getSleeProviderImpl();

    void setTCKPluginMBeanObjectName(String newMBObjectName);

    String getTCKPluginMBeanObjectName();
}