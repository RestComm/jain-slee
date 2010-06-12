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


	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.AbstractSleeContainerModule#sleeStarting()
	 */
	@Override
	public void sleeStarting() {
		
		super.sleeStarting();
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
		super.sleeShutdown();
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
