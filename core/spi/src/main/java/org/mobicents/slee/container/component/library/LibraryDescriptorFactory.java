/**
 * 
 */
package org.mobicents.slee.container.component.library;

import java.io.InputStream;
import java.util.List;

import javax.slee.management.DeploymentException;

/**
 * 
 * Factory to build {@link LibraryDescriptor} objects.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface LibraryDescriptorFactory {

	/**
	 * Builds a list of {@link LibraryDescriptor} objects, from an
	 * {@link InputStream} containing the library jar xml.
	 * 
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public List<? extends LibraryDescriptor> parse(InputStream inputStream)
			throws DeploymentException;
}
