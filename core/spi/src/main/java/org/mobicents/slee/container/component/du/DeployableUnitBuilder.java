/**
 * 
 */
package org.mobicents.slee.container.component.du;

import java.io.File;
import java.net.MalformedURLException;

import javax.slee.management.AlreadyDeployedException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.ComponentRepository;

/**
 * @author martins
 *
 */
public interface DeployableUnitBuilder {

	/**
	 * @param url
	 * @param tempDUJarsDeploymentRoot
	 * @param componentRepository
	 * @return
	 */
	public DeployableUnit build(String url, File deploymentRoot,
			ComponentRepository componentRepository)
			throws DeploymentException, AlreadyDeployedException,
			MalformedURLException;

}
