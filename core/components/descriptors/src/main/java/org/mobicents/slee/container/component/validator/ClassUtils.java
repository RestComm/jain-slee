/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-5-21                             *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.component.validator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.apache.log4j.Logger;

/**
 * @author baranowb
 */
public class ClassUtils {

	private static final String PUBLIC_IDENTIFIER = "Public";
	private static Logger logger = Logger.getLogger(ClassUtils.class);

	public static final String IS_PREFIX = "is";
	public static final String GET_PREFIX = "get";
	public static final String SET_PREFIX = "set";

	/**
	 * Searches for provided interface in passed Class object - it can be class
	 * or interface. If it finds, it return instance of it.
	 * 
	 * @param classOrInterfaceWithInterfaces
	 * @param interfaceSearched
	 * @return
	 */
	public static Class checkInterfaces(Class classOrInterfaceWithInterfaces,
			String interfaceSearched) {
		Class returnValue = null;
		
		
		if(classOrInterfaceWithInterfaces.getName().compareTo(interfaceSearched) == 0) {
			return classOrInterfaceWithInterfaces;

		}
		// we do check only on get interfaces
		for (Class iface : classOrInterfaceWithInterfaces.getInterfaces()) {
			if (iface.getName().compareTo(interfaceSearched) == 0) {
				returnValue = iface;

			} else {
				returnValue = checkInterfaces(iface, interfaceSearched);
			}

			if (returnValue != null)
				break;
		}

		if (!classOrInterfaceWithInterfaces.isInterface()
				&& returnValue == null) {
			Class superClass = classOrInterfaceWithInterfaces.getSuperclass();
			if (superClass != null) {
				returnValue = checkInterfaces(superClass, interfaceSearched);
			}
		}

		return returnValue;
	}

	public static String getMethodKey(Method method) {
		String ret = method.getName()
				+ Arrays.toString(method.getParameterTypes())+method.getReturnType();
		return ret;
	}

	public static Map<String, Method> getConcreteMethodsFromClass(Class xClass) {
		HashMap<String, Method> concreteMethods = new HashMap<String, Method>();
		Method[] methods = xClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			int mods = methods[i].getModifiers();
			if (!Modifier.isAbstract(mods) && !Modifier.isNative(mods)) {
				concreteMethods.put(getMethodKey(methods[i]), methods[i]);
			}
		}
		return concreteMethods;
	}

	public static Map<String, Method> getSuperClassesConcreteMethodsFromClass(
			Class xClass) {
		HashMap<String, Method> concreteMethods = new HashMap<String, Method>();

		// We could; use CtClass.getMethods() which:
		// Returns an array containing CtMethod objects representing all the
		// non-private methods of the class. That array includes non-private
		// methods inherited from the superclasses. But we dont want
		// java.lang.Object
		Method[] methods = null;
		Class superClass;

		superClass = xClass.getSuperclass();

		while (superClass.getName().compareTo("java.lang.Object") != 0) {
			methods = superClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				if (Modifier.isAbstract(methods[i].getModifiers())) {
					concreteMethods.put(getMethodKey(methods[i]), methods[i]);
				}
			}
			superClass = superClass.getSuperclass();
		}

		return concreteMethods;
	}

	/**
	 * Returns methods of this interface and all super interfaces
	 * 
	 * @param ctInterfaceClass
	 * @return
	 */
	public static Map<String, Method> getAllInterfacesMethods(
			Class xInterfaceClass, Set<String> ignore) {
		HashMap<String, Method> abstractMethods = new HashMap<String, Method>();
		Method[] methods = null;
		Class[] superInterfaces;

		superInterfaces = xInterfaceClass.getInterfaces();

		for (Class superInterface : superInterfaces) {
			if (!ignore.contains(superInterface.getName()))
				abstractMethods.putAll(getAllInterfacesMethods(superInterface,
						ignore));
		}

		methods = xInterfaceClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			abstractMethods.put(getMethodKey(methods[i]), methods[i]);
		}

		return abstractMethods;
	}

	public static Map<String, Method> getAbstractMethodsFromClass(Class xClass) {
		HashMap<String, Method> abstractMethods = new HashMap<String, Method>();
		Method[] methods = xClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (Modifier.isAbstract(methods[i].getModifiers())) {
				abstractMethods.put(getMethodKey(methods[i]), methods[i]);
			}
		}

		return abstractMethods;
	}

	public static Map<String, Method> getAbstractSuperClassesMethodsFromClass(
			Class xClass) {
		HashMap<String, Method> abstractMethods = new HashMap<String, Method>();
		Method[] methods = null;
		Class superClass;

		superClass = xClass.getSuperclass();

		while (superClass.getName().compareTo("java.lang.Object") != 0) {
			methods = superClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				if (Modifier.isAbstract(methods[i].getModifiers())) {
					abstractMethods.put(getMethodKey(methods[i]), methods[i]);
				}
			}
			superClass = superClass.getSuperclass();
		}

		return abstractMethods;
	}

}
