package org.rhq.plugins.jslee;

import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class DeployableUnitDiscoveryComponent implements ResourceDiscoveryComponent<JainSleeServerComponent> {

	private final Log log = LogFactory.getLog(this.getClass());

	public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<JainSleeServerComponent> context)
			throws InvalidPluginConfigurationException, Exception {
		log.info("DeployableUnitDiscoveryComponent.discoverResources() called");

		Set<DiscoveredResourceDetails> discoveredDus = new HashSet<DiscoveredResourceDetails>();

		MBeanServerUtils mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();
		MBeanServerConnection connection = mbeanUtils.getConnection();

		ObjectName deploymentmanagement = new ObjectName(DeploymentMBean.OBJECT_NAME);

		DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection,
				deploymentmanagement, javax.slee.management.DeploymentMBean.class, false);

		DeployableUnitID[] deployableUnitIDs = deploymentMBean.getDeployableUnits();
		for (DeployableUnitID deployableUnitID : deployableUnitIDs) {
			DeployableUnitDescriptor deployableUnitDescriptor = deploymentMBean.getDescriptor(deployableUnitID);

			String key = deployableUnitID.getURL();
			
			// replace needed for Windows OS as \ needs to be escaped
			String[] elements = key.split(System.getProperty("file.separator").replaceAll("\\\\", "\\\\\\\\"));
			String lastElement = elements[(elements.length - 1)];
			String name = lastElement.substring(0, lastElement.lastIndexOf("."));

			String description = name + " -- Deployed on : " + deployableUnitDescriptor.getDeploymentDate();

			DiscoveredResourceDetails discoveredDu = new DiscoveredResourceDetails(context.getResourceType(), key,
					name, null, description, null, null);
			discoveredDu.getPluginConfiguration().put(new PropertySimple("url", deployableUnitID.getURL()));

			discoveredDus.add(discoveredDu);

		}

		return discoveredDus;
	}
}
