package org.restcomm.slee.container.build.as7.deployment;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.logging.Logger;
import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;
import org.mobicents.slee.container.deployment.ExternalDeployer;
import org.mobicents.slee.container.deployment.InternalDeployer;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;

public class ExternalDeployerImpl implements ExternalDeployer {

	Logger log = Logger.getLogger(ExternalDeployerImpl.class);

	/**
	 * a list of URLs which the deployer was asked to deploy before it actually started
	 */
	private List<URL> waitingList = new ArrayList<URL>();

	private static class DeploymentUnitRecord {
		DeploymentUnit deploymentUnit;
		SleeDeploymentMetaData deploymentMetaData;
		VirtualFile defaultRoot;
	}

	private Map<URL, DeploymentUnitRecord> records = new HashMap<URL, DeploymentUnitRecord>();

	private InternalDeployer internalDeployer;

	public ExternalDeployerImpl() {
		log.info("Mobicents SLEE External Deployer initialized.");
	}

	@Override
	public void setInternalDeployer(InternalDeployer internalDeployer) {
		// set deployer
		this.internalDeployer = internalDeployer;

		// do the deployments on waiting list
		int failCount = 0;
		for (Iterator<URL> it = this.waitingList.iterator(); it.hasNext();) {
			URL deployableUnitURL = it.next();
			it.remove();
			try {
				callSubDeployer(deployableUnitURL, records.get(deployableUnitURL));
			} catch (Exception e) {
				failCount++;
				log.error("Failure during deployment procedures.", e);
			}
		}
		if (failCount > 0) {
			if (log.isInfoEnabled()) {
				log.info("SLEE External Deployer startup: " + failCount + " DUs rejected by the SLEE Internal Deployer, due to errors.");
			}
		}
		this.waitingList.clear();
	}

	public void deploy(DeploymentUnit deploymentUnit, URL deployableUnitURL, SleeDeploymentMetaData deploymentMetaData, VirtualFile defaultRoot) {
		if (log.isTraceEnabled()) {
			log.trace("ExternalDeployerImpl 'deploy' called:");
			log.trace("DeploymentUnit..........." + deploymentUnit);
			log.trace("SleeDeploymentMetaData..." + deploymentMetaData);
		}

		if (deploymentMetaData != null) {
			String deploymentUnitName = deploymentUnit != null ?
					deploymentUnit.getName() : "default deployment: " + defaultRoot.getName();

			try {
				// create record and store it
				DeploymentUnitRecord record = new DeploymentUnitRecord();
				record.deploymentMetaData = deploymentMetaData;
				record.deploymentUnit = deploymentUnit;
				record.defaultRoot = defaultRoot;
				records.put(deployableUnitURL, record);

				// deploy if possible
				if (internalDeployer == null) {
					if (log.isDebugEnabled()) {
						log.debug("Unable to INSTALL " + deploymentUnitName
								+ " right now. Waiting for Container to start.");
						log.debug("deployableUnitURL: "+deployableUnitURL);
					}
					waitingList.add(deployableUnitURL);
				} else {
					callSubDeployer(deployableUnitURL, record);
				}
			} catch (Exception e) {
				log.error("Failure while deploying " + deploymentUnitName, e);
			}
		}
	}

	private void callSubDeployer(URL deployableUnitURL, DeploymentUnitRecord record) throws Exception {
		String deployableUnitName = record.deploymentUnit != null ? record.deploymentUnit.getName() : "";
		internalDeployer.accepts(deployableUnitURL, deployableUnitName);
		internalDeployer.init(deployableUnitURL, deployableUnitName);

		VirtualFile duFile = null;
		if (record.deploymentUnit != null) {
			duFile = record.deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT).getRoot();
		} else
		if (record.defaultRoot != null) {
			duFile = record.defaultRoot;
		}
		VirtualFile componentFile;
		URL componentURL;
		for (String componentJar : record.deploymentMetaData.duContents) {
			try {
				componentFile = duFile.getChild(componentJar);
				componentURL = VFSUtils.getVirtualURL(componentFile);
			} catch (Exception e) {
				throw new IllegalArgumentException("Failed to locate "
						+ componentJar + " in DU. Does it exists?", e);
			}

			// SergeyLee: add extension checking (jar, war, sar, etc)
			TempFileProvider provider = TempFileProvider.create("tmp", Executors.newScheduledThreadPool(2));
			Closeable closeable = null;

			try {
				closeable = VFS.mountZipExpanded(componentFile, componentFile, provider);
			} catch (IOException e) {
			}

			try {
				internalDeployer.accepts(componentURL, "");
				internalDeployer.init(componentURL, "");
				internalDeployer.start(componentURL, "");
			} finally {
				if (closeable != null) {
					closeable.close();
				}
			}
		}
		internalDeployer.start(deployableUnitURL, deployableUnitName);
	}

	public void undeploy(DeploymentUnit deploymentUnit, URL deployableUnitURL, SleeDeploymentMetaData deploymentMetaData) {
		if (log.isTraceEnabled()) {
			log.trace("ExternalDeployerImpl 'undeploy' called:");
			log.trace("DeploymentUnit..........." + deploymentUnit);
			log.trace("SleeDeploymentMetaData..." + deploymentMetaData);
		}
		
		if (deployableUnitURL != null) {
			records.remove(deployableUnitURL);
			if (internalDeployer != null) {
				String deployableUnitName = deploymentUnit != null ? deploymentUnit.getName() : "";
				try {
					internalDeployer.stop(deployableUnitURL, deployableUnitName);
				} catch (Exception e) {
					log.error(
						"Failure while undeploying " + deployableUnitName, e);
				}
			}
		}
	}

}