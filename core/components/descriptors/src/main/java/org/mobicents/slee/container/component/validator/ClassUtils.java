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

import java.lang.reflect.Modifier;
import java.util.HashMap;
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
	 * Retrieve all concrete (non-abstract and non-native) methods from a class
	 * Stored under key Name+signature for instance: name =
	 * "sbbExceptionThrown", signature =
	 * "(Ljava/lang/Exception;Ljava/lang/Object;Ljavax/slee/ActivityContextInterface;)"
	 * 
	 * @param sbbClass
	 *            the class to extract the concrete methods from
	 * @return all concrete methods from a class
	 */
	public static Map<String, CtMethod> getConcreteMethodsFromClass(
			CtClass xClass) {
		HashMap<String, CtMethod> concreteMethods = new HashMap<String, CtMethod>();
		CtMethod[] methods = xClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			int mods = methods[i].getModifiers();
			if (!Modifier.isAbstract(mods) && !Modifier.isNative(mods)) {
				concreteMethods.put(getMethodKey(methods[i]), methods[i]);
			}
		}
		return concreteMethods;
	}

	/**
	 * Retrieve all concrete (non-abstract and non-native) methods from a class
	 * Stored under key Name+signature for instance: name =
	 * "sbbExceptionThrown", signature =
	 * "(Ljava/lang/Exception;Ljava/lang/Object;Ljavax/slee/ActivityContextInterface;)"
	 * 
	 * @param sbbClass
	 *            the class to extract the concrete methods from
	 * @return all concrete methods from a class
	 */
	public static Map<String, CtMethod> getSuperClassesConcreteMethodsFromClass(
			CtClass xClass) {
		HashMap<String, CtMethod> concreteMethods = new HashMap<String, CtMethod>();

		// We could; use CtClass.getMethods() which:
		// Returns an array containing CtMethod objects representing all the
		// non-private methods of the class. That array includes non-private
		// methods inherited from the superclasses. But we dont want
		// java.lang.Object
		CtMethod[] methods = null;
		CtClass superClass;
		try {
			superClass = xClass.getSuperclass();

			while (superClass.getName().compareTo("java.lang.Object") != 0) {
				methods = superClass.getDeclaredMethods();
				for (int i = 0; i < methods.length; i++) {
					if (Modifier.isAbstract(methods[i].getModifiers())) {
						concreteMethods.put(getMethodKey(methods[i]),
								methods[i]);
					}
				}
				superClass = superClass.getSuperclass();
			}
		} catch (NotFoundException e) {
			String s = "Method not found ! Huh!!";
			// ConcreteClassGeneratorUtils.logger.fatal(s,e);
			throw new RuntimeException("s", e);
		}
		return concreteMethods;
	}

	/**
	 * Create a unique key for store the CtMethod object in an Map object.
	 * 
	 * @param CtMethod
	 *            to store in the Map
	 * @return a String key unique for the method.
	 */
	public static String getMethodKey(CtMethod method) {
		String ret = method.getName() + method.getSignature();
		return ret;
	}

	public static Map<String, CtMethod> getAbstractMethodsFromClass(
			CtClass ctAbstractSbbClass) {
		HashMap<String, CtMethod> abstractMethods = new HashMap<String, CtMethod>();
		CtMethod[] methods = ctAbstractSbbClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (Modifier.isAbstract(methods[i].getModifiers())) {
				abstractMethods.put(getMethodKey(methods[i]), methods[i]);
			}
		}

		return abstractMethods;
	}

	public static Map<String, CtMethod> getAbstractSuperClassesMethodsFromClass(
			CtClass ctAbstractSbbClass) {
		HashMap<String, CtMethod> abstractMethods = new HashMap<String, CtMethod>();
		CtMethod[] methods = null;
		CtClass superClass;
		try {
			superClass = ctAbstractSbbClass.getSuperclass();

			while (superClass.getName().compareTo("java.lang.Object") != 0) {
				methods = superClass.getDeclaredMethods();
				for (int i = 0; i < methods.length; i++) {
					if (Modifier.isAbstract(methods[i].getModifiers())) {
						abstractMethods.put(getMethodKey(methods[i]),
								methods[i]);
					}
				}
				superClass = superClass.getSuperclass();
			}
		} catch (NotFoundException e) {
			String s = "Method not found ! Huh!!";
			throw new RuntimeException("s", e);
		}
		return abstractMethods;
	}

	/**
	 * Returns methods of this interface and all super interfaces
	 * 
	 * @param ctInterfaceClass
	 * @return
	 */
	public static Map<String, CtMethod> getAllInterfacesMethods(
			CtClass ctInterfaceClass, Set<String> ignore) {
		HashMap<String, CtMethod> abstractMethods = new HashMap<String, CtMethod>();
		CtMethod[] methods = null;
		CtClass[] superInterfaces;
		try {
			superInterfaces = ctInterfaceClass.getInterfaces();

			for (CtClass superInterface : superInterfaces) {
				if (!ignore.contains(superInterface.getName()))
					abstractMethods.putAll(getAllInterfacesMethods(
							superInterface, ignore));
			}

			methods = ctInterfaceClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				abstractMethods.put(getMethodKey(methods[i]), methods[i]);
			}

		} catch (NotFoundException e) {
			String s = "Method not found ! Huh!!";
			throw new RuntimeException("s", e);
		}
		return abstractMethods;
	}

	/**
	 * Check if among the interfaces one is the class searched
	 * 
	 * 
	 * @param interfaceSearched
	 *            the interface to look for return true if among the interfaces
	 *            one is the class searched
	 */
	public static CtClass checkInterfaces(
			CtClass classOrInterfaceWithInterfaces, String interfaceSearched) {

		CtClass returnValue = null;
		try {

			// we do check only on get interfaces
			for (CtClass iface : classOrInterfaceWithInterfaces.getInterfaces()) {
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
				CtClass superClass = classOrInterfaceWithInterfaces
						.getSuperclass();
				if (superClass != null) {
					returnValue = checkInterfaces(superClass, interfaceSearched);
				}
			}

		} catch (NotFoundException nfe) {
			nfe.printStackTrace();
		}
		return returnValue;

	}

}
