package org.mobicents.slee.container.deployment.profile;

import java.util.Iterator;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.profile.ProfileLocalObjectImpl;
import org.mobicents.slee.container.profile.ProfileObject;

public class ConcreteProfileLocalObjectGenerator {

	private static final Logger logger = Logger.getLogger(ConcreteProfileLocalObjectGenerator.class);
	
	public static final String _PLO_CMP_INTERCEPTOR = "profileObject";
	public static final String _PLO_MGMT_INTERCEPTOR = "profileObject";
	
	private ProfileSpecificationComponent component = null;
	private String cmpProfileInterfaceName = null;

	private String profileLocalInterfaceName = null;

	private ClassPool pool = null;
	private CtClass mobicentsProfileLocalInterfaceConcrete = null;
	private CtClass profileLocalConcreteClass = null;
	private CtClass userDefinedProfileLocalInterface = null;

	private CtClass cmpProfileInterface = null;

	public ConcreteProfileLocalObjectGenerator(ProfileSpecificationComponent component)
	{
		super();
		this.component = component;
		ProfileSpecificationDescriptorImpl descriptor = component.getDescriptor();
		cmpProfileInterfaceName = descriptor.getProfileClasses().getProfileCMPInterface().getProfileCmpInterfaceName();
		profileLocalInterfaceName = descriptor.getProfileLocalInterface() == null ? null : descriptor.getProfileLocalInterface().getProfileLocalInterfaceName();
		pool = component.getClassPool();
	}

	public void generateProfileLocalConcreteClass() throws Exception
	{
		String profileLocalConcreteClassName = null;
		
		try
		{
			// This is the class we will extend
			mobicentsProfileLocalInterfaceConcrete = this.pool.get(ProfileLocalObjectImpl.class.getName());
      cmpProfileInterface = this.pool.get(cmpProfileInterfaceName);
		}
		catch (NotFoundException nfe) {
			throw new DeploymentException("Failed to locate required class for " + component, nfe);
		}

		Map<String, CtMethod> cmpInterfaceMethods = ClassUtils.getInterfaceMethodsFromInterface(cmpProfileInterface);
		Map<String, CtMethod> ploManagementMethods = null;

		profileLocalConcreteClassName = cmpProfileInterface.getPackageName() + "." + ConcreteClassGeneratorUtils.PROFILE_LOCAL_CLASS_NAME_PREFIX + cmpProfileInterfaceName + "PLO"
				+ ConcreteClassGeneratorUtils.PROFILE_LOCAL_CLASS_NAME_SUFFIX;

		try {
			profileLocalConcreteClass = pool.makeClass(profileLocalConcreteClassName);
		} catch (Exception e) {
			throw new DeploymentException("Failed to create Porfile Local Interface implementation class.");
		}

		CtClass[] targetInterface = null;
		
		if (profileLocalInterfaceName != null)
		{
			try
			{
				userDefinedProfileLocalInterface = pool.get(profileLocalInterfaceName);
			}
			catch (NotFoundException nfe) {
				// nfe.printStackTrace();
				throw new DeploymentException("Failed to locate cmp interface class for " + component, nfe);
			}

			CtClass[] presentInterfaces = mobicentsProfileLocalInterfaceConcrete.getInterfaces();
			targetInterface = new CtClass[presentInterfaces.length + 1];
			for (int index = 0; index < presentInterfaces.length; index++)
			{
				targetInterface[index] = presentInterfaces[index];
			}
			
			targetInterface[targetInterface.length - 1] = userDefinedProfileLocalInterface;

			ploManagementMethods = ClassUtils.getInterfaceMethodsFromInterface(userDefinedProfileLocalInterface);

		}
		else
		{
			ploManagementMethods = ClassUtils.getInterfaceMethodsFromInterface(cmpProfileInterface);
			targetInterface = mobicentsProfileLocalInterfaceConcrete.getInterfaces();
		}
		
		// Create interface and inheritance links
		ConcreteClassGeneratorUtils.createInterfaceLinks(profileLocalConcreteClass, targetInterface);
		ConcreteClassGeneratorUtils.createInheritanceLink(profileLocalConcreteClass, mobicentsProfileLocalInterfaceConcrete);
		// generate constructor
	    try {
	      CtClass[] constructorParameters = new CtClass[] { pool.get(ProfileObject.class.getName()) };
	      String constructorBody = "{ super($$) }";
	      CtConstructor constructor = CtNewConstructor.make(constructorParameters, new CtClass[]{}, constructorBody , profileLocalConcreteClass);
	      profileLocalConcreteClass.addConstructor(constructor);
	    } catch (CannotCompileException e) {
	      throw new SLEEException(e.getMessage(),e);
	    }
		// now lets generate CMPs
		generateCmps(cmpInterfaceMethods, ploManagementMethods, profileLocalConcreteClass);
		// and mgmt methods
		generateManagementMethods(ploManagementMethods, profileLocalConcreteClass);

		try
		{
			profileLocalConcreteClass.writeFile(component.getDeploymentDir().getAbsolutePath());

			if (logger.isDebugEnabled()) {
				logger.debug("Concrete Class " + profileLocalConcreteClassName + " generated in the following path " + component.getDeploymentDir().getAbsolutePath());
			}
		}
		catch (Exception e) {
			String s = "Unexpected exception generating class ";
			logger.error(s, e);
			throw new RuntimeException(s, e);
		}
		finally {
			// let go, so that it's not holding subsequent deployments of the same profile component.
			// This would not have been necessary is the ClassPool is not one shared instance in the SLEE,
			// but there is instead a hierarchy mimicing the classloader hierarchy. This also makes
			// our deployer essentially single threaded.

			profileLocalConcreteClass.defrost();
		}

		Class clazz = null;
		try
		{
			// load the generated class
			clazz = component.getClassLoader().loadClass(profileLocalConcreteClassName);
			component.setProfileLocalObjectConcreteClass(clazz);
		}
		catch (ClassNotFoundException cnfe) {
		  cnfe.printStackTrace();
		}

		if (component.getProfileLocalObjectConcreteClass() == null)
		{
			throw new DeploymentException("Concrete cmp itnerface class is null, could we possibly fail and not throw exception yet?, " + component);
		}
	}

	private void generateManagementMethods(Map<String, CtMethod> ploManagementMethods, CtClass profileLocalConcreteClass) throws Exception
	{
		Iterator<Map.Entry<String, CtMethod>> it = ploManagementMethods.entrySet().iterator();

		while (it.hasNext())
		{
			Map.Entry<String, CtMethod> entry = it.next();

			instrumentBussinesMethod(profileLocalConcreteClass, entry.getValue(), _PLO_MGMT_INTERCEPTOR);
			it.remove();
		}
	}

	private void instrumentBussinesMethod(CtClass concreteClass, CtMethod method, String interceptorAccess) throws Exception
	{
    CtMethod newMethod = CtNewMethod.copy( method, concreteClass, null );
    
		// so we are not abstract
    newMethod.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
		String returnStatement = "";
		boolean hasReturnValue = false;
		if(method.getReturnType().toString().compareTo("void")==0)
		{
			returnStatement="return";
		}else
		{
			hasReturnValue = true;
			returnStatement ="return ($r)";
		}
		
		String body="{ "+
//		_PLO_PO_ALLOCATION+
//		"final ProfileObject localObject = this.profileObject;"+
//		"Thread t = Thread.currentThread();"+
//		"ClassLoader oldClassLoader = t.getContextClassLoader();"+
//		"t.setContextClassLoader(localObject.getProfileSpecificationComponent().getClassLoader());"+
//		"try{" +
//				"Object result" +
//				"if(_PLO_ISOLATE && "+SleeContainer.class.getName()+".isSecurityEnabled())" +
//				"{";
//					if(hasReturnValue)
//					{
//						body+="  result =AccessController.doPrivileged(new PrivilegedAction(){"+
//
//						"public Object run(){"+
//     						"return "+interceptorAccess+"."+method.getName()+"($$);"+
//     						"}});";
//					}
//					else{
//					body+=" AccessController.doPrivileged(new PrivilegedAction(){"+
//
//						"public Object run(){"+
//							interceptorAccess+"."+method.getName()+"($$);"+
//     						"return null;"+
//     						"}});";
//					}
//				body+="}else{";
//				if(hasReturnValue)
//					body+="result = "+interceptorAccess+"."+method.getName()+"($$);";
//				else
//					body+=" = "+interceptorAccess+"."+method.getName()+"($$);";
//				body+="}";	
//		
//		
//		
//		body+="rollback = false;";
//		if(hasReturnValue)
//			body+=returnStatement+"result;";
//		else	
//			body+=returnStatement+";";		
//		
//		body+=	
//		"}catch(java.lang.RuntimeException re)"+
//		"{"+
//		"	try {"+
//		"		this.sleeTransactionManager.rollback();"+
//		"		throw new "+TransactionRolledbackLocalException.class.getName()+"(\"ProfileLocalObject inocation results in RuntimeException, rolling back.\",re);"+
//		"	} catch (Exception e) {"+
//		"		throw new "+SLEEException.class.getName()+"(\"System level failure.,\",e);"+ 
//		"	}"+ 
//		"}" +
//		"finally"+
//		"{"+
//		"	t.setContextClassLoader(oldClassLoader);"+
//		"}"+
	"}";
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Instrumented method, name: " + method.getName() + ", with body:\n" + body);
		}
		
		newMethod.setBody(body);
		concreteClass.addMethod(newMethod);
	}
	
	/**
	 * Generates CMP field relations - it delegates calls to {@link _PLO_CMP_INTERCEPTOR} code.
	 * @param cmpProfileInterfaceMethods - cmp methods. This is required, since we must know which methods from {@link ploManagementMethods} are CMP methods and not bussines methods.
	 * @param ploManagementMethods - contains all methods decale
	 * @param profileLocalConcreteClass
	 * @throws Exception
	 */
	private void generateCmps(Map<String, CtMethod> cmpProfileInterfaceMethods, Map<String, CtMethod> ploManagementMethods, CtClass profileLocalConcreteClass) throws Exception {

		Iterator<Map.Entry<String, CtMethod>> it = ploManagementMethods.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, CtMethod> entry = it.next();

			if (cmpProfileInterfaceMethods.containsKey(entry.getKey())) {
				it.remove();

				instrumentCmpMethod(profileLocalConcreteClass, entry.getValue(), entry.getValue().getName().startsWith(ClassUtils.GET_PREFIX), _PLO_CMP_INTERCEPTOR);

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Not instrumenting method into Profile Local Interface impl as it is management method, name: " + entry.getValue().getName());
				}
			}

		}

	}

	private void instrumentCmpMethod(CtClass concreteClass, CtMethod method, boolean isGet, String interceptorAccess) throws Exception
	{
		//FIXME: for now we have the same part of code as for bussnes
	  
	  CtMethod newMethod = CtNewMethod.copy( method, concreteClass, null );
	  
		// so we are not abstract
	  newMethod.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
	  
		String returnStatement = "";
		boolean hasReturnValue = false;
		if(method.getReturnType().toString().compareTo("void")==0)
		{
			returnStatement="";
		}else
		{
			hasReturnValue = true;
			returnStatement ="return ($r)";
		}
		String body="{  " + returnStatement + interceptorAccess+"."+method.getName()+"($$);" + "}";
//		_PLO_PO_ALLOCATION+
//		"final ProfileObject localObject = this.profileObject;"+
//		"try{" +
//		"Object result;" +
//		"if(_PLO_ISOLATE && "+SleeContainer.class.getName()+".isSecurityEnabled())" +
//		"{";
//			if(hasReturnValue)
//			{
//				body+="  result =AccessController.doPrivileged(new PrivilegedAction(){"+
//
//				"public Object run(){"+
//						"return "+interceptorAccess+"."+method.getName()+"($$);"+
//						"}});";
//			}
//			else{
//			body+=" AccessController.doPrivileged(new PrivilegedAction(){"+
//
//				"public Object run(){"+
//					interceptorAccess+"."+method.getName()+"($$);"+
//						"return null;"+
//						"}});";
//			}
//		body+="}else{";
//			if(hasReturnValue)
//				body+="result = "+interceptorAccess+"."+method.getName()+"($$);";
//			else
//				body+=" = "+interceptorAccess+"."+method.getName()+"($$);";
//	    body+="}";	
//		body+="rollback = false;";
//		if(hasReturnValue)
//			body+=returnStatement+"result;";
//		else	
//			body+=returnStatement+";";		
//
//		body+=	
//		"}catch(java.lang.RuntimeException re)"+
//		"{"+
//		"	try {"+
//		"		this.sleeTransactionManager.rollback();"+
//		"		throw new "+TransactionRolledbackLocalException.class.getName()+"(\"ProfileLocalObject invocation results in RuntimeException, rolling back.\",re);"+
//		"	} catch (Exception e) {"+
//		"		throw new "+SLEEException.class.getName()+"(\"System level failure.\",e);"+ 
//		"	}"+ 
//		"}" +
//	"}";
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Instrumented method, name:"+method.getName()+", with body:\n"+body);
		}
		
		newMethod.setBody(body);
		concreteClass.addMethod(newMethod);
	}
}
