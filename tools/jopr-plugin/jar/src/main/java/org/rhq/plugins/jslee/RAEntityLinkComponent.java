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

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;
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

  private final Log log = LogFactory.getLog(RAEntityLinkComponent.class);

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
    if(log.isTraceEnabled()) {
      log.trace("start(" + context + ") called.");
    }

    this.resourceContext = context;
    this.resourceManagement = new ObjectName(ResourceManagementMBean.OBJECT_NAME);

    this.mbeanUtils = ((RAEntityComponent) context.getParentResourceComponent()).getMBeanServerUtils();

    this.raEntityLinkName = this.resourceContext.getPluginConfiguration().getSimple("linkName").getStringValue();
    this.raEntityName = ((RAEntityComponent) context.getParentResourceComponent()).getRAEntityName();
    this.raId = ((RAEntityComponent) context.getParentResourceComponent()).getResourceAdaptorID();
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

      this.boundSbbs = resourceManagementMBean.getBoundSbbs(this.raEntityLinkName);

    }
    catch (Exception e) {
      log.error("getAvailability failed for RAEntityLinkComponent Link = " + this.raEntityLinkName);

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

  public MBeanServerUtils getMBeanServerUtils() {
    return this.mbeanUtils;
  }

  public void deleteResource() throws Exception {
    if(log.isTraceEnabled()) {
      log.trace("deleteResource() called.");
    }

    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.resourceManagement, javax.slee.management.ResourceManagementMBean.class, false);

      resourceManagementMBean.unbindLinkName(this.raEntityLinkName);
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

  public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException, Exception {
    if(log.isDebugEnabled()) {
      log.debug("invokeOperation(" + name + ", " + parameters + ") called.");
    }

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
    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

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
