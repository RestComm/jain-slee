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

package org.mobicents.slee.container.deployment;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author Ivelin Ivanov
 * @author Stefano Gamma
 */
public class ClassUtils {
    
    private static final String PUBLIC_IDENTIFIER = "Public";
    private static Logger logger = Logger.getLogger(ClassUtils.class);

    public static final String IS_PREFIX = "is";
    public static final String GET_PREFIX = "get";
    public static final String SET_PREFIX = "set";
    
    /**
     * Returns the set of names representing valid, full access (read&write) JavaBean properties declared in the given class.
     * NOTE: the implementation is not as strict as in Introspector (@see java.beans.Introspector#getTargetPropertyInfo()). It probably should be.
     * 
     * @param CtClass the class metadata
     * @return Set of names representing valid, full access (read&write) JavaBean properties declared in the given class
     * @throws NotFoundException if a class type could not be found
     * 
     */
    public static Set getCMPFields(CtClass clazz) {

        Set properties = new HashSet();

	    // Apply some reflection to the current class.
        
	    // First get an array of all the public methods at this level
	    Set methods = getPublicAbstractMethods(clazz);
	    
	    // remove non-public methods

	    Iterator iter = methods.iterator();

	    // the key in the getter/setter tables will be method name
	    HashMap getters = new HashMap();
	    HashMap setters = new HashMap();
	    
	    // split out getters and setters; ignore the rest
	    while (iter.hasNext()) {
	        CtMethod method = (CtMethod)iter.next();
	        String mname = method.getName();
	        
	        // skip static methods
	        if (Modifier.isStatic(method.getModifiers())) continue;
	        
	        String property = "";
	        try {
		        // skip methods, which throw exceptions
		        if (method.getExceptionTypes().length > 0) continue;

		        if (mname.startsWith(GET_PREFIX)) { 
		            property = mname.substring(GET_PREFIX.length());
		            getters.put(property, method); 
		        } else if (mname.startsWith(IS_PREFIX) && method.getReturnType().equals(CtClass.booleanType)) { 
		            property = mname.substring(IS_PREFIX.length());
		            getters.put(property, method);  
		        } else if (mname.startsWith(SET_PREFIX)) { 
		            property = mname.substring(SET_PREFIX.length());
		            setters.put(property, method);  
		        }
	        } catch (NotFoundException e) {
	            logger.warn(e);
	        }
            if (property.length() == 0 || !Character.isUpperCase(property.charAt(0)) || property.equals(PUBLIC_IDENTIFIER)) {
                logger.warn("Method " + mname + " has a non-capitalized first character of the JavaBean property.");
                getters.remove(property);
                setters.remove(property);
            }
		}

	    // iterate over getters and find matching setters
	    iter = getters.entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry entry = (Map.Entry)iter.next();
	        String property = (String)entry.getKey();
	        CtMethod getter = (CtMethod)entry.getValue();
	        // find matching setter
	        CtMethod setter = (CtMethod)setters.get(property);
	        // if setter is null, the property is not full access (read&write), therefore ignored 
	        try {
		        if (setter != null) {
		            CtClass[] sparams = setter.getParameterTypes();
		            if (sparams.length == 1 && sparams[0].equals(getter.getReturnType())) {
		                properties.add(property);
		            }
		        }
	        } catch (NotFoundException e) {
	            logger.warn(e);
	        }
		}
	    return properties;
    }

    /**
     * Return the String allowing one to create an Object (Integer, Boolean,
     * Byte,... ) from a primitive type given in parameter.
     * 
     * @param argumentType -
     *            the primitive type as a String (int, boolean, byte, ...)
     * @param argument -
     *            the argument to transform
     * @return the String allowing one to create an Object from a primitive type
     */
    public static String getObjectFromPrimitiveType(String argumentType,
    		String argument) {
    	if (argumentType.equals("int"))
    		return "new Integer(" + argument + ")";
    	if (argumentType.equals("boolean"))
    		return "new Boolean(" + argument + ")";
    	if (argumentType.equals("byte"))
    		return "new Byte(" + argument + ")";
    	if (argumentType.equals("char"))
    		return "new Character(" + argument + ")";
    	if (argumentType.equals("double"))
    		return "new Double(" + argument + ")";
    	if (argumentType.equals("float"))
    		return "new Float(" + argument + ")";
    	if (argumentType.equals("long"))
    		return "new Long(" + argument + ")";
    	if (argumentType.equals("short"))
    		return "new Short(" + argument + ")";
    	return null;
    }

    /**
     * Return the String allowing one to create an Object (Integer, Boolean,
     * Byte,... ) from a primitive type given in parameter.
     * 
     * @param argumentType -
     *            the primitive type as a String (int, boolean, byte, ...)
     * @param argument -
     *            the argument to transform
     * @return the String allowing one to create an Object from a primitive type
     */
    public static String getClassFromPrimitiveType(String argumentType) {
    	if (argumentType.equals("int"))
    		return "java.lang.Integer";
    	if (argumentType.equals("boolean"))
    		return "java.lang.Boolean";
    	if (argumentType.equals("byte"))
    		return "java.lang.Byte";
    	if (argumentType.equals("char"))
    		return "java.lang.Character";
    	if (argumentType.equals("double"))
    		return "java.lang.Double";
    	if (argumentType.equals("float"))
    		return "java.lang.Float";
    	if (argumentType.equals("long"))
    		return "java.lang.Long";
    	if (argumentType.equals("short"))
    		return "java.lang.Short";
    	return null;
    }

    /**
     * 
     * @param argumentType
     * @return
     */
    public static String getPrimitiveTypeFromClass(String argumentType) {
    	if (argumentType.equals("java.lang.Integer"))
    		return "int";
    	if (argumentType.equals("java.lang.Boolean"))
    		return "boolean";
    	if (argumentType.equals("java.lang.Byte"))
    		return "byte";
    	if (argumentType.equals("java.lang.Character"))
    		return "char";
    	if (argumentType.equals("java.lang.Double"))
    		return "double";
    	if (argumentType.equals("java.lang.Float"))
    		return "float";
    	if (argumentType.equals("java.lang.Long"))
    		return "long";
    	if (argumentType.equals("java.lang.Short"))
    		return "short";
    	return argumentType;
    }

    /**
     * Return the String allowing one to create a primitive type (Integer,
     * Boolean, Byte,... ) from a primitive type given in parameter.
     * 
     * @param argumentType
     * @param argument
     * @return
     */
    public static String getPrimitiveTypeFromObject(String argumentType,
    		String argument) {
    	if (argumentType.equals("int"))
    		return "((Integer)" + argument + ").intValue()";
    	if (argumentType.equals("boolean"))
    		return "((Boolean)" + argument + ").booleanValue()";
    	if (argumentType.equals("byte"))
    		return "((Byte)" + argument + ").byteValue()";
    	if (argumentType.equals("char"))
    		return "((Character)" + argument + ").charValue()";
    	if (argumentType.equals("double"))
    		return "((Double)" + argument + ").doubleValue()";
    	if (argumentType.equals("float"))
    		return "((Float)" + argument + ").floatValue()";
    	if (argumentType.equals("long"))
    		return "((Long)" + argument + ").longValue()";
    	if (argumentType.equals("short"))
    		return "((Short)" + argument + ").shortValue()";
    	return null;
    }

    /**
     * Retrieve all abstract methods from a class
     * 
     * @param sbbAbstractClass
     *            the class to extract the abstract methods
     * @return all abstract methods from a class
     */
    public static Map getAbstractMethodsFromClass(CtClass sbbAbstractClass) {
    	HashMap abstractMethods = new HashMap();
    	CtMethod[] methods = sbbAbstractClass.getDeclaredMethods();
    	for (int i = 0; i < methods.length; i++) {
    		if (Modifier.isAbstract(methods[i].getModifiers())) {
    			abstractMethods.put(methods[i].getName(), methods[i]);
    		}
    	}
    	
    	return abstractMethods;
    }

    /**
     * Retrieve all concrete (non-abstract and non-native) methods from a class
     * Stored under key Name+signature
     * @param sbbClass
     *            the class to extract the concrete methods from
     * @return all concrete methods from a class
     */
    public static Map getConcreteMethodsFromClass(CtClass sbbClass) {
    	HashMap concreteMethods = new HashMap();
    	CtMethod[] methods = sbbClass.getDeclaredMethods();
    	for (int i = 0; i < methods.length; i++) {
    	    int mods = methods[i].getModifiers();
    		if (!Modifier.isAbstract(mods) && !Modifier.isNative(mods)) {
    			concreteMethods.put(getMethodKey(methods[i]), methods[i]);
    		}
    	}
    	return concreteMethods;
    }

    /**
     * Retrieve all concrete (non-abstract and non-native) methods names from a class
     * 
     * @param sbbClass
     *            the class to extract the concrete methods from
     * @return all concrete methods from a class
     */
    public static Set getConcreteMethodsNamesFromClass(CtClass sbbClass) {
    	Set concreteMethods = new HashSet();
    	CtMethod[] methods = sbbClass.getDeclaredMethods();
    	for (int i = 0; i < methods.length; i++) {
    	    int mods = methods[i].getModifiers();
    		if (!Modifier.isAbstract(mods) && !Modifier.isNative(mods)) {
    			concreteMethods.add(methods[i].getName());
    		}
    	}
    	return concreteMethods;
    }
    
    public static Map getSuperClassesConcreteMethodsFromClass(CtClass sbbAbstractClass) {
    	HashMap concrete = new HashMap();
    	CtMethod[] methods = null;
    	CtClass superClass;
    	try {
    		superClass = sbbAbstractClass.getSuperclass();
    
    		while (superClass.getName().compareTo("java.lang.Object") != 0) {
    			methods = superClass.getDeclaredMethods();
    			for (int i = 0; i < methods.length; i++) {
    				if (!Modifier.isAbstract(methods[i].getModifiers())) {
    					concrete.put(methods[i].getName(), methods[i]);
    				}
    			}
    			superClass = superClass.getSuperclass();
    		}
    	} catch (NotFoundException e) {
    		String s = "Method not found ! Huh!!";
    		ConcreteClassGeneratorUtils.logger.fatal(s,e);
    		throw new RuntimeException ("s",e);
    	}
    	return concrete;
    }
    
    public static Map getSuperClassesAbstractMethodsFromClass(CtClass sbbAbstractClass) {
    	HashMap abstractMethods = new HashMap();
    	CtMethod[] methods = null;
    	CtClass superClass;
    	try {
    		superClass = sbbAbstractClass.getSuperclass();
    
    		while (superClass.getName().compareTo("java.lang.Object") != 0) {
    			methods = superClass.getDeclaredMethods();
    			for (int i = 0; i < methods.length; i++) {
    				if (Modifier.isAbstract(methods[i].getModifiers())) {
    					abstractMethods.put(methods[i].getName(), methods[i]);
    				}
    			}
    			superClass = superClass.getSuperclass();
    		}
    	} catch (NotFoundException e) {
    		String s = "Method not found ! Huh!!";
    		ConcreteClassGeneratorUtils.logger.fatal(s,e);
    		throw new RuntimeException ("s",e);
    	}
    	return abstractMethods;
    }

    /**
     * Collects all methods of the interfaces implemented/extended by the specified {@link CtClass}
     * @param sbbAbstractClass
     * @return
     */
    public static Map getSuperClassesAbstractMethodsFromInterface(CtClass sbbAbstractClass) {
    	HashMap abstractMethods = new HashMap();
    	CtMethod[] methods = null;

    	ArrayList<CtClass> superClasses = new ArrayList<CtClass>();
    	superClasses.add(sbbAbstractClass);
    	ArrayList<CtClass> superClassesProcessed = new ArrayList<CtClass>();
    	
    	try {
    		while(!superClasses.isEmpty()) {
    			// remove head
    			CtClass ctClass = superClasses.remove(0);
    			superClassesProcessed.add(ctClass);
    			// get its methods
    			methods = ctClass.getDeclaredMethods();
    			for (CtMethod ctMethod:methods)	{
    				abstractMethods.put(getMethodKey(ctMethod), ctMethod);
    			}
    			// get super interfaces
    			for(CtClass anotherCtClass : ctClass.getInterfaces()) {
    				if (!superClassesProcessed.contains(anotherCtClass)) {
    					superClasses.add(anotherCtClass);
    				}
    			}
    			try {
    				ctClass.detach();
    			}
    			catch (Exception e) {
    				// ignore
    			}
    		}
    	} catch (NotFoundException e) {
    		logger.error(e);
    	}
    	return abstractMethods;
    }

    /**
     * Retrieve all methods from an interface, including super interfaces
     * 
     * @param sbbAbstractClass
     *            the class to extract the abstract methods
     * @return all interface methods from an interface
     */
    public static Map getInterfaceMethodsFromInterface(CtClass interfaceClass) {
    	return getInterfaceMethodsFromInterface(interfaceClass, Collections.emptyMap());
    }

    /**
     * Retrieve all methods from an interface, including super interfaces, except the ones specified in the provided map 
     * @param interfaceClass
     * @param exceptMethods
     * @return
     */
    public static Map getInterfaceMethodsFromInterface(CtClass interfaceClass,
            Map exceptMethods) {
    
        HashMap interfaceMethods = new HashMap();
        CtMethod[] methods = interfaceClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            if (exceptMethods.get(methods[i].getName()) == null)
            {
                    ConcreteClassGeneratorUtils.logger.trace(methods[i].getName());
                    interfaceMethods.put(getMethodKey(methods[i]), methods[i]);
            }
        }
    
        Map temp = getSuperClassesAbstractMethodsFromInterface(interfaceClass);
        for (Iterator i= temp.keySet().iterator(); i.hasNext(); )
        {
            String key = (String)i.next();
            if (!exceptMethods.containsKey(key)) {
                    interfaceMethods.put(key, temp.get(key));
            }
        }
    
        return interfaceMethods;
    }

    /**
     * @param CtClass the class metadata
     * @return Set - all public abstract methods declared in the given class and all its superclasses
     */
    public static Set getPublicAbstractMethods(CtClass abstractClass) {
    	Set methodsSet = new HashSet();
    	// CtClass.getMethods returns all methods, including the ones declared in super classes
    	CtMethod[] methods = abstractClass.getMethods();
    	for(int i=0; i<methods.length; i++){
    	    if (Modifier.isPublic(methods[i].getModifiers()) &&
		            Modifier.isAbstract(methods[i].getModifiers())) 
    	        methodsSet.add(methods[i]);
    	}
    	return methodsSet;
    }
    
    /**
     * @param CtClass the class metadata
     * @return Set - all public methods declared in the given class and all its superclasses
     */
    public static Set getPublicMethods(CtClass abstractClass) {
    	Set methodsSet = new HashSet();
    	// CtClass.getMethods returns all methods, including the ones declared in super classes
    	CtMethod[] methods = abstractClass.getMethods();
    	for(int i=0; i<methods.length; i++){
    	    if (Modifier.isPublic(methods[i].getModifiers())) 
    	        methodsSet.add(methods[i]);
    	}
    	return methodsSet;
    }
    
    /**
     * 
     * Checks if the child method is correctly overriding the parent method.
     * A valid override has identical argument types and order, 
     * identical return type, and is not less accessible than the original method. 
     * The overriding method must not throw any checked exceptions that were not 
     * declared for the original method.
     * 
     * @param CtMethod the parent method
     * @param CtMethod the child method
     * @return true if the child method is a valid override of the parent method
     */
    public static boolean isValidOverride(CtMethod parent, CtMethod child) {
        if (!parent.getName().equals(child.getName())) return false;
        
        try {
            if (!parent.getReturnType().equals(child.getReturnType())) return false;
            
            // compare method arguments
            CtClass[] parentArgs = parent.getParameterTypes();
            CtClass[] childArgs = child.getParameterTypes();
            if (parentArgs.length != childArgs.length) return false;
            for (int i = 0; i < parentArgs.length; i++) {
                if (!parentArgs[i].equals(childArgs[i])) return false;
            }
        
	        // this is not a precise check. Protected access for parent and Default for child will fail it.
	        if (!(Modifier.isPublic(parent.getModifiers()) && Modifier.isPublic(child.getModifiers()))) return false;
	        if (!Modifier.isPrivate(parent.getModifiers()) && Modifier.isPrivate(child.getModifiers())) return false;
	
	        // verify exceptions
	        CtClass[] parentEx = parent.getExceptionTypes();
	        CtClass[] childEx = child.getExceptionTypes();
            if (childEx.length > 0) { 
		        for (int i = 0; i < parentEx.length; i++) {
		            boolean validEx = false;
		            for (int j = 0; j < childEx.length; j++) {
		                if ((childEx[j]).subtypeOf(parentEx[i])) validEx = true;
		            }
		            if (!validEx) return false;
		        }
            }        
        } catch (NotFoundException e) {
            logger.warn("Failed: isValidOverride", e);
            return false;
        }
        
        return true;
    }
    
    /**
     * Create a unique key for store the CtMethod object in an Map object.
     * @param CtMethod to store in the Map
     * @return a String key unique for the method. 
     */
    public static String getMethodKey(CtMethod method) {
    	return method.getName() + method.getSignature();
    }
}
