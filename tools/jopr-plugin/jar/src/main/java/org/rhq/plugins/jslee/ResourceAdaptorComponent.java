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

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorDescriptor;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.Configuration;
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
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;
import org.rhq.plugins.jslee.utils.JainSleeServerUtils;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;
import org.rhq.plugins.jslee.utils.ResourceAdaptorUtils;

public class ResourceAdaptorComponent implements ResourceAdaptorUtils, MeasurementFacet, ConfigurationFacet,
CreateChildResourceFacet {
  private final Log log = LogFactory.getLog(this.getClass());

  private ResourceContext<JainSleeServerComponent> resourceContext;
  private ResourceAdaptorID raId = null;
  private MBeanServerUtils mbeanUtils = null;

  private ConfigProperties configProperties = null;

  private ObjectName resourceManagement;
  private ObjectName deploymentManagement;

  public void start(ResourceContext context) throws InvalidPluginConfigurationException, Exception {
    if(log.isTraceEnabled()) {
      log.trace("start(" + context + ") called.");
    }

    this.resourceContext = context;
    this.resourceManagement = new ObjectName(ResourceManagementMBean.OBJECT_NAME);
    this.deploymentManagement = new ObjectName(DeploymentMBean.OBJECT_NAME);

    this.mbeanUtils = ((JainSleeServerUtils) context.getParentResourceComponent()).getMBeanServerUtils();

    String name = this.resourceContext.getPluginConfiguration().getSimple("name").getStringValue();
    String version = this.resourceContext.getPluginConfiguration().getSimple("version").getStringValue();
    String vendor = this.resourceContext.getPluginConfiguration().getSimple("vendor").getStringValue();

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

      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler
      .newProxyInstance(connection, this.resourceManagement,
          javax.slee.management.ResourceManagementMBean.class, false);

      configProperties = resourceManagementMBean.getConfigurationProperties(this.raId);

    }
    catch (Exception e) {
      log.error("getAvailability failed for ResourceAdaptorID = " + this.raId);

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

    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      for (MeasurementScheduleRequest request : metrics) {
        if (request.getName().equals("entities")) {
          ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler
          .newProxyInstance(connection, this.resourceManagement,
              javax.slee.management.ResourceManagementMBean.class, false);

          String[] raEntities = resourceManagementMBean.getResourceAdaptorEntities(this.raId);
          report.addData(new MeasurementDataNumeric(request, (double) raEntities.length));
        }
        else if (request.getName().equals("ratype")) {
          DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection, deploymentManagement, javax.slee.management.DeploymentMBean.class, false);
          ResourceAdaptorTypeID[] raTypes = ((ResourceAdaptorDescriptor)deploymentMBean.getDescriptor(raId)).getResourceAdaptorTypes();
          String raTypesString = "";
          for (ResourceAdaptorTypeID raTypeId : raTypes) {
            raTypesString += raTypeId + "; ";
          }
          report.addData(new MeasurementDataTrait(request, raTypesString));
        }
      }
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

  public Configuration loadResourceConfiguration() throws Exception {
    Configuration config = new Configuration();

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

  public void updateResourceConfiguration(ConfigurationUpdateReport arg0) {
    // No update is allowed isnt it?
  }

  public CreateResourceReport createResource(CreateResourceReport report) {
    try {
      Configuration configuration = report.getResourceConfiguration();

      String entityName = configuration.getSimple("entityName").getStringValue();
      PropertyList columnList = configuration.getList("properties");

      ConfigProperties props = new ConfigProperties();

      for (Property c : columnList.getList()) {
        PropertyMap column = (PropertyMap) c;

        String propName = column.getSimple("propertyName").getStringValue();
        String propType = column.getSimple("propertyType").getStringValue();
        String propValue = column.getSimple("propertyValue").getStringValue();

        if(log.isDebugEnabled()) {
          log.debug("Property (name=" + propName + ", type=" + propType + ", value=" + propValue + ")");
        }

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
        ConfigProperties.Property p = new ConfigProperties.Property(propName, propType, value);
        props.addProperty(p);
      }

      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler
      .newProxyInstance(connection, this.resourceManagement,
          javax.slee.management.ResourceManagementMBean.class, false);

      report.setResourceKey(entityName);
      resourceManagementMBean.createResourceAdaptorEntity(this.raId, entityName, props);

      report.setStatus(CreateResourceStatus.SUCCESS);
      report.setResourceName(entityName);
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

}
