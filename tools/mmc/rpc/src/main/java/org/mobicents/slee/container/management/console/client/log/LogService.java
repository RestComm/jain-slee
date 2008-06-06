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


import java.util.List;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author baranowb
 *
 */
public interface LogService extends RemoteService {
	/**
	 * 
	 * @return
	 * @throws ManagementConsoleException
	 * @gwt.typeArgs <java.lang.String>
	 */
	public List getLoggerNames() throws ManagementConsoleException;
	public void setLoggerLevel(String loggerName, String level)throws ManagementConsoleException;
	public String resetLoggerLevel(String loggerName) throws ManagementConsoleException;

	/**
	 * If this logger does not exists it will be created with Level.OFF
	 * @param loggerName
	 * @return
	 */
	public LoggerInfo fetchLoggerInfo(String loggerName)throws ManagementConsoleException ;
	
	public void removeHandlerAtIndex(String loggerName, int index) throws ManagementConsoleException;
	
	public boolean getUseParentHandlers(String loggerName) throws ManagementConsoleException;
	public void setUseParentHandlers(String loggerName, boolean value) throws ManagementConsoleException;
 }
