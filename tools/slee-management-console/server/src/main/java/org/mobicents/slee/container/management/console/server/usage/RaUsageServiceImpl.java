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

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.usage.RaUsageService;
import org.mobicents.slee.container.management.console.client.usage.UsageParameterInfo;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.mbeans.RaEntityUsageMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.ResourceUsageMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Povilas Jurna
 * 
 */
public class RaUsageServiceImpl extends RemoteServiceServlet implements RaUsageService {

  private static final long serialVersionUID = -7460460472716859508L;

  private ManagementConsole managementConsole = ManagementConsole.getInstance();

  private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

  private ResourceUsageMBeanUtils getRaUsageMBeanUtils(String entityName) throws ManagementConsoleException {
    return sleeConnection.getSleeManagementMBeanUtils().getResourceManagementMBeanUtils().getResourceUsageMBeanUtils(entityName);
  }

  public String[] getParameterSets(String raId, String entityName) throws ManagementConsoleException {

    ResourceUsageMBeanUtils resourceUsageMBeanUtils = null;

    // Get the ResourceUsageMBean and UsageMBean for the default parameter set
    // If either of them does not exist, no usage parameter interface is defined
    try {
      resourceUsageMBeanUtils = getRaUsageMBeanUtils(entityName);
      resourceUsageMBeanUtils.getRaEntityUsageMBeanUtils();
    }
    catch (Exception e) {
      throw new ManagementConsoleException("No Resource Adaptor Entity usage parameter interface defined for " + raId +" entity " + entityName);
    }

    String defaultParameterSet = "";
    String[] namedParameterSet = {};
    try {
      namedParameterSet = resourceUsageMBeanUtils.getUsageParameterSets();
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

  public void createUsageParameterSet(String entityName, String parameterSet) throws ManagementConsoleException {
    ResourceUsageMBeanUtils resourceUsageMBeanUtils = getRaUsageMBeanUtils(entityName);
    resourceUsageMBeanUtils.createUsageParameterSet(parameterSet);
  }

  public void removeUsageParameterSet(String entityName, String parameterSet) throws ManagementConsoleException {
    ResourceUsageMBeanUtils resourceUsageMBeanUtils = getRaUsageMBeanUtils(entityName);
    resourceUsageMBeanUtils.removeUsageParameterSet(parameterSet);
  }

  public void resetAllUsageParameters(String entityName) throws ManagementConsoleException {
    ResourceUsageMBeanUtils resourceUsageMBeanUtils = getRaUsageMBeanUtils(entityName);
    resourceUsageMBeanUtils.resetAllUsageParameters();
  }

  public UsageParameterInfo[] getRaUsageParameters(String entityName, String parameterSet) throws ManagementConsoleException {

    ResourceUsageMBeanUtils resourceUsageMBeanUtils = getRaUsageMBeanUtils(entityName);

    RaEntityUsageMBeanUtils raEntityUsageMBeanUtils;
    if (parameterSet.length() == 0) {
      raEntityUsageMBeanUtils = resourceUsageMBeanUtils.getRaEntityUsageMBeanUtils();
    }
    else {
      raEntityUsageMBeanUtils = resourceUsageMBeanUtils.getRaEntityUsageMBeanUtils(parameterSet);
    }
    return raEntityUsageMBeanUtils.getUsageParameterInfos();
  }

  public void resetAllUsageParameters(String entityName, String parameterSet) throws ManagementConsoleException {

    ResourceUsageMBeanUtils resourceUsageMBeanUtils = getRaUsageMBeanUtils(entityName);

    RaEntityUsageMBeanUtils raEntityUsageMBeanUtils;
    if (parameterSet.length() == 0) {
      raEntityUsageMBeanUtils = resourceUsageMBeanUtils.getRaEntityUsageMBeanUtils();
    }
    else {
      raEntityUsageMBeanUtils = resourceUsageMBeanUtils.getRaEntityUsageMBeanUtils(parameterSet);
    }
    raEntityUsageMBeanUtils.resetAllUsageParameters();
  }

  public void resetUsageParameter(String entityName, String parameterSet, String parameterName, boolean isCounterType)
      throws ManagementConsoleException {

    ResourceUsageMBeanUtils resourceUsageMBeanUtils = getRaUsageMBeanUtils(entityName);

    RaEntityUsageMBeanUtils raEntityUsageMBeanUtils;
    if (parameterSet.length() == 0) {
      raEntityUsageMBeanUtils = resourceUsageMBeanUtils.getRaEntityUsageMBeanUtils();
    }
    else {
      raEntityUsageMBeanUtils = resourceUsageMBeanUtils.getRaEntityUsageMBeanUtils(parameterSet);
    }

    if (isCounterType) {
      raEntityUsageMBeanUtils.getCounterTypeParameter(parameterName, true);
    }
    else {
      raEntityUsageMBeanUtils.getSampleTypeParameter(parameterName, true);
    }
  }
}
