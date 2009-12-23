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
	
	public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<JainSleeServerComponent> context) throws InvalidPluginConfigurationException, Exception {

		log.info("discoverResources() called");
		Set<DiscoveredResourceDetails> discoveredServices = new HashSet<DiscoveredResourceDetails>();
		ObjectName servicemanagement = new ObjectName(ServiceManagementMBean.OBJECT_NAME);

		MBeanServerUtils mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();
		MBeanServerConnection connection = mbeanUtils.getConnection();

		ServiceID[] activeServices = (ServiceID[]) connection.invoke(servicemanagement, "getServices",
				new Object[] { ServiceState.ACTIVE }, new String[] { ServiceState.class.getName() });

		ServiceID[] inactiveServices = (ServiceID[]) connection.invoke(servicemanagement, "getServices",
				new Object[] { ServiceState.INACTIVE }, new String[] { ServiceState.class.getName() });

		ServiceID[] stoppingServices = (ServiceID[]) connection.invoke(servicemanagement, "getServices",
				new Object[] { ServiceState.STOPPING }, new String[] { ServiceState.class.getName() });

		addService(activeServices, discoveredServices, context.getResourceType());
		addService(inactiveServices, discoveredServices, context.getResourceType());
		addService(stoppingServices, discoveredServices, context.getResourceType());

		log.info("Discovered " + discoveredServices.size() + " JAIN SLEE Services");
		return discoveredServices;
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
