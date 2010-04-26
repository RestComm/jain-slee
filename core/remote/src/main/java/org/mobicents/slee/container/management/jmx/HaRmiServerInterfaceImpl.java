package org.mobicents.slee.container.management.jmx;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.jboss.ha.framework.interfaces.FirstAvailable;
import org.jboss.ha.framework.interfaces.HAPartition;
import org.jboss.ha.framework.server.HARMIServerImpl;
import org.jboss.util.naming.Util;
import org.mobicents.slee.connector.server.RemoteSleeService;
import org.mobicents.slee.connector.server.RemoteSleeServiceImpl;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.rmi.RmiServerInterface;

public class HaRmiServerInterfaceImpl extends AbstractSleeContainerModule implements
RmiServerInterface {

	private final static Logger logger = Logger
			.getLogger(HaRmiServerInterfaceImpl.class);

	private HARMIServerImpl rmiServer;

	private final ObjectName objectName;
	
	/**
	 * @throws NullPointerException 
	 * @throws MalformedObjectNameException 
	 * 
	 */
	public HaRmiServerInterfaceImpl(String objectNameString) throws MalformedObjectNameException, NullPointerException {
		objectName = new ObjectName(objectNameString);
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.rmi.RmiServerInterface#getObjectName()
	 */
	public ObjectName getObjectName() {
		return objectName;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeStarting()
	 */
	@Override
	public void sleeStarting() {
		RemoteSleeService stub = null;
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

			rmiServer = new HARMIServerImpl(myPartition, "RemoteSleeService",
					RemoteSleeService.class, new RemoteSleeServiceImpl());

			stub = (RemoteSleeService) rmiServer
					.createHAStub(new FirstAvailable());

			ctx.rebind("/SleeService", stub);

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
			Util.unbind(ctx, "/SleeService");
			rmiServer.destroy();
		} catch (NamingException e) {
			logger
					.error("Failed to stop HA RMI Server for remote slee service",
							e);
		}
	}
}
