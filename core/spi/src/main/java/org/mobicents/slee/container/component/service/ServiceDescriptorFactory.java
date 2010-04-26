/**
 * 
 */
package org.mobicents.slee.container.component.service;

import java.io.InputStream;
import java.util.List;

import javax.slee.management.DeploymentException;

/**
 * @author martins
 *
 */
public interface ServiceDescriptorFactory {

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public List<? extends ServiceDescriptor> parse(InputStream inputStream) throws DeploymentException;
	
}
