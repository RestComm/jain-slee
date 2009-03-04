/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * SbbDeployer.java
 * 
 * Created on Jul 26, 2004
 *
 */
package org.mobicents.slee.container.deployment;

import javax.slee.management.DeploymentException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;

/**
 * Class to control generation of concrete classes from provided interface and
 * abstract classes.
 * 
 */
public class SbbClassCodeGenerator {
	/**
	 * Logger to logg information
	 */
	private static Logger logger = Logger.getLogger(SbbClassCodeGenerator.class);

	public void process(SbbComponent sbbComponent) throws DeploymentException {
		
		ClassPool classPool = sbbComponent.getClassPool();
		String deploymentDir = sbbComponent.getDeploymentDir().toExternalForm();
		Class usageParametersInterface = sbbComponent.getUsageParametersInterface();
		if (usageParametersInterface != null) {
			try {
				sbbComponent.setUsageParametersInterfaceConcreteClass(new ConcreteUsageParameterClassGenerator(
						usageParametersInterface.getName(),deploymentDir,classPool)
				.generateConcreteUsageParameterClass());
			} catch (Exception ex) {
				throw new DeploymentException(
						"Failed to generate sbb usage parameter class", ex);
			}
		}

		// Enhancement code goes here...
		SbbAbstractDecorator abstractSbbDecorator = new SbbAbstractDecorator(sbbComponent.getAbstractSbbClass().getName(),deploymentDir,
				classPool);
		abstractSbbDecorator.decorateAbstractSbb();

		ConcreteSbbGenerator concreteSbbGenerator = new ConcreteSbbGenerator(sbbComponent);
		Class clazz = concreteSbbGenerator.generateConcreteSbb();

		// TODO if the class has been generated, delete it from the disk
		if (clazz != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Generated all classes!");
			}
		} else
			throw new DeploymentException(" Could not deploy Sbb "
					+ sbbComponent);

	}
	
}