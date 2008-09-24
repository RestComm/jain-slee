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
package org.mobicents.slee.container.management.console.client.log;

import java.util.HashMap;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author baranowb
 * 
 */
public interface LogServiceAsync {

	void addHandler(java.lang.String loggerName, java.lang.String handlerName, java.lang.String handlerClassName, java.lang.String formaterClass, java.lang.String filterClass,
			java.lang.String[] constructorParameters, java.lang.String[] paramValues, com.google.gwt.user.client.rpc.AsyncCallback arg8);

	void addLogger(java.lang.String name, java.lang.String level, com.google.gwt.user.client.rpc.AsyncCallback arg3);

	void addNotificationHandler(java.lang.String loggerName, int numberOfEntries, java.lang.String level, java.lang.String formaterClassName, java.lang.String filterClassName,
			com.google.gwt.user.client.rpc.AsyncCallback arg6);

	void addSocketHandler(java.lang.String loggerName, java.lang.String handlerLevel, java.lang.String handlerName, java.lang.String formaterClassName,
			java.lang.String filterClassName, java.lang.String host, int port, com.google.gwt.user.client.rpc.AsyncCallback arg8);

	void clearLoggers(java.lang.String name, com.google.gwt.user.client.rpc.AsyncCallback arg2);

	void fetchLoggerInfo(java.lang.String loggerName, com.google.gwt.user.client.rpc.AsyncCallback arg2);

	void getDefaultHandlerLevel(com.google.gwt.user.client.rpc.AsyncCallback arg1);

	void getDefaultLoggerLevel(com.google.gwt.user.client.rpc.AsyncCallback arg1);

	void getDefaultNotificationInterval(com.google.gwt.user.client.rpc.AsyncCallback arg1);

	void getLoggerNames(com.google.gwt.user.client.rpc.AsyncCallback arg1);

	void getUseParentHandlers(java.lang.String loggerName, com.google.gwt.user.client.rpc.AsyncCallback arg2);

	void reReadConf(java.lang.String uri, com.google.gwt.user.client.rpc.AsyncCallback arg2);

	void removeHandlerAtIndex(java.lang.String loggerName, int index, com.google.gwt.user.client.rpc.AsyncCallback arg3);

	void resetLoggerLevel(java.lang.String loggerName, com.google.gwt.user.client.rpc.AsyncCallback arg2);

	void setDefaultHandlerLevel(java.lang.String l, com.google.gwt.user.client.rpc.AsyncCallback arg2);

	void setDefaultLoggerLevel(java.lang.String l, com.google.gwt.user.client.rpc.AsyncCallback arg2);

	void setDefaultNotificationInterval(int numberOfEntries, com.google.gwt.user.client.rpc.AsyncCallback arg2);

	void setLoggerFilterClassName(java.lang.String loggerName, java.lang.String className, java.lang.String[] constructorParameters, java.lang.String[] paramValues,
			com.google.gwt.user.client.rpc.AsyncCallback arg5);

	void setLoggerLevel(java.lang.String loggerName, java.lang.String level, com.google.gwt.user.client.rpc.AsyncCallback arg3);

	void setUseParentHandlers(java.lang.String loggerName, boolean value, com.google.gwt.user.client.rpc.AsyncCallback arg3);

	void getLoggerLevel(String fullName, AsyncCallback asyncCallback);
}
