package org.mobicents.slee.container.management.jmx;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ha.framework.interfaces.FirstAvailable;
import org.jboss.ha.framework.interfaces.HAPartition;
import org.jboss.ha.framework.server.HARMIServerImpl;
import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.jboss.util.naming.Util;
import org.mobicents.slee.connector.server.RemoteSleeService;
import org.mobicents.slee.connector.server.RemoteSleeServiceImpl;

public class HaRmiServerInterfaceMBeanImpl extends ServiceMBeanSupport
		implements HaRmiServerInterfaceMBeanImplMBean {

	private final static Logger logger = Logger
			.getLogger(HaRmiServerInterfaceMBeanImpl.class);

	private HARMIServerImpl rmiServer;

	protected void startService() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("HaRmiServerInterfaceMBeanImpl started......");
		}
	}

	protected void stopService() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("HaRmiServerInterfaceMBeanImpl stopped......");
		}
	}

	public void startRMIServer() {
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

	public void stopRMIServer() {
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
