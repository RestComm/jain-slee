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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;
import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.content.PackageType;
import org.rhq.core.domain.content.transfer.ContentResponseResult;
import org.rhq.core.domain.content.transfer.DeployPackageStep;
import org.rhq.core.domain.content.transfer.DeployPackagesResponse;
import org.rhq.core.domain.content.transfer.RemovePackagesResponse;
import org.rhq.core.domain.content.transfer.ResourcePackageDetails;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.pluginapi.content.ContentFacet;
import org.rhq.core.pluginapi.content.ContentServices;
import org.rhq.core.pluginapi.inventory.DeleteResourceFacet;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceComponent;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.operation.OperationFacet;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class DeployableUnitComponent implements ResourceComponent<JainSleeServerComponent>, OperationFacet, DeleteResourceFacet, ContentFacet {
  private final Log log = LogFactory.getLog(this.getClass());

  private ResourceContext<JainSleeServerComponent> resourceContext;
  private DeployableUnitID deployableUnitID = null;
  private MBeanServerUtils mbeanUtils = null;

  private ObjectName deploymentObjName;

  private String deployPathIdentifier;
  private String farmDeployPathIdentifier;

  public void start(ResourceContext<JainSleeServerComponent> context) throws InvalidPluginConfigurationException, Exception {
    if(log.isTraceEnabled()) {
      log.trace("start(" + context + ") called.");
    }

    this.resourceContext = context;
    this.deploymentObjName = new ObjectName(DeploymentMBean.OBJECT_NAME);

    this.mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();

    String url = this.resourceContext.getPluginConfiguration().getSimple("url").getStringValue();
    this.deployableUnitID = new DeployableUnitID(url);

    this.deployPathIdentifier = resourceContext.getParentResourceComponent().getDeployFolderPath();
    this.farmDeployPathIdentifier = resourceContext.getParentResourceComponent().getFarmDeployFolderPath();
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
      mbeanUtils.login();
      DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.deploymentObjName, javax.slee.management.DeploymentMBean.class, false);

      DeployableUnitDescriptor deployableUnitDescriptor = deploymentMBean.getDescriptor(this.deployableUnitID);
      deployableUnitDescriptor.getDeploymentDate();
    }
    catch (Exception e) {
      log.error("getAvailability failed for DeployableUnitID = " + this.deployableUnitID);

      return AvailabilityType.DOWN;
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

    return AvailabilityType.UP;
  }

  public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException, Exception {
    if(log.isDebugEnabled()) {
      log.debug("invokeOperation(" + name + ", " + parameters + ") called.");
    }

    // List the DU Components IDs
    if ("listComponents".equals(name)) {
      return doListComponents();
    }
    else {
      throw new UnsupportedOperationException("Operation [" + name + "] is not supported.");
    }
  }

  public void deleteResource() throws Exception {
    if(deployableUnitID.getURL().contains(deployPathIdentifier.replaceAll("\\\\", "/"))) {
      if(!new File(new URL(deployableUnitID.getURL()).toURI()).delete()) {
        throw new IOException("File '" + deployableUnitID.getURL() + "' could not be deleted. Does it exists?");
      }
    }
    else if(deployableUnitID.getURL().contains(farmDeployPathIdentifier.replaceAll("\\\\", "/"))) {
      if(!new File(new URL(deployableUnitID.getURL()).toURI()).delete()) {
        throw new IOException("File '" + deployableUnitID.getURL() + "' could not be deleted. Does it exists?");
      }
    }
    else {
      try {
        MBeanServerConnection connection = this.mbeanUtils.getConnection();
        mbeanUtils.login();
        
        DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection,
            this.deploymentObjName, javax.slee.management.DeploymentMBean.class, false);
        deploymentMBean.uninstall(this.deployableUnitID);
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

  public DeployPackagesResponse deployPackages(Set<ResourcePackageDetails> packages, ContentServices contentServices) {
    if(log.isTraceEnabled()) {
      log.trace("deployPackages(" + packages + "," + contentServices + ") called.");
    }

    String resourceTypeName = this.resourceContext.getResourceType().getName();

    if (packages.size() != 1) {
      log.warn("Request to update " + resourceTypeName + " file contained multiple packages: " + packages);
      DeployPackagesResponse response = new DeployPackagesResponse(ContentResponseResult.FAILURE);
      response.setOverallRequestErrorMessage("Only one " + resourceTypeName + " can be updated at a time.");
      return response;
    }

    ResourcePackageDetails packageDetails = packages.iterator().next();

    if(log.isDebugEnabled()) {
      log.debug("Updating DU file ' ' using [" + packageDetails + "]...");
    }

    return null;
  }

  public Set<ResourcePackageDetails> discoverDeployedPackages(PackageType packageType) {
    if(log.isTraceEnabled()) {
      log.trace("discoverDeployedPackages(" + packageType + ") called.");
    }
    
    // TODO Auto-generated method stub
    return null;
  }

  public List<DeployPackageStep> generateInstallationSteps(ResourcePackageDetails arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  public RemovePackagesResponse removePackages(Set<ResourcePackageDetails> arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  public InputStream retrievePackageBits(ResourcePackageDetails arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  private OperationResult doListComponents() throws Exception {
    try {
      OperationResult result = new OperationResult();

      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      mbeanUtils.login();

      DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.deploymentObjName, javax.slee.management.DeploymentMBean.class, false);

      DeployableUnitDescriptor deployableUnitDescriptor = deploymentMBean.getDescriptor(this.deployableUnitID);

      ComponentID[] components = deployableUnitDescriptor.getComponents();

      // The pretty table we are building as result
      PropertyList columnList = new PropertyList("result");

      // Add the components
      for (ComponentID componentID : components) {
        PropertyMap col = new PropertyMap("element");

        col.put(new PropertySimple("Name", componentID.getName()));
        col.put(new PropertySimple("Vendor", componentID.getVendor()));
        col.put(new PropertySimple("Version", componentID.getVersion()));

        columnList.add(col);
      }

      result.getComplexResults().put(columnList);

      return result;
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
