/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
