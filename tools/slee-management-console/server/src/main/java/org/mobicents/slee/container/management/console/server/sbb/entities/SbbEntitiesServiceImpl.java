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

package org.mobicents.slee.container.management.console.server.sbb.entities;

import java.util.logging.Logger;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntitiesService;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntityInfo;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.mbeans.SbbEntitiesMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;
import org.mobicents.slee.container.management.console.server.mbeans.SleeManagementMBeanUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Vladimir Ralev
 * 
 */
public class SbbEntitiesServiceImpl extends RemoteServiceServlet implements SbbEntitiesService {
  private static final long serialVersionUID = -5366122723778369L;

  private ManagementConsole managementConsole = ManagementConsole.getInstance();

  private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

  public SbbEntitiesServiceImpl() {

  }

  private static SbbEntityInfo objectToSbbEntityInfo(Object[] info) {
    if (info == null)
      return null;
    SbbEntityInfo sbbInfo = new SbbEntityInfo();
    sbbInfo.setSbbEntityId(info[0] != null ? info[0].toString() : null);
    sbbInfo.setParentId(info[1] != null ? info[1].toString() : null);
    sbbInfo.setRootId(info[2] != null ? info[2].toString() : null);
    sbbInfo.setSbbId(info[3] != null ? info[3].toString() : null);
    sbbInfo.setPriority(info[4] != null ? info[4].toString() : null);
    sbbInfo.setServiceConvergenceName(info[5] != null ? info[5].toString() : null);
    sbbInfo.setServiceId(info[6] != null ? info[6].toString() : null);
    return sbbInfo;
  }

  private static SbbEntityInfo objectByIdToSbbEntityInfo(String id, Object[] list) {
    if (id == null || list == null)
      return null;

    for (Object sbb : list) {
      Object[] info = (Object[]) sbb;
      if (id.equals(info[0].toString())) {
        return objectToSbbEntityInfo(info);
      }
    }

    return null;
  }

  public SbbEntityInfo retrieveSbbEntityInfo(String sbbeId) throws ManagementConsoleException {
    Logger.getLogger(this.getClass().getCanonicalName()).fine("----------- START[retrieveSbbEntityInfo]");
    try {
      SbbEntitiesMBeanUtils activity = sleeConnection.getSleeManagementMBeanUtils().getSbbEntitiesMBeanUtils();
      SbbEntityInfo sei = objectByIdToSbbEntityInfo(sbbeId, activity.retrieveAllSbbEntities());

      return sei;
    }
    catch (Exception e) {
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
    finally {
      Logger.getLogger(this.getClass().getCanonicalName()).fine("----------- END[retrieveSbbEntityInfo]");
    }

  }

  public SbbEntityInfo[] retrieveAllSbbEntities() throws ManagementConsoleException {
    Logger.getLogger(this.getClass().getCanonicalName()).fine("----------- START[retrieveAllSbbEntities]");
    try {
      SbbEntitiesMBeanUtils activity = sleeConnection.getSleeManagementMBeanUtils().getSbbEntitiesMBeanUtils();
      Object[] list = activity.retrieveAllSbbEntities();
      SbbEntityInfo[] sei = new SbbEntityInfo[list.length];
      for (int q = 0; q < sei.length; q++)
        sei[q] = objectToSbbEntityInfo((Object[]) list[q]);
      return sei;
    }
    catch (Exception e) {
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
    finally {
      Logger.getLogger(this.getClass().getCanonicalName()).fine("----------- END[retrieveAllSbbEntities]");
    }
  }

  public void removeSbbEntity(String sbbeId) throws ManagementConsoleException {
    Logger.getLogger(this.getClass().getCanonicalName()).fine("----------- START[removeSbbEntity]");
    try {
      SbbEntitiesMBeanUtils activity = sleeConnection.getSleeManagementMBeanUtils().getSbbEntitiesMBeanUtils();
      activity.removeSbbEntity(sbbeId);
    }
    catch (Exception e) {
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
    finally {
      Logger.getLogger(this.getClass().getCanonicalName()).fine("----------- END[removeSbbEntity]");
    }
  }

}
