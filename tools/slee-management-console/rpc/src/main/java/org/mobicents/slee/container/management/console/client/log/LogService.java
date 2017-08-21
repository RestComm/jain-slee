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

package org.mobicents.slee.container.management.console.client.log;

import java.util.List;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author baranowb
 * 
 */
public interface LogService extends RemoteService {

  public List<String> getLoggerNames() throws ManagementConsoleException;

  public void setLoggerLevel(String loggerName, String level) throws ManagementConsoleException;

  public String resetLoggerLevel(String loggerName) throws ManagementConsoleException;

  /**
   * If this logger does not exists it will be created with Level.OFF
   * 
   * @param loggerName
   * @return
   */
  public LoggerInfo fetchLoggerInfo(String loggerName) throws ManagementConsoleException;

  public void removeHandlerAtIndex(String loggerName, int index) throws ManagementConsoleException;

  public boolean getUseParentHandlers(String loggerName) throws ManagementConsoleException;

  public void setUseParentHandlers(String loggerName, boolean value) throws ManagementConsoleException;

  public void addSocketHandler(String loggerName, String handlerLevel, String handlerName, String formaterClassName, String filterClassName, String host,
      int port) throws ManagementConsoleException;

  public void addNotificationHandler(String loggerName, int numberOfEntries, String level, String formaterClassName, String filterClassName)
      throws ManagementConsoleException;

  public int getDefaultNotificationInterval() throws ManagementConsoleException;

  public void addHandler(String loggerName, String handlerName, String handlerClassName, String formaterClass, String filterClass,
      String[] constructorParameters, String[] paramValues) throws ManagementConsoleException;

  public void setLoggerFilterClassName(String loggerName, String className, String[] constructorParameters, String[] paramValues)
      throws ManagementConsoleException;

  public void setDefaultLoggerLevel(String l) throws ManagementConsoleException;

  public void setDefaultHandlerLevel(String l) throws ManagementConsoleException;

  public void addLogger(String name, String level) throws ManagementConsoleException;

  public void reReadConf(String uri) throws ManagementConsoleException;

  public void clearLoggers(String name) throws ManagementConsoleException;

  public void setDefaultNotificationInterval(int numberOfEntries) throws ManagementConsoleException;

  public String getDefaultLoggerLevel() throws ManagementConsoleException;

  public String getDefaultHandlerLevel() throws ManagementConsoleException;

  public String getLoggerLevel(String fullName) throws ManagementConsoleException;

}
