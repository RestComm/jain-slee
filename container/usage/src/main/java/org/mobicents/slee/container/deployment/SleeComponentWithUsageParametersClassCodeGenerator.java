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

package org.mobicents.slee.container.deployment;

import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ClassPool;
import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;

/**
 * 
 * @author martins
 *
 */
public class SleeComponentWithUsageParametersClassCodeGenerator {

	private static final Logger LOGGER = Logger.getLogger(SleeComponentWithUsageParametersClassCodeGenerator.class);
	
	/**
	 * Generates classes for a slee component, which defines usage parameters
	 * @param component
	 * @throws DeploymentException
	 */
	public void process(SleeComponentWithUsageParametersInterface component) throws DeploymentException {
		
		ClassPool classPool = component.getClassPool();
		String deploymentDir = component.getDeploymentDir().getAbsolutePath();
		Class<?> usageParametersInterface = component
				.getUsageParametersInterface();
		if (usageParametersInterface != null) {
			try {
				// generate the concrete usage param set class
				component
						.setUsageParametersConcreteClass(new ConcreteUsageParameterClassGenerator(
								usageParametersInterface.getName(),
								deploymentDir, classPool)
								.generateConcreteUsageParameterClass());
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Generated usage parameter impl class for "+component);
		        }
				// generate the mbeans
				new ConcreteUsageParameterMBeanGenerator(component)
						.generateConcreteUsageParameterMBean();
				
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Generated usage mbean (interface and impl) for "+component);
		        }
				
			} catch (DeploymentException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new DeploymentException(
						"Failed to generate "+component+" usage parameter class", ex);
			}
		}
		
	}
}
