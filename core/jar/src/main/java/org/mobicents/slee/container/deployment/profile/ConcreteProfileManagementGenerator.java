package org.mobicents.slee.container.deployment.profile;

import java.util.Iterator;
import java.util.Map;

import javax.slee.management.DeploymentException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor;
import org.mobicents.slee.container.management.SleeProfileManager;

public class ConcreteProfileManagementGenerator {

	private static final Logger logger = Logger.getLogger(ConcreteProfileManagementGenerator.class);
	public static final String _INTERCEPTOR_MANAGEMENT= "profileManagementInterceptor";
	
	private ProfileSpecificationComponent component = null;
	private String cmpProfileInterfaceName =null;
	private String managementAbstractClassName = null;
	private String profileManagementInterfaceName = null;
	
	private ClassPool pool = component.getClassPool();
	private CtClass cmpProfileConcreteClass =null;
	private CtClass cmpProfileInterface = null;
	private CtClass sleeProfileManagementInterface = null;
	private CtClass profileManagementAbstractClass = null;
	private CtClass profileManagementInterface = null;
	public ConcreteProfileManagementGenerator(ProfileSpecificationComponent component) {
		super();
		this.component = component;
		ProfileSpecificationDescriptorImpl descriptor = component.getDescriptor();
		 cmpProfileInterfaceName = descriptor.getProfileClasses().getProfileCMPInterface().getProfileCmpInterfaceName();
		 managementAbstractClassName = descriptor.getProfileAbstractClass() == null ? null : descriptor.getProfileAbstractClass().getProfileAbstractClassName();
		 profileManagementInterfaceName = descriptor.getProfileManagementInterface() == null ? null : descriptor.getProfileManagementInterface().getProfileManagementInterfaceName();
		 
		 pool = component.getClassPool();
	}


	public void generateProfileCmpConcreteClass() throws Exception {

		int combination = SleeProfileClassCodeGenerator.checkCombination(component);
		if (combination == -1) {

			throw new DeploymentException("Profile Specification doesn't match any combination " + "from the JSLEE spec 1.0 section 10.5.2, for " + component);

		}
		// pool.childFirstLookup=true;
		
		String tmpClassName =tmpClassName = ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_PREFIX + cmpProfileInterfaceName + ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_SUFFIX;
		cmpProfileConcreteClass = pool.makeClass(tmpClassName);

		// Implementation of the both ProfileCMP interface and the
		// javax.slee.profile.ProfileManagement Interface.
		
		try {
			cmpProfileInterface = pool.get(cmpProfileInterfaceName);

		} catch (NotFoundException nfe) {
			// nfe.printStackTrace();
			throw new DeploymentException("Failed to locate cmp interface class for " + component, nfe);
		}

		

		try {
			sleeProfileManagementInterface = pool.get("javax.slee.profile.ProfileManagement");

		} catch (NotFoundException nfe) {
			// nfe.printStackTrace();
			throw new DeploymentException("Failed to locate profile management class for " + component, nfe);

		}
		CtClass[] interfaces = null;
		
		// if this is combination 1 or 2,
		// the concrete class implements the CMP Profile interface
		if (combination == 1 || combination == 2) {
			interfaces = new CtClass[2];
			interfaces[0] = cmpProfileInterface;
			interfaces[1] = sleeProfileManagementInterface;

			ConcreteClassGeneratorUtils.createInterfaceLinks(cmpProfileConcreteClass, interfaces);
		}

		else {
			// if this is combination 3 or 4,
			// the the concrete class extends the Concrete Profile Management
			// Abstract Class
			if (managementAbstractClassName != null) {
				try {
					profileManagementAbstractClass = pool.get(managementAbstractClassName);
				} catch (NotFoundException nfe) {
					// nfe.printStackTrace();
					throw new DeploymentException("Profile abstract class could not be found for " + component);
				}
				ConcreteClassGeneratorUtils.createInheritanceLink(cmpProfileConcreteClass, profileManagementAbstractClass);
			}
			if (profileManagementInterfaceName != null) {
				interfaces = new CtClass[3];
				try {
					profileManagementInterface = pool.get(profileManagementInterfaceName);
				} catch (NotFoundException nfe) {
					//nfe.printStackTrace();
					throw new DeploymentException("Profile management interface could not be found for " + component);
				}
				interfaces[2] = profileManagementInterface;
			} else {
				interfaces = new CtClass[2];
			}
			interfaces[0] = cmpProfileInterface;
			interfaces[1] = sleeProfileManagementInterface;
			ConcreteClassGeneratorUtils.createInterfaceLinks(cmpProfileConcreteClass, interfaces);
		}
		// Create the interceptor Fields for the concrete profile class
		// createInterceptorFields();
		// generateFields();
		// createDefaultConstructor();
		// Creates the constructor with parameters
		try {
			String[] parameterNames = { "profileManagementInterceptor", "sleeProfileManager", "profileTable", "profileName" };

			CtClass[] parameters = new CtClass[] { pool.get(ProfileManagementInterceptor.class.getName()), pool.get(SleeProfileManager.class.getName()),

			pool.get(String.class.getName()), pool.get(String.class.getName()) };
			createConstructorWithParameter(parameterNames, parameters, cmpProfileConcreteClass, false,null);
		} catch (NotFoundException nfe) {

			String s = "Unexpected Exception - could not find class. Constructor with parameters not created";
			logger.error(s, nfe);
			throw new RuntimeException(s, nfe);
		}
		// Generates the methods to implement from the CMPProfile interface
		Map cmpProfileInterfaceMethods = ClassUtils.getInterfaceMethodsFromInterface(cmpProfileInterface);
		generateConcreteMethods(cmpProfileConcreteClass,profileManagementAbstractClass , cmpProfileInterfaceMethods,_INTERCEPTOR_MANAGEMENT );

		// Generates the methods to implement from the
		// javax.slee.profile.ProfileManagement interface
		Map profileManagementInterfaceMethods = ClassUtils.getInterfaceMethodsFromInterface(sleeProfileManagementInterface);
		generateConcreteMethods(cmpProfileConcreteClass,profileManagementAbstractClass,profileManagementInterfaceMethods, _INTERCEPTOR_MANAGEMENT);

		try {
			// @@2.4+ -> 3.4+
			cmpProfileConcreteClass.writeFile(component.getDeploymentDir().toExternalForm());

			if (logger.isDebugEnabled()) {
				logger.debug("Concrete Class " + tmpClassName + " generated in the following path " + component.getDeploymentDir().toExternalForm());
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
			
			cmpProfileConcreteClass.defrost();

		}
		createPersistentStateHolderClass(component);

		Class clazz = null;
		try {
			// load the generated class
			clazz = Thread.currentThread().getContextClassLoader().loadClass(tmpClassName);
			component.setProfileCmpConcreteClass(clazz);
		} catch (ClassNotFoundException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(component.getProfileCmpConcreteClass()==null)
		{
			throw new DeploymentException("Concrete cmp itnerface class is null, could we possibly fail and not throw exception yet?, "+component);
		}

	}
	
	
	/**
	 * Creates a constructor with parameters<BR>
	 * For every parameter a field of the same class is created in the concrete
	 * class And each field is gonna be initialized with the corresponding
	 * parameter
	 * 
	 * @param parameters
	 *            the parameters of the constructor to add
	 * @param concreteClass
	 *            the class on which to add the constructor
	 * @param mbean
	 *            tells if it the constructor for the mbean class
	 */
	 static void createConstructorWithParameter(String[] parameterNames, CtClass[] parameters,
			CtClass concreteClass, boolean mbean,String cmpProfileInterfaceName) {

		CtConstructor constructorWithParameter = new CtConstructor(parameters,
				concreteClass);
		String constructorBody = "{";
		if (mbean) {
			constructorBody += "super(" + SleeContainerUtils.class.getName()
					+ ".getCurrentThreadClassLoader().loadClass(\""
					+ cmpProfileInterfaceName + "MBean" + "\"));";// {logger.debug(\"constructor
																	// called\");";//
																	// +
		}
		// "this();";
		for (int i = 0; i < parameters.length; i++) {
			
			try {
				CtField ctField = new CtField(parameters[i], parameterNames[i],
						concreteClass);
				if (ctField.getName().equals("java.lang.Object"))
					ctField.setModifiers(Modifier.PUBLIC);
				else
					ctField.setModifiers(Modifier.PRIVATE);
				concreteClass.addField(ctField);
			} catch (CannotCompileException cce) {
				cce.printStackTrace();
			}
			int paramNumber = i + 1;
			constructorBody += parameterNames[i] + "=$" + paramNumber + ";";
		}

		CtField[] ctFields = concreteClass.getDeclaredFields();
		for (int i = 0; i < ctFields.length; i++) {
			int j = 0;
			boolean isProfileManagementInterceptor = false;
			while (j < parameters.length && !isProfileManagementInterceptor) {
				try {
					if (ctFields[i]
							.getType()
							.getName()
							.equals(ProfileManagementInterceptor.class.getName()))
						isProfileManagementInterceptor = true;
				} catch (NotFoundException e1) {
					e1.printStackTrace();
				}
				j++;
			}
			if (isProfileManagementInterceptor) {
				if (!mbean) {
					constructorBody += ctFields[i].getName()
							+ ".setProfileManager(sleeProfileManager); ";
					constructorBody += ctFields[i].getName()
							+ ".setProfileTableName(profileTable); ";
					constructorBody += ctFields[i].getName()
							+ ".setProfileName(profileName); ";
				} else {
					constructorBody += ctFields[i].getName() + ".setProfile(profile); ";
				}
			}
		}

		constructorBody += "}";
		try {
			concreteClass.addConstructor(constructorWithParameter);
			constructorWithParameter.setBody(constructorBody);
			if(logger.isDebugEnabled()) {
				logger.debug("ConstructorWithParameter created");
			}
		} catch (CannotCompileException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates the concrete methods of the class
	 * 
	 * @param source
	 *            the class in which the methods are going to be implemented
	 * @param interfaceMethods
	 *            the methods to implement coming from the
	 * @param interceptor
	 *            the name of the interceptor field the methods are going to
	 *            invoke
	 * 
	 */
	static void generateConcreteMethods(CtClass source,CtClass profileManagementAbstractClass, Map interfaceMethods,
			String interceptor) {
		if (interfaceMethods == null)
			return;
		Iterator it = interfaceMethods.values().iterator();
		while (it.hasNext()) {
			CtMethod interfaceMethod = (CtMethod) it.next();
			if (interfaceMethod.getName().equals("profileInitialize")
					|| interfaceMethod.getName().equals("profileLoad")
					|| interfaceMethod.getName().equals("profileStore")
					|| interfaceMethod.getName().equals("profileVerify"))
				if (profileManagementAbstractClass == null)
					ConcreteClassGeneratorUtils.addInterceptedMethod(source,
							interfaceMethod, interceptor, false);
				else
					ConcreteClassGeneratorUtils.addInterceptedMethod(source,
							interfaceMethod, interceptor, true);
			else
				ConcreteClassGeneratorUtils.addInterceptedMethod(source,
						interfaceMethod, interceptor, false);
		}
	}

	/**
	 * Create the persistent object that will hold the transient state of the
	 * profile
	 */
	protected void createPersistentStateHolderClass(ProfileSpecificationComponent component) throws Exception{
		// Create the class of the persistent state of the sbb
		
		ProfileSpecificationDescriptorImpl descriptor = component.getDescriptor();
		
	
		String tmpClassName = ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_PREFIX + cmpProfileInterfaceName + ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_SUFFIX;
		ClassPool pool = component.getClassPool();
		String deployPath = component.getDeploymentDir().toExternalForm();
		CtClass cmpProfileInterface = null;
		try {
			cmpProfileInterface = pool.get(cmpProfileInterfaceName);

		} catch (NotFoundException nfe) {
			// nfe.printStackTrace();
			throw new DeploymentException("Failed to locate cmp interface class for " + component, nfe);
		}
		
		
		
		CtClass profilePersisentStateClass = pool.makeClass(tmpClassName);
		
		try {
			// Make the persistent state serializable : this is mandatory by the
			// slee spec
			ConcreteClassGeneratorUtils.createInterfaceLinks(
					profilePersisentStateClass, new CtClass[] { pool
							.get("java.io.Serializable") });
		} catch (NotFoundException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		} finally {
			profilePersisentStateClass.defrost();
		}
		// Create the fields of the sbb persistent state class

		// CtMethod[] methods=cmpProfileInterface.getDeclaredMethods();
		CtMethod[] methods = cmpProfileInterface.getMethods();
		if (methods != null) {
			for (int i = 0; i < methods.length; i++) {
				// we get only the getter method
				// Indeed, the return type will give us the type of the field
				String methodName = methods[i].getName();
				if (methodName.startsWith("get")) {
					if (!methodName.equals("getClass")) {
						if(logger.isDebugEnabled()) {
							logger
							.debug("ConcreteProfileManagementGenerator MethodName = "
									+ methodName);
						}
						// the lowerstring cure below is necessary to comply
						// with JavaBean property naming conventions
						String fieldName = methodName.substring("get".length(),
								"get".length() + 1).toLowerCase()
								+ methodName.substring("get".length() + 1);
						try {
							CtClass fieldType = methods[i].getReturnType();
							CtField persistentField = new CtField(fieldType,
									fieldName, profilePersisentStateClass);
							persistentField.setModifiers(Modifier.PUBLIC);
							profilePersisentStateClass
									.addField(persistentField);
						} catch (Exception e) {
							String s = " Error generating profile";
							logger.error(s, e);
							throw new RuntimeException(s, e);
						}
					}
				}
			}
		}

		// generate the persistent state class of the sbb
		try {
			
			profilePersisentStateClass.writeFile(deployPath);
			
			if(logger.isDebugEnabled()) {
				logger
				.debug("Concrete Class "
						+ tmpClassName
						+ " generated in the following path " + deployPath);
			}
		} catch (Exception e) {
			logger.error("Bad error generating profile !", e);
			throw new RuntimeException(
					"Unrecoverable  error genrating profile", e);
		}
		finally {
			profilePersisentStateClass.defrost();
		}
		
		try {
			Class clazz = Thread.currentThread().getContextClassLoader().loadClass(
					tmpClassName);
			
			//FIXME: ??
			component.setProfilePersistanceTransientStateConcreteClass(clazz);
		} catch (ClassNotFoundException e1) {
			throw new DeploymentException("Failed to load transient class",e1);
		}
	}


	public CtClass getProfileManagementAbstractClass() {
		return profileManagementAbstractClass;
	}
	
	
	
}
