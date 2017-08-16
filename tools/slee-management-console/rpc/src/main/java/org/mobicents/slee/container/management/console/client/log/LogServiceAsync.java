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

package org.mobicents.slee.container.management.console.client.log;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author baranowb
 * 
 */
public interface LogServiceAsync {

  void addHandler(String loggerName, String handlerName, String handlerClassName, String formaterClass, String filterClass, String[] constructorParameters,
      String[] paramValues, AsyncCallback arg8);

  void addLogger(String name, String level, AsyncCallback arg3);

  void addNotificationHandler(String loggerName, int numberOfEntries, String level, String formaterClassName, String filterClassName, AsyncCallback arg6);

  void addSocketHandler(String loggerName, String handlerLevel, String handlerName, String formaterClassName, String filterClassName, String host, int port,
      AsyncCallback arg8);

  void clearLoggers(String name, AsyncCallback arg2);

  void fetchLoggerInfo(String loggerName, AsyncCallback arg2);

  void getDefaultHandlerLevel(AsyncCallback arg1);

  void getDefaultLoggerLevel(AsyncCallback arg1);

  void getDefaultNotificationInterval(AsyncCallback arg1);

  void getLoggerNames(AsyncCallback arg1);

  void getUseParentHandlers(String loggerName, AsyncCallback arg2);

  void reReadConf(String uri, AsyncCallback arg2);

  void removeHandlerAtIndex(String loggerName, int index, AsyncCallback arg3);

  void resetLoggerLevel(String loggerName, AsyncCallback arg2);

  void setDefaultHandlerLevel(String l, AsyncCallback arg2);

  void setDefaultLoggerLevel(String l, AsyncCallback arg2);

  void setDefaultNotificationInterval(int numberOfEntries, AsyncCallback arg2);

  void setLoggerFilterClassName(String loggerName, String className, String[] constructorParameters, String[] paramValues, AsyncCallback arg5);

  void setLoggerLevel(String loggerName, String level, AsyncCallback arg3);

  void setUseParentHandlers(String loggerName, boolean value, AsyncCallback arg3);

  void getLoggerLevel(String fullName, AsyncCallback asyncCallback);
}
