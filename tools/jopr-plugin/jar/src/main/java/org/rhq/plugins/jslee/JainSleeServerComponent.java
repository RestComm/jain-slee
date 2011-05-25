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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.security.auth.login.LoginException;
import javax.slee.EventTypeID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean;
import org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean;
import org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean;
import org.mobicents.slee.container.management.jmx.JmxActivityContextHandle;
import org.mobicents.slee.container.management.jmx.MobicentsManagementMBean;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.content.PackageDetailsKey;
import org.rhq.core.domain.content.transfer.ResourcePackageDetails;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataNumeric;
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
    if(log.isTraceEnabled()) {
      log.trace("start(" + resourceContext + ") called.");
    }

    this.resourceContext = resourceContext;

    this.deployFolder = resourceContext.getPluginConfiguration().getSimple(ApplicationServerPluginConfigurationProperties.SERVER_HOME_DIR).getStringValue() + File.separator  + "deploy";
    this.farmDeployFolder = resourceContext.getPluginConfiguration().getSimple(ApplicationServerPluginConfigurationProperties.SERVER_HOME_DIR).getStringValue() + File.separator  + "farm";
    this.logFilePath = resourceContext.getPluginConfiguration().getSimple(ApplicationServerPluginConfigurationProperties.SERVER_HOME_DIR).getStringValue() + File.separator  + "conf" + File.separator + "jboss-log4j.xml";
    this.logConfigurationsFolder = this.deployFolder + File.separator + "mobicents-slee" + File.separator + "log4j-templates";
    // Connect to the JBAS instance's Profile Service and JMX MBeanServer.

    Configuration pluginConfig = resourceContext.getPluginConfiguration();

    String namingURL = pluginConfig.getSimple("namingURL").getStringValue();
    String principal = pluginConfig.getSimple("principal").getStringValue();
    String credentials = pluginConfig.getSimple("credentials").getStringValue();

    if(log.isDebugEnabled()) {
      log.debug("Started JAIN SLEE Server Component @ " + namingURL + ", " + principal + "/" + credentials);
    }

    this.mBeanServerUtils = new MBeanServerUtils(namingURL, principal, credentials);
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

    if (this.mBeanServerUtils != null) {
      try {
        ObjectName sleemanagement = new ObjectName(SleeManagementMBean.OBJECT_NAME);
        MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
        mBeanServerUtils.login();

        this.sleeState = (SleeState) connection.getAttribute(sleemanagement, "State");

        if(log.isDebugEnabled()) {
          log.debug("JAIN SLEE State = " + this.sleeState);
        }
        return AvailabilityType.UP;

      }
      catch (Exception e) {
        log.error("Failed to obtain JAIN SLEE Server state.", e);
        return AvailabilityType.DOWN;
      }
      finally {
        try {
          this.mBeanServerUtils.logout();
        }
        catch (LoginException e) {
          if(log.isDebugEnabled()) {
            log.debug("Failed to logout from secured JMX", e);
          }
        }
      }
    }
    else {
      log.error("Returning availability as DOWN");
      return AvailabilityType.DOWN;
    }
  }

  public void getValues(MeasurementReport report, Set<MeasurementScheduleRequest> metrics) throws Exception {
    if(log.isTraceEnabled()) {
      log.trace("getValues(" + report + "," + metrics + ") called.");
    }

    for (MeasurementScheduleRequest request : metrics) {
      if (request.getName().equals("state")) {
        report.addData(new MeasurementDataTrait(request, this.sleeState.toString()));
      }
      else if (request.getName().equals("activitiesMapped")) {
        EventRouterStatisticsMBean erStatsMBean = getEventRouterStatisticsMBean();
        report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStatsMBean.getActivitiesMapped())));
      }
      else if (request.getName().equals("averageEventRoutingTime")) {
        EventRouterStatisticsMBean erStatsMBean = getEventRouterStatisticsMBean();
        report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStatsMBean.getAverageEventRoutingTime())));
      }
      else if (request.getName().equals("executedTasks")) {
        EventRouterStatisticsMBean erStatsMBean = getEventRouterStatisticsMBean();
        report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStatsMBean.getExecutedTasks())));
      }
      else if (request.getName().equals("miscTasksExecuted")) {
        EventRouterStatisticsMBean erStatsMBean = getEventRouterStatisticsMBean();
        report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStatsMBean.getMiscTasksExecuted())));
      }
    }
  }

  public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException, Exception {
    if(log.isDebugEnabled()) {
      log.debug("invokeOperation(" + name + ", " + parameters + ") called.");
    }

    if ("sleeState".equals(name)) {
      return doSleeState(parameters);
    }
    else if ("listActivityContexts".equals(name)) {
      return doListActivityContexts(parameters);
    }
    else if ("queryActivityContextLiveness".equals(name)) {
      return doQueryActivityContextLiveness(parameters);
    }
    else if ("switchLoggingConfiguration".equals(name)) {
      return doSwitchLoggingConfiguration(parameters);
    }
    else if ("getLoggingConfiguration".equals(name)) {
      return doGetLoggingConfiguration(parameters);
    }
    else if ("setLoggingConfiguration".equals(name)) {
      return doSetLoggingConfiguration(parameters);
    }
    else if("viewEventRouterStatistics".equals(name)) {
      return doViewEventRouterStatistics(parameters);
    }
    // Congestion Control Management
    else if("ccSetMemOn".equals(name)) {
      return doManageCongestionControl(0, parameters);
    }
    else if("ccSetMemOff".equals(name)) {
      return doManageCongestionControl(1, parameters);
    }
    else if("ccSetCheckPeriod".equals(name)) {
      return doManageCongestionControl(2, parameters);
    }
    else if("ccSetRefuseStartActivity".equals(name)) {
      return doManageCongestionControl(3, parameters);
    }
    else if("ccSetRefuseFireEvent".equals(name)) {
      return doManageCongestionControl(4, parameters);
    }
    else {
      throw new UnsupportedOperationException("Operation [" + name + "] is not supported.");
    }
  }

  private OperationResult doManageCongestionControl(int op, Configuration parameters) throws Exception {
    OperationResult result = new OperationResult();

    MBeanServerUtils mbeanUtils = getMBeanServerUtils();
    try {
      MBeanServerConnection connection = mbeanUtils.getConnection();
      mbeanUtils.login();

      ObjectName ccConfigObjectName = new ObjectName("org.mobicents.slee:name=CongestionControlConfiguration");
      CongestionControlConfigurationMBean ccConfigMBean = MBeanServerInvocationHandler.newProxyInstance(connection,
          ccConfigObjectName, CongestionControlConfigurationMBean.class, false);

      switch (op) {
      case 0: // ccSetMemOn
        int oldValue = ccConfigMBean.getMinFreeMemoryToTurnOn();
        int newValue = parameters.getSimple("value").getIntegerValue();
        if(oldValue != newValue) {
          ccConfigMBean.setMinFreeMemoryToTurnOn(newValue);
          result.getComplexResults().put(new PropertySimple("result", "Operation completed successfully (value changed from '" + oldValue + "' to '" + newValue + "')."));
        }
        else {
          result.getComplexResults().put(new PropertySimple("result", "Operation completed successfully (value wasn't changed, it was equal to the current)."));
        }
        break;

      case 1: // ccSetMemOff
        oldValue = ccConfigMBean.getMinFreeMemoryToTurnOff();
        newValue = parameters.getSimple("value").getIntegerValue();
        if(oldValue != newValue) {
          ccConfigMBean.setMinFreeMemoryToTurnOff(newValue);
          result.getComplexResults().put(new PropertySimple("result", "Operation completed successfully (value changed from '" + oldValue + "' to '" + newValue + "')."));
        }
        else {
          result.getComplexResults().put(new PropertySimple("result", "Operation completed successfully (value wasn't changed, it was equal to the current)."));
        }
        break;

      case 2: // ccSetCheckPeriod
        oldValue = ccConfigMBean.getPeriodBetweenChecks();
        newValue = parameters.getSimple("value").getIntegerValue();
        if(oldValue != newValue) {
          ccConfigMBean.setPeriodBetweenChecks(newValue);
          result.getComplexResults().put(new PropertySimple("result", "Operation completed successfully (value changed from '" + oldValue + "' to '" + newValue + "')."));
        }
        else {
          result.getComplexResults().put(new PropertySimple("result", "Operation completed successfully (value wasn't changed, it was equal to the current)."));
        }
        break;

      case 3: // ccSetRefuseStartActivity
        boolean oldValueBool = ccConfigMBean.isRefuseStartActivity();
        boolean newValueBool = parameters.getSimple("value").getBooleanValue();
        if(oldValueBool != newValueBool) {
          ccConfigMBean.setRefuseStartActivity(newValueBool);
          result.getComplexResults().put(new PropertySimple("result", "Operation completed successfully (value changed from '" + oldValueBool + "' to '" + newValueBool + "')."));
        }
        else {
          result.getComplexResults().put(new PropertySimple("result", "Operation completed successfully (value wasn't changed, it was equal to the current)."));
        }
        break;

      case 4: // ccSetRefuseFireEvent
        oldValueBool = ccConfigMBean.isRefuseFireEvent();
        newValueBool = parameters.getSimple("value").getBooleanValue();
        if(oldValueBool != newValueBool) {
          ccConfigMBean.setRefuseFireEvent(newValueBool);
          result.getComplexResults().put(new PropertySimple("result", "Operation completed successfully (value changed from '" + oldValueBool + "' to '" + newValueBool + "')."));
        }
        else {
          result.getComplexResults().put(new PropertySimple("result", "Operation completed successfully (value wasn't changed, it was equal to the current)."));
        }
        break;

      default:
        result.setErrorMessage("Unknown operation in Congestion Control Management (" + op + ")");
        break;
      }
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

    return result;
  }

  private OperationResult doViewEventRouterStatistics(Configuration parameters) throws Exception {
    try {
      OperationResult result = new OperationResult();
      PropertyList statistics = new PropertyList("statistics");
      result.getComplexResults().put(statistics);

      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      this.mBeanServerUtils.login();

      String filter = parameters.getSimple("filter").getStringValue();

      if(filter.equals("global")) {
        doGetERSGlobal(connection, statistics);
      }
      else if(filter.equals("executors")) {
        doGetERSExecutors(connection, statistics);
      }
      else if(filter.equals("eventTypes")) {
        doGetERSEventTypes(connection, statistics);
      }
      else if(filter.equals("executorsByEventTypes")) {
        doGetERSCombined(connection, statistics);
      }
      else {
        doGetERSGlobal(connection, statistics);
        doGetERSExecutors(connection, statistics);
        doGetERSEventTypes(connection, statistics);            
        doGetERSCombined(connection, statistics);
      }
      return result;
    }
    finally {
      try {
        this.mBeanServerUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

  private void doGetERSGlobal(MBeanServerConnection connection, PropertyList statistics) throws Exception {
    EventRouterStatisticsMBean erStatsMBean = getEventRouterStatisticsMBean();

    Integer activitiesMapped = erStatsMBean.getActivitiesMapped();
    Long averageEventRoutingTime = erStatsMBean.getAverageEventRoutingTime();
    Long executedTasks = erStatsMBean.getExecutedTasks();
    Long miscTasksExecuted = erStatsMBean.getMiscTasksExecuted();

    PropertyMap query = new PropertyMap("EventRouterStatistics", new PropertySimple("id", "JAIN SLEE Container"),
        new PropertySimple("activitiesMapped", activitiesMapped), 
        new PropertySimple("averageEventRoutingTime", averageEventRoutingTime), 
        new PropertySimple("executedTasks", executedTasks),
        new PropertySimple("miscTasksExecuted", miscTasksExecuted));

    statistics.add(query);
  }

  private void doGetERSExecutors(MBeanServerConnection connection, PropertyList statistics) throws Exception {
    EventRouterConfigurationMBean erConfigMBean = getEventRouterConfigurationMBean();

    int numExecutors = erConfigMBean.getEventRouterThreads();

    EventRouterStatisticsMBean erStatsMBean = getEventRouterStatisticsMBean();

    for(int executorId = 0; executorId < numExecutors; executorId++) {
      Integer activitiesMapped = erStatsMBean.getActivitiesMapped(executorId);
      Long averageEventRoutingTime = erStatsMBean.getAverageEventRoutingTime(executorId);
      Long executedTasks = erStatsMBean.getExecutedTasks(executorId);
      Long executingTime = erStatsMBean.getExecutingTime(executorId);
      Long idleTime = erStatsMBean.getIdleTime(executorId);
      Long miscTasksExecuted = erStatsMBean.getMiscTasksExecuted(executorId);

      PropertyMap query = new PropertyMap("EventRouterStatistics", new PropertySimple("id", "Executor #" + executorId),
          new PropertySimple("activitiesMapped", activitiesMapped), 
          new PropertySimple("averageEventRoutingTime", averageEventRoutingTime), 
          new PropertySimple("executedTasks", executedTasks),
          new PropertySimple("executingTime", executingTime),
          new PropertySimple("idleTime", idleTime),
          new PropertySimple("miscTasksExecuted", miscTasksExecuted));

      statistics.add(query);
    }
  }

  private void doGetERSEventTypes(MBeanServerConnection connection, PropertyList statistics) throws Exception {
    ObjectName deploymentObjectName = new ObjectName(DeploymentMBean.OBJECT_NAME);
    DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
        deploymentObjectName, DeploymentMBean.class, false);

    EventTypeID[] eventTypes = deploymentMBean.getEventTypes();

    EventRouterStatisticsMBean erStatsMBean = getEventRouterStatisticsMBean();

    for(EventTypeID eventType : eventTypes) {
      Long averageEventRoutingTime = erStatsMBean.getAverageEventRoutingTime(eventType);
      Long eventsRouted = erStatsMBean.getEventsRouted(eventType);

      PropertyMap query = new PropertyMap("EventRouterStatistics", new PropertySimple("id", eventType.toString()),
          new PropertySimple("averageEventRoutingTime", averageEventRoutingTime), 
          new PropertySimple("eventsRouted", eventsRouted));

      statistics.add(query);
    }
  }

  private void doGetERSCombined(MBeanServerConnection connection, PropertyList statistics) throws Exception {
    EventRouterConfigurationMBean erConfigMBean = getEventRouterConfigurationMBean();

    int numExecutors = erConfigMBean.getEventRouterThreads();

    ObjectName deploymentObjectName = new ObjectName(DeploymentMBean.OBJECT_NAME);
    DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
        deploymentObjectName, DeploymentMBean.class, false);

    EventTypeID[] eventTypes = deploymentMBean.getEventTypes();

    EventRouterStatisticsMBean erStatsMBean = getEventRouterStatisticsMBean();

    for(EventTypeID eventType : eventTypes) {
      for(int executorId = 0; executorId < numExecutors; executorId++) {
        Long averageEventRoutingTime = erStatsMBean.getAverageEventRoutingTime(executorId, eventType);
        Long eventsRouted = erStatsMBean.getEventsRouted(executorId, eventType);
        Long routingTime = erStatsMBean.getRoutingTime(executorId, eventType);

        PropertyMap query = new PropertyMap("EventRouterStatistics", new PropertySimple("id", eventType.toString() + "@Executor #" + executorId),
            new PropertySimple("averageEventRoutingTime", averageEventRoutingTime), 
            new PropertySimple("routingTime", routingTime), 
            new PropertySimple("eventsRouted", eventsRouted));

        statistics.add(query);
      }
    }
  }

  public CreateResourceReport createResource(CreateResourceReport createResourceReport) {
    ResourceType resourceType = createResourceReport.getResourceType();
    if (resourceType.getCreationDataType() == ResourceCreationDataType.CONTENT)
      createContentBasedResource(createResourceReport, resourceType);

    return createResourceReport;
  }

  private void createContentBasedResource(CreateResourceReport createResourceReport, ResourceType resourceType) {
    if(log.isTraceEnabled()) {
      log.trace("createContentBasedResource(" + createResourceReport + ", " + resourceType + ")");
    }
    
    try {
      OutputStream os = null;

      ResourcePackageDetails details = createResourceReport.getPackageDetails();
      PackageDetailsKey key = details.getKey();
      String archiveName = key.getName();

      if(log.isDebugEnabled()) {
        log.debug("createContentBasedResource: archiveName = " + archiveName);
      }

      Configuration deployTimeConfig = createResourceReport.getPackageDetails().getDeploymentTimeConfiguration();

      boolean persistentDeploy = deployTimeConfig.getSimple("persistentDeploy").getBooleanValue();
      boolean deployFarmed = deployTimeConfig.getSimple("deployFarmed").getBooleanValue();
      if(log.isDebugEnabled()) {
        log.debug("createContentBasedResource: persistentDeploy = " + persistentDeploy + ", deployFarmed = " + deployFarmed);
      }

      // Validate deploy options
      if(deployFarmed) {
        if(!persistentDeploy) {
          throw new IllegalArgumentException("Invalid options. If 'Deploy Farmed' is set to 'Yes', 'Persistent Deploy' should also be set to 'Yes'.");
        }
        if(!(new File(this.farmDeployFolder).exists())) {
          throw new IllegalArgumentException("Invalid options. 'Deploy Farmed' is set to 'Yes', but " + farmDeployFolder + " does not exist.");          
        }
      }

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

      if(log.isDebugEnabled()) {
        log.debug("createContentBasedResource: tmpFile = " + tempDir.getAbsolutePath());
      }

      File archiveFile = new File(key.getName());

      // this is to ensure that we only get the filename part no matter whether the key contains full path or not.
      File contentCopy = new File(tempDir, archiveFile.getName());

      os = new BufferedOutputStream(new FileOutputStream(contentCopy));
      ContentContext contentContext = resourceContext.getContentContext();
      ContentServices contentServices = contentContext.getContentServices();
      contentServices.downloadPackageBitsForChildResource(contentContext, resourceType.getName(), key, os);

      if(log.isDebugEnabled()) {
        log.debug("createContentBasedResource: contentCopy = " + contentCopy.getAbsolutePath());
      }

      ObjectName deploymentObjName = new ObjectName(DeploymentMBean.OBJECT_NAME);

      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      this.mBeanServerUtils.login();

      DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
          deploymentObjName, javax.slee.management.DeploymentMBean.class, false);

      String depKey = null;

      if(!persistentDeploy) {
        DeployableUnitID deployableUnitID = deploymentMBean.install(contentCopy.toURI().toURL().toString());
        if(log.isDebugEnabled()) {
          log.debug("createContentBasedResource: Deployed "+ deployableUnitID );
        }

        depKey = deployableUnitID.getURL();
      }
      else {
        File destination = new File((deployFarmed ? this.farmDeployFolder : this.deployFolder) + File.separator + archiveName);
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
    finally {
      try {
        this.mBeanServerUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
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

  private OperationResult doSleeState(Configuration parameters) throws Exception {
    try {
      OperationResult result = new OperationResult();

      String message = null;
      String action = parameters.getSimple("action").getStringValue();
      ObjectName sleemanagement = new ObjectName(SleeManagementMBean.OBJECT_NAME);

      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      this.mBeanServerUtils.login();

      SleeManagementMBean sleeManagementMBean = (SleeManagementMBean) MBeanServerInvocationHandler.newProxyInstance(connection,
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
    finally {
      try {
        this.mBeanServerUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

  private OperationResult doQueryActivityContextLiveness(Configuration parameters) throws Exception {
    try {
      OperationResult result = new OperationResult();

      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      this.mBeanServerUtils.login();

      ObjectName actMana = new ObjectName("org.mobicents.slee:name=ActivityManagementMBean");

      ActivityManagementMBeanImplMBean aciManagMBean = (ActivityManagementMBeanImplMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
          actMana, org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean.class, false);

      aciManagMBean.queryActivityContextLiveness();

      result.getComplexResults().put(new PropertySimple("result", "Activity Context Liveness queried successfully."));

      return result;
    }
    finally {
      try {
        this.mBeanServerUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

  private OperationResult doSwitchLoggingConfiguration(Configuration parameters) throws Exception {
    try {
      OperationResult result = new OperationResult();

      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      this.mBeanServerUtils.login();

      ObjectName actMana = new ObjectName("org.mobicents.slee:service=MobicentsManagement" /* FIXME */);

      MobicentsManagementMBean mobicentsManagementMBean = (MobicentsManagementMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
          actMana, org.mobicents.slee.container.management.jmx.MobicentsManagementMBean.class, false);

      String profile = parameters.getSimple("profile").getStringValue();

      mobicentsManagementMBean.switchLoggingConfiguration(profile);

      result.getComplexResults().put(new PropertySimple("result", "Log4j Configuration Profile successfully changed to " + profile + "."));
      return result;
    }
    finally {
      try {
        this.mBeanServerUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

  private OperationResult doGetLoggingConfiguration(Configuration parameters) throws Exception {
    try {
      OperationResult result = new OperationResult();

      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      this.mBeanServerUtils.login();

      ObjectName actMana = new ObjectName("org.mobicents.slee:service=MobicentsManagement" /* FIXME */);

      MobicentsManagementMBean mobicentsManagementMBean = (MobicentsManagementMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
          actMana, org.mobicents.slee.container.management.jmx.MobicentsManagementMBean.class, false);

      String profile = parameters.getSimple("profile").getStringValue();

      result.getComplexResults().put(new PropertySimple("result", mobicentsManagementMBean.getLoggingConfiguration(profile)));
      return result;
    }
    finally {
      try {
        this.mBeanServerUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

  private OperationResult doSetLoggingConfiguration(Configuration parameters) throws Exception {
    try {
      OperationResult result = new OperationResult();

      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      this.mBeanServerUtils.login();

      ObjectName actMana = new ObjectName("org.mobicents.slee:service=MobicentsManagement" /* FIXME */);

      MobicentsManagementMBean mobicentsManagementMBean = (MobicentsManagementMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
          actMana, org.mobicents.slee.container.management.jmx.MobicentsManagementMBean.class, false);

      String profile = parameters.getSimple("profile").getStringValue().toLowerCase();
      String contents = parameters.getSimple("contents").getStringValue();

      mobicentsManagementMBean.setLoggingConfiguration(profile, contents);

      result.getComplexResults().put(new PropertySimple("result", "Log4j " + profile + " Configuration successfully updated."));
      return result;
    }
    finally {
      try {
        this.mBeanServerUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

  private OperationResult doListActivityContexts(Configuration paramteres) throws Exception {
    try {
      OperationResult result = new OperationResult();

      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      this.mBeanServerUtils.login();

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
    finally {
      try {
        this.mBeanServerUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

  private void copyFile(File sourceFile, File destFile) throws IOException {
    if(log.isDebugEnabled()) {
      log.debug("CopyFile : Source[" + sourceFile.getAbsolutePath() + "] Dest[" + destFile.getAbsolutePath() + "]");
    }

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

  private String farmDeployFolder;

  public String getFarmDeployFolderPath() {
    return this.farmDeployFolder;
  }

  private String logFilePath;

  public String getLogFilePath() {
    return logFilePath;
  }

  private String logConfigurationsFolder;

  public String getLogConfigurationsFolder() {
    return logConfigurationsFolder;
  }

  private EventRouterStatisticsMBean getEventRouterStatisticsMBean() throws Exception {
    try {
      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      this.mBeanServerUtils.login();

      ObjectName erStatsObjectName = new ObjectName("org.mobicents.slee:name=EventRouterStatistics"/* FIXME */);
      EventRouterStatisticsMBean erStatsMBean = (EventRouterStatisticsMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
          erStatsObjectName, EventRouterStatisticsMBean.class, false);

      return erStatsMBean;
    }
    finally {
      try {
        this.mBeanServerUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

  private EventRouterConfigurationMBean getEventRouterConfigurationMBean() throws Exception {
    try {
      MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
      this.mBeanServerUtils.login();

      ObjectName erConfigObjectName = new ObjectName("org.mobicents.slee:name=EventRouterConfiguration"/* FIXME */);
      EventRouterConfigurationMBean erConfigMBean = (EventRouterConfigurationMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
          erConfigObjectName, EventRouterConfigurationMBean.class, false);

      return erConfigMBean;
    }
    finally {
      try {
        this.mBeanServerUtils.logout();
      }
      catch (LoginException e) {
        if(log.isDebugEnabled()) {
          log.debug("Failed to logout from secured JMX", e);
        }
      }
    }
  }

}
