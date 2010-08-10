package org.rhq.plugins.jslee;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.slee.SbbID;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.pluginapi.inventory.DeleteResourceFacet;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.operation.OperationFacet;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;
import org.rhq.plugins.jslee.utils.ResourceAdaptorUtils;

public class RAEntityLinkComponent implements ResourceAdaptorUtils, DeleteResourceFacet, OperationFacet {

  private final Log log = LogFactory.getLog(this.getClass());

  private ResourceContext<RAEntityComponent> resourceContext;
  private String raEntityLinkName;
  private String raEntityName;
  private ResourceAdaptorID raId = null;
  private MBeanServerUtils mbeanUtils = null;
  private SbbID[] boundSbbs = null;

  private ConfigProperties configProperties = null;

  private ObjectName resourceManagement;

  public ResourceAdaptorID getResourceAdaptorID() {
    // TODO Auto-generated method stub
    return null;
  }

  public void start(ResourceContext context) throws InvalidPluginConfigurationException, Exception {
    log.info("RAEntityComponent.start");

    this.resourceContext = context;
    this.resourceManagement = new ObjectName(ResourceManagementMBean.OBJECT_NAME);

    this.mbeanUtils = ((RAEntityComponent) context.getParentResourceComponent()).getMBeanServerUtils();

    this.raEntityLinkName = this.resourceContext.getPluginConfiguration().getSimple("linkName").getStringValue();
    this.raEntityName = ((RAEntityComponent) context.getParentResourceComponent()).getRAEntityName();
    this.raId = ((RAEntityComponent) context.getParentResourceComponent()).getResourceAdaptorID();
  }

  public void stop() {
    // TODO Auto-generated method stub
  }

  public AvailabilityType getAvailability() {
    log.info("RAEntityLinkComponent.getAvailability");
    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.resourceManagement, javax.slee.management.ResourceManagementMBean.class, false);

      this.boundSbbs = resourceManagementMBean.getBoundSbbs(this.raEntityLinkName);

    }
    catch (Exception e) {
      log.error("getAvailability failed for RAEntityLinkComponent Link = " + this.raEntityLinkName);

      return AvailabilityType.DOWN;
    }

    return AvailabilityType.UP;
  }

  public MBeanServerUtils getMBeanServerUtils() {
    return this.mbeanUtils;
  }

  public void deleteResource() throws Exception {
    MBeanServerConnection connection = this.mbeanUtils.getConnection();
    ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
        connection, this.resourceManagement, javax.slee.management.ResourceManagementMBean.class, false);

    resourceManagementMBean.unbindLinkName(this.raEntityLinkName);
  }

  public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException, Exception {
    log.info("RAEntityLinkComponent.invokeOperation() with name = " + name);
    OperationResult result = new OperationResult();

    if ("listBoundSbbs".equals(name)) {
      result = doListBoundSBBs();
    }
    else {
      throw new UnsupportedOperationException("Operation [" + name + "] is not supported yet.");
    }

    return result;
  }

  private OperationResult doListBoundSBBs() throws Exception {
    MBeanServerConnection connection = this.mbeanUtils.getConnection();
    ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
        connection, this.resourceManagement, javax.slee.management.ResourceManagementMBean.class, false);

    SbbID[] sbbIds = resourceManagementMBean.getBoundSbbs(this.raEntityLinkName);

    PropertyList columnList = new PropertyList("result");

    for (SbbID sbbID : sbbIds) {
      PropertyMap col = new PropertyMap("element");
      col.put(new PropertySimple("SbbName", sbbID.getName()));
      col.put(new PropertySimple("SbbVendeor", sbbID.getVendor()));
      col.put(new PropertySimple("SbbVersion", sbbID.getVersion()));

      columnList.add(col);
    }

    OperationResult result = new OperationResult();
    result .getComplexResults().put(columnList);

    return result;
  }
}
