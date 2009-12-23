package org.rhq.plugins.jslee;

import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.slee.SbbID;
import javax.slee.management.DeploymentMBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.resource.ResourceType;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class SbbDiscoveryComponent implements ResourceDiscoveryComponent<JainSleeServerComponent> {

  private final Log log = LogFactory.getLog(this.getClass());

  public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<JainSleeServerComponent> context) throws InvalidPluginConfigurationException, Exception {

    log.info("discoverResources() called");

    // Get the connection up and ready
    MBeanServerUtils mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();
    MBeanServerConnection connection = mbeanUtils.getConnection();
    ObjectName depMBeanObj = new ObjectName(DeploymentMBean.OBJECT_NAME);

    Set<DiscoveredResourceDetails> discoveredSBBs = new HashSet<DiscoveredResourceDetails>();

    DeploymentMBean depMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection, depMBeanObj, DeploymentMBean.class, false);

    SbbID[] sbbIds = depMBean.getSbbs();

    addSbb(sbbIds, discoveredSBBs, context.getResourceType());

    log.info("Discovered " + discoveredSBBs.size() + " JAIN SLEE SBBs");
    return discoveredSBBs;
  }

  private void addSbb(SbbID[] sbbIds, Set<DiscoveredResourceDetails> discoveredServices, ResourceType resourceType) {
    for (SbbID sbbId : sbbIds) {

      String key = sbbId.toString();
      String description = sbbId.toString();

      // Create new Service resource
      DiscoveredResourceDetails discoveredService = new DiscoveredResourceDetails(resourceType, key, sbbId.getName(), sbbId.getVersion(), 
          description, null, null);

      // Add properties to Service resource
      discoveredService.getPluginConfiguration().put(new PropertySimple("name", sbbId.getName()));
      discoveredService.getPluginConfiguration().put(new PropertySimple("version", sbbId.getVersion()));
      discoveredService.getPluginConfiguration().put(new PropertySimple("vendor", sbbId.getVendor()));

      discoveredServices.add(discoveredService);
    }
  }
}
