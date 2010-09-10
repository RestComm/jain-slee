package org.rhq.plugins.jslee;

import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class ExecutorDiscoveryComponent implements ResourceDiscoveryComponent<JainSleeServerComponent> {

  private final Log log = LogFactory.getLog(ExecutorDiscoveryComponent.class);

  public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext<JainSleeServerComponent> context) throws InvalidPluginConfigurationException, Exception {
    if(log.isDebugEnabled()) {
      log.debug("ExecutorDiscoveryComponent.discoverResources() called");
    }

    Set<DiscoveredResourceDetails> discoveredExecutors = new HashSet<DiscoveredResourceDetails>();

    MBeanServerUtils mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();

    try {
      MBeanServerConnection connection = mbeanUtils.getConnection();
      mbeanUtils.login();

      ObjectName erConfigObjectName = new ObjectName("org.mobicents.slee:name=EventRouterConfiguration"/* FIXME */);

      EventRouterConfigurationMBean erConfigMBean = (EventRouterConfigurationMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
          erConfigObjectName, EventRouterConfigurationMBean.class, false);

      for (int executorId = 0; executorId < erConfigMBean.getEventRouterThreads(); executorId++) {
        String key = "Executor #" + executorId;

        String name = key;
        String description = "Mobicents JAIN SLEE Event Router " + name;

        DiscoveredResourceDetails discoveredExecutor = new DiscoveredResourceDetails(context.getResourceType(), key,
            name, null, description, null, null);

        discoveredExecutor.getPluginConfiguration().put(new PropertySimple("executorId", executorId));
        discoveredExecutors.add(discoveredExecutor);

      }

      return discoveredExecutors;
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
