/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.management.console.server.usage;

import javax.slee.SbbID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.usage.UsageParameterInfo;
import org.mobicents.slee.container.management.console.client.usage.UsageService;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.mbeans.SbbUsageMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.ServiceUsageMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Stefano Zappaterra
 * 
 */
public class UsageServiceImpl extends RemoteServiceServlet implements UsageService {

  private static final long serialVersionUID = -7460460472716859508L;

  private ManagementConsole managementConsole = ManagementConsole.getInstance();

  private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

  private ServiceUsageMBeanUtils getServiceUsageMBeanUtils(ServiceID serviceID) throws ManagementConsoleException {
    return sleeConnection.getSleeManagementMBeanUtils().getServiceManagementMBeanUtils().getServiceUsageMBeanUtils(serviceID);
  }

  public String[] getParameterSets(String strServiceID, String strSbbID) throws ManagementConsoleException {

    ServiceID serviceID = (ServiceID) managementConsole.getComponentIDMap().get(strServiceID);

    ServiceUsageMBeanUtils serviceUsageMBeanUtils = getServiceUsageMBeanUtils(serviceID);

    SbbID sbbID = (SbbID) managementConsole.getComponentIDMap().get(strSbbID);

    // Get the SbbUsageMBean for the default parameter set
    // If it does not exist, no sbb usage parameter interface is defined
    try {
      serviceUsageMBeanUtils.getSbbUsageMBeanUtils(sbbID);
    }
    catch (Exception e) {
      throw new ManagementConsoleException("No SBB usage parameter interface defined for SBB " + sbbID.toString());
    }

    String defaultParameterSet = "";
    String[] namedParameterSet = {};
    try {
      namedParameterSet = serviceUsageMBeanUtils.getUsageParameterSets(sbbID);
    }
    catch (Exception e) {
    }

    String[] allParameterSet = new String[namedParameterSet.length + 1];

    allParameterSet[0] = defaultParameterSet;
    for (int i = 0; i < namedParameterSet.length; i++) {
      allParameterSet[i + 1] = namedParameterSet[i];
    }

    return allParameterSet;

  }

  public void createUsageParameterSet(String strServiceID, String strSbbID, String parameterSet) throws ManagementConsoleException {

    ServiceID serviceID = (ServiceID) managementConsole.getComponentIDMap().get(strServiceID);

    ServiceUsageMBeanUtils serviceUsageMBeanUtils = getServiceUsageMBeanUtils(serviceID);

    SbbID sbbID = (SbbID) managementConsole.getComponentIDMap().get(strSbbID);

    serviceUsageMBeanUtils.createUsageParameterSet(sbbID, parameterSet);

  }

  public void removeUsageParameterSet(String strServiceID, String strSbbID, String parameterSet) throws ManagementConsoleException {
    ServiceID serviceID = (ServiceID) managementConsole.getComponentIDMap().get(strServiceID);

    ServiceUsageMBeanUtils serviceUsageMBeanUtils = getServiceUsageMBeanUtils(serviceID);

    SbbID sbbID = (SbbID) managementConsole.getComponentIDMap().get(strSbbID);

    serviceUsageMBeanUtils.removeUsageParameterSet(sbbID, parameterSet);
  }

  public void resetAllUsageParameters(String strServiceID, String strSbbID) throws ManagementConsoleException {
    ServiceID serviceID = (ServiceID) managementConsole.getComponentIDMap().get(strServiceID);

    ServiceUsageMBeanUtils serviceUsageMBeanUtils = getServiceUsageMBeanUtils(serviceID);

    SbbID sbbID = (SbbID) managementConsole.getComponentIDMap().get(strSbbID);

    serviceUsageMBeanUtils.resetAllUsageParameters(sbbID);
  }

  public UsageParameterInfo[] getSBBUsageParameters(String strServiceID, String strSbbID, String parameterSet) throws ManagementConsoleException {
    ServiceID serviceID = (ServiceID) managementConsole.getComponentIDMap().get(strServiceID);

    ServiceUsageMBeanUtils serviceUsageMBeanUtils = getServiceUsageMBeanUtils(serviceID);

    SbbID sbbID = (SbbID) managementConsole.getComponentIDMap().get(strSbbID);

    SbbUsageMBeanUtils sbbUsageMBeanUtils;
    if (parameterSet.length() == 0) {
      sbbUsageMBeanUtils = serviceUsageMBeanUtils.getSbbUsageMBeanUtils(sbbID);
    }
    else {
      sbbUsageMBeanUtils = serviceUsageMBeanUtils.getSbbUsageMBeanUtils(sbbID, parameterSet);
    }

    return sbbUsageMBeanUtils.getSBBUsageParameterInfos();
  }

  public void resetAllUsageParameters(String strServiceID, String strSbbID, String parameterSet) throws ManagementConsoleException {
    ServiceID serviceID = (ServiceID) managementConsole.getComponentIDMap().get(strServiceID);

    ServiceUsageMBeanUtils serviceUsageMBeanUtils = getServiceUsageMBeanUtils(serviceID);

    SbbID sbbID = (SbbID) managementConsole.getComponentIDMap().get(strSbbID);

    SbbUsageMBeanUtils sbbUsageMBeanUtils;
    if (parameterSet.length() == 0) {
      sbbUsageMBeanUtils = serviceUsageMBeanUtils.getSbbUsageMBeanUtils(sbbID);
    }
    else {
      sbbUsageMBeanUtils = serviceUsageMBeanUtils.getSbbUsageMBeanUtils(sbbID, parameterSet);
    }
    sbbUsageMBeanUtils.resetAllUsageParameters();
  }

  public void resetUsageParameter(String strServiceID, String strSbbID, String parameterSet, String parameterName, boolean isCounterType)
      throws ManagementConsoleException {

    ServiceID serviceID = (ServiceID) managementConsole.getComponentIDMap().get(strServiceID);

    ServiceUsageMBeanUtils serviceUsageMBeanUtils = getServiceUsageMBeanUtils(serviceID);

    SbbID sbbID = (SbbID) managementConsole.getComponentIDMap().get(strSbbID);

    SbbUsageMBeanUtils sbbUsageMBeanUtils;
    if (parameterSet.length() == 0) {
      sbbUsageMBeanUtils = serviceUsageMBeanUtils.getSbbUsageMBeanUtils(sbbID);
    }
    else {
      sbbUsageMBeanUtils = serviceUsageMBeanUtils.getSbbUsageMBeanUtils(sbbID, parameterSet);
    }

    if (isCounterType)
      sbbUsageMBeanUtils.getCounterTypeParameter(parameterName, true);
    else
      sbbUsageMBeanUtils.getSampleTypeParameter(parameterName, true);
  }
}
