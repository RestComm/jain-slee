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
