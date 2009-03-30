package org.mobicents.slee.container.deployment;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.ClassPool;

/**
 * 
 * @author martins
 *
 */
public class SleeComponentWithUsageParametersClassCodeGenerator {

	/**
	 * Generates classes for a slee component, which defines usage parameters
	 * @param component
	 * @throws DeploymentException
	 */
	public void process(SleeComponentWithUsageParametersInterface component) throws DeploymentException {
		
		ClassPool classPool = component.getClassPool();
		String deploymentDir = component.getDeploymentDir().getAbsolutePath();
		Class usageParametersInterface = component
				.getUsageParametersInterface();
		if (usageParametersInterface != null) {
			try {
				// generate the concrete usage param set class
				component
						.setUsageParametersConcreteClass(new ConcreteUsageParameterClassGenerator(
								usageParametersInterface.getName(),
								deploymentDir, classPool)
								.generateConcreteUsageParameterClass());
				// generate the mbeans
				new ConcreteUsageParameterMBeanGenerator(component)
						.generateConcreteUsageParameterMBean();
			} catch (DeploymentException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new DeploymentException(
						"Failed to generate "+component+" usage parameter class", ex);
			}
		}
		
	}
}
