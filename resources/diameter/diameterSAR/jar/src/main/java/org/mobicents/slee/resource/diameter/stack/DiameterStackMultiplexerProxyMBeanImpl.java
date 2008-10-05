package org.mobicents.slee.resource.diameter.stack;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
//import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
//import javax.management.ObjectName;

import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationAlreadyUseException;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Configuration;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Network;
import org.jdiameter.api.NetworkReqListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;
import org.mobicents.slee.resource.diameter.stack.dictionary.AvpDictionary;
//import org.mobicents.slee.container.SleeContainer;

public class DiameterStackMultiplexerProxyMBeanImpl extends ServiceMBeanSupport implements DiameterStackMultiplexerProxyMBeanImplMBean, NetworkReqListener,
		EventListener<Request, Answer> {

	protected Stack stack = null;
	protected Logger logger = super.log;
	protected HashMap<RADiameterListener, RADiameterListenerDataPlaceHolder> registeredListeners = new HashMap<RADiameterListener, RADiameterListenerDataPlaceHolder>(3);
	protected HashMap<Long, RADiameterListenerDataPlaceHolder> commandCode2HolderMapping = new HashMap<Long, RADiameterListenerDataPlaceHolder>(3);
	// For now it is ignored....
	protected HashSet<ApplicationId> registeredAppIds = new HashSet<ApplicationId>();

	// ARGHHHHHHH = Synchronization, or should we stop stack??
	protected ReentrantLock lock = new ReentrantLock();

	public void deregisterRa(RADiameterListener raListener) {

		try {
			lock.lock();
			RADiameterListenerDataPlaceHolder holder = this.registeredListeners.remove(raListener);
			if (raListener == null) {
				return;
			}
			for (long commandCode : holder.getCommandCodes()) {
				this.commandCode2HolderMapping.remove(new Long(commandCode));
			}

			Network network = stack.unwrap(Network.class);
			for (ApplicationId appId : holder.getApplicationIds()) {
				try {
					this.registeredAppIds.remove(appId);
					network.removeNetworkReqListener(appId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (InternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void registerRa(RADiameterListener raListener, Object _appIds, Object _commandCodes) throws IllegalStateException {

		int endIndex = 0;
		ApplicationId[] appIds=(ApplicationId[]) _appIds;
		long[] commandCodes=(long[]) _commandCodes;
		try {
			lock.lock();
			
			RADiameterListenerDataPlaceHolder holder = new RADiameterListenerDataPlaceHolder(appIds, commandCodes, raListener);
			if (!validateCommandCodes(holder))
				throw new IllegalStateException("Command code already has been assigned to other RA Listener!");

			Network network = stack.unwrap(Network.class);

			logger.info("Diameter Proxy - Registering  " + appIds.length + " applications.");

			for (; endIndex < appIds.length; endIndex++) {
				ApplicationId appId = appIds[endIndex];
				logger.info("Diameter Base RA :: Adding Listener for [" + appId + "].");
				network.addNetworkReqListener(this, appId);
				this.registeredAppIds.add(appId);
			}
			for (long commandCode : commandCodes) {
				this.commandCode2HolderMapping.put(new Long(commandCode), holder);
			}

			this.registeredListeners.put(raListener, holder);
		} catch (InternalException e) {

			e.printStackTrace();
		} catch (ApplicationAlreadyUseException e) {

			e.printStackTrace();
			try {
				Network network = stack.unwrap(Network.class);
				for (; endIndex >= 0; endIndex--) {
					this.registeredAppIds.add(appIds[endIndex]);
					network.removeNetworkReqListener(appIds[endIndex]);

				}
				for (long commandCode : commandCodes) {
					this.commandCode2HolderMapping.remove(new Long(commandCode));
				}
			} catch (Exception e1) {

				e1.printStackTrace();
			}

		} finally {
			lock.unlock();
		}
	}

	public Stack getStack() {

		return new StackProxy(this.stack);
	}

	//public void create() throws Exception {

	//	super.create();
		//initStack();
	//}

	//public void destroy() {

	//	super.destroy();
		//this.stack.destroy();
		//this.stack = null;
	//}

	//public void start() throws Exception {

	//	super.start();

	//}

	public void stop() {

		super.stop();
		//try {
		//	stack.stop(5, TimeUnit.SECONDS);
		//} catch (Exception e) {
		//	logger.error("Diameter Base RA :: Failure while stopping ");
		//}
	}
	
	
	

	@Override
	protected void createService() throws Exception {
		
		super.createService();
		this.initStack();

	}

	@Override
	protected void destroyService() throws Exception {
		super.destroyService();
		this.stack.destroy();
		this.stack = null;
	}

	@Override
	protected void startService() throws Exception {
	
		super.startService();
		this.stack.start();
		
	}

	@Override
	protected void stopService() throws Exception {
		
		super.stopService();
		stack.stop(5, TimeUnit.SECONDS);
	}

	public Answer processRequest(Request request) {

		long commandCode = request.getCommandCode();
		RADiameterListenerDataPlaceHolder holder = commandCode2HolderMapping.get(new Long(commandCode));
		if (holder == null)
			return null;

		return holder.getListener().processRequest(request);
	}

	public void receivedSuccessMessage(Request request, Answer answer) {// TODO
																		// Auto
		long commandCode = request.getCommandCode();
		RADiameterListenerDataPlaceHolder holder = commandCode2HolderMapping.get(new Long(commandCode));
		if (holder == null)
			return;

		holder.getListener().receivedSuccessMessage(request, answer);

	}

	public void timeoutExpired(Request request) {
		long commandCode = request.getCommandCode();
		RADiameterListenerDataPlaceHolder holder = commandCode2HolderMapping.get(new Long(commandCode));
		if (holder == null)
			return;

		holder.getListener().timeoutExpired(request);

	}

	private void initStack() throws Exception {
		InputStream is = null;

		try {
			// Create and configure stack
			this.stack = new StackImpl();

			// Get configuration
			String configFile = "jdiameter-config.xml";
			is = this.getClass().getResourceAsStream(configFile);

			// Load the configuration
			Configuration config = new XMLConfiguration(is);

			this.stack.init(config);

			Network network = stack.unwrap(Network.class);

			Set<ApplicationId> appIds = stack.getMetaData().getLocalPeer().getCommonApplications();

			logger.info("Diameter Base RA :: Supporting " + appIds.size() + " applications.");

			for (ApplicationId appId : appIds) {
				logger.info("Diameter Base RA :: Adding Listener for [" + appId + "].");
				network.addNetworkReqListener(this, appId);
				this.registeredAppIds.add(appId);
			}

			
			
			 try
			    {
			      logger.info( "Parsing AVP Dictionary file..." );
			      AvpDictionary.INSTANCE.parseDictionary( AvpDictionary.class.getResourceAsStream("dictionary.xml") );
			      logger.info( "AVP Dictionary file successfuly parsed!" );
			    }
			    catch ( Exception e )
			    {
			      logger.error( "Error while parsing dictionary file.", e );
			    }
			
		} finally {
			if (is != null)
				is.close();

			is = null;
		}

		logger.info("Diameter Base RA :: Successfully initialized stack.");
	}

	private boolean validateCommandCodes(RADiameterListenerDataPlaceHolder holder) {

		for (long localCommand : holder.getCommandCodes()) {
			// for (RADiameterListenerDataPlaceHolder h :
			// this.registeredListeners.values()) {
			// if (h.containsCommandCode(localCommand))
			// return false;
			// }
			if (registeredListeners.containsKey(new Long(localCommand)))
				return false;
		}

		return true;
	}

	private class RADiameterListenerDataPlaceHolder {
		private RADiameterListener listener = null;
		private ApplicationId[] applicationIds = null;
		private long[] commandCodes = null;

		public RADiameterListener getListener() {
			return listener;
		}

		public ApplicationId[] getApplicationIds() {
			return applicationIds;
		}

		public long[] getCommandCodes() {
			return commandCodes;
		}

		public void setListener(RADiameterListener listener) {
			this.listener = listener;
		}

		public void setApplicationIds(ApplicationId[] applicationIds) {
			this.applicationIds = applicationIds;
		}

		public void setCommandCodes(long[] commandCodes) {
			this.commandCodes = commandCodes;
		}

		public boolean containsCommandCode(long code) {

			for (long _code : commandCodes)
				if (code == _code) {
					return true;
				}
			return false;
		}

		public RADiameterListenerDataPlaceHolder(ApplicationId[] applicationIds, long[] commandCodes, RADiameterListener listener) {
			super();
			this.applicationIds = applicationIds;
			this.commandCodes = commandCodes;
			this.listener = listener;
		}

	}

	// ----------- SERVICE METHODS:

	public void startService(String name) throws MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {

		//MBeanServer mbs = SleeContainer.lookupFromJndi().getMBeanServer();
		//ObjectName on = null;
		//TMP: this is not invoked when registering directly, not via jboss-service.xml
		//try {
			//initStack();
		//	on = new ObjectName(MBEAN_NAME_PREFIX + name);

		//	mbs.registerMBean(this, on);
		//} catch (Exception e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
		
		logger.info("RA Configuration exposed");

	}

	public static void stopService(String name) {

		//MBeanServer mbs = SleeContainer.lookupFromJndi().getMBeanServer();
		//ObjectName on;
		//try {
		//	on = new ObjectName(MBEAN_NAME_PREFIX + name);
		//	mbs.unregisterMBean(on);
		//} catch (Exception e) {
		//	e.printStackTrace();
		//}

	}

	public Object getMultiplexerMBean() {
	
		return this;
	}

}
