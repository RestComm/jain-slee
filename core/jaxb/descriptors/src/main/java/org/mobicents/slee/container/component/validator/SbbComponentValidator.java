/**
 * Start time:17:06:21 2009-01-30<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetChildRelationMethod;

/**
 * Start time:17:06:21 2009-01-30<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbComponentValidator implements Validator {

	// public static final String _SBB_EXCEPTION_THROWN_NAME =
	// "sbbExceptionThrown";
	// public static final String _SBB_EXCEPTION_THROWN_SIGNATURE =
	// "(Ljava/lang/Exception;Ljava/lang/Object;Ljavax/slee/ActivityContextInterface;)V";

	// public static final String _SBB_ROLLERD_BACK_NAME = "sbbRolledBack";
	// public static final String _SBB_ROLLERD_BACK_SIGNATURE =
	// "(Ljavax/slee/RolledBackContext;)V";

	public static final String _SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE = "asSbbActivityContextInterface";
	// This has to be terminated by
	// component.getSbbActivityContextInterface().getName+";" to form full
	// signature, however in 1.1 The return type must be the Activity Context
	// Interface interface of the SBB, or a base interface of
	// the Activity Context Interface interface of the SBB. <--- what does this
	// mean - we define ACI_X(extends ACI_Y) as aci of sbb, but we can return
	// ACI_Y ?
	public static final String _SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE_SIGNATURE_PART = "(Ljavax/slee/ActivityContextInterface;)L";

	public static final String _SBB_GET_CHILD_RELATION_SIGNATURE_PART = "()Ljavax/slee/ChildRelation;";

	private SbbComponent component = null;
	private ComponentRepository repository = null;
	private final static transient Logger logger = Logger
			.getLogger(SbbComponentValidator.class);
	private final static Set<String> _PRIMITIVES;
	static {
		Set<String> tmp = new HashSet<String>();
		tmp.add("int");
		tmp.add("boolean");
		tmp.add("byte");
		tmp.add("char");
		tmp.add("double");
		tmp.add("float");
		tmp.add("long");
		tmp.add("short");
		_PRIMITIVES = Collections.unmodifiableSet(tmp);

	}

	public boolean validate() {

		return false;
	}

	public void setComponentRepository(ComponentRepository repository) {
		this.repository = repository;

	}

	public SbbComponent getComponent() {
		return component;
	}

	public void setComponent(SbbComponent component) {
		this.component = component;
	}

	/**
	 * Sbb abstract class(general rule – methods cannot start neither with “ejb”
	 * nor “sbb”)
	 * <ul>
	 * <li>(1.1 ?) must have package declaration
	 * <li>must implement in some way javax.slee.Sbb(only methods from interface
	 * can have “sbb” prefix)
	 * <ul>
	 * <li>each method defined must be implemented as public – not abstract,
	 * final or static
	 * </ul>
	 * <li>must be public and abstract
	 * <li>must have public no arg constructor
	 * <li>must implement sbbExceptionThrown method
	 * <ul>
	 * <li>
	 * public, not abstract, final or static no return type 3 arguments:
	 * java.lang.Exception, java.lang.Object,
	 * javax.slee.ActivityContextInterface
	 * </ul>
	 * <li>must implement sbbRolledBack
	 * <ul>
	 * <li>method must be public, not abstract, final or static
	 * <li>no return type
	 * <li>with single argument - javax.slee.RoledBackContext
	 * </ul>
	 * <li>there is no finalize method
	 * </ul>
	 * 
	 * @return
	 */
	boolean validateAbstractClassConstraints(
			Map<String, CtMethod> concreteMethods,
			Map<String, CtMethod> concreteSuperClassesMethods) {

		StringBuffer errorBuffer = new StringBuffer();
		boolean passed = true;
		// Presence of those classes must be checked elsewhere
		CtClass sbbAbstractClass = this.component.getCtAbstractSbbClass();

		// Must be public and abstract
		int modifiers = sbbAbstractClass.getModifiers();
		// check that the class modifiers contain abstratc and public
		if (!Modifier.isAbstract(modifiers) || !Modifier.isPublic(modifiers)) {
			passed = false;
			appendToBuffer(this.component.getAbstractSbbClass()
					+ " is nor abstract neither public", errorBuffer);
		}

		// 1.1 - must be in package
		if (this.component.isSlee11()) {
			String packageName = sbbAbstractClass.getPackageName();
			if (packageName == null || packageName.compareTo("") == 0) {
				passed = false;
				appendToBuffer(
						this.component.getAbstractSbbClass()
								+ " has no pacakge declaration, in SLEE 1.1 its mandatory",
						errorBuffer);
			}

		}

		// Public no arg constructor - can it have more ?
		// sbbAbstractClass.getConstructor
		// FIXME: no arg constructor has signature "()V" when added from
		// javaassist we check for such constructor and if its public
		try {
			CtConstructor constructor = sbbAbstractClass.getConstructor("()V");
			int conMod = constructor.getModifiers();
			if (!Modifier.isPublic(conMod)) {
				passed = false;
				appendToBuffer(
						this.component.getAbstractSbbClass()
								+ " has no no arg constructor, however its not public: "
								+ constructor, errorBuffer);
			}
		} catch (NotFoundException e) {

			// e.printStackTrace();
			passed = false;
			appendToBuffer(this.component.getAbstractSbbClass()
					+ " has no no arg constructor", errorBuffer);
		}

		// Must implements javax.slee.Sbb - and each method there defined, only
		// those methods and two above can have "sbb" prefix
		// those methods MUST be in concrete methods map, later we will use them
		// to see if there is ant "sbbXXXX" method

		// Check if we implement javax.slee.Sbb - either directly or from super
		// class
		CtClass javaxSleeSbbInterface = null;

		boolean implementSbbLinkFound = false;
		javaxSleeSbbInterface = ClassUtils.checkInterfaces(sbbAbstractClass,
				"javax.slee.Sbb");

		if (javaxSleeSbbInterface == null) {

			passed = false;
			appendToBuffer(
					"sbb abstract class "
							+ sbbAbstractClass.getName()
							+ " doesn't implements the javax.slee.Sbb class either directly or by inheritance",
					errorBuffer);
		}

		// Now we have to check methods from javax.slee.Sbb
		// This takes care of method throws clauses
		if (javaxSleeSbbInterface != null) {
			// if it is, we dont have those methods for sure, or maybe we do,
			// implemnted by hand
			// either way its a failure
			// We want only java.slee.Sbb methods :)
			CtMethod[] sbbLifecycleMethods = javaxSleeSbbInterface
					.getDeclaredMethods();
			for (CtMethod lifecycleMehtod : sbbLifecycleMethods) {
				// It must be implemented - so only in concrete methods, if we
				// are left with one not checked bang, its an error

				String methodKey = ClassUtils.getMethodKey(lifecycleMehtod);
				CtMethod concreteLifeCycleImpl = null;
				if (concreteMethods.containsKey(methodKey)) {
					concreteLifeCycleImpl = concreteMethods.remove(methodKey);
				} else if (concreteSuperClassesMethods.containsKey(methodKey)) {
					concreteLifeCycleImpl = concreteSuperClassesMethods
							.remove(methodKey);
				} else {
					passed = false;
					appendToBuffer(
							"sbb abstract class "
									+ sbbAbstractClass.getName()
									+ " doesn't provide public implementation of the javax.slee.Sbb interface method: "
									+ methodKey, errorBuffer);
					continue;
				}

				// now we now there is such method, its not private and abstract
				// If we are here its not null
				int lifeCycleModifier = concreteLifeCycleImpl.getModifiers();
				if (!Modifier.isPublic(lifeCycleModifier)
						|| Modifier.isStatic(lifeCycleModifier)
						|| Modifier.isFinal(lifeCycleModifier)) {
					passed = false;
					appendToBuffer(
							"sbb abstract class "
									+ sbbAbstractClass.getName()
									+ " doesn't provide public implementation of the javax.slee.Sbb interface method: "
									+ methodKey
									+ " it must be public, not static, final, abstract",
							errorBuffer);
				}

			}
		}

		// there can not be any method which start with ejb/sbb - we removed
		// every from concrete, lets iterate over those sets
		for (CtMethod concreteMethod : concreteMethods.values()) {

			if (concreteMethod.getName().startsWith("ejb")
					|| concreteMethod.getName().startsWith("sbb")) {

				passed = false;
				appendToBuffer(
						"invalid method name determined in sbb abstract class "
								+ sbbAbstractClass.getName() + " method: "
								+ concreteMethod.getName()
								+ concreteMethod.getSignature(), errorBuffer);

			}
		}

		for (CtMethod concreteMethod : concreteSuperClassesMethods.values()) {
			if (concreteMethod.getName().startsWith("ejb")
					|| concreteMethod.getName().startsWith("sbb")) {

				passed = false;
				appendToBuffer(
						"invalid method name determined in sbbs abstract class super classes"
								+ sbbAbstractClass.getName() + " method: "
								+ concreteMethod.getName()
								+ concreteMethod.getSignature(), errorBuffer);

			}
		}

		// check that the field names do not start with sbb or ejb
		// FIXME: is this valid test ?
		CtField[] ctFields = sbbAbstractClass.getFields();
		for (int i = 0; i < ctFields.length; i++) {
			if (Modifier.isPublic(ctFields[i].getModifiers())
					&& (ctFields[i].getName().startsWith("sbb") || ctFields[i]
							.getName().startsWith("ejb"))) {

				passed = false;
				appendToBuffer("the sbb abstract class "
						+ sbbAbstractClass.getName()
						+ " has a field starting with sbb or ejb["
						+ (!Modifier.isPrivate(ctFields[i].getModifiers()))
						+ "]: " + ctFields[i].getName(), errorBuffer);

			}
		}

		if (!passed) {
			logger.error(errorBuffer.toString());
			System.err.println(errorBuffer);
		}

		return passed;

	}

	/**
	 * This method checks for presence of
	 * 
	 * @param sbbAbstractClassAbstractMethod
	 * @param sbbAbstractClassAbstractMethodFromSuperClasses
	 * @return
	 */
	boolean validateSbbActivityContextInterface(
			Map<String, CtMethod> sbbAbstractClassAbstractMethod,
			Map<String, CtMethod> sbbAbstractClassAbstractMethodFromSuperClasses) {

		StringBuffer errorBuffer = new StringBuffer();
		if (this.component.getDescriptor().getSbbActivityContextInterface() == null) {
			// FIXME: add check for asSbbActivityContextInteface method ? This
			// will be catched at the end of check anyway
			if (logger.isInfoEnabled()) {
				logger.info(this.component.getDescriptor().getSbbComponentKey()
						+ " : No Sbb activity context interface defined");
			}
			return true;
		}

		boolean passed = true;

		CtClass sbbAbstractClass = this.component.getCtAbstractSbbClass();
		CtMethod asACIMethod = null;
		try {

			// lets go through methods of sbbAbstract class,

			for (CtMethod someMethod : sbbAbstractClassAbstractMethod.values()) {

				if (someMethod.getName().compareTo(
						_SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE) == 0) {
					// we have a winner, possibly - we have to check parameter
					// list, cause someone can create abstract method(or crap,
					// it can be concrete) with different parametrs, in case its
					// abstract, it will fail later on

					if (someMethod.getParameterTypes().length == 1
							&& someMethod.getParameterTypes()[0]
									.getName()
									.compareTo(
											"javax.slee.ActivityContextInterface") == 0) {
						asACIMethod = someMethod;
						break;
					}
				}
			}

			if (asACIMethod == null)
				for (CtMethod someMethod : sbbAbstractClassAbstractMethodFromSuperClasses
						.values()) {

					if (someMethod.getName().compareTo(
							_SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE) == 0) {
						// we have a winner, possibly - we have to check
						// parameter
						// list, cause someone can create abstract method(or
						// crap,
						// it can be concrete) with different parametrs, in case
						// its
						// abstract, it will fail later on
						if (someMethod.getParameterTypes().length == 1
								&& someMethod.getParameterTypes()[0]
										.getName()
										.compareTo(
												"javax/slee/ActivityContextInterface") == 0) {
							asACIMethod = someMethod;
							break;
						}
					}
				}
			if (asACIMethod == null) {
				passed = false;
				appendToBuffer(
						sbbAbstractClass.getName()
								+ " class does not define asSbbActivityContextInterface method with signature:  "
								+ ""
								+ _SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE
								+ _SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE_SIGNATURE_PART
								+ this.component.getDescriptor()
										.getSbbActivityContextInterface()
										.getInterfaceName()
								+ "; or with super type return type",
						errorBuffer);

			} else {
				// must be public, abstract? FIXME: not native?
				int asACIMethodModifiers = asACIMethod.getModifiers();
				if (!Modifier.isPublic(asACIMethodModifiers)
						|| !Modifier.isAbstract(asACIMethodModifiers)
						|| Modifier.isNative(asACIMethodModifiers)) {
					passed = false;
					appendToBuffer(
							sbbAbstractClass.getName()
									+ " class does not define asSbbActivityContextInterface method which is public, abstract and not native",
							errorBuffer);
				}

				// now this misery comes to play, return type check
				CtClass returnType = asACIMethod.getReturnType();
				// Must return something from Sbb defined aci class inheritance
				// tree
				CtClass definedReturnType = this.component
						.getCtActivityContextInterface();

				if (returnType == null) {
					passed = false;
					appendToBuffer(
							sbbAbstractClass.getName()
									+ " class defines method asSbbActivitContextInterface with void return type, which is wrong.",
							errorBuffer);
				} else if (returnType.equals(definedReturnType)) {
					// its ok

				} else if (ClassUtils.checkInterfaces(definedReturnType,
						returnType.getName()) != null) {

				} else {
					passed = false;
					appendToBuffer(
							sbbAbstractClass.getName()
									+ " class defines method asSbbActivitContextInterface wrong return type: "
									+ returnType.getName(), errorBuffer);

				}

				// no throws clause
				if (asACIMethod.getExceptionTypes() != null
						&& asACIMethod.getExceptionTypes().length > 0) {

					passed = false;
					appendToBuffer(
							sbbAbstractClass.getName()
									+ " class defines method asSbbActivitContextInterface which has throws clause.",
							errorBuffer);
				}

			}

		} catch (NotFoundException e) {

			e.printStackTrace();
			passed = false;
			appendToBuffer(
					sbbAbstractClass.getName()
							+ " class does not define asSbbActivityContextInterface method with signature:  "
							+ ""
							+ _SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE
							+ _SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE_SIGNATURE_PART
							+ this.component.getDescriptor()
									.getSbbActivityContextInterface()
									.getInterfaceName() + ";", errorBuffer);
		}

		// Even if we fail above we can do some checks on ACI if its present.
		// this has to be present
		CtClass sbbActivityContextInterface = this.component
				.getCtActivityContextInterface();

		// ACI VALIDATION
		// (1.1) = must be declared in package

		if (this.component.isSlee11()
				&& sbbActivityContextInterface.getPackageName() == null) {
			passed = false;
			appendToBuffer(
					sbbAbstractClass.getName()
							+ " In Speciifcation 1.1 of JSLEE, Sbb ACI must be defined inside a package",
					errorBuffer);
		}

		if (!Modifier.isPublic(sbbActivityContextInterface.getModifiers())) {
			passed = false;
			appendToBuffer(sbbAbstractClass.getName()
					+ " Sbb custom ACI must be public", errorBuffer);
		}

		// We can have here ACI objects and java primitives, ugh, both methods
		// dont have to be shown
		passed = checkSbbAciFieldsConstraints(this.component
				.getCtActivityContextInterface());

		// finally lets remove asSbb method form abstract lists, this is used
		// later to determine methods that didnt match any sbb definition
		if (asACIMethod != null) {
			sbbAbstractClassAbstractMethod.remove(ClassUtils
					.getMethodKey(asACIMethod));
			sbbAbstractClassAbstractMethodFromSuperClasses.remove(ClassUtils
					.getMethodKey(asACIMethod));
		}

		if (!passed) {
			logger.error(errorBuffer.toString());
			System.err.println(errorBuffer);
		}
		return passed;
	}

	/**
	 * This method validates all methods in ACI interface:
	 * <ul>
	 * <li>set/get methods and parameter names as in CMP fields decalration
	 * <li>methods must
	 * <ul>
	 * <li>be public, abstract
	 * <li>setters must have one param
	 * <li>getters return type must match setter type
	 * <li>allowe types are: primitives and serilizable types
	 * </ul>
	 * </ul>
	 * <br>
	 * Sbb descriptor provides method to obtain aci field names, if this test
	 * passes it means that all fields there should be correct and can be used
	 * to verify aliases
	 * 
	 * @param sbbAciInterface
	 * @return
	 */
	boolean checkSbbAciFieldsConstraints(CtClass sbbAciInterface) {

		boolean passed = true;
		StringBuffer errorBuffer = new StringBuffer();
		// here we need all fields :)
		HashSet<String> ignore = new HashSet<String>();
		ignore.add("javax.slee.ActivityContextInterface");
		// FIXME: we could go other way, run this for each super interface we
		// have???
		Map<String, CtMethod> aciInterfacesDefinedMethods = ClassUtils
				.getAllInterfacesMethods(sbbAciInterface, ignore);

		// Here we will store fields name-type - if there is getter and setter,
		// type must match!!!
		Map<String, CtClass> localNameToType = new HashMap<String, CtClass>();

		for (String methodKey : aciInterfacesDefinedMethods.keySet()) {

			CtMethod fieldMethod = aciInterfacesDefinedMethods.get(methodKey);
			String methodName = fieldMethod.getName();
			if (!(methodName.startsWith("get") || methodName.startsWith("set"))) {
				passed = false;
				appendToBuffer(
						sbbAciInterface.getName()
								+ " custom ACI  has wrong method definition(decalred or inherited), all methods must start either with \"set\" or \"get\": "
								+ methodName, errorBuffer);
				continue;
			}

			// let us get field name:

			String fieldName = methodName.replaceFirst("set", "").replaceFirst(
					"get", "");

			if (!Character.isUpperCase(fieldName.charAt(0))) {
				passed = false;
				appendToBuffer(
						sbbAciInterface.getName()
								+ " custom ACI  has wrong method definition(decalred or inherited), getter/setter 4th char must be upper case character"
								+ methodName, errorBuffer);

			}

			// check throws clause.
			// number of parameters
			try {
				if (fieldMethod.getExceptionTypes().length > 0) {
					passed = false;
					appendToBuffer(
							sbbAciInterface.getName()
									+ " custom ACI  has wrong method definition(decalred or inherited), getter/setter should not have throws clause: "
									+ methodName, errorBuffer);

				}
			} catch (NotFoundException e) {
				// This should nto happen?
				e.printStackTrace();
			}

			try {
				boolean isGetter = methodName.startsWith("get");
				CtClass fieldType = null;
				if (isGetter) {
					// no params
					if (fieldMethod.getParameterTypes() != null
							&& fieldMethod.getParameterTypes().length > 0) {
						passed = false;
						appendToBuffer(
								sbbAciInterface.getName()
										+ " custom ACI  has wrong method definition(decalred or inherited),getter must not have parameters: "
										+ methodName, errorBuffer);

					}

					fieldType = fieldMethod.getReturnType();
					if (fieldType.getName().compareTo("void") == 0) {
						passed = false;
						appendToBuffer(
								sbbAciInterface.getName()
										+ " custom ACI has wrong method definition(decalred or inherited), getter must have return type: "
										+ methodName, errorBuffer);

					}

				} else {
					if (fieldMethod.getParameterTypes() != null
							&& fieldMethod.getParameterTypes().length != 1) {
						passed = false;
						appendToBuffer(
								sbbAciInterface.getName()
										+ " custom ACI  has wrong method definition(decalred or inherited),setter must  have exactly one parameter: "
										+ methodName, errorBuffer);

						// Here we quick fail
						continue;
					}

					fieldType = fieldMethod.getParameterTypes()[0];
					if (fieldMethod.getReturnType().getName().compareTo("void") != 0) {
						passed = false;
						appendToBuffer(
								sbbAciInterface.getName()
										+ " custom ACI has wrong method definition(decalred or inherited), setter must not have return type: "
										+ methodName, errorBuffer);

					}

				}

				// Field type can be primitive and serialzable
				if (!(_PRIMITIVES.contains(fieldType.getName()) || ClassUtils
						.checkInterfaces(fieldType, "java.io.Serializable") != null)) {
					passed = false;
					appendToBuffer(
							sbbAciInterface.getName()
									+ " custom ACI has wrong field type - only seirlizables and primitives are allowed, method: "
									+ fieldMethod.getName() + ", type: "
									+ fieldType.getName(), errorBuffer);
					// we fail here
					continue;
				}

				if (localNameToType.containsKey(fieldName)) {
					CtClass storedType = localNameToType.get(fieldName);
					if (!storedType.equals(fieldType)) {
						passed = false;
						appendToBuffer(
								sbbAciInterface.getName()
										+ " custom ACI has wrong definition of parameter - setter and getter types do not match: "
										+ fieldName + ", type1: "
										+ fieldType.getName() + " typ2:"
										+ storedType.getName(), errorBuffer);
						// we fail here
						continue;
					}
				} else {
					// simply store
					localNameToType.put(fieldName, fieldType);
				}

			} catch (NotFoundException nfe) {
			}
		}

		// FIXME: add check against components get aci fields ?

		if (!passed) {
			logger.error(errorBuffer.toString());
			System.err.println(errorBuffer);
		}

		return passed;
	}

	boolean validateGetChildRelationMethods(
			Map<String, CtMethod> sbbAbstractClassAbstractMethod,
			Map<String, CtMethod> sbbAbstractClassAbstractMethodFromSuperClasses) {

		boolean passed = true;
		StringBuffer errorBuffer = new StringBuffer();

		// FIXME: its cant be out of scope, since its byte....
		// we look for method key
		for (MGetChildRelationMethod mMetod : this.component.getDescriptor()
				.getSbbAbstractClass().getChildRelationMethods()) {
			if (mMetod.getDefaultPriority() > 127
					|| mMetod.getDefaultPriority() < -128) {
				passed = false;

				appendToBuffer("Defined in "
						+ this.component.getCtAbstractSbbClass().getName()
						+ " get child relation method priority for method: "
						+ mMetod.getChildRelationMethodName()
						+ " is out of scope!!", errorBuffer);
			}

			// This is key == <<methodName>>()Ljavax/slee/ChildRelation
			// We it makes sure that method name, parameters, and return type is
			// ok.
			String methodKey = mMetod.getChildRelationMethodName()
					+ _SBB_GET_CHILD_RELATION_SIGNATURE_PART;

			CtMethod childRelationMethod = null;
			childRelationMethod = sbbAbstractClassAbstractMethod.get(methodKey);

			if (childRelationMethod == null) {
				childRelationMethod = sbbAbstractClassAbstractMethodFromSuperClasses
						.get(methodKey);

			}

			if (childRelationMethod == null) {
				passed = false;

				appendToBuffer(
						"Defined in "
								+ this.component.getCtAbstractSbbClass()
										.getName()
								+ " get child relation method defined in descriptor: "
								+ mMetod.getChildRelationMethodName()
								+ " is not matched by any abstract method, either its not abstract, is private, has parameter or has wrong return type(should be javax.slee.ChildRelation)!",
						errorBuffer);
				// we fail fast here
				continue;
			}

			// if we are here we have to check throws clause, prefix - it cant
			// be ejb or sbb

			try {
				if (childRelationMethod.getExceptionTypes().length > 0) {
					passed = false;

					appendToBuffer(
							"Defined in "
									+ this.component.getCtAbstractSbbClass()
											.getName()
									+ " get child relation method defined in descriptor: "
									+ mMetod.getChildRelationMethodName()
									+ " defines throws clause.!", errorBuffer);
				}
			} catch (NotFoundException e) {

				e.printStackTrace();
				passed = false;

				appendToBuffer("Defined in "
						+ this.component.getCtAbstractSbbClass().getName()
						+ " get child relation method defined in descriptor: "
						+ mMetod.getChildRelationMethodName()
						+ " defines throws clause.!", errorBuffer);
			}

			if (childRelationMethod.getName().startsWith("ejb")
					|| childRelationMethod.getName().startsWith("sbb")) {
				// this is checked for concrete methods only
				passed = false;

				appendToBuffer(
						"Defined in "
								+ this.component.getCtAbstractSbbClass()
										.getName()
								+ " get child relation method defined in descriptor: "
								+ mMetod.getChildRelationMethodName()
								+ " has wrong prefix, it can not start with \"ejb\" or \"sbb\".!",
						errorBuffer);
			}

			// remove, we will later determine methods that were not implemented
			// by this
			if (childRelationMethod != null) {
				sbbAbstractClassAbstractMethod.remove(methodKey);
				sbbAbstractClassAbstractMethodFromSuperClasses
						.remove(methodKey);
			}

		}

		if (!passed) {
			logger.error(errorBuffer.toString());
			System.err.println(errorBuffer);
		}

		return passed;

	}

	boolean validateSbbLocalInterface(
			Map<String, CtMethod> sbbAbstractClassConcreteMethods,
			Map<String, CtMethod> sbbAbstractClassConcreteFromSuperClasses) {

		boolean passed = true;
		StringBuffer errorBuffer = new StringBuffer();
		if (this.component.getDescriptor().getSbbLocalInterface() == null)
			return passed;

		CtClass sbbLocalInterfaceClass = this.component
				.getCtSbbLocalInterface();
		CtClass genericSbbLocalInterface = ClassUtils.checkInterfaces(
				sbbLocalInterfaceClass, "javax.slee.SbbLocalObject");

		if (genericSbbLocalInterface == null) {
			passed = false;
			appendToBuffer(
					" Defined in descriptor SbbLocalInterface: "
							+ sbbLocalInterfaceClass.getName()
							+ " does not implement javax.slee.SbbLocalInterface super interface in any way!!!",
					errorBuffer);
		}

		int sbbLocalInterfaceClassModifiers = sbbLocalInterfaceClass
				.getModifiers();
		if (this.component.isSlee11()
				&& sbbLocalInterfaceClass.getPackageName() == null) {
			passed = false;
			appendToBuffer(
					" Defined in descriptor SbbLocalInterface: "
							+ sbbLocalInterfaceClass.getName()
							+ " is not contained in java package, according to JSLEE 1.1 it should be!!!",
					errorBuffer);
		}

		if (!Modifier.isPublic(sbbLocalInterfaceClassModifiers)) {
			passed = false;
			appendToBuffer(" Defined in descriptor SbbLocalInterface: "
					+ sbbLocalInterfaceClass.getName() + " must be public!!!",
					errorBuffer);
		}

		Set<String> ignore = new HashSet<String>();
		ignore.add("javax.slee.SbbLocalObject");
		ignore.add("java.lang.Object");
		Map<String, CtMethod> interfaceMethods = ClassUtils
				.getAllInterfacesMethods(sbbLocalInterfaceClass, ignore);

		// here we have all defined methods in interface, we have to checkif
		// their names do not start with sbb/ejb and if they are contained in
		// collections with concrete methods from sbb

		for (CtMethod methodToCheck : interfaceMethods.values()) {
			if (methodToCheck.getName().startsWith("ejb")
					|| methodToCheck.getName().startsWith("sbb")) {
				passed = false;
				appendToBuffer(" Method from SbbLocalInterface: "
						+ sbbLocalInterfaceClass.getName()
						+ " starts with wrong prefix: "
						+ methodToCheck.getName(), errorBuffer);

			}

			CtMethod methodFromSbbClass = sbbAbstractClassConcreteMethods
					.get(ClassUtils.getMethodKey(methodToCheck));
			if (methodFromSbbClass == null) {
				methodFromSbbClass = sbbAbstractClassConcreteFromSuperClasses
						.get(ClassUtils.getMethodKey(methodToCheck));
			}

			if (methodFromSbbClass == null) {

				passed = false;
				appendToBuffer(
						" Method from SbbLocalInterface: "
								+ sbbLocalInterfaceClass.getName()
								+ "with name:  "
								+ methodToCheck.getName()
								+ " is not implemented by sbb class or its super classes!",
						errorBuffer);

				// we fails fast here
				continue;
			}

			// XXX: Note this does not check throws clause, only name and
			// signature
			// this side
			//FIXME: Note that we dont check modifier, is this corerct
			try {
				if (!methodFromSbbClass.equals(methodToCheck)
						
						|| !Arrays.equals((Object[])methodFromSbbClass.getExceptionTypes(),
								(Object[])methodToCheck.getExceptionTypes())) {

					passed = false;
					appendToBuffer(
							" Method from SbbLocalInterface: "
									+ sbbLocalInterfaceClass.getName()
									+ "with name:  "
									+ methodToCheck.getName()
									+ " is not implemented by sbb class or its super classes. Its visibility, throws clause is different or modifiers are different!",
							errorBuffer);

					// we fails fast here
					continue;

				}
			} catch (NotFoundException e) {
				passed = false;
				appendToBuffer(
						" Method from SbbLocalInterface: "
								+ sbbLocalInterfaceClass.getName()
								+ "with name:  "
								+ methodToCheck.getName()
								+ " is not implemented by sbb class or its super classes. Its visibility or throws clause is different!",
						errorBuffer);
				//e.printStackTrace();
			}

		}

		// FIXME: is this ok, is it needed ? If not checked here, abstract
		// methods check will make it fail later, but not concrete ?
		// now lets check javax.slee.SbbLocalObject methods - sbb cant have
		// those implemented or defined as abstract.

		if (!passed) {
			logger.error(errorBuffer.toString());
			System.err.println(errorBuffer);
		}

		return passed;
	}

	boolean validateCMPFields(
			Map<String, CtMethod> sbbAbstractClassAbstractMethod,
			Map<String, CtMethod> sbbAbstractClassAbstractMethodFromSuperClasses) {

		boolean passed = true;

		return passed;

	}

	protected void appendToBuffer(String message, StringBuffer buffer) {
		buffer.append(this.component.getDescriptor().getSbbComponentKey()
				+ " : " + message + "\n");
	}

	public static void main(String[] args) {
		ClassPool pool = new ClassPool();
		CtClass ct = pool
				.makeClass("org.mobicents.slee.container.component.SbbComponent");
		CtClass ctInterface = pool
				.makeClass("org.mobicents.slee.container.component.SbbComponent2");
		CtConstructor con = new CtConstructor(null, ct);
		try {

			con.setModifiers(Modifier.PUBLIC);
			ct.addConstructor(con);
			CtClass[] parameters = new CtClass[] {
					pool.makeClass("java.lang.Exception"),
					pool.makeClass("java.lang.Object"),
					pool.makeClass("javax.slee.ActivityContextInterface") };
			CtMethod sbbExceptionThrown = new CtMethod(null,
					"sbbExceptionThrown", parameters, ct);
			sbbExceptionThrown.setModifiers(Modifier.PUBLIC);

			CtMethod sbbExceptionThrown2 = new CtMethod(null,
					"sbbExceptionThrown", parameters, ct);
			sbbExceptionThrown2.setModifiers(Modifier.PUBLIC);
			ct.addMethod(sbbExceptionThrown2);

			CtMethod sbbRolledBack = new CtMethod(null, "sbbRolledBack",
					new CtClass[] { pool
							.makeClass("javax.slee.RoledBackContext") }, ct);

			System.out.println("Class " + ct);
			System.out
					.println(ct
							.getMethod(
									"sbbExceptionThrown",
									"(Ljava/lang/Exception;Ljava/lang/Object;Ljavax/slee/ActivityContextInterface;)"));

			System.out.println(sbbExceptionThrown.getName()
					+ sbbExceptionThrown.getSignature());
			System.out.println(sbbRolledBack.getName() + ":"
					+ sbbRolledBack.getSignature());

			ctInterface.setModifiers(Modifier.INTERFACE);
			ctInterface.setModifiers(Modifier.PUBLIC);
			CtMethod sbbExceptionThrown3 = new CtMethod(null,
					"sbbExceptionThrown", parameters, ctInterface);
			sbbExceptionThrown3.setModifiers(Modifier.PUBLIC);
			ctInterface.addMethod(sbbExceptionThrown3);

			CtClass asSbbActiityContextInterfaceReturn = pool
					.makeClass("javax.slee.RoledBackContext");
			CtClass[] asSbbActiityContextInterfaceParamters = new CtClass[] { pool
					.makeClass("javax/slee/ActivityContextInterface") };
			CtMethod asSbbActivityCOntextInterface = new CtMethod(
					asSbbActiityContextInterfaceReturn,
					"asSbbActiityContextInterface",
					asSbbActiityContextInterfaceParamters, ctInterface);
			sbbExceptionThrown3.setModifiers(Modifier.PUBLIC);

			ctInterface.addMethod(asSbbActivityCOntextInterface);

			System.out
					.println("=============================================================");
			// ctInterface.getMethods();
			// System.out.println(""+Arrays.toString(ctInterface.getMethods()));
			// System.out.println(""+Arrays.toString(ct.getMethods()));

			CtMethod childRelation = new CtMethod(pool
					.makeClass("javax.slee.ChildRelation"),
					"testgetChildRelation", null, ct);

			ct.addMethod(childRelation);
			System.out.println(childRelation);
			System.out.println(childRelation.getName()
					+ childRelation.getSignature());
			System.out
					.println("=============================================================");
			ct.writeFile();
			ct.freeze();
		} catch (CannotCompileException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
