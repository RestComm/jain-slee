package org.mobicents.slee.container.deployment.jboss;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.ManagementException;

import org.apache.log4j.Logger;
import org.jboss.deployment.DeploymentException;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.deployment.ExternalDeployer;
import org.mobicents.slee.container.deployment.InternalDeployer;
import org.mobicents.slee.container.deployment.SleeContainerDeployer;

/**
 * The SLEE module responsible for persistent deployments. It complements the
 * JMX MBeans provided by the SLEE spec, providing "automated" (de)activations
 * of components in DUs, and is able to hold a deployment till all its
 * dependencies are deployed.
 * 
 * @author martins
 * 
 */
@SuppressWarnings("deprecation")
public class SleeContainerDeployerImpl extends AbstractSleeContainerModule
		implements InternalDeployer, SleeContainerDeployer {

	private final static Logger LOGGER = Logger
			.getLogger(SleeContainerDeployerImpl.class);

	// FIXME these two need to be merged
	private final SLEESubDeployer sleeSubDeployer = new SLEESubDeployer(this);
	private final DeploymentManager deploymentManager = new DeploymentManager(
			this);

	// the specs MBean used to install/uninstall DUs
	private DeploymentMBean deploymentMBean;

	// the external deployer, responsible for adapting the SLEE to the
	// underlying framework/app server
	private ExternalDeployer externalDeployer;

	private boolean shutdown; 
	
	/**
	 * 
	 * @return
	 */
	public ExternalDeployer getExternalDeployer() {
		return externalDeployer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.deployment.InternalDeployer#setExternalDeployer
	 * (org.mobicents.slee.container.deployment.ExternalDeployer)
	 */
	@Override
	public void setExternalDeployer(ExternalDeployer externalDeployer) {
		this.externalDeployer = externalDeployer;
	}

	/**
	 * 
	 * @return
	 */
	public DeploymentMBean getDeploymentMBean() {
		return deploymentMBean;
	}

	/**
	 * 
	 * @param deploymentMBean
	 */
	public void setDeploymentMBean(DeploymentMBean deploymentMBean) {
		this.deploymentMBean = deploymentMBean;
	}

	/**
	 * 
	 * @return
	 */
	public DeploymentManager getDeploymentManager() {
		return deploymentManager;
	}

	/**
	 * 
	 * @return
	 */
	public SLEESubDeployer getSleeSubDeployer() {
		return sleeSubDeployer;
	}

	@Override
	public void sleeInitialization() {
		// time to trigger the internal deployer
		Runnable r = new Runnable() {
			@Override
			public void run() {

				// establish link with external deployer
				externalDeployer
						.setInternalDeployer(SleeContainerDeployerImpl.this);

				// lets do some log reporting
				deploymentManager.updateDeployedComponents();

				// log deployed DUs on DEBUG level
				if (LOGGER.isDebugEnabled()) {
					StringBuilder sb = new StringBuilder("Deployed SLEE DUs: ");
					DeployableUnitID[] deployedDUs = null;
					try {
						deployedDUs = deploymentMBean.getDeployableUnits();
					} catch (ManagementException e) {
						LOGGER.error(e.getMessage(), e);
					}
					if (deployedDUs == null || deployedDUs.length == 0) {
						sb.append('0');
					} else {
						for (DeployableUnitID deployedDU : deployedDUs) {
							sb.append("\n").append(deployedDU.getURL());
						}
					}
					LOGGER.debug(sb.toString());
				}

				// log DUs on hold, if exist, on WARN level

				Collection<DeployableUnit> waitingForInstallDUs = deploymentManager
						.getWaitingForInstallDUs();
				if (waitingForInstallDUs != null
						&& !waitingForInstallDUs.isEmpty()) {
					Set<DeployableUnit> dUsWaitingforDeps = new HashSet<DeployableUnit>();
					Set<DeployableUnit> dUsWithErrors = new HashSet<DeployableUnit>();
					for (DeployableUnit du : waitingForInstallDUs) {
						if (du.hasDependenciesSatisfied(false)) {
							dUsWithErrors.add(du);
						} else {
							dUsWaitingforDeps.add(du);
						}
					}
					if (!dUsWithErrors.isEmpty()) {
						StringBuilder sb = new StringBuilder(
								"SLEE DUs not deployed, due to error(s):");
						for (DeployableUnit du : dUsWithErrors) {
							sb.append("\n").append(du.getURL());
						}
						LOGGER.warn(sb.toString());
					}
					if (!dUsWaitingforDeps.isEmpty()) {
						StringBuilder sb = new StringBuilder(
								"SLEE DUs not deployed, due to missing dependencies:");
						for (DeployableUnit du : dUsWaitingforDeps) {
							sb.append("\n").append(du.getURL());
							sb.append("\n\tMissing Dependencies:");
							Collection<String> deps = du.getExternalDependencies();
							deps.removeAll(deploymentManager.getDeployedComponents());
							for (String dependency : deps) {
								sb.append("\n\t\t").append(dependency);
							}
						}
						LOGGER.warn(sb.toString());
					}

				}
			}
		};
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {
			executorService.submit(r).get();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		executorService.shutdown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.AbstractSleeContainerModule#sleeShutdown()
	 */
	@Override
	public void sleeShutdown() {
		synchronized (this) {
			shutdown = true;
			// let the deployment manager know about that, it will undeploy DUs
			deploymentManager.sleeShutdown();
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.deployment.InternalDeployer#accepts(java
	 * .net.URL)
	 */
	@Override
	public boolean accepts(URL deployableUnitURL) {
		return sleeSubDeployer.accepts(deployableUnitURL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.deployment.InternalDeployer#init(java.net
	 * .URL)
	 */
	@Override
	public void init(URL deployableUnitURL) throws DeploymentException {
		synchronized (this) {
			if (!shutdown) {
				sleeSubDeployer.init(deployableUnitURL);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.deployment.InternalDeployer#start(java.net
	 * .URL)
	 */
	@Override
	public void start(URL deployableUnitURL) throws DeploymentException {
		synchronized (this) {
			if (!shutdown) {
				sleeSubDeployer.start(deployableUnitURL);
			}
			else {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Ignoring deploy invoked from external deployer, SLEE in shutdown");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.deployment.InternalDeployer#stop(java.net
	 * .URL)
	 */
	@Override
	public void stop(URL deployableUnitURL) throws DeploymentException {
		synchronized (this) {
			if (!shutdown) {
				sleeSubDeployer.stop(deployableUnitURL);
			}
			else {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Ignoring undeploy invoked from external deployer, SLEE in shutdown");
			}
		}			
	}
}
