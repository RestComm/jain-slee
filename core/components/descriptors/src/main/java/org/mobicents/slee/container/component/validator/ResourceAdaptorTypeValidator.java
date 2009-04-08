package org.mobicents.slee.container.component.validator;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MEventTypeRef;

public class ResourceAdaptorTypeValidator implements Validator {

	private ComponentRepository repository = null;
	private ResourceAdaptorTypeComponent component = null;
	private Logger logger = Logger
			.getLogger(ResourceAdaptorTypeValidator.class);

	public void setComponentRepository(ComponentRepository repository) {
		this.repository = repository;

	}

	public void setComponent(ResourceAdaptorTypeComponent component) {
		this.component = component;
	}

	public boolean validate() {

		boolean valid = true;
		// Uf here we go
		try {
			if (!validateDescriptor()) {
				valid = false;
				return valid;
			}

			if (!validateRaTypeInterfaces()) {
				valid = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			valid = false;

		}

		return valid;
	}

	boolean validateDescriptor() {

		boolean passed = true;
		String errorBuffer = new String("");

		try {

			Set<MEventTypeRef> declaredReferences = new HashSet<MEventTypeRef>();
			for (MEventTypeRef ref : this.component.getDescriptor()
					.getEventTypeRefs()) {

				if (declaredReferences.contains(ref)) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Ra Type descriptor declares event reference more than once, id: "
									+ ref.getComponentID(), "15.3.2",
							errorBuffer);

				}

			}

			if (this.component.getDescriptor().getActivityTypes().size() > 0
					&& this.component.getDescriptor()
							.getActivityContextInterfaceFactoryInterface() == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Ra Type descriptor declares activity types, but it does not declare activity context interface.",
						"15.3.2", errorBuffer);
			}

			if (this.component.getDescriptor().getActivityTypes().size() == 0
					&& this.component.getDescriptor()
							.getActivityContextInterfaceFactoryInterface() != null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Ra Type descriptor declares  activity context interface but it does not declare activity types.",
						"15.3.2", errorBuffer);
			}

		} finally {
			if (!passed) {
				if(logger.isEnabledFor(Level.ERROR))
					logger.error(errorBuffer);
			}

		}

		return passed;

	}

	boolean validateRaTypeInterfaces() {
		boolean passed = true;
		String errorBuffer = new String("");

		try {
		} finally {
			if (this.component.isSlee11())
			{
				if (component.getActivityContextInterfaceFactoryInterface() != null) {
					Class aciClass = component
							.getActivityContextInterfaceFactoryInterface();
					
					if(aciClass.getPackage() == null)
					{
						errorBuffer = appendToBuffer(
								"Ra Type descriptor declares  activity context interface which is not declared in package.",
								"X", errorBuffer);
					}
					
				}
				
				if (component.getResourceAdaptorSBBInterface() != null) {
					Class aciClass = component
							.getResourceAdaptorSBBInterface();
					
					if(aciClass.getPackage() == null)
					{
						errorBuffer = appendToBuffer(
								"Ra Type descriptor declares  resource adaptor sbb interface which is not declared in package.",
								"X", errorBuffer);
					}
					
				}
				
			}
			if (!passed) {
				if(logger.isEnabledFor(Level.ERROR))
					logger.error(errorBuffer);
			}

		}

		return passed;
	}

	protected String appendToBuffer(String message, String section,
			String buffer) {
		buffer += (this.component.getDescriptor().getResourceAdaptorTypeID()
				+ " : violates section " + section
				+ " of jSLEE 1.1 specification : " + message + "\n");
		return buffer;
	}
}
