package org.rhq.plugins.jslee;

import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
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
	
	public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<JainSleeServerComponent> context)
			throws InvalidPluginConfigurationException, Exception {

		log.info("discoverResources() called");
		Set<DiscoveredResourceDetails> discoveredEndpoints = new HashSet<DiscoveredResourceDetails>();
		ObjectName servicemanagement = new ObjectName(ServiceManagementMBean.OBJECT_NAME);

		MBeanServerUtils mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();
		MBeanServerConnection connection = mbeanUtils.getConnection();

		ServiceID[] activeServices = (ServiceID[]) connection.invoke(servicemanagement, "getServices",
				new Object[] { ServiceState.ACTIVE }, new String[] { ServiceState.class.getName() });

		ServiceID[] inactiveServices = (ServiceID[]) connection.invoke(servicemanagement, "getServices",
				new Object[] { ServiceState.INACTIVE }, new String[] { ServiceState.class.getName() });

		ServiceID[] stoppingServices = (ServiceID[]) connection.invoke(servicemanagement, "getServices",
				new Object[] { ServiceState.STOPPING }, new String[] { ServiceState.class.getName() });

		addService(activeServices, discoveredEndpoints, context.getResourceType());
		addService(inactiveServices, discoveredEndpoints, context.getResourceType());
		addService(stoppingServices, discoveredEndpoints, context.getResourceType());

		
		log.info("discovered "+discoveredEndpoints.size()+" number of Services");
		return discoveredEndpoints;
	}

	private void addService(ServiceID[] services, Set<DiscoveredResourceDetails> discoveredServices,
			ResourceType resourceType) {
		for (ServiceID serviceID : services) {
			String key = serviceID.getName() + serviceID.getVendor() + serviceID.getVersion();
			String description = serviceID.getName() + "#" + serviceID.getVendor() + "#" + serviceID.getVersion();

			DiscoveredResourceDetails discoveredService = new DiscoveredResourceDetails(resourceType, key, serviceID
					.getName(), serviceID.getVersion(), description, null, null);
			discoveredService.getPluginConfiguration().put(new PropertySimple("name", serviceID.getName()));
			discoveredService.getPluginConfiguration().put(new PropertySimple("version", serviceID.getVersion()));
			discoveredService.getPluginConfiguration().put(new PropertySimple("vendor", serviceID.getVendor()));
			discoveredServices.add(discoveredService);
		}
	}
}
