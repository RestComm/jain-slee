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
import org.rhq.plugins.jslee.jbossas5.ApplicationServerPluginConfigurationProperties;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class DeployableUnitComponent implements ResourceComponent<JainSleeServerComponent>, OperationFacet, DeleteResourceFacet, ContentFacet {
  private final Log log = LogFactory.getLog(this.getClass());

  private ResourceContext<JainSleeServerComponent> resourceContext;
  private DeployableUnitID deployableUnitID = null;
  private MBeanServerUtils mbeanUtils = null;

  private ObjectName deploymentObjName;

  private String deployPathIdentifier;
  
  public void start(ResourceContext<JainSleeServerComponent> context) throws InvalidPluginConfigurationException, Exception {
    log.info("DeployableUnitComponent.start");

    this.resourceContext = context;
    this.deploymentObjName = new ObjectName(DeploymentMBean.OBJECT_NAME);

    this.mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();

    String url = this.resourceContext.getPluginConfiguration().getSimple("url").getStringValue();
    this.deployableUnitID = new DeployableUnitID(url);

    this.deployPathIdentifier = resourceContext.getParentResourceComponent().getDeployFolderPath();
  }

  public void stop() {
    log.info("DeployableUnitComponent.stop");
  }

  public AvailabilityType getAvailability() {
    log.info("getAvailability");
    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(
          connection, this.deploymentObjName, javax.slee.management.DeploymentMBean.class, false);

      DeployableUnitDescriptor deployableUnitDescriptor = deploymentMBean.getDescriptor(this.deployableUnitID);
      deployableUnitDescriptor.getDeploymentDate();
    }
    catch (Exception e) {
      log.error("getAvailability failed for DeployableUnitID = " + this.deployableUnitID);

      return AvailabilityType.DOWN;
    }

    return AvailabilityType.UP;
  }

  public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException, Exception {
    log.info("DeployableUnitComponent.invokeOperation() with name = " + name);

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
    else {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
      DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection,
          this.deploymentObjName, javax.slee.management.DeploymentMBean.class, false);
      deploymentMBean.uninstall(this.deployableUnitID);
    }
  }

  public DeployPackagesResponse deployPackages(Set<ResourcePackageDetails> packages, ContentServices contentServices) {

    log.info("DeployableUnitComponent.deployPackages()");

    String resourceTypeName = this.resourceContext.getResourceType().getName();

    if (packages.size() != 1) {
      log.warn("Request to update " + resourceTypeName + " file contained multiple packages: " + packages);
      DeployPackagesResponse response = new DeployPackagesResponse(ContentResponseResult.FAILURE);
      response.setOverallRequestErrorMessage("Only one " + resourceTypeName + " can be updated at a time.");
      return response;
    }

    ResourcePackageDetails packageDetails = packages.iterator().next();

    log.info("Updating DU file ' ' using [" + packageDetails + "]...");

    return null;
  }

  public Set<ResourcePackageDetails> discoverDeployedPackages(PackageType packageType) {
    log.info("DeployableUnitComponent.discoverDeployedPackages() "+ packageType.getDisplayName());
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
    OperationResult result = new OperationResult();

    MBeanServerConnection connection = this.mbeanUtils.getConnection();
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


}
