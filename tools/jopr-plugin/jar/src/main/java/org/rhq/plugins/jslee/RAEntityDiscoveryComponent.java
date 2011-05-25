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

package org.rhq.plugins.jslee;

import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.resource.ResourceAdaptorID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class RAEntityDiscoveryComponent implements ResourceDiscoveryComponent<ResourceAdaptorComponent> {

  private final Log log = LogFactory.getLog(this.getClass());

  public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<ResourceAdaptorComponent> context) throws InvalidPluginConfigurationException, Exception {
    MBeanServerUtils mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();
    try {
      if(log.isDebugEnabled()) {
        log.debug("RAEntityDiscoveryComponent.discoverResources() called");
      }
      Set<DiscoveredResourceDetails> discoveredRAEntities = new HashSet<DiscoveredResourceDetails>();

      MBeanServerConnection connection = mbeanUtils.getConnection();
      mbeanUtils.login();

      ObjectName resourceManagement = new ObjectName(ResourceManagementMBean.OBJECT_NAME);

      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, resourceManagement, javax.slee.management.ResourceManagementMBean.class, false);

      ResourceAdaptorID raID = context.getParentResourceComponent().getResourceAdaptorID();
      String[] raEntities = resourceManagementMBean.getResourceAdaptorEntities(raID);

      for (String entityName : raEntities) {
        String description = "RA Entity : " + entityName + " For ResourceAdaptor : " + raID.toString();

        DiscoveredResourceDetails discoveredEntity = new DiscoveredResourceDetails(context.getResourceType(),
            entityName, entityName+" Entity", raID.getVersion(), description, null, null);
        discoveredEntity.getPluginConfiguration().put(new PropertySimple("entityName", entityName));
        discoveredEntity.getPluginConfiguration().put(new PropertySimple("name", raID.getName()));
        discoveredEntity.getPluginConfiguration().put(new PropertySimple("version", raID.getVersion()));
        discoveredEntity.getPluginConfiguration().put(new PropertySimple("vendor", raID.getVendor()));
        discoveredRAEntities.add(discoveredEntity);
      }

      if(log.isInfoEnabled()) {
        log.info("Discovered " + discoveredRAEntities.size() + " JAIN SLEE Resource Adaptor Entity Components.");
      }
      return discoveredRAEntities;
    }
    finally {
      try {
        mbeanUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

}
