/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.component.validator;

import java.util.HashSet;
import java.util.Set;

import javax.slee.EventTypeID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponentImpl;

public class ResourceAdaptorTypeValidator implements Validator {

	private ResourceAdaptorTypeComponentImpl component = null;
	private Logger logger = Logger
			.getLogger(ResourceAdaptorTypeValidator.class);

	public void setComponentRepository(ComponentRepository repository) {
		
	}

	public void setComponent(ResourceAdaptorTypeComponentImpl component) {
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

			Set<EventTypeID> declaredReferences = new HashSet<EventTypeID>();
			for (EventTypeID ref : this.component.getDescriptor()
					.getEventTypeRefs()) {

				if (declaredReferences.contains(ref)) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Ra Type descriptor declares event reference more than once, id: "
									+ ref, "15.3.2",
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
					Class<?> aciClass = component
							.getActivityContextInterfaceFactoryInterface();
					
					if(aciClass.getPackage() == null)
					{
						errorBuffer = appendToBuffer(
								"Ra Type descriptor declares  activity context interface which is not declared in package.",
								"X", errorBuffer);
					}
					
				}
				
				if (component.getResourceAdaptorSBBInterface() != null) {
					Class<?> aciClass = component
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
