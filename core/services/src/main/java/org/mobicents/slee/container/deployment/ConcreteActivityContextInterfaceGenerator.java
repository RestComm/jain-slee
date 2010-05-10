package org.mobicents.slee.container.deployment;

import java.util.Iterator;
import java.util.Map;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ClassPool;
import org.mobicents.slee.runtime.sbb.SbbActivityContextInterfaceImpl;

/**
 * Class generating the concrete activity context interface class from the
 * activity context interface provided by a sbb developer
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 * @author F.Moggia fixed activitycontext
 */
public class ConcreteActivityContextInterfaceGenerator {

	/**
	 * Logger to logg information
	 */
	private static Logger logger = null;

	/**
	 * Pool to generate or read classes with javassist
	 */
	private final ClassPool pool;

	/**
	 * The path where classes will reside
	 */
	private final String deployDir;

	/**
	 * the sbb activity context interface name
	 */
	private final String activityContextInterfaceName;

	/**
	 * The interface from which to generate the concrete interface
	 */
	protected CtClass activityContextInterface = null;

	/**
	 * The the concrete activity interface
	 */
	protected CtClass concreteActivityContextInterface = null;

	static {
		logger = Logger
				.getLogger(ConcreteActivityContextInterfaceGenerator.class);
	}

	/**
	 * 
	 */
	public ConcreteActivityContextInterfaceGenerator(
			String activityContextInterfaceName, String deployDir,
			ClassPool classPool) {
		this.pool = classPool;
		this.deployDir = deployDir;
		this.activityContextInterfaceName = activityContextInterfaceName;
	}

	/**
	 * Generate the Activity Context Interface Class
	 * 
	 * @param activityContextInterfaceName
	 *            the name of the Activity Context Interface
	 * @return the concrete Activity Context Interface class implementing the
	 *         Activity Context Interface
	 */
	public Class generateActivityContextInterfaceConcreteClass()
			throws DeploymentException {

		String tmpClassName = ConcreteClassGeneratorUtils.CONCRETE_ACTIVITY_INTERFACE_CLASS_NAME_PREFIX
				+ activityContextInterfaceName
				+ ConcreteClassGeneratorUtils.CONCRETE_ACTIVITY_INTERFACE_CLASS_NAME_SUFFIX;

		concreteActivityContextInterface = pool.makeClass(tmpClassName);
		CtClass sbbActivityContextInterface = null;
		try {
			activityContextInterface = pool.get(activityContextInterfaceName);
			sbbActivityContextInterface = pool
					.get(SbbActivityContextInterfaceImpl.class.getName());
		} catch (NotFoundException nfe) {
			throw new DeploymentException("Could not find aci "
					+ activityContextInterfaceName, nfe);
		}

		// Generates the extends link
		ConcreteClassGeneratorUtils.createInheritanceLink(
				concreteActivityContextInterface, sbbActivityContextInterface);

		// Generates the implements link
		ConcreteClassGeneratorUtils.createInterfaceLinks(
				concreteActivityContextInterface,
				new CtClass[] { activityContextInterface });
		
		// Generates the methods to implement from the interface
		Map interfaceMethods = ClassUtils
				.getInterfaceMethodsFromInterface(activityContextInterface);
		generateConcreteMethods(interfaceMethods);
		// generates the class
		String sbbDeploymentPathStr = deployDir;

		try {
			concreteActivityContextInterface.writeFile(sbbDeploymentPathStr);
			if (logger.isDebugEnabled()) {
				logger.debug("Concrete Class " + tmpClassName
						+ " generated in the following path "
						+ sbbDeploymentPathStr);
			}
		} catch (Exception e) {

			logger.error("problem generating concrete class", e);
			throw new DeploymentException(
					"problem generating concrete class! ", e);
		}

		// load the class
		Class clazz = null;
		try {
			clazz = Thread.currentThread().getContextClassLoader().loadClass(
					tmpClassName);
		} catch (Exception e1) {
			logger.error("problem loading generated class", e1);
			throw new DeploymentException(
					"problem loading the generated class! ", e1);
		}

		this.concreteActivityContextInterface.defrost();

		return clazz;

	}

	private boolean isBaseInterfaceMethod(String methodName) {
		if ("getActivity".equals(methodName) || "attach".equals(methodName)
				|| "detach".equals(methodName) || "isEnding".equals(methodName)
				|| "isAttached".equals(methodName)
				|| "equals".equals(methodName) || "hashCode".equals(methodName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Generates the concrete methods of the class It generates a specific
	 * method implementation for the javax.slee.ActivityContextInterface methods
	 * for the methods coming from the ActivityContextInterface developer the
	 * call is routed to the base asbtract class
	 * 
	 * @param interfaceMethods
	 *            the methods to implement coming from the
	 *            ActivityContextInterface developer
	 */
	private void generateConcreteMethods(Map interfaceMethods) {
		if (interfaceMethods == null)
			return;

		Iterator it = interfaceMethods.values().iterator();
		while (it.hasNext()) {
			CtMethod interfaceMethod = (CtMethod) it.next();
			if (interfaceMethod != null
					&& isBaseInterfaceMethod(interfaceMethod.getName()))
				continue; // @todo: need to check args also

			try {
				// copy method from abstract to concrete class
				CtMethod concreteMethod = CtNewMethod.copy(interfaceMethod,
						concreteActivityContextInterface, null);
				// create the method body
				String fieldName = interfaceMethod.getName().substring(3);
				fieldName = fieldName.substring(0, 1).toLowerCase()
						+ fieldName.substring(1);
				String concreteMethodBody = null;
				if (interfaceMethod.getName().startsWith("get")) {
					concreteMethodBody = "{ return ($r)getFieldValue(\""
							+ fieldName + "\","+concreteMethod.getReturnType().getName()+".class); }";
				} else if (interfaceMethod.getName().startsWith("set")) {
					concreteMethodBody = "{ setFieldValue(\"" + fieldName
							+ "\",$1); }";
				} else {
					throw new SLEEException("unexpected method name <"
							+ interfaceMethod.getName()
							+ "> to implement in sbb aci interface");
				}

				if (logger.isTraceEnabled()) {
		            logger.trace("Generated method "
							+ interfaceMethod.getName() + " , body = "
							+ concreteMethodBody);
				}
				concreteMethod.setBody(concreteMethodBody);
				concreteActivityContextInterface.addMethod(concreteMethod);
			} catch (Exception cce) {
				throw new SLEEException("Cannot compile method "
						+ interfaceMethod.getName(), cce);
			}
		}

	}

}