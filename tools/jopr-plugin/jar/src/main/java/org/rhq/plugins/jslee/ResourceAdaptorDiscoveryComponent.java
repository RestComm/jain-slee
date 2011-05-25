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
import javax.slee.management.DeploymentMBean;
import javax.slee.resource.ResourceAdaptorID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class ResourceAdaptorDiscoveryComponent implements ResourceDiscoveryComponent<JainSleeServerComponent> {

  private final Log log = LogFactory.getLog(ResourceAdaptorDiscoveryComponent.class);

  public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<JainSleeServerComponent> context)
  throws InvalidPluginConfigurationException, Exception {
    if(log.isDebugEnabled()) {
      log.debug("ResourceAdapterDiscoveryComponent.discoverResources() called");
    }

    Set<DiscoveredResourceDetails> discoveredRAs = new HashSet<DiscoveredResourceDetails>();

    MBeanServerUtils mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();
    try {
      MBeanServerConnection connection = mbeanUtils.getConnection();
      mbeanUtils.login();

      ObjectName deploymentMBeanObj = new ObjectName(DeploymentMBean.OBJECT_NAME);
      DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection,
          deploymentMBeanObj, javax.slee.management.DeploymentMBean.class, false);

      ResourceAdaptorID[] ras = deploymentMBean.getResourceAdaptors();

      for (ResourceAdaptorID raID : ras) {
        String key = raID.toString();
        String description = raID.toString();

        DiscoveredResourceDetails discoveredService = new DiscoveredResourceDetails(context.getResourceType(), key,
            raID.getName(), raID.getVersion(), description, null, null);
        discoveredService.getPluginConfiguration().put(new PropertySimple("name", raID.getName()));
        discoveredService.getPluginConfiguration().put(new PropertySimple("version", raID.getVersion()));
        discoveredService.getPluginConfiguration().put(new PropertySimple("vendor", raID.getVendor()));
        discoveredRAs.add(discoveredService);
      }

      if(log.isInfoEnabled()) {
        log.info("Discovered " + discoveredRAs.size() + " JAIN SLEE Resource Adaptor Components.");
      }

      return discoveredRAs;
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
