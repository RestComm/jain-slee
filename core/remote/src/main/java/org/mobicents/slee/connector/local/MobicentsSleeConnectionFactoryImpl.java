/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

/**
 * 
 */
package org.mobicents.slee.connector.local;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import javax.slee.connection.SleeConnection;
import org.apache.log4j.Logger;
import org.jboss.util.naming.Util;
import org.mobicents.slee.connector.local.MobicentsSleeConnectionFactory;
import org.mobicents.slee.container.AbstractSleeContainerModule;


/**
 * @author baranowb
 *
 */
public class MobicentsSleeConnectionFactoryImpl extends AbstractSleeContainerModule implements MobicentsSleeConnectionFactory, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//not this class implements serializable, but its bound only to loca JVM, its not accessbile outside!
	private static final String _DEFAULT_NAME="/MobicentsConnectionFactory";
	private static final Logger logger = Logger.getLogger(MobicentsSleeConnectionFactoryImpl.class);
	private String jndiName = _DEFAULT_NAME;
	private List<SleeConnection> connectionList = new ArrayList<SleeConnection>();
	private transient SleeConnectionService service;
	
	public MobicentsSleeConnectionFactoryImpl() {
		super();
		
	}


	/* (non-Javadoc)
	 * @see javax.slee.connection.SleeConnectionFactory#getConnection()
	 */
	public SleeConnection getConnection() throws ResourceException {
		//this method is listed as MBean method, upon lookup in jmx console it will create connection, its a possible leak 
		//with list.
		synchronized (connectionList) {
			SleeConnectionImpl conn;
			if(connectionList.size()>0)
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("Using connection from pool.");
				}
				conn = (SleeConnectionImpl) connectionList.remove(0);
				conn.start(this.service);
			}else
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("Creating new connection.");
				}
				conn = new SleeConnectionImpl(this.service,this);
			}
			return conn;
			
		}
		
	}

	
	public void start() throws Exception
	{
		
	}
	
	public void stop()
	{
		
	}
	
	//local methods called by connection
	void connectionClosed(SleeConnectionImpl conn)
	{
		synchronized (connectionList) {
			this.connectionList.add(conn);
		}
	}

	//bean methods

	public String getJNDIName() {
		
		return this.jndiName;
	}


	public void setJNDIName(String name) {
		this.jndiName = name;
		
	}

	@Override
	public void sleeInitialization() {
		this.service = super.sleeContainer.getSleeConnectionService();
		//bind
		try{
			Context ctx = (Context) new InitialContext().lookup("java:");
			Util.bind(ctx, this.getJNDIName(), this);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sleeShutdown() {
		//unbind
		try {
			Context ctx;
			ctx = (Context) new InitialContext().lookup("java:");
			Util.unbind(ctx, this.jndiName);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
}
