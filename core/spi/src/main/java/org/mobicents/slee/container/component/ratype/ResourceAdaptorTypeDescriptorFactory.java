/**
 * 
 */
package org.mobicents.slee.container.component.ratype;

import java.io.InputStream;
import java.util.List;

import javax.slee.management.DeploymentException;

/**
 * 
 * Factory to build {@link ResourceAdaptorTypeDescriptor} objects.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ResourceAdaptorTypeDescriptorFactory {

	/**
	 * Builds a list of {@link ResourceAdaptorTypeDescriptor} objects, from an
	 * {@link InputStream} containing the resource adaptor type jar xml.
	 * 
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public List<? extends ResourceAdaptorTypeDescriptor> parse(
			InputStream inputStream) throws DeploymentException;
}
