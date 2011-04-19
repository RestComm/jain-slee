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

package org.mobicents.slee.container.deployment;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.mobicents.slee.SbbLocalObjectExt;
import org.mobicents.slee.container.component.ClassPool;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectConcrete;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectImpl;

/**
 * Class generating the concrete Sbb local object class
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 * @author martins 
 */
public class ConcreteSbbLocalObjectGenerator {

    /**
     * Logger to log information
     */
    private static final Logger logger = Logger.getLogger(ConcreteSbbLocalObjectGenerator.class);

    private final String sbbLocalObjectName;
    
    private final String sbbAbstractClassName;
    
    private final String deployPath;
    
    /**
     * Pool to generate or read classes with javassist
     */
    private final ClassPool pool;

    /**
     *  
     */
    protected CtClass sleeSbbLocalObject = null;

    /**
     * The interface from which to generate the concrete class
     */
    protected CtClass sbbLocalObjectInterface = null;

    /**
     * The concrete sbb local object class
     */
    protected CtClass concreteSbbLocalObject = null;
    
    public ConcreteSbbLocalObjectGenerator(String sbbLocalObjectName, String sbbAbstractClassName, String deployPath,
            ClassPool pool) {
        this.sbbLocalObjectName = sbbLocalObjectName;
        this.sbbAbstractClassName = sbbAbstractClassName;
        this.deployPath = deployPath;
        this.pool = pool;
    }

    /**
     * Generate the Sbb Local Object Class
     * 
     * @param sbbLocalObjectName
     *            the name of the Sbb Local Object
     * @return the concrete Sbb Local Object class implementing the Sbb Local
     *         Object
     */
    public Class generateSbbLocalObjectConcreteClass() {
        //Generates the implements link
        if (logger.isTraceEnabled()) {
            logger.trace("generateSbbLocalObjectConcreteClass: sbbLocalObjectInterface = "
                            + sbbLocalObjectName
                            + " deployPath = "
                            + deployPath);
        }

        try {
        	
        	concreteSbbLocalObject = pool.makeClass(ConcreteClassGeneratorUtils.SBB_LOCAL_OBJECT_CLASS_NAME_PREFIX + sbbLocalObjectName + ConcreteClassGeneratorUtils.SBB_LOCAL_OBJECT_CLASS_NAME_SUFFIX);
    		
            try {
                sleeSbbLocalObject = pool.get(SbbLocalObjectImpl.class
                        .getName());
                sbbLocalObjectInterface = pool.get(sbbLocalObjectName);
            } catch (NotFoundException nfe) {
                nfe.printStackTrace();
                String s = "Problem with pool ";
                logger.error(s, nfe);
                throw new RuntimeException(s, nfe);
            }
            // This is our implementation interface.
            CtClass concreteClassInterface;
            try {
                concreteClassInterface = pool.get(SbbLocalObjectConcrete.class
                        .getName());
            } catch (NotFoundException nfe) {
                nfe.printStackTrace();
                String s = "Problem with the pool! ";
                logger.error(s, nfe);
                throw new RuntimeException(s, nfe);
            }

            ConcreteClassGeneratorUtils.createInterfaceLinks(
                    concreteSbbLocalObject, new CtClass[] {
                            sbbLocalObjectInterface, concreteClassInterface });
            //Generates an inheritance link to the slee implementation of the
            // SbbLocalObject interface
            ConcreteClassGeneratorUtils.createInheritanceLink(
                    concreteSbbLocalObject, sleeSbbLocalObject);
            
            //Generates the methods to implement from the interface

            Map interfaceMethods = ClassUtils
                    .getInterfaceMethodsFromInterface(sbbLocalObjectInterface);

            generateConcreteMethods(interfaceMethods, sbbAbstractClassName);           

            try {
            	concreteSbbLocalObject.writeFile(deployPath);
            	if (logger.isDebugEnabled()) {
                    logger
                        .debug("Concrete Class "
                                + concreteSbbLocalObject.getName()
                                + " generated in the following path "
                                + deployPath);
                }            
            } catch (CannotCompileException e) {

                String s = " Unexpected exception ! ";
                logger.fatal(s, e);
                throw new RuntimeException(s, e);

            } catch (IOException e) {
                String s = "IO Exception!";
                logger.error(s, e);
                return null;

            }
            //load the class
            try {
				return Thread.currentThread().getContextClassLoader().loadClass(concreteSbbLocalObject.getName());
			} catch (ClassNotFoundException e) {
				logger.error("unable to load sbb local object impl class", e);
				return null;
			}
            
        } finally {
            if (this.concreteSbbLocalObject != null)
                this.concreteSbbLocalObject.defrost();
        }
    }

    /**
     * Generates the concrete methods of the class
     * 
     * @param interfaceMethods
     *            the methods to implement coming from the
     *            ActivityContextInterface developer
     * @param sbbAbstractClassName
     */
    private void generateConcreteMethods(Map interfaceMethods,
            String sbbAbstractClassName) {
        if (interfaceMethods == null)
            return;

        Iterator it = interfaceMethods.values().iterator();
        while (it.hasNext()) {
            CtMethod interfaceMethod = (CtMethod) it.next();
            if (interfaceMethod == null)
                return;

            // The following methods are implemented in the superclass

            if (interfaceMethod.getName().equals("isIdentical")
            		|| interfaceMethod.getName().equals("equals")
            		|| interfaceMethod.getName().equals("hashCode")
            		|| interfaceMethod.getName().equals("getSbbPriority")
                    || interfaceMethod.getName().equals("remove")
                    || interfaceMethod.getName().equals("setSbbPriority")
                	|| interfaceMethod.getName().equals("getName")
            		|| interfaceMethod.getName().equals("getChildRelation")
            		|| interfaceMethod.getName().equals("getParent"))         		
                continue;
            
            String methodToAdd = "public ";
            //Add the return type
            boolean hasReturn = false;
            CtClass returnType = null;
            try {
                // There's probably a more elegant way to do this.
                returnType = interfaceMethod.getReturnType();
                if (returnType.equals(CtClass.voidType)) {
                    methodToAdd += "void ";
                } else {
                    methodToAdd = methodToAdd
                            .concat(returnType.getName() + " ");
                    hasReturn = true;
                }
            } catch (NotFoundException nfe) {
                nfe.printStackTrace();
                methodToAdd = methodToAdd + "void ";
            }
            //Add the method name
            methodToAdd += interfaceMethod.getName() + "(";
            //Add the parameters
            CtClass[] parameterTypes = null;
            ;
            try {
                parameterTypes = interfaceMethod.getParameterTypes();
                for (int argNumber = 0; argNumber < parameterTypes.length; argNumber++) {
                    methodToAdd = methodToAdd.concat(parameterTypes[argNumber]
                            .getName()
                            + " arg_" + argNumber);
                    if (argNumber + 1 < parameterTypes.length)
                        methodToAdd = methodToAdd + ",";
                }
            } catch (NotFoundException nfe) {
                nfe.printStackTrace();
                throw new RuntimeException("unexpected Exception ! ", nfe);
            }
            methodToAdd = methodToAdd + ") { ";

            // We need to do this in a type neutral way !
            methodToAdd += SbbConcrete.class.getName() + " concrete = "
                    + " getSbbEntity().getSbbObject().getSbbConcrete();";

            //These methods are delegated to superclass.
            // Note the convoluted code here because finally is not supported
            // yet by javaassist.

            methodToAdd += "Object[] args = new Object ["
                    + parameterTypes.length + "];";

            methodToAdd += "Class[] types = new Class ["
                    + parameterTypes.length + "];";

            if (parameterTypes != null && parameterTypes.length > 0) {
                for (int argNumber = 0; argNumber < parameterTypes.length; argNumber++) {
                    methodToAdd += "args[" + argNumber + "]  = ";
                    // Check if parameter type is primitive and add the wrapper
                    // types.
                    if (parameterTypes[argNumber].isPrimitive()) {
                        CtClass ptype = parameterTypes[argNumber];
                        if (ptype.equals(CtClass.intType)) {
                            methodToAdd += "Integer.valueOf(" + "arg_" + argNumber
                                    + ");";
                        } else if (ptype.equals(CtClass.booleanType)) {
                            methodToAdd += "Boolean.valueOf(" + "arg_" + argNumber
                                    + ");";
                        } else if (ptype.equals(CtClass.longType)) {
                            methodToAdd += "Long.valueOf(" + "arg_" + argNumber
                                    + ");";
                        } else if (ptype.equals(CtClass.shortType)) {
                            methodToAdd += "Short.valueOf(" + "arg_" + argNumber
                                    + ");";
                        } else if (ptype.equals(CtClass.floatType)) {
                            methodToAdd += "Float.valueOf(" + "arg_" + argNumber
                                    + ");";
                        } else if (ptype.equals(CtClass.doubleType)) {
                            methodToAdd += "Double.valueOf(" + "arg_" + argNumber
                                    + ");";
                        } else if (ptype.equals(CtClass.charType)) {
                            methodToAdd += "Character.valueOf(" + "arg_"
                                    + argNumber + ");";
                        }
                    } else {
                        methodToAdd += "arg_" + argNumber + ";";
                    }

                }

                for (int i = 0; i < parameterTypes.length; i++) {
                    methodToAdd += "types[" + i + "] = ";
                    if (parameterTypes[i].isPrimitive()) {
                        CtClass ptype = parameterTypes[i];
                        if (ptype.equals(CtClass.intType)) {
                            methodToAdd += "Integer.TYPE;";
                        } else if (ptype.equals(CtClass.booleanType)) {
                            methodToAdd += "Boolean.TYPE;";
                        } else if (ptype.equals(CtClass.longType)) {
                            methodToAdd += "Long.TYPE;";
                        } else if (ptype.equals(CtClass.shortType)) {
                            methodToAdd += "Short.TYPE;";
                        } else if (ptype.equals(CtClass.floatType)) {
                            methodToAdd += "Float.TYPE;";
                        } else if (ptype.equals(CtClass.doubleType)) {
                            methodToAdd += "Double.TYPE;";
                        } else if (ptype.equals(CtClass.charType)) {
                            methodToAdd += "Character.TYPE;";
                        }
                    } else {
                    	methodToAdd += parameterTypes[i].getName() + ".class; ";
                    }
                }
            }
            if (hasReturn) {
                methodToAdd += " return  " + "(" + returnType.getName() + ")";
            }

            if (returnType.isPrimitive()) {
                methodToAdd += "sbbLocalObjectInterceptor.invokeAndReturn"
                        + returnType.getSimpleName() + "(concrete," + "\""
                        + interfaceMethod.getName() + "\"" + ", args, types); ";
            } else {
                methodToAdd += "sbbLocalObjectInterceptor.invokeAndReturnObject(concrete,"
                        + "\""
                        + interfaceMethod.getName()
                        + "\""
                        + ", args, types );";
            }
            methodToAdd += "}";

            //Add the implementation code
            if (logger.isTraceEnabled()) {
                logger.trace("Method " + methodToAdd + " added");
            }
            CtMethod methodTest;
            try {
                methodTest = CtNewMethod.make(methodToAdd,
                        concreteSbbLocalObject);
                concreteSbbLocalObject.addMethod(methodTest);
            } catch (CannotCompileException cce) {
                cce.printStackTrace();
                throw new RuntimeException("error generating method ", cce);
            }
        }
    }

}