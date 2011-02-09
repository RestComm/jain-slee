/**
 * 
 */
package org.mobicents.slee.container.component.profile;

import java.io.InputStream;
import java.util.List;

import javax.slee.management.DeploymentException;

/**
 * Factory to build {@link ProfileSpecificationDescriptor} objects.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ProfileSpecificationDescriptorFactory {

	/**
	 * Builds a list of {@link ProfileSpecificationDescriptor} objects, from an
	 * {@link InputStream} containing the profile specification jar xml.
	 * 
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public List<? extends ProfileSpecificationDescriptor> parse(
			InputStream inputStream) throws DeploymentException;

}
