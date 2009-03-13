package org.mobicents.slee.container.deployment.profile;

import java.io.IOException;
import java.util.Map;

import javax.slee.management.DeploymentException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;

public class ConcreteProfileMBeanGenerator {

	private static final Logger logger = Logger.getLogger(ConcreteProfileManagementGenerator.class);
	public static final String _INTERCEPTOR_MANAGEMENT = "profileManagementInterceptor";

	private ProfileSpecificationComponent component = null;
	private String cmpProfileInterfaceName = null;

	private String profileManagementInterfaceName = null;
	private ClassPool pool = component.getClassPool();
	private CtClass cmpProfileInterface = null;
	private CtClass profileManagementInterface = null;
	private CtClass profileMBeanConcreteClass = null;
	private CtClass profileMBeanConcreteInterface = null;
	private CtClass sleeProfileMBean = null; 
	private CtClass profileManagementAbstractClass = null;
	public ConcreteProfileMBeanGenerator(ProfileSpecificationComponent component, CtClass profileManagementAbstractClass) {
		super();
		this.component = component;
		ProfileSpecificationDescriptorImpl descriptor = component.getDescriptor();
		this.cmpProfileInterfaceName = descriptor.getProfileClasses().getProfileCMPInterface().getProfileCmpInterfaceName();
		this.profileManagementInterfaceName = descriptor.getProfileManagementInterface() == null ? null : descriptor.getProfileManagementInterface().getProfileManagementInterfaceName();
		this.pool = component.getClassPool();
		this.profileManagementAbstractClass= profileManagementAbstractClass;
		
	}

	/**
	 * Generates the Profile MBean interface
	 * 
	 * @return the interface generated
	 */
	public void generateProfileMBeanInterface() throws Exception {

		int combination = SleeProfileClassCodeGenerator.checkCombination(component);
		if (combination == -1) {
			throw new DeploymentException("Profile Specification doesn't match any combination " + "from the JSLEE spec 1.0 section 10.5.2");

		}
		// Extends the javax.slee.profile.ProfileMBean

		profileMBeanConcreteInterface = pool.makeInterface(cmpProfileInterfaceName + "MBean");
		
		try {
			sleeProfileMBean = pool.get("javax.slee.profile.ProfileMBean");
		} catch (NotFoundException nfe) {
			nfe.printStackTrace();
			throw new DeploymentException("Failed to locate MBean class for " + this.component);
		}
		ConcreteClassGeneratorUtils.createInterfaceLinks(profileMBeanConcreteInterface, new CtClass[] { sleeProfileMBean });
		CtClass[] exceptions = new CtClass[3];
		try {
			exceptions[0] = pool.get("javax.slee.management.ManagementException");
			exceptions[1] = pool.get("javax.slee.InvalidStateException");
			exceptions[2] = pool.get("javax.slee.profile.ProfileImplementationException");
		} catch (NotFoundException nfe) {
			throw new DeploymentException("Failed to locate exception class for " + component, nfe);
		}
		// If the Profile Specification defines a Profile Management Interface,
		// the profileMBean interface has the same methods
		if (profileManagementInterfaceName != null) {
			try {
				profileManagementInterface = pool.get(profileManagementInterfaceName);
			} catch (NotFoundException nfe) {
				throw new DeploymentException("Failed to locate managemnt interface class for " + component, nfe);
			}
			ConcreteClassGeneratorUtils.copyMethods(profileManagementInterface, profileMBeanConcreteInterface, exceptions);
		}
		// otherwise it has all the methods of the CMP profile interface
		else {
			try {
				cmpProfileInterface = pool.get(cmpProfileInterfaceName);
			} catch (NotFoundException nfe) {
				throw new DeploymentException("Failed to locate cmp interface class for " + component, nfe);
			}
			ConcreteClassGeneratorUtils.copyMethods(cmpProfileInterface, profileMBeanConcreteInterface, exceptions);
		}
		try {
			// @@2.4+ -> 3.4+
			// pool.writeFile(cmpProfileInterfaceName+"MBean", deployPath);
			pool.get(cmpProfileInterfaceName + "MBean").writeFile(this.component.getDeploymentDir().toExternalForm());
			if (logger.isDebugEnabled()) {
				logger.debug("Concrete Interface " + cmpProfileInterfaceName + "MBean" + " generated in the following path " + this.component.getDeploymentDir().toExternalForm());
			}
		} catch (Exception e) {
			throw new DeploymentException("Encountered exception while generating class.", e);
		} finally {
			if (profileMBeanConcreteInterface != null)
				profileMBeanConcreteInterface.defrost();
		}

		Class clazz = null;
		try {
			clazz = Thread.currentThread().getContextClassLoader().loadClass(cmpProfileInterfaceName + "MBean");
			this.component.setProfileMBeanConcreteInterfaceClass(clazz);
		} catch (ClassNotFoundException e) {
			throw new DeploymentException("Encountered exception while generating class.", e);
		}

		if(this.component.getProfileMBeanConcreteInterfaceClass() == null)
		{
			throw new DeploymentException("Failed to generate MBean interface, some sort of bug?");
		}
		
	}

	/**
	 * This method generates concrete class of MBean impl
	 */
	public void generateProfileMBean() throws Exception {
		int combination = SleeProfileClassCodeGenerator.checkCombination(component);
		if (combination == -1) {
			throw new DeploymentException("Profile Specification doesn't match any combination " + "from the JSLEE spec 1.0 section 10.5.2");

		}
		
		String tmpClassName=ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_PREFIX + cmpProfileInterfaceName + ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_SUFFIX;
							
		profileMBeanConcreteClass = pool.makeClass(tmpClassName);
		
		// Implements the javax.management.StandardMBean
		CtClass jmxMBean = null;
		try {
			jmxMBean = pool.get("javax.management.StandardMBean");
		} catch (NotFoundException nfe) {
			throw new DeploymentException("Failed to locate requried class.",nfe);
		}
		// Implements the javax.slee.profile.ProfileMBean
		/*
		 * try{ sleeProfileMBean=pool.get("javax.slee.profile.ProfileMBean"); }
		 * catch(NotFoundException nfe){ nfe.printStackTrace(); return null; }
		 */
		// Implements the ProfileMBean previously generated
		try {
			profileMBeanConcreteInterface = pool.get(cmpProfileInterfaceName
					+ "MBean");
		} catch (NotFoundException nfe) {
			throw new DeploymentException("Failed to locate requried class.",nfe);
		}
		CtClass[] interfaces = new CtClass[2];
		interfaces[0] = sleeProfileMBean;
		interfaces[1] = profileMBeanConcreteInterface;
		ConcreteClassGeneratorUtils.createInterfaceLinks(
				profileMBeanConcreteClass, interfaces);
		ConcreteClassGeneratorUtils.createInheritanceLink(
				profileMBeanConcreteClass, jmxMBean);
		// createMBeanInterceptorFields();
		// createDefaultMBeanConstructor();
		// createInterceptorFields();
		// createDefaultConstructor();
		// Creates the constructor with parameters
		try {
			String [] parameterNames = { _INTERCEPTOR_MANAGEMENT, "profile" };
			CtClass[] parametersClasses = new CtClass[] {
					pool
							.get("org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor"),
					pool.get("java.lang.Object") };
			ConcreteProfileManagementGenerator.createConstructorWithParameter(parameterNames,parametersClasses,
					profileMBeanConcreteClass, true,this.component.getDescriptor().getProfileCMPInterface().getProfileCmpInterfaceName());
		} catch (NotFoundException nfe) {
			logger.error("Constructor With Parameter not created");
			nfe.printStackTrace();
		}
	

		// implements methods defined in the profileMBean interface previously
		// generated
		Map profileMBeanInterfaceMethods = ClassUtils
				.getInterfaceMethodsFromInterface(profileMBeanConcreteInterface);



		ConcreteProfileManagementGenerator.generateConcreteMethods(profileMBeanConcreteClass,profileManagementAbstractClass,
				profileMBeanInterfaceMethods, _INTERCEPTOR_MANAGEMENT);

		try {
			// @@2.4+ -> 3.4+
			// pool.writeFile(
			// ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_PREFIX+
			// cmpProfileInterfaceName+
			// ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_SUFFIX,
			// deployPath);
			pool.get(tmpClassName).writeFile(this.component.getDeploymentDir().toExternalForm());
			if(logger.isDebugEnabled()) {
				logger
				.debug("Concrete Class "
						+ tmpClassName
						+ " generated in the following path " + this.component.getDeploymentDir().toExternalForm());
			}
		} catch (NotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} finally {
			profileMBeanConcreteClass.defrost();
		}

		Class clazz = null;
		try {
			clazz = Thread
					.currentThread()
					.getContextClassLoader()
					.loadClass(
							tmpClassName);
			this.component.setProfileMBeanConcreteImplClass(clazz);
		} catch (ClassNotFoundException e1) {
			throw new DeploymentException("Failed to load concrete MBean impl class",e1);
		}
		
		if(this.component.getProfileMBeanConcreteImplClass()==null)
		{
			throw new DeploymentException("Failed to obtain concrete MBean impl class, bug?");
		}
		
		
	}

}
