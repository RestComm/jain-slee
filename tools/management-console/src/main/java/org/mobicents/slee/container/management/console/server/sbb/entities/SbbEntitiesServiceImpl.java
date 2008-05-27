/*
 * Mobicents: The Open Source VoIP Middleware Platform
 *
 * Copyright 2003-2006, Mobicents, 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU General Public License (GPL) as
 * published by the Free Software Foundation; 
 * either version 2 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package org.mobicents.slee.container.management.console.server.sbb.entities;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntitiesService;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntityInfo;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.mbeans.SbbEntitiesMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Vladimir Ralev
 *
 */
public class SbbEntitiesServiceImpl extends RemoteServiceServlet implements SbbEntitiesService
{
   private static final long serialVersionUID = -5366122723778369L;

   private ManagementConsole managementConsole = ManagementConsole
           .getInstance();

   private SleeMBeanConnection sleeConnection = managementConsole
           .getSleeConnection();

   public SbbEntitiesServiceImpl() {

   }
   
   private static SbbEntityInfo objectToSbbEntityInfo(Object[] info)
   {
      if(info == null) return null;
      SbbEntityInfo sbbInfo = new SbbEntityInfo();
      sbbInfo.setSbbEntityId((String)info[0]);
      sbbInfo.setCurrentEvent((String)info[9]);
      sbbInfo.setParentId((String)info[2]);
      sbbInfo.setPriority((String)info[5]);
      sbbInfo.setRootId((String)info[3]);
      sbbInfo.setSbbId((String)info[4]);
      sbbInfo.setServiceConvergenceName((String)info[6]);
      sbbInfo.setServiceId((String)info[8]);
      sbbInfo.setUsageParameterPath((String)info[7]);
      sbbInfo.setNodeName((String)info[1]);
      return sbbInfo;
   }
   
   public SbbEntityInfo retrieveSbbEntityInfo(String sbbeId)
   throws ManagementConsoleException
   {
      SbbEntitiesMBeanUtils activity = 
         sleeConnection.getSleeManagementMBeanUtils()
         .getSbbEntitiesMBeanUtils();
      SbbEntityInfo sei = 
         objectToSbbEntityInfo(activity.retrieveSbbEntityInfo(sbbeId));
      return sei;
   }
   
   public SbbEntityInfo[] retrieveAllSbbEntities()
   throws ManagementConsoleException
   {
      SbbEntitiesMBeanUtils activity = 
         sleeConnection.getSleeManagementMBeanUtils()
         .getSbbEntitiesMBeanUtils();
      Object[] list = activity.retrieveAllSbbEntities();
      SbbEntityInfo[] sei = new SbbEntityInfo[list.length];
      for(int q=0; q<sei.length; q++)
         sei[q] = objectToSbbEntityInfo((Object[])list);
      return sei;
   }

  public void removeSbbEntity(String sbbeId) throws ManagementConsoleException
  {
     SbbEntitiesMBeanUtils activity = 
        sleeConnection.getSleeManagementMBeanUtils()
        .getSbbEntitiesMBeanUtils();
     activity.removeSbbEntity(sbbeId);
  }


}
