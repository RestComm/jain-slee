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

import java.util.Date;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.JmxActivityContextHandle;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.ConfigurationUpdateStatus;
import org.rhq.core.domain.configuration.Property;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataNumeric;
import org.rhq.core.domain.measurement.MeasurementDataTrait;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.domain.resource.CreateResourceStatus;
import org.rhq.core.pluginapi.configuration.ConfigurationFacet;
import org.rhq.core.pluginapi.configuration.ConfigurationUpdateReport;
import org.rhq.core.pluginapi.inventory.CreateChildResourceFacet;
import org.rhq.core.pluginapi.inventory.CreateResourceReport;
import org.rhq.core.pluginapi.inventory.DeleteResourceFacet;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;
import org.rhq.core.pluginapi.operation.OperationFacet;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.jslee.utils.JainSleeServerUtils;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;
import org.rhq.plugins.jslee.utils.ResourceAdaptorUtils;

public class RAEntityComponent implements ResourceAdaptorUtils, ConfigurationFacet, DeleteResourceFacet, CreateChildResourceFacet, OperationFacet, MeasurementFacet {

  private final Log log = LogFactory.getLog(this.getClass());

  private ResourceContext<ResourceAdaptorComponent> resourceContext;
  private String raEntityName;
  private ResourceAdaptorID raId = null;
  private MBeanServerUtils mbeanUtils = null;

  private ConfigProperties configProperties = null;

  private ObjectName resourceManagement;

  public void start(ResourceContext context) throws InvalidPluginConfigurationException, Exception {
    if(log.isTraceEnabled()) {
      log.trace("start(" + context + ") called.");
    }

    this.resourceContext = context;
    this.resourceManagement = new ObjectName(ResourceManagementMBean.OBJECT_NAME);

    this.mbeanUtils = ((JainSleeServerUtils) context.getParentResourceComponent()).getMBeanServerUtils();

    String name = this.resourceContext.getPluginConfiguration().getSimple("name").getStringValue();
    String version = this.resourceContext.getPluginConfiguration().getSimple("version").getStringValue();
    String vendor = this.resourceContext.getPluginConfiguration().getSimple("vendor").getStringValue();

    raEntityName = this.resourceContext.getPluginConfiguration().getSimple("entityName").getStringValue();

    this.raId = new ResourceAdaptorID(name, vendor, version);
  }

  public void stop() {
    if(log.isTraceEnabled()) {
      log.trace("stop() called.");
    }
  }

  public AvailabilityType getAvailability() {
    if(log.isTraceEnabled()) {
      log.trace("getAvailability() called.");
    }

    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.resourceManagement, javax.slee.management.ResourceManagementMBean.class, false);

      configProperties = resourceManagementMBean.getConfigurationProperties(this.raEntityName);
    }
    catch (Exception e) {
      log.error("getAvailability failed for ResourceAdaptor Entity = " + this.raEntityName);
      return AvailabilityType.DOWN;
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

    return AvailabilityType.UP;
  }

  public void getValues(MeasurementReport report, Set<MeasurementScheduleRequest> metrics) throws Exception {
    if(log.isTraceEnabled()) {
      log.trace("getValues(" + report + "," + metrics + ") called.");
    }

    for (MeasurementScheduleRequest request : metrics) {
      if (request.getName().equals("state")) {
        report.addData(new MeasurementDataTrait(request, this.getState().toString()));
      }
      if (request.getName().equals("activites")) {
        Object[] activities = getActivityContextID();
        report.addData(new MeasurementDataNumeric(request, activities == null ? 0.0 : (double) activities.length));
      }
    }
  }

  private ResourceAdaptorEntityState getState() throws Exception {
    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.resourceManagement, javax.slee.management.ResourceManagementMBean.class, false);

      return resourceManagementMBean.getState(this.raEntityName);
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

  private Object[] getActivityContextID() throws Exception {
    try {
      ObjectName actMana = new ObjectName("org.mobicents.slee:name=ActivityManagementMBean");

      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ActivityManagementMBeanImplMBean aciManagMBean = (ActivityManagementMBeanImplMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, actMana, org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean.class, false);

      Object[] activities = aciManagMBean.retrieveActivityContextIDByResourceAdaptorEntityName(this.raEntityName);

      return activities;
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

  public Configuration loadResourceConfiguration() throws Exception {
    Configuration config = new Configuration();

    config.put(new PropertySimple("entityName", this.raEntityName));

    PropertyList columnList = new PropertyList("properties");
    for (ConfigProperties.Property confProp : this.configProperties.getProperties()) {
      PropertyMap col = new PropertyMap("propertyDefinition");

      col.put(new PropertySimple("propertyName", confProp.getName()));
      col.put(new PropertySimple("propertyType", confProp.getType()));
      col.put(new PropertySimple("propertyValue", confProp.getValue()));

      columnList.add(col);
    }

    config.put(columnList);

    return config;
  }

  public void updateResourceConfiguration(ConfigurationUpdateReport configurationUpdateReport) {
    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.resourceManagement, javax.slee.management.ResourceManagementMBean.class, false);

      // Get the JOPR updated properties
      PropertyList propsList = configurationUpdateReport.getConfiguration().getList("properties");

      // Get the current RA Entity properties
      ConfigProperties cps = resourceManagementMBean.getConfigurationProperties(this.raEntityName);

      for(Property p : propsList.getList()) {
        PropertyMap pMap = (PropertyMap)p;
        String propName = ((PropertySimple)pMap.get("propertyName")).getStringValue();
        String propType = ((PropertySimple)pMap.get("propertyType")).getStringValue();
        String propValue = ((PropertySimple)pMap.get("propertyValue")).getStringValue();
        Object value = null;
        if (propType.equals("java.lang.String")) {
          value = propValue;
        }
        else if (propType.equals("java.lang.Integer")) {
          value = Integer.valueOf(propValue);
        }
        else if (propType.equals("java.lang.Long")) {
          value = Long.valueOf(propValue);
        }
        ConfigProperties.Property cp = cps.getProperty(propName);
        if(value != null && cp != null && !value.equals(cp.getValue())) {
          if(log.isDebugEnabled()) {
            log.debug("Changing property '" + propName + "' from value [" + cp.getValue() + "] to [" + value  + "].");
          }
          cp.setValue(value);
        }
      }
      resourceManagementMBean.updateConfigurationProperties(this.raEntityName, cps);
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
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

  public void deleteResource() throws Exception {
    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.resourceManagement, javax.slee.management.ResourceManagementMBean.class, false);

      resourceManagementMBean.removeResourceAdaptorEntity(this.raEntityName);
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

  public ResourceAdaptorID getResourceAdaptorID() {
    return this.raId;
  }

  public MBeanServerUtils getMBeanServerUtils() {
    return this.mbeanUtils;
  }

  public String getRAEntityName() {
    return this.raEntityName;
  }

  public CreateResourceReport createResource(CreateResourceReport report) {
    try {
      Configuration configuration = report.getResourceConfiguration();

      String linkName = configuration.getSimple("linkName").getStringValue();

      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.resourceManagement,javax.slee.management.ResourceManagementMBean.class, false);

      report.setResourceKey(linkName);
      resourceManagementMBean.bindLinkName(this.raEntityName, linkName);

      report.setStatus(CreateResourceStatus.SUCCESS);
      report.setResourceName(linkName);
    }
    catch (Exception e) {
      log.error("Adding new ResourceAdaptor Entity failed ", e);
      report.setException(e);
      report.setStatus(CreateResourceStatus.FAILURE);
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

    return report;
  }

  public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException, Exception {
    if(log.isDebugEnabled()) {
      log.debug("invokeOperation(" + name + ", " + parameters + ") called.");
    }

    OperationResult result = new OperationResult();
    if ("changeRaEntityState".equals(name)) {
      result = doChangeRaEntityState(parameters);
    }
    else if ("listActivityContexts".equals(name)) {
      result = doListActivityContexts();
    }
    else {
      throw new UnsupportedOperationException("Operation [" + name + "] is not supported yet.");
    }

    return result;
  }

  private OperationResult doChangeRaEntityState(Configuration parameters) throws Exception {
    try {
      String message = null;
      String action = parameters.getSimple("action").getStringValue();

      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler
      .newProxyInstance(connection, this.resourceManagement,
          javax.slee.management.ResourceManagementMBean.class, false);
      if ("activate".equals(action)) {
        resourceManagementMBean.activateResourceAdaptorEntity(this.raEntityName);
        message = "Successfully Activated Resource Adaptor Entity " + this.raEntityName;
      }
      else if ("deactivate".equals(action)) {
        resourceManagementMBean.deactivateResourceAdaptorEntity(this.raEntityName);
        message = "Successfully DeActivated Resource Adaptor Entity " + this.raEntityName;
      }

      OperationResult result = new OperationResult();
      result .getComplexResults().put(new PropertySimple("result", message));

      return result;
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

  private OperationResult doListActivityContexts() throws Exception {
    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ObjectName actMana = new ObjectName("org.mobicents.slee:name=ActivityManagementMBean");

      ActivityManagementMBeanImplMBean aciManagMBean = (ActivityManagementMBeanImplMBean) MBeanServerInvocationHandler
      .newProxyInstance(connection, actMana,
          org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean.class, false);

      Object[] activities = aciManagMBean.retrieveActivityContextIDByResourceAdaptorEntityName(this.raEntityName);
      // Object[] activities = aciManagMBean.listActivityContexts(true);

      PropertyList columnList = new PropertyList("result");
      if (activities != null) {
        for (Object obj : activities) {
          Object[] tempObjects = (Object[]) obj;
          PropertyMap col = new PropertyMap("element");

          Object tempObj = tempObjects[0];
          PropertySimple activityHandle = new PropertySimple("ActivityHandle",
              tempObj != null ? ((JmxActivityContextHandle) tempObj).getActivityHandleToString() : "-");

          col.put(activityHandle);

          col.put(new PropertySimple("Class", tempObjects[1]));

          tempObj = tempObjects[2];
          Date d = new Date(Long.parseLong((String) tempObj));
          col.put(new PropertySimple("LastAccessTime", d));

          tempObj = tempObjects[3];
          col.put(new PropertySimple("ResourceAdaptor", tempObj == null ? "-" : tempObj));

          tempObj = tempObjects[4];
          // PropertyList propertyList = new PropertyList("SbbAttachments");
          String[] strArr = (String[]) tempObj;
          StringBuffer sb = new StringBuffer();
          for (String s : strArr) {
            // PropertyMap SbbAttachment = new PropertyMap("SbbAttachment");
            // SbbAttachment.put(new PropertySimple("SbbAttachmentValue", s));
            // propertyList.add(SbbAttachment);
            sb.append(s).append("; ");
          }
          col.put(new PropertySimple("SbbAttachmentValue", sb.toString()));

          tempObj = tempObjects[5];
          // propertyList = new PropertyList("NamesBoundTo");
          sb = new StringBuffer();
          strArr = (String[]) tempObj;
          for (String s : strArr) {
            // PropertyMap NameBoundTo = new PropertyMap("NameBoundTo");
            // NameBoundTo.put(new PropertySimple("NameBoundToValue", s));
            // propertyList.add(NameBoundTo);
            sb.append(s).append("; ");
          }
          col.put(new PropertySimple("NameBoundToValue", sb.toString()));

          tempObj = tempObjects[6];
          // propertyList = new PropertyList("Timers");
          sb = new StringBuffer();
          strArr = (String[]) tempObj;
          for (String s : strArr) {
            // PropertyMap Timer = new PropertyMap("Timer");
            // Timer.put(new PropertySimple("TimerValue", s));
            // propertyList.add(Timer);
            sb.append(s).append("; ");
          }
          col.put(new PropertySimple("TimerValue", sb.toString()));

          tempObj = tempObjects[7];
          // propertyList = new PropertyList("DataProperties");
          sb = new StringBuffer();
          strArr = (String[]) tempObj;
          for (String s : strArr) {
            // PropertyMap DataProperty = new PropertyMap("DataProperty");
            // DataProperty.put(new PropertySimple("DataPropertyValue", s));
            // propertyList.add(DataProperty);
            sb.append(s).append("; ");
          }
          col.put(new PropertySimple("DataPropertyValue", sb.toString()));

          columnList.add(col);
        }
      }

      OperationResult result = new OperationResult();
      result.getComplexResults().put(columnList);

      return result;
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
}
