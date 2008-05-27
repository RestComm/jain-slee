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
package org.mobicents.slee.container.management.console.server.log;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.log.HandlerInfo;
import org.mobicents.slee.container.management.console.client.log.LogService;
import org.mobicents.slee.container.management.console.client.log.LoggerInfo;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.activity.ActivityServiceImpl;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author baranowb
 * 
 */
public class LogServiceImpl extends RemoteServiceServlet implements LogService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ManagementConsole managementConsole = ManagementConsole
			.getInstance();

	private SleeMBeanConnection sleeConnection = managementConsole
			.getSleeConnection();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.management.console.client.log.LogService#getLoggerNames()
	 */
	public List getLoggerNames() throws ManagementConsoleException {

		return sleeConnection.getSleeManagementMBeanUtils()
				.getLogManagementMBeanUtils().getLoggerNames(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.management.console.client.log.LogService#activateLogger(java.lang.String)
	 */
	public void setLoggerLevel(String loggerName, String level) 
			throws ManagementConsoleException {

		sleeConnection.getSleeManagementMBeanUtils()
				.getLogManagementMBeanUtils().setLoggerLevel(loggerName, level);

	}

	public String resetLoggerLevel(String loggerName)
			throws ManagementConsoleException {
		// This is called when logger is activated.
		sleeConnection.getSleeManagementMBeanUtils()
				.getLogManagementMBeanUtils().addLogger(loggerName, Level.OFF);
		sleeConnection.getSleeManagementMBeanUtils()
				.getLogManagementMBeanUtils().resetLoggerLevel(loggerName);
		return sleeConnection.getSleeManagementMBeanUtils()
				.getLogManagementMBeanUtils().getDefaultLoggerLevel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.management.console.client.log.LogService#fetchLoggerInfo(java.lang.String)
	 */
	public LoggerInfo fetchLoggerInfo(String loggerName) throws ManagementConsoleException {

		if(!sleeConnection.getSleeManagementMBeanUtils()
				.getLogManagementMBeanUtils().getLoggerNames(null).contains(loggerName))
			sleeConnection.getSleeManagementMBeanUtils()
			.getLogManagementMBeanUtils().addLogger(loggerName, Level.OFF);
			int handlerNum = sleeConnection.getSleeManagementMBeanUtils()
					.getLogManagementMBeanUtils().numberOfHandlers(loggerName);
			
			

			String _name=loggerName;
			String _level=sleeConnection.getSleeManagementMBeanUtils()
			.getLogManagementMBeanUtils().getLoggerLevel(loggerName);
			boolean _parent=sleeConnection.getSleeManagementMBeanUtils()
			.getLogManagementMBeanUtils().getUseParentHandlersFlag(loggerName);
			String _filter=sleeConnection.getSleeManagementMBeanUtils()
			.getLogManagementMBeanUtils().getLoggerFilterClassName(loggerName);
			HandlerInfo[] hInfos=new HandlerInfo[handlerNum];
			
			for(int i=0;i<handlerNum;i++)
			{
				String _formatterClass=sleeConnection.getSleeManagementMBeanUtils()
				.getLogManagementMBeanUtils().getGenericHandlerFormatterClassName(loggerName, i);
				String _filterClass=sleeConnection.getSleeManagementMBeanUtils()
				.getLogManagementMBeanUtils().getGenericHandlerFilterClassName(loggerName, i);
				String _h_level=sleeConnection.getSleeManagementMBeanUtils()
				.getLogManagementMBeanUtils().getGenericHandlerLevel(loggerName, i);
				String _h_name=sleeConnection.getSleeManagementMBeanUtils().getLogManagementMBeanUtils().getHandlerName(loggerName, i);
				String _h_className=sleeConnection.getSleeManagementMBeanUtils().getLogManagementMBeanUtils().getHandlerClassName(loggerName, i);
				//Add fetch for other options
				HandlerInfo hi=new HandlerInfo(i,(_h_name==null?"":_h_name),_filterClass,_formatterClass,_h_className,_h_level,new HashMap());
				hInfos[i]=hi;
			}
			

			return new LoggerInfo(_parent,_name,_filter,_level,hInfos);
		
	}

	
	public void removeHandlerAtIndex(String loggerName, int index) throws ManagementConsoleException
	{
		
		sleeConnection.getSleeManagementMBeanUtils()
		.getLogManagementMBeanUtils().removeHandler(loggerName, index);
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.console.client.log.LogService#getUseParentHandlers(java.lang.String)
	 */
	public boolean getUseParentHandlers(String loggerName)
			throws ManagementConsoleException {
		
		return sleeConnection.getSleeManagementMBeanUtils().getLogManagementMBeanUtils().getUseParentHandlersFlag(loggerName);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.console.client.log.LogService#setUseParentHandlers(java.lang.String, boolean)
	 */
	public void setUseParentHandlers(String loggerName, boolean value)
			throws ManagementConsoleException {
		
		sleeConnection.getSleeManagementMBeanUtils().getLogManagementMBeanUtils().setUseParentHandlersFlag(loggerName, value);
		
	}
	
}
