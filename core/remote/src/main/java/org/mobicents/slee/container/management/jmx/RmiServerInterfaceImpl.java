package org.mobicents.slee.container.management.jmx;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.jboss.util.naming.Util;
import org.mobicents.slee.connector.server.RemoteSleeService;
import org.mobicents.slee.connector.server.RemoteSleeServiceImpl;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.rmi.RMIServerImpl;
import org.mobicents.slee.container.rmi.RmiServerInterface;

public class RmiServerInterfaceImpl extends AbstractSleeContainerModule implements
		RmiServerInterface {

	private final static Logger logger = Logger
			.getLogger(RmiServerInterfaceImpl.class);

	private RMIServerImpl rmiServer;
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeStarting()
	 */
	@Override
	public void sleeStarting() {
		RemoteSleeService stub = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Starting Slee Service RMI Server");
			}

			InitialContext ctx = new InitialContext();

			rmiServer = new RMIServerImpl("RemoteSleeService",
					RemoteSleeService.class, new RemoteSleeServiceImpl());

			stub = (RemoteSleeService) rmiServer.createStub();

			ctx.rebind("/SleeService", stub);

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
			Util.unbind(ctx, "/SleeService");
			rmiServer.destroy();
		} catch (NamingException e) {
			logger.error(
					"Failed to stop RMI Server for remote slee service", e);
		}
	}

}
