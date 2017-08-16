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