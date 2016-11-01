/*
 * JBoss, Home of Professional Open Source
 * Copyright 2003-2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.slee.container.management.console.server.mbeans;

import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.slee.management.SleeProvider;
import javax.slee.management.SleeProviderFactory;
import javax.slee.management.SleeState;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SleeManagementMBeanUtils {
  private MBeanServerConnection mbeanServer;

  private ObjectName sleeManagementMBean;
  private ObjectName mobicentsManagementMBean;

  private DeploymentMBeanUtils deploymentMBeanUtils;

  private ServiceManagementMBeanUtils serviceManagementMBeanUtils;

  private ProfileProvisioningMBeanUtils profileProvisioningMBeanUtils;

  private ResourceManagementMBeanUtils resourceManagementMBeanUtils;

  private ActivityManagementMBeanUtils activityManagementMBeanUtils;

  private SbbEntitiesMBeanUtils sbbEntitiesMBeanUtils;

  private LogManagementMBeanUtils logManagementMBeanUtils;
  
  private AlarmMBeanUtils alarmMBeanUtils;

  public SleeManagementMBeanUtils() throws ManagementConsoleException {
    try {
      InitialContext ctx;
      ctx = new InitialContext();

      mbeanServer = (MBeanServerConnection) ctx.lookup("jmx/rmi/RMIAdaptor");

      SleeProvider sleeProvider = SleeProviderFactory.getSleeProvider("org.mobicents.slee.container.management.jmx.SleeProviderImpl");
      sleeManagementMBean = sleeProvider.getSleeManagementMBean();
      mobicentsManagementMBean = new ObjectName("org.mobicents.slee:service=MobicentsManagement");
      // sleeManagementMBean = new ObjectName("slee:service=SleeManagement");

    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

    deploymentMBeanUtils = new DeploymentMBeanUtils(mbeanServer, sleeManagementMBean);

    serviceManagementMBeanUtils = new ServiceManagementMBeanUtils(mbeanServer, sleeManagementMBean);

    profileProvisioningMBeanUtils = new ProfileProvisioningMBeanUtils(mbeanServer, sleeManagementMBean);

    resourceManagementMBeanUtils = new ResourceManagementMBeanUtils(mbeanServer, sleeManagementMBean);

    activityManagementMBeanUtils = new ActivityManagementMBeanUtils(mbeanServer, sleeManagementMBean);

    sbbEntitiesMBeanUtils = new SbbEntitiesMBeanUtils(mbeanServer, sleeManagementMBean);

    logManagementMBeanUtils = new LogManagementMBeanUtils(mbeanServer, sleeManagementMBean);
    
    alarmMBeanUtils = new AlarmMBeanUtils(mbeanServer, sleeManagementMBean);
  }

  public DeploymentMBeanUtils getDeploymentMBeanUtils() {
    return deploymentMBeanUtils;
  }

  public ActivityManagementMBeanUtils getActivityManagementMBeanUtils() {
    return activityManagementMBeanUtils;
  }

  public ServiceManagementMBeanUtils getServiceManagementMBeanUtils() {
    return serviceManagementMBeanUtils;
  }

  public SleeState getState() throws ManagementConsoleException {
    try {
      return (SleeState) mbeanServer.getAttribute(sleeManagementMBean, "State");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void shutdown() throws ManagementConsoleException {
    try {
      mbeanServer.invoke(sleeManagementMBean, "shutdown", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void start() throws ManagementConsoleException {
    try {
      mbeanServer.invoke(sleeManagementMBean, "start", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void stop() throws ManagementConsoleException {
    try {
      mbeanServer.invoke(sleeManagementMBean, "stop", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void addNotificationListener(NotificationListener listener) throws ManagementConsoleException {
    try {
      mbeanServer.addNotificationListener(sleeManagementMBean, listener, null, null);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void removeNotificationListener(NotificationListener listener) throws ManagementConsoleException {
    try {
      mbeanServer.removeNotificationListener(sleeManagementMBean, listener);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String getVersion() throws ManagementConsoleException {
    try {
      return (String) mbeanServer.getAttribute(mobicentsManagementMBean, "Version");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(e);
    }
  }

  public ProfileProvisioningMBeanUtils getProfileProvisioningMBeanUtils() {
    return profileProvisioningMBeanUtils;
  }

  public ResourceManagementMBeanUtils getResourceManagementMBeanUtils() {
    return resourceManagementMBeanUtils;
  }

  public SbbEntitiesMBeanUtils getSbbEntitiesMBeanUtils() {
    return sbbEntitiesMBeanUtils;
  }

  public LogManagementMBeanUtils getLogManagementMBeanUtils() {
    return logManagementMBeanUtils;
  }

  public AlarmMBeanUtils getAlarmMBeanUtils() {
    return alarmMBeanUtils;
  }

  public static String doMessage(Throwable t) {
    StringBuffer sb = new StringBuffer();

    // Just show the interesting bits to the management console log...
    if(t instanceof MBeanException) {
      Throwable tCause = t;
      while(tCause.getCause() != null) {
        tCause = tCause.getCause();
      }
      sb.append(tCause.getMessage() + "<br />\n");
      sb.append("Check the JAIN SLEE logs for more detailed information about this error.");
    }
    else {
      int tick = 0;
      Throwable e = t;
      do {
        StackTraceElement[] trace = e.getStackTrace();
        if (tick++ == 0)
          sb.append(e.getClass().getCanonicalName() + ":" + e.getLocalizedMessage() + "<br/>\n");
        else
          sb.append("Caused by: " + e.getClass().getCanonicalName() + ":" + e.getLocalizedMessage() + "<br/>\n");

        for (StackTraceElement ste : trace)
          sb.append("\t" + ste + "<br/>\n");

            e = e.getCause();
      } while (e != null);
    }

    return sb.toString();

  }

}
