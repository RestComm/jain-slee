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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;

import javax.slee.management.DeploymentException;
import javax.slee.usage.SampleStatistics;

import org.apache.log4j.Logger;
import org.jboss.util.Strings;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.InstalledUsageParameterSet;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.management.jmx.SampleStatisticsImpl;
import org.mobicents.slee.container.management.jmx.SbbUsageMBeanImpl;

/**
 * @author M.Ranganathan
 *  
 */
public class ConcreteUsageParameterClassGenerator {

    private static Logger logger;

    private ClassPool classPool;

    private static final String NAME_FIELD = "name";

    public static final String SET_NAME = "setName";

    public static final String GET_NAME = "getName";

    private static final String USAGE_PARAMETER_MBEAN_FIELD = "sbbUsageParameterMBean";

    public static final String SET_USAGE_PARAMETER_MBEAN = "setSbbUsageMBean";

    public static final String GET_USAGE_PARAMETER_MBEAN = "getSbbUsageMBean";

    private HashSet generatedFields;

    static {
        logger = Logger.getLogger(ConcreteUsageParameterClassGenerator.class);
    }

    public ConcreteUsageParameterClassGenerator(MobicentsSbbDescriptor sbbDescriptor) {
        this.classPool = ((DeployableUnitIDImpl) sbbDescriptor
                .getDeployableUnit()).getDUDeployer().getClassPool();
        this.generatedFields = new HashSet();
    }

    /**
     * Check if the usage parameter inteface conforms to the restrictions that
     * are present in Chapter 11 of the SLEE specification.
     * 
     * @param sbbDescriptor --
     *            the sbb descriptor
     * @return true if all checks pass correctly and false if not.
     * @throws DeploymentException 
     */
    public static boolean checkUsageParameterInterface(
            MobicentsSbbDescriptor sbbDescriptor) throws DeploymentException {
        try {
            Class clazz = Thread.currentThread().getContextClassLoader()
                    .loadClass(sbbDescriptor.getUsageParametersInterface());
            if (!clazz.isInterface())
                return false;
            Method[] methods = clazz.getMethods();
            HashSet incrementMethods = new HashSet();
            HashSet sampleMethods = new HashSet();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                // The method name must start with incremment or sample
                if (!method.getName().startsWith("increment")
                        && !method.getName().startsWith("sample")) {
                    logger.debug("method name " + method.getName()
                            + " is invalid! ");
                    return false;
                }
                if (method.getExceptionTypes() != null
                        && method.getExceptionTypes().length != 0) {
                    // See page 163 of Slee 1.0 spec.
                    logger.debug("Signature of usage parameter is invalid -- "
                            + " should not throw an exception : "
                            + method.getName());
                    return false;
                }
                if (method.getParameterTypes() == null
                        || method.getParameterTypes().length > 1) {
                    logger.error("Signature of usage parameter is invalid -- "
                            + "should have a single long parameter type "
                            + method.getName());
                    return false;
                }
                Class[] paramTypes = method.getParameterTypes();
                if (paramTypes[0] != Long.TYPE) {
                    logger.error("the parameter type should be long! ");
                    return false;
                }

                /*
                 * A single usage parameter name can only be associated with a
                 * single usage parameter type. The SLEE must check and reject
                 * an SBB Usage Parameters interface that declares both an
                 * increment method and a sample method for the same usage
                 * parameter name. (Spec page 162 )
                 */
                if (method.getName().startsWith("increment")) {
                    String p = method.getName().substring("increment".length());
                    if (sampleMethods.contains(p)) {
                        logger.error("already saw method in sample method set "
                                + p);
                        return false;
                    }

                    //Also first character of parameter name must be
                    // capitalized
                    if (!Character.isUpperCase(p.charAt(0))) {
                        logger
                                .error("First character of usage parameter name must be upper case : "
                                        + p);
                        return false;
                    }

                    incrementMethods.add(p);
                }
                if (method.getName().startsWith("sample")) {
                    String p = method.getName().substring("sample".length());
                    if (incrementMethods.contains(p)) {
                        logger
                                .error("usage parameter is already an increment parameter "
                                        + method.getName());
                        return false;
                    }

                    //Also first character of parameter name must be
                    // capitalized
                    if (!Character.isUpperCase(p.charAt(0))) {
                        logger
                                .error("First character of usage parameter name must be upper case : "
                                        + p);
                        return false;
                    }

                    sampleMethods.add(p);
                }

            }
            return true;
        } catch (Exception ex) {
            throw new DeploymentException("Exception while checking sbb usage parameter interface",ex);
        }
    }

    public Class generateConcreteUsageParameterClass(
            MobicentsSbbDescriptor sbbDescriptor) throws Exception {
        String usageParamInterfaceName = sbbDescriptor
                .getUsageParametersInterface();
        if (usageParamInterfaceName == null)
            return null;
        String concreteClassName = usageParamInterfaceName + "Impl";
        CtClass usageParamInterface = classPool.get(usageParamInterfaceName);

        CtClass implClassInterface = classPool
                .get(InstalledUsageParameterSet.class.getName());
        CtMethod[] methods = usageParamInterface.getMethods();
        
        CtClass ctClass = classPool.makeClass(concreteClassName);
		
        try {
            // createDefaultConstructor(ctClass);
            this.generateFields(ctClass,new CtClass[] {
                    classPool.get(ServiceIDImpl.class.getName()),
                    classPool.get(SbbIDImpl.class.getName()) });

            
            //Generates the implements link
            ConcreteClassGeneratorUtils.createInterfaceLinks(ctClass,
                    new CtClass[] { usageParamInterface, implClassInterface });
            CtField ctField = new CtField(classPool.get(SbbUsageMBeanImpl.class
                    .getName()), USAGE_PARAMETER_MBEAN_FIELD, ctClass);
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);

            generateSbbUsageMBeanSetter(ctClass);
            generateSbbUsageMBeanGetter(ctClass);

            generateSbbIDGetter(ctClass);
            generateServiceIDGetter(ctClass);
            ctField = new CtField(classPool.get(String.class.getName()),
                    NAME_FIELD, ctClass);

            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);

            generateNameSetter(ctClass);
            generateNameGetter(ctClass);

            for (int i = 0; i < methods.length; i++) {
                // Generate the concrete method.

                generateConcreteMethod(ctClass, methods[i]);

            }

            generateResetMethod(ctClass);
            createConstructor(ctClass, new CtClass[] {
                    classPool.get(ServiceIDImpl.class.getName()),
                    classPool.get(SbbIDImpl.class.getName()) });
            
            this.createDefaultConstructor(ctClass);

            String sbbDeploymentPathStr = sbbDescriptor.getDeploymentPath();
            ctClass.writeFile(sbbDeploymentPathStr);
            if (logger.isDebugEnabled())
                logger.debug("UsageParameterGenerator Writing file "
                        + concreteClassName);
            Class retval = Thread.currentThread().getContextClassLoader()
                    .loadClass(concreteClassName);
            return retval;
        } finally {
            
            ctClass.defrost();
        }

    }

    private void generateSbbUsageMBeanSetter(CtClass concreteClass)
            throws Exception {

        String body = "public void " + SET_USAGE_PARAMETER_MBEAN + " ( "
                + SbbUsageMBeanImpl.class.getName() + " usageMbean ) { this."
                + USAGE_PARAMETER_MBEAN_FIELD + "= usageMbean; }";
        CtMethod ctMethod = CtNewMethod.make(body, concreteClass);
        concreteClass.addMethod(ctMethod);
    }

    private void generateSbbUsageMBeanGetter(CtClass concreteClass)
            throws Exception {
        String body = "public " + SbbUsageMBeanImpl.class.getName() + " "
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

    private void generateServiceIDGetter(CtClass concreteClass)
            throws Exception {
        String body = "public " + ServiceIDImpl.class.getName()
                + " getServiceID() { return this.serviceIDImpl; }";
        CtMethod ctMethod = CtNewMethod.make(body, concreteClass);
        concreteClass.addMethod(ctMethod);
    }

    private void generateSbbIDGetter(CtClass concreteClass) throws Exception {
        String body = "public " + SbbIDImpl.class.getName()
                + " getSbbID() { return this.sbbIDImpl; }";
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
     * Generate the fields.
     * 
     * @param concreteClass
     * @param parameters
     * @throws Exception
     */
    private void generateFields (CtClass concreteClass, CtClass[] parameters ) {
        for (int i = 0; i < parameters.length; i++) {
            String parameterName = paramNameFromClassName(parameters[i]);

            try {
                CtField ctField = new CtField(parameters[i], parameterName,
                        concreteClass);
                ctField.setModifiers(Modifier.PRIVATE);
                concreteClass.addField(ctField);
            } catch (CannotCompileException cce) {
                cce.printStackTrace();
            }
        }
    }
    
    /**
     * Create a constructor. This method simply records the input parameters in
     * appropriately named fields.
     * 
     * @param
     * @param classes
     */
    private void createConstructor(CtClass concreteClass, CtClass[] parameters)
            throws Exception {

        CtConstructor ctCons = new CtConstructor(parameters, concreteClass);
        String constructorBody = "{";
        constructorBody += " this.reset(); ";
        for (int i = 0; i < parameters.length; i++) {
            String parameterName = paramNameFromClassName(parameters[i]);

            
            int paramNumber = i + 1;
            constructorBody += parameterName + "=$" + paramNumber + ";";

        }
        constructorBody += "}";
        ctCons.setBody(constructorBody);
        concreteClass.addConstructor(ctCons);

    }

    private static String paramNameFromClassName(CtClass parameter) {

        String parameterName = parameter.getName();
        parameterName = parameterName
                .substring(parameterName.lastIndexOf(".") + 1);
        if (parameterName.indexOf('[') != -1) {
            parameterName = parameterName.substring(0, parameterName
                    .indexOf('['));
        }
        String firstCharLowerCase = parameterName.substring(0, 1).toLowerCase();
        parameterName = firstCharLowerCase.concat(parameterName.substring(1));
        return parameterName;
    }

    /**
     * Reset the usage parameters.
     */
    private void generateResetMethod(CtClass ctClass) throws Exception {
        String body = "public synchronized void reset() {";
        for (Iterator it = this.generatedFields.iterator(); it.hasNext();) {
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

    private void generateSampleMethod(CtClass ctClass,
            String sampleParameterName) throws Exception {
        String body = "public " + SampleStatisticsImpl.class.getName() + " "
                + sampleParameterName + "SampleStatistics ( ) { ";
        body += " return new " + SampleStatisticsImpl.class.getName() + "("
                + sampleParameterName + "Mean," + sampleParameterName + "Max , "
                + sampleParameterName + "Min, " + sampleParameterName
                + "Count ); " + " } ";
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
            paramName = methodName.substring("increment".length());
            String firstCharLowerCase = paramName.substring(0, 1).toLowerCase();
            paramName = firstCharLowerCase.concat(paramName.substring(1));
            
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
            paramName = methodName.substring("sample".length());
            String firstCharLowerCase = paramName.substring(0, 1).toLowerCase();
            paramName = firstCharLowerCase.concat(paramName.substring(1));
            
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
        
        //	checks if the parameter is a valid java identifier
        if (Strings.isJavaKeyword(paramName))
            throw new DeploymentException(
                    "Usage parameter concrete class generator is a Java keyword!");

        if (!Strings.isValidJavaIdentifier(paramName))
            throw new DeploymentException(
                    "Usage parameter concrete class generator, invalid Java identifier!");

        if (logger.isDebugEnabled())
            logger.debug("Generating usage method = " + methodName);
        
        String body = "";
        String getterBody = "";


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
            logger.debug("body = " + getterBody);
            CtMethod newmethod = CtNewMethod.make(getterBody, ctClass);
            ctClass.addMethod(newmethod);

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
                    + "= new " + SampleStatisticsImpl.class.getName()
                    + "(this." + paramName + "Mean, " + "this." + paramName
                    + "Min, " + "this." + paramName + "Max, " + "this."
                    + paramName + "Count);";
            getterBody += "if (reset == true) {this." + paramName + "Count=0;";
            getterBody += "this." + paramName + "Sum = 0;";
            getterBody += "this." + paramName + "Mean = 0;";
            getterBody += "this." + paramName + "Value = 0;";
            getterBody += "this." + paramName + "Min = Long.MAX_VALUE ;";
            getterBody += "this." + paramName + "Max = Long.MIN_VALUE ;}";
            getterBody += "return tempValue;";
            getterBody += "}";
            logger.debug("body = " + getterBody);
            CtMethod newmethod = CtNewMethod.make(getterBody, ctClass);
            ctClass.addMethod(newmethod);
            
        } else {
            return;
        }

        logger.debug("body = " + body);
        CtMethod newmethod = CtNewMethod.make(body, ctClass);
        ctClass.addMethod(newmethod);

        if (methodName.startsWith("sample")) {
            this.generateSampleMethod(ctClass, paramName);
        }

    }

}

