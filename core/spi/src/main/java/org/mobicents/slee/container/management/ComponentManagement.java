/**
 * 
 */
package org.mobicents.slee.container.management;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.ComponentDescriptorFactory;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.classloading.ClassLoaderFactory;
import org.mobicents.slee.container.component.du.DeployableUnitManagement;

/**
 * @author martins
 * 
 */
public interface ComponentManagement extends SleeContainerModule {

	/**
	 * Retrieves the class loader factory.
	 * 
	 * @return
	 */
	public ClassLoaderFactory getClassLoaderFactory();

	/**
	 * Retrieves the component descriptor factory
	 * 
	 * @return
	 */
	public ComponentDescriptorFactory getComponentDescriptorFactory();

	/**
	 * Retrieves the component repository.
	 * 
	 * @return
	 */
	public ComponentRepository getComponentRepository();

	/**
	 * Retrieves the deployable unit management.
	 * 
	 * @return
	 */
	public DeployableUnitManagement getDeployableUnitManagement();

}
