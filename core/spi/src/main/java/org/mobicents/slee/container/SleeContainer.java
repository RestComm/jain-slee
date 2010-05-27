package org.mobicents.slee.container;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

import javax.management.MBeanServer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.management.SleeState;

import org.apache.log4j.Logger;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.util.naming.Util;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VFSUtils;
import org.mobicents.cache.MobicentsCache;
import org.mobicents.cluster.MobicentsCluster;
import org.mobicents.slee.connector.local.MobicentsSleeConnectionFactory;
import org.mobicents.slee.connector.local.SleeConnectionService;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.classloading.ReplicationClassLoader;
import org.mobicents.slee.container.component.du.DeployableUnitManagement;
import org.mobicents.slee.container.congestion.CongestionControl;
import org.mobicents.slee.container.event.EventContextFactory;
import org.mobicents.slee.container.eventrouter.EventRouter;
import org.mobicents.slee.container.facilities.ActivityContextNamingFacility;
import org.mobicents.slee.container.facilities.TimerFacility;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityContextInterfaceFactory;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityFactory;
import org.mobicents.slee.container.management.AlarmManagement;
import org.mobicents.slee.container.management.ComponentManagement;
import org.mobicents.slee.container.management.ProfileManagement;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.SbbManagement;
import org.mobicents.slee.container.management.ServiceManagement;
import org.mobicents.slee.container.management.TraceManagement;
import org.mobicents.slee.container.management.UsageParametersManagement;
import org.mobicents.slee.container.management.jmx.editors.SleePropertyEditorRegistrator;
import org.mobicents.slee.container.rmi.RmiServerInterface;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.util.JndiRegistrationManager;

/**
 * Implements the SleeContainer. The SleeContainer is the anchor for the SLEE.
 * It is the central location from where all container modules are accessible.
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author Emil Ivov
 * @author Tim Fox
 * @author eduardomartins
 */
public class SleeContainer {

	private final static Logger logger = Logger.getLogger(SleeContainer.class);

	static {
		// Force this property to allow invocation of getters.
		// http://code.google.com/p/mobicents/issues/detail?id=63
		System.setProperty("jmx.invoke.getters", "true");
		// establish the location of mobicents in JBoss AS deploy folder
		try {
			java.net.URL url = VFSUtils.getCompatibleURL(VFS
					.getRoot(SleeContainer.class.getClassLoader().getResource(
							"..")));
			java.net.URI uri = new java.net.URI(url.toExternalForm()
					.replaceAll(" ", "%20"));
			deployPath = new File(uri).getAbsolutePath();
		} catch (Exception e) {
			logger
					.error(
							"Failed to establish path to Mobicents root deployment directory",
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
	
	private static SleeContainer sleeContainer;

	/**
	 * 
	 * @return the full file system path where mobicents.sar is located
	 */
	public static String getDeployPath() {
		return deployPath;
	}

	
	private ArrayList<SleeContainerModule> modules = new ArrayList<SleeContainerModule>();
	
	private final SleeTransactionManager sleeTransactionManager;
	private final MobicentsCluster cluster;
	// mbean server where the container's mbeans are registred
	private final MBeanServer mbeanServer;
	/** The lifecycle state of the SLEE */
	private SleeState sleeState;

	// the class that actually posts events to the SBBs.
	// This should be made into a facility and registered with jmx and jndi
	// so it can be independently controlled.
	private final EventRouter router;
	// monitor object for sync on management operations
	private final Object managementMonitor = new Object();
	// for external access to slee
	@SuppressWarnings("unused")
	private RmiServerInterface rmiServerInterfaceMBean;
	// component managers
	private final ComponentManagement componentManagement;
	private final ServiceManagement serviceManagement;

	private final ProfileManagement sleeProfileTableManager;
	private final ResourceManagement resourceManagement;
	private final SbbManagement sbbManagement;
	// object pool management
	// non clustered scheduler
	private final ScheduledExecutorService nonClusteredScheduler;
	private static final int NON_CLUSTERED_SCHEDULER_THREADS = 4;
	// slee factories
	private final ActivityContextFactory activityContextFactory;
	private final NullActivityContextInterfaceFactory nullActivityContextInterfaceFactory;

	private final NullActivityFactory nullActivityFactory;

	// slee facilities
	private final ActivityContextNamingFacility activityContextNamingFacility;

	private final AlarmManagement alarmMBeanImpl;

	private final TraceManagement traceMBeanImpl;

	private final TimerFacility timerFacility;

	private final MobicentsUUIDGenerator uuidGenerator;

	private final UsageParametersManagement usageParametersManagement;

	private ReplicationClassLoader replicationClassLoader;

	private final SbbEntityFactory sbbEntityFactory;

	private final EventContextFactory eventContextFactory;

	private final MobicentsCache localCache; 
	
	private final CongestionControl congestionControl;
	
	private final SleeConnectionService sleeConnectionService;
 	
	private final MobicentsSleeConnectionFactory sleeConnectionFactory; 

	
	/**
	 * Creates a new instance of SleeContainer -- This is called from the
	 * SleeManagementMBean to get the whole thing running.
	 * 
	 */
	public SleeContainer(
			ComponentManagement componentManagement,
			SbbManagement sbbManagement,
			ServiceManagement serviceManagement,
			ResourceManagement resourceManagement,
			ProfileManagement profileManagement,
			EventContextFactory eventContextFactory,
			EventRouter eventRouter,
			TimerFacility timerFacility,
			ActivityContextFactory activityContextFactory,
			ActivityContextNamingFacility activityContextNamingFacility,
			NullActivityContextInterfaceFactory nullActivityContextInterfaceFactory,
			NullActivityFactory nullActivityFactory,
			RmiServerInterface rmiServerInterface,
			SleeTransactionManager sleeTransactionManager,
			MobicentsCluster cluster, MobicentsCache localCache, AlarmManagement alarmMBeanImpl,
			TraceManagement traceMBeanImpl,
			UsageParametersManagement usageParametersManagement,
			SbbEntityFactory sbbEntityFactory, CongestionControl congestionControl,
			SleeConnectionService sleeConnectionService, MobicentsSleeConnectionFactory sleeConnectionFactory) throws Exception {
		
		// created in STOPPED state and remain so until started
		this.sleeState = SleeState.STOPPED;
		this.mbeanServer = MBeanServerLocator.locateJBoss();

		this.sleeTransactionManager = sleeTransactionManager;
		addModule(sleeTransactionManager);

		this.componentManagement = componentManagement;
		addModule(componentManagement);

		this.replicationClassLoader = componentManagement
				.getClassLoaderFactory().newReplicationClassLoader(
						this.getClass().getClassLoader());

		this.cluster = cluster;
		cluster.getMobicentsCache().setReplicationClassLoader(
				this.replicationClassLoader);
		this.localCache = localCache;

		this.uuidGenerator = new MobicentsUUIDGenerator(cluster
				.getMobicentsCache().isLocalMode());

		this.alarmMBeanImpl = alarmMBeanImpl;
		addModule(alarmMBeanImpl);

		this.traceMBeanImpl = traceMBeanImpl;
		addModule(traceMBeanImpl);

		this.usageParametersManagement = usageParametersManagement;
		addModule(usageParametersManagement);

		this.serviceManagement = serviceManagement;
		addModule(serviceManagement);

		this.sbbManagement = sbbManagement;
		addModule(sbbManagement);

		this.resourceManagement = resourceManagement;
		addModule(resourceManagement);

		this.sleeProfileTableManager = profileManagement;
		addModule(sleeProfileTableManager);

		this.activityContextFactory = activityContextFactory;
		addModule(activityContextFactory);

		this.activityContextNamingFacility = activityContextNamingFacility;
		addModule(activityContextNamingFacility);

		this.nullActivityFactory = nullActivityFactory;
		addModule(nullActivityFactory);

		this.nullActivityContextInterfaceFactory = nullActivityContextInterfaceFactory;
		addModule(nullActivityContextInterfaceFactory);

		this.timerFacility = timerFacility;
		addModule(timerFacility);

		this.eventContextFactory = eventContextFactory;
		addModule(eventContextFactory);

		this.router = eventRouter;
		addModule(router);

		this.rmiServerInterfaceMBean = rmiServerInterface;
		addModule(rmiServerInterface);

		this.nonClusteredScheduler = new ScheduledThreadPoolExecutor(
				NON_CLUSTERED_SCHEDULER_THREADS);

		this.sbbEntityFactory = sbbEntityFactory;
		addModule(sbbEntityFactory);

		this.congestionControl = congestionControl;
		addModule(congestionControl);
		
		this.sleeConnectionService = sleeConnectionService;
		addModule(sleeConnectionService);
		
		this.sleeConnectionFactory = sleeConnectionFactory;
		addModule(sleeConnectionFactory);
	}

	private void addModule(SleeContainerModule module) {
		modules.add(module);
		module.setSleeContainer(this);
	}

	// GETTERS -- managers

	/**
	 * dumps the container state as a string, useful for debug/profiling
	 * 
	 * @return
	 */
	public String dumpState() {
		return componentManagement + "\n" + resourceManagement + "\n"
				+ timerFacility + "\n" + traceMBeanImpl + "\n"
				+ sleeProfileTableManager + "\n" + activityContextFactory
				+ "\n" + activityContextNamingFacility + "\n"
				+ nullActivityFactory + "\n" + getEventRouter() + "\n"
				+ getTransactionManager();
	}

	public ActivityContextFactory getActivityContextFactory() {
		return this.activityContextFactory;
	}

	public ActivityContextNamingFacility getActivityContextNamingFacility() {
		return activityContextNamingFacility;
	}

	/**
	 * 
	 * @return
	 */
	public AlarmManagement getAlarmManagement() {
		return alarmMBeanImpl;
	}

	/**
	 * The cache which manages the container's HA and FT data
	 * 
	 * @return
	 */
	public MobicentsCluster getCluster() {
		return cluster;
	}

	/**
	 * 
	 * @return
	 */
	public MobicentsCache getLocalCache() {
		return localCache;
	}
	
	/**
	 * @return the componentManagement
	 */
	public ComponentManagement getComponentManagement() {
		return componentManagement;
	}

	/**
	 * retrieves the container's component repository implementation
	 * 
	 * @return
	 */
	public ComponentRepository getComponentRepository() {
		return componentManagement.getComponentRepository();
	}

	/**
	 * 
	 * @return
	 */
	public CongestionControl getCongestionControl() {
		return congestionControl;
	}
	
	/**
	 * Retrieves the deployable unit manager
	 * 
	 * @return
	 */
	public DeployableUnitManagement getDeployableUnitManagement() {
		return componentManagement.getDeployableUnitManagement();
	}

	/**
	 * @return the eventContextFactory
	 */
	public EventContextFactory getEventContextFactory() {
		return eventContextFactory;
	}

	/**
	 * the container's event router
	 */
	public EventRouter getEventRouter() {
		return this.router;
	}

	/**
	 * object for synchronization on management operations that (un)install
	 * components
	 */
	public Object getManagementMonitor() {
		return managementMonitor;
	}

	// GETTERS -- slee factories

	/**
	 * Return the MBeanServer that the SLEEE is registers with in the current
	 * JVM
	 */
	public MBeanServer getMBeanServer() {
		return mbeanServer;
	}

	/**
	 * Retrieves the container's non clustered scheduler.
	 * 
	 * @return the nonClusteredScheduler
	 */
	public ScheduledExecutorService getNonClusteredScheduler() {
		return nonClusteredScheduler;
	}

	public NullActivityContextInterfaceFactory getNullActivityContextInterfaceFactory() {
		return this.nullActivityContextInterfaceFactory;
	}

	/**
	 * 
	 * @return
	 */
	public MobicentsSleeConnectionFactory getSleeConnectionFactory() {
		return sleeConnectionFactory;
	}
		
	/**
	 * 
	 * @return
	 */
	public SleeConnectionService getSleeConnectionService() {
		return this.sleeConnectionService;
	}
	
	// GETTERS -- slee facilities

	public NullActivityFactory getNullActivityFactory() {
		return this.nullActivityFactory;
	}

	/**
	 * Retrieves the class loader used in data replication.
	 * 
	 * @return
	 */
	public ReplicationClassLoader getReplicationClassLoader() {
		return replicationClassLoader;
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
	 * @return the sbbEntityFactory
	 */
	public SbbEntityFactory getSbbEntityFactory() {
		return sbbEntityFactory;
	}

	// GETTERS -- slee runtime

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

	public ProfileManagement getSleeProfileTableManager() {
		return this.sleeProfileTableManager;
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
	 * 
	 * @return
	 */
	public TimerFacility getTimerFacility() {
		return timerFacility;
	}

	/**
	 * 
	 * @return
	 */
	public TraceManagement getTraceManagement() {
		return traceMBeanImpl;
	}

	/**
	 * Get the transaction manager
	 * 
	 * @throws
	 */
	public SleeTransactionManager getTransactionManager() {
		return sleeTransactionManager;
	}

	// JNDI RELATED

	/**
	 * 
	 * @return
	 */
	public UsageParametersManagement getUsageParametersManagement() {
		return usageParametersManagement;
	}

	/**
	 * a UUID generator for the container
	 */
	public MobicentsUUIDGenerator getUuidGenerator() {
		return uuidGenerator;
	}

	
	/**
	 * Set the current state of the Slee Container. CAUTION: Do not invoke this
	 * method directly! Use the SleeManagementMBean to change the Slee State
	 * 
	 * @param SleeState
	 */
	public void setSleeState(SleeState newState) {
		for (SleeContainerModule module : modules) {
			if (newState == SleeState.RUNNING) {
				module.beforeSleeRunning();
			} else if (newState == SleeState.STOPPED) {
				module.sleeStopping();
			}
		}
		this.sleeState = newState;
		for (SleeContainerModule module : modules) {
			if (newState == SleeState.RUNNING) {
				module.afterSleeRunning();
			} else if (newState == SleeState.STOPPED) {
				module.sleeStopped();
			}
		}
	}

	/**
	 * 
	 * Cleanup in the reverse order of init()
	 * 
	 * @throws NamingException
	 * 
	 */
	public void sleeShutdown() throws NamingException {
		for (SleeContainerModule module : modules) {
			module.sleeShutdown();
		}
		unregisterWithJndi();
		Context ctx = new InitialContext();
		Util.unbind(ctx, JVM_ENV + CTX_SLEE);
	}

	/**
	 * Initialization code.
	 * 
	 */
	public void sleeStarting() throws Exception {

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

		registerWithJndi();

		// Register property editors for the composite SLEE types so that the
		// jboss jmx console can pass it as an argument.
		new SleePropertyEditorRegistrator().register();

		for (SleeContainerModule module : modules) {
			module.sleeStarting();
		}
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
		if (sleeContainer == null) {
			try {
				sleeContainer = (SleeContainer) JndiRegistrationManager.getFromJndi("slee/" + JNDI_NAME);					
			} catch (Throwable ex) {
				logger.error("Unexpected error: Cannot retrieve SLEE Container!",ex);					
			}			
		}
		return sleeContainer;		
	}

	private void registerWithJndi() {
		JndiRegistrationManager.registerWithJndi("slee", JNDI_NAME, this);
		sleeContainer = this;
	}

	private void unregisterWithJndi() {
		JndiRegistrationManager.unregisterWithJndi("slee/" + JNDI_NAME);
	}

}