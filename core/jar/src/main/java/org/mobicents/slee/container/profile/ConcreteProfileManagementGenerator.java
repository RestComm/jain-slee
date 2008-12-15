/***************************************************
 *                                                 *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ConcreteProfileManagementGenerator.java
 * 
 * Created on 30 sept. 2004
 *
 */
package org.mobicents.slee.container.profile;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javax.slee.profile.ProfileSpecificationDescriptor;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;

/**
 * Class generating a concrete profile implementation class from the Sbb
 * Developer's provided Profile Specification.
 * 
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 * 
 */
public class ConcreteProfileManagementGenerator {
	/**
	 * Logger to log information
	 */
	private static Logger logger = Logger
			.getLogger(ConcreteProfileManagementGenerator.class);

	/**
	 * Pool to generate or read classes with javassist
	 */
	private ClassPool pool = null;

	/**
	 * Concrete class to be generated
	 */
	private CtClass cmpProfileConcreteClass = null;

	/**
	 * CMPProfile Interface
	 */
	private CtClass cmpProfileInterface = null;

	/**
	 * profile Management Interface
	 */
	private CtClass profileManagementInterface = null;

	/**
	 * profile Management Abstract Class
	 */
	private CtClass profileManagementAbstractClass = null;

	/**
	 * javax.slee.profile.ProfileManagementInterface's javasist representation
	 */
	private CtClass sleeProfileManagementInterface = null;

	/**
	 * CMP Profile Interface name
	 */
	private String cmpProfileInterfaceName = null;

	/**
	 * Profile Management Interface name
	 */
	private String profileManagementInterfaceName = null;

	/**
	 * Profile Management abstract class name
	 */
	private String managementAbstractClassName = null;

	/**
	 * Profile Specification combination from the JSLEE spec 1.0 section 10.5.2
	 */
	private int combination = -1;

	private CtClass profileMBeanConcreteInterface;

	private CtClass sleeProfileMBean;

	private CtClass profileMBeanConcreteClass;

	private String deployPath;

	/**
	 * @param profileSpecificationDescriptor
	 */
	public ConcreteProfileManagementGenerator(
			ProfileSpecificationDescriptor profileSpecificationDescriptor) {
		this.deployPath = ((DeployableUnitIDImpl) ((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
				.getDeployableUnit()).getDUDeployer()
				.getTempClassDeploymentDir().getAbsolutePath();
		this.pool = ((DeployableUnitIDImpl) ((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
					.getDeployableUnit()).getDUDeployer().getClassPool();
		
		//if (pool.doPruning)
		//	pool.doPruning = false;
		
		cmpProfileInterfaceName = ((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
				.getCMPInterfaceName();
		profileManagementInterfaceName = ((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
				.getManagementInterfaceName();
		managementAbstractClassName = ((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
				.getManagementAbstractClassName();

		// check the combination from the JSLEE spec 1.0 section 10.5.2
		combination = ProfileVerifier.checkCombination(cmpProfileInterfaceName,
				profileManagementInterfaceName, managementAbstractClassName);
	}

	/**
	 * generating a concrete profile implementation class from the Sbb
	 * Developer's provided Profile Specification.
	 * 
	 * @return The Implementation Class (Concrete Class)
	 */
	public Class generateConcreteProfileCMP() {
		if (combination == -1) {
			logger.error("Profile Specification doesn't match any combination "
					+ "from the JSLEE spec 1.0 section 10.5.2");
			return null;
		}
		// pool.childFirstLookup=true;
		String tmpClassName = ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_PREFIX+ cmpProfileInterfaceName+ ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_SUFFIX;
		
		cmpProfileConcreteClass = pool.makeClass(tmpClassName);
		
		// Implementation of the both ProfileCMP interface and the
		// javax.slee.profile.ProfileManagement Interface.
		try {
			cmpProfileInterface = pool.get(cmpProfileInterfaceName);

		} catch (NotFoundException nfe) {
			nfe.printStackTrace();
			return null;
		}
		try {
			sleeProfileManagementInterface = pool
					.get("javax.slee.profile.ProfileManagement");

		} catch (NotFoundException nfe) {
			nfe.printStackTrace();
			return null;
		}
		CtClass[] interfaces = null;
		// if this is combination 1 or 2,
		// the concrete class implements the CMP Profile interface
		if (combination == 1 || combination == 2) {
			interfaces = new CtClass[2];
			interfaces[0] = cmpProfileInterface;
			interfaces[1] = sleeProfileManagementInterface;

			ConcreteClassGeneratorUtils.createInterfaceLinks(
					cmpProfileConcreteClass, interfaces);
		}
		// if this is combination 3 or 4,
		// the the concrete class extends the Concrete Profile Management
		// Abstract Class
		else {
			if (managementAbstractClassName != null) {
				try {
					profileManagementAbstractClass = pool
							.get(managementAbstractClassName);
				} catch (NotFoundException nfe) {
					nfe.printStackTrace();
					return null;
				}
				ConcreteClassGeneratorUtils
						.createInheritanceLink(cmpProfileConcreteClass,
								profileManagementAbstractClass);
			}
			if (profileManagementInterfaceName != null) {
				interfaces = new CtClass[3];
				try {
					profileManagementInterface = pool
							.get(profileManagementInterfaceName);
				} catch (NotFoundException nfe) {
					nfe.printStackTrace();
					return null;
				}
				interfaces[2] = profileManagementInterface;
			} else {
				interfaces = new CtClass[2];
			}
			interfaces[0] = cmpProfileInterface;
			interfaces[1] = sleeProfileManagementInterface;
			ConcreteClassGeneratorUtils.createInterfaceLinks(
					cmpProfileConcreteClass, interfaces);
		}
		// Create the interceptor Fields for the concrete profile class
		// createInterceptorFields();
		// generateFields();
		// createDefaultConstructor();
		// Creates the constructor with parameters
		try {
			CtClass[] parameters = new CtClass[] {
					pool
							.get("org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor"),
					pool
							.get("org.mobicents.slee.container.profile.SleeProfileManager"),
					pool.get("java.lang.String") };
			createConstructorWithParameter(parameters, cmpProfileConcreteClass,
					false);
		} catch (NotFoundException nfe) {

			String s = "Unexpected Exception - could not find class. Constructor with parameters not created";
			logger.error(s, nfe);
			throw new RuntimeException(s, nfe);
		}
		// Generates the methods to implement from the CMPProfile interface
		Map cmpProfileInterfaceMethods = ClassUtils
				.getInterfaceMethodsFromInterface(cmpProfileInterface);
		generateConcreteMethods(cmpProfileConcreteClass,
				cmpProfileInterfaceMethods, "profileManagementInterceptor");

		// Generates the methods to implement from the
		// javax.slee.profile.ProfileManagement interface
		Map profileManagementInterfaceMethods = ClassUtils
				.getInterfaceMethodsFromInterface(sleeProfileManagementInterface);
		generateConcreteMethods(cmpProfileConcreteClass,
				profileManagementInterfaceMethods,
				"profileManagementInterceptor");

		try {
			// @@2.4+ -> 3.4+
			cmpProfileConcreteClass.writeFile(deployPath);
			
			if(logger.isDebugEnabled()) {
				logger
					.debug("Concrete Class "
							+ tmpClassName
							+ " generated in the following path " + deployPath);
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
		createPersistentStateHolderClass();

		Class clazz = null;
		try {
			// load the generated class
			clazz = Thread
					.currentThread()
					.getContextClassLoader()
					.loadClass(
					tmpClassName);
			
		} catch (ClassNotFoundException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
		
		return clazz;
	}

	/**
	 * Generates the Profile MBean interface
	 * 
	 * @return the interface generated
	 */
	public Class generateProfileMBeanInterface() {
		if (combination == -1) {
			logger.error("Profile Specification doesn't match any combination "
					+ "from the JSLEE spec 1.0 section 10.5.2");
			return null;
		}
		// Extends the javax.slee.profile.ProfileMBean
				
		profileMBeanConcreteInterface = pool.makeInterface(cmpProfileInterfaceName + "MBean");
		
		try {
			sleeProfileMBean = pool.get("javax.slee.profile.ProfileMBean");
		} catch (NotFoundException nfe) {
			nfe.printStackTrace();
			return null;
		}
		ConcreteClassGeneratorUtils.createInterfaceLinks(
				profileMBeanConcreteInterface,
				new CtClass[] { sleeProfileMBean });
		CtClass[] exceptions = new CtClass[3];
		try {
			exceptions[0] = pool
					.get("javax.slee.management.ManagementException");
			exceptions[1] = pool.get("javax.slee.InvalidStateException");
			exceptions[2] = pool
					.get("javax.slee.profile.ProfileImplementationException");
		} catch (NotFoundException nfe) {
			nfe.printStackTrace();
			return null;
		}
		// If the Profile Specification defines a Profile Management Interface,
		// the profileMBean interface has the same methods
		if (profileManagementInterfaceName != null) {
			try {
				profileManagementInterface = pool
						.get(profileManagementInterfaceName);
			} catch (NotFoundException nfe) {
				nfe.printStackTrace();
				return null;
			}
			ConcreteClassGeneratorUtils.copyMethods(profileManagementInterface,
					profileMBeanConcreteInterface, exceptions);
		}
		// otherwise it has all the methods of the CMP profile interface
		else {
			try {
				cmpProfileInterface = pool.get(cmpProfileInterfaceName);
			} catch (NotFoundException nfe) {
				nfe.printStackTrace();
				return null;
			}
			ConcreteClassGeneratorUtils.copyMethods(cmpProfileInterface,
					profileMBeanConcreteInterface, exceptions);
		}
		try {
			// @@2.4+ -> 3.4+
			// pool.writeFile(cmpProfileInterfaceName+"MBean", deployPath);
			pool.get(cmpProfileInterfaceName + "MBean").writeFile(deployPath);
			if(logger.isDebugEnabled()) {
				logger.debug("Concrete Interface " + cmpProfileInterfaceName
						+ "MBean" + " generated in the following path "
						+ deployPath);
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
			profileMBeanConcreteInterface.defrost();
		}

		Class clazz = null;
		try {
			clazz = Thread.currentThread().getContextClassLoader().loadClass(
					cmpProfileInterfaceName + "MBean");
		} catch (ClassNotFoundException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
		return clazz;
	}

	/**
	 * Generates the Profile MBean class
	 * 
	 * @return the class generated
	 */
	public Class generateConcreteProfileMBean() {
		if (combination == -1) {
			logger.error("Profile Specification doesn't match any combination "
					+ "from the JSLEE spec 1.0 section 10.5.2");
			return null;
		}
		
		String tmpClassName=ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_PREFIX + cmpProfileInterfaceName + ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_SUFFIX;
							
		profileMBeanConcreteClass = pool.makeClass(tmpClassName);
		
		// Implements the javax.management.StandardMBean
		CtClass jmxMBean = null;
		try {
			jmxMBean = pool.get("javax.management.StandardMBean");
		} catch (NotFoundException nfe) {
			nfe.printStackTrace();
			return null;
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
			nfe.printStackTrace();
			return null;
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
			CtClass[] parameters = new CtClass[] {
					pool
							.get("org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor"),
					pool.get("java.lang.Object") };
			createConstructorWithParameter(parameters,
					profileMBeanConcreteClass, true);
		} catch (NotFoundException nfe) {
			logger.error("Constructor With Parameter not created");
			nfe.printStackTrace();
		}
		// JEAN not needed anymore since Buddy added superclasses method
		// generation
		// implements methods defined in the
		// javax.slee.profile.ProfileManagement
		/*
		 * Map sleeProfileMBeanInterfaceMethods=ConcreteClassGeneratorUtils.
		 * getInterfaceMethodsFromInterface(sleeProfileMBean);
		 * generateConcreteMethods( profileMBeanConcreteClass,
		 * sleeProfileMBeanInterfaceMethods, "profileManagementInterceptor");
		 */

		// implements methods defined in the profileMBean interface previously
		// generated
		Map profileMBeanInterfaceMethods = ClassUtils
				.getInterfaceMethodsFromInterface(profileMBeanConcreteInterface);

		// Buddy
		// Map profileMBeanInterfaceMethods=ConcreteClassGeneratorUtils.
		// getInterfaceMethodsFromInterface(profileMBeanConcreteInterface,
		// sleeProfileMBeanInterfaceMethods);

		generateConcreteMethods(profileMBeanConcreteClass,
				profileMBeanInterfaceMethods, "profileManagementInterceptor");

		try {
			// @@2.4+ -> 3.4+
			// pool.writeFile(
			// ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_PREFIX+
			// cmpProfileInterfaceName+
			// ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_SUFFIX,
			// deployPath);
			pool.get(tmpClassName).writeFile(deployPath);
			if(logger.isDebugEnabled()) {
				logger
				.debug("Concrete Class "
						+ tmpClassName
						+ " generated in the following path " + deployPath);
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
		} catch (ClassNotFoundException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
		return clazz;
	}

	/**
	 * Create the default constructor of the concrete profile implementation
	 * class
	 */
	/*
	 * private void createDefaultConstructor() { //FIXME Get the interceptors
	 * from a xml mapping file CtConstructor defaultConstructor=new
	 * CtConstructor(null,cmpProfileConcreteClass); String constructorBody="{";
	 * //constructorBody+="profilePersistenceInterceptor=new " + //
	 * "org.mobicents.slee.container.deployment.interceptors.DefaultProfilePersistenceInterceptor();";
	 * constructorBody+="profileManagementInterceptor=new " +
	 * "org.mobicents.slee.container.deployment.interceptors.DefaultProfileManagementInterceptor();";
	 * constructorBody+="}"; try { defaultConstructor.setBody(constructorBody);
	 * cmpProfileConcreteClass.addConstructor(defaultConstructor);
	 * logger.debug("Profile DefaultConstructor created"); } catch
	 * (CannotCompileException e) { //Auto-generated catch block
	 * e.printStackTrace(); } }
	 */

	/**
	 * Create the default constructor of the profile MBean concrete class
	 */
	/*
	 * private void createDefaultMBeanConstructor() { //FIXME Get the
	 * interceptors from a xml mapping file CtConstructor defaultConstructor=new
	 * CtConstructor(null,profileMBeanConcreteClass); String
	 * constructorBody="{"; constructorBody+="profileMBeanInterceptor=new " +
	 * "org.mobicents.slee.container.deployment.interceptors.DefaultProfileMBeanInterceptor();";
	 * constructorBody+="}"; try { defaultConstructor.setBody(constructorBody);
	 * profileMBeanConcreteClass.addConstructor(defaultConstructor);
	 * logger.debug("Profile MBean DefaultConstructor created"); } catch
	 * (CannotCompileException e) { //Auto-generated catch block
	 * e.printStackTrace(); } }
	 */

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
	protected void createConstructorWithParameter(CtClass[] parameters,
			CtClass concreteClass, boolean mbean) {

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
			String parameterName = parameters[i].getName();
			parameterName = parameterName.substring(parameterName
					.lastIndexOf(".") + 1);
			String firstCharLowerCase = parameterName.substring(0, 1)
					.toLowerCase();
			parameterName = firstCharLowerCase.concat(parameterName
					.substring(1));
			try {
				CtField ctField = new CtField(parameters[i], parameterName,
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
			constructorBody += parameterName + "=$" + paramNumber + ";";
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
							.equals(
									"org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor"))
						isProfileManagementInterceptor = true;
				} catch (NotFoundException e1) {
					e1.printStackTrace();
				}
				j++;
			}
			if (isProfileManagementInterceptor) {
				if (!mbean) {
					constructorBody += ctFields[i].getName()
							+ ".setProfileManager(" + "sleeProfileManager); ";
					constructorBody += ctFields[i].getName()
							+ ".setProfileKey(" + "string); ";
				} else {
					constructorBody += ctFields[i].getName() + ".setProfile("
							+ "object); ";
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
	 * Create the persistent object that will hold the transient state of the
	 * profile
	 */
	protected void createPersistentStateHolderClass() {
		// Create the class of the persistent state of the sbb
		
		String tmpClassName=ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_PREFIX
		+ cmpProfileInterfaceName
		+ ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_SUFFIX;
		
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
			Thread.currentThread().getContextClassLoader().loadClass(
					tmpClassName);
		} catch (ClassNotFoundException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Create the Interceptor fields of the concrete profile implementation
	 * class
	 */
	/*
	 * private void createInterceptorFields() { //TODO Get the interceptors from
	 * a xml file //Add the profile persistence Manager CtField
	 * persistenceInterceptor=null; try { persistenceInterceptor=new CtField(
	 * pool.get("org.mobicents.slee.container.deployment.interceptors.ProfilePersistenceInterceptor"),
	 * "profilePersistenceInterceptor", cmpProfileConcreteClass);
	 * persistenceInterceptor.setModifiers(Modifier.PRIVATE);
	 * cmpProfileConcreteClass.addField(persistenceInterceptor); } catch
	 * (CannotCompileException cce) { cce.printStackTrace(); } catch
	 * (NotFoundException nfe) { nfe.printStackTrace(); } //Add the profile
	 * management Manager CtField managementInterceptor=null; try {
	 * managementInterceptor=new CtField(
	 * pool.get("org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor"),
	 * "profileManagementInterceptor", cmpProfileConcreteClass);
	 * managementInterceptor.setModifiers(Modifier.PRIVATE);
	 * cmpProfileConcreteClass.addField(managementInterceptor); } catch
	 * (CannotCompileException cce) { cce.printStackTrace(); } catch
	 * (NotFoundException nfe) { nfe.printStackTrace(); } }
	 */

	/**
	 * Create the fields of the MBean Profile Implementation class
	 * 
	 */
	/*
	 * private void createMBeanInterceptorFields() { //TODO Get the interceptors
	 * from a xml file //Add the profile persistence Manager CtField
	 * profileMBeanInterceptor=null; try { profileMBeanInterceptor=new CtField(
	 * pool.get("org.mobicents.slee.container.deployment.interceptors.ProfileMBeanInterceptor"),
	 * "profileMBeanInterceptor", profileMBeanConcreteClass);
	 * profileMBeanInterceptor.setModifiers(Modifier.PRIVATE);
	 * profileMBeanConcreteClass.addField(profileMBeanInterceptor); } catch
	 * (CannotCompileException cce) { cce.printStackTrace(); } catch
	 * (NotFoundException nfe) { nfe.printStackTrace(); } }
	 */
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
	private void generateConcreteMethods(CtClass source, Map interfaceMethods,
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
}
