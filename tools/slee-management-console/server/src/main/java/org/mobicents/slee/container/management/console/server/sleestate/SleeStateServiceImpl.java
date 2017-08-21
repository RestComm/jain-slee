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

package org.mobicents.slee.container.management.console.server.sleestate;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletException;
import javax.slee.management.SleeState;
import javax.slee.management.SleeStateChangeNotification;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.sleestate.SleeStateInfo;
import org.mobicents.slee.container.management.console.client.sleestate.SleeStateService;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Stefano Zappaterra
 * 
 */
public class SleeStateServiceImpl extends RemoteServiceServlet implements SleeStateService, NotificationListener {

  private static final long serialVersionUID = -5366129803407478369L;

  private SleeState sleeState;

  private ManagementConsole managementConsole = ManagementConsole.getInstance();

  private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

  public SleeStateServiceImpl() {
    super();
    try {
      sleeState = sleeConnection.getSleeManagementMBeanUtils().getState();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public SleeStateInfo getState() throws ManagementConsoleException {
    return SleeStateInfoUtils.toSleeStateInfo(sleeState);
  }

  public String getVersion() throws ManagementConsoleException {
    return sleeConnection.getSleeManagementMBeanUtils().getVersion();
  }

  public void shutdown() throws ManagementConsoleException {
    sleeConnection.getSleeManagementMBeanUtils().shutdown();
  }

  public void start() throws ManagementConsoleException {
    sleeConnection.getSleeManagementMBeanUtils().start();
  }

  public void stop() throws ManagementConsoleException {
    sleeConnection.getSleeManagementMBeanUtils().stop();
  }

  public void handleNotification(Notification notification, Object handback) {
    if (notification instanceof SleeStateChangeNotification) {
      SleeStateChangeNotification sleeStateChangeNotification = (SleeStateChangeNotification) notification;
      sleeState = sleeStateChangeNotification.getNewState();
    }
  }

  public void addNotificationListener() throws ManagementConsoleException {
    sleeConnection.getSleeManagementMBeanUtils().addNotificationListener(this);
  }

  public void removeNotificationListener() throws ManagementConsoleException {
    sleeConnection.getSleeManagementMBeanUtils().removeNotificationListener(this);
  }

  public void destroy() {
    try {
      removeNotificationListener();
    }
    catch (ManagementConsoleException e) {
      e.printStackTrace();
    }
    super.destroy();
  }

  public void init() throws ServletException {
    try {
      addNotificationListener();
    }
    catch (ManagementConsoleException e) {
      throw new ServletException(e);
    }
    super.init();
  }
}
