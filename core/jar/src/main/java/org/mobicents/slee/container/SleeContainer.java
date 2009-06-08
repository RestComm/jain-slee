/*
 * Mobicents: The Open Source SLEE Platform      
 *
 * Copyright 2003-2005, CocoonHive, LLC., 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU General Public License (GPL) as
 * published by the Free Software Foundation; 
 * either version 2 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */

package org.mobicents.slee.container;

import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeState;
import javax.slee.profile.ProfileFacility;

import org.apache.log4j.Logger;
import org.jboss.mx.util.MBeanProxy;
import org.jboss.util.naming.Util;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VFSUtils;
import org.mobicents.slee.container.component.ComponentRepositoryImpl;
import org.mobicents.slee.container.component.management.DeployableUnitManagement;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.SbbManagement;
import org.mobicents.slee.container.management.ServiceManagement;
import org.mobicents.slee.container.management.SleeProfileTableManager;
import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.container.management.jmx.editors.SleePropertyEditorRegistrator;
import org.mobicents.slee.container.management.xml.DefaultSleeEntityResolver;
import org.mobicents.slee.container.profile.ProfileObjectPoolManagement;
import org.mobicents.slee.container.rmi.RmiServerInterfaceMBean;
import org.mobicents.slee.container.service.ServiceActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.container.service.ServiceActivityFactoryImpl;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.cache.MobicentsCache;
import org.mobicents.slee.runtime.eventrouter.EventRouter;
import org.mobicents.slee.runtime.eventrouter.EventRouterImpl;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.facilities.TraceFacilityImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileFacilityImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.sbb.SbbObjectPoolManagement;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.util.JndiRegistrationManager;

/**
 * Implements the SleeContainer. The SleeContainer is the anchor for the Slee.
 * It is the central location from where all other major data structures are
 * accessible.
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author Emil Ivov
 * @author Tim Fox
 * @author eduardomartins
 */
public class SleeContainer {

	// STATIC

	private final static Logger logger = Logger.getLogger(SleeContainer.class);

	// static init code
	static {
		// establish the location of mobicents.sar
		try {
			java.net.URL url = VFSUtils.getCompatibleURL(VFS.getRoot(SleeContainer.class.getClassLoader()
					.getResource("META-INF/..")));;
			java.net.URI uri = new java.net.URI(url.toExternalForm()
					.replaceAll(" ", "%20"));
			deployPath = new File(uri).getAbsolutePath();
		} catch (Exception e) {
			logger
					.error(
							"Failed to establish path to Mobicents root deployment directory (mobicents.sar)",
							e);
			deployPath = null;
		}
		// Config JUL logger to use Log4J filter
		Handler[] handlers = java.util.logging.Logger.getLogger("")
				.getHandlers();
		for (Handler handler : handlers)
			if (handler instanceof ConsoleHandler)
				handler.setFilter(new MobicentsLogFilter());
	}

	private static String deployPath;

	/**
	 * 
	 * @return the full file system path where mobicents.sar is located
	 */
	public static String getDeployPath() {
		return deployPath;
	}



	private static SleeContainer sleeContainer;
	private final SleeTransactionManager sleeTransactionManager;
	private final MobicentsCache cache;

	// FIELDS
	// mbean server where the container's mbeans are registred
	private MBeanServer mbeanServer;
	/** The lifecycle state of the SLEE */
	private SleeState sleeState;
	// the class that actually posts events to the SBBs.
	// This should be made into a facility and registered with jmx and jndi
	// so it can be independently controlled.
	private EventRouter router;
	// monitor object for sync on management operations
	private final Object managementMonitor = new Object();
	// for external access to slee
	private RmiServerInterfaceMBean rmiServerInterfaceMBeanImpl;
	// component managers
	private ComponentRepositoryImpl componentRepositoryImpl;
	private ServiceManagement serviceManagement;
	
	//private SleeProfileManager sleeProfileManager;
	private SleeProfileTableManager sleeProfileTableManager;
	private ResourceManagement resourceManagement;
	private SbbManagement sbbManagement;
	// object pool management
	private SbbObjectPoolManagement sbbPoolManagement;
	private ProfileObjectPoolManagement profileObjectPoolManagement;
	
	/**
	 * where DUs are stored
	 */
	private final DeployableUnitManagement deployableUnitManagement = new DeployableUnitManagement();
	
	// slee factories
	private ActivityContextFactoryImpl activityContextFactory;
	private NullActivityContextInterfaceFactoryImpl nullActivityContextInterfaceFactory;
	private NullActivityFactoryImpl nullActivityFactory;
	private ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory;
	private ServiceActivityContextInterfaceFactoryImpl serviceActivityContextInterfaceFactory;
	private ServiceActivityFactoryImpl serviceActivityFactory;
	// slee facilities
	private ActivityContextNamingFacilityImpl activityContextNamingFacility;
	private AlarmMBeanImpl alarmFacility;
	private ProfileFacilityImpl profileFacility;
	private TimerFacilityImpl timerFacility;
	private TraceFacilityImpl traceFacility;
	
	private final MobicentsUUIDGenerator uuidGenerator = MobicentsUUIDGenerator.getInstance(); 
	
	private SleeContainerTimer timer;
	
	// LIFECYLE RELATED

	/**
	 * Creates a new instance of SleeContainer -- This is called from the
	 * SleeManagementMBean to get the whole thing running.
	 * 
	 */
	public SleeContainer(MBeanServer mbserver, SleeTransactionManager sleeTransactionManager, MobicentsCache cache) throws Exception {
		// created in STOPPED state and remain so until started
		this.sleeState = SleeState.STOPPED;
		this.mbeanServer = mbserver;
		this.sleeTransactionManager = sleeTransactionManager;
		this.cache = cache;
		// Force this property to allow invocation of getters.
		// http://code.google.com/p/mobicents/issues/detail?id=63
		System.setProperty("jmx.invoke.getters", "true");
	}

	/**
	 * Initialization code.
	 * 
	 */
	public void init(SleeManagementMBean sleeManagementMBean,
			ObjectName rmiServerInterfaceMBean) throws Exception {

		logger.info("Initializing SLEE container...");

		// init jndi
		Context ctx = new InitialContext();
		ctx = Util.createSubcontext(ctx, JVM_ENV + CTX_SLEE);
		Util.createSubcontext(ctx, "resources");
		Util.createSubcontext(ctx, "container");
		Util.createSubcontext(ctx, "facilities");
		Util.createSubcontext(ctx, "sbbs");
		ctx = Util.createSubcontext(ctx, "nullactivity");
		Util.createSubcontext(ctx, "factory");
		Util.createSubcontext(ctx, "nullactivitycontextinterfacefactory");

		// Force class loading of ConcreteClassGeneratorUtils to initilize the
		// static pool
		ConcreteClassGeneratorUtils.class.getClass();
		DefaultSleeEntityResolver.init(this.getClass().getClassLoader());

		this.timer = new SleeContainerTimer();		
		this.componentRepositoryImpl = new ComponentRepositoryImpl();
		this.serviceManagement = new ServiceManagement(this);
		//this.sleeProfileManager = new SleeProfileManager(this);
		this.sleeProfileTableManager = new SleeProfileTableManager(this);
		this.sbbManagement = new SbbManagement(this);
		this.resourceManagement = new ResourceManagement(this);
		this.activityContextFactory = new ActivityContextFactoryImpl(this);
		this.router = new EventRouterImpl(this,MobicentsManagement.eventRouterExecutors,MobicentsManagement.monitoringUncommittedAcAttachs);
		this.activityContextNamingFacility = new ActivityContextNamingFacilityImpl(this);
		JndiRegistrationManager.registerWithJndi("slee/facilities", "activitycontextnaming",
				activityContextNamingFacility);
		this.nullActivityFactory = new NullActivityFactoryImpl(this);
		this.nullActivityContextInterfaceFactory = new NullActivityContextInterfaceFactoryImpl(
				this);
		JndiRegistrationManager.registerWithJndi("slee/nullactivity",
				"nullactivitycontextinterfacefactory",
				nullActivityContextInterfaceFactory);
		
		JndiRegistrationManager.registerWithJndi("slee/nullactivity", "nullactivityfactory",
				nullActivityFactory);
		this.profileTableActivityContextInterfaceFactory = new ProfileTableActivityContextInterfaceFactoryImpl();
		JndiRegistrationManager.registerWithJndi("slee/facilities",
				ProfileTableActivityContextInterfaceFactoryImpl.JNDI_NAME,
				profileTableActivityContextInterfaceFactory);
		this.timerFacility = new TimerFacilityImpl(this);
		JndiRegistrationManager.registerWithJndi("slee/facilities", TimerFacilityImpl.JNDI_NAME,
				timerFacility);
		this.profileFacility = new ProfileFacilityImpl(this);
		JndiRegistrationManager.registerWithJndi("slee/facilities", ProfileFacilityImpl.JNDI_NAME,
				profileFacility);
		this.serviceActivityFactory = new ServiceActivityFactoryImpl();
		JndiRegistrationManager.registerWithJndi("slee/serviceactivity/",
				ServiceActivityFactoryImpl.JNDI_NAME, serviceActivityFactory);
		this.serviceActivityContextInterfaceFactory = new ServiceActivityContextInterfaceFactoryImpl();
		JndiRegistrationManager.registerWithJndi("slee/serviceactivity/",
				ServiceActivityContextInterfaceFactoryImpl.JNDI_NAME,
				serviceActivityContextInterfaceFactory);

		registerWithJndi();
		
		startRMIServer(rmiServerInterfaceMBean);

		// Register property editors for the composite SLEE types so that the
		// jboss jmx console can pass it as an argument.
		new SleePropertyEditorRegistrator().register();	
		
		this.sbbPoolManagement = new SbbObjectPoolManagement(sleeContainer);
		this.sbbPoolManagement.register();
		
		this.profileObjectPoolManagement = new ProfileObjectPoolManagement(sleeContainer);
		this.profileObjectPoolManagement.register();
	}

	/**
	 * 
	 * Cleanup in the reverse order of init()
	 * 
	 * @throws NamingException
	 * 
	 */
	public void close() throws NamingException {
		unregisterWithJndi();
		Context ctx = new InitialContext();
		Util.unbind(ctx, JVM_ENV + CTX_SLEE);
		stopRMIServer();
		timer.realCancel();
	}

	/**
	 * Get the current state of the Slee Container
	 * 
	 * @return SleeState
	 */
	public SleeState getSleeState() {
		return this.sleeState;
	}

	/**
	 * Set the current state of the Slee Container. CAUTION: Do not invoke this
	 * method directly! Use the SleeManagementMBean to change the Slee State
	 * 
	 * @param SleeState
	 */
	public SleeState setSleeState(SleeState newState) {
		return this.sleeState = newState;
	}

	/**
	 * dumps the container state as a string, useful for debug/profiling
	 * 
	 * @return
	 */
	public String dumpState() {
		return deployableUnitManagement + "\n" 
				+ componentRepositoryImpl + "\n"
				+ resourceManagement + "\n"
				+ sbbPoolManagement + "\n"
				+ timerFacility + "\n"
				+ traceFacility + "\n"
				+ sleeProfileTableManager + "\n"
				+ profileObjectPoolManagement + "\n"
				+ activityContextFactory + "\n"
				+ activityContextNamingFacility	+ "\n"
				+ nullActivityFactory + "\n"
				+ getEventRouter() + "\n"
				+ getTransactionManager();
	}

	// GETTERS -- managers

	/**
	 * object for synchronization on management operations that (un)install
	 * components
	 */
	public Object getManagementMonitor() {
		return managementMonitor;
	}

	/**
	 * retrieves the container's component repository implementation
	 * @return
	 */
	public ComponentRepositoryImpl getComponentRepositoryImpl() {
		return componentRepositoryImpl;
	}
	
	/**
	 * manages (un)install of resource adaptors
	 * 
	 * @return
	 */
	public ResourceManagement getResourceManagement() {
		return resourceManagement;
	}

	/**
	 * manages (un)install of sbbs
	 * 
	 * @return
	 */
	public SbbManagement getSbbManagement() {
		return sbbManagement;
	}

	/**
	 * Retrieves the manager of sbb object pools
	 * @return
	 */
	public SbbObjectPoolManagement getSbbPoolManagement() {
		return sbbPoolManagement;
	}

	/**
	 * Retrieves the profile object pool management
	 * @return
	 */
	public ProfileObjectPoolManagement getProfileObjectPoolManagement() {
		return profileObjectPoolManagement;
	}
	
	/**
	 * manages (un)install of services
	 * 
	 * @return
	 */
	public ServiceManagement getServiceManagement() {
		return this.serviceManagement;
	}

	public SleeProfileTableManager getSleeProfileTableManager() {
		return this.sleeProfileTableManager;
	}
	
	/**
	 * Retrieves the deployable unit manager
	 * @return
	 */
	public DeployableUnitManagement getDeployableUnitManagement() {
		return deployableUnitManagement;
	}
	
	// GETTERS -- slee factories

	public ActivityContextFactoryImpl getActivityContextFactory() {
		return this.activityContextFactory;
	}

	public NullActivityContextInterfaceFactoryImpl getNullActivityContextInterfaceFactory() {
		return this.nullActivityContextInterfaceFactory;
	}

	public NullActivityFactoryImpl getNullActivityFactory() {
		return this.nullActivityFactory;
	}

	public ProfileTableActivityContextInterfaceFactoryImpl getProfileTableActivityContextInterfaceFactory() {
		if (profileTableActivityContextInterfaceFactory == null) {
			try {
				profileTableActivityContextInterfaceFactory = (ProfileTableActivityContextInterfaceFactoryImpl) lookupFacilityInJndi(ProfileTableActivityContextInterfaceFactoryImpl.JNDI_NAME);
			} catch (NamingException e) {
				logger.error("failed to lookup factory", e);
			}
		}
		return profileTableActivityContextInterfaceFactory;
	}

	public ServiceActivityContextInterfaceFactoryImpl getServiceActivityContextFactory() {
		return this.serviceActivityContextInterfaceFactory;
	}

	// GETTERS -- slee facilities

	public javax.slee.facilities.ActivityContextNamingFacility getActivityContextNamingFacility() {
		return activityContextNamingFacility;
	}
	
	/**
	 * Return AlarmMBean impl object, which encapsualtes all AlarmFacitlity object
	 * @return
	 */
	public AlarmMBeanImpl getAlarmFacility() {
		if (alarmFacility == null) {
			// lookup from jndi
			try {
				alarmFacility = (AlarmMBeanImpl) lookupFacilityInJndi(AlarmMBeanImpl.JNDI_NAME);
			} catch (NamingException e) {
				logger.error("failed to lookup alarm facility", e);
			}
		}
		return alarmFacility;
	}

	public ProfileFacility getProfileFacility() {
		return profileFacility;
	}

	public TimerFacilityImpl getTimerFacility() {
		return timerFacility;
	}

	public TraceFacilityImpl getTraceFacility() {
		if (traceFacility == null) {
			// lookup from jndi
			try {
				traceFacility = (TraceFacilityImpl) lookupFacilityInJndi(TraceMBeanImpl.JNDI_NAME);
			} catch (NamingException e) {
				logger.error("failed to lookup trace facility", e);
			}
		}
		return traceFacility;
	}
	
	// GETTERS -- slee runtime
	
	/**
	 * a UUID generator for the container
	 */
	public MobicentsUUIDGenerator getUuidGenerator() {
		return uuidGenerator;
	}
	
	/**
	 * the container's event router
	 */
	public EventRouter getEventRouter() {
		return this.router;
	}

	/**
	 * Get the transaction manager
	 * 
	 * @throws
	 */
	public SleeTransactionManager getTransactionManager() {
		return sleeTransactionManager;		
	}

	/**
	 * The cache which manages the container's HA and FT data 
	 * @return
	 */
	public MobicentsCache getCache() {
		return cache;
	}
	
	/**
	 * Return the MBeanServer that the SLEEE is registers with in the current
	 * JVM
	 */
	public MBeanServer getMBeanServer() {
		return mbeanServer;
	}

	/**
	 * Retrieves the shared timer
	 * @return
	 */
	public SleeContainerTimer getTimer() {
		return timer;
	}
	
	// RMI RELATED

	/*
	 * Start the HA RMI Server, used by JCA resource adaptors to communicate
	 * with the SLEE
	 */
	private void startRMIServer(ObjectName rmiServerInterfaceMBean) {
		try {
			logger.debug("creating RmiServerInterface using MBeanProxy");
			rmiServerInterfaceMBeanImpl = (RmiServerInterfaceMBean) MBeanProxy
					.get(RmiServerInterfaceMBean.class,
							rmiServerInterfaceMBean, mbeanServer);
			rmiServerInterfaceMBeanImpl.startRMIServer();
		} catch (Throwable e) {
			logger.error(
					"Failed to start HA RMI server for Remote slee service", e);
		}
	}

	private void stopRMIServer() {
		logger.debug("Stopping RMI Server for slee service");
		rmiServerInterfaceMBeanImpl.stopRMIServer();
	}
	
	public int getRmiRegistryPort() throws Exception {
		Integer port = (Integer) mbeanServer.getAttribute(new ObjectName(
				"slee:service=SleeTCKWrapper"), "RMIRegistryPort");
		return port.intValue();
	}

	// JNDI RELATED

	// For unit testing only -- to be removed later.
	private static final String JNDI_NAME = "container";

	public static final String JVM_ENV = "java:";

	/** standard ENC name in JNDI */
	public static final String COMP_ENV = "java:comp/env";

	/** the root context for SLEE */
	private static final String CTX_SLEE = "slee";

	/**
	 * Return the SleeContainer instance registered in the JVM scope of JNDI
	 */
	public static SleeContainer lookupFromJndi() {
		try {
			if (sleeContainer != null)
				return sleeContainer;
			else {
				sleeContainer = (SleeContainer) JndiRegistrationManager.getFromJndi("slee/" + JNDI_NAME);
				return sleeContainer;
			}
		} catch (Exception ex) {
			logger.error("Unexpected error: Cannot retrieve SLEE Container!",
					ex);
			return null;
		}
	}

	/**
	 * Convenience method for registering SLEE facilities with JNDI
	 * 
	 * @param facilityName
	 * @param facility
	 */
	public static void registerFacilityWithJndi(String facilityName,
			Object facility) {
		JndiRegistrationManager.registerWithJndi("slee/facilities", facilityName, facility);
	}

	/**
	 * Convenience method for unregistering SLEE facilities with JNDI
	 * 
	 * @param facilityName
	 * @param facility
	 */
	public static void unregisterFacilityWithJndi(String facilityName) {
		JndiRegistrationManager.unregisterWithJndi("slee/facilities/" + facilityName);
	}

	/**
	 * Convenience method for unregistering SLEE facilities with JNDI
	 * 
	 * @param facilityName
	 * @param facility
	 * @throws NamingException
	 */
	private static Object lookupFacilityInJndi(String facilityName)
			throws NamingException {
		return JndiRegistrationManager.getFromJndi("slee/facilities/" + facilityName);
	}

	private void registerWithJndi() {
		JndiRegistrationManager.registerWithJndi("slee", JNDI_NAME, this);
	}

	private void unregisterWithJndi() {
		JndiRegistrationManager.unregisterWithJndi("slee/" + JNDI_NAME);
	}
	
	
}