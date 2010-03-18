package org.mobicents.slee.container.component.validator;

import javax.slee.SbbID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;

public class ServiceValidator implements Validator {

	private ServiceComponent component = null;
	private ComponentRepository repository = null;
	private final static transient Logger logger = Logger.getLogger(ServiceValidator.class);

	public void setComponentRepository(ComponentRepository repository) {
		this.repository = repository;

	}

	public ServiceComponent getComponent() {
		return component;
	}

	public void setComponent(ServiceComponent component) {
		this.component = component;
	}

	public boolean validate() {
		boolean passed = true;
		
		if(!validateDescriptor())
		{
			passed = false;
			return passed;
		}
		
		
		if(validateCompatibilityReferenceConstraints())
		{
			passed = false;
			
		}
		
		
		return passed;
	}

	boolean validateDescriptor() {
		boolean passed = true;
		String errorBuffer = new String("");

		try {
			ServiceDescriptorImpl descritpor=this.component.getDescriptor();
			//Nothign so far here.
		} finally {
			if (!passed) {
				if (logger.isEnabledFor(Level.ERROR)) {
					logger.error(errorBuffer);
				}
			}
		}
		return passed;
	}

	/**
	 * See section 1.3 of jslee 1.1 specs
	 * 
	 * @return
	 */
	boolean validateCompatibilityReferenceConstraints() {

		boolean passed = true;
		String errorBuffer = new String("");

		try {
			if (!this.component.isSlee11()) {
				// A 1.0 SBB must not reference or use a 1.1 Profile
				// Specification. This must be enforced by a 1.1
				// JAIN SLEE.

				ServiceComponent specComponent = this.repository.getComponentByID(this.component.getServiceID());
				if (specComponent == null) {
					// should not happen
					passed = false;
					errorBuffer = appendToBuffer("Referenced " + this.component.getServiceID()
							+ " was not found in component repository, this should not happen since dependencies were already verified", "1.3", errorBuffer);

				} else {
					if (specComponent.isSlee11()) {
						passed = false;
						errorBuffer = appendToBuffer("Service is following 1.0 JSLEE contract, it must not reference 1.1 Sbb as root: " + this.component.getServiceID(), "1.3", errorBuffer);
					}
				}

			}
		} finally {
			if (!passed) {
				if (logger.isEnabledFor(Level.ERROR)) {
					logger.error(errorBuffer);
				}
			}

		}

		return passed;
	}

	protected String appendToBuffer(String message, String section, String buffer) {
		buffer += (this.component.getDescriptor().getServiceID() + " : violates section " + section + " of jSLEE 1.1 specification : " + message + "\n");
		return buffer;
	}
}
