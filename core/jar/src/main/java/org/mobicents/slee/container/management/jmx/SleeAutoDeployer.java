/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.container.management.jmx;

import javax.slee.InvalidStateException;

import org.jboss.deployment.scanner.URLDeploymentScannerMBean;
import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;

/**
 *  
 * MBean service that enables auto deployment of SLEE DUs in a given directory
 * 
 * @author Ivelin Ivanov
 *
 */
public class SleeAutoDeployer extends ServiceMBeanSupport 
	implements SleeAutoDeployerMBean {

	private String deploymentDirectory = null;
	private URLDeploymentScannerMBean baseDeployer = null;
	
    private static Logger logger;

    static {
        logger = Logger.getLogger(SleeAutoDeployer.class);
    }
	
	public String getDeploymentDirectory() {
		return deploymentDirectory;
	}

	public void setDeploymentDirectory(String dir) {
		deploymentDirectory = dir;
	}
	
	protected void startService() throws Exception {
		if (deploymentDirectory == null || baseDeployer == null)
		{
			throw new InvalidStateException("Start skipped due to uninitialized properties. DeploymentDirectory or BaseDeployer is null.");
		}
        baseDeployer.addURL(deploymentDirectory);
	}

	protected void stopService() throws Exception {
        baseDeployer.removeURL(deploymentDirectory);
	}

	public URLDeploymentScannerMBean getBaseDeployer() {
		return baseDeployer;
	}

	public void setBaseDeployer(URLDeploymentScannerMBean deployer) {
		baseDeployer = deployer;
	}

}
