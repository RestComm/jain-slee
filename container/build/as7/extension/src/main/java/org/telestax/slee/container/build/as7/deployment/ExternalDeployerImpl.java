package org.telestax.slee.container.build.as7.deployment;

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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class ExternalDeployerImpl implements ExternalDeployer {

	Logger log = Logger.getLogger(ExternalDeployerImpl.class);

	private static class DeploymentUnitRecord {
		DeploymentUnit du;
		SleeDeploymentMetaData sdmd;
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
		// TODO: do the deployments on waiting list
	}

	public void deploy(DeploymentUnit du, URL deployableUnitURL, SleeDeploymentMetaData sdmd) {
		if (log.isTraceEnabled()) {
			log.trace("ExternalDeployerImpl 'deploy' called:");
			log.trace("DeploymentUnit..........." + du);
			log.trace("SleeDeploymentMetaData..." + sdmd);
		}

		if (sdmd != null) {
			try {
				// create record and store it
				DeploymentUnitRecord record = new DeploymentUnitRecord();
				record.sdmd = sdmd;
				record.du = du;
				records.put(deployableUnitURL, record);

				log.info("internalDeployer "+internalDeployer);
				// deploy if possible
				if (internalDeployer == null) {
					log.debug("Unable to INSTALL "
							+ du.getName()
							+ " right now. Waiting for Container to start.");
					//waitingList.add(url);
				} else {
					callSubDeployer(deployableUnitURL, record);
				}
			} catch (Exception e) {
				log.error("Failure while deploying " + du.getName(), e);
			}
		}
	}

	private void callSubDeployer(URL deployableUnitURL, DeploymentUnitRecord record) throws Exception {
		internalDeployer.accepts(deployableUnitURL);
		internalDeployer.init(deployableUnitURL);
		for (String componentJar : record.sdmd.duContents) {
			log.info("componentJar: "+componentJar);
			final VirtualFile duFile = record.du.getAttachment(Attachments.DEPLOYMENT_ROOT).getRoot();
			VirtualFile componentFile;
			URL componentURL;
			try {
				componentFile = duFile.getChild(componentJar);
				log.info("componentFile: "+componentFile);
				componentURL = VFSUtils.getVirtualURL(componentFile);
				log.info("componentURL: "+componentURL);
				//log.info("componentURL: "+componentFile.toURL());
				//log.info("componentURL Root    : "+VFSUtils.getRootURL(componentFile));
				log.info("componentURL Virtual : "+VFSUtils.getVirtualURL(componentFile));
				log.info("componentURL Physical: "+VFSUtils.getPhysicalURL(componentFile));
			} catch (Exception e) {
				throw new IllegalArgumentException("Failed to locate "
						+ componentJar + " in DU. Does it exists?", e);
			}

			TempFileProvider provider = TempFileProvider.create("tmp", Executors.newScheduledThreadPool(2));
			Closeable closeable = null;

			try {
				closeable = VFS.mountZipExpanded(componentFile, componentFile, provider);
			} catch (IOException e) {
				log.error("IOException on VFS.mountZipExpanded");
			}

			try {
				if (componentFile.getChild("META-INF").exists()) {
					log.info("META-INF exists!");
				} else {
					log.info("META-INF does not exists!");
				}

				internalDeployer.accepts(componentURL);
				internalDeployer.init(componentURL);
				internalDeployer.start(componentURL);
			} finally {
				if (closeable != null) {
					closeable.close();
				}
			}
		}
		internalDeployer.start(deployableUnitURL);
	}

	public void undeploy() {
		// TODO:
	}

}
