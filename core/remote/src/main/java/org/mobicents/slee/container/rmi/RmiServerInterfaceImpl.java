package org.mobicents.slee.container.rmi;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.jboss.util.naming.Util;
import org.mobicents.slee.connector.remote.RemoteSleeConnectionService;
import org.mobicents.slee.connector.remote.RemoteSleeConnectionServiceImpl;
import org.mobicents.slee.container.AbstractSleeContainerModule;

public class RmiServerInterfaceImpl extends AbstractSleeContainerModule implements
		RmiServerInterface {

	private final static Logger logger = Logger
			.getLogger(RmiServerInterfaceImpl.class);

	private RMIServerImpl rmiServer;
	
	private String jndiName;

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeStarting()
	 */
	@Override
	public void sleeStarting() {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Starting Slee Service RMI Server");
			}

			InitialContext ctx = new InitialContext();

			RemoteSleeConnectionService stub = new RemoteSleeConnectionServiceImpl(super.sleeContainer.getSleeConnectionService());
			rmiServer =  new RMIServerImpl(this.jndiName,RemoteSleeConnectionService.class,stub);
			
			stub = (RemoteSleeConnectionService) rmiServer.createStub();
			
			ctx.rebind(this.jndiName, stub);
					 

			if (logger.isDebugEnabled()) {
				logger.debug("Bound SleeService rmi stub in jndi");
			}
		} catch (Exception e) {
			logger.error(
					"Failed to start RMI server for Remote slee service", e);
		}

	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeShutdown()
	 */
	@Override
	public void sleeShutdown() {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Stopping RMI Server for slee service");
			}
			InitialContext ctx = new InitialContext();
			Util.unbind(ctx, this.jndiName);
			rmiServer.destroy();
		} catch (NamingException e) {
			logger.error(
					"Failed to stop RMI Server for remote slee service", e);
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
