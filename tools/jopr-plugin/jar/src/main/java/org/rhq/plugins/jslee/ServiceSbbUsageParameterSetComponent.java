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

import java.util.Set;

import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ServiceUsageMBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.ConfigurationUpdateStatus;
import org.rhq.core.domain.configuration.Property;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataTrait;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.pluginapi.configuration.ConfigurationFacet;
import org.rhq.core.pluginapi.configuration.ConfigurationUpdateReport;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceComponent;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;
import org.rhq.core.pluginapi.operation.OperationFacet;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class ServiceSbbUsageParameterSetComponent implements ResourceComponent<ServiceSbbComponent>, MeasurementFacet, OperationFacet, ConfigurationFacet {
  private final Log log = LogFactory.getLog(this.getClass());

  private ResourceContext<ServiceSbbComponent> resourceContext;

  String usageParameterSetName = null;
  
  private ServiceID serviceId = null;
  private SbbID sbbId = null;
  private MBeanServerUtils mbeanUtils = null;

  private boolean isUp = false;

  public void start(ResourceContext<ServiceSbbComponent> context) throws InvalidPluginConfigurationException, Exception {
    log.info("start");

    this.resourceContext = context;

    this.mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();

    String name = this.resourceContext.getPluginConfiguration().getSimple("name").getStringValue();
    String version = this.resourceContext.getPluginConfiguration().getSimple("version").getStringValue();
    String vendor = this.resourceContext.getPluginConfiguration().getSimple("vendor").getStringValue();

    sbbId = new SbbID(name, vendor, version);

    serviceId = context.getParentResourceComponent().getServiceID();
    
    usageParameterSetName = this.resourceContext.getPluginConfiguration().getSimple("usageParameterSet").getStringValue();
  }

  public void stop() {
    log.info("stop");
  }

  public AvailabilityType getAvailability() {
    log.info("getAvailability");

    // TODO: Complete method
    this.isUp = true; /* false;

    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      DeploymentMBean depMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection, deploymentMBeanObj, DeploymentMBean.class, false);

      for(SbbID activeSbbId : depMBean.getSbbs()) {
        if(activeSbbId.equals(sbbId)) {
          this.isUp = true;
        }
      }
    }
    catch (Exception e) {
      log.error("getAvailability failed for SbbID = " + this.sbbId);
    }
    */
    
    return this.isUp ? AvailabilityType.UP : AvailabilityType.DOWN;
  }

  public void getValues(MeasurementReport report, Set<MeasurementScheduleRequest> metrics) throws Exception {
    log.info("getValues");
    for (MeasurementScheduleRequest request : metrics) {
      if (request.getName().equals("state")) {
        report.addData(new MeasurementDataTrait(request, this.isUp ? "UP" : "DOWN"));
      }
    }
  }

  public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException, Exception {
    log.info("SbbComponent.invokeOperation() with name = " + name);

    throw new UnsupportedOperationException("Operation [" + name + "] is not supported.");
  }
  
  public MBeanServerUtils getMBeanServerUtils() {
    return mbeanUtils;
  }

  @Override
  public Configuration loadResourceConfiguration() throws Exception {
    try {
      Configuration config = new Configuration();
  
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();
  
      // As an example, if a particular service has the name �FooService�, vendor �FooCompany�, and version �1.0�,
      // then the Object Name of a ServiceUsageMBean for that service would be:
      // �javax.slee.management.usage:type=ServiceUsage,serviceName="FooService", serviceVendor="FooCompany",serviceVersion="1.0"�
      ObjectName serviceUsageON = new ObjectName(ServiceUsageMBean.BASE_OBJECT_NAME + ','
          + ServiceUsageMBean.SERVICE_NAME_KEY + '='
          + ObjectName.quote(serviceId.getName()) + ','
          + ServiceUsageMBean.SERVICE_VENDOR_KEY + '='
          + ObjectName.quote(serviceId.getVendor()) + ','
          + ServiceUsageMBean.SERVICE_VERSION_KEY + '='
          + ObjectName.quote(serviceId.getVersion()));
  
      ServiceUsageMBean serviceUsageMBean = (ServiceUsageMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, serviceUsageON, javax.slee.management.ServiceUsageMBean.class, false);
  
      PropertyList columnList = new PropertyList("usageParameter");
      ObjectName sbbUsageON = null;
      if(usageParameterSetName != null && !usageParameterSetName.equals("<default>")) {
        sbbUsageON = serviceUsageMBean.getSbbUsageMBean(sbbId, usageParameterSetName);
      }
      else {
        sbbUsageON = serviceUsageMBean.getSbbUsageMBean(sbbId);
      }
        
      MBeanInfo usageInfo = connection.getMBeanInfo(sbbUsageON);
      
      for (MBeanOperationInfo operation : usageInfo.getOperations()) {
        String opName = operation.getName();
        if (opName.startsWith("get")) {
          PropertyMap col = new PropertyMap("usageParameterDefinition");
          
          col.put(new PropertySimple("usageParameterName", opName.replaceFirst("get", "")));

          boolean isSampleType = operation.getReturnType().equals("javax.slee.usage.SampleStatistics");
          col.put(new PropertySimple("usageParameterType", isSampleType ? "Sample" : "Counter"));

          Object value = connection.invoke(sbbUsageON, opName, new Object[]{false}, new String[]{"boolean"});
          col.put(new PropertySimple("usageParameterValue", value));
    
          columnList.add(col);
        }
      }
      config.put(columnList);
  
      return config;
    }
    finally {
      try {
        this.mbeanUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

  @Override
  public void updateResourceConfiguration(ConfigurationUpdateReport configurationUpdateReport) {
    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      // As an example, if a particular service has the name �FooService�, vendor �FooCompany�, and version �1.0�,
      // then the Object Name of a ServiceUsageMBean for that service would be:
      // �javax.slee.management.usage:type=ServiceUsage,serviceName="FooService", serviceVendor="FooCompany",serviceVersion="1.0"�
      ObjectName serviceUsageON = new ObjectName(ServiceUsageMBean.BASE_OBJECT_NAME + ',' + ServiceUsageMBean.SERVICE_NAME_KEY + '=' + ObjectName.quote(serviceId.getName()) + ','
          + ServiceUsageMBean.SERVICE_VENDOR_KEY + '=' + ObjectName.quote(serviceId.getVendor()) + ',' + ServiceUsageMBean.SERVICE_VERSION_KEY + '='
          + ObjectName.quote(serviceId.getVersion()));

      ServiceUsageMBean serviceUsageMBean = (ServiceUsageMBean) MBeanServerInvocationHandler.newProxyInstance(connection, serviceUsageON,
          javax.slee.management.ServiceUsageMBean.class, false);

      PropertyList columnList = configurationUpdateReport.getConfiguration().getList("usageParameter");
      ObjectName sbbUsageON = serviceUsageMBean.getSbbUsageMBean(sbbId);

      for (Property p : columnList.getList()) {
        PropertyMap pMap = (PropertyMap) p;
        String usageParamName = ((PropertySimple) pMap.get("usageParameterName")).getStringValue();
        Object curValue = pMap.get("usageParameterValue");

        Object newValue = connection.invoke(sbbUsageON, "get" + usageParamName, new Object[] { false }, new String[] { "boolean" });
        if (newValue != null && !newValue.equals(curValue)) {
          if (log.isDebugEnabled()) {
            log.debug("Changing Usage Parameter '" + usageParamName + "' from value [" + curValue + "] to [" + newValue + "].");
          }
        }
      }
      configurationUpdateReport.setStatus(ConfigurationUpdateStatus.SUCCESS);
    }
    catch (Exception e) {
      log.error("Failed to update Resource Configuration.", e);
      configurationUpdateReport.setErrorMessageFromThrowable(e);
      configurationUpdateReport.setStatus(ConfigurationUpdateStatus.FAILURE);
    }
    finally {
      try {
        this.mbeanUtils.logout();
      }
      catch (LoginException e) {
        if (log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }
}
