package org.mobicents.slee.container.deployment.profile;

import java.util.HashSet;
import java.util.Set;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;
import javax.slee.management.ManagementException;
import javax.slee.profile.ProfileImplementationException;
import javax.slee.profile.ProfileMBean;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.profile.AbstractProfileMBean;

public class ConcreteProfileMBeanGenerator {

	private static final Logger logger = Logger.getLogger(ConcreteProfileManagementGenerator.class);

	private ProfileSpecificationComponent component = null;
	private String cmpProfileInterfaceName = null;

	private String profileManagementInterfaceName = null;
	private ClassPool pool = null;
	private CtClass cmpProfileInterface = null;
	private CtClass profileManagementInterface = null;
	private CtClass profileMBeanConcreteClass = null;
	private CtClass profileMBeanConcreteInterface = null;
	private CtClass sleeProfileMBean = null;
	
	/**
	 * holds all cmp acessor methods to copy to mbean interface and implement in mbean impl
	 */
	private Set<CtMethod> mBeanCmpAcessorMethods = new HashSet<CtMethod>();
	/**
	 * holds all management methods to copy to mbean interface and implement in mbean impl
	 */
	private Set<CtMethod> mBeanManagementMethods = new HashSet<CtMethod>();
	
	public ConcreteProfileMBeanGenerator(ProfileSpecificationComponent component) {
		this.component = component;
		ProfileSpecificationDescriptorImpl descriptor = component.getDescriptor();
		this.cmpProfileInterfaceName = descriptor.getProfileClasses().getProfileCMPInterface().getProfileCmpInterfaceName();
		this.profileManagementInterfaceName = descriptor.getProfileManagementInterface() == null ? null : descriptor.getProfileManagementInterface().getProfileManagementInterfaceName();
		this.pool = component.getClassPool();
	}

	/**
	 * Generates the Profile MBean interface
	 * 
	 * @return the interface generated
	 */
	public void generateProfileMBeanInterface() throws Exception {
		
		if (SleeProfileClassCodeGenerator.checkCombination(component) == -1) {
			throw new DeploymentException("Profile Specification doesn't match any combination " + "from the JSLEE spec 1.0 section 10.5.2");
		}
		
		String profileMBeanConcreteInterfaceName = cmpProfileInterfaceName + "MBean";
		
		profileMBeanConcreteInterface = pool.makeInterface(profileMBeanConcreteInterfaceName);

		try {
			cmpProfileInterface = pool.get(cmpProfileInterfaceName);
			profileManagementInterface = profileManagementInterfaceName != null ? pool.get(profileManagementInterfaceName) : null;
		}
		catch (NotFoundException nfe) {
			throw new DeploymentException("Failed to locate CMP/Management Interface for " + component, nfe);
		}
		
		// Extends the javax.slee.profile.ProfileMBean
		try {
			sleeProfileMBean = pool.get(ProfileMBean.class.getName());
		}
		catch (NotFoundException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		profileMBeanConcreteInterface.addInterface(sleeProfileMBean);
		
		// gather exceptions that the mbean methods may throw
		CtClass[] managementMethodExceptions = new CtClass[3];
		try {
			managementMethodExceptions[0] = pool.get(ManagementException.class.getName());
			managementMethodExceptions[1] = pool.get(InvalidStateException.class.getName());
			managementMethodExceptions[2] = pool.get(ProfileImplementationException.class.getName());
		}
		catch (NotFoundException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		CtClass[] cmpGetAcessorMethodExceptions = new CtClass[] {managementMethodExceptions[0]};
		CtClass[] cmpSetAcessorMethodExceptions = new CtClass[] {managementMethodExceptions[0],managementMethodExceptions[1]};
		
		// gather all Object class methods, we don't want those in the mbean
		Set<CtMethod> objectMethods = new HashSet<CtMethod>();
		try {
			CtClass objectClass = pool.get(Object.class.getName());
			for (CtMethod ctMethod : objectClass.getMethods()) {
				objectMethods.add(ctMethod);
			}
		}
		catch (NotFoundException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		
		// gather methods to copy
		Set<CtMethod> cmpAcessorMethods = new HashSet<CtMethod>();
		Set<CtMethod> managementMethods = new HashSet<CtMethod>();
		if (profileManagementInterface != null) {
			// If the Profile Specification defines a Profile Management Interface, the profileMBean interface has the same methods
			// 1. gather all methods from management interface
			for (CtMethod ctMethod : profileManagementInterface.getMethods()) {
				if (!objectMethods.contains(ctMethod)) {
					managementMethods.add(ctMethod);
				}
			}
			// 2. gather all methods present also in cmp interface, removing those from the ones gather from management interface
			for (CtMethod ctMethod : cmpProfileInterface.getMethods()) {
				if (!objectMethods.contains(ctMethod)) {
					if (managementMethods.remove(ctMethod)) {
						cmpAcessorMethods.add(ctMethod);					
					}
				}
			}
		}
		else {
			for (CtMethod ctMethod : cmpProfileInterface.getMethods()) {
				if (!objectMethods.contains(ctMethod)) {
					cmpAcessorMethods.add(ctMethod);									
				}
			}
		}
		
		// copy cmp acessor & mngt methods
		for (CtMethod ctMethod : cmpAcessorMethods) {
			// copy method
			CtMethod methodCopy = new CtMethod(ctMethod, profileMBeanConcreteInterface, null);
			// set exceptions
			CtClass[] exceptions = null;
			if(ctMethod.getName().startsWith("set")) {
				exceptions = cmpSetAcessorMethodExceptions;							
			}
			else if(ctMethod.getName().startsWith("get")) {
				exceptions = cmpGetAcessorMethodExceptions;
			}
			else {
				throw new DeploymentException("unexpected method in profile cmp interface "+ctMethod);
			}
			methodCopy.setExceptionTypes(exceptions);
			// add to class
			profileMBeanConcreteInterface.addMethod(methodCopy);
			// store in set to be used in mbean impl
			mBeanCmpAcessorMethods.add(methodCopy);
		}
		for (CtMethod ctMethod : managementMethods) {
			// copy method
			CtMethod methodCopy = new CtMethod(ctMethod, profileMBeanConcreteInterface, null);
			// set exceptions
			methodCopy.setExceptionTypes(managementMethodExceptions);
			// add to class
			profileMBeanConcreteInterface.addMethod(methodCopy);
			// store in set to be used in mbean impl
			mBeanManagementMethods.add(methodCopy);

		}
		
		// write class file
		try {
			profileMBeanConcreteInterface.writeFile(this.component.getDeploymentDir().getAbsolutePath());
			if (logger.isDebugEnabled()) {
				logger.debug("Concrete Interface " + cmpProfileInterfaceName + "MBean" + " generated in the following path " + this.component.getDeploymentDir().getAbsolutePath());
			}
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(), e);
		}
		finally {
			profileMBeanConcreteInterface.defrost();	
		}
		
		// and load it to the component
		try {
			this.component.setProfileMBeanConcreteInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(profileMBeanConcreteInterfaceName));
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(), e);
		}
		
		
	}

	/**
	 * This method generates concrete class of MBean impl
	 */
	public void generateProfileMBean() throws Exception
	{
		if (SleeProfileClassCodeGenerator.checkCombination(component) == -1)
		{
			throw new DeploymentException("Profile Specification doesn't match any combination " + "from the JSLEE spec 1.0 section 10.5.2");
		}

		String profileMBeanConcreteClassName = profileMBeanConcreteInterface.getName() + "Impl";

		profileMBeanConcreteClass = pool.makeClass(profileMBeanConcreteClassName);

		// set interface & super class
		try {
			profileMBeanConcreteClass.setInterfaces(new CtClass[] {profileMBeanConcreteInterface});
			profileMBeanConcreteClass.setSuperclass(pool.get(AbstractProfileMBean.class.getName()));			
		}
		catch (NotFoundException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		
		// implement cmp acessor & management methods gather in the mbean interface building
		for (CtMethod method : mBeanCmpAcessorMethods) {
			
			// copy method & remove abstract modifier			
			CtMethod newMethod = CtNewMethod.copy( method, profileMBeanConcreteClass, null );
			// generate body
			String body = null;				
			if (method.getName().startsWith("set")) {
				body = 	"{ " +
						"	boolean resumedTransaction = beforeSetCmpField();" +
						"	try { " +
						"		(("+component.getProfileCmpConcreteClass().getName()+")getProfileObject().getProfileConcrete())." + method.getName()+"($1);" +
						"	} finally {" +
						"		afterSetCmpField(resumedTransaction);" +
						"	}" +
						"}"; 				
			}
			else {
				body = 	"{ " +
						"	boolean activatedTransaction = beforeGetCmpField();" +
						"	try { " +
						"		return ($r) (("+component.getProfileCmpConcreteClass().getName()+")getProfileObject().getProfileConcrete())." + method.getName()+"();" +
						"	} finally {" +
						"		afterGetCmpField(activatedTransaction);" +
						"	}" +
						"}";
			}
			if(logger.isDebugEnabled()) {
				logger.debug("Implemented profile mbean method named "+method.getName()+", with body:\n"+body);
			}
			newMethod.setBody(body);
			profileMBeanConcreteClass.addMethod(newMethod);
		}
		
		for (CtMethod method : mBeanManagementMethods) {
			
			// copy method & remove abstract modifier			
			CtMethod newMethod = CtNewMethod.copy( method, profileMBeanConcreteClass, null );
			
			// generate body
			String body = "{ boolean activatedTransaction = beforeManagementMethodInvocation(); try { ";
			if (!newMethod.getReturnType().equals(CtClass.voidType)) {
				body += "return ($r) ";
			}
			body += "(("+component.getProfileCmpConcreteClass().getName()+")getProfileObject().getProfileConcrete())." + method.getName()+"($$); } catch(Throwable t) { throwableOnManagementMethodInvocation(t); } finally { afterManagementMethodInvocation(activatedTransaction); } throw new "+SLEEException.class.getName()+"(\"bad code generated\"); }"; 				
						
			if(logger.isDebugEnabled()) {
				logger.debug("Implemented profile mbean method named "+method.getName()+", with body:\n"+body);
			}
			newMethod.setBody(body);
			profileMBeanConcreteClass.addMethod(newMethod);
		}		
		
		try {
			profileMBeanConcreteClass.writeFile(this.component.getDeploymentDir().getAbsolutePath());
			if (logger.isDebugEnabled()) {
				logger.debug("Concrete Class " + profileMBeanConcreteClassName + " generated in the following path " + this.component.getDeploymentDir().getAbsolutePath());
			}
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
		finally {
			profileMBeanConcreteClass.defrost();
		}

		try {
			component.setProfileMBeanConcreteImplClass(Thread.currentThread().getContextClassLoader().loadClass(profileMBeanConcreteClassName));
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
	}

}
