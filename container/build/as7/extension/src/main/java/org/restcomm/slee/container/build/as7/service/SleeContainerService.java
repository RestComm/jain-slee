/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.restcomm.slee.container.build.as7.service;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.Executors;

import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.slee.management.AlarmMBean;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.TraceMBean;
import javax.transaction.TransactionManager;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.configuration.global.ShutdownHookBehavior;
import org.infinispan.util.concurrent.IsolationLevel;
import org.jboss.as.controller.services.path.PathManager;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;
import org.mobicents.slee.connector.local.SleeConnectionService;
import org.mobicents.slee.connector.local.SleeConnectionServiceImpl;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.component.ComponentManagementImpl;
import org.mobicents.slee.container.component.classloading.ReplicationClassLoader;
import org.mobicents.slee.container.component.management.jmx.PolicyMBeanImpl;
import org.mobicents.slee.container.component.management.jmx.PolicyMBeanImplMBean;
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
import org.mobicents.slee.container.management.ComponentManagement;
import org.mobicents.slee.container.management.ProfileManagement;
import org.mobicents.slee.container.management.ProfileManagementImpl;
import org.mobicents.slee.container.management.ResourceManagementImpl;
import org.mobicents.slee.container.management.SbbManagement;
import org.mobicents.slee.container.management.SbbManagementImpl;
import org.mobicents.slee.container.management.ServiceManagementImpl;
import org.mobicents.slee.container.management.UsageParametersManagement;
import org.mobicents.slee.container.management.UsageParametersManagementImpl;
import org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImpl;
import org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;
import org.mobicents.slee.container.management.jmx.CongestionControlConfiguration;
import org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean;
import org.mobicents.slee.container.management.jmx.DeploymentMBeanImpl;
import org.mobicents.slee.container.management.jmx.EventContextFactoryConfiguration;
import org.mobicents.slee.container.management.jmx.EventContextFactoryConfigurationMBean;
import org.mobicents.slee.container.management.jmx.EventRouterConfiguration;
import org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean;
import org.mobicents.slee.container.management.jmx.EventRouterStatistics;
import org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;
import org.mobicents.slee.container.management.jmx.MobicentsManagementMBean;
import org.mobicents.slee.container.management.jmx.ProfileProvisioningMBeanImpl;
import org.mobicents.slee.container.management.jmx.ResourceManagementMBeanImpl;
import org.mobicents.slee.container.management.jmx.SbbEntitiesMBeanImpl;
import org.mobicents.slee.container.management.jmx.SbbEntitiesMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.ServiceManagementMBeanImpl;
import org.mobicents.slee.container.management.jmx.SleeManagementMBeanImpl;
import org.mobicents.slee.container.management.jmx.SleeManagementMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.TimerFacilityConfiguration;
import org.mobicents.slee.container.management.jmx.TimerFacilityConfigurationMBean;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.container.remote.RmiServerInterfaceImpl;
import org.mobicents.slee.container.rmi.RmiServerInterface;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.activity.ActivityManagementConfiguration;
import org.mobicents.slee.runtime.eventrouter.EventRouterImpl;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactoryImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManagerImpl;
import org.restcomm.cache.CacheDataExecutorService;
import org.restcomm.cache.CacheExecutorConfiguration;
import org.restcomm.cluster.MobicentsClusterFactory;
import org.restcomm.cluster.election.DefaultClusterElector;
import org.restcomm.slee.container.build.as7.deployment.ExternalDeployerImpl;
import org.restcomm.slee.container.build.as7.deployment.SleeDeploymentMetaData;
import org.restcomm.slee.container.build.as7.naming.JndiManagementImpl;
import org.restcomm.slee.container.build.as7.tckwrapper.SleeTCKPluginWrapper;

public class SleeContainerService implements Service<SleeContainer> {

	Logger log = Logger.getLogger(SleeContainerService.class);

	// TODO obtain real path through
	// org.jboss.as.controller.services.path.PathManager (see WebServerService)
	// or expression resolve ?
	private static final String JBOSS_DIR = "jboss.home.dir";
	private static final String CONFIG_DIR = "jboss.server.config.dir";
	private static final String TEMP_DIR = "jboss.server.temp.dir";

	private String cacheConfig;
	private ModelNode fullModel;

	private final InjectedValue<MBeanServer> mbeanServer = new InjectedValue<MBeanServer>();
	private final InjectedValue<PathManager> pathManagerInjector = new InjectedValue<PathManager>();
	private final InjectedValue<TransactionManager> transactionManager = new InjectedValue<TransactionManager>();
	private final InjectedValue<ManagedReferenceFactory> managedReferenceFactory = new InjectedValue<ManagedReferenceFactory>();
	private final LinkedList<String> registeredMBeans = new LinkedList<String>();

	private SleeContainer sleeContainer;

	private ExternalDeployer externalDeployer;

	public ExternalDeployer getExternalDeployer() {
		return externalDeployer;
	}

	public SleeContainerService(ModelNode model, String cacheConfig) {
		this.fullModel = model;
		this.cacheConfig = cacheConfig;
	}

	private ModelNode peek(ModelNode node, String... args) {
		for (String arg : args) {
			if (!node.hasDefined(arg)) {
				return null;
			}
			node = node.get(arg);
		}
		return node;
	}

	private String getPropertyString(String mbeanName, String propertyName, String defaultValue) {
		String result = defaultValue;
		ModelNode propertyNode = peek(fullModel, "mbean", mbeanName, "property", propertyName);
		if (propertyNode != null && propertyNode.isDefined()) {
			result = propertyNode.get("value").asString();
		}
		return (result == null) ? defaultValue : result;
	}

	private int getPropertyInt(String mbeanName, String propertyName, int defaultValue) {
		int result = defaultValue;
		ModelNode propertyNode = peek(fullModel, "mbean", mbeanName, "property", propertyName);
		if (propertyNode != null && propertyNode.isDefined()) {
			result = propertyNode.get("value").asInt();
		}
		return result;
	}
	
	private long getPropertyLong(String mbeanName, String propertyName, long defaultValue) {
		long result = defaultValue;
		ModelNode propertyNode = peek(fullModel, "mbean", mbeanName, "property", propertyName);
		if (propertyNode != null && propertyNode.isDefined()) {
			result = propertyNode.get("value").asLong();
		}
		return result;
	}

	private boolean getPropertyBoolean(String mbeanName, String propertyName, boolean defaultValue) {
		boolean result = defaultValue;
		ModelNode propertyNode = peek(fullModel, "mbean", mbeanName, "property", propertyName);
		if (propertyNode != null && propertyNode.isDefined()) {
			result = propertyNode.get("value").asBoolean();
		}
		return result;
	}

	@Override
	public SleeContainer getValue() throws IllegalStateException, IllegalArgumentException {
		return sleeContainer;
	}

	@Override
	public void start(StartContext context) throws StartException {
		log.info("Starting SLEE Container service");

		final String deployPath = pathManagerInjector.getValue().getPathEntry(TEMP_DIR).resolvePath() + "/slee";

		this.externalDeployer = new ExternalDeployerImpl();
		final SleeContainerDeployerImpl internalDeployer = new SleeContainerDeployerImpl();
		internalDeployer.setExternalDeployer(this.externalDeployer);

		// inits the SLEE cache and cluster

		final ComponentManagement componentManagement = new ComponentManagementImpl();

		ReplicationClassLoader replicationClassLoader = componentManagement.getClassLoaderFactory()
				.newReplicationClassLoader(this.getClass().getClassLoader());

		final MobicentsClusterFactory clusterFactory = initClusterFactory(replicationClassLoader);

		
		// init the tx manager
		final SleeTransactionManager sleeTransactionManager = new SleeTransactionManagerImpl(
				getTransactionManager().getValue());

		final TraceMBeanImpl traceMBean = new TraceMBeanImpl();

		final AlarmMBeanImpl alarmMBean = new AlarmMBeanImpl(traceMBean);

		final MobicentsManagement mobicentsManagement = new MobicentsManagement();
		mobicentsManagement.setEntitiesRemovalDelay(getPropertyInt("MobicentsManagement", "entitiesRemovalDelay", 1));
		mobicentsManagement.setInitializeReferenceDataTypesWithNull(
				getPropertyBoolean("MobicentsManagement", "initializeReferenceDataTypesWithNull", true));

		final SbbManagement sbbManagement = new SbbManagementImpl();

		final ServiceManagementImpl serviceManagement = new ServiceManagementImpl();

		final ResourceManagementImpl resourceManagement = ResourceManagementImpl.getInstance();

		final org.mobicents.slee.container.deployment.profile.jpa.Configuration profileConfiguration = new org.mobicents.slee.container.deployment.profile.jpa.Configuration();

		// TODO: ExtensionConfiguration for Profile Management
		profileConfiguration.setPersistProfiles(getPropertyBoolean("H2DBConfig", "persistProfiles", true));
		profileConfiguration.setClusteredProfiles(getPropertyBoolean("H2DBConfig", "clusteredProfiles", false));
		profileConfiguration.setHibernateDatasource(
				getPropertyString("H2DBConfig", "hibernateDatasource", "java:jboss/datasources/ExampleDS"));
		profileConfiguration.setHibernateDialect(
				getPropertyString("H2DBConfig", "hibernateDialect", "org.hibernate.dialect.H2Dialect"));
		final ProfileManagement profileManagement = new ProfileManagementImpl(profileConfiguration);

		// TODO: ExtensionConfiguration for EventRouter
		final EventRouterConfiguration eventRouterConfiguration = new EventRouterConfiguration();
		eventRouterConfiguration
				.setEventRouterThreads(getPropertyInt("EventRouterConfiguration", "eventRouterThreads", 64));
		eventRouterConfiguration.setCollectStats(getPropertyBoolean("EventRouterConfiguration", "collectStats", true));
		eventRouterConfiguration.setConfirmSbbEntityAttachement(
				getPropertyBoolean("EventRouterConfiguration", "confirmSbbEntityAttachement", true));
		try {
			eventRouterConfiguration
					.setExecutorMapperClassName(getPropertyString("EventRouterConfiguration", "executorMapperClassName",
							"org.mobicents.slee.runtime.eventrouter.mapping.ActivityHashingEventRouterExecutorMapper"));
		} catch (ClassNotFoundException e) {
			throw new StartException(e);
		}
		final EventRouter eventRouter = new EventRouterImpl(eventRouterConfiguration);

		// TODO: ExtensionConfiguration for TimerFacility
		final TimerFacilityConfiguration timerFacilityConfiguration = new TimerFacilityConfiguration();
		timerFacilityConfiguration.setTimerThreads(4);
		timerFacilityConfiguration.setPurgePeriod(0);
		timerFacilityConfiguration.setTaskExecutionWaitsForTxCommitConfirmation(true);
		final TimerFacility timerFacility = new TimerFacilityImpl(timerFacilityConfiguration);

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
		final RmiServerInterface rmiServerInterface = new RmiServerInterfaceImpl();
		rmiServerInterface.setAddress(getPropertyString("RmiServerInterface", "rmiAddress", "127.0.0.1"));
		rmiServerInterface.setPort(getPropertyInt("RmiServerInterface", "rmiPort", 1099));

		final UsageParametersManagement usageParametersManagement = new UsageParametersManagementImpl();

		final SbbEntityFactory sbbEntityFactory = new SbbEntityFactoryImpl();

		final EventContextFactoryDataSource eventContextFactoryDataSource = new DefaultEventContextFactoryDataSource();
		final EventContextFactoryConfiguration eventContextFactoryConfiguration = new EventContextFactoryConfiguration();
		eventContextFactoryConfiguration.setDefaultEventContextSuspensionTimeout(
				getPropertyInt("EventContextFactoryConfiguration", "defaultEventContextSuspensionTimeout", 10000));
		final EventContextFactory eventContextFactory = new EventContextFactoryImpl(eventContextFactoryDataSource,
				eventContextFactoryConfiguration);

		final CongestionControlConfiguration congestionControlConfiguration = new CongestionControlConfiguration();
		congestionControlConfiguration.setPeriodBetweenChecks(getPropertyInt("CongestionControlConfiguration", "periodBetweenChecks", 0));
		congestionControlConfiguration.setMinFreeMemoryToTurnOn(getPropertyInt("CongestionControlConfiguration", "minFreeMemoryToTurnOn", 10));
		congestionControlConfiguration.setMinFreeMemoryToTurnOff(getPropertyInt("CongestionControlConfiguration", "minFreeMemoryToTurnOff", 20));
		congestionControlConfiguration.setRefuseStartActivity(getPropertyBoolean("CongestionControlConfiguration", "refuseStartActivity", true));
		congestionControlConfiguration.setRefuseFireEvent(getPropertyBoolean("CongestionControlConfiguration", "refuseFireEvent", false));
		final CongestionControl congestionControl = new CongestionControlImpl(congestionControlConfiguration);

		// FIXME this needs further work on dependencies
		final PolicyMBeanImpl policyMBeanImpl = new PolicyMBeanImpl();
		policyMBeanImpl.setUseMPolicy(true);

		ServiceController<?> serviceController = context.getController();

		try {
			sleeContainer = new SleeContainer(deployPath, serviceController, getMbeanServer().getValue(),
					componentManagement, sbbManagement, serviceManagement, resourceManagement, profileManagement,
					eventContextFactory, eventRouter, timerFacility, activityContextFactory,
					activityContextNamingFacility, nullActivityContextInterfaceFactory, nullActivityFactory,
					rmiServerInterface, sleeTransactionManager, clusterFactory, replicationClassLoader, alarmMBean, traceMBean,
					usageParametersManagement, sbbEntityFactory, congestionControl, sleeConnectionService,
					internalDeployer);
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

		ProfileProvisioningMBeanImpl profileProvisioningMBean = null;
		try {
			profileProvisioningMBean = new ProfileProvisioningMBeanImpl(sleeContainer);
			registerMBean(profileProvisioningMBean, ProfileProvisioningMBeanImpl.OBJECT_NAME);
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
		registerMBean(policyMBeanImpl, PolicyMBeanImplMBean.OBJECT_NAME);

		// slee management mbean
		final SleeManagementMBeanImpl sleeManagementMBean = new SleeManagementMBeanImpl(sleeContainer);
		sleeManagementMBean.setDeploymentMBean(deploymentMBean.getObjectName());
		sleeManagementMBean.setServiceManagementMBean(serviceManagementMBean.getObjectName());
		sleeManagementMBean.setResourceManagementMBean(resourceManagementMBean.getObjectName());
		sleeManagementMBean.setSbbEntitiesMBean(sbbEntitiesMBean.getObjectName());
		sleeManagementMBean.setActivityManagementMBean(activityManagementMBean.getObjectName());
		sleeManagementMBean.setDeploymentMBean(deploymentMBean.getObjectName());
		if (profileProvisioningMBean != null)
			sleeManagementMBean.setProfileProvisioningMBean(profileProvisioningMBean.getObjectName());

		registerMBean(sleeManagementMBean, SleeManagementMBeanImplMBean.OBJECT_NAME);

		boolean sleeTCKPlugin = (peek(fullModel, "mbean", "SleeTCKPluginWrapper") != null);
		if (sleeTCKPlugin) {
			sleeTCKPlugin = getPropertyBoolean("SleeTCKPluginWrapper", "active", false);
		}
		if (sleeTCKPlugin) {
			log.debug("Registering SleeTCKPluginWrapper");
			final SleeTCKPluginWrapper tckPluginWrapper = new SleeTCKPluginWrapper();
			registerMBean(tckPluginWrapper, SleeTCKPluginWrapper.OBJECT_NAME);
			tckPluginWrapper.startService();
		}

		// Install internal deployments: standard-components DU
		try {
			installInternalDeployments();
		} catch (IOException e) {
			// e.printStackTrace();
			throw new StartException(e);
		}
	}

	private void installInternalDeployments() throws IOException {
		String deploymentFolderName = "deployments";
		URL deplURL = this.getClass().getClassLoader().getResource(deploymentFolderName + "/");
		if (deplURL == null || !deplURL.getProtocol().equals("jar")) {
			return;
		}

		String urlExtension = deplURL.toString().substring(deplURL.toString().indexOf("file:") + 5,
				deplURL.toString().indexOf("!"));

		TempFileProvider provider = TempFileProvider.create("temp", Executors.newScheduledThreadPool(2));
		Closeable extensionCloseable = null;
		Closeable deploymentClosable = null;
		VirtualFile extensionVfs = VFS.getChild(urlExtension);
		File extensionFile;
		try {
			extensionFile = extensionVfs.getPhysicalFile();
			extensionCloseable = VFS.mountZipExpanded(extensionFile, extensionVfs, provider);

			for (VirtualFile resourcesVfs : extensionVfs.getChildren()) {
				if (resourcesVfs.toString().contains(deploymentFolderName)) {
					File deploymentFile;
					URL deploymentRootURL = null;
					for (VirtualFile deploymentVfs : resourcesVfs.getChildren()) {
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

						SleeDeploymentMetaData deploymentMetaData = new SleeDeploymentMetaData(deploymentVfs, true);
						((ExternalDeployerImpl) externalDeployer).deploy(null, deploymentRootURL, deploymentMetaData,
								deploymentVfs);
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

	private MobicentsClusterFactory initClusterFactory(ClassLoader classLoader) {

		Configuration defaultConfig = new ConfigurationBuilder().invocationBatching().enable().clustering()
				.cacheMode(CacheMode.LOCAL)
				// .clustering().cacheMode(CacheMode.REPL_ASYNC)
				// .transaction().transactionMode(TransactionMode.TRANSACTIONAL)
				.transaction().syncCommitPhase(true).locking().isolationLevel(IsolationLevel.REPEATABLE_READ).locking()
				.lockAcquisitionTimeout(30000).useLockStriping(false).jmxStatistics().disable().build();
		GlobalConfiguration globalConfig = new GlobalConfigurationBuilder().globalJmxStatistics().disable()
				// .transport().defaultTransport()
				.shutdown().hookBehavior(ShutdownHookBehavior.DONT_REGISTER).build();
		byte[] cacheConfigData=null;
		try {
			cacheConfigData = this.cacheConfig.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e2) {
		}
		
		int cacheExecutorThreads = getPropertyInt("CacheExecutorConfiguration", "cacheExecutorThreads", 16);
		long cacheExecutorTerminateTimeout = getPropertyLong("CacheExecutorConfiguration", "cacheExecutorTerminateTimeout", 5000L);
		long cacheExecutorCommandTimeout = getPropertyLong("CacheExecutorConfiguration", "cacheExecutorCommandTimeout", 1000L);
		CacheExecutorConfiguration cacheExecutorConfiguration = new CacheExecutorConfiguration(cacheExecutorThreads, cacheExecutorTerminateTimeout, cacheExecutorCommandTimeout);

		CacheDataExecutorService cacheExecutorService = new CacheDataExecutorService(cacheExecutorConfiguration);
		return new MobicentsClusterFactory(this.cacheConfig, cacheConfigData, defaultConfig, globalConfig,
				transactionManager.getValue(), new DefaultClusterElector(), classLoader, cacheExecutorService);
	}

	@Override
	public void stop(StopContext context) {
		// shutdown the SLEE
		while (!registeredMBeans.isEmpty()) {
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

	public InjectedValue<PathManager> getPathManagerInjector() {
		return pathManagerInjector;
	}

	public InjectedValue<TransactionManager> getTransactionManager() {
		return transactionManager;
	}

	public InjectedValue<ManagedReferenceFactory> getManagedReferenceFactory() {
		return managedReferenceFactory;
	}

}
