package org.telestax.slee.container.build.as7.service;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.transaction.TransactionManager;

import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.RuntimeConfig;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.mobicents.cache.MobicentsCache;
import org.mobicents.cluster.DefaultMobicentsCluster;
import org.mobicents.cluster.MobicentsCluster;
import org.mobicents.slee.connector.local.MobicentsSleeConnectionFactory;
import org.mobicents.slee.connector.local.SleeConnectionService;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.component.ComponentManagementImpl;
import org.mobicents.slee.container.congestion.CongestionControl;
import org.mobicents.slee.container.congestion.CongestionControlImpl;
import org.mobicents.slee.container.deployment.ExternalDeployer;
import org.mobicents.slee.container.deployment.SleeContainerDeployer;
import org.mobicents.slee.container.deployment.jboss.SleeContainerDeployerImpl;
import org.mobicents.slee.container.event.DefaultEventContextFactoryDataSource;
import org.mobicents.slee.container.event.EventContextFactory;
import org.mobicents.slee.container.event.EventContextFactoryDataSource;
import org.mobicents.slee.container.event.EventContextFactoryImpl;
import org.mobicents.slee.container.eventrouter.EventRouter;
import org.mobicents.slee.container.facilities.ActivityContextNamingFacility;
import org.mobicents.slee.container.facilities.TimerFacility;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityContextInterfaceFactory;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityFactory;
import org.mobicents.slee.container.management.ComponentManagement;
import org.mobicents.slee.container.management.ProfileManagement;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.ResourceManagementImpl;
import org.mobicents.slee.container.management.SbbManagement;
import org.mobicents.slee.container.management.SbbManagementImpl;
import org.mobicents.slee.container.management.ServiceManagement;
import org.mobicents.slee.container.management.ServiceManagementImpl;
import org.mobicents.slee.container.management.UsageParametersManagement;
import org.mobicents.slee.container.management.UsageParametersManagementImpl;
import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;
import org.mobicents.slee.container.management.jmx.CongestionControlConfiguration;
import org.mobicents.slee.container.management.jmx.DeploymentMBeanImpl;
import org.mobicents.slee.container.management.jmx.DeploymentMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.EventContextFactoryConfiguration;
import org.mobicents.slee.container.management.jmx.EventRouterConfiguration;
import org.mobicents.slee.container.management.jmx.EventRouterStatistics;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;
import org.mobicents.slee.container.management.jmx.SleeManagementMBeanImpl;
import org.mobicents.slee.container.management.jmx.SleeManagementMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.TimerFacilityConfiguration;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.container.rmi.RmiServerInterface;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.activity.ActivityManagementConfiguration;
import org.mobicents.slee.runtime.eventrouter.EventRouterImpl;
import org.mobicents.slee.runtime.eventrouter.mapping.ActivityHashingEventRouterExecutorMapper;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactoryImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManagerImpl;
import org.telestax.slee.container.build.as7.deployment.ExternalDeployerImpl;
import org.telestax.slee.container.build.as7.naming.JndiManagementImpl;

public class SleeContainerService implements Service<SleeContainer> {

	Logger log = Logger.getLogger(SleeContainerService.class);

	// TODO obtain real path through
	// org.jboss.as.controller.services.path.PathManager (see WebServerService)
	// or expression resolve ?
	private static final String TEMP_DIR = "jboss.server.temp.dir";

	private final InjectedValue<MBeanServer> mbeanServer = new InjectedValue<MBeanServer>();
	private final InjectedValue<TransactionManager> transactionManager = new InjectedValue<TransactionManager>();

	private SleeContainer sleeContainer;

	@Override
	public SleeContainer getValue() throws IllegalStateException,
			IllegalArgumentException {
		return sleeContainer;
	}

	@Override
	public void start(StartContext context) throws StartException {
		log.info("Starting SLEE Container service");

		String deployPath = System.getProperty(TEMP_DIR) + "/slee";

		// inits the SLEE cache and cluster
		MobicentsCache cache = initCache();
		MobicentsCluster cluster = new DefaultMobicentsCluster(cache,
				getTransactionManager().getValue(), null);

		// init the tx manager
		SleeTransactionManager sleeTransactionManager = new SleeTransactionManagerImpl(
				getTransactionManager().getValue());

		// TODO register trace mbean
		TraceMBeanImpl traceMBean = new TraceMBeanImpl();

		// TODO register alarm mbean
		AlarmMBeanImpl alarmMBean = new AlarmMBeanImpl(traceMBean);		

		MobicentsManagement mobicentsManagement = new MobicentsManagement();
		mobicentsManagement.setEntitiesRemovalDelay(1);

		ComponentManagement componentManagement = new ComponentManagementImpl();

		SbbManagement sbbManagement = new SbbManagementImpl();

		ServiceManagement serviceManagement = new ServiceManagementImpl();

		ResourceManagement resourceManagement = ResourceManagementImpl
				.getInstance();

		// TODO init profile management
		ProfileManagement profileManagement = null;

		// TODO register mbean
		EventRouterConfiguration eventRouterConfiguration = new EventRouterConfiguration();
		eventRouterConfiguration.setEventRouterThreads(8);
		eventRouterConfiguration.setCollectStats(true);
		eventRouterConfiguration.setConfirmSbbEntityAttachement(true);
		try {
			eventRouterConfiguration
					.setExecutorMapperClassName(ActivityHashingEventRouterExecutorMapper.class
							.getName());
		} catch (ClassNotFoundException e) {
			throw new StartException(e);
		}
		EventRouter eventRouter = new EventRouterImpl(eventRouterConfiguration);
		// TODO register mbean
		EventRouterStatistics eventRouterStatistics = new EventRouterStatistics(
				eventRouter);

		// TODO register mbean
		TimerFacilityConfiguration timerFacilityConfiguration = new TimerFacilityConfiguration();
		timerFacilityConfiguration.setTimerThreads(4);
		timerFacilityConfiguration.setPurgePeriod(0);
		timerFacilityConfiguration
				.setTaskExecutionWaitsForTxCommitConfirmation(true);
		TimerFacility timerFacility = new TimerFacilityImpl(
				timerFacilityConfiguration);

		ActivityManagementConfiguration activityManagementConfiguration = new ActivityManagementConfiguration();
		activityManagementConfiguration.setTimeBetweenLivenessQueries(60);
		activityManagementConfiguration.setMaxTimeIdle(60);
		activityManagementConfiguration.setMinTimeBetweenUpdates(15);
		ActivityContextFactory activityContextFactory = new ActivityContextFactoryImpl(
				activityManagementConfiguration);

		NullActivityContextInterfaceFactory nullActivityContextInterfaceFactory = new NullActivityContextInterfaceFactoryImpl();
		NullActivityFactory nullActivityFactory = new NullActivityFactoryImpl();

		ActivityContextNamingFacility activityContextNamingFacility = new ActivityContextNamingFacilityImpl();

		// TODO SLEE Connection Factory + RMI stuff
		SleeConnectionService sleeConnectionService = null;
		MobicentsSleeConnectionFactory sleeConnectionFactory = null;
		RmiServerInterface rmiServerInterface = null;

		UsageParametersManagement usageParametersManagement = new UsageParametersManagementImpl();

		SbbEntityFactory sbbEntityFactory = new SbbEntityFactoryImpl();

		EventContextFactoryDataSource eventContextFactoryDataSource = new DefaultEventContextFactoryDataSource();
		// TODO register MBean
		EventContextFactoryConfiguration eventContextFactoryConfiguration = new EventContextFactoryConfiguration();
		eventContextFactoryConfiguration
				.setDefaultEventContextSuspensionTimeout(10000);
		EventContextFactory eventContextFactory = new EventContextFactoryImpl(
				eventContextFactoryDataSource, eventContextFactoryConfiguration);

		// TODO register MBean
		CongestionControlConfiguration congestionControlConfiguration = new CongestionControlConfiguration();
		congestionControlConfiguration.setPeriodBetweenChecks(0);
		congestionControlConfiguration.setMinFreeMemoryToTurnOn(10);
		congestionControlConfiguration.setMinFreeMemoryToTurnOff(20);
		congestionControlConfiguration.setRefuseStartActivity(true);
		congestionControlConfiguration.setRefuseFireEvent(false);
		CongestionControl congestionControl = new CongestionControlImpl(
				congestionControlConfiguration);

		// FIXME deployer stuff
		ExternalDeployer externalDeployer = new ExternalDeployerImpl();
		SleeContainerDeployerImpl internalDeployer = new SleeContainerDeployerImpl();
		internalDeployer.setExternalDeployer(externalDeployer);

		try {
			sleeContainer = new SleeContainer(deployPath, getMbeanServer()
					.getValue(), componentManagement, sbbManagement,
					serviceManagement, resourceManagement, profileManagement,
					eventContextFactory, eventRouter, timerFacility,
					activityContextFactory, activityContextNamingFacility,
					nullActivityContextInterfaceFactory, nullActivityFactory,
					rmiServerInterface, sleeTransactionManager, cluster,
					alarmMBean, traceMBean, usageParametersManagement,
					sbbEntityFactory, congestionControl, sleeConnectionService,
					sleeConnectionFactory, internalDeployer);
		} catch (Throwable e) {
			throw new StartException(e);
		}

		// set AS7+ Jndi Management 
		sleeContainer.setJndiManagement(new JndiManagementImpl());
		
		// deployment mbean
		try {
			getMbeanServer().getValue().registerMBean(new DeploymentMBeanImpl(internalDeployer), new ObjectName(DeploymentMBeanImplMBean.OBJECT_NAME));
		} catch (Throwable e) {
			throw new StartException(e);
		}

		// slee management mbean
		try {
			getMbeanServer().getValue().registerMBean(
					new SleeManagementMBeanImpl(sleeContainer), new ObjectName(SleeManagementMBeanImplMBean.OBJECT_NAME));
		} catch (Throwable e) {
			throw new StartException(e);
		}

	}

	private MobicentsCache initCache() {
		RuntimeConfig runtimeConfig = new RuntimeConfig();
		runtimeConfig.setTransactionManager(getTransactionManager().getValue());
		Configuration configuration = new Configuration();
		configuration.setRuntimeConfig(runtimeConfig);
		configuration.setCacheMode("LOCAL");
		configuration.setLockAcquisitionTimeout(3000);
		configuration.setUseLockStriping(false);
		configuration.setExposeManagementStatistics(false);
		configuration.setShutdownHookBehavior("DONT_REGISTER");
		return new MobicentsCache(configuration);
	}

	@Override
	public void stop(StopContext context) {
		// shutdown the SLEE
		try {
			getMbeanServer().getValue().unregisterMBean(
					new ObjectName(SleeManagementMBeanImplMBean.OBJECT_NAME));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		sleeContainer = null;
	}

	public InjectedValue<MBeanServer> getMbeanServer() {
		return mbeanServer;
	}

	public InjectedValue<TransactionManager> getTransactionManager() {
		return transactionManager;
	}
}
