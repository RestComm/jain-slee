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

import java.beans.PropertyEditorManager;
import java.io.File;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.facilities.Level;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeState;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;

import org.apache.log4j.Logger;
import org.jboss.mx.util.MBeanProxy;
import org.jboss.util.naming.NonSerializableFactory;
import org.jboss.util.naming.Util;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VFSUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.management.ComponentManagement;
import org.mobicents.slee.container.management.DeployableUnitManagement;
import org.mobicents.slee.container.management.EventManagement;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.SbbManagement;
import org.mobicents.slee.container.management.ServiceManagement;
import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;
import org.mobicents.slee.container.management.jmx.ComponentIDArrayPropertyEditor;
import org.mobicents.slee.container.management.jmx.ComponentIDPropertyEditor;
import org.mobicents.slee.container.management.jmx.DeployableUnitIDPropertyEditor;
import org.mobicents.slee.container.management.jmx.LevelPropertyEditor;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;
import org.mobicents.slee.container.management.jmx.ObjectPropertyEditor;
import org.mobicents.slee.container.management.jmx.PropertiesPropertyEditor;
import org.mobicents.slee.container.management.jmx.ServiceStatePropertyEditor;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.container.management.xml.DefaultSleeEntityResolver;
import org.mobicents.slee.container.profile.ProfileSpecificationIDPropertyEditor;
import org.mobicents.slee.container.profile.SleeProfileManager;
import org.mobicents.slee.container.rmi.RmiServerInterfaceMBean;
import org.mobicents.slee.resource.EventLookup;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.cache.XACache;
import org.mobicents.slee.runtime.eventrouter.EventRouter;
import org.mobicents.slee.runtime.eventrouter.EventRouterImpl;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.facilities.AlarmFacilityImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.facilities.TraceFacilityImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileFacilityImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityFactoryImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

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

	public static boolean isSecurityEnabled = false;

	public static boolean isSecurityEnabled() {
		return isSecurityEnabled;
	}

	private static SleeContainer sleeContainer;
	private static SleeTransactionManager sleeTransactionManager;

	// FIELDS
	// mbean server where the container's mbeans are registred
	private MBeanServer mbeanServer;
	/** The lifecycle state of the SLEE */
	private SleeState sleeState;
	// An interface for the resource adaptor to retrieve the event type id.
	private EventLookup eventLookup;
	// the class that actually posts events to the SBBs.
	// This should be made into a facility and registered with jmx and jndi
	// so it can be independently controlled.
	private EventRouter router;
	// monitor object for sync on management operations
	private final Object managementMonitor = new Object();
	// for external access to slee
	private RmiServerInterfaceMBean rmiServerInterfaceMBeanImpl;
	// component managers
	private ServiceManagement serviceManagement;
	private ComponentManagement componentManagement;
	private DeployableUnitManagement deployableUnitManagement;
	private EventManagement eventManagement;
	private SleeProfileManager sleeProfileManager;
	private ResourceManagement resourceManagement;
	private SbbManagement sbbManagement;
	// slee factories
	private ActivityContextFactoryImpl activityContextFactory;
	private NullActivityContextInterfaceFactoryImpl nullActivityContextInterfaceFactory;
	private NullActivityFactoryImpl nullActivityFactory;
	private ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory;
	private ServiceActivityContextInterfaceFactoryImpl serviceActivityContextInterfaceFactory;
	private ServiceActivityFactoryImpl serviceActivityFactory;
	// slee facilities
	private ActivityContextNamingFacilityImpl activityContextNamingFacility;
	private AlarmFacilityImpl alarmFacility;
	private ProfileFacilityImpl profileFacility;
	private TimerFacilityImpl timerFacility;
	private TraceFacilityImpl traceFacility;
	
	private final MobicentsUUIDGenerator uuidGenerator = MobicentsUUIDGenerator.getInstance(); 
	
	// LIFECYLE RELATED

	/**
	 * Creates a new instance of SleeContainer -- This is called from the
	 * SleeManagementMBean to get the whole thing running.
	 * 
	 */
	public SleeContainer(MBeanServer mbserver) throws Exception {
		// created in STOPPED state and remain so until started
		this.sleeState = SleeState.STOPPED;
		this.mbeanServer = mbserver;
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

		this.componentManagement = new ComponentManagement(this);
		this.deployableUnitManagement = new DeployableUnitManagement(this);
		this.serviceManagement = new ServiceManagement(this);
		this.eventManagement = new EventManagement(this);
		this.eventLookup = new EventLookup(this);
		this.sleeProfileManager = new SleeProfileManager(this);
		this.sbbManagement = new SbbManagement(this);
		this.resourceManagement = new ResourceManagement(this);
		this.activityContextFactory = new ActivityContextFactoryImpl(this);
		this.router = new EventRouterImpl(this,MobicentsManagement.eventRouterExecutors,MobicentsManagement.monitoringUncommittedAcAttachs);
		this.activityContextNamingFacility = new ActivityContextNamingFacilityImpl();
		registerWithJndi("slee/facilities", "activitycontextnaming",
				activityContextNamingFacility);
		this.nullActivityFactory = new NullActivityFactoryImpl(this);
		this.nullActivityContextInterfaceFactory = new NullActivityContextInterfaceFactoryImpl(
				this);
		registerWithJndi("slee/nullactivity",
				"nullactivitycontextinterfacefactory",
				nullActivityContextInterfaceFactory);
		
		registerWithJndi("slee/nullactivity", "nullactivityfactory",
				nullActivityFactory);
		this.profileTableActivityContextInterfaceFactory = new ProfileTableActivityContextInterfaceFactoryImpl();
		registerWithJndi("slee/facilities",
				ProfileTableActivityContextInterfaceFactoryImpl.JNDI_NAME,
				profileTableActivityContextInterfaceFactory);
		this.timerFacility = new TimerFacilityImpl(this);
		registerWithJndi("slee/facilities", TimerFacilityImpl.JNDI_NAME,
				timerFacility);
		this.profileFacility = new ProfileFacilityImpl();
		registerWithJndi("slee/facilities", ProfileFacilityImpl.JNDI_NAME,
				profileFacility);
		this.serviceActivityFactory = new ServiceActivityFactoryImpl();
		registerWithJndi("slee/serviceactivity/",
				ServiceActivityFactoryImpl.JNDI_NAME, serviceActivityFactory);
		this.serviceActivityContextInterfaceFactory = new ServiceActivityContextInterfaceFactoryImpl();
		registerWithJndi("slee/serviceactivity/",
				ServiceActivityContextInterfaceFactoryImpl.JNDI_NAME,
				serviceActivityContextInterfaceFactory);

		registerWithJndi();
		
		startRMIServer(this.nullActivityFactory,this.eventLookup, rmiServerInterfaceMBean);

		// Register property editors for the composite SLEE types so that the
		// jboss
		// jmx console can pass it as an argument.
		PropertyEditorManager.registerEditor(ComponentID.class,
				ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(Level.class,
				LevelPropertyEditor.class);
		PropertyEditorManager.registerEditor(Properties.class,
				PropertiesPropertyEditor.class);
		PropertyEditorManager.registerEditor(ProfileSpecificationID.class,
				ProfileSpecificationIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(ComponentID[].class,
				ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(SbbID[].class,
				ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(DeployableUnitID.class,
				DeployableUnitIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(Object.class,
				ObjectPropertyEditor.class);
		PropertyEditorManager.registerEditor(ServiceID.class,
				ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(Object.class,
				ServiceStatePropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorID.class,
				ComponentIDPropertyEditor.class);	
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
		return eventManagement.toString() + "\n" + resourceManagement + "\n"
				+ sbbManagement + "\n" + timerFacility + "\n"
				+ deployableUnitManagement + "\n" + componentManagement + "\n"
				+ sleeProfileManager + "\n"
				+ activityContextFactory + "\n" + activityContextNamingFacility
				+ "\n" + nullActivityFactory + "\n" + serviceManagement + "\n"
				+ profileFacility + "\n" + getEventRouter() + "\n" + XACache.dumpState();
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
	 * manages (un)install of components in general and takes care of
	 * dependencies
	 * 
	 * @return
	 */
	public ComponentManagement getComponentManagement() {
		return componentManagement;
	}

	/**
	 * manages (un)install of deployable units
	 * 
	 * @return
	 */
	public DeployableUnitManagement getDeployableUnitManagement() {
		return deployableUnitManagement;
	}

	/**
	 * manages (un)install of events
	 * 
	 * @return
	 */
	public EventManagement getEventManagement() {
		return eventManagement;
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
	 * manages (un)install of services
	 * 
	 * @return
	 */
	public ServiceManagement getServiceManagement() {
		return this.serviceManagement;
	}

	/**
	 * manages profile and profile tables
	 * (including activities)
	 * @return
	 */
	public SleeProfileManager getSleeProfileManager() {
		return sleeProfileManager;
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

	public AlarmFacilityImpl getAlarmFacility() {
		if (alarmFacility == null) {
			// lookup from jndi
			try {
				alarmFacility = (AlarmFacilityImpl) lookupFacilityInJndi(AlarmMBeanImpl.JNDI_NAME);
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

	public EventLookup getEventLookupFacility() {
		return eventLookup;
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
	 * Get the transaction manager from class or JNDI
	 * 
	 * @throws
	 */
	public static SleeTransactionManager getTransactionManager() {
		try {
			if (sleeTransactionManager == null)
				sleeTransactionManager = (SleeTransactionManager) getFromJndi("slee/"
						+ SleeTransactionManager.JNDI_NAME);
			return sleeTransactionManager;
		} catch (Exception ex) {
			logger.error("Error fetching transaciton manager!", ex);
			return null;
		}
	}

	/**
	 * Return the MBeanServer that the SLEEE is registers with in the current
	 * JVM
	 */
	public MBeanServer getMBeanServer() {
		return mbeanServer;
	}

	// RMI RELATED

	/*
	 * Start the HA RMI Server, used by JCA resource adaptors to communicate
	 * with the SLEE
	 */
	private void startRMIServer(NullActivityFactoryImpl naf,
			EventLookup eventLookup,
			ObjectName rmiServerInterfaceMBean) {
		try {
			logger.debug("creating RmiServerInterface using MBeanProxy");
			rmiServerInterfaceMBeanImpl = (RmiServerInterfaceMBean) MBeanProxy
					.get(RmiServerInterfaceMBean.class,
							rmiServerInterfaceMBean, mbeanServer);
			rmiServerInterfaceMBeanImpl.startRMIServer(naf,eventLookup, activityContextFactory);
		} catch (Exception e) {
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
				sleeContainer = (SleeContainer) getFromJndi("slee/" + JNDI_NAME);
				return sleeContainer;
			}
		} catch (Exception ex) {
			logger.error("Unexpected error: Cannot retrieve SLEE Container!",
					ex);
			return null;
		}
	}

	/**
	 * Unregister an internal slee component with jndi.
	 * 
	 * @param Name -
	 *            the full path to the resource except the "java:" prefix.
	 */
	public static void unregisterWithJndi(String name) {
		Context ctx;
		String path = JVM_ENV + name;
		try {
			ctx = new InitialContext();
			Util.unbind(ctx, path);
		} catch (NamingException ex) {
			logger.warn("unregisterWithJndi failed for " + path);
		}
	}

	/**
	 * Register a internal slee component with jndi.
	 */
	public static void registerWithJndi(String prefix, String name,
			Object object) {
		String fullName = JVM_ENV + prefix + "/" + name;
		try {
			Context ctx = new InitialContext();
			try {
				Util.createSubcontext(ctx, fullName);
			} catch (NamingException e) {
				logger.warn("Context, " + fullName + " might have been bound.");
				logger.warn(e);
			}
			ctx = (Context) ctx.lookup(JVM_ENV + prefix);
			// ctx.createSubcontext(name);
			// ctx = (Context) ctx.lookup(name);
			// Util.rebind(JVM_ENV + prefix + "/" + name, object);
			NonSerializableFactory.rebind(fullName, object);
			StringRefAddr addr = new StringRefAddr("nns", fullName);
			Reference ref = new Reference(object.getClass().getName(), addr,
					NonSerializableFactory.class.getName(), null);
			Util.rebind(ctx, name, ref);
			logger.debug("registered with jndi " + fullName);
		} catch (Exception ex) {
			logger.warn("registerWithJndi failed for " + fullName, ex);
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
		registerWithJndi("slee/facilities", facilityName, facility);
	}

	/**
	 * Convenience method for unregistering SLEE facilities with JNDI
	 * 
	 * @param facilityName
	 * @param facility
	 */
	public static void unregisterFacilityWithJndi(String facilityName) {
		unregisterWithJndi("slee/facilities/" + facilityName);
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
		return getFromJndi("slee/facilities/" + facilityName);
	}

	private void registerWithJndi() {
		registerWithJndi("slee", JNDI_NAME, this);
	}

	private void unregisterWithJndi() {
		unregisterWithJndi("slee/" + JNDI_NAME);
	}
	
	/**
	 * lookup a name reference from jndi.
	 * 
	 * @param resourceName --
	 *            name to lookup.
	 * @throws NamingException
	 */
	private static Object getFromJndi(String resourceName)
			throws NamingException {

		Context initialContext = new InitialContext();
		Context compEnv = (Context) initialContext.lookup(JVM_ENV);
		return compEnv.lookup(resourceName);
	}
}