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
import org.mobicents.slee.container.component.sbb.SbbComponent;

/**
 * Class to control generation of concrete classes from provided interface and
 * abstract classes.
 * @author martins
 */
public class SbbClassCodeGenerator {

	private final static Logger logger = Logger
			.getLogger(SbbClassCodeGenerator.class);

	public void process(SbbComponent sbbComponent) throws DeploymentException {
		if (logger.isDebugEnabled()) {
			logger.debug("Generation concrete class code for "+sbbComponent);
		}
		// generate usage param classes?
		new SleeComponentWithUsageParametersClassCodeGenerator().process(sbbComponent);
		// generate concrete sbb class
		new ConcreteSbbGenerator(sbbComponent).generateConcreteSbb();

		
	}

}