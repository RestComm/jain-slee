/**
 * 
 */
package org.mobicents.slee.container.component.event;

import java.io.InputStream;
import java.util.List;

import javax.slee.management.DeploymentException;

/**
 * Factory to build {@link EventTypeDescriptor} objects.
 * 
 * @author martins
 * 
 */
public interface EventTypeDescriptorFactory {

	/**
	 * Builds a list of {@link EventTypeDescriptor} objects, from an
	 * {@link InputStream} containing the event jar xml.
	 * 
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public List<? extends EventTypeDescriptor> parse(InputStream inputStream)
			throws DeploymentException;

}
