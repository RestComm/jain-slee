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

import java.util.List;
import java.util.logging.Level;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

/**
 * @author baranowb
 * 
 */
public class LogManagementMBeanUtils {

  private MBeanServerConnection mbeanServer = null;

  private ObjectName logMgmtMBeanName = null;

  public LogManagementMBeanUtils(MBeanServerConnection mbeanServer, ObjectName sleeManagementMBean) throws ManagementConsoleException {
    super();
    this.mbeanServer = mbeanServer;
    try {
      logMgmtMBeanName = new ObjectName("org.mobicents.slee:name=LogManagementMBean");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void _setDefaultLoggerLevel(Level l) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "_setDefaultLoggerLevel", new Object[] { l }, new String[] { "java.util.logging.Level" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  public Level _getDefaultLoggerLevel() throws ManagementConsoleException {
    try {
      return (Level) this.mbeanServer.invoke(logMgmtMBeanName, "_getDefaultLoggerLevel", null, null);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void setDefaultLoggerLevel(String l) throws ManagementConsoleException {
    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setDefaultLoggerLevel", new Object[] { l }, new String[] { "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String getDefaultLoggerLevel() throws ManagementConsoleException {
    try {
      return (String) this.mbeanServer.invoke(logMgmtMBeanName, "getDefaultLoggerLevel", null, null);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void _setDefaultHandlerLevel(Level l) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "_setDefaultHandlerLevel", new Object[] { l }, new String[] { "java.util.logging.Level" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  public Level _getDefaultHandlerLevel() throws ManagementConsoleException {
    try {
      return (Level) this.mbeanServer.invoke(logMgmtMBeanName, "_getDefaultHandlerLevel", null, null);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void setDefaultHandlerLevel(String l) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setDefaultHandlerLevel", new Object[] { l }, new String[] { "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String getDefaultHandlerLevel() throws ManagementConsoleException {
    try {
      return (String) this.mbeanServer.invoke(logMgmtMBeanName, "getDefaultHandlerLevel", null, null);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void setDefaultNotificationInterval(int numberOfEntries) throws ManagementConsoleException {
    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setDefaultNotificationInterval", new Object[] { numberOfEntries }, new String[] { "int" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public int getDefaultNotificationInterval() throws ManagementConsoleException {
    try {
      return ((Integer) this.mbeanServer.invoke(logMgmtMBeanName, "getDefaultNotificationInterval", null, null)).intValue();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  // ========= END PROPS

  /**
   * SImilar to LoggingMXBean, return list of available loggers. Filter is string that has to occur in loggers name.
   * 
   * @param - specifies string that has to occur in loggers name, if null - all names are returned. (Simply regex)
   * @return
   */
  public List<String> getLoggerNames(String regex) throws ManagementConsoleException {
    try {
      return (List<String>) this.mbeanServer.invoke(logMgmtMBeanName, "getLoggerNames", new Object[] { regex }, new String[] { "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * Same as LoggingMXBean - sets level for certain logger.
   * 
   * @param loggerName
   *          - name of the logger
   * @param level
   *          - level to be set
   * @throws IllegalArgumentException
   *           - thrown when there is no logger under certain name.
   */
  public void setLoggerLevel(String loggerName, Level level) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setLoggerLevel", new Object[] { loggerName, level }, new String[] { "java.lang.String",
          "java.util.logging.Level" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  /**
   * Same as LoggingMXBean - sets level for certain logger. This method accepts String representation of logger level.
   * 
   * @param loggerName
   *          - name of the logger
   * @param level
   *          - level to be set
   * @throws IllegalArgumentException
   *           - thrown when there is no logger under certain name or String dose not represent valid logger level.
   */
  public void setLoggerLevel(String loggerName, String level) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setLoggerLevel", new Object[] { loggerName, level }, new String[] { "java.lang.String", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * Resets all loggers level to default one
   */
  public void resetLoggerLevels() throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "resetLoggerLevels", null, null);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  /**
   * Resets loggers under certain name to log on certain level.
   * 
   * @param loggerName
   */
  public void resetLoggerLevel(String loggerName) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "resetLoggerLevel", new Object[] { loggerName }, new String[] { "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * Removes(set level to Level.OFF, handlers are removed) all loggers that were created by this MBean - this will usually include loggers
   * for whole packages. It also removes all handlers - either NotificationHandler and SocketHandler for each defined logger(either by this
   * MBean or by source code).
   */
  public void clearLoggers() throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "clearLoggers", null, null);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * Removes all loggers under certain branch.
   * 
   * @param name
   *          - logger name(branch name)
   */
  public void clearLoggers(String name) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "clearLoggers", new Object[] { name }, new String[] { "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  /**
   * Tries to add logger if it doesnt exist
   * 
   * @param name
   *          - name of logger
   * @param level
   *          - level for this logger, if <b>null</b> than default level for logger is used
   * @return <ul>
   *         <li><b>true</b> - if logger didnt exist and it was created</li>
   *         <li><b>false</b> - if logger did exist and it was not created</li>
   *         </ul>
   * @throws NullPointerException
   *           - when arg is null{}
   */
  public boolean addLogger(String name, Level level) throws NullPointerException, ManagementConsoleException {
    try {
      return ((Boolean) this.mbeanServer.invoke(logMgmtMBeanName, "addLogger", new Object[] { name, level }, new String[] { "java.lang.String",
          "java.util.logging.Level" })).booleanValue();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * Tries to add logger if it doesnt exist
   * 
   * @param name
   *          - name of logger
   * @param level
   *          - level for this logger, if <b>null</b> than default level for logger is used
   * @return <ul>
   *         <li><b>true</b> - if logger didnt exist and it was created</li>
   *         <li><b>false</b> - if logger did exist and it was not created</li>
   *         </ul>
   * 
   * @throws IllegalArgumentException
   *           - when level is not valid string representation of logging level
   * @throws NullPointerException
   *           - when arg is null{}
   */
  public boolean addLogger(String name, String level) throws ManagementConsoleException {
    try {
      return ((Boolean) this.mbeanServer.invoke(logMgmtMBeanName, "addLogger", new Object[] { name, level }, new String[] { "java.lang.String",
          "java.lang.String" })).booleanValue();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * Adds SocketHandler to certain logger, this logger must exist prior this function is called
   * 
   * @param loggerName
   *          - name of the logger, cant be null
   * @param handlerLevel
   *          - level for this handler, if its null default level for handlers will be used
   * @param handlerName
   *          - name for this handler, cant be duplicate.
   * @param formaterClassName
   *          - name of formater class for this handler, can be null.
   * @param filterClassName
   *          - name of filter class for this handler, can be null.
   * @param host
   *          - host address
   * @param port
   *          - port address
   * @throws IllegalArgumentException
   *           - thrown when:
   *           <ul>
   *           <li>host is not a valid inet address</li>
   *           <li>port <0</li>
   *           </ul>
   * @throws NullPointerException
   *           - if arg other than:
   *           <ul>
   *           <li>formaterClassName</li>
   *           <li>filterClassName</li>
   *           <li>handlerLevel</li>
   *           </ul>
   *           is null.
   * @throws IllegalStateException
   *           :- thrown when:
   *           <ul>
   *           <li>Logger under certain name doesnt exist</li>
   *           <li>handler name is duplicate of another handler for this logger or is reserved one ->NOTIFICATION</li>
   *           </ul>
   * @throw IOException - when host cant be reached
   */
  public void addSocketHandler(String loggerName, Level handlerLevel, String handlerName, String formaterClassName, String filterClassName, String host,
      int port) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "addSocketHandler", new Object[] { loggerName, handlerLevel, handlerName, formaterClassName, filterClassName,
          host, port }, new String[] { "java.lang.String", "java.util.logging.Level", "java.lang.String", "java.lang.String", "java.lang.String",
          "java.lang.String", "int" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  /**
   * Adds SocketHandler to certain logger, this logger must exist prior this function is called
   * 
   * @param loggerName
   *          - name of the logger, cant be null
   * @param handlerLevel
   *          - level for this handler, if its null default level for handlers will be used
   * @param handlerName
   *          - name for this handler, cant be duplicate.
   * @param formaterClassName
   *          - name of formater class for this handler, can be null.
   * @param filterClassName
   *          - name of filter class for this handler, can be null.
   * @param host
   *          - host address
   * @param port
   *          - port address
   * @throws IllegalArgumentException
   *           - thrown when:
   *           <ul>
   *           <li>host is not a valid inet address</li>
   *           <li>port <0</li>
   *           <li>handlerLevel!=null and does not represent valid logging level</li>
   *           <li>formaterClassName is not valid formater class</li>
   *           <li>filterClassName is not valid filter class</li>
   *           </ul>
   * @throws NullPointerException
   *           - if arg other than:
   *           <ul>
   *           <li>formaterClassName</li>
   *           <li>filterClassName</li>
   *           <li>handlerLevel</li>
   *           </ul>
   *           is null.
   * @throws IllegalStateException
   *           :- thrown when:
   *           <ul>
   *           <li>Logger under certain name doesnt exist</li>
   *           <li>handler name is duplicate of another handler for this logger or is reserved one ->NOTIFICATION</li>
   *           </ul>
   * @throw IOException - when host cant be reached
   */
  public void addSocketHandler(String loggerName, String handlerLevel, String handlerName, String formaterClassName, String filterClassName, String host,
      int port) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "addSocketHandler", new Object[] { loggerName, handlerLevel, handlerName, formaterClassName, filterClassName,
          host, port }, new String[] { "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String",
          "int" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * Tries to remove handler from logger.
   * 
   * @param loggerName
   *          - name of the logger
   * @param handlerName
   *          - handler name that has been added by this MBean
   * @return <ul>
   *         <li><b>true</b> - if logger exists, and it was removed</li>
   *         <li><b>false</b> - otherwise</li>
   *         </ul>
   */
  public boolean removeHandler(String loggerName, String handlerName) throws ManagementConsoleException {
    try {
      return ((Boolean) this.mbeanServer.invoke(logMgmtMBeanName, "removeHandler", new Object[] { loggerName, handlerName }, new String[] { "java.lang.String",
          "java.lang.String" })).booleanValue();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public boolean removeHandler(String loggerName, int index) throws ManagementConsoleException {
    try {
      return ((Boolean) this.mbeanServer.invoke(logMgmtMBeanName, "removeHandler", new Object[] { loggerName, index },
          new String[] { "java.lang.String", "int" })).booleanValue();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * Adds notification handler to logger if it exists. Its name is set to <b>NOTIFICATION</b>. There can be only one notification handler.
   * This handler holds reference to up to numberOfEntries log entries and fires notification. Notification can be triggered prematurely in
   * case when someone calls fetchLogContent function, this will cause notification to be fired along with log entries return as outcome of
   * invocation.
   * 
   * @param loggerName
   *          - name of the logger, it must exists prior this function call
   * @param numberOfEntries
   *          - number of log entries after notification is sent. If <=0 default value is used
   * @param level
   *          - level for this handler, if null, default value is used
   * @param formaterClassName
   *          - name of formater class for this handler, can be null.
   * @param filterClassName
   *          - name of filter class for this handler, can be null.
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>logger under certain name does not exist</li>
   *           <li>formaterClassName is not valid formater class</li>
   *           <li>filterClassName is not valid filter class</li>
   *           </ul>
   * @throws IllegalStateException
   *           - logger under that name already has notification handler
   * @throws NullPointerException
   *           - loggerName is null
   */
  public void addNotificationHandler(String loggerName, int numberOfEntries, Level level, String formaterClassName, String filterClassName)
      throws IllegalArgumentException, IllegalStateException, NullPointerException, ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "addNotificationHandler",
          new Object[] { loggerName, numberOfEntries, level, formaterClassName, filterClassName }, new String[] { "java.lang.String", "int",
              "java.util.logging.Logger", "java.lang.String", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  public void addNotificationHandler(String loggerName, int numberOfEntries, String level, String formaterClassName, String filterClassName)
      throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "addNotificationHandler",
          new Object[] { loggerName, numberOfEntries, level, formaterClassName, filterClassName }, new String[] { "java.lang.String", "int",
              "java.lang.String", "java.lang.String", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  /**
   * Returns names of handlers for logger
   * 
   * @param loggerName
   *          - logger name, cant be null, must be valid
   * @return List with names of handlers for this loggers
   * @throws IllegalArgumentException
   *           - logger does not exist
   */
  public List getHandlerNamesForLogger(String loggerName) throws ManagementConsoleException {
    try {
      return (List) this.mbeanServer.invoke(logMgmtMBeanName, "getHandlerNamesForLogger", new Object[] { loggerName }, new String[] { "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * Fetches level of particular handler
   * 
   * @param loggerName
   *          - name of the logger, cant be null and logger must exist
   * @param handlerName
   *          - valid name of handler, cant be null
   * @return
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>handler does not exist</li>
   *           </ul>
   * 
   * @throws NullPointerException
   *           thrown when:
   *           <ul>
   *           <li>arg is null</li>
   *           </ul>
   */
  public String getHandlerLevel(String loggerName, String handlerName) throws ManagementConsoleException {
    try {
      return (String) this.mbeanServer.invoke(logMgmtMBeanName, "getHandlerLevel", new Object[] { loggerName, handlerName }, new String[] { "java.lang.String",
          "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * 
   * @param loggerName
   *          - name of the logger, cant be null and logger must exist
   * @param handlerName
   *          - name of the handler, cant be null and handler ust exist
   * @return
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>logger does not exist</li>
   *           <li>handler under specific name does not exist</li>
   *           </ul>
   * @throws NullPointerException
   *           thrown when:
   *           <ul>
   *           <li>arg is null</li>
   *           </ul>
   */
  public String getHandlerFilterClassName(String loggerName, String handlerName) throws IllegalArgumentException, NullPointerException,
      ManagementConsoleException {
    try {
      return (String) this.mbeanServer.invoke(logMgmtMBeanName, "getHandlerFilterClassName", new Object[] { loggerName, handlerName }, new String[] {
          "java.lang.String", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * 
   * @param loggerName
   *          - name of the logger, cant be null and logger must exist
   * @param handlerName
   *          - name of the handler, cant be null and handler ust exist
   * @return
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>logger does not exist</li>
   *           <li>handler under specific name does not exist</li>
   *           </ul>
   * @throws NullPointerException
   *           thrown when:
   *           <ul>
   *           <li>arg is null</li>
   *           </ul>
   */
  public String getHandlerFormaterClassName(String loggerName, String handlerName) throws IllegalArgumentException, NullPointerException,
      ManagementConsoleException {
    try {
      return (String) this.mbeanServer.invoke(logMgmtMBeanName, "getHandlerFormaterClassName", new Object[] { loggerName, handlerName }, new String[] {
          "java.lang.String", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * 
   * @param loggerName
   *          - name of the logger, cant be null and logger must exist
   * @return
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>logger does not exist</li>
   *           </ul>
   * @throws NullPointerException
   *           thrown when:
   *           <ul>
   *           <li>args is null</li>
   * @throws IllegalStateException
   *           thrown when:
   *           <ul>
   *           <li>logger does not have notification handler</li>
   *           </ul>
   */
  public int getHandlerNotificationInterval(String loggerName) throws ManagementConsoleException {
    try {
      return ((Integer) this.mbeanServer.invoke(logMgmtMBeanName, "getHandlerNotificationInterval", new Object[] { loggerName },
          new String[] { "java.lang.String" })).intValue();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String getHandlerClassName(String loggerName, String handlerName) throws IllegalArgumentException, NullPointerException, IllegalStateException,
      ManagementConsoleException {
    try {
      return (String) this.mbeanServer.invoke(logMgmtMBeanName, "getHandlerClassName", new Object[] { loggerName, handlerName }, new String[] {
          "java.lang.String", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String getHandlerClassName(String loggerName, int index) throws IllegalArgumentException, NullPointerException, IllegalStateException,
      ManagementConsoleException {
    try {
      return (String) this.mbeanServer.invoke(logMgmtMBeanName, "getHandlerClassName", new Object[] { loggerName, index }, new String[] { "java.lang.String",
          "int" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  /**
   * Fetches level of particular handler
   * 
   * @param loggerName
   *          - name of the logger, cant be null and logger must exist
   * @param handlerName
   *          - valid name of handler, cant be null
   * @param level
   *          - new handelr level
   * @return
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>handler does not exist</li>
   *           <li>logger does not exist</li>
   *           <li>level is not valid string representation of logging level</li>
   *           </ul>
   * 
   * @throws NullPointerException
   *           thrown when:
   *           <ul>
   *           <li>arg is null</li>
   *           </ul>
   */
  public void setHandlerLevel(String loggerName, String handlerName, String level) throws IllegalArgumentException, NullPointerException,
      ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setHandlerLevel", new Object[] { loggerName, handlerName, level }, new String[] { "java.lang.String",
          "java.lang.String", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  /**
   * Fetches level of particular handler
   * 
   * @param loggerName
   *          - name of the logger, cant be null and logger must exist
   * @param handlerName
   *          - valid name of handler, cant be null
   * @param level
   *          - new handelr level
   * @return
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>handler does not exist</li>
   *           <li>logger does not exist</li>
   *           </ul>
   * 
   * @throws NullPointerException
   *           thrown when:
   *           <ul>
   *           <li>arg is null</li>
   *           </ul>
   */
  public void setHandlerLevel(String loggerName, String handlerName, Level level) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setHandlerLevel", new Object[] { loggerName, handlerName, level }, new String[] { "java.lang.String",
          "java.lang.String", "java.util.logging.Level" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  /**
   * 
   * @param loggerName
   *          - name of the logger, cant be null and logger must exist
   * @param handlerName
   *          - name of the handler, cant be null and handler ust exist
   * @return
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>logger does not exist</li>
   *           <li>handler under specific name does not exist</li>
   *           <li>className is not valid filter class name</li>
   *           </ul>
   * @throws NullPointerException
   *           thrown when:
   *           <ul>
   *           <li>args is null</li>
   *           </ul>
   */
  public void setHandlerFilterClassName(String loggerName, String handlerName, String className) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setHandlerFilterClassName", new Object[] { loggerName, handlerName, className }, new String[] {
          "java.lang.String", "java.lang.String", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  /**
   * 
   * @param loggerName
   *          - name of the logger, cant be null and logger must exist
   * @param handlerName
   *          - name of the handler, cant be null and handler ust exist
   * @return
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>logger does not exist</li>
   *           <li>handler under specific name does not exist</li>
   *           <li>className is not a valid formater class name</li>
   *           </ul>
   * @throws NullPointerException
   *           thrown when:
   *           <ul>
   *           <li>args is null</li>
   *           </ul>
   */
  public void setHandlerFormaterClassName(String loggerName, String handlerName, String className) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setHandlerFormaterClassName", new Object[] { loggerName, handlerName, className }, new String[] {
          "java.lang.String", "java.lang.String", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  /**
   * 
   * @param loggerName
   *          - name of the logger, cant be null and logger must exist
   * @return
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>logger does not exist</li>
   *           </ul>
   * @throws NullPointerException
   *           thrown when:
   *           <ul>
   *           <li>args is null</li>
   * @throws IllegalStateException
   *           thrown when:
   *           <ul>
   *           <li>logger does not have notification handler</li>
   *           </ul>
   */
  public void setHandlerNotificationInterval(String loggerName, int numberOfEntries) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setHandlerNotificationInterval", new Object[] { loggerName, numberOfEntries }, new String[] {
          "java.lang.String", "int" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  /**
   * 
   * @param loggerName
   *          - name of the logger, cant be null and logger must exist
   * @throws IllegalArgumentException
   *           thrown when:
   *           <ul>
   *           <li>logger does not exist</li>
   *           </ul>
   * @throws NullPointerException
   *           thrown when:
   *           <ul>
   *           <li>args is null</li>
   * @throws IllegalStateException
   *           thrown when:
   *           <ul>
   *           <li>logger does not have notification handler</li>
   *           </ul>
   */
  public String fetchLog(String loggerName) throws ManagementConsoleException {
    try {
      return (String) this.mbeanServer.invoke(logMgmtMBeanName, "fetchLog", new Object[] { loggerName }, new String[] { "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  // ** METHODS BELOW ARE GENERIC, THEY ALLOW TO CONTROLL ALL HANDLERS
  /**
   * Triggers reareadigng configuration file.
   * 
   * @param uri
   *          - target configuration file in Properties format, if null, local file is read(jre local)
   */
  public void reReadConf(String uri) throws ManagementConsoleException {

  }

  public void addHandler(String loggerName, String handlerName, String handlerClassName, String formaterClass, String filterClass,
      String[] constructorParameters, String[] paramValues) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "addHandler", new Object[] { loggerName, handlerName, handlerClassName, formaterClass, filterClass,
          constructorParameters, paramValues }, new String[] { "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String",
          "java.lang.String", "java.lang.String[]", "java.lang.String[]", });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  public int numberOfHandlers(String loggerName) throws ManagementConsoleException {
    try {
      return ((Integer) this.mbeanServer.invoke(logMgmtMBeanName, "numberOfHandlers", new Object[] { loggerName }, new String[] { "java.lang.String" }))
          .intValue();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String getGenericHandlerLevel(String loggerName, int index) throws ManagementConsoleException {
    try {
      return ((String) this.mbeanServer.invoke(logMgmtMBeanName, "getGenericHandlerLevel", new Object[] { loggerName, index }, new String[] {
          "java.lang.String", "int" }));
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String getGenericHandlerFilterClassName(String loggerName, int index) throws NullPointerException, IllegalArgumentException,
      ManagementConsoleException {
    try {
      return ((String) this.mbeanServer.invoke(logMgmtMBeanName, "getGenericHandlerFilterClassName", new Object[] { loggerName, index }, new String[] {
          "java.lang.String", "int" }));
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String getGenericHandlerFormatterClassName(String loggerName, int index) throws ManagementConsoleException {
    try {
      return ((String) this.mbeanServer.invoke(logMgmtMBeanName, "getGenericHandlerFormatterClassName", new Object[] { loggerName, index }, new String[] {
          "java.lang.String", "int" }));
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void setGenericHandlerLevel(String loggerName, int index, String level) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setGenericHandlerLevel", new Object[] { loggerName, index, level }, new String[] { "java.lang.String", "int",
          "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  public void setGenericHandlerFilterClassName(String loggerName, int index, String className) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setGenericHandlerFilterClassName", new Object[] { loggerName, index, className }, new String[] {
          "java.lang.String", "int", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  public void setGenericHandlerFormatterClassName(String loggerName, int index, String className) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setGenericHandlerFormatterClassName", new Object[] { loggerName, index, className }, new String[] {
          "java.lang.String", "int", "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  public String getHandlerName(String loggerName, int index) throws ManagementConsoleException {
    try {
      return (String) this.mbeanServer.invoke(logMgmtMBeanName, "getHandlerName", new Object[] { loggerName, index },
          new String[] { "java.lang.String", "int" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void setHandlerName(String loggerName, int index, String newName) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setHandlerName", new Object[] { loggerName, index, newName }, new String[] { "java.lang.String", "int",
          "java.lang.String" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  public void setUseParentHandlersFlag(String loggerName, boolean flag) throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "setUseParentHandlersFlag", new Object[] { loggerName, flag }, new String[] { "java.lang.String", "boolean" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  public boolean getUseParentHandlersFlag(String loggerName) throws ManagementConsoleException {
    try {
      return ((Boolean) this.mbeanServer.invoke(logMgmtMBeanName, "getUseParentHandlersFlag", new Object[] { loggerName }, new String[] { "java.lang.String" }))
          .booleanValue();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

  public String getLoggerLevel(String loggerName) throws ManagementConsoleException {
    try {
      return ((String) this.mbeanServer.invoke(logMgmtMBeanName, "getLoggerLevel", new Object[] { loggerName }, new String[] { "java.lang.String" }));
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void setLoggerFilterClassName(String loggerName, String className, String[] constructorParameters, String[] paramValues)
      throws ManagementConsoleException {

    try {
      this.mbeanServer.invoke(logMgmtMBeanName, "getLoggerFilterClassName", new Object[] { loggerName, className, constructorParameters, paramValues },
          new String[] { "java.lang.String", "java.lang.String", "java.lang.String[]", "java.lang.String[]" });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String getLoggerFilterClassName(String loggerName) throws ManagementConsoleException {
    try {
      return ((String) this.mbeanServer.invoke(logMgmtMBeanName, "getLoggerFilterClassName", new Object[] { loggerName }, new String[] { "java.lang.String" }));
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

}
