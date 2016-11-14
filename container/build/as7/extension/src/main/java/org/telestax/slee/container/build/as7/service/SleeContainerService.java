package org.telestax.slee.container.build.as7.service;

import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.RuntimeConfig;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;
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

	private final InjectedValue<MBeanServer> mbeanServer = new InjectedValue<MBeanServer>();
	private final InjectedValue<TransactionManager> transactionManager = new InjectedValue<TransactionManager>();
	
	private final InjectedValue<ManagedReferenceFactory> managedReferenceFactory = new InjectedValue<ManagedReferenceFactory>();
	public InjectedValue<ManagedReferenceFactory> getManagedReferenceFactory() {
    	return managedReferenceFactory;
	}

	private final LinkedList<String> registeredMBeans = new LinkedList<String>();
	
	private SleeContainer sleeContainer;

	private ExternalDeployer externalDeployer;

	public ExternalDeployer getExternalDeployer() {
		return externalDeployer;
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

		log.debug("TransactionManager: "+getTransactionManager().getValue());

		// inits the SLEE cache and cluster
		final MobicentsCache cache = initCache();
		final MobicentsCluster cluster = new DefaultMobicentsCluster(cache,
				getTransactionManager().getValue(), null);

		// init the tx manager
		final SleeTransactionManager sleeTransactionManager = new SleeTransactionManagerImpl(
				getTransactionManager().getValue());
		log.debug("SLEE TransactionManager: "+sleeTransactionManager);

		log.debug("TransactionManager Class: "+sleeTransactionManager.getRealTransactionManager().getClass().getName());

		final TraceMBeanImpl traceMBean = new TraceMBeanImpl();
		
		final AlarmMBeanImpl alarmMBean = new AlarmMBeanImpl(traceMBean);		
		
		final MobicentsManagement mobicentsManagement = new MobicentsManagement();
		mobicentsManagement.setEntitiesRemovalDelay(1);

		final ComponentManagement componentManagement = new ComponentManagementImpl();

		final SbbManagement sbbManagement = new SbbManagementImpl();

		final ServiceManagementImpl serviceManagement = new ServiceManagementImpl();

		final ResourceManagementImpl resourceManagement = ResourceManagementImpl.getInstance();

		// TODO profile management and its config
		final org.mobicents.slee.container.deployment.
				profile.jpa.Configuration profileConfiguration = new org.mobicents.slee.container.deployment.
				profile.jpa.Configuration();

		profileConfiguration.setPersistProfiles(true);
		profileConfiguration.setClusteredProfiles(false);
		profileConfiguration.setHibernateDatasource("java:jboss/datasources/ExampleDS");
		profileConfiguration.setHibernateDialect("org.hibernate.dialect.H2Dialect");
		final ProfileManagement profileManagement = new ProfileManagementImpl(profileConfiguration);
		//log.debug("SLEE profileManagement: "+profileManagement);

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
		
		final TimerFacilityConfiguration timerFacilityConfiguration = new TimerFacilityConfiguration();
		timerFacilityConfiguration.setTimerThreads(4);
		timerFacilityConfiguration.setPurgePeriod(0);
		timerFacilityConfiguration
				.setTaskExecutionWaitsForTxCommitConfirmation(true);
		final TimerFacility timerFacility = new TimerFacilityImpl(
				timerFacilityConfiguration);

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
		final SleeConnectionService sleeConnectionService = null;
		final MobicentsSleeConnectionFactory sleeConnectionFactory = null;
		final RmiServerInterface rmiServerInterface = null;

		final UsageParametersManagement usageParametersManagement = new UsageParametersManagementImpl();

		final SbbEntityFactory sbbEntityFactory = new SbbEntityFactoryImpl();

		final EventContextFactoryDataSource eventContextFactoryDataSource = new DefaultEventContextFactoryDataSource();
		final EventContextFactoryConfiguration eventContextFactoryConfiguration = new EventContextFactoryConfiguration();
		eventContextFactoryConfiguration
				.setDefaultEventContextSuspensionTimeout(10000);
		final EventContextFactory eventContextFactory = new EventContextFactoryImpl(
				eventContextFactoryDataSource, eventContextFactoryConfiguration);

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
		
		try {
			sleeContainer = new SleeContainer(deployPath, getMbeanServer().getValue(),
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
		registerMBean(new DeploymentManagerMBeanImpl(internalDeployer), DeploymentManagerMBeanImplMBean.OBJECT_NAME);
		registerMBean(new DeploymentMBeanImpl(internalDeployer), DeploymentMBean.OBJECT_NAME);		
		registerMBean(new ServiceManagementMBeanImpl(serviceManagement), ServiceManagementMBean.OBJECT_NAME);

		// TODO ProfileProvisioningMBeanImpl
		try {
			registerMBean(new ProfileProvisioningMBeanImpl(sleeContainer), ProfileProvisioningMBeanImpl.OBJECT_NAME);
		} catch (NotCompliantMBeanException e) {
			e.printStackTrace();
		}

		registerMBean(new ResourceManagementMBeanImpl(resourceManagement), ResourceManagementMBean.OBJECT_NAME);
		registerMBean(new SbbEntitiesMBeanImpl(sbbEntityFactory), SbbEntitiesMBeanImplMBean.OBJECT_NAME);
		registerMBean(new ActivityManagementMBeanImpl(sleeContainer), ActivityManagementMBeanImplMBean.OBJECT_NAME);
		// TODO PolicyMBeanImpl
		//registerMBean(policyMBeanImpl, PolicyMBeanImplMBean.OBJECT_NAME);
		
		// slee management mbean
		registerMBean(new SleeManagementMBeanImpl(sleeContainer), SleeManagementMBeanImplMBean.OBJECT_NAME);


		//
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
}
