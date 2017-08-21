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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javassist.Modifier;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.ResourceAdaptorComponentImpl;
import org.mobicents.slee.container.component.ra.ConfigPropertyDescriptor;

public class ResourceAdaptorValidator implements Validator {

	private ResourceAdaptorComponentImpl component = null;
	private Logger logger = Logger.getLogger(ResourceAdaptorValidator.class);

	private static final Set<String> _VALID_CONF_PROPERTIES;

	static {

		Set<String> tmp = new HashSet<String>();

		// java.lang.Integer, java.lang.Long,
		// java.lang.Double, java.lang.Float, java.lang.Short,
		// java.lang.Byte, java.lang.Character, java.lang.Boolean, and
		// java.lang.String.

		tmp.add(java.lang.Integer.class.getName());
		tmp.add(java.lang.Long.class.getName());
		tmp.add(java.lang.Double.class.getName());
		tmp.add(java.lang.Float.class.getName());
		tmp.add(java.lang.Short.class.getName());
		tmp.add(java.lang.Byte.class.getName());
		tmp.add(java.lang.Character.class.getName());
		tmp.add(java.lang.Boolean.class.getName());
		tmp.add(java.lang.String.class.getName());

		_VALID_CONF_PROPERTIES = Collections.unmodifiableSet(tmp);
	}

	public void setComponentRepository(ComponentRepository repository) {

	}

	public void setComponent(ResourceAdaptorComponentImpl component) {
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

			if (!validateResourceAdaptorClass()) {
				valid = false;
			}

			if (!validateUsageInterface()) {
				valid = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			valid = false;

		}

		return valid;
	}

	boolean validateUsageInterface() {
		boolean passed = true;
		// String errorBuffer = new String("");

		if (!this.component.isSlee11()) {
			return passed;
		}

		try {

			if (this.component.getDescriptor()
					.getResourceAdaptorUsageParametersInterface() != null) {

				return UsageInterfaceValidator
						.validateResourceAdaptorUsageParameterInterface(this.component);

			}

		} finally {
			// if (!passed) {
			// System.err.println(errorBuffer);
			// logger.error(errorBuffer);
			// }

		}

		return passed;

	}

	boolean validateDescriptor() {

		boolean passed = true;
		//String errorBuffer = new String("");

		try {

			if(!validateConfigProperties())
			{
				passed = false;
			}
			
		} finally {
			// if (!passed) {
			// System.err.println(errorBuffer);
			// logger.error(errorBuffer);
			// }

		}

		return passed;

	}

	boolean validateConfigProperties() {
		boolean passed = true;
		String errorBuffer = new String("");

		if (!this.component.isSlee11())
			return passed;

		try {

			Set<String> declaredConfigProperty = new HashSet<String>();
			// Map<String, String> nameToType = new HashMap<String, String>();

			for (ConfigPropertyDescriptor prop : this.component.getDescriptor()
					.getConfigProperties()) {

				if (prop.getConfigPropertyName() == null
						|| prop.getConfigPropertyName().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Resource adaptor descriptor declares config property with empty name.",
							"15.4.1", errorBuffer);
					continue;

				} else if (prop.getConfigPropertyType() == null
						|| prop.getConfigPropertyType().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Resource adaptor descriptor declares config property with empty type.",
							"15.4.1", errorBuffer);
					continue;

				} else if (declaredConfigProperty.contains(prop
						.getConfigPropertyName())) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Resource adaptor descriptor declares config property more than once, name: "
									+ prop.getConfigPropertyName(), "15.4.1",
							errorBuffer);
				} else {
					declaredConfigProperty.add(prop.getConfigPropertyName());
					
					if(!_VALID_CONF_PROPERTIES.contains(prop.getConfigPropertyType()))
					{
						passed = false;
						errorBuffer = appendToBuffer(
								"Resource adaptor descriptor declares config property of wrong type, type: "+prop.getConfigPropertyType(), "15.4.1",errorBuffer);
					}					
				}
				
				// confirm config properties can be built
				try {
					component.getDefaultConfigPropertiesInstance();
				}
				catch (Throwable e) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Resource adaptor descriptor config property validation passed but creation of ConfigProperties object failed:\n"+e.getStackTrace()+"\n", "15.4.1",errorBuffer);
				}
			}

		} finally {
			if (!passed) {
				if(logger.isEnabledFor(Level.ERROR))
					logger.error(errorBuffer);
			}

		}

		return passed;

	}

	boolean validateResourceAdaptorClass() {

		boolean passed = true;
		String errorBuffer = new String("");

		try {

			Class<?> resourceAdaptorClass = this.component
					.getResourceAdaptorClass();

			if(this.component.isSlee11() && resourceAdaptorClass.getPackage()==null)
			{
				passed = false;
				errorBuffer = appendToBuffer(
						"Resouce adaptor class must defined in package.",
						"15.6", errorBuffer);
			}
			
			
			// must implement javax.slee.resource.ResourceAdaptor
			if (ClassUtils.checkInterfaces(resourceAdaptorClass,
					"javax.slee.resource.ResourceAdaptor") == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Resouce adaptor class must implement javax.slee.ResourceAdaptor.",
						"15.6", errorBuffer);

			}

			int modifiers = resourceAdaptorClass.getModifiers();

			if (!Modifier.isPublic(modifiers)) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Resouce adaptor class must be public.", "15.6",
						errorBuffer);

			}

			if (Modifier.isAbstract(modifiers)) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Resouce adaptor class must not be abstract.", "15.6",
						errorBuffer);

			}

			if (Modifier.isFinal(modifiers)) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Resouce adaptor class must not be final.", "15.6",
						errorBuffer);

			}

			try {
				resourceAdaptorClass.getConstructor();
			} catch (Exception e) {

				// e.printStackTrace();
				passed = false;
				errorBuffer = appendToBuffer(
						"Resouce adaptor class must decalre public no arg constructor.",
						"15.6", errorBuffer);
			}


			
		} finally {
			if (!passed) {
				if(logger.isEnabledFor(Level.ERROR))
					logger.error(errorBuffer);
			}

		}

		return passed;

	}

	// FIXME: add check for access?

	protected String appendToBuffer(String message, String section,
			String buffer) {
		buffer += (this.component.getDescriptor().getResourceAdaptorID()
				+ " : violates section " + section
				+ " of jSLEE 1.1 specification : " + message + "\n");
		return buffer;
	}
}
