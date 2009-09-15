package org.mobicents.slee.container.deployment;

import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.SbbComponent;

/**
 * Class to control generation of concrete classes from provided interface and
 * abstract classes.
 * @author martins
 */
public class SbbClassCodeGenerator {

	private final static Logger logger = Logger
			.getLogger(SbbClassCodeGenerator.class);

	public void process(SbbComponent sbbComponent) throws DeploymentException {
		// generate usage param classes?
		new SleeComponentWithUsageParametersClassCodeGenerator().process(sbbComponent);
		// generate concrete sbb class
		new ConcreteSbbGenerator(sbbComponent).generateConcreteSbb();

		if (logger.isDebugEnabled()) {
			logger.debug("Generated all classes!");
		}
	}

}