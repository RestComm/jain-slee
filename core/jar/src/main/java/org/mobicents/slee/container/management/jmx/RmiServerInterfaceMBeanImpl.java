package org.mobicents.slee.container.management.jmx;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.jboss.util.naming.Util;
import org.mobicents.slee.connector.server.RemoteSleeService;
import org.mobicents.slee.connector.server.RemoteSleeServiceImpl;
import org.mobicents.slee.container.rmi.RMIServerImpl;
import org.mobicents.slee.resource.EventLookup;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;

public class RmiServerInterfaceMBeanImpl extends ServiceMBeanSupport implements
		RmiServerInterfaceMBeanImplMBean {

	private final static Logger logger = Logger
			.getLogger(RmiServerInterfaceMBeanImpl.class);

	private RMIServerImpl rmiServer;

	protected void startService() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("RmiServerInterfaceMBeanImpl started......");
		}
	}

	protected void stopService() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("RmiServerInterfaceMBeanImpl stopped......");
		}
	}
	
//	public ObjectName getRmiServerInterfaceImplMBean(){
//		ObjectName objName = null;
//		try{
//		objName = new ObjectName(OBJECT_NAME);
//		}
//		catch(MalformedObjectNameException e){
//			log.error("Could not create ObjectName for "+OBJECT_NAME, e);
//		}
//		return objName;
//	}

	public void startRMIServer(NullActivityFactoryImpl naf, EventLookup eventLookup,
			ActivityContextFactoryImpl activityContextFactory) {
		RemoteSleeService stub = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Starting Slee Service RMI Server");
			}

			InitialContext ctx = new InitialContext();

			rmiServer = new RMIServerImpl("RemoteSleeService",
					RemoteSleeService.class, new RemoteSleeServiceImpl(naf,
							eventLookup, activityContextFactory));

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

	public void stopRMIServer() {
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
