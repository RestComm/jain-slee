package org.telestax.slee.container.build.as7.service;

import org.jboss.as.naming.ManagedReferenceFactory;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.configuration.global.ShutdownHookBehavior;
import org.jboss.logging.Logger;
import org.jboss.msc.service.*;
import org.jboss.msc.value.InjectedValue;
import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;
import org.mobicents.slee.connector.local.MobicentsSleeConnectionFactory;
import org.mobicents.slee.connector.local.SleeConnectionService;
import org.mobicents.slee.connector.local.SleeConnectionServiceImpl;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.component.ComponentManagementImpl;
import org.mobicents.slee.container.congestion.CongestionControl;
import org.mobicents.slee.container.congestion.CongestionControlImpl;
import org.mobicents.slee.container.deployment.ExternalDeployer;
import org.mobicents.slee.container.deployment.jboss.DeploymentManagerMBeanImpl;
import org.mobicents.slee.container.deployment.jboss.DeploymentManagerMBeanImplMBean;
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
import org.mobicents.slee.container.management.*;
import org.mobicents.slee.container.management.jmx.*;
import org.mobicents.slee.container.remote.RmiServerInterfaceImpl;
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
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.DefaultMobicentsCluster;
import org.restcomm.cluster.MobicentsCluster;
import org.telestax.slee.container.build.as7.deployment.ExternalDeployerImpl;
import org.telestax.slee.container.build.as7.deployment.SleeDeploymentMetaData;
import org.telestax.slee.container.build.as7.naming.JndiManagementImpl;

import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.slee.management.*;
import javax.transaction.TransactionManager;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.Executors;

public class SleeContainerService implements Service<SleeContainer> {

	Logger log = Logger.getLogger(SleeContainerService.class);

	// TODO obtain real path through
	// org.jboss.as.controller.services.path.PathManager (see WebServerService)
	// or expression resolve ?
	private static final String TEMP_DIR = "jboss.server.temp.dir";

	private String rmiAddress;
	private int rmiPort;
	private boolean persistProfiles;
	private boolean clusteredProfiles;
	private String hibernateDatasource;
	private String hibernateDialect;

	private final InjectedValue<MBeanServer> mbeanServer = new InjectedValue<MBeanServer>();
	private final InjectedValue<TransactionManager> transactionManager = new InjectedValue<TransactionManager>();
	private final InjectedValue<ManagedReferenceFactory> managedReferenceFactory = new InjectedValue<ManagedReferenceFactory>();
	private final LinkedList<String> registeredMBeans = new LinkedList<String>();
	
	private SleeContainer sleeContainer;

	private ExternalDeployer externalDeployer;
	public ExternalDeployer getExternalDeployer() {
		return externalDeployer;
	}

	public SleeContainerService(
			String rmiAddress, int rmiPort,
			boolean persistProfiles, boolean clusteredProfiles,
			String hibernateDatasource, String hibernateDialect) {
		this.rmiAddress = rmiAddress;
		this.rmiPort = rmiPort;
		this.persistProfiles = persistProfiles;
		this.clusteredProfiles = clusteredProfiles;
		this.hibernateDatasource = hibernateDatasource;
		this.hibernateDialect = hibernateDialect;
	}

	@Override
	public SleeContainer getValue() throws IllegalStateException,
			IllegalArgumentException {
		return sleeContainer;
	}

	@Override
	public void start(StartContext context) throws StartException {
		log.info("Starting SLEE Container service");

		final String deployPath = System.getProperty(TEMP_DIR) + "/slee";

		this.externalDeployer = new ExternalDeployerImpl();
		final SleeContainerDeployerImpl internalDeployer = new SleeContainerDeployerImpl();
		internalDeployer.setExternalDeployer(this.externalDeployer);

		// inits the SLEE cache and cluster
		final MobicentsCache cache = initCache();
		final MobicentsCluster cluster = new DefaultMobicentsCluster(cache,
				getTransactionManager().getValue(), null);

		// init the tx manager
		final SleeTransactionManager sleeTransactionManager = new SleeTransactionManagerImpl(
				getTransactionManager().getValue());

		final TraceMBeanImpl traceMBean = new TraceMBeanImpl();
		
		final AlarmMBeanImpl alarmMBean = new AlarmMBeanImpl(traceMBean);		
		
		final MobicentsManagement mobicentsManagement = new MobicentsManagement();
		mobicentsManagement.setEntitiesRemovalDelay(1);

		final ComponentManagement componentManagement = new ComponentManagementImpl();

		final SbbManagement sbbManagement = new SbbManagementImpl();

		final ServiceManagementImpl serviceManagement = new ServiceManagementImpl();

		final ResourceManagementImpl resourceManagement = ResourceManagementImpl.getInstance();

		final org.mobicents.slee.container.deployment.profile.jpa.Configuration
				profileConfiguration = new org.mobicents.slee.container.deployment.profile.jpa.Configuration();

		// TODO: ExtensionConfiguration for Profile Management
		profileConfiguration.setPersistProfiles(this.persistProfiles);
		profileConfiguration.setClusteredProfiles(this.clusteredProfiles);
		profileConfiguration.setHibernateDatasource(this.hibernateDatasource);
		profileConfiguration.setHibernateDialect(this.hibernateDialect);
		final ProfileManagement profileManagement = new ProfileManagementImpl(profileConfiguration);

		// TODO: ExtensionConfiguration for EventRouter
		final EventRouterConfiguration eventRouterConfiguration = new EventRouterConfiguration();
		eventRouterConfiguration.setEventRouterThreads(8);
		eventRouterConfiguration.setCollectStats(true);
		eventRouterConfiguration.setConfirmSbbEntityAttachement(true);
		try {
			eventRouterConfiguration
				.setExecutorMapperClassName(ActivityHashingEventRouterExecutorMapper.class.getName());
		} catch (ClassNotFoundException e) {
			throw new StartException(e);
		}
		final EventRouter eventRouter = new EventRouterImpl(eventRouterConfiguration);

		// TODO: ExtensionConfiguration for TimerFacility
		final TimerFacilityConfiguration timerFacilityConfiguration = new TimerFacilityConfiguration();
		timerFacilityConfiguration.setTimerThreads(4);
		timerFacilityConfiguration.setPurgePeriod(0);
		timerFacilityConfiguration
				.setTaskExecutionWaitsForTxCommitConfirmation(true);
		final TimerFacility timerFacility = new TimerFacilityImpl(
				timerFacilityConfiguration);

		// TODO: ExtensionConfiguration for Activity Management
		final ActivityManagementConfiguration activityManagementConfiguration = new ActivityManagementConfiguration();
		activityManagementConfiguration.setTimeBetweenLivenessQueries(60);
		activityManagementConfiguration.setMaxTimeIdle(60);
		activityManagementConfiguration.setMinTimeBetweenUpdates(15);
		final ActivityContextFactory activityContextFactory = new ActivityContextFactoryImpl(
				activityManagementConfiguration);

		final NullActivityContextInterfaceFactory nullActivityContextInterfaceFactory = new NullActivityContextInterfaceFactoryImpl();
		final NullActivityFactory nullActivityFactory = new NullActivityFactoryImpl();

		final ActivityContextNamingFacility activityContextNamingFacility = new ActivityContextNamingFacilityImpl();

		// TODO SLEE Connection Factory + RMI stuff
		final SleeConnectionService sleeConnectionService = new SleeConnectionServiceImpl();
		final MobicentsSleeConnectionFactory sleeConnectionFactory = null; // new MobicentsSleeConnectionFactoryImpl();
        final RmiServerInterface rmiServerInterface = new RmiServerInterfaceImpl();
		rmiServerInterface.setAddress(this.rmiAddress);
		rmiServerInterface.setPort(this.rmiPort);

		final UsageParametersManagement usageParametersManagement = new UsageParametersManagementImpl();

		final SbbEntityFactory sbbEntityFactory = new SbbEntityFactoryImpl();

		final EventContextFactoryDataSource eventContextFactoryDataSource = new DefaultEventContextFactoryDataSource();
		final EventContextFactoryConfiguration eventContextFactoryConfiguration = new EventContextFactoryConfiguration();
		eventContextFactoryConfiguration
				.setDefaultEventContextSuspensionTimeout(10000);
		final EventContextFactory eventContextFactory = new EventContextFactoryImpl(
				eventContextFactoryDataSource, eventContextFactoryConfiguration);

		// TODO: ExtensionConfiguration for Congestion Control
		final CongestionControlConfiguration congestionControlConfiguration = new CongestionControlConfiguration();
		congestionControlConfiguration.setPeriodBetweenChecks(0);
		congestionControlConfiguration.setMinFreeMemoryToTurnOn(10);
		congestionControlConfiguration.setMinFreeMemoryToTurnOff(20);
		congestionControlConfiguration.setRefuseStartActivity(true);
		congestionControlConfiguration.setRefuseFireEvent(false);
		final CongestionControl congestionControl = new CongestionControlImpl(
				congestionControlConfiguration);

		// FIXME this needs further work on dependencies
		// final PolicyMBeanImpl policyMBeanImpl = new PolicyMBeanImpl();
		// policyMBeanImpl.setUseMPolicy(true);

		ServiceController<?> serviceController = context.getController();
		
		try {
			sleeContainer = new SleeContainer(deployPath, serviceController, getMbeanServer().getValue(),
					componentManagement, sbbManagement, serviceManagement,
					resourceManagement, profileManagement,
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
		
		// register mbeans
		registerMBean(traceMBean, TraceMBean.OBJECT_NAME);
		registerMBean(alarmMBean, AlarmMBean.OBJECT_NAME);
		registerMBean(mobicentsManagement, MobicentsManagementMBean.OBJECT_NAME);
		registerMBean(eventRouterConfiguration, EventRouterConfigurationMBean.OBJECT_NAME);		
		registerMBean(new EventRouterStatistics(eventRouter), EventRouterStatisticsMBean.OBJECT_NAME);
		registerMBean(timerFacilityConfiguration, TimerFacilityConfigurationMBean.OBJECT_NAME);
		registerMBean(eventContextFactoryConfiguration, EventContextFactoryConfigurationMBean.OBJECT_NAME);
		registerMBean(congestionControlConfiguration, CongestionControlConfigurationMBean.OBJECT_NAME);

		final DeploymentManagerMBeanImpl deploymentManagerMBean = new DeploymentManagerMBeanImpl(internalDeployer);
		registerMBean(deploymentManagerMBean, DeploymentManagerMBeanImplMBean.OBJECT_NAME);
		final DeploymentMBeanImpl deploymentMBean = new DeploymentMBeanImpl(internalDeployer);
		registerMBean(deploymentMBean, DeploymentMBean.OBJECT_NAME);
		final ServiceManagementMBeanImpl serviceManagementMBean = new ServiceManagementMBeanImpl(serviceManagement);
		registerMBean(serviceManagementMBean, ServiceManagementMBean.OBJECT_NAME);

		try {
			registerMBean(new ProfileProvisioningMBeanImpl(sleeContainer), ProfileProvisioningMBeanImpl.OBJECT_NAME);
		} catch (NotCompliantMBeanException e) {
			log.error("ProfileProvisioningMBean is not compliant MBean.", e);
		}

		final ResourceManagementMBeanImpl resourceManagementMBean = new ResourceManagementMBeanImpl(resourceManagement);
		registerMBean(resourceManagementMBean, ResourceManagementMBean.OBJECT_NAME);
		final SbbEntitiesMBeanImpl sbbEntitiesMBean = new SbbEntitiesMBeanImpl(sbbEntityFactory);
		registerMBean(sbbEntitiesMBean, SbbEntitiesMBeanImplMBean.OBJECT_NAME);
		final ActivityManagementMBeanImpl activityManagementMBean = new ActivityManagementMBeanImpl(sleeContainer);
		registerMBean(activityManagementMBean, ActivityManagementMBeanImplMBean.OBJECT_NAME);
		// TODO PolicyMBeanImpl
		//registerMBean(policyMBeanImpl, PolicyMBeanImplMBean.OBJECT_NAME);
		
		// slee management mbean
		final SleeManagementMBeanImpl sleeManagementMBean = new SleeManagementMBeanImpl(sleeContainer);
		sleeManagementMBean.setDeploymentMBean(deploymentMBean.getObjectName());
		sleeManagementMBean.setServiceManagementMBean(serviceManagementMBean.getObjectName());
		sleeManagementMBean.setResourceManagementMBean(resourceManagementMBean.getObjectName());
		sleeManagementMBean.setSbbEntitiesMBean(sbbEntitiesMBean.getObjectName());
		sleeManagementMBean.setActivityManagementMBean(activityManagementMBean.getObjectName());
		sleeManagementMBean.setDeploymentMBean(deploymentMBean.getObjectName());

		registerMBean(sleeManagementMBean, SleeManagementMBeanImplMBean.OBJECT_NAME);

		// Install internal deployments: standard-components DU
		try {
			installInternalDeployments();
		} catch (IOException e) {
			//e.printStackTrace();
			throw new StartException(e);
		}
	}

	private void installInternalDeployments() throws IOException {
		String deploymentFolderName = "deployments";
		URL deplURL = this.getClass().getClassLoader().getResource(deploymentFolderName + "/");
		if (deplURL == null || !deplURL.getProtocol().equals("jar")) {
			return;
		}

		String urlExtension = deplURL.toString().substring(
				deplURL.toString().indexOf("file:")+5, deplURL.toString().indexOf("!"));

		TempFileProvider provider = TempFileProvider.create("temp", Executors.newScheduledThreadPool(2));
		Closeable extensionCloseable = null;
		Closeable deploymentClosable = null;
		VirtualFile extensionVfs = VFS.getChild(urlExtension);
		File extensionFile;
		try {
			extensionFile = extensionVfs.getPhysicalFile();
			extensionCloseable = VFS.mountZipExpanded(extensionFile, extensionVfs, provider);

			for (VirtualFile resourcesVfs: extensionVfs.getChildren()) {
				if (resourcesVfs.toString().contains(deploymentFolderName)) {
					File deploymentFile;
					URL deploymentRootURL = null;
					for (VirtualFile deploymentVfs: resourcesVfs.getChildren()) {
						deploymentFile = deploymentVfs.getPhysicalFile();

						if (!deploymentFile.getAbsolutePath().toLowerCase(Locale.ENGLISH).endsWith(".jar")) {
							continue;
						}

						deploymentClosable = VFS.mountZip(deploymentFile, deploymentVfs, provider);

						try {
							deploymentRootURL = VFSUtils.getRootURL(deploymentVfs);
						} catch (Exception ex) {
							log.error("Cannot get URL for deployable unit: " + ex.getLocalizedMessage());
						}

						SleeDeploymentMetaData deploymentMetaData = new SleeDeploymentMetaData(deploymentVfs);
						((ExternalDeployerImpl) externalDeployer)
							.deploy(null, deploymentRootURL, deploymentMetaData, deploymentVfs);
					}
				}
			}
		} finally {
			if (extensionCloseable != null) {
				extensionCloseable.close();
			}
			if (deploymentClosable != null) {
				deploymentClosable.close();
			}
			provider.close();
		}
	}

	private MobicentsCache initCache() {
		Configuration defaultConfig = new ConfigurationBuilder()
				.invocationBatching().enable()
				.clustering().cacheMode(CacheMode.LOCAL)
				.locking().lockAcquisitionTimeout(3000)
				.locking().useLockStriping(false)
				.build();
		GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
				.shutdown().hookBehavior(ShutdownHookBehavior.DONT_REGISTER)
				.build();
		return new MobicentsCache(defaultConfig, globalConfig);
	}
	
	@Override
	public void stop(StopContext context) {
		// shutdown the SLEE
		while(!registeredMBeans.isEmpty()) {
			unregisterMBean(registeredMBeans.pop());
		}		
		sleeContainer = null;
	}

	private void registerMBean(Object mBean, String name) throws StartException {
		try {
			getMbeanServer().getValue().registerMBean(mBean, new ObjectName(name));
		} catch (Throwable e) {
			throw new StartException(e);
		}
		registeredMBeans.push(name);
	}
	
	private void unregisterMBean(String name) {
		try {
			getMbeanServer().getValue().unregisterMBean(new ObjectName(name));
		} catch (Throwable e) {
			log.error("failed to unregister mbean", e);
		}		
	}
	
	public InjectedValue<MBeanServer> getMbeanServer() {
		return mbeanServer;
	}

	public InjectedValue<TransactionManager> getTransactionManager() {
		return transactionManager;
	}

	public InjectedValue<ManagedReferenceFactory> getManagedReferenceFactory() {
		return managedReferenceFactory;
	}

}
