/**
 * 
 */
package org.mobicents.slee.container.component.du;

import java.io.InputStream;

import javax.slee.management.DeploymentException;

/**
 * Factory to build {@link DeployableUnitDescriptor} objects.
 * @author martins
 *
 */
public interface DeployableUnitDescriptorFactory {

	/**
	 * Builds a {@link DeployableUnitDescriptor} object, from an
	 * {@link InputStream} containing the deployable-unit jar xml.
	 * 
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public DeployableUnitDescriptor parse(InputStream inputStream)
			throws DeploymentException;

}
