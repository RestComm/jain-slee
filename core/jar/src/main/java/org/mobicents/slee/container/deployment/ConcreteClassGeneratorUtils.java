/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ConcreteClassGeneratorUtils.java
 * 
 * Created on Aug 19, 2004
 *
 */
package org.mobicents.slee.container.deployment;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import javax.slee.SLEEException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainerUtils;

/**
 * Utility class to generate concrete classes
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 * @author <a href="mailto:michele.laporta@gmail.com">Michele La Porta</a>
 */
public class ConcreteClassGeneratorUtils {
	public static Logger logger = null;

	public static final String SBB_CONCRETE_CLASS_NAME_PREFIX = "";

	public static final String SBB_CONCRETE_CLASS_NAME_SUFFIX = "Impl";

	public static final String CONCRETE_ACTIVITY_INTERFACE_CLASS_NAME_PREFIX = "";

	public static final String CONCRETE_ACTIVITY_INTERFACE_CLASS_NAME_SUFFIX = "Impl";

	public static final String SBB_LOCAL_OBJECT_CLASS_NAME_PREFIX = "";

	public static final String SBB_LOCAL_OBJECT_CLASS_NAME_SUFFIX = "Impl";

	public static final String PROFILE_CONCRETE_CLASS_NAME_PREFIX = "";

	public static final String PROFILE_CONCRETE_CLASS_NAME_SUFFIX = "Impl";

	public static final String PROFILE_MBEAN_CONCRETE_CLASS_NAME_PREFIX = "";

	public static final String PROFILE_MBEAN_CONCRETE_CLASS_NAME_SUFFIX = "MBeanImpl";

	public static final String PROFILE_TRANSIENT_CLASS_NAME_PREFIX = "";

	public static final String PROFILE_TRANSIENT_CLASS_NAME_SUFFIX = "TransientState";

	public static final String SBB_USAGE_PARAMETERS_INTERFACE_PREFIX = "";

	public static final String SBB_USAGE_PARAMETERS_INTERFACE_SUFFIX = "Impl";
	
	public static final String PROFILE_LOCAL_CLASS_NAME_PREFIX = "";

	public static final String PROFILE_LOCAL_CLASS_NAME_SUFFIX = "LocalImpl";
	
	public static final String PROFILE_TABLE_CLASS_NAME_PREFIX = "";

	public static final String PROFILE_TABLE_CLASS_NAME_SUFFIX = "TableImpl";
	
	// The root class pool
	
	

	static {
		logger = Logger.getLogger(ConcreteClassGeneratorUtils.class);
		
	}

	
    /**
	 * Recursively walk a directory tree and return a List of all jars Files found;
	 * the List is sorted using File.compareTo.
	 * 
	 * @param aStartingDir
	 *            is a valid directory, which can be read.
	 */
	static public List getJarsFileListing(File aStartingDir)
			throws FileNotFoundException {
		validateDirectory(aStartingDir);
		List result = new ArrayList();

		File[] filesAndDirs = aStartingDir.listFiles(new FileFilter() {
		
			public boolean accept(File pathname) {
				if(pathname.getName().endsWith(".jar"))
					return true;
				return false;
			}
		
		});
		List filesDirs = Arrays.asList(filesAndDirs);
		Iterator filesIter = filesDirs.iterator();
		File file = null;
		while (filesIter.hasNext()) {
			file = (File) filesIter.next();
			result.add(file); // always add, even if directory
			if (!file.isFile()) {
				// must be a directory
				// recursive call!
				List deeperList = getJarsFileListing(file);
				result.addAll(deeperList);
			}

		}
		Collections.sort(result);
		return result;
	}

	  /**
	  * Directory is valid if it exists, does not represent a file, and can be read.
	  */
	  static private void validateDirectory(File aDirectory)
			throws FileNotFoundException {
		if (aDirectory == null) {
			throw new IllegalArgumentException("Directory should not be null.");
		}
		if (!aDirectory.exists()) {
			throw new FileNotFoundException("Directory does not exist: "
					+ aDirectory);
		}
		if (!aDirectory.isDirectory()) {
			throw new IllegalArgumentException("Is not a directory: "
					+ aDirectory);
		}
		if (!aDirectory.canRead()) {
			throw new IllegalArgumentException("Directory cannot be read: "
					+ aDirectory);
		}
	}

	/**
	 * 
	 * Create a concrete sbb class name based on a given sbb abstract class name
	 * 
	 * @param sbbAbstractClassName the sbb abstract name which will be the base of the concrete class
	 */
	public static String getSbbConcreteClassName(String sbbAbstractClassName)
	{
		return 
			ConcreteClassGeneratorUtils.SBB_CONCRETE_CLASS_NAME_PREFIX
			+ sbbAbstractClassName
			+ ConcreteClassGeneratorUtils.SBB_CONCRETE_CLASS_NAME_SUFFIX;
	}
	
	
	/**
	 * Create the links with possible interfaces
	 * 
	 * @param concreteClass
	 *            the concrete class to which to add the interface links
	 * @param interfaces
	 *            the interfaces the concrete class has to implement
	 */
	public static void createInterfaceLinks(CtClass concreteClass,
			CtClass[] interfaces) {
		if (interfaces != null)
			concreteClass.setInterfaces(interfaces);
		else
			return;
		for (int i = 0; i < interfaces.length; i++) {
			logger.debug(concreteClass.getName() + " Implements link with "
					+ interfaces[i].getName() + " interface created");
		}
	}

	/**
	 * Create the inheritance link with the sbb absract class provided by the
	 * sbb developer
	 * 
	 * @param concreteClass
	 *            the concrete class to which to add the inheritance link
	 * @param superClass
	 *            the superClass to set
	 */
	public static void createInheritanceLink(CtClass concreteClass,
			CtClass superClass) {
		if (superClass == null)
			return;
		try {
			concreteClass.setSuperclass(superClass);
			logger.debug(concreteClass.getName() + " Inheritance link with "
					+ superClass.getName() + " class created");
		} catch (CannotCompileException cce) {
			cce.printStackTrace();
		}
	}

	
	/**
	 * Add a concrete method invoking the interceptor <B>interceptorName </B>
	 * with the same parameters,name,return type as the method <B>method </B>
	 * 
	 * @param concreteClass
	 *            the concrete where to add the interceptor method
	 * @param method
	 *            method from which is generating the new proxy method
	 * @param interceptorName
	 *            the interceptor name implementing the interface
	 *            InvocationHandler. This name must be the name of an existing
	 *            field of the concrete class
	 * @param callSuperMethod
	 *            if true the concrete implementation of the method will call
	 *            super()
	 */
	public static void addInterceptedMethod(CtClass concreteClass,
			CtMethod method, String interceptorName, boolean callSuperMethod) {
		if (method == null) throw new InvalidParameterException("Intercepted method should not be null");
		if (interceptorName == null) throw new InvalidParameterException("Interceptor class name should not be null");

		String methodToAdd = "public ";
		//Add the return type
		boolean hasReturn = false;
		CtClass returnType = null;
		try {
			returnType = method.getReturnType();
			methodToAdd = methodToAdd.concat(returnType.getName() + " ");
			hasReturn = true;
		} catch (NotFoundException nfe) {
			//nfe.printStackTrace();
		    logger.debug("No return type -- assuming return type is void " );
			methodToAdd = methodToAdd + "void ";
		}
		//Add the method name
		methodToAdd = methodToAdd.concat(method.getName() + "(");
		//Add the parameters
		CtClass[] parameterTypes = null;
		;
		String parametersInit = "Object[] args=null;";
		String argsInit = "Class[] classes=null;";
		try {
			parameterTypes = method.getParameterTypes();
			parametersInit = parametersInit + "args=new Object["
					+ parameterTypes.length + "];";
			argsInit = argsInit + "classes=new Class[" + parameterTypes.length
					+ "];";
			for (int argNumber = 0; argNumber < parameterTypes.length; argNumber++) {
				methodToAdd = methodToAdd.concat(parameterTypes[argNumber]
						.getName()
						+ " arg_" + argNumber);
				//handle the primitive types
				if (!parameterTypes[argNumber].isPrimitive())
					parametersInit = parametersInit + " args[" + argNumber
							+ "]=arg_" + argNumber + ";";
				else
					parametersInit = parametersInit
							+ " args["
							+ argNumber
							+ "]="
							+ ClassUtils
									.getObjectFromPrimitiveType(
											parameterTypes[argNumber].getName(),
											"arg_" + argNumber) + ";";
				String typeClass = parameterTypes[argNumber].getName();
				//handle the primitive types
				if (!parameterTypes[argNumber].isPrimitive()) {
					if (parameterTypes[argNumber].isArray()) {
						String arrayClassRepresentation = toArray(parameterTypes[argNumber]);
						if (arrayClassRepresentation != null)
							argsInit = argsInit + "classes[" + argNumber + "]="
									+ SleeContainerUtils.class.getName() + ".getCurrentThreadClassLoader().loadClass(\""
									+ arrayClassRepresentation + "\");";
					} else
						argsInit = argsInit + "classes[" + argNumber
								+ "]="+SleeContainerUtils.class.getName() + ".getCurrentThreadClassLoader().loadClass(\"" + typeClass + "\");";
				} else
					argsInit = argsInit + "classes[" + argNumber + "]="
							+ ClassUtils.getClassFromPrimitiveType(typeClass) + ".TYPE;";
				if (argNumber + 1 < parameterTypes.length)
					methodToAdd = methodToAdd + ",";
			}
			
			methodToAdd += ") ";

			// Add method exceptions
			if (method.getExceptionTypes().length > 0)
			{
			    CtClass[] exceptions = method.getExceptionTypes();
				methodToAdd += " throws ";
				for (int i = 0; i < exceptions.length-1; i++) {
				    String exName = exceptions[i].getName();
				    methodToAdd += exName + ", ";
				}
				methodToAdd += exceptions[exceptions.length-1].getName();
			}

		} catch (NotFoundException nfe) {
			nfe.printStackTrace();
			throw new SLEEException("Failed creating concrete Profile MBean implementation class", nfe);
		}
		
		// Start adding method body
		methodToAdd +=  " { ";
		methodToAdd += "" + parametersInit;
		methodToAdd += "" + argsInit;
		methodToAdd += "Class clazz=this.getClass();";
		methodToAdd += "Object result=null;";
		methodToAdd += "try{";
		//call the super method
		if (callSuperMethod) {
		    if(method.getName().equals("profileStore")){
				methodToAdd = methodToAdd + "super." + method.getName() + "(";
				if (parameterTypes != null && parameterTypes.length > 0) {
					for (int argNumber = 0; argNumber < parameterTypes.length; argNumber++) {
						methodToAdd = methodToAdd + "arg_" + argNumber;
						if (argNumber + 1 < parameterTypes.length)
							methodToAdd = methodToAdd + ",";
					}
				}
				methodToAdd = methodToAdd + ");";
		    }
		}
		methodToAdd = methodToAdd
				+ "java.lang.reflect.Method method=clazz.getDeclaredMethod(\""
				+ method.getName() + "\",classes" + ");";
		methodToAdd = methodToAdd + "result=" + interceptorName
				+ ".invoke(this,method,args); ";
		methodToAdd = methodToAdd
				+ "}catch(RuntimeException t){t.printStackTrace(); throw (t); "
				+ " } catch (Exception ex1) { ex1.printStackTrace(); throw (ex1); }";
		//call the super method
		if (callSuperMethod) {
		    if(!method.getName().equals("profileStore")){				
				methodToAdd = methodToAdd + "super." + method.getName() + "(";
				if (parameterTypes != null && parameterTypes.length > 0) {
					for (int argNumber = 0; argNumber < parameterTypes.length; argNumber++) {
						methodToAdd = methodToAdd + "arg_" + argNumber;
						if (argNumber + 1 < parameterTypes.length)
							methodToAdd += ",";
					}
				}
				methodToAdd += ");";
		    }
		}
		//handle the primitive types
		if (hasReturn) {
			if (!returnType.getName().equalsIgnoreCase("void")) {
				if (!returnType.isPrimitive())
					methodToAdd = methodToAdd + "return ("
							+ returnType.getName() + ")result;";
				else
					methodToAdd = methodToAdd
							+ "return "
							+ ClassUtils
									.getPrimitiveTypeFromObject(returnType
											.getName(), "result") + ";";
			}

		}
		methodToAdd += "}";
		//Add the implementation code
		logger.debug("Method " + methodToAdd + " added");
		CtMethod methodTest;
		try {
			methodTest = CtNewMethod.make(methodToAdd, concreteClass);
			concreteClass.addMethod(methodTest);
		} catch (CannotCompileException cce) {
			throw new SLEEException("Cannot compile method " + method.getName(), cce);
		}
	}

	/**
	 * Transform an array class to its string representation <BR>
	 * example: String[] -> [Ljava.lang.String;
	 * 
	 * @param typeClass
	 *            the array class to transform
	 * @return the String representation of the class
	 */
	public static String toArray(CtClass typeClass) {
		StringTokenizer st = new StringTokenizer(typeClass.getName(), "[");
		String name = null;
		CtClass arrayClass;
		try {
			arrayClass = typeClass.getComponentType();
			if (!arrayClass.isPrimitive())
				name = "L" + arrayClass.getName().replace('/', '.') + ";";
			else
				name = toJvmRepresentation(arrayClass.getName());
			st.nextToken();
			while (st.hasMoreTokens()) {
				st.nextToken();
				name = "[" + name;
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * Get the jvm representation of the primitives types <BR>
	 * Exemple: int -> I, boolean -> Z and so on...
	 * 
	 * 
	 * @param primitiveTypeName
	 *            the primitive type name whose the jvm representation is wanted
	 * @return the jvm representation of the primitive type
	 */
	public static String toJvmRepresentation(String primitiveTypeName) {
		if (primitiveTypeName.equals("int"))
			return "I";
		if (primitiveTypeName.equals("boolean"))
			return "Z";
		if (primitiveTypeName.equals("byte"))
			return "B";
		if (primitiveTypeName.equals("char"))
			return "C";
		if (primitiveTypeName.equals("double"))
			return "D";
		if (primitiveTypeName.equals("float"))
			return "F";
		if (primitiveTypeName.equals("long"))
			return "J";
		if (primitiveTypeName.equals("short"))
			return "S";
		if (primitiveTypeName.equals("void"))
			return "V";
		return primitiveTypeName;
	}

	/**
	 * Copy declared methods from one class to another
	 * 
	 * @param source
	 *            the class from which the methods are copied
	 * @param destination
	 *            the class to which the methods are copied
	 * @param exceptions
	 *            optionnal, defines the set of exceptions the methods can throw
	 */
	public static void copyMethods(CtClass source, CtClass destination,
			CtClass[] exceptions) {
		copyMethods(source.getDeclaredMethods(), destination, exceptions);
	}


	/**
	 * Copy all methods from one class to another
	 * FIXME emmartins: merge with other method when 1.1 dev cycle ends
	 * @param source
	 *            the class from which the methods are copied
	 * @param destination
	 *            the class to which the methods are copied
	 * @param exceptions
	 *            optionnal, defines the set of exceptions the methods can throw
	 */
	public static void copyAllMethods(CtClass source, CtClass destination,
			CtClass[] exceptions) {
		copyMethods(source.getDeclaredMethods(), destination, exceptions);
	}
	
	/**
	 * Copy methods to a class
	 *  
	 * @param methods
	 *            the methods to copy
	 * @param destination
	 *            the class to which the methods are copied
	 * @param exceptions
	 *            optional, defines the set of exceptions the methods can throw
	 */
	public static void copyMethods(CtMethod[] methods, CtClass destination,
			CtClass[] exceptions) {
		CtMethod methodCopy = null;
		for (CtMethod method : methods) {
			try {
				methodCopy = new CtMethod(method, destination, null);
				if (exceptions != null) {
					try {
						methodCopy.setExceptionTypes(exceptions);
					} catch (NotFoundException e) {
						throw new SLEEException(e.getMessage(),e);
					}
				}
				destination.addMethod(methodCopy);
			} catch (CannotCompileException e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
	}
}