package org.mobicents.slee.container.deployment.profile;

import java.beans.Introspector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileTable;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.profile.ProfileQueryHandler;
import org.mobicents.slee.container.profile.ProfileTableImpl;

public class ConcreteProfileTableGenerator {

	private static final Logger logger = Logger.getLogger(ConcreteProfileTableGenerator.class);
	public static final String _INTERCEPTOR_QUERY = ProfileQueryHandler.class.getName()+".handle";
	public static final String _QUERY_METHOD_NAME_PREFIX = "query";
	private ProfileSpecificationComponent component = null;
	private String cmpProfileInterfaceName = null;
	private String profileTableInterfaceName = null;
	private String profileTableConcreteClassName = null;

	private ClassPool pool;
	private CtClass cmpProfileInterface;
	private CtClass profileTableInterface = null;
	private CtClass sleeProfileTableInterface = null;
	private CtClass mobicentsConcreteProfileTableClass = null;
	private CtClass profileTableConcreteClass = null;

	public ConcreteProfileTableGenerator(ProfileSpecificationComponent component) {
		super();
		this.component = component;
		ProfileSpecificationDescriptorImpl descriptor = component.getDescriptor();
		cmpProfileInterfaceName = descriptor.getProfileClasses().getProfileCMPInterface().getProfileCmpInterfaceName();
		profileTableInterfaceName = descriptor.getProfileClasses().getProfileTableInterface() == null ? null : descriptor.getProfileClasses().getProfileTableInterface().getProfileTableInterfaceName();

		pool = component.getClassPool();
	}

	public void generateProfileTable() throws Exception {
		try {
			cmpProfileInterface = pool.get(cmpProfileInterfaceName);

		} catch (NotFoundException nfe) {
			// nfe.printStackTrace();
			throw new SLEEException("Failed to locate cmp interface class for " + component, nfe);
		}

		try {
			mobicentsConcreteProfileTableClass = pool.get(ProfileTableImpl.class.getName());

		} catch (NotFoundException nfe) {
			// nfe.printStackTrace();
			throw new SLEEException("Failed to locate ProfileTableConcreteImpl class for " + component, nfe);
		}

		try {
			sleeProfileTableInterface = pool.get(ProfileTable.class.getName());

		} catch (NotFoundException nfe) {
			// nfe.printStackTrace();
			throw new SLEEException("Failed to locate ProfileTable interface for " + component, nfe);
		}

		//Those methods are already implemented.
		Map<String,CtMethod> alreadyImplemented = ClassUtils.getInterfaceMethodsFromInterface(sleeProfileTableInterface);
		Map<String, CtMethod> queriesMapToDivert = new HashMap<String, CtMethod>();
		
		CtClass[] targetInterfaces = null;
		
		if (profileTableInterfaceName != null) {
			// we have some interface, also we have methods from generic
			// ProfileTable interface, we want only queries.
			try {
				profileTableInterface = pool.get(profileTableInterfaceName);
				queriesMapToDivert = ClassUtils.getInterfaceMethodsFromInterface(profileTableInterface);
				//Lets remove methods that we already have
				queriesMapToDivert.keySet().removeAll(alreadyImplemented.keySet());
			} catch (NotFoundException nfe) {
				// nfe.printStackTrace();
				throw new DeploymentException("Failed to locate ProfiteTable interface for " + component, nfe);
			}
			profileTableConcreteClassName  = profileTableInterface.getPackageName()+"."+ConcreteClassGeneratorUtils.PROFILE_TABLE_CLASS_NAME_PREFIX+profileTableInterface.getName()+ConcreteClassGeneratorUtils.PROFILE_TABLE_CLASS_NAME_SUFFIX;
			CtClass[] presentInterfaces = this.mobicentsConcreteProfileTableClass.getInterfaces();
			targetInterfaces = new CtClass[presentInterfaces.length + 1];
			for (int index = 0; index < presentInterfaces.length; index++) {
				targetInterfaces[index] = presentInterfaces[index];
			}
			targetInterfaces[targetInterfaces.length - 1] = profileTableInterface;

		} else {

			//There is no custom profile table interface.
			profileTableConcreteClassName  = cmpProfileInterface.getPackageName()+"."+ConcreteClassGeneratorUtils.PROFILE_TABLE_CLASS_NAME_PREFIX+cmpProfileInterfaceName+ConcreteClassGeneratorUtils.PROFILE_TABLE_CLASS_NAME_SUFFIX;
			targetInterfaces = this.mobicentsConcreteProfileTableClass.getInterfaces();
		}
		
		
		//lets make concrete class
		try {
			profileTableConcreteClass = pool.makeClass(profileTableConcreteClassName);
		} catch (Exception e) {
			throw new DeploymentException("Failed to create ProfileTableConcreteClass implementation class.");
		}

		
		ConcreteClassGeneratorUtils.createInterfaceLinks(profileTableConcreteClass, targetInterfaces);
		ConcreteClassGeneratorUtils.createInheritanceLink(profileTableConcreteClass, mobicentsConcreteProfileTableClass);
		
		
		//now we have to instrument queries methods.
		generateQueries(profileTableConcreteClass,queriesMapToDivert);
		
		
		//write and store
		try {
			// @@2.4+ -> 3.4+
			profileTableConcreteClass.writeFile(component.getDeploymentDir().getAbsolutePath());

			if (logger.isDebugEnabled()) {
				logger.debug("Concrete Class " + profileTableConcreteClass.getName() + " generated in the following path " + component.getDeploymentDir().getAbsolutePath());
			}
		} catch (Exception e) {
			String s = "Unexpected exception generating class ";
			logger.error(s, e);
			throw new RuntimeException(s, e);
		} finally {
			// let go, so that it's not holding subsequent deployments of the
			// same profile component.
			// This would not have been necessary is the ClassPool is not one
			// shared instance in the SLEE,
			// but there is instead a hierarchy mimicing the classloader
			// hierarchy. This also makes
			// our deployer essentially single threaded.

			profileTableConcreteClass.defrost();

		}

		Class clazz = null;
		try {
			// load the generated class
			clazz = component.getClassLoader().loadClass(profileTableConcreteClass.getName());
			component.setProfileTableConcreteClass(clazz);
		} catch (ClassNotFoundException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}

		if (component.getProfileTableConcreteClass() == null) {
			throw new DeploymentException("Concrete cmp itnerface class is null, could we possibly fail and not throw exception yet?, " + component);
		}
	}

	private void generateQueries(CtClass profileTableConcreteClass, Map<String, CtMethod> queriesMapToDivert) throws Exception{
		
		Iterator<Map.Entry<String, CtMethod>> it = queriesMapToDivert.entrySet().iterator();
		while(it.hasNext())
		{
			instrumentQuery(profileTableConcreteClass,it.next().getValue(),_INTERCEPTOR_QUERY);
			it.remove();
		}
	}

	private void instrumentQuery(CtClass profileTableConcreteClass, CtMethod method, String interceptorQuery) throws Exception{
		if(logger.isDebugEnabled())
		{
			logger.debug("About to instrument query method: "+method.getName()+", into: "+profileTableConcreteClass);
		}
		String queryName = method.getName();
		if(queryName.startsWith(_QUERY_METHOD_NAME_PREFIX))
		{
			queryName= Introspector.decapitalize(queryName.replace(_QUERY_METHOD_NAME_PREFIX, ""));
		}else
		{
			throw new DeploymentException("Method has wrong prefix, method name: "+queryName);
		}
		
		
		method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
		String body = "{";
	
		body+="return "+_INTERCEPTOR_QUERY+"(this,\""+queryName+"\",$args);";
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Instrument query method: "+method.getName()+", into: "+profileTableConcreteClass+", with body:\n"+body);
		}
		
		body+="}";
		method.setBody(body);
		profileTableConcreteClass.addMethod(method);

	}

}
