package org.mobicents.slee.container.deployment.profile;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
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
import org.mobicents.slee.container.profile.ProfileCallRecorderTransactionData;
import org.mobicents.slee.container.profile.ProfileCmpHandler;
import org.mobicents.slee.container.profile.ProfileConcrete;
import org.mobicents.slee.container.profile.ProfileManagementHandler;
import org.mobicents.slee.container.profile.ProfileObject;
import org.mobicents.slee.container.profile.ProfileTableImpl;

public class ConcreteProfileManagementGenerator {

	private static final Logger logger = Logger.getLogger(ConcreteProfileManagementGenerator.class);
	public static final String _INTERCEPTOR_MANAGEMENT = "profileManagementInterceptor";

	private ProfileSpecificationComponent component = null;
	private String cmpProfileInterfaceName = null;
	private String managementAbstractClassName = null;
	private String profileManagementInterfaceName = null;

	private ClassPool pool = null;
	private CtClass cmpProfileConcreteClass = null;
	private CtClass cmpProfileInterface = null;
	private CtClass sleeProfileManagementInterface = null;
	private CtClass sleeProfileInterface = null;
	private CtClass profileManagementAbstractClass = null;
	private CtClass profileManagementInterface = null;

	private CtClass mobicentsProfileConcreteInterface = null;

	public static final String _PROFILE_OBJECT_FIELD = "ProfileObject";
	public static final String _PROFILE_NAME_FIELD = "ProfileName";
	public static final String _PROFILE_TABLE_FIELD = "ProfileTable";
	public static final String _PROFILE_DIRTY_FIELD = "ProfileDirty";
	public static final String _PROFILE_INBACK_FIELD = "ProfileInBackEndStorage";

	public static final String _HANDLER_NAME_CMP = "cmpHandler";
	public static final String _HANDLER_NAME_MANAGEMENT = "managementHandler";

	private static final Set<String> mustInterceptMethods;

	static {
		Set<String> ss = new HashSet<String>();
		ss.add("isProfileDirty");
		ss.add("isProfileValid");
		ss.add("markProfileDirty");
		mustInterceptMethods = Collections.unmodifiableSet(ss);
	}

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

			throw new DeploymentException("Profile Specification doesn't match any combination " + "from the JSLEE spec 1.0 section 10.5.2 or JSLEE spec 1.1 section 10.5.1.2, for " + component);

		}

		String tmpClassName = ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_PREFIX + cmpProfileInterfaceName + ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_SUFFIX;
		cmpProfileConcreteClass = pool.makeClass(tmpClassName);

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

		try {
			sleeProfileInterface = pool.get("javax.slee.profile.Profile");

		} catch (NotFoundException nfe) {
			nfe.printStackTrace();
			throw new DeploymentException("Failed to locate profile management class for " + component, nfe);

		}

		try {
			// This interface extends Profile and ProfileManagement interfaces.
			mobicentsProfileConcreteInterface = pool.get(ProfileConcrete.class.getName());

		} catch (NotFoundException nfe) {
			// nfe.printStackTrace();
			throw new DeploymentException("Failed to locate profile management class for " + component, nfe);

		}
		if (profileManagementInterfaceName != null) {

			try {
				profileManagementInterface = pool.get(profileManagementInterfaceName);
			} catch (NotFoundException nfe) {
				// nfe.printStackTrace();
				throw new DeploymentException("Profile management interface could not be found for " + component);
			}
		}
		List<CtClass> interfacesToLink = new ArrayList<CtClass>();
		interfacesToLink.add(mobicentsProfileConcreteInterface);
		interfacesToLink.add(cmpProfileInterface);
		// is there any reason we need this ?
		// if(profileManagementInterface!=null)
		// interfacesToLink.add(profileManagementInterface);

		CtClass[] interfaces = new CtClass[interfacesToLink.size()];
		interfaces = interfacesToLink.toArray(interfaces);
		ConcreteClassGeneratorUtils.createInterfaceLinks(cmpProfileConcreteClass, interfaces);

		// now we have to create inheritance link between our concrete class and
		// possible user defined

		if (managementAbstractClassName != null) {
			try {
				profileManagementAbstractClass = pool.get(managementAbstractClassName);
			} catch (NotFoundException nfe) {
				// nfe.printStackTrace();
				throw new DeploymentException("Profile abstract class could not be found for " + component);
			}
			ConcreteClassGeneratorUtils.createInheritanceLink(cmpProfileConcreteClass, profileManagementAbstractClass);
		}

		// now lets create constructor for us.
		try {
			String[] parameterNames = { _HANDLER_NAME_CMP, _HANDLER_NAME_MANAGEMENT, };

			CtClass[] parameters = new CtClass[] { pool.get(ProfileCmpHandler.class.getName()), pool.get(ProfileManagementHandler.class.getName()) };
			createConstructorWithParameter(parameterNames, parameters, cmpProfileConcreteClass);
		} catch (NotFoundException nfe) {

			String s = "Unexpected Exception - could not find class. Constructor with parameters not created";
			logger.error(s, nfe);
			throw new RuntimeException(s, nfe);
		}

		// Those methods come from
		Map<String, CtMethod> cmpProfileInterfaceMethods = ClassUtils.getInterfaceMethodsFromInterface(cmpProfileInterface);

		generateCmps(cmpProfileInterfaceMethods, cmpProfileConcreteClass);

		Map<String, CtMethod> profileManagementMethods = ClassUtils.getInterfaceMethodsFromInterface(sleeProfileManagementInterface);
		profileManagementMethods.putAll(ClassUtils.getInterfaceMethodsFromInterface(sleeProfileInterface));
		// We need to intercept those calls, to throw proper exceptions

		if (profileManagementInterface != null) {
			profileManagementMethods.putAll(org.mobicents.slee.container.deployment.ClassUtils.getInterfaceMethodsFromInterface(profileManagementInterface));
		}
		if (profileManagementAbstractClass != null) {
			// We do get concrete methods, so we can intercept calls before
			// delegating them
			profileManagementMethods.putAll(ClassUtils.getConcreteMethodsFromClass(profileManagementAbstractClass));
			Map<String, CtMethod> abstractMethods = ClassUtils.getSuperClassesAbstractMethodsFromClass(profileManagementAbstractClass);
			abstractMethods.putAll(ClassUtils.getAbstractMethodsFromClass(profileManagementAbstractClass));
			// First we generate Usage methods and remove them from management
			// methods map

			createDefaultUsageParameterGetter(abstractMethods, cmpProfileConcreteClass);
			createNamedUsageParameterGetter(abstractMethods, cmpProfileConcreteClass);

		}

		generateManagementMethods(profileManagementMethods, cmpProfileInterfaceMethods, cmpProfileConcreteClass, profileManagementAbstractClass);

		// now lets implement fields.
		createField(ProfileObject.class, _PROFILE_OBJECT_FIELD, cmpProfileConcreteClass, this.pool);
		createField(String.class, _PROFILE_NAME_FIELD, cmpProfileConcreteClass, this.pool);
		createField(ProfileTableImpl.class, _PROFILE_TABLE_FIELD, cmpProfileConcreteClass, this.pool);
		createField(Boolean.class, _PROFILE_DIRTY_FIELD, cmpProfileConcreteClass, this.pool);
		createField(Boolean.class, _PROFILE_INBACK_FIELD, cmpProfileConcreteClass, this.pool);

		try {
			// @@2.4+ -> 3.4+
			cmpProfileConcreteClass.writeFile(component.getDeploymentDir().getAbsolutePath());

			if (logger.isDebugEnabled()) {
				logger.debug("Concrete Class " + tmpClassName + " generated in the following path " + component.getDeploymentDir().getAbsolutePath());
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

		Class clazz = null;
		try {
			// load the generated class
			clazz = component.getClassLoader().loadClass(tmpClassName);
			component.setProfileCmpConcreteClass(clazz);
		} catch (ClassNotFoundException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}

		if (component.getProfileCmpConcreteClass() == null) {
			throw new DeploymentException("Concrete cmp itnerface class is null, could we possibly fail and not throw exception yet?, " + component);
		}

	}

	// FIXME: add jdocs, but all of this will propably be changed once other
	// classes are generated.

	void generateCmps(Map<String, CtMethod> possibleCmp, CtClass classToBeInstrumented) throws Exception {
		Iterator<Map.Entry<String, CtMethod>> mm = possibleCmp.entrySet().iterator();

		while (mm.hasNext()) {
			Map.Entry<String, CtMethod> entry = mm.next();
			if (entry.getKey().startsWith(org.mobicents.slee.container.deployment.ClassUtils.GET_PREFIX) || entry.getKey().startsWith(org.mobicents.slee.container.deployment.ClassUtils.SET_PREFIX)) {
				// mm.remove();
				generateDelegateCmpMethod(_HANDLER_NAME_CMP, entry.getValue(), classToBeInstrumented);

			}
		}
	}

	void generateDelegateCmpMethod(String fieldIntercepting, CtMethod method, CtClass classToBeInstrumented) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("About to instrument: " + method.getName() + ", into: " + classToBeInstrumented.getName());
		}
		// so we are not abstract
		method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);

		String fieldName = method.getName();
		String retStatement = "";
		String cmpMethod = null;
		if (fieldName.startsWith(ClassUtils.SET_PREFIX)) {
			fieldName = Introspector.decapitalize(fieldName.replace(ClassUtils.SET_PREFIX, ""));
			cmpMethod = "setCmpField(fName,$1);";
		} else if (method.getName().startsWith(ClassUtils.GET_PREFIX)) {
			fieldName = Introspector.decapitalize(fieldName.replace(ClassUtils.GET_PREFIX, ""));
			cmpMethod = "getCmpField(fName);";
		} else {
			throw new IllegalArgumentException("Illegal method for CMP accessor: " + method.getName());
		}
		String body = "{ String fName= \"" + fieldName + "\"; ";
		try {
			if (method.getReturnType().toString().compareTo("void") == 0) {

			} else {
				// FIXME: will this work for
				retStatement = "return ($r) ";
			}
		} catch (NotFoundException e) {
			// this should nto happen
			throw e;
		}

		body += retStatement + "  this." + fieldIntercepting + "." + cmpMethod + " }";

		if (logger.isDebugEnabled()) {
			logger.debug("About to instrumented: " + method.getName() + ", body: " + body);
		}
		method.setBody(body);
		classToBeInstrumented.addMethod(method);

	}

	void generateManagementMethods(Map<String, CtMethod> managementMethods, Map<String, CtMethod> cmpInterfaceMethods, CtClass classToBeInstrumented, CtClass profileManagementAbstractClass)
			throws Exception {
		Iterator<Map.Entry<String, CtMethod>> mm = managementMethods.entrySet().iterator();

		while (mm.hasNext()) {
			Map.Entry<String, CtMethod> entry = mm.next();
			if (cmpInterfaceMethods.containsKey(entry.getKey())) {
				if (logger.isDebugEnabled()) {
					logger.debug("Not instrumenting method as it is CMP method: " + entry.getKey());
				}
			} else if (entry.getKey().contains("commitChanges")) {
				generateDelegateCmpMethod(_HANDLER_NAME_CMP, entry.getValue(), classToBeInstrumented);
			} else if (!cmpInterfaceMethods.containsKey(org.mobicents.slee.container.deployment.ClassUtils.getMethodKey(entry.getValue()))) {
				generateDelegateMethod(_HANDLER_NAME_MANAGEMENT, entry.getValue(), classToBeInstrumented, profileManagementAbstractClass);
			} else {
				logger.error("Found weird method: " + entry.getKey());
			}
		}

	}

	void generateDelegateMethod(String fieldIntercepting, CtMethod method, CtClass classToBeInstrumented, CtClass profileManagementAbstractClass) throws Exception {

		// FIXME: should we add check for concrete methods from
		// profileManagementAbstractClass and do clone?

		if (logger.isDebugEnabled()) {
			logger.debug("About to instrument: " + method.getName() + ", into: " + classToBeInstrumented.getName());
		}
		// Set it not abstract
		//method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
		method = CtNewMethod.copy(method, classToBeInstrumented, null);
		method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
		String retStatement = "";
		try {
			if (method.getReturnType().toString().compareTo("void") == 0) {

			} else {
				retStatement = "return (r$) ";
			}
		} catch (NotFoundException e) {
			// this should nto happen
			throw e;
		}
		String body = "{  " + "try{" + ProfileCallRecorderTransactionData.class.getName() + ".addProfileCall(this);" + "" + retStatement;

		if (profileManagementAbstractClass != null) {
			if (!mustInterceptMethods.contains(method.getName())) {
				body += "super." + method.getName() + "($$);        }";
			} else {
				body += "this." + fieldIntercepting + "." + method.getName() + "($$);        ";
			}
		} else {
			body += "this." + fieldIntercepting + "." + method.getName() + "($$);        ";
		}

		body += "}finally{" + ProfileCallRecorderTransactionData.class.getName() + ".removeProfileCall(this);" + "" + "}";
		if (logger.isDebugEnabled()) {
			logger.debug("About to instrumented: " + method.getName() + ", body: " + body);
		}
		method.setBody(body);
		classToBeInstrumented.addMethod(method);
	}

	/**
	 * Create field of specified type and bean accessors.
	 * 
	 * @param fieldType
	 * @param fieldName
	 *            - name of field: Test,StringParser. (fir char must be
	 *            capital.)
	 * @param fieldOwner
	 * @param pool
	 * @throws Exception
	 */
	static void createField(Class fieldType, String fieldName, CtClass fieldOwner, ClassPool pool) throws Exception {
		String _fieldName = Introspector.decapitalize(fieldName);
		CtClass ctFieldType = pool.get(fieldType.getName());
		CtField field = new CtField(ctFieldType, _fieldName, fieldOwner);
		field.setModifiers(Modifier.PRIVATE);
		fieldOwner.addField(field);

		String getBody = "{ return this." + _fieldName + "}";
		CtMethod getMethod = new CtMethod(ctFieldType, org.mobicents.slee.container.component.validator.ClassUtils.GET_PREFIX + fieldName, null, fieldOwner);
		getMethod.setBody(getBody);
		fieldOwner.addMethod(getMethod);

		String setBody = "{ this." + _fieldName + "=$0}";
		CtMethod setMethod = new CtMethod(ctFieldType, org.mobicents.slee.container.component.validator.ClassUtils.GET_PREFIX + fieldName, null, fieldOwner);
		setMethod.setBody(setBody);
		fieldOwner.addMethod(setMethod);

		if (logger.isDebugEnabled()) {
			logger.debug("Instrumented class: " + fieldOwner.getName() + ", with field: " + _fieldName + ", along with setter: " + setBody + ", and getter: " + getBody);
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
	static void createConstructorWithParameter(String[] parameterNames, CtClass[] parameters, CtClass concreteClass) throws Exception {

		CtConstructor constructorWithParameter = new CtConstructor(parameters, concreteClass);
		String constructorBody = "{";

		// "this();";
		for (int i = 0; i < parameters.length; i++) {

			try {
				CtField ctField = new CtField(parameters[i], parameterNames[i], concreteClass);
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

		constructorBody += "}";
		try {
			concreteClass.addConstructor(constructorWithParameter);
			constructorWithParameter.setBody(constructorBody);
			if (logger.isDebugEnabled()) {
				logger.debug("ConstructorWithParameter created: " + constructorBody);
			}
		} catch (CannotCompileException e) {

			throw new DeploymentException("Failed to instrument constructor.", e);
		}
	}

	public CtClass getProfileManagementAbstractClass() {
		return profileManagementAbstractClass;
	}

	/**
	 * Create a default usage parameter getter and setter.
	 * 
	 * @param profileManagementMethods
	 * 
	 * @param profileManagementConcreteClass
	 * @throws DeploymentException
	 */
	private void createDefaultUsageParameterGetter(Map<String, CtMethod> abstractMethods, CtClass profileManagementConcreteClass) throws DeploymentException {
		// FIXME: shoudl we add in both checks for exceptions, return type and
		// params?
		String methodName = "getDefaultUsageParameterSet";
		CtMethod method = null;
		for (CtMethod m : abstractMethods.values()) {
			if (m.getName().compareTo(methodName) == 0) {
				method = m;
				break;
			}
		}
		
		if (method != null) {
			try {
				method = CtNewMethod.copy(method, profileManagementConcreteClass, null);
				method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
				// copy method from abstract to concrete class
				
				// create the method body
				String concreteMethodBody = "{ return ($r) " + ProfileManagementHandler.class.getName() + ".getProfileUsageParam(this); }";
				if (logger.isDebugEnabled()) {
					logger.debug("Generated method " + methodName + " , body = " + concreteMethodBody);
				}
				method.setBody(concreteMethodBody);
				profileManagementConcreteClass.addMethod(method);
			} catch (CannotCompileException cce) {
				throw new SLEEException("Cannot compile method " + method.getName(), cce);
			}
		}
	}

	/**
	 * Create a named usage parameter getter.
	 * 
	 * @param profileManagementMethods
	 * 
	 * @param profileManagementConcreteClass
	 * @throws DeploymentException
	 */

	private void createNamedUsageParameterGetter(Map<String, CtMethod> abstractMethods, CtClass profileManagementConcreteClass) throws DeploymentException {
		String methodName = "getUsageParameterSet";
		CtMethod method = null;
		for (CtMethod m : abstractMethods.values()) {
			if (m.getName().compareTo(methodName) == 0) {
				method = m;
				break;
			}
		}
		
		if (method != null) {
			try {
				method = CtNewMethod.copy(method, profileManagementConcreteClass, null);
				method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
				// copy method from abstract to concrete class
				
				// create the method body
				String concreteMethodBody = "{ return ($r) " + ProfileManagementHandler.class.getName() + ".getProfileUsageParam(this,$1); }";
				if (logger.isDebugEnabled()) {
					logger.debug("Generated method " + methodName + " , body = " + concreteMethodBody);
				}
				method.setBody(concreteMethodBody);
				profileManagementConcreteClass.addMethod(method);
			} catch (CannotCompileException cce) {
				throw new SLEEException("Cannot compile method " + method.getName(), cce);
			}
		}
	}

}
