package org.rhq.plugins.jslee;

import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
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

	private final Log log = LogFactory.getLog(this.getClass());

	public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<JainSleeServerComponent> context)
			throws InvalidPluginConfigurationException, Exception {

		log.info("ResourceAdapterDiscoveryComponent.discoverResources() called");
		Set<DiscoveredResourceDetails> discoveredRAs = new HashSet<DiscoveredResourceDetails>();

		MBeanServerUtils mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();
		MBeanServerConnection connection = mbeanUtils.getConnection();

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

		log.info("discovered " + discoveredRAs.size() + " number of Services");
		return discoveredRAs;
	}

}
