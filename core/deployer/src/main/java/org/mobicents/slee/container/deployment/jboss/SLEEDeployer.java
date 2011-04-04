package org.mobicents.slee.container.deployment.jboss;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.vfs.spi.deployer.AbstractSimpleVFSRealDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.virtual.VFSUtils;
import org.mobicents.slee.container.deployment.InternalDeployer;
import org.mobicents.slee.container.deployment.ExternalDeployer;

/**
 * JAIN SLEE Real Deployer for JBoss AS 5.x
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author martins
 */
public class SLEEDeployer extends
		AbstractSimpleVFSRealDeployer<SLEEDeploymentMetaData> implements
		ExternalDeployer {
	
	private static final Logger logger = Logger.getLogger(SLEEDeployer.class);
	
	/**
	 * a list of URLs which the deployer was asked to deploy before it actually started
	 */
	private List<URL> waitingList = new ArrayList<URL>();

	private static class DeploymentUnitRecord {
		VFSDeploymentUnit vfsDU;
		SLEEDeploymentMetaData sdmd;		
	}
	
	private Map<URL, DeploymentUnitRecord> records = new HashMap<URL, DeploymentUnitRecord>();

	private InternalDeployer internalDeployer;
		
	public SLEEDeployer() {
		super(SLEEDeploymentMetaData.class);
		setOutput(SLEEDeploymentMetaData.class);
		logger.info("Mobicents SLEE External Deployer initialized.");	
	}
	
	@Override
	public void deploy(VFSDeploymentUnit vfsDU, SLEEDeploymentMetaData sdmd)
			throws DeploymentException {		
		synchronized (this) {
			if (logger.isTraceEnabled()) {
				logger.trace("SLEEParserDeployer 'deploy' called:");
				logger.trace("vfsDeploymentUnit.." + vfsDU);
				logger.trace("sdmd..............." + sdmd);
			}
			if (sdmd != null) {
				try {
					// create URL
					URL deployableUnitURL = VFSUtils.getCompatibleURL(vfsDU
							.getRoot());
					// create record and store it
					DeploymentUnitRecord record = new DeploymentUnitRecord();
					record.sdmd = sdmd;
					record.vfsDU = vfsDU;
					records.put(deployableUnitURL, record);
					// deploy if possible
					if (internalDeployer == null) {
						logger.debug("Unable to INSTALL "
								+ vfsDU.getSimpleName()
								+ " right now. Waiting for Container to start.");
						waitingList.add(deployableUnitURL);
					} else {
						callSubDeployer(deployableUnitURL,record);						
					}
				} catch (Exception e) {
					logger.error("Failure while deploying " + vfsDU.getName(),
							e);
				}
			}
		}
	}

	private void callSubDeployer(URL deployableUnitURL, DeploymentUnitRecord record) throws Exception {		
		internalDeployer.accepts(deployableUnitURL);
		internalDeployer.init(deployableUnitURL);
		for (String componentJar : record.sdmd.duContents) {
			URL componentURL = null;
			try {
				componentURL = VFSUtils.getCompatibleURL(record.vfsDU
						.getFile(componentJar));
			} catch (Exception e) {
				throw new IllegalArgumentException("Failed to locate "
						+ componentJar + " in DU. Does it exists?", e);
			}
			internalDeployer.accepts(componentURL);
			internalDeployer.init(componentURL);
			internalDeployer.start(componentURL);
		}
		internalDeployer.start(deployableUnitURL);
	}

	@Override
	public void undeploy(VFSDeploymentUnit vfsDU, SLEEDeploymentMetaData sdmd) {		
		synchronized (this) {
			if (logger.isTraceEnabled()) {
				logger.trace("SLEEParserDeployer 'undeploy' called:");
				logger.trace("vfsDeploymentUnit.." + vfsDU);
				logger.trace("sdmd..............." + sdmd);
			}
			URL deployableUnitURL = null;
			if (sdmd != null && vfsDU != null) {
				try {
					deployableUnitURL = VFSUtils.getCompatibleURL(vfsDU
							.getRoot());					
				} catch (Exception e) {
					logger.error(
							"Failure while undeploying " + vfsDU.getName(), e);
				}
			}
			if (deployableUnitURL != null) {
				records.remove(deployableUnitURL);		
				if (internalDeployer != null) {
					try {									
						internalDeployer.stop(deployableUnitURL);
					} catch (Exception e) {
						logger.error(
								"Failure while undeploying " + vfsDU.getName(), e);
					}
				}				
			}			
		}
	}

	@Override
	public void setInternalDeployer(InternalDeployer internalDeployer) {
		synchronized (this) {
			// set deployer
			this.internalDeployer = internalDeployer;
			// do the deployments on waiting list
			int failCount = 0;
			for (Iterator<URL> it = this.waitingList.iterator(); it.hasNext();) {
				URL deployableUnitURL = it.next();
				it.remove();
				try {
					callSubDeployer(deployableUnitURL,records.get(deployableUnitURL));					
				} catch (Exception e) {
					failCount++;
					logger.error("Failure during deployment procedures.", e);
				}
			}			
			if (failCount > 0) {
				if (logger.isInfoEnabled()) {
					logger.info("SLEE External Deployer startup: " + failCount + " DUs rejected by the SLEE Internal Deployer, due to errors.");
				}			
			}
			this.waitingList.clear();
		}
	}

}