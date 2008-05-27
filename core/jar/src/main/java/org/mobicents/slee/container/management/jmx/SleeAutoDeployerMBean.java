/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.container.management.jmx;

import org.jboss.system.ServiceMBean;

/**
 *
 * MBean service that enables auto deployment of SLEE DUs in a given directory
 * 
 * @author Ivelin Ivanov
 *
 */public interface SleeAutoDeployerMBean extends ServiceMBean {

    public org.jboss.deployment.scanner.URLDeploymentScannerMBean getBaseDeployer();
    public void setBaseDeployer(org.jboss.deployment.scanner.URLDeploymentScannerMBean deployer);
	public String getDeploymentDirectory();
	public void setDeploymentDirectory(String dir);
	
}
