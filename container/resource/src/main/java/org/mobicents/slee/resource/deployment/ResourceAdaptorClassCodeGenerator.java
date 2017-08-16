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

package org.mobicents.slee.resource.deployment;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.deployment.SleeComponentWithUsageParametersClassCodeGenerator;

/**
 * Class to control generation of concrete classes from provided ra.
 * @author martins
 */
public class ResourceAdaptorClassCodeGenerator {

	/**
	 * generates all class code for the specified ra component
	 * @param component
	 * @throws DeploymentException
	 */
	public void process(ResourceAdaptorComponent component) throws DeploymentException {
		// resource adaptors only define usage param to be generated
		new SleeComponentWithUsageParametersClassCodeGenerator().process(component);
	}

}