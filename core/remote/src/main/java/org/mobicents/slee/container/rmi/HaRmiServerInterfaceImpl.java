package org.mobicents.slee.container.rmi;

import javax.management.MalformedObjectNameException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.jboss.ha.framework.interfaces.FirstAvailable;
import org.jboss.ha.framework.interfaces.HAPartition;
import org.jboss.ha.framework.server.HARMIServerImpl;
import org.jboss.util.naming.Util;
import org.mobicents.slee.connector.remote.RemoteSleeConnectionService;
import org.mobicents.slee.connector.remote.RemoteSleeConnectionServiceImpl;
import org.mobicents.slee.container.AbstractSleeContainerModule;

public class HaRmiServerInterfaceImpl extends AbstractSleeContainerModule implements
RmiServerInterface {

	private final static Logger logger = Logger
			.getLogger(HaRmiServerInterfaceImpl.class);

	private HARMIServerImpl rmiServer;

	private String jndiName;
	
	/**
	 * @throws NullPointerException 
	 * @throws MalformedObjectNameException 
	 * 
	 */
	public HaRmiServerInterfaceImpl() throws MalformedObjectNameException, NullPointerException {
		
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeStarting()
	 */
	@Override
	public void sleeStarting() {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Starting Slee Service HARMI Server");
			}

			InitialContext ctx = new InitialContext();

			// TODO This shouldn't be hardcoded - use a config. file to
			// configure the partition name, jndi name, port number and
			// inetaddress
			HAPartition myPartition = (HAPartition) ctx
					.lookup("/HAPartition/DefaultPartition");

			RemoteSleeConnectionService stub = new RemoteSleeConnectionServiceImpl(super.sleeContainer.getSleeConnectionService());
			rmiServer = new HARMIServerImpl(myPartition, this.jndiName,RemoteSleeConnectionService.class, stub);
			stub = (RemoteSleeConnectionService) rmiServer
			 					.createHAStub(new FirstAvailable());
			 
			ctx.rebind(this.jndiName, stub);


			if (logger.isDebugEnabled()) {
				logger.debug("Bound SleeService rmi stub in jndi");
			}
		} catch (Exception e) {
			logger.error("Failed to start HA RMI server for Remote slee service",
					e);
		}

	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeShutdown()
	 */
	@Override
	public void sleeShutdown() {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Stopping HA RMI Server for slee service");
			}
			InitialContext ctx = new InitialContext();
			Util.unbind(ctx, this.jndiName);
			rmiServer.destroy();
		} catch (NamingException e) {
			logger
					.error("Failed to stop HA RMI Server for remote slee service",
							e);
		}
	}
	
	/* (non-Javadoc)
	* @see org.mobicents.slee.container.rmi.RmiServerInterface#getJNDIName()
	*/
	public String getJndiName() {
		return this.jndiName;
	}
	
	/* (non-Javadoc)
	* @see org.mobicents.slee.container.rmi.RmiServerInterface#setJNDIName(java.lang.String)
	*/
	public void setJndiName(String name) {
		this.jndiName = name;
	}

}
