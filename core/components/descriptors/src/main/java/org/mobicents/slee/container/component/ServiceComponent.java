/**
 * Start time:16:00:31 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.HashSet;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.ServiceDescriptor;
import javax.slee.management.ServiceUsageMBean;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorImpl;

/**
 * Start time:16:00:31 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServiceComponent extends SleeComponent {

	/**
	 * the service descriptor
	 */
	private final ServiceDescriptorImpl descriptor;

	/**
	 * the JAIN SLEE specs descriptor
	 */
	private ServiceDescriptor specsDescriptor = null;

	/**
	 * the {@link SbbComponent} the service defines as root
	 */
	private SbbComponent rootSbbComponent = null;

	/**
	 * the usage mbean for this service
	 */
	private ServiceUsageMBean serviceUsageMBean;

	/**
	 * 
	 * @param descriptor
	 */
	public ServiceComponent(ServiceDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Retrieves the service descriptor
	 * 
	 * @return
	 */
	public ServiceDescriptorImpl getDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the id of the service
	 * 
	 * @return
	 */
	public ServiceID getServiceID() {
		return descriptor.getServiceID();
	}

	@Override
	boolean addToDeployableUnit() {
		return getDeployableUnit().getServiceComponents().put(getServiceID(), this) == null;
	}
	
	@Override
	public Set<ComponentID> getDependenciesSet() {
		return descriptor.getDependenciesSet();
	}

	@Override
	public boolean isSlee11() {
		return descriptor.isSlee11();
	}

	@Override
	public ComponentID getComponentID() {
		return getServiceID();
	}

	@Override
	public boolean validate() throws DependencyException, DeploymentException {
		// validator needed?
		return true;
	}

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public ServiceDescriptor getSpecsDescriptor() {
		if (specsDescriptor == null) {
			specsDescriptor = new ServiceDescriptor(getServiceID(),
					getDeployableUnit().getDeployableUnitID(),
					getDeploymentUnitSource(), getDescriptor().getRootSbbID(),
					getDescriptor().getMService().getAddressProfileTable(),
					getDescriptor().getMService().getResourceInfoProfileTable());
		}
		return specsDescriptor;
	}

	@Override
	public ComponentDescriptor getComponentDescriptor() {
		return getSpecsDescriptor();
	}

	/**
	 * Retrieves the set of sbbs used by this service
	 * 
	 * @param componentRepository
	 * @return
	 */
	public Set<SbbID> getSbbIDs(ComponentRepository componentRepository) {
		Set<SbbID> result = new HashSet<SbbID>();
		buildSbbTree(getDescriptor().getRootSbbID(), result,
				componentRepository);
		return result;
	}

	private void buildSbbTree(SbbID sbbID, Set<SbbID> result,
			ComponentRepository componentRepository) {
		result.add(sbbID);
		SbbComponent sbbComponent = componentRepository.getComponentByID(sbbID);
		for (ComponentID componentID : sbbComponent.getDependenciesSet()) {
			if (componentID instanceof SbbID) {
				SbbID anotherSbbID = (SbbID) componentID;
				if (!result.contains(anotherSbbID)) {
					buildSbbTree(anotherSbbID, result, componentRepository);
				}
			}
		}
	}

	/**
	 * Retrieves the {@link SbbComponent} the service defines as root
	 * 
	 * @return
	 */
	public SbbComponent getRootSbbComponent() {
		return rootSbbComponent;
	}

	/**
	 * Sets the {@link SbbComponent} the service defines as root
	 * 
	 * @param rootSbbComponent
	 */
	public void setRootSbbComponent(SbbComponent rootSbbComponent) {
		this.rootSbbComponent = rootSbbComponent;
	}

	/**
	 * Retrieves the usage mbean for this service
	 * 
	 * @return
	 */
	public ServiceUsageMBean getServiceUsageMBean() {
		return serviceUsageMBean;
	}

	/**
	 * Sets the usage mbean for this service
	 * 
	 * @param serviceUsageMBean
	 */
	public void setServiceUsageMBean(ServiceUsageMBean serviceUsageMBean) {
		this.serviceUsageMBean = serviceUsageMBean;
	}
	@Override
	public void processSecurityPermissions() throws DeploymentException {
		//Do nothing
		
	}
	
	@Override
	public void undeployed() {
		super.undeployed();
		specsDescriptor = null;
		rootSbbComponent = null;
		serviceUsageMBean = null;		
	}
}
