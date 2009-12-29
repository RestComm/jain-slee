package org.rhq.plugins.jslee;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.JmxActivityContextHandle;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.content.PackageDetailsKey;
import org.rhq.core.domain.content.transfer.ResourcePackageDetails;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataTrait;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.domain.resource.CreateResourceStatus;
import org.rhq.core.domain.resource.ResourceCreationDataType;
import org.rhq.core.domain.resource.ResourceType;
import org.rhq.core.pluginapi.content.ContentContext;
import org.rhq.core.pluginapi.content.ContentServices;
import org.rhq.core.pluginapi.inventory.CreateChildResourceFacet;
import org.rhq.core.pluginapi.inventory.CreateResourceReport;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;
import org.rhq.core.pluginapi.operation.OperationFacet;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.jslee.jbossas5.ApplicationServerPluginConfigurationProperties;
import org.rhq.plugins.jslee.utils.JainSleeServerUtils;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class JainSleeServerComponent implements JainSleeServerUtils, MeasurementFacet, OperationFacet, CreateChildResourceFacet {
  private final Log log = LogFactory.getLog(this.getClass());

  volatile MBeanServerUtils mBeanServerUtils = null;

  volatile private SleeState sleeState = SleeState.STOPPED;

  private ResourceContext resourceContext;

  public MBeanServerUtils getMBeanServerUtils() {
    return this.mBeanServerUtils;
  }

  public void start(ResourceContext resourceContext) throws InvalidPluginConfigurationException, Exception {
    log.info("start called");

    this.resourceContext = resourceContext;

    this.deployFolder = resourceContext.getPluginConfiguration().getSimple(ApplicationServerPluginConfigurationProperties.SERVER_HOME_DIR).getStringValue() + File.separator  + "deploy";
    // Connect to the JBAS instance's Profile Service and JMX MBeanServer.

    Configuration pluginConfig = resourceContext.getPluginConfiguration();

    String namingURL = pluginConfig.getSimple("namingURL").getStringValue();
    String principal = pluginConfig.getSimple("principal").getStringValue();
    String credentials = pluginConfig.getSimple("credentials").getStringValue();

    this.mBeanServerUtils = new MBeanServerUtils(namingURL);
  }

  public void stop() {
    // TODO Auto-generated method stub
  }

  public AvailabilityType getAvailability() {
    log.info("getAvailability called " + this.mBeanServerUtils);
    if (this.mBeanServerUtils != null) {

      try {
        ObjectName sleemanagement = new ObjectName(SleeManagementMBean.OBJECT_NAME);
        MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
        this.sleeState = (SleeState) connection.getAttribute(sleemanagement, "State");
        log.info("SleeState = " + this.sleeState);
        return AvailabilityType.UP;

      }
      catch (Exception e) {
        log.error("Failed to obtain JAIN SLEE Server state.", e);
        return AvailabilityType.DOWN;
      }
    }
    else {
      log.error("Returning availability as DOWN");
      return AvailabilityType.DOWN;
    }
  }

  public void getValues(MeasurementReport report, Set<MeasurementScheduleRequest> metrics) throws Exception {
    log.info("getValues() called hurray");
    for (MeasurementScheduleRequest request : metrics) {
      if (request.getName().equals("state")) {
        report.addData(new MeasurementDataTrait(request, this.sleeState.toString()));
      }
    }
  }

  public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException, Exception {
    log.info("invokeOperation() with name = " + name);

    if ("sleeState".equals(name)) {
      return doSleeState(parameters);
    }
    else if ("listActivityContexts".equals(name)) {
      return doListActivityContexts(parameters);
    }
    else if ("queryActivityContextLiveness".equals(name)) {
      return doQueryActivityContextLiveness(parameters);
    }
    else {
      throw new UnsupportedOperationException("Operation [" + name + "] is not supported.");
    }
  }

  private static void validateNamingURL(String namingURL) {
    URI namingURI;
    try {
      namingURI = new URI(namingURL);
    } catch (URISyntaxException e) {
      throw new RuntimeException("Naming URL '" + namingURL + "' is not valid: " + e.getLocalizedMessage());
    }
    if (!namingURI.isAbsolute())
      throw new RuntimeException("Naming URL '" + namingURL + "' is not absolute.");
    if (!namingURI.getScheme().equals("jnp"))
      throw new RuntimeException("Naming URL '" + namingURL + "' has an invalid protocol - the only valid protocol is 'jnp'.");
  }

  public CreateResourceReport createResource(CreateResourceReport createResourceReport) {
    ResourceType resourceType = createResourceReport.getResourceType();
    if (resourceType.getCreationDataType() == ResourceCreationDataType.CONTENT)
      createContentBasedResource(createResourceReport, resourceType);

    return createResourceReport;
  }

  private void createContentBasedResource(CreateResourceReport createResourceReport, ResourceType resourceType) {
    log.info("JainSleeServerComponent.createContentBasedResource");
    try {
      OutputStream os = null;

      ResourcePackageDetails details = createResourceReport.getPackageDetails();
      PackageDetailsKey key = details.getKey();
      String archiveName = key.getName();

      log.info("JainSleeServerComponent.createContentBasedResource archiveName = " + archiveName);

      Configuration deployTimeConfig = createResourceReport.getPackageDetails().getDeploymentTimeConfiguration();
      //boolean deployFarmed = deployTimeConfig.getSimple("deployFarmed").getBooleanValue();
      //log.info("JainSleeServerComponent.createContentBasedResource deployFarmed = " + deployFarmed);

      boolean persistentDeploy = deployTimeConfig.getSimple("persistentDeploy").getBooleanValue();
      log.info("JainSleeServerComponent.createContentBasedResource persistentDeploy = " + persistentDeploy);

      // Validate file name
      if (!archiveName.toLowerCase().endsWith(".jar")) {
        createResourceReport.setStatus(CreateResourceStatus.FAILURE);
        createResourceReport.setErrorMessage("Deployed file must have a .jar extension");
        return;
      }

      Configuration pluginConfiguration = this.resourceContext.getPluginConfiguration();
      PropertySimple propSimple = pluginConfiguration.getSimple(ApplicationServerPluginConfigurationProperties.SERVER_TMP_DIR);
      String tmpPath = propSimple.getStringValue();

      File serverTmpFile = new File(tmpPath);
      File tempDir = null;

      tempDir = createTempDirectory("jopr-jainslee-deploy-content", null, serverTmpFile);

      log.info("tmpFile = " + tempDir.getAbsolutePath());

      File archiveFile = new File(key.getName());

      // this is to ensure that we only get the filename part no matter whether the key contains full path or not.
      File contentCopy = new File(tempDir, archiveFile.getName());

      os = new BufferedOutputStream(new FileOutputStream(contentCopy));
      ContentContext contentContext = resourceContext.getContentContext();
      ContentServices contentServices = contentContext.getContentServices();
      contentServices.downloadPackageBitsForChildResource(contentContext, resourceType.getName(), key, os);

      log.info("contentCopy = " + contentCopy.getAbsolutePath());

      ObjectName deploymentObjName = new ObjectName(DeploymentMBean.OBJECT_NAME);
      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
          deploymentObjName, javax.slee.management.DeploymentMBean.class, false);

      String depKey = null;
      
      if(!persistentDeploy) {
        DeployableUnitID deployableUnitID = deploymentMBean.install(contentCopy.toURI().toURL().toString());
        log.info("Deployed "+ deployableUnitID );

        depKey = deployableUnitID.getURL();
      }
      else {
        File destination = new File(this.deployFolder + File.separator + archiveName);
        copyFile(contentCopy, destination);

        // Since toURL is deprecated
        depKey = destination.toURI().toURL().toString();
      }

      String[] elements = depKey.split(System.getProperty("file.separator").replaceAll("\\\\", "\\\\\\\\"));
      String lastElement = elements[(elements.length - 1)];
      String name = lastElement.substring(0, lastElement.lastIndexOf("."));

      createResourceReport.setResourceName(name);
      createResourceReport.setResourceKey(depKey);
      createResourceReport.setStatus(CreateResourceStatus.SUCCESS);
    }
    catch (Exception e) {
      createResourceReport.setStatus(CreateResourceStatus.FAILURE);
      createResourceReport.setErrorMessage(e.getMessage());
      createResourceReport.setException(e);
      return;
    }
  }

  private static File createTempDirectory(String prefix, String suffix, File parentDirectory) throws IOException {
    // Let's reuse the algorithm the JDK uses to determine a unique name:
    // 1) create a temp file to get a unique name using JDK createTempFile
    // 2) then quickly delete the file and...
    // 3) convert it to a directory

    File tmpDir = File.createTempFile(prefix, suffix, parentDirectory); // create file with unique name
    boolean deleteOk = tmpDir.delete(); // delete the tmp file and...
    boolean mkdirsOk = tmpDir.mkdirs(); // ...convert it to a directory

    if (!deleteOk || !mkdirsOk) {
      throw new IOException("Failed to create temp directory named [" + tmpDir + "]");
    }

    return tmpDir;
  }



  private boolean runningEmbedded() {
    return false;
  }

  private OperationResult doSleeState(Configuration parameters) throws Exception {
    OperationResult result = new OperationResult();

    String message = null;
    String action = parameters.getSimple("action").getStringValue();
    ObjectName sleemanagement = new ObjectName(SleeManagementMBean.OBJECT_NAME);
    SleeManagementMBean sleeManagementMBean = (SleeManagementMBean) MBeanServerInvocationHandler.newProxyInstance(this.mBeanServerUtils.getConnection(),
        sleemanagement, javax.slee.management.SleeManagementMBean.class, false);

    if ("start".equals(action)) {
      sleeManagementMBean.start();
      message = "Successfully started Mobicents JAIN SLEE Server";
    }
    else if ("stop".equals(action)) {
      sleeManagementMBean.stop();
      message = "Successfully stopped Mobicents JAIN SLEE Server";
    }
    else if ("shutdown".equals(action)) {
      sleeManagementMBean.shutdown();
      message = "Successfully shutdown Mobicents JAIN SLEE Server";
    }
    result.getComplexResults().put(new PropertySimple("result", message));

    return result;
  }

  private OperationResult doQueryActivityContextLiveness(Configuration parameters) throws Exception {
    OperationResult result = new OperationResult();

    MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
    ObjectName actMana = new ObjectName("org.mobicents.slee:name=ActivityManagementMBean");

    ActivityManagementMBeanImplMBean aciManagMBean = (ActivityManagementMBeanImplMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
        actMana, org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean.class, false);

    aciManagMBean.queryActivityContextLiveness();

    result.getComplexResults().put(new PropertySimple("result", "Activity Context Liveness queried successfully."));

    return result;
  }

  private OperationResult doListActivityContexts(Configuration paramteres) throws Exception {
    OperationResult result = new OperationResult();

    MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
    ObjectName actMana = new ObjectName("org.mobicents.slee:name=ActivityManagementMBean");

    ActivityManagementMBeanImplMBean aciManagMBean = (ActivityManagementMBeanImplMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
        actMana, org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean.class, false);

    Object[] activities = aciManagMBean.listActivityContexts(true);

    PropertyList columnList = new PropertyList("result");
    if (activities != null) {
      for (Object obj : activities) {
        Object[] tempObjects = (Object[]) obj;
        PropertyMap col = new PropertyMap("element");

        Object tempObj = tempObjects[0];
        PropertySimple activityHandle = new PropertySimple("ActivityHandle", tempObj != null ? ((JmxActivityContextHandle) tempObj).getActivityHandleToString() : "-");

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
    result.getComplexResults().put(columnList);

    return result;
  }

  private void copyFile(File sourceFile, File destFile) throws IOException {
    log.info("CopyFile : Source[" + sourceFile.getAbsolutePath() + "] Dest[" + destFile.getAbsolutePath() + "]");
    if(!destFile.exists()) {
      destFile.createNewFile();
    }

    FileChannel source = null;
    FileChannel destination = null;
    try {
      source = new FileInputStream(sourceFile).getChannel();
      destination = new FileOutputStream(destFile).getChannel();
      destination.transferFrom(source, 0, source.size());
    }
    finally {
      if(source != null) {
        source.close();
      }
      if(destination != null) {
        destination.close();
      }
    }
  }

  private String deployFolder;
  
  public String getDeployFolderPath() {
    return this.deployFolder;
  }
}
