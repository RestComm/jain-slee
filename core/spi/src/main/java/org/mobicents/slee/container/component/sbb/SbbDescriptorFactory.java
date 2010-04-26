/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import java.io.InputStream;
import java.util.List;

import javax.slee.management.DeploymentException;

/**
 * 
 * Factory to build {@link SbbDescriptor} objects.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:emmartins@gmail.com"> Eduardo Martins </a>
 */
public interface SbbDescriptorFactory {

	/**
	 * Builds a list of {@link SbbDescriptor} objects, from an
	 * {@link InputStream} containing the sbb jar xml.
	 * 
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public List<? extends SbbDescriptor> parse(InputStream inputStream)
			throws DeploymentException;
}
