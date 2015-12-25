/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2014, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * This file incorporates work covered by the following copyright contributed under the GNU LGPL : Copyright 2007-2011 Red Hat.
 */
package org.mobicents.slee.container.deployment;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewMethod;

import javax.slee.management.DeploymentException;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.SbbNotification;
import javax.slee.usage.SampleStatistics;
import javax.slee.usage.SbbUsageMBean;
import javax.slee.usage.UsageMBean;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ClassPool;
import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.management.jmx.SbbUsageMBeanImpl;
import org.mobicents.slee.container.management.jmx.UsageMBeanImpl;

/**
 * Generator for the concrete usage usage parameter mbean.
 * @author F.Moggia
 * @author <a href="mailto:michele.laporta@gmail.com">Michele La Porta</a>
 * @author martins
 */
public class ConcreteUsageParameterMBeanGenerator {

    private static Logger logger= Logger.getLogger(ConcreteUsageParameterMBeanGenerator.class);
    
    private final SleeComponentWithUsageParametersInterface component;

    public ConcreteUsageParameterMBeanGenerator(SleeComponentWithUsageParametersInterface component) {
        this.component = component;        
    }
    
    /**
     * Create a constructor. This method simply records the input parameters in
     * appropriately named fields.
     * 
     * @param
     * @param classes
     */
    private void createConstructor(CtClass concreteClass, CtClass usageMBeanInterface, CtClass notificationSource)
            throws Exception {

        CtConstructor ctCons = new CtConstructor(new CtClass[]{usageMBeanInterface,notificationSource}, concreteClass);
        ctCons.setBody("{ super($1,$2); }");
        concreteClass.addConstructor(ctCons);

    }
    
    public void generateConcreteUsageParameterMBean(
            ) throws Exception {
        
        Class usageParamInterfaceClass = component.getUsageParametersInterface();
        if (usageParamInterfaceClass == null)
            return;
        
        ClassPool classPool = component.getClassPool();
       
        String usageParamInterfaceName = usageParamInterfaceClass.getName();
        CtClass usageParamInterface = classPool.get(usageParamInterfaceName);
        
        CtClass usageMBeanInterface = null;
        CtClass usageMBeanImplClass = null;
        CtClass constructorSecondParameterCtClass = null;
        if (component.isSlee11()) {
            usageMBeanInterface = classPool.get(UsageMBean.class
                    .getName());
            usageMBeanImplClass = classPool.get(UsageMBeanImpl.class
                    .getName());
            constructorSecondParameterCtClass = classPool.get(NotificationSource.class
                    .getName());            
        }
        else {
            if (component instanceof SbbComponent) {
                usageMBeanInterface = classPool.get(SbbUsageMBean.class
                        .getName());
                usageMBeanImplClass = classPool.get(SbbUsageMBeanImpl.class
                        .getName());
                constructorSecondParameterCtClass = classPool.get(SbbNotification.class.getName());
            }
            else {
                throw new DeploymentException("only sbb components can have usage param mbeans in slee 1.0, how did this pass validation?!? component = "+component);
            }
        }
            
        // generate the mbean interface
        String concreteMBeanInterfaceName = usageParamInterfaceName + "MBean";
        CtClass ctInterface = classPool.makeInterface(concreteMBeanInterfaceName);
        ctInterface.addInterface(usageMBeanInterface);
        // generate the mbean class
        String concreteMBeanClassName = usageParamInterfaceName + "MBeanImpl";
        CtClass ctClass = classPool.makeClass(concreteMBeanClassName);
        ctClass.setSuperclass(usageMBeanImplClass);
        ConcreteClassGeneratorUtils.createInterfaceLinks(ctClass,new CtClass[]{ctInterface});
        
        if (logger.isTraceEnabled()) {
            logger.trace("generating "+concreteMBeanInterfaceName+" and "+concreteMBeanClassName);
        }
        
        // create constructor
        this.createConstructor(ctClass, classPool.get(Class.class.getName()),constructorSecondParameterCtClass);
                
        CtMethod[] methods = usageParamInterface.getMethods();

        for (int i = 0; i < methods.length; i++) {
            // Generate the concrete method.
            generateAbstractMethod(ctInterface, methods[i]);
            generateConcreteMethod(ctClass, methods[i]);
        }

        String deploymentPathStr = component.getDeploymentDir().getAbsolutePath();
        ctInterface.writeFile(deploymentPathStr);
        logger.trace("Writing file " + concreteMBeanInterfaceName);
        ctClass.writeFile(deploymentPathStr);
        logger.trace("Writing file " + concreteMBeanClassName);
                
        component.setUsageParametersMBeanConcreteInterface(Thread.currentThread().getContextClassLoader().loadClass(concreteMBeanInterfaceName));
        component.setUsageParametersMBeanImplConcreteClass(Thread.currentThread().getContextClassLoader().loadClass(concreteMBeanClassName));
       
        ctInterface.defrost();
        ctClass.defrost();
        
        // slee 1.1. components also need a notification manager
        if (component.isSlee11()) {
            new ConcreteUsageNotificationManagerMBeanGenerator(component).generateConcreteUsageNotificationManagerMBean();
        }
    }

    private void generateConcreteMethod(CtClass ctClass, CtMethod method) throws Exception {
        String methodName = method.getName();
        if (methodName.startsWith("increment")) {
            String usageParameterName = methodName.substring("increment".length());
            String body = "public long get" + usageParameterName + "(boolean reset) throws " + ManagementException.class.getName() + " {"
                    + "return ((" + component.getUsageParametersConcreteClass().getName() + ")getUsageParameter()).get" + usageParameterName + "(reset);"
                    + "}";
            logger.trace("METHOD BODY " + body);
            ctClass.addMethod(CtNewMethod.make(body, ctClass));
            
            // exposing the usage parameter as a JMX attribute for easier read-only access, for example, via SNMP
            body = "public long get" + usageParameterName + "() throws " + ManagementException.class.getName() + " {"
                    + "return get" + usageParameterName + "(false);"
                    + "}";
            logger.trace("attribute getter METHOD BODY " + body);
            ctClass.addMethod(CtNewMethod.make(body, ctClass));
        } else if (methodName.startsWith("sample")) {
            String usageParameterName = methodName.substring("sample".length());
            String body = "public " + SampleStatistics.class.getName() + " get" + usageParameterName + "(boolean reset) throws "
                            + ManagementException.class.getName() + " {"
                            + "return ((" + component.getUsageParametersConcreteClass().getName() + ")getUsageParameter()).get"
                            + usageParameterName + "(reset);"
                            + "}";
            logger.trace("METHOD BODY " + body);
            ctClass.addMethod(CtNewMethod.make(body, ctClass));
        } else {
            return;
        }
    }
    
    private void generateAbstractMethod(CtClass ctClass, CtMethod method) throws Exception {
        String methodName = method.getName();
        ClassPool classPool = component.getClassPool();
        CtClass managementExceptionClass = classPool.get(ManagementException.class.getName());
        CtClass sampleStatisticsClass = classPool.get(SampleStatistics.class.getName());
        CtMethod newMethod = null;

        if (methodName.startsWith("increment")) {
            CtClass[] parameters = {CtClass.booleanType};
            CtClass[] exceptions = {managementExceptionClass};
            String usageParameterName = methodName.substring("increment".length());
            newMethod = CtNewMethod.abstractMethod(CtClass.longType, "get" + usageParameterName, parameters, exceptions, ctClass);
            
            // exposing the usage parameter as a JMX attribute for easier read-only access, for example, via SNMP
            ctClass.addMethod(CtNewMethod.abstractMethod(CtClass.longType, "get" + usageParameterName, null, exceptions, ctClass));
        } else if (methodName.startsWith("sample")) {
            CtClass[] parameters = {CtClass.booleanType};
            CtClass[] exceptions = {managementExceptionClass};
            newMethod = CtNewMethod.abstractMethod(sampleStatisticsClass, "get" + methodName.substring("sample".length()), parameters, exceptions, ctClass);
        } else {
            return;
        }

        ctClass.addMethod(newMethod);
    }
}
