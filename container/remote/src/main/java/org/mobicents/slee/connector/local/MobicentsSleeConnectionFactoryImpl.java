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

/**
 * 
 */
package org.mobicents.slee.connector.local;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import org.jboss.util.naming.Util;
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;

import javax.resource.ResourceException;
import javax.slee.connection.SleeConnection;

import org.apache.log4j.Logger;
//import org.jboss.as.naming.WritableServiceBasedNamingStore;
//import org.jboss.msc.service.ServiceName;
//import org.jboss.msc.service.ServiceTarget;
import org.mobicents.slee.container.AbstractSleeContainerModule;


/**
 * @author baranowb
 *
 */
public class MobicentsSleeConnectionFactoryImpl
		extends AbstractSleeContainerModule
		implements MobicentsSleeConnectionFactory, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//not this class implements serializable, but its bound only to local JVM, its not accessbile outside!
	private static final String _DEFAULT_NAME="/MobicentsConnectionFactory";
	private static final Logger logger = Logger.getLogger(MobicentsSleeConnectionFactoryImpl.class);

	//private JndiRegistry registry = null;
	private String jndiName = _DEFAULT_NAME;
	private List<SleeConnection> connectionList = new ArrayList<SleeConnection>();
	private transient SleeConnectionService service;
	
	public MobicentsSleeConnectionFactoryImpl() {
		super();
		//registry = new JndiRegistry();
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

        /*
		try {
			registry.setServiceContainer(super.sleeContainer.getServiceController().getServiceContainer());
			registry.bindToJndi("java:"+this.getJNDIName(), this);
		} catch (Throwable t) {
			t.printStackTrace();
		}
        */

		/*
		//ServiceTarget serviceTarget = super.sleeContainer.getServiceController().getServiceContainer();
		ServiceName serviceName = super.sleeContainer.getServiceController().getName();
		if (serviceName != null) {
			logger.info("serviceName: "+serviceName);

			//bind
			try {
				InitialContext ctx = new InitialContext();
				WritableServiceBasedNamingStore.pushOwner(serviceName);
				ctx.bind("java:global" + this.getJNDIName(), this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				WritableServiceBasedNamingStore.popOwner();
			}
		}
		*/
	}

	@Override
	public void sleeShutdown() {

        /*
		try {
			registry.unbindFromJndi("java:"+this.getJNDIName());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		*/

		/*
		//unbind
		try {
			//Context ctx;
			//ctx = (Context) new InitialContext(); //.lookup("jndi:");
			//Util.unbind(ctx, "jndi:"+this.jndiName);

			NamingContext ctx = new InitialContext(null);
			ctx.unbind("jndi:"+this.jndiName);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

}
