package org.mobicents.slee.container.deployment;

import javax.slee.management.DeploymentException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;

/**
 * Class to control generation of concrete classes from provided interface and
 * abstract classes.
 * @author martins
 */
public class SbbClassCodeGenerator {

	private static Logger logger = Logger
			.getLogger(SbbClassCodeGenerator.class);

	public void process(SbbComponent sbbComponent) throws DeploymentException {

		ClassPool classPool = sbbComponent.getClassPool();
		String deploymentDir = sbbComponent.getDeploymentDir().toExternalForm();
		Class usageParametersInterface = sbbComponent
				.getUsageParametersInterface();
		if (usageParametersInterface != null) {
			try {
				// generate the concrete usage param set class
				sbbComponent
						.setUsageParametersConcreteClass(new ConcreteUsageParameterClassGenerator(
								usageParametersInterface.getName(),
								deploymentDir, classPool)
								.generateConcreteUsageParameterClass());
				// generate the mbeans
				new ConcreteUsageParameterMBeanGenerator(sbbComponent)
						.generateConcreteUsageParameterMBean();
			} catch (DeploymentException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new DeploymentException(
						"Failed to generate sbb usage parameter class", ex);
			}
		}

		// Enhancement code goes here...
		SbbAbstractDecorator abstractSbbDecorator = new SbbAbstractDecorator(
				sbbComponent.getAbstractSbbClass().getName(), deploymentDir,
				classPool);
		abstractSbbDecorator.decorateAbstractSbb();
		// generate concrete sbb class
		new ConcreteSbbGenerator(sbbComponent).generateConcreteSbb();

		if (logger.isDebugEnabled()) {
			logger.debug("Generated all classes!");
		}
	}

}