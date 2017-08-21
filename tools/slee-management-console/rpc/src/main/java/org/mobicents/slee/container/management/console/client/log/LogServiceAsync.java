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
