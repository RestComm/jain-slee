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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
import javax.slee.EventTypeID;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.facilities.Level;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.SbbDescriptor;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeState;
import javax.slee.management.UnrecognizedDeployableUnitException;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileSpecificationDescriptor;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeDescriptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.jboss.mx.loading.UnifiedClassLoader3;
import org.jboss.mx.util.MBeanProxy;
import org.jboss.util.naming.NonSerializableFactory;
import org.jboss.util.naming.Util;
import org.mobicents.slee.container.component.ComponentContainer;
import org.mobicents.slee.container.component.ComponentIDImpl;
import org.mobicents.slee.container.component.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptor;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.ProfileSpecificationIDImpl;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.component.SbbEventEntry;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.deployment.DeploymentManager;
import org.mobicents.slee.container.component.deployment.EventTypeDeploymentDescriptorParser;
import org.mobicents.slee.container.component.deployment.ProfileSpecificationDescriptorParser;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.deployment.SbbDeployer;
import org.mobicents.slee.container.management.EventManagement;
import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;
import org.mobicents.slee.container.management.jmx.ComponentIDArrayPropertyEditor;
import org.mobicents.slee.container.management.jmx.ComponentIDPropertyEditor;
import org.mobicents.slee.container.management.jmx.DeployableUnitIDPropertyEditor;
import org.mobicents.slee.container.management.jmx.LevelPropertyEditor;
import org.mobicents.slee.container.management.jmx.ObjectPropertyEditor;
import org.mobicents.slee.container.management.jmx.PropertiesPropertyEditor;
import org.mobicents.slee.container.management.jmx.ResourceManagement;
import org.mobicents.slee.container.management.jmx.ServiceManagement;
import org.mobicents.slee.container.management.jmx.ServiceStatePropertyEditor;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.container.management.xml.DefaultSleeEntityResolver;
import org.mobicents.slee.container.profile.ProfileDeployer;
import org.mobicents.slee.container.profile.ProfileSpecificationIDPropertyEditor;
import org.mobicents.slee.container.rmi.RmiServerInterfaceMBean;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.resource.EventLookup;
import org.mobicents.slee.resource.ResourceAdaptorContext;
import org.mobicents.slee.resource.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.resource.ResourceAdaptorTypeDescriptorImpl;
import org.mobicents.slee.runtime.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.EventRouter;
import org.mobicents.slee.runtime.EventRouterImpl;
import org.mobicents.slee.runtime.SleeInternalEndpoint;
import org.mobicents.slee.runtime.SleeInternalEndpointImpl;
import org.mobicents.slee.runtime.cache.CacheableSet;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.facilities.AlarmFacilityImpl;
import org.mobicents.slee.runtime.facilities.NullActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.facilities.ProfileFacilityImpl;
import org.mobicents.slee.runtime.facilities.ProfileTableActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.facilities.TraceFacilityImpl;
import org.mobicents.slee.runtime.sbb.SbbObjectPoolManagement;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityFactoryImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
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
 * 
 */
public class SleeContainer implements ComponentContainer {

	private final static Logger logger = Logger.getLogger(SleeContainer.class);

	/** The lifecycle state of the SLEE */
	private SleeState sleeState;

	// An interface for the resource adaptor to retrieve the event type id.
	private EventLookup eventLookup;

	public EventLookup getEventLookupFacility() {
		return eventLookup;
	}

	// An abstraction used by resource adaptors to post events to the
	// slee event queue.
	private SleeInternalEndpoint sleeEndpoint;

	// the class that actually posts events to the SBBs.
	// This should be made into a facility and registered with jmx and jndi
	// so it can be independently controlled.
	private EventRouter router;

	public EventRouter getEventRouter() {
		return this.router;
	}

	// monitor object for sync on management operations
	private final Object managementMonitor = new Object();

	public Object getManagementMonitor() {
		return managementMonitor;
	}

	// ------------ slee mbeans

	private ServiceManagement serviceManagement;

	/**
	 * Get the service management
	 * 
	 * @throws Exception
	 */
	public ServiceManagement getServiceManagement() {
		return this.serviceManagement;
	}

	// ------------ other mbeans

	private RmiServerInterfaceMBean rmiServerInterfaceMBeanImpl;
	private EventManagement eventManagement;
	private ResourceManagement resourceManagement;
	private SbbObjectPoolManagement sbbPoolManagement;

	public EventManagement getEventManagement() {
		return eventManagement;
	}

	public ResourceManagement getResourceManagement() {
		return resourceManagement;
	}

	public SbbObjectPoolManagement getSbbPoolManagement() {
		return sbbPoolManagement;
	}

	// ------------ slee factories

	private ActivityContextFactoryImpl activityContextFactory;
	private NullActivityContextInterfaceFactoryImpl nullActivityContextInterfaceFactory;
	private NullActivityFactoryImpl nullActivityFactory;
	private ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory;
	private ServiceActivityContextInterfaceFactoryImpl serviceActivityContextInterfaceFactory;
	private ServiceActivityFactoryImpl serviceActivityFactory;

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

	// ------------ slee facilities

	private ActivityContextNamingFacilityImpl activityContextNamingFacility;
	private AlarmFacilityImpl alarmFacility;
	private ProfileFacilityImpl profileFacility;
	private TimerFacilityImpl timerFacility;
	private TraceFacilityImpl traceFacility;

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

	// --- UNCHECKED

	public static boolean isSecurityEnabled = false;

	// For unit testing only -- to be removed later.
	private static final String JNDI_NAME = "container";

	public static final String JVM_ENV = "java:";

	/** standard ENC name in JNDI */
	public static final String COMP_ENV = "java:comp/env";

	/** the root context for SLEE */
	private static final String CTX_SLEE = "slee";

	private DeploymentManager deploymentManager;

	private ResourceAdaptorContext bootstrapContext;

	private MBeanServer mbeanServer;

	private ClassLoader loader;

	{
		// establish the location of mobicents.sar
		initDeployPath();
		// Config JUL logger to use Log4J filter
		configLogger();
	}

	/*
	 * private DeploymentCacheManager getDeploymentCacheManager() throws
	 * SystemException { DeploymentCacheManager cm = (DeploymentCacheManager)
	 * SleeContainer
	 * .getTransactionManager().getDeferredData("DeploymentCache"); if (cm ==
	 * null) { cm = new DeploymentCacheManager();
	 * SleeContainer.getTransactionManager().putDeferredData( "DeploymentCache",
	 * cm); cm.loadFromCache(); }
	 * 
	 * return cm; }
	 */
	private DeploymentCacheManager deploymentCacheManager;

	private static SleeContainer sleeContainer;

	private static SleeTransactionManager sleeTransactionManager;

	/**
	 * location of mobicents.sar
	 */
	private static String deployPath;

	private static void initDeployPath() {
		try {
			java.net.URL url = SleeContainer.class.getClassLoader()
					.getResource("META-INF/..");
			java.net.URI uri = new java.net.URI(url.toExternalForm());
			deployPath = new File(uri).getAbsolutePath();
		} catch (URISyntaxException e) {
			logger
					.error(
							"Failed to establish path to Mobicents root deployment directory (mobicents.sar)",
							e);
			deployPath = null;
		}
	}

	private static void configLogger() {
		Handler[] handlers = java.util.logging.Logger.getLogger("")
				.getHandlers();

		for (Handler handler : handlers)
			if (handler instanceof ConsoleHandler)
				handler.setFilter(new MobicentsLogFilter());
	}

	/**
	 * 
	 * @return the full file system path where mobicents.sar is located
	 */
	public static String getDeployPath() {
		return deployPath;
	}

	public DeploymentCacheManager getDeploymentManager() {
		if (deploymentCacheManager == null) {
			deploymentCacheManager = new DeploymentCacheManager();
		}
		return deploymentCacheManager;
	}

	public ClassLoader getClassLoader() {
		return this.loader;
	}

	/**
	 * Initialization code.
	 * 
	 */
	public void init(SleeManagementMBean sleeManagementMBean,
			ObjectName rmiServerInterfaceMBean) throws Exception {

		logger.info("Initializing SLEE container...");

		initNamingContexts();

		serviceManagement = new ServiceManagement(this);

		// Force class loading of ConcreteClassGeneratorUtils to initilize the
		// static pool
		ConcreteClassGeneratorUtils.class.getClass();

		DefaultSleeEntityResolver.init(this.getClass().getClassLoader());

		this.deploymentManager = new DeploymentManager();

		this.eventManagement = new EventManagement(this);
		this.eventLookup = new EventLookup(this);

		this.deployStandardProfileSpecs();

		this.activityContextFactory = new ActivityContextFactoryImpl(this);
		this.router = new EventRouterImpl(this);
		this.sleeEndpoint = new SleeInternalEndpointImpl(
				activityContextFactory, router, this);

		// Initialize the various facilities for the slee.

		this.activityContextNamingFacility = new ActivityContextNamingFacilityImpl();
		registerWithJndi("slee/facilities", "activitycontextnaming",
				activityContextNamingFacility);

		this.nullActivityContextInterfaceFactory = new NullActivityContextInterfaceFactoryImpl(
				this);
		registerWithJndi("slee/nullactivity",
				"nullactivitycontextinterfacefactory",
				nullActivityContextInterfaceFactory);

		this.nullActivityFactory = new NullActivityFactoryImpl(this);
		registerWithJndi("slee/nullactivity", "nullactivityfactory",
				nullActivityFactory);

		this.profileTableActivityContextInterfaceFactory = new ProfileTableActivityContextInterfaceFactoryImpl();
		registerWithJndi("slee/facilities",
				ProfileTableActivityContextInterfaceFactoryImpl.JNDI_NAME,
				profileTableActivityContextInterfaceFactory);

		timerFacility = new TimerFacilityImpl(this);
		registerWithJndi("slee/facilities", TimerFacilityImpl.JNDI_NAME,
				timerFacility);

		this.profileFacility = new ProfileFacilityImpl();
		registerWithJndi("slee/facilities", ProfileFacilityImpl.JNDI_NAME,
				profileFacility);

		this.serviceActivityFactory = new ServiceActivityFactoryImpl();
		registerWithJndi("slee/serviceactivity/",
				ServiceActivityFactoryImpl.JNDI_NAME, serviceActivityFactory);

		this.serviceActivityContextInterfaceFactory = new ServiceActivityContextInterfaceFactoryImpl(
				this);
		registerWithJndi("slee/serviceactivity/",
				ServiceActivityContextInterfaceFactoryImpl.JNDI_NAME,
				serviceActivityContextInterfaceFactory);

		this.bootstrapContext = new ResourceAdaptorContext(this
				.getSleeEndpoint(), this.getEventLookupFacility());

		registerWithJndi();

		startRMIServer(this.nullActivityFactory, this.sleeEndpoint,
				this.eventLookup, rmiServerInterfaceMBean);

		registerPropertyEditors();

		// init the sbb pool manager
		this.sbbPoolManagement = new SbbObjectPoolManagement(this);
		this.sbbPoolManagement.register();

		resourceManagement = new ResourceManagement(this);
	}

	private void registerWithJndi() {
		registerWithJndi("slee", JNDI_NAME, this);
	}

	private void unregisterWithJndi() {
		unregisterWithJndi("slee/" + JNDI_NAME);
	}

	/**
	 * Return the SleeContainer instance registered in the JVM scope of JNDI
	 * 
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

	/*
	 * Start the HA RMI Server, used by JCA resource adaptors to communicate
	 * with the SLEE
	 */
	private void startRMIServer(NullActivityFactoryImpl naf,
			SleeInternalEndpoint endpoint, EventLookup eventLookup,
			ObjectName rmiServerInterfaceMBean) {

		try {
			logger.debug("creating RmiServerInterface using MBeanProxy");

			rmiServerInterfaceMBeanImpl = (RmiServerInterfaceMBean) MBeanProxy
					.get(RmiServerInterfaceMBean.class,
							rmiServerInterfaceMBean, mbeanServer);

			rmiServerInterfaceMBeanImpl.startRMIServer(naf, endpoint,
					eventLookup, activityContextFactory);

		} catch (Exception e) {
			logger.error(
					"Failed to start HA RMI server for Remote slee service", e);
		}
	}

	private void stopRMIServer() {

		logger.debug("Stopping RMI Server for slee service");
		rmiServerInterfaceMBeanImpl.stopRMIServer();
	}

	/**
	 * Load up all the services from the deployment directory.
	 * 
	 * @throws Exception
	 */
	public void initializeServices() throws Exception {

		try {
			deploymentManager.deployAllUnitsAtLocation(getDeployPath()
					+ "/deploy", // get
					// deployable
					// units
					// here,
					getDeployPath(), // put classes from them here
					this); // and install them in this container

		} catch (Throwable ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * 
	 * Cleanup in the reverse order of init()
	 * 
	 * @throws NamingException
	 * 
	 */
	public void close() throws NamingException {

		// Cleaning up the cache is not desired for failover scenarios.
		// Cache should be cleaned only when the last node in a SLEE cluster is
		// shutting down.
		// TODO: cleanSleeCache();

		unregisterWithJndi();
		Context ctx = new InitialContext();
		Util.unbind(ctx, JVM_ENV + CTX_SLEE);
		stopRMIServer();
	}

	/**
	 * These are slee defined events. These are stored in the location
	 * slee-event-jar.xml Parse these and make them known to the slee.
	 * 
	 */
	/*
	private void deployStandardEvents() throws Exception {
		// TODO: modify to use new deployment mechanisms (have one or 2 DU for
		// the standard events)
		logger.info("Deploying standard events.");
		List stdEvents = EventTypeDeploymentDescriptorParser
				.parseStandardEvents(getDeployPath()
						+ "/xml/slee-event-jar.xml");
		Iterator it = stdEvents.iterator();
		while (it.hasNext()) {
			MobicentsEventTypeDescriptor etype = (MobicentsEventTypeDescriptor) it
					.next();
			eventManagement.installEventType(etype);
		}
	}
	 */
	/**
	 * These are slee defined profiles. These are stored in the location
	 * slee-profile-spec-jar.xml Parse these and make them known to the slee.
	 * 
	 */
	private void deployStandardProfileSpecs() throws Exception {
		logger.info("Deploying standard profile specs.");
		List stdProfileSpecs = ProfileSpecificationDescriptorParser
				.parseStandardProfileSpecifications(getDeployPath()
						+ "/xml/slee-profile-spec-jar.xml");
		Iterator it = stdProfileSpecs.iterator();
		while (it.hasNext()) {
			ProfileSpecificationDescriptorImpl profileSpecification = (ProfileSpecificationDescriptorImpl) it
					.next();
			this.install(profileSpecification, null);
		}
	}

	/**
	 * Unregister an internal slee component with jndi.
	 * 
	 */
	public static void unregisterWithJndi(String prefix, String name) {
		unregisterWithJndi(prefix + "/" + name);
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
	 * 
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
	 * 
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
	 * 
	 * Convenience method for unregistering SLEE facilities with JNDI
	 * 
	 * @param facilityName
	 * @param facility
	 */
	public static void unregisterFacilityWithJndi(String facilityName) {
		unregisterWithJndi("slee/facilities", facilityName);
	}

	/**
	 * 
	 * Convenience method for unregistering SLEE facilities with JNDI
	 * 
	 * @param facilityName
	 * @param facility
	 * @throws NamingException
	 */
	private static Object lookupFacilityInJndi(String facilityName)
			throws NamingException {
		return getFromJndi("slee/facilities" + "/" + facilityName);
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

	/**
	 * Deploys an SBB. This generates the code to convert abstract to concrete
	 * class and registers the component in the component table and creates an
	 * object pool for the sbb id.
	 * 
	 * @param mobicentsSbbDescriptor
	 *            the descriptor of the sbb to install
	 * @throws Exception
	 */
	private synchronized void installSbb(
			final MobicentsSbbDescriptor mobicentsSbbDescriptor)
			throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing SBB " + mobicentsSbbDescriptor.getID());
		}

		getTransactionManager().mandateTransaction();

		// Iterate over the set of event entries and initialize descriptor
		for (Iterator it = mobicentsSbbDescriptor.getSbbEventEntries()
				.iterator(); it.hasNext();) {
			SbbEventEntry eventEntry = (SbbEventEntry) it.next();
			EventTypeID eventTypeId = eventManagement.getEventType(eventEntry
					.getEventTypeRefKey());
			if (eventTypeId == null)
				throw new DeploymentException("Unknown event type "
						+ eventEntry.getEventTypeRefKey());
			mobicentsSbbDescriptor.addEventEntry(eventTypeId, eventEntry);
		}

		// create deployer
		SbbDeployer sbbDeployer = new SbbDeployer(getDeployPath());
		// change classloader
		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					mobicentsSbbDescriptor.getClassLoader());
			// Set up the comp/env naming context for the Sbb.
			mobicentsSbbDescriptor.setupSbbEnvironment();
			// deploy the sbb
			sbbDeployer.deploySbb(mobicentsSbbDescriptor, this);
		} catch (Exception ex) {
			throw ex;
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
		// add a rollback action to unregister class loader
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				((UnifiedClassLoader3) mobicentsSbbDescriptor.getClassLoader())
						.unregister();
			}
		};
		sleeTransactionManager.addAfterRollbackAction(action);

		// create the pool for the given SbbID
		sbbPoolManagement.createObjectPool(mobicentsSbbDescriptor,
				sleeTransactionManager);

		// Set Trace to off
		getTraceFacility().setTraceLevelOnTransaction(
				mobicentsSbbDescriptor.getID(), Level.OFF);
		getAlarmFacility().registerComponent(mobicentsSbbDescriptor.getID());

		// add sbb component
		this.addSbbComponent(mobicentsSbbDescriptor);

		logger.info("Installed SBB " + mobicentsSbbDescriptor.getID());
	}

	/**
	 * check the number of activity contexts.
	 * 
	 */
	public int getActivityContextCount() {
		return this.activityContextFactory.getActivityContextCount();
	}

	/**
	 * get the slee endpoint
	 * 
	 */
	public SleeInternalEndpoint getSleeEndpoint() {
		return this.sleeEndpoint;
	}

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
	 * Get the sbb components
	 */
	public SbbIDImpl[] getSbbIDs() throws Exception {
		boolean b = SleeContainer.getTransactionManager().requireTransaction();
		try {
			SbbIDImpl[] retval = new SbbIDImpl[getDeploymentManager()
					.getSbbComponents().size()];
			this.getDeploymentManager().getSbbComponents().keySet().toArray(
					retval);
			return retval;
		} finally {
			if (b)
				SleeContainer.getTransactionManager().commit();
		}
	}

	public SbbDescriptor getSbbComponent(SbbID sbbComponentId) {

		getTransactionManager().mandateTransaction();
		if (logger.isDebugEnabled()) {
			logger.debug("getting " + sbbComponentId);
		}
		return (SbbDescriptor) this.getDeploymentManager().getSbbComponents()
				.get(sbbComponentId);
	}

	private void addSbbComponent(SbbDescriptor sbbComponent) throws Exception {

		getTransactionManager().mandateTransaction();
		if (logger.isDebugEnabled()) {
			logger.debug("adding sbb component for "
					+ sbbComponent.getID().toString());
		}
		this.getDeploymentManager().getSbbComponents().put(
				sbbComponent.getID(), sbbComponent);
		SbbID[] sbbs = sbbComponent.getSbbs();
		// I refer to all these sbbs.
		for (int i = 0; i < sbbs.length; i++) {
			this.addReferringComponent(sbbs[i], sbbComponent.getID());
		}

		// I refer to all these profile specifications.
		ProfileSpecificationID[] profileIDs = sbbComponent
				.getProfileSpecifications();

		for (int i = 0; i < profileIDs.length; i++) {
			this.addReferringComponent(profileIDs[i], sbbComponent.getID());
		}

		// I refer to all the following EventTypeIDs.
		EventTypeID[] eventTypes = sbbComponent.getEventTypes();
		for (int i = 0; i < eventTypes.length; i++) {
			this.addReferringComponent(eventTypes[i], sbbComponent.getID());
		}

		// I refer to the following resource adaptor type ids.
		ResourceAdaptorTypeID[] raTypeIDs = sbbComponent
				.getResourceAdaptorTypes();
		if (logger.isDebugEnabled()) {
			logger.debug("ResourceAdaptorTypeIDs " + raTypeIDs.length);
		}
		for (int i = 0; i < raTypeIDs.length; i++) {
			this.addReferringComponent(raTypeIDs[i], sbbComponent.getID());
		}

		// I refer to the following resource adaptor links
		String[] entityLinks = sbbComponent.getResourceAdaptorEntityLinks();
		for (int i = 0; i < entityLinks.length; i++) {
			ResourceAdaptorID raID = resourceManagement
					.getResourceAdaptor(resourceManagement
							.getResourceAdaptorEntityName(entityLinks[i]));
			this.addReferringComponent(raID, sbbComponent.getID());
		}

		// I refer to the following AddressProfile.
		ProfileSpecificationID addressProfile = sbbComponent
				.getAddressProfileSpecification();
		if (addressProfile != null)
			this.addReferringComponent(addressProfile, sbbComponent.getID());

		if (logger.isDebugEnabled()) {
			logger.debug("dependencyTable "
					+ this.getDeploymentManager().getReferringComponents());
			logger.debug("sbbComponents "
					+ this.getDeploymentManager().getSbbComponents());
		}
	}

	/**
	 * Get the transaction manager from JNDI
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
	 * Add a referring component to a given component ID. This is only a table
	 * of direct references. A component cannot be uninstalled if there are any
	 * components that refer to it.
	 * 
	 * @param componentID
	 * @param referringComponent
	 * @throws DeploymentException
	 */
	private void addReferringComponent(ComponentID componentID,
			ComponentID referringComponent) {
		if (logger.isDebugEnabled()) {
			logger.debug("AddReferringComponent componentID = " + componentID
					+ " referringComponent = " + referringComponent);
		}

		Set rcomp = (Set) this.getDeploymentManager().getReferringComponents()
				.get(componentID);
		if (rcomp == null) {
			rcomp = getDeploymentManager().newReferringCompSet();
			this.getDeploymentManager().getReferringComponents().put(
					componentID, rcomp);
		}
		rcomp.add(referringComponent);
	}

	/**
	 * This is done pre-removal. Go through the component table and remove all
	 * places where I refer to somebody.
	 * 
	 */
	private void removeReferredComponent(ComponentID componentID) {
		Iterator it = this.getDeploymentManager().getReferringComponents()
				.keySet().iterator();
		while (it.hasNext()) {
			ComponentID key = (ComponentID) it.next();
			Map refComps = (Map) this.getDeploymentManager()
					.getReferringComponents();
			CacheableSet rcomp = (CacheableSet) refComps.get(key);
			rcomp.remove(componentID);
			if (rcomp.isEmpty()) {
				refComps.remove(key);
				rcomp.remove();
			}
		}
	}

	/**
	 * This is called by our management interface.
	 * 
	 * @param referredComponent
	 * @return
	 */
	public ComponentIDImpl[] getReferringComponents(
			ComponentID referredComponent) {
		boolean b = getTransactionManager().requireTransaction();
		try {
			Set rcomp = (Set) this.getDeploymentManager()
					.getReferringComponents().get(referredComponent);
			if (rcomp == null)
				return new ComponentIDImpl[0];
			else {
				ComponentIDImpl[] retval = new ComponentIDImpl[rcomp.size()];
				rcomp.toArray(retval);
				return retval;
			}
		} catch (Exception ex) {
			throw new RuntimeException("Unexpected exception!", ex);

		} finally {
			try {
				if (b)
					getTransactionManager().commit();
			} catch (Exception ex) {
				throw new RuntimeException("tx manager failed", ex);
			}
		}
	}

	/**
	 * 
	 * @param cid
	 * @param duid
	 * @return
	 *            <ul>
	 *            <li><b>true</b> - if this cid has been installed for the
	 *            first time - no cid-duid mapping was present</li>
	 *            <li><b>false</b> - if cid has been installed by some other
	 *            DU, some cid-duid mapping is present</li>
	 *            </ul>
	 */
	private boolean addReferringDU(ComponentID cid, DeployableUnitID duid) {
		boolean installedForTheFirstTime = false;
		try {
			Set referringDus = (Set) this.getDeploymentManager()
					.getComponentIDToDeployableUnitIDMap().get(cid);
			if (referringDus == null) {
				referringDus = newReferringDuSet();
				this.getDeploymentManager()
						.getComponentIDToDeployableUnitIDMap().put(cid,
								referringDus);
				installedForTheFirstTime = true;
			}
			referringDus.add(duid);
			if (logger.isDebugEnabled()) {
				logger.debug("componentToDUMap = "
						+ this.getDeploymentManager()
								.getComponentIDToDeployableUnitIDMap());
			}
			return installedForTheFirstTime;
		} catch (Exception ex) {
			throw new RuntimeException("Tx manager failed !", ex);
		}
	}

	private Set newReferringDuSet() {
		Set refs = getDeploymentManager().newReferringDuSet();
		return refs;
	}

	private void removeReferredDU(DeployableUnitID duid) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("removeReferredDU " + duid);
			}
			Map ci2du = getDeploymentManager()
					.getComponentIDToDeployableUnitIDMap();
			for (Iterator it = ci2du.keySet().iterator(); it.hasNext();) {
				ComponentID cid = (ComponentID) it.next();
				CacheableSet dus = (CacheableSet) ci2du.get(cid);
				dus.remove(duid);
				if (dus.isEmpty()) {
					ci2du.remove(cid);
					dus.remove();
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("removeReferredDU After Remove "
						+ this.getDeploymentManager()
								.getComponentIDToDeployableUnitIDMap());
			}
		} catch (Exception ex) {
			throw new RuntimeException("tx manager failed ! ", ex);
		}
	}

	/**
	 * Return true if there are any components that hold references to the
	 * deployable unit
	 * 
	 * @param dudesc --
	 *            the deployable unit descriptor to check.
	 * @return true -- if there are referring components.
	 * @throws Exception --
	 *             if theres a problem with the cache.
	 */
	private boolean hasReferringDU(DeployableUnitDescriptorImpl dudesc)
			throws Exception {

		ComponentID[] cid = dudesc.getComponents();
		for (int i = 0; i < cid.length; i++) {
			Set hset = (Set) this.getDeploymentManager()
					.getComponentIDToDeployableUnitIDMap().get(cid[i]);
			if (hset == null)
				return false;
			else if (hset.size() != 1) {
				// See if there's another DU that refers directly to me.
				if (logger.isDebugEnabled()) {
					logger.debug("Direct reference detected to component  "
							+ cid[i]);
					logger.debug("componentID to DUID Map = " + hset);
				}
				// return true;
				if (dudesc.hasInstalledComponent(cid[i])) {
					if (logger.isDebugEnabled()) {
						logger
								.debug("dudesc.hasInstalledComponent(cid[i])[TRUE]");
					}
					return true;
				} else {
					if (logger.isDebugEnabled()) {
						logger
								.debug("dudesc.hasInstalledComponent(cid[i])[FALSE]");
					}
					return false;
				}

			} else {
				// I'm the only one that references this component.
				ComponentID[] referringComponents = this
						.getReferringComponents(cid[i]);
				// See if there's any other component that refers to me that
				// belongs
				// to another DU.
				if (referringComponents != null) {
					for (int k = 0; k < referringComponents.length; k++) {
						if (logger.isDebugEnabled()) {
							logger.debug("checking referring component "
									+ referringComponents[k]);

						}
						if (!((Set) this.getDeploymentManager()
								.getComponentIDToDeployableUnitIDMap().get(
										referringComponents[k]))
								.contains(dudesc.getDeployableUnit())) {
							if (logger.isDebugEnabled()) {
								logger
										.debug("referring component = "
												+ this
														.getDeploymentManager()
														.getComponentIDToDeployableUnitIDMap()
														.get(
																referringComponents[k]));
								logger.debug("attempting to remove = "
										+ dudesc.getDeployableUnit());
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * This is an implementation of the
	 * <code>ComponentContainer.installComponent
	 * </code> method.
	 * 
	 * (Ivelin) TODO: this method does not need to be synchronized. Right now it
	 * is, because the deploymeny code is not clean. For example there is one
	 * global ClassPool shared between all deployments, instead of an hierarchy
	 * with clean isolation.
	 * 
	 * @param descriptor
	 *            a descriptor of the component to deploy.
	 */
	public synchronized void install(ComponentDescriptor descriptor,
			DeployableUnitDescriptor duDescriptor) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing " + descriptor);
		}

		boolean b = getTransactionManager().requireTransaction();
		try {
			if (descriptor instanceof MobicentsSbbDescriptor) {
				installSbb((MobicentsSbbDescriptor) descriptor);
			} else if (descriptor instanceof ServiceDescriptorImpl) {
				serviceManagement
						.installService((ServiceDescriptorImpl) descriptor);
			} else if (descriptor instanceof MobicentsEventTypeDescriptor) {
				eventManagement
						.installEventType((MobicentsEventTypeDescriptor) descriptor);
			} else if (descriptor instanceof ResourceAdaptorTypeDescriptor) {
				resourceManagement
						.installResourceAdaptorType((ResourceAdaptorTypeDescriptorImpl) descriptor);
			} else if (descriptor instanceof ProfileSpecificationDescriptorImpl) {
				installProfile((ProfileSpecificationDescriptorImpl) descriptor);
			} else if (descriptor instanceof ResourceAdaptorDescriptorImpl) {
				resourceManagement
						.installResourceAdaptor((ResourceAdaptorDescriptorImpl) descriptor);
			} else {
				logger.fatal("unknown component type!");
			}
		} catch (Exception ex) {
			logger.error("Exception caught while installing component", ex);
			throw ex;
		} finally {
			if (b)
				getTransactionManager().commit();
		}
	}

	/**
	 * 
	 * Deploys a Profile. This generates the code to generate concrete classes
	 * and registers the component in the component table
	 * 
	 * @param profileSpecificationDescriptorImpl
	 *            the descriptor of the profile to install
	 * @throws DeploymentException
	 * @throws SystemException
	 * @throws Exception
	 */
	private void installProfile(
			ProfileSpecificationDescriptorImpl profileSpecificationDescriptorImpl)
			throws DeploymentException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing Profile "
					+ profileSpecificationDescriptorImpl.getID());
		}

		sleeTransactionManager.mandateTransaction();

		File duPath = null;
		if (profileSpecificationDescriptorImpl.getDeployableUnit() != null) {
			duPath = ((DeployableUnitIDImpl) profileSpecificationDescriptorImpl
					.getDeployableUnit()).getDUDeployer()
					.getTempClassDeploymentDir();
		} else {
			duPath = new File(getDeployPath());
		}

		new ProfileDeployer(duPath)
				.deployProfile(profileSpecificationDescriptorImpl);

		this.getDeploymentManager().getProfileComponents().put(
				profileSpecificationDescriptorImpl.getID(),
				profileSpecificationDescriptorImpl);

		logger.info("Installed Profile "
				+ profileSpecificationDescriptorImpl.getID());
	}

	/**
	 * Register a deployable unit given its descriptor.
	 * 
	 * @param descriptor -
	 *            descriptor to register
	 * 
	 */
	public synchronized void addDeployableUnit(
			DeployableUnitDescriptor descriptor) {
		SleeTransactionManager transactionManager = getTransactionManager();
		boolean b = false;
		try {
			b = transactionManager.requireTransaction();

			DeployableUnitDescriptorImpl dudesc = (DeployableUnitDescriptorImpl) descriptor;
			DeployableUnitID deployableUnitId = ((DeployableUnitDescriptorImpl) descriptor)
					.getDeployableUnit();
			if (logger.isDebugEnabled()) {
				logger.debug("Installing DU with descriptor:  " + descriptor);
			}
			if (descriptor == null)
				logger.fatal("null descriptor");
			this.getDeploymentManager().getDeployableUnitIDtoDescriptorMap()
					.put(deployableUnitId, descriptor);
			String url = descriptor.getURL();
			this.getDeploymentManager().getUrlToDeployableUnitIDMap().put(url,
					deployableUnitId);
			ComponentID[] components = dudesc.getComponents();
			// Add dependencies from all services to me.
			for (int i = 0; i < components.length; i++) {
				// add to the dependency table.
				// this.addReferringDU(components[i], deployableUnitId);

				if (this.addReferringDU(components[i], deployableUnitId)) {
					// THIS deployableUnitId == virgin INSTALLATION OF THIS
					// components[i](cid)
					dudesc.installComponent(components[i]);
					if (logger.isDebugEnabled()) {
						logger.debug("Installed [" + components[i] + "] from ["
								+ dudesc.getURL() + "]");
					}
				} else {
					// DO NOTHING MORE, SOMEONE ELSE HAS INSTALLED THIS
					// COMPONENT
					if (logger.isDebugEnabled()) {
						logger.debug("Already exists, didn't install ["
								+ components[i] + "] from [" + dudesc.getURL()
								+ "]");
					}
				}
			}
		} catch (Exception ex) {
			try {
				transactionManager.setRollbackOnly();
			} catch (SystemException se) {
				String err = "Failed addDeployableUnit("
						+ descriptor
						+ "), because of system exception during tx.setRollbackOnly()! ";
				logger.error(err, se);
				throw new RuntimeException(err, se);
			}
			throw new RuntimeException("unexpected exception ! ", ex);
		} finally {
			try {
				if (b)
					transactionManager.commit();
			} catch (SystemException se) {
				String err = "Failed addDeployableUnit(" + descriptor
						+ "), because of system exception during tx.commit()! ";
				logger.error(err, se);
				throw new RuntimeException("unexpected exception ! ", se);
			}
		}
	}

	/**
	 * Get an array containing the deployable unit ids known to the container.
	 * 
	 * @return
	 */
	public DeployableUnitID[] getDeployableUnits() {
		// TODO : defaultSleeDUs is a hard coded variable in order to pass test
		// 3776, which is incorrect.
		// See the discussion:
		// https://mobicents.dev.java.net/servlets/ProjectForumMessageView?messageID=7035&forumID=739
		boolean b = getTransactionManager().requireTransaction();

		try {
			final int defaultSleeDUs = 2;
			int duds = getDeploymentManager()
					.getDeployableUnitIDtoDescriptorMap().size();
			DeployableUnitID[] duArray = new DeployableUnitIDImpl[duds
					+ defaultSleeDUs];
			this.getDeploymentManager().getDeployableUnitIDtoDescriptorMap()
					.keySet().toArray(duArray);
			// pad with non-null objects. The SLEE TCK expects non-null values
			// in the array
			// This is a hack to cover another TCK hack, which expects at least
			// 2 DUDs in a SLEE,
			// although the spec does not mandate it. Using -1 to ensure that
			// there is no overlap with valid DUD
			DeployableUnitID dummyDud = new DeployableUnitIDImpl(-1);
			for (int i = duds; i < duArray.length; i++) {
				duArray[i] = dummyDud;
			}

			return duArray;
		} catch (Exception ex) {
			throw new RuntimeException("Unexpected exception ", ex);
		} finally {
			try {
				if (b)
					getTransactionManager().commit();
			} catch (Exception ex) {
				throw new RuntimeException("unexpected exception ! ", ex);
			}
		}
	}

	/**
	 * Get the deployable unit descriptor for a given deployable unit.
	 * 
	 * @param deployableUnitID --
	 *            the deployable unit id
	 * 
	 * @return
	 */
	public DeployableUnitDescriptor getDeployableUnitDescriptor(
			DeployableUnitID deployableUnitID) {
		boolean b = getTransactionManager().requireTransaction();
		try {
			DeployableUnitDescriptor dud = (DeployableUnitDescriptor) getDeploymentManager()
					.getDeployableUnitIDtoDescriptorMap().get(deployableUnitID);
			return dud;
		} catch (Exception ex) {
			throw new RuntimeException(
					"unexpected error getting du descriptor", ex);
		} finally {
			try {
				if (b) {
					getTransactionManager().commit();
				}
			} catch (Exception ex) {
				throw new RuntimeException("Unexpected exception ", ex);
			}
		}
	}

	/**
	 * Get the descriptor of a component given its component ID
	 * 
	 * @param componentId --
	 *            component id for which we want the descriptor
	 * 
	 * @return the descriptor corresponding to the component id.
	 */

	public ComponentDescriptor getComponentDescriptor(ComponentID componentId)
			throws IllegalArgumentException {
		boolean b = false;
		try {
			b = SleeContainer.getTransactionManager().requireTransaction();
			ComponentIDImpl cidImpl = (ComponentIDImpl) componentId;
			if (cidImpl.isSbbID()) {
				return (ComponentDescriptor) this.getDeploymentManager()
						.getSbbComponents().get(componentId);
			} else if (cidImpl.isServiceID()) {
				return (ComponentDescriptor) ((ServiceComponent) this
						.getDeploymentManager().getServiceComponents().get(
								componentId)).getServiceDescriptor();
			} else if (cidImpl.isProfileSpecificationID()) {
				return (ComponentDescriptor) ((ProfileSpecificationDescriptor) this
						.getDeploymentManager().getProfileComponents().get(
								componentId));
			} else if (cidImpl.isEventTypeID()) {
				return eventManagement
						.getEventDescriptor((EventTypeID) componentId);
			} else if (cidImpl.isResourceAdaptorTypeID()) {
				return resourceManagement.getResourceAdaptorType(
						(ResourceAdaptorTypeID) componentId).getRaTypeDescr();
			} else if (cidImpl instanceof ResourceAdaptorIDImpl) {
				return resourceManagement.getInstalledResourceAdaptor(
						(ResourceAdaptorID) componentId).getDescriptor();
			} else
				throw new IllegalArgumentException(" bad component id");
		} catch (Exception ex) {
			throw new RuntimeException("Tx manager failed !", ex);
		} finally {
			try {
				if (b)
					getTransactionManager().commit();
			} catch (SystemException ex) {
				throw new RuntimeException("Tx manager failed !", ex);
			}
		}
	}

	public ResourceAdaptorContext getBootstrapContext() {
		return this.bootstrapContext;
	}

	/**
	 * Check if a deployable Unit is deployed.
	 * 
	 * @param deployableUnitID
	 * 
	 * @return true if the deployable unit is deployed.
	 */
	public boolean isInstalled(DeployableUnitID deployableUnitID) {
		boolean b = getTransactionManager().requireTransaction();
		try {
			return this.getDeploymentManager()
					.getDeployableUnitIDtoDescriptorMap().containsKey(
							deployableUnitID);
		} catch (Exception ex) {
			throw new RuntimeException("tx manager failed! ", ex);
		} finally {
			try {
				if (b)
					getTransactionManager().commit();
			} catch (SystemException ex) {
				throw new RuntimeException("tx maanger failed ", ex);
			}
		}
	}

	/**
	 * @param componentId
	 * @return
	 */
	public boolean isInstalled(ComponentID componentId) {
		boolean b = getTransactionManager().requireTransaction();

		try {
			if (componentId instanceof SbbID) {
				return this.getDeploymentManager().getSbbComponents()
						.containsKey(componentId);
			} else if (componentId instanceof ServiceID) {
				return this.getDeploymentManager().getServiceComponents()
						.containsKey(componentId);
			} else if (componentId instanceof EventTypeID) {
				return eventManagement.isInstalled((EventTypeID) componentId);
			} else if (componentId instanceof ProfileSpecificationID) {
				return this.getDeploymentManager().getProfileComponents()
						.containsKey(componentId);
			} else
				return false;
		} catch (Exception ex) {
			throw new RuntimeException("tx manager failed! ", ex);
		} finally {
			try {
				if (b)
					getTransactionManager().commit();
			} catch (SystemException ex) {
				throw new RuntimeException("tx maanger failed ", ex);
			}
		}
	}

	private void removeReferredComponents(DeployableUnitDescriptorImpl dudesc) {

		// We return true if theres a component of the Deployable Unit that is
		// referenced by another component. We do not need to check about
		// service components.
		ComponentID[] cid = dudesc.getComponents();
		for (int i = 0; i < cid.length; i++) {
			this.removeReferredComponent(cid[i]);
		}
	}

	/**
	 * Uninstall a deployable unit and clean up all the files generated for it.
	 * This is a terrible mess.
	 * 
	 * @param deployableUnitID
	 */
	public void removeDeployableUnit(DeployableUnitID deployableUnitID)
			throws Exception {

		if (logger.isDebugEnabled())
			logger.debug("removeDeployableUnit: deployableUnitID="
					+ deployableUnitID);

		try {
			getTransactionManager().begin();
			DeployableUnitDescriptorImpl deployableUnitDescriptor = (DeployableUnitDescriptorImpl) this
					.getDeploymentManager()
					.getDeployableUnitIDtoDescriptorMap().get(deployableUnitID);
			if (deployableUnitDescriptor == null)
				throw new UnrecognizedDeployableUnitException(
						"Unrecognized deployable unit " + deployableUnitID);

			// Check if its safe to remove the deployable unit.
			if (this.hasReferringDU(deployableUnitDescriptor)) {
				throw new DependencyException(
						"Somebody is referencing a component of this DU"
								+ " -- cannot uninstall it!");
			}
			removeDU(deployableUnitDescriptor);
		} catch (Exception ex) {
			//logger.error("Exception while removing deployable unit", ex);
			getTransactionManager().setRollbackOnly();
			throw ex;
		} finally {
			getTransactionManager().commit();
		}
	}

	/**
	 * Actually do the unistall.
	 * 
	 * @param deployableUnitID
	 */
	public void removeDU(DeployableUnitDescriptorImpl deployableUnitDescriptor)
			throws Exception {

		if (logger.isDebugEnabled())
			logger.debug("removeDU: "
					+ deployableUnitDescriptor.getDeployableUnit());

		// Check if this refers to a service
		DeployableUnitIDImpl deployableUnitID = deployableUnitDescriptor
				.getDeployableUnit();
		boolean b = getTransactionManager().requireTransaction();
		try {
			this.removeReferredComponents(deployableUnitDescriptor);
			this.removeReferredDU(deployableUnitID);

			checkServicesStateOnDUUndeploy(deployableUnitID);
			this.getDeploymentManager().getDeployableUnitIDtoDescriptorMap()
					.remove(deployableUnitID);

			resourceManagement.uninstallRA(deployableUnitID);
			resourceManagement.uninstallRAType(deployableUnitID);
			eventManagement.removeEventType(deployableUnitID);
			serviceManagement.uninstallServices(deployableUnitID);

			removeDeployedProfileComps(deployableUnitID);
			removeDeployedSbbComps(deployableUnitID);

			// Clean up the various tables that refer to this Deployable Unit
			// ID.
			String url = deployableUnitID.getSourceURL().toString();
			this.getDeploymentManager().getUrlToDeployableUnitIDMap().remove(
					url);

			// Clean up all the class files.
			this.deploymentManager.undeployUnit(deployableUnitID);

		} finally {
			if (b)
				getTransactionManager().commit();
		}
	}

	private void checkServicesStateOnDUUndeploy(
			DeployableUnitIDImpl deployableUnitID) throws SystemException,
			InvalidStateException {
		Map serviceComps = this.getDeploymentManager().getServiceComponents();
		for (Iterator it = serviceComps.entrySet().iterator(); it.hasNext();) {
			Map.Entry nentry = (Map.Entry) it.next();
			ServiceComponent svc = (ServiceComponent) nentry.getValue();
			if (svc.getDeployableUnit().equals(deployableUnitID)
					&& svc.isLocked())
				throw new InvalidStateException("Service state is not stopped");
		}
	}

	private void removeDeployedSbbComps(DeployableUnitIDImpl deployableUnitID)
			throws SystemException, Exception, NamingException {

		Map sbbComps = getDeploymentManager().getSbbComponents();
		for (Iterator it = sbbComps.values().iterator(); it.hasNext();) {
			final MobicentsSbbDescriptor sbbDescriptor = (MobicentsSbbDescriptor) it
					.next();
			if (sbbDescriptor.getDeployableUnit().equals(deployableUnitID)) {
				if (!deployableUnitID.getDescriptor().hasInstalledComponent(
						sbbDescriptor.getID())) {
					if (logger.isDebugEnabled()) {
						logger.debug(" === SBBComp[" + sbbDescriptor.getID()
								+ "] WAS NOT ISNTALLED BY DU["
								+ deployableUnitID.getSourceURL() + "] ===");
					}
					continue;
				}
				if (logger.isDebugEnabled())
					logger.debug("Uninstalling SBB " + sbbDescriptor.getID()
							+ " on DU " + deployableUnitID);

				// removes the sbb object pool
				sbbPoolManagement.removeObjectPool(sbbDescriptor,
						sleeTransactionManager);

				// remove sbb from trace and alarm facilities
				getTraceFacility().unSetTraceLevel(sbbDescriptor.getID());
				getAlarmFacility().unRegisterComponent(sbbDescriptor.getID());
				if (logger.isDebugEnabled()) {
					logger.debug("Removed SBB " + sbbDescriptor.getID()
							+ " from trace and alarm facilities");
				}

				// remove sbb descriptor
				sbbComps.remove(sbbDescriptor.getID());

				logger.info("Uninstalled SBB " + sbbDescriptor.getID()
						+ " on DU " + sbbDescriptor.getDeployableUnit());

				// remove class loader
				((UnifiedClassLoader3) sbbDescriptor.getClassLoader())
						.unregister();
				if (logger.isDebugEnabled()) {
					logger.debug("Removed class loader for SBB "
							+ sbbDescriptor.getID());
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("SBB " + sbbDescriptor.getID()
							+ " belongs to "
							+ sbbDescriptor.getDeployableUnit());
				}
			}
		}
	}

	private void removeDeployedProfileComps(
			DeployableUnitIDImpl deployableUnitID) throws SystemException {
		Map profileComps = getDeploymentManager().getProfileComponents();
		for (Iterator it = profileComps.entrySet().iterator(); it.hasNext();) {
			Map.Entry nentry = (Map.Entry) it.next();
			ProfileSpecificationDescriptorImpl profileDescriptor = (ProfileSpecificationDescriptorImpl) nentry
					.getValue();
			if (logger.isDebugEnabled())
				logger.debug("deployableUnit = "
						+ profileDescriptor.getDeployableUnit());

			// Null check here because standard profiles do not have a DU
			// descriptor
			// associated with it.
			if (profileDescriptor.getDeployableUnit() != null
					&& profileDescriptor.getDeployableUnit().equals(
							deployableUnitID)) {
				if (!deployableUnitID.getDescriptor().hasInstalledComponent(
						(ComponentID) nentry.getKey())) {
					if (logger.isDebugEnabled()) {
						logger.debug(" === ProfileComp["

						+ profileDescriptor.getID()
								+ "] WAS NOT ISNTALLED BY DU["
								+ deployableUnitID.getSourceURL() + "] ===");
					}
					continue;
				}

				if (logger.isDebugEnabled())
					logger.debug("Uninstalling Profile "
							+ profileDescriptor.getID());
				ComponentID cid = (ComponentID) nentry.getKey();
				profileComps.remove(cid);

				logger.info("Uninstalled Profile " + profileDescriptor.getID());
			}
		}
	}

	/**
	 * Return the deployable Unit id given the url string from where it was
	 * loaded.
	 * 
	 * @param deploymentUrl --
	 *            url from where the unit was loaded.
	 * @return the deployable unit id for the url.
	 */
	public DeployableUnitID getDeployableUnitIDFromUrl(String deploymentUrl)
			throws UnrecognizedDeployableUnitException {
		boolean b = getTransactionManager().requireTransaction();
		try {
			DeployableUnitID retval = (DeployableUnitID) this
					.getDeploymentManager().getUrlToDeployableUnitIDMap().get(
							deploymentUrl);
			if (retval == null)
				throw new UnrecognizedDeployableUnitException(
						"Unrecognized  deployable unit " + deploymentUrl);
			else
				return retval;
		} finally {
			if (b) {
				try {
					getTransactionManager().commit();
				} catch (Exception e) {
					logger
							.error(
									"Failed to complete tx for getDeployableUnitIDFromUrl()",
									e);
					throw new SLEEException(
							"Failed to complete tx for getDeployableUnitIDFromUrl()",
							e);
				}
			}
		}
	}

	/**
	 * Get a list of profiles known to me
	 * 
	 * @return A list of profiles that are registered with me.
	 */
	public ProfileSpecificationID[] getProfileSpecificationIDs() {
		boolean b = SleeContainer.getTransactionManager().requireTransaction();
		try {
			ProfileSpecificationIDImpl[] retval = new ProfileSpecificationIDImpl[this
					.getDeploymentManager().getProfileComponents().size()];
			this.getDeploymentManager().getProfileComponents().keySet()
					.toArray(retval);
			return retval;
		} catch (Exception ex) {
			throw new RuntimeException("Tx manager failed ", ex);
		} finally {
			try {
				if (b)
					getTransactionManager().commit();
			} catch (SystemException ex) {
				throw new RuntimeException("tx maanger failed ", ex);
			}
		}
	}

	/**
	 * Register standard SLEE contexts in JNDI
	 * 
	 * @throws Exception
	 */
	protected void initNamingContexts() throws Exception {
		// Initialize the SLEE name space

		/*
		 * The following code sets the global name for the Slee
		 */
		Context ctx = new InitialContext();
		ctx = Util.createSubcontext(ctx, JVM_ENV + CTX_SLEE);

		Util.createSubcontext(ctx, "resources");
		Util.createSubcontext(ctx, "container");
		Util.createSubcontext(ctx, "facilities");
		Util.createSubcontext(ctx, "sbbs");
		ctx = Util.createSubcontext(ctx, "nullactivity");
		Util.createSubcontext(ctx, "factory");
		Util.createSubcontext(ctx, "nullactivitycontextinterfacefactory");
	}

	/*
	 * Register property editors for the composite SLEE types so that the jboss
	 * jmx console can pass it as an argument.
	 */
	protected void registerPropertyEditors() {
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
	 * Return the MBeanServer that the SLEEE is registers with in the current
	 * JVM
	 * 
	 */
	public MBeanServer getMBeanServer() {
		return mbeanServer;
	}

	public int getRmiRegistryPort() throws Exception {
		Integer port = (Integer) mbeanServer.getAttribute(new ObjectName(
				"slee:service=SleeTCKWrapper"), "RMIRegistryPort");
		return port.intValue();
	}

	/**
	 * @return
	 */
	public DeployableUnitDescriptor[] getDeployableUnitDescriptors()
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("getDeployableUnitDescriptors() ");
		}
		boolean b = SleeContainer.getTransactionManager().requireTransaction();

		try {
			Object[] descriptors = this.getDeploymentManager()
					.getDeployableUnitIDtoDescriptorMap().values().toArray();

			DeployableUnitDescriptor[] retval = new DeployableUnitDescriptor[descriptors.length];
			for (int i = 0; i < descriptors.length; i++) {

				retval[i] = (DeployableUnitDescriptor) descriptors[i];
			}
			return retval;

		} finally {
			try {
				if (b)
					getTransactionManager().commit();

			} catch (SystemException ex) {
				throw new RuntimeException("tx maanger failed ", ex);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("getDeployableUnitDescriptors() exit");
			}
		}
	}

	/**
	 * Return a list of component descriptors for the given list of component
	 * ids.
	 * 
	 * @param componentIds
	 * 
	 * @return an array of component descriptors.
	 * 
	 */
	public ComponentDescriptor[] getDescriptors(ComponentID[] componentIds) {

		boolean b = getTransactionManager().requireTransaction();
		try {
			ArrayList knownComps = new ArrayList();
			for (int i = 0; i < componentIds.length; i++) {
				if (isInstalled(componentIds[i])) {
					// may be too strict (spec says 'recognized', 14.7.8, p214)

					ComponentDescriptor descr = this
							.getComponentDescriptor(componentIds[i]);
					knownComps.add(descr);

				}
			}
			ComponentDescriptor[] components = new ComponentDescriptor[knownComps
					.size()];
			knownComps.toArray(components);
			return components;
		} finally {
			try {
				if (b)
					getTransactionManager().commit();

			} catch (SystemException ex) {
				throw new RuntimeException("tx maanger failed ", ex);
			}

		}
	}

	public static boolean isSecurityEnabled() {
		return isSecurityEnabled;
	}

	public String dumpState() {
		return eventManagement.toString() + "\n" + resourceManagement + "\n"
				+ sbbPoolManagement + "\n" + timerFacility + "\n"
				+ deploymentCacheManager + "\n" + activityContextFactory + "\n"
				+ activityContextNamingFacility + "\n" + nullActivityFactory
				+ "\n" + serviceManagement + "\n" + profileFacility;
	}
}
