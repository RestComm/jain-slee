package org.rhq.plugins.jslee;

import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ServiceUsageMBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class ServiceSbbUsageParameterSetDiscoveryComponent implements ResourceDiscoveryComponent<ServiceSbbComponent> {

  private final Log log = LogFactory.getLog(ServiceSbbUsageParameterSetDiscoveryComponent.class);

  public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<ServiceSbbComponent> context) throws InvalidPluginConfigurationException, Exception {
    MBeanServerUtils mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();
    try {
      if(log.isDebugEnabled()) {
        log.debug("RAEntityDiscoveryComponent.discoverResources() called");
      }
      Set<DiscoveredResourceDetails> discoveredUsageParameterSets = new HashSet<DiscoveredResourceDetails>();

      MBeanServerConnection connection = mbeanUtils.getConnection();
      mbeanUtils.login();

      ServiceID serviceId = context.getParentResourceComponent().getServiceID();

      // As an example, if a particular service has the name “FooService”, vendor “FooCompany”, and version “1.0”,
      // then the Object Name of a ServiceUsageMBean for that service would be:
      // “javax.slee.management.usage:type=ServiceUsage,serviceName="FooService", serviceVendor="FooCompany",serviceVersion="1.0"”
      ObjectName serviceUsageON = new ObjectName(ServiceUsageMBean.BASE_OBJECT_NAME + ','
          + ServiceUsageMBean.SERVICE_NAME_KEY + '='
          + ObjectName.quote(serviceId.getName()) + ','
          + ServiceUsageMBean.SERVICE_VENDOR_KEY + '='
          + ObjectName.quote(serviceId.getVendor()) + ','
          + ServiceUsageMBean.SERVICE_VERSION_KEY + '='
          + ObjectName.quote(serviceId.getVersion()));

      ServiceUsageMBean serviceUsageMBean = (ServiceUsageMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, serviceUsageON, javax.slee.management.ServiceUsageMBean.class, false);

      SbbID sbbId = context.getParentResourceComponent().getSbbID();
      String[] usageParameterSets = serviceUsageMBean.getUsageParameterSets(sbbId);

      // This is default one
      String defaultDescription = "Usage Parameter Set : " + "<default>" + " for " + sbbId + " @ " + serviceId;
      String defaultKey = "<default>" + "@" + sbbId + "@" + serviceId;

      DiscoveredResourceDetails defaultDiscoveredEntity = new DiscoveredResourceDetails(context.getResourceType(),
          defaultKey, "<default>" + " Usage Paramaters Set", sbbId.getVersion(), defaultDescription, null, null);
      defaultDiscoveredEntity.getPluginConfiguration().put(new PropertySimple("usageParameterSet", "<default>"));
      defaultDiscoveredEntity.getPluginConfiguration().put(new PropertySimple("name", sbbId.getName()));
      defaultDiscoveredEntity.getPluginConfiguration().put(new PropertySimple("version", sbbId.getVersion()));
      defaultDiscoveredEntity.getPluginConfiguration().put(new PropertySimple("vendor", sbbId.getVendor()));
      discoveredUsageParameterSets.add(defaultDiscoveredEntity);

      for (String usageParameterSet : usageParameterSets) {
        String description = "Usage Parameter Set : " + usageParameterSet + " for " + sbbId + " @ " + serviceId;
        String key = usageParameterSet + "@" + sbbId + "@" + serviceId;

        DiscoveredResourceDetails discoveredEntity = new DiscoveredResourceDetails(context.getResourceType(),
            key, usageParameterSet + " Usage Paramaters Set", sbbId.getVersion(), description, null, null);
        discoveredEntity.getPluginConfiguration().put(new PropertySimple("usageParameterSet", usageParameterSet));
        discoveredEntity.getPluginConfiguration().put(new PropertySimple("name", sbbId.getName()));
        discoveredEntity.getPluginConfiguration().put(new PropertySimple("version", sbbId.getVersion()));
        discoveredEntity.getPluginConfiguration().put(new PropertySimple("vendor", sbbId.getVendor()));
        discoveredUsageParameterSets.add(discoveredEntity);
      }

      if(log.isInfoEnabled()) {
        log.info("Discovered " + discoveredUsageParameterSets.size() + " JAIN SLEE Usage Parameter Sets for " + sbbId + " @ " + serviceId + ".");
      }
      return discoveredUsageParameterSets;
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
