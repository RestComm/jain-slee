/**
 * 
 */
package org.mobicents.slee.container.component.ra;

import java.io.InputStream;
import java.util.List;

import javax.slee.management.DeploymentException;

/**
 * 
 * Factory to build {@link ResourceAdaptorDescriptor} objects.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ResourceAdaptorDescriptorFactory {

	/**
	 * Builds a list of {@link ResourceAdaptorDescriptor} objects, from an
	 * {@link InputStream} containing the resource adaptor jar xml.
	 * 
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public List<? extends ResourceAdaptorDescriptor> parse(InputStream inputStream)
			throws DeploymentException;
}
