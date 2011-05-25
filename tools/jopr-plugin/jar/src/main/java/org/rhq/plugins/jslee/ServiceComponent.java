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

import java.util.ArrayList;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;
import javax.slee.ServiceID;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.ServiceState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.container.management.jmx.SbbEntitiesMBeanImplMBean;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataNumeric;
import org.rhq.core.domain.measurement.MeasurementDataTrait;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceComponent;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;
import org.rhq.core.pluginapi.operation.OperationFacet;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class ServiceComponent implements ResourceComponent<JainSleeServerComponent>, MeasurementFacet, OperationFacet {
  private final Log log = LogFactory.getLog(this.getClass());

  private ResourceContext<JainSleeServerComponent> resourceContext;
  private ServiceID serviceId = null;
  private MBeanServerUtils mbeanUtils = null;

  private ObjectName servicemanagement;
  private ServiceState serviceState = ServiceState.INACTIVE;

  public void start(ResourceContext<JainSleeServerComponent> context) throws InvalidPluginConfigurationException, Exception {
    if(log.isTraceEnabled()) {
      log.trace("start(" + context + ") called.");
    }

    this.resourceContext = context;
    this.servicemanagement = new ObjectName(ServiceManagementMBean.OBJECT_NAME);

    this.mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();

    String name = this.resourceContext.getPluginConfiguration().getSimple("name").getStringValue();
    String version = this.resourceContext.getPluginConfiguration().getSimple("version").getStringValue();
    String vendor = this.resourceContext.getPluginConfiguration().getSimple("vendor").getStringValue();

    serviceId = new ServiceID(name, vendor, version);
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

      serviceState = (ServiceState) connection.invoke(this.servicemanagement, "getState",
          new Object[] { this.serviceId }, new String[] { ServiceID.class.getName() });
    }
    catch (Exception e) {
      log.error("getAvailability failed for ServiceID = " + this.serviceId);
      this.serviceState = ServiceState.INACTIVE;
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
        report.addData(new MeasurementDataTrait(request, this.serviceState.toString()));
      }
      else if (request.getName().equals("SbbEntitiesCount")) {
        try {
          report.addData(new MeasurementDataNumeric(request, Double.valueOf(getServiceSbbEntities().size())));
        }
        catch (Exception e) {
          log.error("getAvailability failed for Service = " + this.serviceId);
          report.addData(new MeasurementDataNumeric(request, -1.0));
        }
      }
    }
  }

  public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException, Exception {
    if(log.isDebugEnabled()) {
      log.debug("invokeOperation(" + name + ", " + parameters + ") called.");
    }

    if ("changeServiceState".equals(name)) {
      return doChangeServiceState(parameters);
    }
    else if ("retrieveSbbEntities".equals(name)) {
      return doRetrieveSbbEntities();
    }
    else {
      throw new UnsupportedOperationException("Operation [" + name + "] is not supported.");
    }
  }

  private OperationResult doChangeServiceState(Configuration parameters) throws Exception {
    try {
      String message = null;
      String action = parameters.getSimple("action").getStringValue();

      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ServiceManagementMBean serviceManagementMBean = (ServiceManagementMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.servicemanagement, javax.slee.management.ServiceManagementMBean.class, false);
      if ("activate".equals(action)) {
        serviceManagementMBean.activate(this.serviceId);
        message = "Successfully Activated Service " + this.serviceId;
        this.serviceState = ServiceState.ACTIVE;
      }
      else if ("deactivate".equals(action)) {
        serviceManagementMBean.deactivate(this.serviceId);
        message = "Successfully Deactivated Service " + this.serviceId;
        this.serviceState = ServiceState.INACTIVE;
      }

      OperationResult result = new OperationResult();
      result.getComplexResults().put(new PropertySimple("result", message));
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

  private OperationResult doRetrieveSbbEntities() throws Exception {
    // The pretty table we are building as result
    PropertyList columnList = new PropertyList("result");

    for(Object[] sbbEntity : getServiceSbbEntities()) {
      PropertyMap col = new PropertyMap("element");

      col.put(new PropertySimple("SBB Entity Id", sbbEntity[0] == null ? "-" : sbbEntity[0]));
      col.put(new PropertySimple("Parent SBB Entity Id", sbbEntity[1] == null ? "-" : sbbEntity[1]));
      col.put(new PropertySimple("Priority", sbbEntity[4] == null ? "-" : sbbEntity[4]));
      col.put(new PropertySimple("Attachment Count", sbbEntity[9] == null ? "-" : ((String[])sbbEntity[9]).length));

      columnList.add(col);
    }

    OperationResult result = new OperationResult();
    result.getComplexResults().put(columnList);

    return result;
  }

  /**
   * Obtain all SBB Entities and filter the ones which service id is the same as this.
   * 
   * @return an ArrayList of SbbEntity
   * @throws Exception
   */
  private ArrayList<Object[]> getServiceSbbEntities() throws Exception {
    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      this.mbeanUtils.login();

      ObjectName sbbEntitiesMBeanObj = new ObjectName("org.mobicents.slee:name=SbbEntitiesMBean");
      SbbEntitiesMBeanImplMBean sbbEntititesMBean = (SbbEntitiesMBeanImplMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, sbbEntitiesMBeanObj, SbbEntitiesMBeanImplMBean.class, false);
      Object[] objs = sbbEntititesMBean.retrieveAllSbbEntities();

      ArrayList<Object[]> list = new ArrayList<Object[]>();
      for(Object obj : objs) {
        Object[] sbbEntity = (Object[])obj; 
        if(sbbEntity[7] != null && sbbEntity[7].equals(this.serviceId)) {
          list.add(sbbEntity);
        }
      }

      return list;
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

  public ServiceID getServiceID() {
    return this.serviceId;
  }

  public MBeanServerUtils getMBeanServerUtils() {
    return this.mbeanUtils;
  }

}
