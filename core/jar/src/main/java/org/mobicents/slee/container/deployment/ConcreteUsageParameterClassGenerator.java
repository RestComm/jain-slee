/*
 * ConcreteUsageParameterClassGenerator.java
 * 
 * Created on Jan 9, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
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
import java.util.Iterator;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;

import javax.slee.usage.SampleStatistics;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.management.jmx.InstalledUsageParameterSet;
import org.mobicents.slee.container.management.jmx.UsageMBeanImpl;

/**
 * @author M.Ranganathan
 * @author martins
 *  
 */
public class ConcreteUsageParameterClassGenerator {

    private static Logger logger = Logger.getLogger(ConcreteUsageParameterClassGenerator.class);

    private static final String NAME_FIELD = "name";

    public static final String SET_NAME = "setName";

    public static final String GET_NAME = "getName";

    private static final String USAGE_PARAMETER_MBEAN_FIELD = "usageMBean";

    public static final String SET_USAGE_PARAMETER_MBEAN = "setUsageMBean";

    public static final String GET_USAGE_PARAMETER_MBEAN = "getUsageMBean";
    
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
        
        CtClass implClassInterface = classPool.get(InstalledUsageParameterSet.class.getName());
        
        CtMethod[] methods = usageParamInterface.getMethods();
        
        CtClass ctClass = classPool.makeClass(concreteClassName);
		
        try {
                   
            //Generates the implements link
            ConcreteClassGeneratorUtils.createInterfaceLinks(ctClass,
                    new CtClass[] { usageParamInterface, implClassInterface });
            // generate the "usage mbean" field, getter and setter
            CtField ctField = new CtField(classPool.get(UsageMBeanImpl.class
                    .getName()), USAGE_PARAMETER_MBEAN_FIELD, ctClass);
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);            
            generateUsageMBeanSetter(ctClass);
            generateUsageMBeanGetter(ctClass);
            // generate the "name" field, getter and setter
            ctField = new CtField(classPool.get(String.class.getName()),
                    NAME_FIELD, ctClass);
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
            generateNameSetter(ctClass);
            generateNameGetter(ctClass);
            // generate concrete methods for each usage param
            for (int i = 0; i < methods.length; i++) {
                generateConcreteMethod(ctClass, methods[i]);
            }
            // generate reset method
            generateResetMethod(ctClass);
            // generate constructor
            createDefaultConstructor(ctClass);
            // write file
            ctClass.writeFile(deploymentDir);
            if (logger.isDebugEnabled())
                logger.debug("UsageParameterGenerator Writing file "
                        + concreteClassName);
            Class<?> retval = Thread.currentThread().getContextClassLoader()
                    .loadClass(concreteClassName);
            return retval;
        } finally {
            
            ctClass.defrost();
        }
    }

    private void generateUsageMBeanSetter(CtClass concreteClass)
            throws Exception {

        String body = "public void " + SET_USAGE_PARAMETER_MBEAN + " ( "
                + UsageMBeanImpl.class.getName() + " usageMbean ) { this."
                + USAGE_PARAMETER_MBEAN_FIELD + "= usageMbean; }";
        CtMethod ctMethod = CtNewMethod.make(body, concreteClass);
        concreteClass.addMethod(ctMethod);
    }

    private void generateUsageMBeanGetter(CtClass concreteClass)
            throws Exception {
        String body = "public " + UsageMBeanImpl.class.getName() + " "
                + GET_USAGE_PARAMETER_MBEAN + "() {";
        body += "return " + USAGE_PARAMETER_MBEAN_FIELD + "; }";
        CtMethod ctMethod = CtNewMethod.make(body, concreteClass);
        concreteClass.addMethod(ctMethod);

    }
   
    private void generateNameGetter(CtClass concreteClass) throws Exception {
        String body = "public " + String.class.getName()
                + " getName() { return " + NAME_FIELD + "; } ";
        CtMethod ctMethod = CtNewMethod.make(body, concreteClass);
        concreteClass.addMethod(ctMethod);
    }

    private void generateNameSetter(CtClass concreteClass) throws Exception {
        String body = "public void " + SET_NAME + "( "
                + java.lang.String.class.getName() + " n) { " + NAME_FIELD
                + " = n   ; } ";
        CtMethod ctMethod = CtNewMethod.make(body, concreteClass);
        concreteClass.addMethod(ctMethod);
    }

    private void createDefaultConstructor(CtClass concreteClass) {

        CtConstructor defaultConstructor = new CtConstructor(null,
                concreteClass);
        String constructorBody = "{ this.reset(); }";
        try {
            defaultConstructor.setBody(constructorBody);
            concreteClass.addConstructor(defaultConstructor);
            logger.debug("DefaultConstructor created");
        } catch (CannotCompileException e) {
            //Auto-generated catch block
            e.printStackTrace();
        }
    }

    

    /**
     * Reset the usage parameters.
     */
    private void generateResetMethod(CtClass ctClass) throws Exception {
        String body = "public synchronized void reset() {";
        for (Iterator<String> it = this.generatedFields.iterator(); it.hasNext();) {
            String generatedField = (String) it.next();
            if ( generatedField.endsWith("Max")) {
                body += generatedField + " = Long.MIN_VALUE  ; ";
            } else if ( generatedField.endsWith("Min")) {
                body += generatedField + " = Long.MAX_VALUE ; ";
            } else 
                body += generatedField + " = 0; ";
        }
        body += "}";
        if ( logger.isDebugEnabled()) {
            logger.debug("generateResetMethod(): body = " + body);
        }
        CtMethod newMethod = CtNewMethod.make(body, ctClass);
        ctClass.addMethod(newMethod);
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
         * of the increment method is derived by adding an “increment” prefix to
         * the usage parameter name. ( See chapter 11.2.1 ).
         */
        String methodName = method.getName();

        String paramName = null;
        if (methodName.startsWith("increment")) {
            
        	paramName = Introspector.decapitalize(methodName.substring("increment".length()));
            
            CtField ctField = new CtField(CtClass.longType, paramName + "Value",
                    ctClass);

            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
            this.generatedFields.add(paramName + "Value");

            ctField = new CtField(CtClass.longType, paramName + "Sum", ctClass);

            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
            this.generatedFields.add(paramName + "Sum");
            
            ctField = new CtField(CtClass.longType, paramName + "Count", ctClass);
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
            this.generatedFields.add(paramName + "Count");

        } else if (methodName.startsWith("sample")) {
            /*
             * Sample-type usage parameters. The SBB can add sample values to a
             * sample -type usage parameter. The Adminstrator can get the
             * minimum, maximum, mean, and number of the sample values added to
             * a sample -type usage parameter and reset the state of the usage
             * parameter through the SLEE’s management interface.
             */
            
            paramName = Introspector.decapitalize(methodName.substring("sample".length()));
            
            CtField ctField = new CtField(CtClass.longType, paramName + "Value",
                    ctClass);

            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
            this.generatedFields.add(paramName + "Value");

            ctField = new CtField(CtClass.doubleType, paramName + "Mean", ctClass);

            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
            this.generatedFields.add(paramName + "Mean");

            ctField = new CtField(CtClass.longType, paramName + "Min", ctClass);

            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
            this.generatedFields.add(paramName + "Min");

            ctField = new CtField(CtClass.longType, paramName + "Max", ctClass);

            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
            this.generatedFields.add(paramName + "Max");

            ctField = new CtField(CtClass.longType, paramName + "Count", ctClass);
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
            this.generatedFields.add(paramName + "Count");
            
            ctField = new CtField(CtClass.doubleType, paramName + "Sum", ctClass);
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
            this.generatedFields.add(paramName + "Sum");

        } else
            return; // TODO -- should I throw exception here ?
        
        if ( logger.isDebugEnabled() )
            logger.debug("generateConcreteMethod(): USAGEPARAM variable is " + paramName);
        
        if (logger.isDebugEnabled())
            logger.debug("Generating usage method = " + methodName);
        
        String body = "";
        String getterBody = "";
        String getter11Body = "";

        if (methodName.startsWith("increment")) {

            body += "public synchronized void " + methodName + "( long longValue ) { ";
            // code for notification propagation here.

            body += "this." + paramName + "Count += 	longValue;";
            body += "this." + paramName + "Sum += longValue;";
            body += "this." + paramName + "Value = longValue;";

            // sendUsageNotification (long value, long seqno, String
            // usageParameterSetName, String usageParameterName, boolean
            // isCounter)

            body += "this." + USAGE_PARAMETER_MBEAN_FIELD
                    + ".sendUsageNotification(" + "this." + paramName + "Sum, "
                    + "this." + paramName + "Count, " + "this." + NAME_FIELD
                    + "," + "\"" + paramName + "\"" + ","
                    + Boolean.TRUE.toString() + ");";

            body += "}";

            getterBody += "public synchronized long get" + methodName.substring("increment".length())
                    + "( boolean reset) { ";
            getterBody += "long tempCount = this." + paramName + "Count;";
            getterBody += "if (reset == true) {this." + paramName + "Count=0;";
            getterBody += "this." + paramName + "Sum = 0;";
            getterBody += "this." + paramName + "Value = 0;}";
            //getterBody += "this." + paramName + "Min = Long.MAX_VALUE ;";
            //getterBody += "this." + paramName + "Max = Long.MIN_VALUE ;}";
            getterBody += "return tempCount;";
            getterBody += "}";
            
            getter11Body += "public long get" + methodName.substring("increment".length())
            + "() { return get" + methodName.substring("increment".length())+ "(false); }";

        } else if (methodName.startsWith("sample")) {
            /*
             * Sample-type usage parameters. The SBB can add sample values to a
             * sample -type usage parameter. The Adminstrator can get the
             * minimum, maximum, mean, and number of the sample values added to
             * a sample -type usage parameter and reset the state of the usage
             * parameter through the SLEE’s management interface.
             */

            body += "public synchronized void " + methodName + "( long longValue ) { ";
            body += "this." + paramName + "Sum += longValue;";
            body += "this." + paramName + "Mean = this." + paramName + "Sum / (this." + paramName + "Count+1);";
            body += " if ( this." + paramName + "Max <  longValue ) this."
                    + paramName + "Max = longValue;";
            body += " if ( this." + paramName + "Min >  longValue ) this."
                    + paramName + "Min = longValue;";
            body += "this." + paramName + "Count++;";
            
            // sendUsageNotification (long value, long seqno, String
            // usageParameterSetName, String usageParameterName, boolean
            // isCounter)

            body += "this." + USAGE_PARAMETER_MBEAN_FIELD
                    + ".sendUsageNotification( longValue, "
                    + "this." + paramName + "Count, " + "this." + NAME_FIELD
                    + "," + "\"" + paramName + "\"" + ","
                    + Boolean.FALSE.toString() + ");";

            body += "}";

            getterBody += "public synchronized " + SampleStatistics.class.getName() + " get"
                    + methodName.substring("sample".length()) + "( boolean reset) { ";
            getterBody += SampleStatistics.class.getName() + " tempValue"
                    + "= new " + SampleStatistics.class.getName()
                    + "(this." + paramName + "Count, " + "this." + paramName
                    + "Min, " + "this." + paramName + "Max, " + "this."
                    + paramName + "Mean);";
            getterBody += "if (reset == true) {this." + paramName + "Count=0;";
            getterBody += "this." + paramName + "Sum = 0;";
            getterBody += "this." + paramName + "Mean = 0;";
            getterBody += "this." + paramName + "Value = 0;";
            getterBody += "this." + paramName + "Min = Long.MAX_VALUE ;";
            getterBody += "this." + paramName + "Max = Long.MIN_VALUE ;}";
            getterBody += "return tempValue;";
            getterBody += "}";
            
            getter11Body += "public " + SampleStatistics.class.getName() + " get" + methodName.substring("sample".length())
            + "() { return get" + methodName.substring("sample".length())+ "(false); }";          
            
        } else {
            return;
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("Adding SLEE 1.0 usage param getter with source "+getterBody);
        }
        CtMethod getterMethod = CtNewMethod.make(getterBody, ctClass);
        ctClass.addMethod(getterMethod);
        
        if (logger.isDebugEnabled()) {
        	logger.debug("Adding SLEE 1.1 usage param getter with source "+getter11Body);
        }
        CtMethod getter11Method = CtNewMethod.make(getter11Body, ctClass);
        ctClass.addMethod(getter11Method);
        
        if (logger.isDebugEnabled()) {
        	logger.debug("Adding usage param setter with source "+body);
        }
        CtMethod setterMethod = CtNewMethod.make(body, ctClass);
        ctClass.addMethod(setterMethod);
    }

}

