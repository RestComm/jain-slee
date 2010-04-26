/**
 * 
 */
package org.mobicents.slee.container.component;

import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.component.deployment.classloading.ClassLoaderFactoryImpl;
import org.mobicents.slee.container.component.deployment.classloading.ClassLoadingConfiguration;
import org.mobicents.slee.container.component.management.DeployableUnitManagementImpl;
import org.mobicents.slee.container.management.ComponentManagement;

/**
 * @author martins
 * 
 */
public class ComponentManagementImpl extends AbstractSleeContainerModule implements ComponentManagement {

	private final ComponentDescriptorFactoryImpl componentDescriptorFactory;
	private final ComponentRepositoryImpl componentRepository;
	private final DeployableUnitManagementImpl deployableUnitManagement;
	private final ClassLoaderFactoryImpl classLoaderFactory;

	/**
	 * 
	 */
	public ComponentManagementImpl(ClassLoadingConfiguration configuration) {
		classLoaderFactory = new ClassLoaderFactoryImpl(configuration);
		componentDescriptorFactory = new ComponentDescriptorFactoryImpl();
		componentRepository = new ComponentRepositoryImpl();
		deployableUnitManagement = new DeployableUnitManagementImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.management.ComponentManagement#
	 * getClassLoaderFactory()
	 */
	public ClassLoaderFactoryImpl getClassLoaderFactory() {
		return classLoaderFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentManagement#
	 * getComponentDescriptorFactory()
	 */
	public ComponentDescriptorFactoryImpl getComponentDescriptorFactory() {
		return componentDescriptorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.ComponentManagement#getComponentRepository
	 * ()
	 */
	public ComponentRepositoryImpl getComponentRepository() {
		return componentRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentManagement#
	 * getDeployableUnitManagement()
	 */
	public DeployableUnitManagementImpl getDeployableUnitManagement() {
		return deployableUnitManagement;
	}

}
