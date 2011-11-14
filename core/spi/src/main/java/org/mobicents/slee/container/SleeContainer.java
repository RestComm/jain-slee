/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

import javax.management.MBeanServer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.InvalidStateException;
import javax.slee.management.SleeState;

import org.apache.log4j.Logger;
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
import org.mobicents.slee.container.deployment.SleeContainerDeployer;
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
import org.mobicents.slee.container.management.SleeStateChangeRequest;
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
			deployPath = new File(new File(uri).getParent()).getAbsolutePath();
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

	
	private LinkedList<SleeContainerModule> modules = new LinkedList<SleeContainerModule>();
	
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

	private final CongestionControl congestionControl;
	
	private final SleeConnectionService sleeConnectionService;
 	
	private final MobicentsSleeConnectionFactory sleeConnectionFactory; 

	private final SleeContainerDeployer deployer;
	
	/**
	 * Creates a new instance of SleeContainer -- This is called from the
	 * SleeManagementMBean to get the whole thing running.
	 * 
	 */
	public SleeContainer(
			MBeanServer mBeanServer,
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
			MobicentsCluster cluster, AlarmManagement alarmMBeanImpl,
			TraceManagement traceMBeanImpl,
			UsageParametersManagement usageParametersManagement,
			SbbEntityFactory sbbEntityFactory, CongestionControl congestionControl,
			SleeConnectionService sleeConnectionService, MobicentsSleeConnectionFactory sleeConnectionFactory, SleeContainerDeployer sleeContainerDeployer) throws Exception {
		
		this.mbeanServer = mBeanServer;

		this.sleeTransactionManager = sleeTransactionManager;
		addModule(sleeTransactionManager);

		this.componentManagement = componentManagement;
		addModule(componentManagement);

		this.replicationClassLoader = componentManagement
				.getClassLoaderFactory().newReplicationClassLoader(
						this.getClass().getClassLoader());

		this.cluster = cluster;

		this.uuidGenerator = new MobicentsUUIDGenerator(cluster
				.getMobicentsCache().isLocalMode());

		this.alarmMBeanImpl = alarmMBeanImpl;
		addModule(alarmMBeanImpl);

		this.traceMBeanImpl = traceMBeanImpl;
		addModule(traceMBeanImpl);

		this.usageParametersManagement = usageParametersManagement;
		addModule(usageParametersManagement);
				
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

		// these must be the last ones to be notified of startup, and in this
		// order
		this.resourceManagement = resourceManagement;
		addModule(resourceManagement);

		this.sbbManagement = sbbManagement;
		addModule(sbbManagement);

		this.serviceManagement = serviceManagement;
		addModule(serviceManagement);	
		
		this.sleeProfileTableManager = profileManagement;
		addModule(sleeProfileTableManager);

		this.deployer = sleeContainerDeployer;
		addModule(sleeContainerDeployer);	

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
				+ nullActivityFactory + "\n" 
				+ getEventRouter() + "\n"
				+ getEventContextFactory() + "\n"
				+ getTransactionManager() + "\n"
				+ cluster.getMobicentsCache().getCacheContent();
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
	public SleeContainerDeployer getDeployer() {
		return deployer;
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
	 * Initiates the SLEE container
	 */
	public void initSlee() throws InvalidStateException {
		if (sleeState != null) {
			throw new InvalidStateException("slee in "+sleeState+" state");
		}
		// slee init
		beforeModulesInitialization();
		for (Iterator<SleeContainerModule> i = modules.iterator(); i
			.hasNext();) {
			i.next().sleeInitialization();
		}
		afterModulesInitialization();
		sleeState = SleeState.STOPPED;		
	}
	
	/**
	 * Shutdown of the SLEE container
	 * @throws InvalidStateException
	 */
	public void shutdownSlee() throws InvalidStateException {
		if (sleeState != SleeState.STOPPED) {
			throw new InvalidStateException("slee in "+sleeState+" state");
		}
		// slee shutdown
		beforeModulesShutdown();
		for (Iterator<SleeContainerModule> i = modules
				.descendingIterator(); i.hasNext();) {
			i.next().sleeShutdown();
		}
		afterModulesShutdown();
		sleeState = null;
	}

	/**
	 * 
	 * @param newState
	 */
	public void setSleeState(final SleeStateChangeRequest request) throws InvalidStateException {

		final SleeState newState = request.getNewState();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Changing state: " + sleeState + " -> " + newState);
		}

		validateStateTransition(sleeState, newState);

		// change state
		SleeState oldState = this.sleeState;
		this.sleeState = newState;
		request.stateChanged(oldState);
		
		// notify modules and complete request
		final Runnable task = new Runnable() {			
			@Override
			public void run() {
				try {
					if (newState == SleeState.STARTING) {
						for (Iterator<SleeContainerModule> i = modules.iterator(); i.hasNext();) {
							i.next().sleeStarting();
						}						
					}
					else if (newState == SleeState.RUNNING) {
						for (Iterator<SleeContainerModule> i = modules.iterator(); i.hasNext();) {
							i.next().sleeRunning();
						}
					}
					else if (newState == SleeState.STOPPING) {
						for (Iterator<SleeContainerModule> i = modules.descendingIterator(); i.hasNext();) {
							i.next().sleeStopping();
						}
					}
					else if (newState == SleeState.STOPPED) {
						for (Iterator<SleeContainerModule> i=modules.descendingIterator(); i.hasNext();) {
							i.next().sleeStopped();
						}			
					}	
				}
				catch (Throwable e) {
					logger.error(e.getMessage(),e);
				}
				request.requestCompleted();
			}							
		};
		if (request.isBlockingRequest()) {
			task.run();
		}
		else {
			final ExecutorService executorService = Executors.newSingleThreadExecutor();
			try {
				executorService.submit(task);				
			}
			catch (Throwable e) {
				logger.error(e.getMessage(),e);
			}
			executorService.shutdown();
		}		
	}
	
	/**
	 * Ensures the standard SLEE lifecycle.
	 * 
	 * @param oldState
	 * @param newState
	 * @throws InvalidStateException
	 */
	private void validateStateTransition(SleeState oldState, SleeState newState)
			throws InvalidStateException {
		if (oldState == SleeState.STOPPED) {
			if (newState == SleeState.STARTING) {
				return;
			}		
		} else if (oldState == SleeState.STARTING) {
			if (newState == SleeState.RUNNING || newState == SleeState.STOPPING) {
				return;
			}
		} else if (oldState == SleeState.RUNNING) {
			if (newState == SleeState.STOPPING) {
				return;
			}
		} else if (oldState == SleeState.STOPPING) {
			if (newState == SleeState.STOPPED) {
				return;
			}
		}
		throw new InvalidStateException("illegal slee state transition: " + oldState + " -> "+ newState);
	}
	
	public void beforeModulesInitialization() {
		try {
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

		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}		
	}
	
	public void afterModulesInitialization() {
		if (!cluster.getMobicentsCache().isLocalMode()) {
			cluster.getMobicentsCache().setReplicationClassLoader(
				this.replicationClassLoader);
		}
		// start cluster
		cluster.startCluster();
	}
	
	public void beforeModulesShutdown() {
		// stop the cluster
		cluster.stopCluster();
	}
	
	public void afterModulesShutdown() {		
		try {
			unregisterWithJndi();
			Context ctx = new InitialContext();
			Util.unbind(ctx, JVM_ENV + CTX_SLEE);
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
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