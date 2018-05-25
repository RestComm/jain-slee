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

/*
 * ConcreteUsageParameterClassGenerator.java
 * 
 * Created on Jan 9, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Restcomm Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.container.deployment;

import java.beans.Introspector;
import java.util.HashSet;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import javax.slee.usage.SampleStatistics;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ClassPool;
import org.mobicents.slee.runtime.usage.AbstractUsageParameterSet;

/**
 * @author M.Ranganathan
 * @author martins
 * @author baranowb 
 */
public class ConcreteUsageParameterClassGenerator {

    private static Logger logger = Logger.getLogger(ConcreteUsageParameterClassGenerator.class);

    private static final String _METHOD_INCREMENT_="incrementParameter";
    private static final String _METHOD_GET_PARAMETER_="getParameter";
    private static final String _METHOD_SAMPLE_="sampleParameter";
    private static final String _METHOD_GET_SAMPLE_="getParameterSampleStatistics";
    private static final String _METHOD_GET_PARAM_NAMES_="getParameterNames";

    
    private final ClassPool classPool;

    private final String usageParameterInterfaceName;
    
    private final String deploymentDir;
    
    private final HashSet<String> generatedFields = new HashSet<String>();

    public ConcreteUsageParameterClassGenerator(String usageParameterInterfaceName, String deploymentDir, ClassPool classPool) {
        this.usageParameterInterfaceName = usageParameterInterfaceName;
        this.deploymentDir = deploymentDir;
    	this.classPool = classPool;
    }

	public Class<?> generateConcreteUsageParameterClass() throws Exception {
		CtClass usageParamInterface = classPool.get(usageParameterInterfaceName);

		String concreteClassName = usageParameterInterfaceName + "Impl";

		CtClass abstractSuperClass = classPool.get(AbstractUsageParameterSet.class.getName());

		CtMethod[] methods = usageParamInterface.getMethods();

		CtClass ctClass = classPool.makeClass(concreteClassName);

		try {
			//Generates the implements link
            ConcreteClassGeneratorUtils.createInterfaceLinks(ctClass,
                    new CtClass[] { usageParamInterface });
            ConcreteClassGeneratorUtils.createInheritanceLink(ctClass, abstractSuperClass);
			
            for (int i = 0; i < methods.length; i++) {
                generateConcreteMethod(ctClass, methods[i]);
            }
            
            generateParamNamesGetter(abstractSuperClass,ctClass);
            //generateResetMethod(ctClass);
         // write file
            ctClass.writeFile(deploymentDir);
            if (logger.isTraceEnabled())
            	logger.trace("UsageParameterGenerator Writing file "
                        + concreteClassName);
            return Thread.currentThread().getContextClassLoader()
                    .loadClass(concreteClassName);
		} finally {

			ctClass.defrost();
		}
	}
    
  

    private void generateParamNamesGetter(CtClass declaring, CtClass destination) throws NotFoundException, CannotCompileException {
		//little helper method, it is used on init, only once.
    	CtMethod abstractMethod = declaring.getDeclaredMethod(_METHOD_GET_PARAM_NAMES_);
    	CtMethod concreteMethod = CtNewMethod.copy(abstractMethod,
    			destination, null);
		String body="{";
		body += java.util.HashSet.class.getName()+" ret = new "+java.util.HashSet.class.getName()+"();";
		for(String fieldName : this.generatedFields)
		{
			body +="ret.add(\""+fieldName+"\");";
		}
		
		
		body += "return ($r)ret;";
		body += "}";
		
		 if (logger.isTraceEnabled())
	        logger.trace("Adding get usage param names getter with source "+body);
	        
		 
		 concreteMethod.setBody(body);
		 destination.addMethod(concreteMethod);
	}

	
    /**
     * @param method
     * @return
     */
    private void generateConcreteMethod(CtClass ctClass, CtMethod method)
            throws Exception {
        /*
         * The SLEE derives the usage parameter type associated with this usage
         * parameter name from the method name of the declared method.The SBB
         * Developer declares an increment method to declare the presence of and
         * to permit updates to a counter-type usage parameter. The method name
         * of the increment method is derived by adding an increment prefix to
         * the usage parameter name. ( See chapter 11.2.1 ).
         */
        String methodName = method.getName();

        String paramName = null;
        if (methodName.startsWith("increment")) {
            
        	paramName = Introspector.decapitalize(methodName.substring("increment".length()));
            
            
            this.generatedFields.add(paramName);

        } else if (methodName.startsWith("sample")) {
            /*
             * Sample-type usage parameters. The SBB can add sample values to a
             * sample -type usage parameter. The Administrator can get the
             * minimum, maximum, mean, and number of the sample values added to
             * a sample -type usage parameter and reset the state of the usage
             * parameter through the SLEE's management interface.
             */
            
            paramName = Introspector.decapitalize(methodName.substring("sample".length()));
            
           
            this.generatedFields.add(paramName);

        } else
            return; // TODO -- should I throw exception here ?
        
        if (logger.isTraceEnabled())
        	logger.trace("generateConcreteMethod(): USAGEPARAM variable is " + paramName);
        
        if (logger.isTraceEnabled())
        	logger.trace("Generating usage method = " + methodName);
        
        String body = "";
        String getterBody = "";
        String getter11Body = "";

        if (methodName.startsWith("increment")) {

            body += "public synchronized void " + methodName + "( long longValue ) { ";
            body +="super."+_METHOD_INCREMENT_+"(\""+paramName+"\",longValue);";

            body += "}";

            getterBody += "public long get" + methodName.substring("increment".length())
                    + "( boolean reset) { ";
            getterBody +="return super."+_METHOD_GET_PARAMETER_+"(\""+paramName+"\",reset);";
            getterBody += "}";
            
            getter11Body += "public long get" + methodName.substring("increment".length())
            + "() { return super."+_METHOD_GET_PARAMETER_+"(\""+paramName+"\",false); }";

        } else if (methodName.startsWith("sample")) {
            /*
             * Sample-type usage parameters. The SBB can add sample values to a
             * sample -type usage parameter. The Administrator can get the
             * minimum, maximum, mean, and number of the sample values added to
             * a sample -type usage parameter and reset the state of the usage
             * parameter through the SLEE's management interface.
             */

            body += "public void " + methodName + "( long longValue ) { ";
            body += "super."+_METHOD_SAMPLE_+"(\""+paramName+"\",longValue);";
            body += "}";

            getterBody += "public " + SampleStatistics.class.getName() + " get"
                    + methodName.substring("sample".length()) + "( boolean reset) { ";
            getterBody += "return super."+_METHOD_GET_SAMPLE_+"(\""+paramName+"\",reset);";
            getterBody += "}";
            
            getter11Body += "public " + SampleStatistics.class.getName() + " get" + methodName.substring("sample".length())
            + "() { return super."+_METHOD_GET_SAMPLE_+"(\""+paramName+"\",false); }";          
            
        } else {
            return;
        }

        if (logger.isTraceEnabled()) {
        	logger.trace("Adding SLEE 1.0 usage param getter with source "+getterBody);
        }
        CtMethod getterMethod = CtNewMethod.make(getterBody, ctClass);
        ctClass.addMethod(getterMethod);
        
        if (logger.isTraceEnabled()) {
        	logger.trace("Adding SLEE 1.1 usage param getter with source "+getter11Body);
        }
        CtMethod getter11Method = CtNewMethod.make(getter11Body, ctClass);
        ctClass.addMethod(getter11Method);
        
        if (logger.isTraceEnabled()) {
        	logger.trace("Adding usage param setter with source "+body);
        }
        CtMethod setterMethod = CtNewMethod.make(body, ctClass);
        ctClass.addMethod(setterMethod);
    }
   // private void generateResetMethod(CtClass ctClass) throws Exception {
    //	
    //}
}

