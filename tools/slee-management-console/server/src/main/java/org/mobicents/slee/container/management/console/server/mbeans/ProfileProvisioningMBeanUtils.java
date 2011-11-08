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

import java.util.Collection;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ProfileProvisioningMBeanUtils {

  private MBeanServerConnection mbeanServer;

  private ObjectName profileProvisioningMBean;

  public ProfileProvisioningMBeanUtils(MBeanServerConnection mbeanServer, ObjectName sleeManagementMBean) throws ManagementConsoleException {
    super();
    this.mbeanServer = mbeanServer;

    try {
      profileProvisioningMBean = (ObjectName) mbeanServer.getAttribute(sleeManagementMBean, "ProfileProvisioningMBean");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#createProfile(java.lang.String, java.lang.String)
   */
  public ObjectName createProfile(String arg0, String arg1) throws ManagementConsoleException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#createProfileTable(javax.slee.profile.ProfileSpecificationID, java.lang.String)
   */
  public void createProfileTable(ProfileSpecificationID arg0, String arg1) throws ManagementConsoleException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#getDefaultProfile(java.lang.String)
   */
  public ObjectName getDefaultProfile(String arg0) throws ManagementConsoleException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#getProfile(java.lang.String, java.lang.String)
   */
  public ObjectName getProfile(String arg0, String arg1) throws ManagementConsoleException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#getProfileSpecification(java.lang.String)
   */
  public ProfileSpecificationID getProfileSpecification(String arg0) throws ManagementConsoleException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#getProfileTables()
   */
  public Collection getProfileTables() throws ManagementConsoleException {
    try {
      return (Collection) mbeanServer.invoke(profileProvisioningMBean, "getProfileTables", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#getProfiles(java.lang.String)
   */
  public Collection getProfiles(String arg0) throws ManagementConsoleException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#getProfilesByIndexedAttribute(java.lang.String, java.lang.String, java.lang.Object)
   */
  public Collection getProfilesByIndexedAttribute(String arg0, String arg1, Object arg2) throws ManagementConsoleException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#removeProfile(java.lang.String, java.lang.String)
   */
  public void removeProfile(String arg0, String arg1) throws ManagementConsoleException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#removeProfileTable(java.lang.String)
   */
  public void removeProfileTable(String arg0) throws ManagementConsoleException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.slee.management.ProfileProvisioningMBean#renameProfileTable(java.lang.String, java.lang.String)
   */
  public void renameProfileTable(String arg0, String arg1) throws ManagementConsoleException {
    // TODO Auto-generated method stub

  }

}
