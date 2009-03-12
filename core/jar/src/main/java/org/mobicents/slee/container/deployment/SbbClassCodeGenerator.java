package org.mobicents.slee.container.deployment;

import javax.slee.management.DeploymentException;

import org.jboss.logging.Logger;
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

		new SleeComponentWithUsageParametersClassCodeGenerator().process(sbbComponent);

		// Enhancement code goes here...
		SbbAbstractDecorator abstractSbbDecorator = new SbbAbstractDecorator(
				sbbComponent.getAbstractSbbClass().getName(), sbbComponent.getDeploymentDir().toExternalForm(),
				sbbComponent.getClassPool());
		abstractSbbDecorator.decorateAbstractSbb();
		// generate concrete sbb class
		new ConcreteSbbGenerator(sbbComponent).generateConcreteSbb();

		if (logger.isDebugEnabled()) {
			logger.debug("Generated all classes!");
		}
	}

}