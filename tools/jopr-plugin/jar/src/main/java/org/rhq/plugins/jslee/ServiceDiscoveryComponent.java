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
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;
import javax.slee.ServiceID;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.ServiceState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.resource.ResourceType;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class ServiceDiscoveryComponent implements ResourceDiscoveryComponent<JainSleeServerComponent> {

	private final Log log = LogFactory.getLog(this.getClass());
	
	public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<JainSleeServerComponent> context) throws InvalidPluginConfigurationException, Exception {
	  if(log.isTraceEnabled()) {
		  log.trace("discoverResources(" + context + ") called.");
	  }
		Set<DiscoveredResourceDetails> discoveredServices = new HashSet<DiscoveredResourceDetails>();
		ObjectName servicemanagement = new ObjectName(ServiceManagementMBean.OBJECT_NAME);

		MBeanServerUtils mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();
		
		try {
		MBeanServerConnection connection = mbeanUtils.getConnection();
    mbeanUtils.login();

    ServiceID[] activeServices = (ServiceID[]) connection.invoke(servicemanagement, "getServices",
				new Object[] { ServiceState.ACTIVE }, new String[] { ServiceState.class.getName() });

		ServiceID[] inactiveServices = (ServiceID[]) connection.invoke(servicemanagement, "getServices",
				new Object[] { ServiceState.INACTIVE }, new String[] { ServiceState.class.getName() });

		ServiceID[] stoppingServices = (ServiceID[]) connection.invoke(servicemanagement, "getServices",
				new Object[] { ServiceState.STOPPING }, new String[] { ServiceState.class.getName() });

    addService(activeServices, discoveredServices, context.getResourceType());
		addService(inactiveServices, discoveredServices, context.getResourceType());
		addService(stoppingServices, discoveredServices, context.getResourceType());

		if(log.isInfoEnabled()) {
		  log.info("Discovered " + discoveredServices.size() + " JAIN SLEE Service Components.");
		}

		return discoveredServices;
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

	private void addService(ServiceID[] services, Set<DiscoveredResourceDetails> discoveredServices, ResourceType resourceType) {
		for (ServiceID serviceID : services) {
			String key = serviceID.toString();
			String description = serviceID.toString();

			// Create new Service resource
			DiscoveredResourceDetails discoveredService = new DiscoveredResourceDetails(resourceType, key, serviceID.getName(), 
			    serviceID.getVersion(), description, null, null);
			
			// Add properties to Service resource
			discoveredService.getPluginConfiguration().put(new PropertySimple("name", serviceID.getName()));
			discoveredService.getPluginConfiguration().put(new PropertySimple("version", serviceID.getVersion()));
			discoveredService.getPluginConfiguration().put(new PropertySimple("vendor", serviceID.getVendor()));
			
			discoveredServices.add(discoveredService);
		}
	}
}
