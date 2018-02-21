/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * Start time:17:06:21 2009-01-30<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.SbbLocalObject;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.slee.ActivityContextInterfaceExt;
import org.mobicents.slee.SbbLocalObjectExt;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.SbbComponentImpl;
import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetChildRelationMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetProfileCMPMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbCMPField;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.sbb.CMPFieldDescriptor;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.component.sbb.GetChildRelationMethodDescriptor;
import org.mobicents.slee.container.component.sbb.GetProfileCMPMethodDescriptor;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.sbb.SbbDescriptor;
import org.mobicents.slee.container.component.sbb.SbbRefDescriptor;

/**
 * Start time:17:06:21 2009-01-30<br>
 * Project: restcomm-jainslee-server-core<br>
 * base validator for sbb components. It validates all sbb class constraints.
 * However it does not check referential constraints and similar. Checks that
 * have to be done before this class is used are: reference checks(this includes
 * dependencies), field values - like duplicate cmps, duplicate entries.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbComponentValidator implements Validator {

	public static final String _SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE = "asSbbActivityContextInterface";

	public static final String _SBB_GET_CHILD_RELATION_SIGNATURE_PART = "[]";

	private SbbComponentImpl component = null;
	private ComponentRepository repository = null;
	private final static transient Logger logger = Logger.getLogger(SbbComponentValidator.class);
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

	private final static Set<String> _ENV_ENTRIES_TYPES;
	static {
		Set<String> tmp = new HashSet<String>();
		tmp.add(Integer.class.getName());
		tmp.add(Boolean.class.getName());
		tmp.add(Byte.class.getName());
		tmp.add(Character.class.getName());
		tmp.add(Double.class.getName());
		tmp.add(Float.class.getName());
		tmp.add(Long.class.getName());
		tmp.add(Short.class.getName());
		tmp.add(String.class.getName());
		_ENV_ENTRIES_TYPES = Collections.unmodifiableSet(tmp);

	}

	public boolean validate() {

		boolean valid = true;
		// Uf here we go
		try {
			if (!validateDescriptor()) {
				valid = false;
				return valid;
			}
			Map<String, Method> abstractMehotds, superClassesAbstractMethod, concreteMethods, superClassesConcreteMethods;

			abstractMehotds = ClassUtils.getAbstractMethodsFromClass(this.component.getAbstractSbbClass());
			superClassesAbstractMethod = ClassUtils.getAbstractMethodsFromSuperClasses(this.component.getAbstractSbbClass());
			concreteMethods = ClassUtils.getConcreteMethodsFromClass(this.component.getAbstractSbbClass());
			superClassesConcreteMethods = ClassUtils.getConcreteMethodsFromSuperClasses(this.component.getAbstractSbbClass());

			//NOTE: wont this hide method exceptions or visibility change?
			superClassesAbstractMethod.keySet().removeAll(abstractMehotds.keySet());
			superClassesConcreteMethods.keySet().removeAll(concreteMethods.keySet());
			
			if (!validateAbstractClassConstraints(concreteMethods, superClassesConcreteMethods)) {
				valid = false;
			}

			if (!validateCmpFileds(abstractMehotds, superClassesAbstractMethod)) {
				valid = false;
			}

			if (!validateEventHandlers(abstractMehotds, superClassesAbstractMethod, concreteMethods, superClassesConcreteMethods)) {
				valid = false;
			}

			if (!validateGetChildRelationMethods(abstractMehotds, superClassesAbstractMethod)) {
				valid = false;
			}

			if (!validateGetProfileCmpInterfaceMethods(abstractMehotds, superClassesAbstractMethod)) {
				valid = false;
			}

			if (!validateSbbActivityContextInterface(abstractMehotds, superClassesAbstractMethod)) {
				valid = false;
			}

			if (!validateSbbLocalInterface(concreteMethods, superClassesConcreteMethods)) {
				valid = false;
			}

			if (!validateSbbUsageParameterInterface(abstractMehotds, superClassesAbstractMethod)) {
				valid = false;
			}

			if (!validateEnvEntries()) {
				valid = false;
			}

			// now lets test abstract methods, we have to remove all of them by
			// now?
			if (abstractMehotds.size() > 0) {
				valid = false;
				logger
						.error(this.component.getDescriptor().getSbbID()
								+ " : violates sbb constraints, the sbb abstract class declares more abstract methods than SLEE is bound to implement: "
								+ Arrays.toString(abstractMehotds.keySet().toArray()));
								
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			valid = false;

		}

		return valid;

	}

	public void setComponentRepository(ComponentRepository repository) {
		this.repository = repository;

	}

	public SbbComponentImpl getComponent() {
		return component;
	}

	public void setComponent(SbbComponentImpl component) {
		this.component = component;
	}

	/**
	 * Sbb abstract class(general rule � methods cannot start neither with �ejb�
	 * nor �sbb�)
	 * <ul>
	 * <li>(1.1 ?) must have package declaration
	 * <li>must implement in some way javax.slee.Sbb(only methods from interface
	 * can have �sbb� prefix)
	 * <ul>
	 * <li>each method defined must be implemented as public � not abstract,
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
	boolean validateAbstractClassConstraints(Map<String, Method> concreteMethods, Map<String, Method> concreteSuperClassesMethods) {

		String errorBuffer = new String("");
		boolean passed = true;
		// Presence of those classes must be checked elsewhere
		Class sbbAbstractClass = this.component.getAbstractSbbClass();

		// Must be public and abstract
		int modifiers = sbbAbstractClass.getModifiers();
		// check that the class modifiers contain abstratc and public
		if (!Modifier.isAbstract(modifiers) || !Modifier.isPublic(modifiers)) {
			errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "sbb abstract class must be public and abstract", "6.1", errorBuffer);
		}

		// 1.1 - must be in package
		if (this.component.isSlee11()) {
			Package declaredPackage = sbbAbstractClass.getPackage();
			if (declaredPackage == null || declaredPackage.getName().compareTo("") == 0) {
				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "sbb abstract class must be defined inside package space", "6.1",
						errorBuffer);
			}

		}

		// Public no arg constructor - can it have more ?
		// sbbAbstractClass.getConstructor
		// FIXME: no arg constructor has signature "()V" when added from
		// javaassist we check for such constructor and if its public
		try {
			Constructor constructor = sbbAbstractClass.getConstructor();
			int conMod = constructor.getModifiers();
			if (!Modifier.isPublic(conMod)) {
				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "sbb abstract class must have public constructor ", "6.1",
						errorBuffer);
			}
		} catch (SecurityException e) {

			e.printStackTrace();
			passed = false;
			errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "sbb abstract class must have no arg constructor, error:"
					+ e.getMessage(), "6.1", errorBuffer);
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
			passed = false;
			errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "sbb abstract class must have no arg constructor, error:"
					+ e.getMessage(), "6.1", errorBuffer);
		}

		// Must implements javax.slee.Sbb - and each method there defined, only
		// those methods and two above can have "sbb" prefix
		// those methods MUST be in concrete methods map, later we will use them
		// to see if there is ant "sbbXXXX" method

		// Check if we implement javax.slee.Sbb - either directly or from super
		// class
		Class javaxSleeSbbInterface = ClassUtils.checkInterfaces(sbbAbstractClass, "javax.slee.Sbb");
		
		// sbbAbstractClass.getI
		if (javaxSleeSbbInterface == null) {

			passed = false;
			errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
					+ "sbb abstract class  must implement, directly or indirectly, the javax.slee.Sbb interface.", "6.1", errorBuffer);
		}

		// FIXME: add check for finalize method

		// Now we have to check methods from javax.slee.Sbb
		// This takes care of method throws clauses
		if (javaxSleeSbbInterface != null) {
			// if it is, we dont have those methods for sure, or maybe we do,
			// implemnted by hand
			// either way its a failure
			// We want only java.slee.Sbb methods :)
			Method[] sbbLifecycleMethods = javaxSleeSbbInterface.getDeclaredMethods();
			for (Method lifecycleMehtod : sbbLifecycleMethods) {
				// It must be implemented - so only in concrete methods, if we
				// are left with one not checked bang, its an error

				String methodKey = ClassUtils.getMethodKey(lifecycleMehtod);
				Method concreteLifeCycleImpl = null;
				if (concreteMethods.containsKey(methodKey)) {
					concreteLifeCycleImpl = concreteMethods.remove(methodKey);
				} else if (concreteSuperClassesMethods.containsKey(methodKey)) {
					concreteLifeCycleImpl = concreteSuperClassesMethods.remove(methodKey);
				} else {
					passed = false;
					errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
							+ "sbb abstract class must implement life cycle methods, it lacks concrete implementation of: "
							+ lifecycleMehtod.getName(), "6.1.1", errorBuffer);
					continue;
				}

				// now we now there is such method, its not private and abstract
				// If we are here its not null
				int lifeCycleModifier = concreteLifeCycleImpl.getModifiers();
				if (!Modifier.isPublic(lifeCycleModifier) || Modifier.isStatic(lifeCycleModifier) || Modifier.isFinal(lifeCycleModifier)) {
					passed = false;
					errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
							+ "sbb abstract class must implement life cycle methods, which can not be static, final or not public, method: "
							+ lifecycleMehtod.getName(), "6.1.1", errorBuffer);
				}

			}
		}

		// there can not be any method which start with ejb/sbb - we removed
		// every from concrete, lets iterate over those sets
		for (Method concreteMethod : concreteMethods.values()) {

			if (concreteMethod.getName().startsWith("ejb") || concreteMethod.getName().startsWith("sbb")) {

				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + " with method:  " + concreteMethod.getName(), "6.12", errorBuffer);

			}

			if (concreteMethod.getName().compareTo("finalize") == 0) {
				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "sbb abstract class  must not implement \"finalize\" method.",
						"6.1", errorBuffer);
			}
		}

		for (Method concreteMethod : concreteSuperClassesMethods.values()) {
			if (concreteMethod.getName().startsWith("ejb") || concreteMethod.getName().startsWith("sbb")) {

				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + " with method from super classes:  " + concreteMethod.getName(),
						"6.12", errorBuffer);

			}

			if (concreteMethod.getName().compareTo("finalize") == 0) {
				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
						+ "sbb abstract class  must not implement \"finalize\" method. Its implemented by super class.", "6.1", errorBuffer);
			}

		}

		if (!passed) {
			logger.error(errorBuffer);
		}

		return passed;

	}

	/**
	 * This method checks for presence of
	 * 
	 * @param sbbAbstractClassAbstraMethod
	 * @param sbbAbstractClassAbstraMethodFromSuperClasses
	 * @return
	 */
	boolean validateSbbActivityContextInterface(Map<String, Method> sbbAbstractClassAbstraMethod,
			Map<String, Method> sbbAbstractClassAbstraMethodFromSuperClasses) {

		if (this.component.getDescriptor().getSbbActivityContextInterface() == null) {
			// FIXME: add check for asSbbActivityContextInteface method ? This
			// will be catched at the end of check anyway
			if (logger.isTraceEnabled()) {
				logger.trace(this.component.getDescriptor().getSbbID() + " : No Sbb activity context interface defined");
			}
			return true;
		}

		String errorBuffer = new String("");
		boolean passed = true;

		Method asACIMethod = null;

		// lets go through methods of sbbAbstract class,
		Iterator<Method> it = sbbAbstractClassAbstraMethod.values().iterator();
		while(it.hasNext())
		{
			Method someMethod = it.next();
	
			if (someMethod.getName().compareTo(_SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE) == 0) {
				// we have a winner, possibly - we have to check parameter
				// list, cause someone can create abstract method(or crap,
				// it can be concrete) with different parametrs, in case its
				// abstract, it will fail later on	


				if (someMethod.getParameterTypes().length == 1
						&& someMethod.getParameterTypes()[0].getName().compareTo("javax.slee.ActivityContextInterface") == 0) {
					asACIMethod = someMethod;

					it.remove();
					break;
				}
			}
		}
		

		if (asACIMethod == null)
			it = sbbAbstractClassAbstraMethodFromSuperClasses.values().iterator();
		while(it.hasNext())
		{
			Method someMethod = it.next();

				if (someMethod.getName().compareTo(_SBB_AS_SBB_ACTIVITY_CONTEXT_INTERFACE) == 0) {
					// we have a winner, possibly - we have to check
					// parameter
					// list, cause someone can create abstract method(or
					// crap,
					// it can be concrete) with different parametrs, in case
					// its
					// abstract, it will fail later on

					if (someMethod.getParameterTypes().length == 1
							&& someMethod.getParameterTypes()[0].getName().compareTo("javax.slee.ActivityContextInterface") == 0) {
						asACIMethod = someMethod;
	
						it.remove();
						break;
					}
				}
			}
		if (asACIMethod == null) {
			passed = false;
			errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + " must imlement narrow method asSbbActivityContextInterface",
					"7.7.2", errorBuffer);

		} else {
			// must be public, abstract? FIXME: not native?
			int asACIMethodModifiers = asACIMethod.getModifiers();
			if (!Modifier.isPublic(asACIMethodModifiers) || !Modifier.isAbstract(asACIMethodModifiers) || Modifier.isNative(asACIMethodModifiers)) {
				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
						+ " narrow method asSbbActivityContextInterface must be public,abstract and not native.", "7.7.2", errorBuffer);
			}

			// now this misery comes to play, return type check
			Class returnType = asACIMethod.getReturnType();
			// Must return something from Sbb defined aci class inheritance
			// tree
			Class definedReturnType = this.component.getActivityContextInterface();

			if (returnType.getName().compareTo("void") == 0) {
				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
						+ " narrow method asSbbActivityContextInterface must have return type.", "7.7.2", errorBuffer);
			} else if (returnType.equals(definedReturnType)) {
				// its ok

				// } else if (ClassUtils.checkInterfaces(definedReturnType,
				// returnType
				// .getName()) != null) {

			} else {
				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
						+ " narrow method asSbbActivityContextInterface has wrong return type: " + returnType, "7.7.2", errorBuffer);

			}

			// no throws clause
			if (asACIMethod.getExceptionTypes() != null && asACIMethod.getExceptionTypes().length > 0) {

				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
						+ " narrow method asSbbActivityContextInterface must not have throws clause.", "7.7.2", errorBuffer);
			}

		}

		// Even if we fail above we can do some checks on ACI if its present.
		// this has to be present
		Class sbbActivityContextInterface = this.component.getActivityContextInterface();

		// ACI VALIDATION
		// (1.1) = must be declared in package

		if (this.component.isSlee11() && sbbActivityContextInterface.getPackage() == null) {
			passed = false;
			errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + " sbb activity context interface must be declared in package.",
					"7.5", errorBuffer);
		}

		if (!Modifier.isPublic(sbbActivityContextInterface.getModifiers())) {
			passed = false;
			errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + " sbb activity context interface must be declared as public.", "7.5",
					errorBuffer);
		}

		// We can have here ACI objects and java primitives, ugh, both methods
		// dont have to be shown
		passed = checkSbbAciFieldsConstraints(this.component.getActivityContextInterface());

		// finally lets remove asSbb method form abstract lists, this is used
		// later to determine methods that didnt match any sbb definition
		if (asACIMethod != null) {
			sbbAbstractClassAbstraMethod.remove(ClassUtils.getMethodKey(asACIMethod));
			sbbAbstractClassAbstraMethodFromSuperClasses.remove(ClassUtils.getMethodKey(asACIMethod));
		}

		if (!passed) {
			logger.error(errorBuffer);		
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
	boolean checkSbbAciFieldsConstraints(Class sbbAciInterface) {

		boolean passed = true;
		String errorBuffer = new String("");
		try {

			if (!sbbAciInterface.isInterface()) {
				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + " sbb activity context interface MUST be an interface.", "7.5",
						errorBuffer);
				return passed;
			}

			// here we need all fields :)
			HashSet<String> ignore = new HashSet<String>();
			ignore.add(ActivityContextInterfaceExt.class.getName());
			ignore.add(ActivityContextInterface.class.getName());

			// FIXME: we could go other way, run this for each super interface
			// we
			// have???

			Map<String, Method> aciInterfacesDefinedMethods = ClassUtils.getAllInterfacesMethods(sbbAciInterface, ignore);

			// Here we will store fields name-type - if there is getter and
			// setter,
			// type must match!!!
			Map<String, Class> localNameToType = new HashMap<String, Class>();

			for (String methodKey : aciInterfacesDefinedMethods.keySet()) {

				Method fieldMethod = aciInterfacesDefinedMethods.get(methodKey);
				String methodName = fieldMethod.getName();
				if (!(methodName.startsWith("get") || methodName.startsWith("set"))) {
					passed = false;
					errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
							+ " sbb activity context interface can have only getter/setter  methods.", "7.5.1", errorBuffer);
					continue;
				}

				// let us get field name:

				String fieldName = methodName.replaceFirst("set", "").replaceFirst("get", "");

				if (!Character.isUpperCase(fieldName.charAt(0))) {
					passed = false;
					errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
							+ " sbb activity context interface can have only getter/setter  methods - 4th char in those methods must be capital.",
							"7.5.1", errorBuffer);

				}

				// check throws clause.
				// number of parameters

				if (fieldMethod.getExceptionTypes().length > 0) {
					passed = false;
					errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
							+ " sbb activity context interface getter method must have empty throws clause: " + fieldMethod.getName(), "7.5.1",
							errorBuffer);

				}

				boolean isGetter = methodName.startsWith("get");
				Class fieldType = null;
				if (isGetter) {
					// no params
					if (fieldMethod.getParameterTypes() != null && fieldMethod.getParameterTypes().length > 0) {
						passed = false;
						errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
								+ " sbb activity context interface getter method must not have parameters: " + fieldMethod.getName(), "7.5.1",
								errorBuffer);

					}

					fieldType = fieldMethod.getReturnType();
					if (fieldType.getName().compareTo("void") == 0) {
						passed = false;
						errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
								+ " sbb activity context interface getter method must have return type: " + fieldMethod.getName(), "7.5.1",
								errorBuffer);

					}

				} else {
					if (fieldMethod.getParameterTypes() != null && fieldMethod.getParameterTypes().length != 1) {
						passed = false;
						errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
								+ " sbb activity context interface setter method must single parameter: " + fieldMethod.getName(), "7.5.1",
								errorBuffer);

						// Here we quick fail
						continue;
					}

					fieldType = fieldMethod.getParameterTypes()[0];
					if (fieldMethod.getReturnType().getName().compareTo("void") != 0) {
						passed = false;
						errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
								+ " sbb activity context interface setter method must not have return type: " + fieldMethod.getName(), "7.5.1",
								errorBuffer);

					}

				}

				// Field type can be primitive and serialzable
				if (!(_PRIMITIVES.contains(fieldType.getName()) || ClassUtils.checkInterfaces(fieldType, "java.io.Serializable") != null)) {
					passed = false;
					errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + " sbb activity context interface field(" + fieldName
							+ ") has wrong type, only primitives and serializable: " + fieldType, "7.5.1", errorBuffer);
					// we fail here
					continue;
				}

				if (localNameToType.containsKey(fieldName)) {
					Class storedType = localNameToType.get(fieldName);
					if (!storedType.equals(fieldType)) {
						passed = false;

						errorBuffer = appendToBuffer(this.component.getAbstractSbbClass()
								+ " sbb activity context interface has wrong definition of parameter - setter and getter types do not match: "
								+ fieldName + ", type1: " + fieldType.getName() + " typ2:" + storedType.getName(), "7.5.1", errorBuffer);

						// we fail here
						continue;
					}
				} else {
					// simply store
					localNameToType.put(fieldName, fieldType);
				}

			}

			// FIXME: add check against components get aci fields ?
		} finally {
			if (!passed) {
				logger.error(errorBuffer);				
			}
		}

		return passed;
	}

	boolean validateGetChildRelationMethods(Map<String, Method> sbbAbstractClassAbstractMethod,
			Map<String, Method> sbbAbstractClassAbstractMethodFromSuperClasses) {

		boolean passed = true;
		String errorBuffer = new String("");

		// FIXME: its cant be out of scope, since its byte....
		// we look for method key
		for (GetChildRelationMethodDescriptor mMetod : this.component.getDescriptor().getSbbAbstractClass().getChildRelationMethods().values()) {
			if (mMetod.getDefaultPriority() > 127 || mMetod.getDefaultPriority() < -128) {
				passed = false;

				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "Defined  get child relation method priority for method: "
						+ mMetod.getChildRelationMethodName() + " is out of scope!!", "6.8", errorBuffer);

			}

			// This is key == <<methodName>>()Ljavax/slee/ChildRelation
			// We it makes sure that method name, parameters, and return type is
			// ok.
			String methodKey = mMetod.getChildRelationMethodName() + _SBB_GET_CHILD_RELATION_SIGNATURE_PART;

			Method childRelationMethod = null;
			childRelationMethod = sbbAbstractClassAbstractMethod.get(methodKey);

			if (childRelationMethod == null) {
				childRelationMethod = sbbAbstractClassAbstractMethodFromSuperClasses.get(methodKey);

			}

			if (childRelationMethod == null) {
				passed = false;

				errorBuffer = appendToBuffer(
						this.component.getAbstractSbbClass() //rekatuib ??
								+ "Defined  get child relation method: "
								+ mMetod.getChildRelationMethodName()
								+ " is not matched by any abstract method, either its not abstract, is private, has parameter or has wrong return type(should be javax.slee.ChildRelation)!!",
						"6.8", errorBuffer);

				// we fail fast here
				continue;
			}

			// if we are here we have to check throws clause, prefix - it cant
			// be ejb or sbb

			if (childRelationMethod.getExceptionTypes().length > 0) {
				passed = false;

				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "Defined  get child relation method priority for method: "
						+ mMetod.getChildRelationMethodName() + " must hot have throws clause", "6.8", errorBuffer);
			}

			if (childRelationMethod.getName().startsWith("ejb") || childRelationMethod.getName().startsWith("sbb")) {
				// this is checked for concrete methods only
				passed = false;

				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "Defined  get child relation method priority for method: "
						+ mMetod.getChildRelationMethodName() + " has wrong prefix, it can not start with \"ejb\" or \"sbb\".!", "6.8", errorBuffer);

			}

			// remove, we will later determine methods that were not implemented
			// by this
			if (childRelationMethod != null) {
				sbbAbstractClassAbstractMethod.remove(methodKey);
				sbbAbstractClassAbstractMethodFromSuperClasses.remove(methodKey);
			}

		}

		if (!passed) {
			logger.error(errorBuffer);			
		}

		return passed;

	}

	boolean validateSbbLocalInterface(Map<String, Method> sbbAbstractClassConcreteMethods,
			Map<String, Method> sbbAbstractClassConcreteFromSuperClasses) {

		boolean passed = true;
		String errorBuffer = new String("");

		try {
			if (this.component.getDescriptor().getSbbLocalInterface() == null)
				return passed;

			Class sbbLocalInterfaceClass = this.component.getSbbLocalInterfaceClass();

			if (!sbbLocalInterfaceClass.isInterface()) {
				passed = false;

				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "DSbbLocalInterface: " + sbbLocalInterfaceClass.getName()
						+ " MUST be an interface!", "5.6", errorBuffer);
				return passed;
			}

			Class genericSbbLocalInterface = ClassUtils.checkInterfaces(sbbLocalInterfaceClass, SbbLocalObject.class.getName());

			if (genericSbbLocalInterface == null) {
				passed = false;

				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "DSbbLocalInterface: " + sbbLocalInterfaceClass.getName()
						+ " does not implement javax.slee.SbbLocalInterface super interface in any way!!!", "5.6", errorBuffer);

			}

			int sbbLocalInterfaceClassModifiers = sbbLocalInterfaceClass.getModifiers();
			if (this.component.isSlee11() && sbbLocalInterfaceClass.getPackage() == null) {
				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "SbbLocalInterface: " + sbbLocalInterfaceClass.getName()
						+ " is not defined in package", "6.5", errorBuffer);
			}

			if (!Modifier.isPublic(sbbLocalInterfaceClassModifiers)) {
				passed = false;
				errorBuffer = appendToBuffer(this.component.getAbstractSbbClass() + "SbbLocalInterface: " + sbbLocalInterfaceClass.getName()
						+ " must be public!", "5.6", errorBuffer);
			}

			Set<String> ignore = new HashSet<String>();
			ignore.add(SbbLocalObjectExt.class.getName());
			ignore.add(SbbLocalObject.class.getName());
			ignore.add(Object.class.getName());
			Map<String, Method> interfaceMethods = ClassUtils.getAllInterfacesMethods(sbbLocalInterfaceClass, ignore);

			// here we have all defined methods in interface, we have to checkif
			// their names do not start with sbb/ejb and if they are contained
			// in
			// collections with concrete methods from sbb
			//System.err.println(sbbAbstractClassConcreteMethods.keySet());

			for (Method methodToCheck : interfaceMethods.values()) {
				if (methodToCheck.getName().startsWith("ejb") || methodToCheck.getName().startsWith("sbb")) {
					passed = false;
					errorBuffer = appendToBuffer("Method from SbbLocalInterface: " + sbbLocalInterfaceClass.getName() + " starts with wrong prefix: "
							+ methodToCheck.getName(), "5.6", errorBuffer);

				}

				Method methodFromSbbClass = ClassUtils
						.getMethodFromMap(
								methodToCheck.getName(),
								methodToCheck.getParameterTypes(),
								sbbAbstractClassConcreteMethods,
								sbbAbstractClassConcreteFromSuperClasses,
								ClassUtils
										.getAbstractMethodsFromSuperClasses(this.component
												.getAbstractSbbClass()),
								ClassUtils
										.getAbstractMethodsFromClass(this.component
												.getAbstractSbbClass()));

				if (methodFromSbbClass == null) {

					passed = false;
					errorBuffer = appendToBuffer("Method from SbbLocalInterface: " + sbbLocalInterfaceClass.getName() + " with name:  "
							+ methodToCheck.getName() + " is not implemented by sbb class or its super classes!", "5.6", errorBuffer);

					// we fails fast here
					continue;
				}

				// XXX: Note this does not check throws clause, only name and
				// signature
				// this side
				// FIXME: Note that we dont check modifier, is this corerct

				if (!methodFromSbbClass.getName().equals(methodToCheck.getName())
						|| !methodFromSbbClass.getReturnType().equals(methodToCheck.getReturnType())
						|| !Arrays.equals(methodFromSbbClass.getParameterTypes(), methodToCheck.getParameterTypes())
						|| !Arrays.equals((Object[]) methodFromSbbClass.getExceptionTypes(), (Object[]) methodToCheck.getExceptionTypes())) {

					passed = false;
					errorBuffer = appendToBuffer("Method from SbbLocalInterface: " + sbbLocalInterfaceClass.getName() + " with name:  "
							+ methodToCheck.getName()
							+ " is not implemented by sbb class or its super classes. Its visibility, throws clause or modifiers are different!",
							"5.6", errorBuffer);

					// we fails fast here
					continue;

				}

			}

			// FIXME: is this ok, is it needed ? If not checked here, abstract
			// methods check will make it fail later, but not concrete ?
			// now lets check javax.slee.SbbLocalObject methods - sbb cant have
			// those implemented or defined as abstract.
		} finally {
			if (!passed) {
				logger.error(errorBuffer);				
			}
		}
		return passed;
	}

	boolean validateCmpFileds(Map<String, Method> sbbAbstractClassMethods, Map<String, Method> sbbAbstractMethodsFromSuperClasses) {

		boolean passed = true;
		String errorBuffer = new String("");

		for (CMPFieldDescriptor cmpFieldDescriptor : this.component.getDescriptor().getSbbAbstractClass().getCmpFields()) {

			MSbbCMPField entry = (MSbbCMPField) cmpFieldDescriptor;
			String fieldName = entry.getCmpFieldName();

			Character c = fieldName.charAt(0);
			// we must start with lower case letter
			if (!Character.isLetter(c) || !Character.isLowerCase(c)) {
				passed = false;
				errorBuffer = appendToBuffer("Failed to validate CMP field name. Name must start with lower case letter: " + fieldName, "6.5.1",
						errorBuffer);

				// In this case we should fail fast?
				continue;
			}

			// lets find method in abstracts, it cannot be implemented
			// FIXME: do we have to check concrete as well?
			String methodPartFieldName = Character.toUpperCase(c) + fieldName.substring(1);
			String getterName = "get" + methodPartFieldName;
			String setterName = "set" + methodPartFieldName;
			Method getterFieldMethod = null;
			Method setterFieldMethod = null;

			// Both have to be present, lets do trick, first we can get getter
			// so we know field type and can get setter

			getterFieldMethod = ClassUtils.getMethodFromMap(getterName, new Class[0], sbbAbstractClassMethods, sbbAbstractMethodsFromSuperClasses);

			if (getterFieldMethod == null) {
				errorBuffer = appendToBuffer("Failed to validate CMP field. Could not find getter method: " + getterName
						+ ". Both accessors must be present.", "6.5.1", errorBuffer);

				passed = false;
				// we fail fast here
				continue;
			}

			Class fieldType = getterFieldMethod.getReturnType();

			setterFieldMethod = ClassUtils.getMethodFromMap(setterName, new Class[] { fieldType }, sbbAbstractClassMethods,
					sbbAbstractMethodsFromSuperClasses);

			if (setterFieldMethod == null) {
				errorBuffer = appendToBuffer("Failed to validate CMP field. Could not find setter method: " + setterName
						+ " with single parameter of type: " + getterFieldMethod.getReturnType()
						+ ". Both accessors must be present and have the same type.", "6.5.1", errorBuffer);

				passed = false;
				// we fail fast here
				continue;
			}

			// not needed
			// if (setterFieldMethod.getReturnType().getName().compareTo("void")
			// != 0) {
			// errorBuffer = appendToBuffer(
			// "Failed to validate CMP field. Setter method: "
			// + setterName + " has return type of: "
			// + setterFieldMethod.getReturnType(), "6.5.1",
			// errorBuffer);
			// }

			// we know methods are here, we must check if they are - abstract,
			// public, what about static and native?
			int modifiers = getterFieldMethod.getModifiers();

			if (!Modifier.isPublic(modifiers) || !Modifier.isAbstract(modifiers)) {
				errorBuffer = appendToBuffer("Failed to validate CMP field. Getter method is either public or not abstract: " + getterName, "6.5.1",
						errorBuffer);

				passed = false;
			}

			modifiers = setterFieldMethod.getModifiers();

			if (!Modifier.isPublic(modifiers) || !Modifier.isAbstract(modifiers)) {
				errorBuffer = appendToBuffer("Failed to validate CMP field. Setter method is neither public nor abstract: " + getterName, "6.5.1",
						errorBuffer);

				passed = false;
			}

			// 1.1 and 1.0 allow
			// primitives and serializables and if reference is present sbbLo or
			// derived type
			boolean referenceIsPresent = entry.getSbbAliasRef() != null;
			boolean isSbbLOFieldType = false;
			if (_PRIMITIVES.contains(fieldType.getName())) {
				// do nothing, this does not include wrapper classes,
				isSbbLOFieldType = false;
			} else if (ClassUtils.checkInterfaces(fieldType, "javax.slee.SbbLocalObject") != null) {
				// FIXME: is there a better way?
				// in 1.0 sbb ref MUST be present always
				// again, if referenced sbb has wrong type of SbbLO defined here
				// it mail fail, however it a matter of validating other
				// component
				isSbbLOFieldType = true;

				/*
				 * emmartins: page 70 of slee 1.1 specs say that sbb-alias-ref is now optional (it doesn't say it is just for slee 1.1 components)
				 * 
				if (!this.component.isSlee11() && !referenceIsPresent) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Failed to validate CMP field. In JSLEE 1.0 Sbb reference element must be present when CMP type is Sbb Local Object or derived, field name: "
									+ fieldName + " type: " + fieldType, "6.5.1", errorBuffer);

					// for this we fail fast, nothing more to do.
					continue;

				}
				 */
				
				// now its a check for 1.1 and 1.0
				if (referenceIsPresent) {
					
					SbbID referencedSbb = null;
					SbbComponent referencedComponent = null;
					if (entry.getSbbAliasRef().equals(this.component.getDescriptor().getSbbAlias())) {
						referencedSbb = this.component.getSbbID();
						referencedComponent = this.component;
					}
					else {
						for (SbbRefDescriptor mSbbRef : this.component.getDescriptor().getSbbRefs()) {
							if (mSbbRef.getSbbAlias().equals(entry.getSbbAliasRef())) {
								referencedSbb =  mSbbRef.getComponentID();
								break;
							}
						}
						
						if (referencedSbb == null) {
							passed = false;
							errorBuffer = appendToBuffer(
									"Failed to validate CMP field. Field references sbb with alias "+entry.getSbbAliasRef()+" but no sbb ref has been found with which such alias. Field: " + fieldName,
									"6.5.1", errorBuffer);
							continue;
						}
						else {
							referencedComponent = this.repository.getComponentByID(referencedSbb);
							if (referencedComponent == null) {
								//FIXME: throw or fail?
								throw new SLEEException("Referenced (in cmp field) "+referencedSbb+" was not found in component repository, this should not happen since dependencies were already verified");
							}
						}
					}
										
					// FIXME: field type must be equal to defined or must be
					// javax.slee.SbbLocalObject = what about intermediate types
					// X -> Y -> SbbLocalObject - and we have Y?
					if (referencedComponent.getDescriptor().getSbbLocalInterface()!=null && fieldType.getName().compareTo(referencedComponent.getDescriptor().getSbbLocalInterface().getSbbLocalInterfaceName()) == 0) {
						// its ok
					} else if (fieldType.getName().compareTo("javax.slee.SbbLocalObject") == 0) {
						// its ok?
					} else if (ClassUtils.checkInterfaces(referencedComponent.getSbbLocalInterfaceClass(), fieldType.getName()) != null) {
						// its ok
					} else {
						passed = false;
						errorBuffer = appendToBuffer(
								"Failed to validate CMP field. Field type for sbb entities must be of generic type javax.slee.SbbLocalObject or type declared by referenced sbb, field name: "
										+ fieldName + " type: " + fieldType, "6.5.1", errorBuffer);
					}

				}
				/*
				 * emmartins: page 70 of slee 1.1 specs say that sbb-alias-ref is now optional
				 * 
				else {
					// here only 1.1 will go
					if (fieldType.getName().compareTo("javax.slee.SbbLocalObject") == 0) {
						// its ok?
					} else {
						passed = false;
						errorBuffer = appendToBuffer(
								"Failed to validate CMP field. Field type for sbb entities must be of generic type javax.slee.SbbLocalObject when no reference to sbb is present, field name: "
										+ fieldName + " type: " + fieldType, "6.5.1", errorBuffer);
					}
				}
				*/
				// FIXME: end of checks here?

			} else if (this.component.isSlee11()) {
				isSbbLOFieldType = false;

				if (fieldType.getName().compareTo("javax.slee.EventContext") == 0) {
					// we do nothing, its ok.
				} else if (ClassUtils.checkInterfaces(fieldType, "javax.slee.profile.ProfileLocalObject") != null) {
					// FIXME: there is no ref maybe we shoudl check referenced
					// profiles?
				} else if (ClassUtils.checkInterfaces(fieldType, "java.io.Serializable") != null) {

					// do nothing, its check same as below
				} else if (ClassUtils.checkInterfaces(fieldType, "javax.slee.ActivityContextInterface") != null) {

					// we can haev generic ACI or derived object defined in
					// sbb,... uffff
					Class definedAciType = this.component.getActivityContextInterface();
					if (definedAciType != null && definedAciType.getName().equals(fieldType.getName())) {
						// do nothing
					} else if (fieldType.getName().compareTo("javax.slee.ActivityContextInterface") == 0) {
						// do nothing
					} else if (definedAciType != null && ClassUtils.checkInterfaces(definedAciType, fieldType.getName()) != null) {
						// do anything
					} else {
						passed = false;
						errorBuffer = appendToBuffer(
								"Failed to validate CMP field. Field type for ACIs must be of generic type javax.slee.ActivityContextInterface or defined by sbb Custom ACI, field name: "
										+ fieldName + " type: " + fieldType, "6.5.1", errorBuffer);
					}

				} else {
					// FAIL
					passed = false;
					errorBuffer = appendToBuffer(
							"Failed to validate CMP field. Field type must be: primitive,serializable, SbbLocalObject or derived,(1.1): EventContext, ActivityContextInterface or derived, field name: "
									+ fieldName + " type: " + fieldType, "6.5.1", errorBuffer);
				}

			} else if (ClassUtils.checkInterfaces(fieldType, "java.io.Serializable") != null) {
				// This is tricky, someone can implement serializable in SbbLO
				// derived objec, however it could not be valid SBB LO(for
				// isntance wrong Sbb,not extending SbbLO) but if this was first
				// it would pass test without checks on constraints

				// this includes all serializables and primitive wrapper classes
				isSbbLOFieldType = false;
			} else {
				// FAIL

			}

			if (referenceIsPresent && !isSbbLOFieldType) {
				// this is not permited?
				passed = false;
				errorBuffer = appendToBuffer(
						"Failed to validate CMP field. Sbb reefrence is present when field type is not Sbb Local Object or derived, field name: "
								+ fieldName + " type: " + fieldType, "6.5.1", errorBuffer);

			}
			// Check throws clause

			if (getterFieldMethod.getExceptionTypes().length > 0) {
				passed = false;
				errorBuffer = appendToBuffer("Failed to validate CMP field. Getter method declared throws clause: "
						+ Arrays.toString(getterFieldMethod.getExceptionTypes()), "6.5.1", errorBuffer);
			}

			if (setterFieldMethod.getExceptionTypes().length > 0) {
				passed = false;
				errorBuffer = appendToBuffer("Failed to validate CMP field. Setter method declared throws clause: "
						+ Arrays.toString(setterFieldMethod.getExceptionTypes()), "6.5.1", errorBuffer);
			}

			// else remove those from list
			sbbAbstractClassMethods.remove(ClassUtils.getMethodKey(setterFieldMethod));
			sbbAbstractClassMethods.remove(ClassUtils.getMethodKey(getterFieldMethod));
			sbbAbstractMethodsFromSuperClasses.remove(ClassUtils.getMethodKey(setterFieldMethod));
			sbbAbstractMethodsFromSuperClasses.remove(ClassUtils.getMethodKey(getterFieldMethod));

		}

		if (!passed) {
			logger.error(errorBuffer);	
		}

		return passed;

	}

	boolean validateEventHandlers(Map<String, Method> sbbAbstractClassMethods, Map<String, Method> sbbAbstractMethodsFromSuperClasses,
			Map<String, Method> concreteMethods, Map<String, Method> concreteMethodsFromSuperClasses) {

		boolean passed = true;
		// String errorBuffer = new String("");
		// Abstract methods are for fire methods, we have to check them and
		// remove if present :)

		Map<EventTypeID,EventEntryDescriptor> events = this.component.getDescriptor().getEventEntries();

		for (EventEntryDescriptor event : events.values()) {

			switch (event.getEventDirection()) {

			case Fire:
				if (!validateFireEvent(event, sbbAbstractClassMethods, sbbAbstractMethodsFromSuperClasses)) {
					passed = false;
				}
				break;

			case Receive:
				if (!validateReceiveEvent(event, concreteMethods, concreteMethodsFromSuperClasses)) {
					passed = false;
				}
				break;

			case FireAndReceive:
				if (!validateFireEvent(event, sbbAbstractClassMethods, sbbAbstractMethodsFromSuperClasses)) {
					passed = false;
				}
				if (!validateReceiveEvent(event, concreteMethods, concreteMethodsFromSuperClasses)) {
					passed = false;
				}
				break;

			}

		}

		return passed;
	}

	boolean validateReceiveEvent(EventEntryDescriptor event, Map<String, Method> concreteMethods, Map<String, Method> concreteMethodsFromSuperClasses) {
		boolean passed = true;
		String errorBuffer = new String("");

		try {
			// we can have only one receive method

			EventTypeComponent eventTypeComponent = this.repository.getComponentByID(event.getEventReference());

			if (eventTypeComponent == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Failed to validate event receive method. reference points to event comopnent that has not been validated, event name: "
								+ event.getEventName(), "8.5.2", errorBuffer);
				return passed;
			}

			Class eventClass = eventTypeComponent.getEventTypeClass();
			String methodName = "on" + event.getEventName();

			// lets look for basic event handler
			// the thing with each event handler is that aci can be generic or
			// sbb
			// custom....

			Method receiveMethod = null;
			Method receiveMethodCustomACI = null;
			boolean receiverWithContextPresent = false;
			boolean receiverWithoutContextPresent = false;
			if (this.component.isSlee11()) {

				receiveMethod = ClassUtils.getMethodFromMap(methodName, new Class[] { eventClass, javax.slee.ActivityContextInterface.class,
						javax.slee.EventContext.class }, concreteMethods, concreteMethodsFromSuperClasses);
				if (this.component.getActivityContextInterface() != null)

					receiveMethodCustomACI = ClassUtils.getMethodFromMap(methodName, new Class[] { eventClass,
							this.component.getActivityContextInterface(), javax.slee.EventContext.class }, concreteMethods,
							concreteMethodsFromSuperClasses);

				// is there any clever way to lookup those?

				// ok, here only one method can be present
				if (receiveMethod != null && receiveMethodCustomACI != null) {
					// we cant have both
					passed = false;
					errorBuffer = appendToBuffer(
							"Failed to validate event receive method. Sbb can not define event receive method with generic and custom aci, event name: "
									+ event.getEventName(), "8.5.2", errorBuffer);
				}

				// now here we are sure we have one or none
				if (receiveMethod != null) {
					receiverWithContextPresent = true;

					if (!validateReceiveMethodSignature(receiveMethod, "8.5.2")) {
						passed = false;
					}
					receiveMethod = null;
				} else if (receiveMethodCustomACI != null) {
					receiverWithContextPresent = true;
					if (!validateReceiveMethodSignature(receiveMethodCustomACI, "8.5.2")) {
						passed = false;
					}

					receiveMethodCustomACI = null;
				}

			}

			// this is for all

			receiveMethod = ClassUtils.getMethodFromMap(methodName, new Class[] { eventClass, javax.slee.ActivityContextInterface.class },
					concreteMethods, concreteMethodsFromSuperClasses);

			if (this.component.getActivityContextInterface() != null)
				receiveMethodCustomACI = ClassUtils.getMethodFromMap(methodName, new Class[] { eventClass,
						this.component.getActivityContextInterface() }, concreteMethods, concreteMethodsFromSuperClasses);
			// is there any clever way to lookup those?

			// ok, here only one method can be present
			if (receiveMethod != null && receiveMethodCustomACI != null) {
				// we cant have both
				passed = false;
				errorBuffer = appendToBuffer(
						"Failed to validate event receive method. Sbb can not define event receive method with generic and custom aci, event name: "
								+ event.getEventName(), "8.5.2", errorBuffer);
			}

			// now here we are sure we have one or none
			if (receiveMethod != null) {
				receiverWithoutContextPresent = true;

				if (!validateReceiveMethodSignature(receiveMethod, "8.5.2"))
					passed = false;
			} else if (receiveMethodCustomACI != null) {
				receiverWithoutContextPresent = true;
				if (!validateReceiveMethodSignature(receiveMethodCustomACI, "8.5.2"))
					passed = false;
			}

			// here we have to check, only one can be present
			if (receiverWithoutContextPresent && receiverWithContextPresent) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Failed to validate event receive method. Sbb can not define event receive method with and without EventContext, only one can be present: "
								+ event.getEventName(), "8.5.2", errorBuffer);
			}

			if (!receiverWithoutContextPresent && !receiverWithContextPresent) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Failed to validate event receive method. Sbb must define handler method when direction is \"XReceive\". Event receiver has different name or wrong parameters, event: "
								+ event.getEventName(), "8.5.2", errorBuffer);
			}
			// now its time for initial event selector
			if (event.isInitialEvent() && event.getInitialEventSelectorMethod() != null
					&& !validateInitialEventSelector(event, concreteMethods, concreteMethodsFromSuperClasses)) {
				// FIXME: we dont check if variable or selector method is
				// present
				passed = false;

			}
		} finally {
			if (!passed) {
				logger.error(errorBuffer);
			}
		}
		return passed;
	}

	boolean validateInitialEventSelector(EventEntryDescriptor event, Map<String, Method> concreteMethods, Map<String, Method> concreteMethodsFromSuperClasses) {
		boolean passed = true;
		String errorBuffer = new String("");
		try {

			if (event.getInitialEventSelectorMethod().startsWith("ejb") || event.getInitialEventSelectorMethod().startsWith("sbb")) {
				passed = false;
				errorBuffer = appendToBuffer("Initial event seelctor method can not start with \"ejb\" or \"sbb\", method name: "
						+ event.getInitialEventSelectorMethod(), "8.6.4", errorBuffer);
			}

			Method m = ClassUtils.getMethodFromMap(event.getInitialEventSelectorMethod(), new Class[] { javax.slee.InitialEventSelector.class },
					concreteMethods, concreteMethodsFromSuperClasses);
			if (m == null) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				passed = false;
				errorBuffer = appendToBuffer("Failed to find initial event selector method, method name: " + event.getInitialEventSelectorMethod(),
						"8.6.4", errorBuffer);
				return passed;
			}

			// FIXME: It has to be concrete...
			int modifiers = m.getModifiers();
			if (Modifier.isAbstract(modifiers)) {
				passed = false;
				errorBuffer = appendToBuffer("Failed to validate initial event selector method" + " Receive method is abstract, method name: "
						+ m.getName(), "8.6.4", errorBuffer);
			}

			if (!Modifier.isPublic(modifiers)) {
				passed = false;
				errorBuffer = appendToBuffer("Failed to validate initial event selector method" + " Receive method is not public, method name: "
						+ m.getName(), "8.6.4", errorBuffer);
			}

			if (Modifier.isStatic(modifiers)) {
				passed = false;
				errorBuffer = appendToBuffer("Failed to validate initial event selector method" + " Receive method is static, method name: "
						+ m.getName(), "8.6.4", errorBuffer);
			}

			if (Modifier.isFinal(modifiers)) {
				passed = false;
				errorBuffer = appendToBuffer("Failed to validate initial event selector method" + " Receive method is final, method name: "
						+ m.getName(), "8.6.4", errorBuffer);
			}
			// FIXME: native?

			if (m.getExceptionTypes().length > 0) {
				passed = false;
				errorBuffer = appendToBuffer("Failed to validate initial event selector method" + " Method has throws clause, method: " + m.getName(), "8.6.4",
						errorBuffer);

			}

			if (m.getReturnType().getName().compareTo("javax.slee.InitialEventSelector") != 0) {

				passed = false;
				errorBuffer = appendToBuffer("Failed to validate initial event selector method"
						+ " Return type must be javax.slee.InitialEventSelector, method: " + m.getName(), "8.6.4", errorBuffer);

			}
		} finally {
			if (!passed) {
				logger.error(errorBuffer);		
			}
		}
		return passed;
	}

	boolean validateReceiveMethodSignature(Method m, String section) {
		boolean passed = true;
		String errorBuffer = new String("");

		int modifiers = m.getModifiers();
		if (Modifier.isAbstract(modifiers)) {
			passed = false;
			errorBuffer = appendToBuffer("Failed to validate receive event method" + " Receive method is abstract, method name: " + m.getName(),
					section, errorBuffer);
		}

		if (!Modifier.isPublic(modifiers)) {
			passed = false;
			errorBuffer = appendToBuffer("Failed to validate receive event method" + " Receive method is not public, method name: " + m.getName(),
					section, errorBuffer);
		}

		if (Modifier.isStatic(modifiers)) {
			passed = false;
			errorBuffer = appendToBuffer("Failed to validate receive event method" + " Receive method is static, method name: " + m.getName(),
					section, errorBuffer);
		}

		if (Modifier.isFinal(modifiers)) {
			passed = false;
			errorBuffer = appendToBuffer("Failed to validate receive event method" + " Receive method is final, method name: " + m.getName(),
					section, errorBuffer);
		}
		// FIXME: native?

		// FIXME: only runtime exceptions?
		if (m.getExceptionTypes().length > 0) {
			passed = false;
			errorBuffer = appendToBuffer("Failed to validate receive event method" + " Fire method is has throws clause, method: " + m.getName(),
					section, errorBuffer);

		}

		if (m.getReturnType().getName().compareTo("void") != 0) {

			passed = false;
			errorBuffer = appendToBuffer("Failed to validate Receive event method" + " Receive method cant have return type, method: " + m.getName(),
					section, errorBuffer);

		}

		if (!passed) {
			logger.error(errorBuffer);		
		}

		return passed;
	}

	boolean validateFireEvent(EventEntryDescriptor event, Map<String, Method> sbbAbstractClassMethods, Map<String, Method> sbbAbstractMethodsFromSuperClasses) {

		boolean passed = true;
		String errorBuffer = new String("");

		try {
			// we can have only one receive method

			EventTypeComponent eventTypeComponent = this.repository.getComponentByID(event.getEventReference());

			if (eventTypeComponent == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Failed to validate event receive method. reference points to event comopnent that has not been validated, event name: "
								+ event.getEventName(), "8.5.2", errorBuffer);
				return passed;
			}

			Class eventClass = eventTypeComponent.getEventTypeClass();

			String methodName = "fire" + event.getEventName();

			// we we have to validate abstract methods :}
			// this is 1.0
			Method fireMethod = null;
			// this is 1.1
			Method fire11Method = null;

			// FIXME: is it ok to refer directly to:
			// javax.slee.ActivityContextInterface.class ??

			fireMethod = ClassUtils.getMethodFromMap(methodName, new Class[] { eventClass, javax.slee.ActivityContextInterface.class,
					javax.slee.Address.class }, sbbAbstractClassMethods, sbbAbstractMethodsFromSuperClasses);

			fire11Method = ClassUtils.getMethodFromMap(methodName, new Class[] { eventClass, javax.slee.ActivityContextInterface.class,
					javax.slee.Address.class, javax.slee.ServiceID.class }, sbbAbstractClassMethods, sbbAbstractMethodsFromSuperClasses);

			if (!this.component.isSlee11()) {
				if (fireMethod == null) {
					// fail fast
					passed = false;
					errorBuffer = appendToBuffer(
							"Failed to validate fire vent method, JSLEE 1.0 sbbs have to have method with signature: public abstract void fire<event name>(<event class> event,ActivityContextInterface activity,Address address);. Event name: "
									+ event.getEventName(), "8.5.1", errorBuffer);
					// the end
					return passed;
				}
			} else {
				if (fireMethod == null && fire11Method == null) {
					// fail fast
					passed = false;
					errorBuffer = appendToBuffer(
							"Failed to validate fire vent method, JSLEE 1.1 sbbs have to have one of those methods. Sbb class either does not declare them, method has wrong parameters/name or is concrete. Event name: "
									+ event.getEventName(), "8.5.1", errorBuffer);
					// the end
					return passed;
				}
			}

			if (this.component.isSlee11() && fire11Method != null && !validateFireMethodSignature(fire11Method, "8.5.1")) {
				passed = false;
			}

			// if we are here we are either 1.0 in which case this method is not
			// null, or we are 1.1 in which case it could be
			if (fireMethod != null && !validateFireMethodSignature(fireMethod, "8.5.1")) {
				passed = false;
			}

			if (fire11Method != null) {
				sbbAbstractClassMethods.remove(ClassUtils.getMethodKey(fire11Method));
				sbbAbstractMethodsFromSuperClasses.remove(ClassUtils.getMethodKey(fire11Method));
			}
			if (fireMethod != null) {
				sbbAbstractClassMethods.remove(ClassUtils.getMethodKey(fireMethod));
				sbbAbstractMethodsFromSuperClasses.remove(ClassUtils.getMethodKey(fireMethod));
			}

		} finally {
			if (!passed) {
				logger.error(errorBuffer);
			}
		}

		return passed;

	}

	boolean validateFireMethodSignature(Method m, String section) {

		boolean passed = true;
		String errorBuffer = new String("");

		int modifiers = m.getModifiers();
		if (!Modifier.isAbstract(modifiers)) {
			passed = false;
			errorBuffer = appendToBuffer("Failed to validate fire event method" + " Fire method is not abstract, method name: " + m.getName(),
					section, errorBuffer);
		}

		if (!Modifier.isPublic(modifiers)) {
			passed = false;
			errorBuffer = appendToBuffer("Failed to validate fire event method" + " Fire method is not public, method name: " + m.getName(), section,
					errorBuffer);
		}

		if (Modifier.isStatic(modifiers)) {
			passed = false;
			errorBuffer = appendToBuffer("Failed to validate fire event method" + " Fire method is static, method name: " + m.getName(), section,
					errorBuffer);
		}

		// FIXME: native?
		if (m.getExceptionTypes().length > 0) {
			passed = false;
			errorBuffer = appendToBuffer("Failed to validate fire event method" + " Fire method has throws clause, method: " + m.getName(), section,
					errorBuffer);

		}

		if (m.getReturnType().getName().compareTo("void") != 0) {

			passed = false;
			errorBuffer = appendToBuffer("Failed to validate fire event method" + " Fire method cant have return type, method: " + m.getName(),
					section, errorBuffer);

		}

		if (!passed) {
			logger.error(errorBuffer);		
		}

		return passed;

	}

	boolean validateSbbUsageParameterInterface(Map<String, Method> sbbAbstractClassMethods, Map<String, Method> sbbAbstractMethodsFromSuperClasses) {
		if (this.component.getUsageParametersInterface() == null) {
			return true;
		} else {
			return UsageInterfaceValidator.validateSbbUsageParameterInterface(this.component, sbbAbstractClassMethods,
					sbbAbstractMethodsFromSuperClasses);
		}
	}

	boolean validateGetProfileCmpInterfaceMethods(Map<String, Method> sbbAbstractClassMethods, Map<String, Method> sbbAbstractMethodsFromSuperClasses) {

		// Section 6.7
		boolean passed = true;
		String errorBuffer = new String("");

		try {
			Map<String,GetProfileCMPMethodDescriptor> profileCmpMethods = this.component.getDescriptor().getSbbAbstractClass()
					.getProfileCMPMethods();
			if (profileCmpMethods.size() == 0) {
				return passed;
			}

			// eh, else we have to do all checks
			for (GetProfileCMPMethodDescriptor getProfileCMPMethodDescriptor : profileCmpMethods.values()) {

				MGetProfileCMPMethod method = (MGetProfileCMPMethod) getProfileCMPMethodDescriptor;

				if (method.getProfileCmpMethodName().startsWith("ejb") || method.getProfileCmpMethodName().startsWith("sbb")) {
					passed = false;

					errorBuffer = appendToBuffer(
							"Wrong method prefix, get profile cmp interface method must not start with \"sbb\" or \"ejb\", method: "
									+ method.getProfileCmpMethodName(), "6,7", errorBuffer);

				}

				Method m = ClassUtils.getMethodFromMap(method.getProfileCmpMethodName(), new Class[] { ProfileID.class }, sbbAbstractClassMethods,
						sbbAbstractMethodsFromSuperClasses);
				;

				if (m == null) {
					passed = false;

					errorBuffer = appendToBuffer(
							"Failed to find method in sbb abstract class - it either does not exist,is private or has wrong parameter, method: "
									+ method.getProfileCmpMethodName(), "6,7", errorBuffer);

					continue;
				}

				sbbAbstractClassMethods.values().remove(m);
				sbbAbstractMethodsFromSuperClasses.values().remove(m);
				int modifiers = m.getModifiers();

				if (!Modifier.isPublic(modifiers)) {
					passed = false;
					errorBuffer = appendToBuffer("Get profile CMP interface method must be public, method: " + method.getProfileCmpMethodName(),
							"6,7", errorBuffer);
					continue;
				}

				if (!Modifier.isAbstract(modifiers)) {
					passed = false;
					errorBuffer = appendToBuffer("Get profile CMP interface method must be abstract, method: " + method.getProfileCmpMethodName(),
							"6,7", errorBuffer);
					continue;
				}

				Class methodReturnType = m.getReturnType();

				// this is referential integrity
				Map<String, ProfileSpecificationID> map = new HashMap<String, ProfileSpecificationID>();

				for (ProfileSpecRefDescriptor rf : this.component.getDescriptor().getProfileSpecRefs()) {
					map.put(rf.getProfileSpecAlias(), rf.getComponentID());
				}

				ProfileSpecificationID profileID = map.get(method.getProfileSpecAliasRef());
				ProfileSpecificationComponent profileComponent = this.repository.getComponentByID(profileID);

				if (profileComponent == null) {
					// this means referential integrity is nto met, possibly
					// class is not loaded
					passed = false;
					errorBuffer = appendToBuffer("Get profile CMP interface method references profile which has not been validated, method: "
							+ method.getProfileCmpMethodName() + " , reference: " + method.getProfileSpecAliasRef(), "6,7", errorBuffer);
					continue;
				}

				Class profileDefinedCMPInterface = profileComponent.getProfileCmpInterfaceClass();

				if (methodReturnType.getName().compareTo(profileDefinedCMPInterface.getName()) != 0
						&& ClassUtils.checkInterfaces(profileDefinedCMPInterface, methodReturnType.getName()) == null) {

					passed = false;
					errorBuffer = appendToBuffer(
							"Get profile CMP interface method has wrong return type - it has to be defined CMP interface or its super class, method: "
									+ method.getProfileCmpMethodName(), "6,7", errorBuffer);

				}

				Class[] exceptions = m.getExceptionTypes();
				// UnrecognizedProfileTableNameException,
				// UnrecognizedProfileNameException
				if (exceptions.length != 2) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Get profile CMP interface method has wrong number of exceptions, it can throw only two - UnrecognizedProfileNameException and UnrecognizedProfileTableNameException, method: "
									+ method.getProfileCmpMethodName(), "6,7", errorBuffer);
				}

				HashSet<String> possibleExcpetions = new HashSet<String>();

				possibleExcpetions.add(UnrecognizedProfileTableNameException.class.getName());
				possibleExcpetions.add(UnrecognizedProfileNameException.class.getName());

				for (Class c : exceptions) {
					if (possibleExcpetions.contains(c.getName())) {
						possibleExcpetions.remove(c.getName());
					}
				}

				if (!possibleExcpetions.isEmpty()) {
					passed = false;
					errorBuffer = appendToBuffer("Get profile CMP interface method has decalration of throws clause, it lacks following exceptions: "
							+ Arrays.toString(possibleExcpetions.toArray()) + " , method: " + method.getProfileCmpMethodName(), "6,7", errorBuffer);
				}

			}

		} finally {
			if (!passed) {
				logger.error(errorBuffer);
			}
		}

		return passed;

	}

	boolean validateEnvEntries() {

		boolean passed = true;
		String errorBuffer = new String("");

		try {

			List<EnvEntryDescriptor> envEntries = this.component.getDescriptor().getEnvEntries();
			for (EnvEntryDescriptor e : envEntries) {
				if (!_ENV_ENTRIES_TYPES.contains(e.getEnvEntryType())) {

					passed = false;
					errorBuffer = appendToBuffer("Env entry has wrong type: " + e.getEnvEntryType() + " , method: " + e.getEnvEntryName(), "6.13",
							errorBuffer);
				}

			}

		} finally {
			if (!passed) {
				logger.error(errorBuffer);
			}
		}

		return passed;
	}

	boolean validateDescriptor() {
		boolean passed = true;
		String errorBuffer = new String("");

		try {

			Map<String, ProfileSpecRefDescriptor> declaredProfileReferences = new HashMap<String, ProfileSpecRefDescriptor>();
			Map<String, SbbRefDescriptor> declaredSbbreferences = new HashMap<String, SbbRefDescriptor>();

			SbbDescriptor descriptor = this.component.getDescriptor();

			for (ProfileSpecRefDescriptor ref : descriptor.getProfileSpecRefs()) {
				// if(ref.getProfileSpecAlias()==null ||
				// ref.getProfileSpecAlias().compareTo("")==0)
				if (ref.getProfileSpecAlias() != null && ref.getProfileSpecAlias().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares profile spec reference without alias, id: " + ref.getComponentID(),
							"3.1.8", errorBuffer);

				} else if (ref.getProfileSpecAlias() != null && declaredProfileReferences.containsKey(ref.getProfileSpecAlias())) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Sbb descriptor declares profile spec reference more than once, alias: " + ref.getProfileSpecAlias(), "3.1.8",
							errorBuffer);

				} else if (ref.getProfileSpecAlias() == null && declaredProfileReferences.containsKey(ref.getComponentID().toString())) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Sbb descriptor declares profile spec reference more than once, alias is null, ID " + ref.getComponentID(), "3.1.8",
							errorBuffer);

				} else {
					logger.info("No errors!");
					String alias = ref.getProfileSpecAlias();
					if (alias == null) {
						alias = ref.getComponentID().toString();
						logger.debug("Temporary Alias is: "+alias);
					}
					declaredProfileReferences.put(alias, ref);
				}
			}

			for (SbbRefDescriptor ref : descriptor.getSbbRefs()) {

				if (ref.getSbbAlias() == null || ref.getSbbAlias().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares sbb reference without alias, id: " + ref.getComponentID(), "3.1.8",
							errorBuffer);

				} else if (declaredSbbreferences.containsKey(ref.getSbbAlias())) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares sbb reference more than once, alias: " + ref.getSbbAlias(), "3.1.8",
							errorBuffer);

				} else {
					declaredSbbreferences.put(ref.getSbbAlias(), ref);
				}
			}

			Set<String> childRelationMethods = new HashSet<String>();

			for (GetChildRelationMethodDescriptor childMethodInterface : descriptor.getGetChildRelationMethodsMap().values()) {
				MGetChildRelationMethod childMethod = (MGetChildRelationMethod) childMethodInterface;
				if (childMethod.getChildRelationMethodName() == null || childMethod.getChildRelationMethodName().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Sbb descriptor declares child relation method without name, alias: " + childMethod.getSbbAliasRef(), "3.1.8",
							errorBuffer);
				} else if (childMethod.getSbbAliasRef() == null || childMethod.getSbbAliasRef().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares child relation method without sbb alias, name: "
							+ childMethod.getChildRelationMethodName(), "3.1.8", errorBuffer);
				} else if (!declaredSbbreferences.containsKey(childMethod.getSbbAliasRef())) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares child relation method with sbb alias that has not been declared, name: "
							+ childMethod.getChildRelationMethodName() + ", method alias: " + childMethod.getSbbAliasRef(), "3.1.8", errorBuffer);
				} else if (childRelationMethods.contains(childMethod.getChildRelationMethodName())) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares child relation method more than once, name: "
							+ childMethod.getChildRelationMethodName(), "3.1.8", errorBuffer);
				} else {
					childRelationMethods.add(childMethod.getChildRelationMethodName());
				}

			}

			Map<String, MSbbCMPField> declaredCmps = new HashMap<String, MSbbCMPField>();
			for (CMPFieldDescriptor cmpFieldDescriptor : descriptor.getSbbAbstractClass().getCmpFields()) {
				MSbbCMPField cmp = (MSbbCMPField) cmpFieldDescriptor;
				if (cmp.getCmpFieldName() == null || cmp.getCmpFieldName().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor cmp field with empty name.", "3.1.8", errorBuffer);
				} else if (cmp.getSbbAliasRef() != null && cmp.getSbbAliasRef().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares cmp field with empty sbb alias, name: " + cmp.getCmpFieldName(), "3.1.8",
							errorBuffer);
				} else if (declaredCmps.containsKey(cmp.getCmpFieldName())) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares cmp field more than once, name: " + cmp.getCmpFieldName(), "3.1.8",
							errorBuffer);
				} else {
					declaredCmps.put(cmp.getCmpFieldName(), cmp);
				}
			}

			// This is required, events can be decalred once
			Map<String, EventTypeID> eventNameToReference = new HashMap<String, EventTypeID>();
			for (EventEntryDescriptor event : descriptor.getEventEntries().values()) {
				if (event.getEventName() == null || event.getEventName().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares event with empty event name, ", "3.1.8", errorBuffer);
				} else if (eventNameToReference.containsKey(event.getEventName())) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares event with the same event name more than once, name: "
							+ event.getEventName(), "3.1.8", errorBuffer);
				} else if (eventNameToReference.containsValue(event.getEventReference())) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares  event reference twice, events can be references only once, name: "
							+ event.getEventName(), "3.1.8", errorBuffer);
				} else {
					eventNameToReference.put(event.getEventName(), event.getEventReference());
				}

			}

			// FIXME: ra part?

			if (descriptor.getSbbActivityContextInterface() != null) {
				if (descriptor.getSbbActivityContextInterface() == null
						|| descriptor.getSbbActivityContextInterface().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares sbb aci which is empty.", "3.1.8", errorBuffer);
				}
			}

			if (descriptor.getSbbLocalInterface() != null) {
				if (descriptor.getSbbLocalInterface().getSbbLocalInterfaceName() == null
						|| descriptor.getSbbLocalInterface().getSbbLocalInterfaceName().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares sbb local interface which is empty.", "3.1.8", errorBuffer);
				}
			}

			if (descriptor.getSbbUsageParametersInterface() != null) {
				if (descriptor.getSbbUsageParametersInterface().getUsageParametersInterfaceName() == null
						|| descriptor.getSbbUsageParametersInterface().getUsageParametersInterfaceName().compareTo("") == 0) {
					passed = false;
					errorBuffer = appendToBuffer("Sbb descriptor declares sbb usage interface which is empty.", "3.1.8", errorBuffer);
				}
			}

		} finally {
			if (!passed) {
				logger.error(errorBuffer);
			}
		}

		return passed;
	}

	/**
	 * See section 1.3 of jslee 1.1 specs
	 * 
	 * @return
	 */
	boolean validateCompatibilityReferenceConstraints() {

		boolean passed = true;
		String errorBuffer = new String("");

		try {
			if (!this.component.isSlee11()) {
				// A 1.0 SBB must not reference or use a 1.1 Profile
				// Specification. This must be enforced by a 1.1
				// JAIN SLEE.

				for (ProfileSpecRefDescriptor profileReference : this.component.getDescriptor().getProfileSpecRefs()) {
					ProfileSpecificationComponent specComponent = this.repository.getComponentByID(profileReference.getComponentID());
					if (specComponent == null) {
						// should not happen
						passed = false;
						errorBuffer = appendToBuffer("Referenced "+profileReference.getComponentID()+" was not found in component repository, this should not happen since dependencies were already verified","1.3", errorBuffer);
						
					} else {
						if (specComponent.isSlee11()) {
							passed = false;
							errorBuffer = appendToBuffer("Sbb is following 1.0 JSLEE contract, it must not reference 1.1 profile specification: " + profileReference.getComponentID(), "1.3", errorBuffer);
						}
					}
				}

			}
		} finally {
			if (!passed) {
				if (logger.isEnabledFor(Level.ERROR)) {
					logger.error(errorBuffer);
				}
			}

		}

		return passed;
	}
	
	
	
	protected String appendToBuffer(String message, String section, String buffer) {
		buffer += (this.component.getDescriptor().getSbbID() + " : violates section " + section + " of jSLEE 1.1 specification : " + message + "\n");
		return buffer;
	}

}
