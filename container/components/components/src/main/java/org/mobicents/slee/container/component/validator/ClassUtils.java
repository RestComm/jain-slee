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

/***************************************************
 *                                                 *
 *  Restcomm: The Open Source VoIP Platform      *
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
	public static Class checkInterfaces(Class classOrInterfaceWithInterfaces, String interfaceSearched) {
		Class returnValue = null;

		if (classOrInterfaceWithInterfaces.getName().compareTo(interfaceSearched) == 0) {
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

		if (!classOrInterfaceWithInterfaces.isInterface() && returnValue == null) {
			Class superClass = classOrInterfaceWithInterfaces.getSuperclass();
			if (superClass != null) {
				returnValue = checkInterfaces(superClass, interfaceSearched);
			}
		}

		return returnValue;
	}

	public static String getMethodKey(Method method) {
		return method.getName() + Arrays.toString(method.getParameterTypes());
		// //System.err.println("KEY: "+ret);
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

	public static Map<String, Method> getConcreteMethodsFromSuperClasses(Class xClass) {
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
				if (!Modifier.isAbstract(methods[i].getModifiers()) && !Modifier.isNative(methods[i].getModifiers())) {
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
	public static Map<String, Method> getAllInterfacesMethods(Class xInterfaceClass, Set<String> ignore) {
		HashMap<String, Method> abstractMethods = new HashMap<String, Method>();
		Method[] methods = null;
		Class[] superInterfaces;

		superInterfaces = xInterfaceClass.getInterfaces();

		for (Class superInterface : superInterfaces) {
			if (!ignore.contains(superInterface.getName()))
				abstractMethods.putAll(getAllInterfacesMethods(superInterface, ignore));
		}

		methods = xInterfaceClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			abstractMethods.put(getMethodKey(methods[i]), methods[i]);
		}

		return abstractMethods;
	}

	public static Map<String, Method> getAbstractMethodsFromClass(Class xClass) {
		HashMap<String, Method> abstractMethods = new HashMap<String, Method>();

		// This includes methods only decalred, we also methods that are nto
		// implemented, but abstract since they come from interfaces.... ech ;[
		Method[] methods = xClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (Modifier.isAbstract(methods[i].getModifiers())) {
				abstractMethods.put(getMethodKey(methods[i]), methods[i]);
			}
		}

		// now here comes methods from interfaces
		Set<String> ignore = new HashSet<String>();
		ignore.add("java.lang.Object");
		Map<String, Method> interfaceMethods = getAllInterfacesMethods(xClass, ignore);
		for (Method im : interfaceMethods.values()) {
			try {
				Method m = xClass.getMethod(im.getName(), im.getParameterTypes());
				if (Modifier.isAbstract(m.getModifiers())) {
					abstractMethods.put(getMethodKey(m), m);
				}
			} catch (SecurityException e) {

			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// in any case this shoudl be present there
			}
		}

		// //System.err.println("RET : "+abstractMethods);
		return abstractMethods;
	}

	public static Map<String, Method> getAbstractMethodsFromSuperClasses(Class xClass) {
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

			Set<String> ignore = new HashSet<String>();
			ignore.add("java.lang.Object");
			Map<String, Method> interfaceMethods = getAllInterfacesMethods(superClass, ignore);
			for (Method im : interfaceMethods.values()) {
				try {
					// yes, xClass
					Method m = xClass.getMethod(im.getName(), im.getParameterTypes());
					if (Modifier.isAbstract(m.getModifiers())) {
						abstractMethods.put(getMethodKey(m), m);
					}
				} catch (SecurityException e) {

				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					// in any case this shoudl be present there
				}
			}

			superClass = superClass.getSuperclass();
		}

		// //System.err.println("RET SUPER: "+abstractMethods);
		return abstractMethods;
	}

	// public static Method getMethodFromMap(String name, String[]
	// parameterNames,Map<String,Method>... methods)
	// {
	// String key=name+Arrays.toString(parameterNames);
	// for(Map<String,Method> m:methods)
	// if(m.containsKey(key))
	// {
	// return m.get(key);
	// }else
	// {
	//			
	// }
	//		
	// return null;
	//		
	// }

	public static Method getMethodFromMap(String name, Class[] parameters, Map<String, Method>... methods) {
		String key = name + Arrays.toString(parameters);
		for (Map<String, Method> m : methods) {
			if (m.containsKey(key)) {
				return m.get(key);
			} else {

			}
		}
		return null;
	}

	public static Class checkClasses(Class xClass, String classToSearch) {

		if (xClass.getName().compareTo(classToSearch) == 0) {
			return xClass;

		}
		Class superClass;

		superClass = xClass.getSuperclass();

		while (superClass.getName().compareTo("java.lang.Object") != 0) {

			if (xClass.getName().compareTo(classToSearch) == 0) {
				return xClass;

			}

			superClass = superClass.getSuperclass();
		}

		return null;
	}

}
