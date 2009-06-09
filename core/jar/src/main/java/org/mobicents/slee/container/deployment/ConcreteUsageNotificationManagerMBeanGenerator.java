/*
 * Created on Mar 14, 2005
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.deployment;

import java.beans.Introspector;
import java.util.HashSet;
import java.util.Set;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewMethod;

import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.usage.UsageNotificationManagerMBean;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.management.jmx.UsageNotificationManagerMBeanImpl;

/**
 * Generator for the concrete usage usage parameter mbean.
 * @author F.Moggia
 * @author <a href="mailto:michele.laporta@gmail.com">Michele La Porta</a>
 * @author martins
 */
public class ConcreteUsageNotificationManagerMBeanGenerator {

    private static Logger logger= Logger.getLogger(ConcreteUsageNotificationManagerMBeanGenerator.class);
    
    private final SleeComponentWithUsageParametersInterface component;

    public ConcreteUsageNotificationManagerMBeanGenerator(SleeComponentWithUsageParametersInterface component) {
    	this.component = component;        
    }
    
    /**
	 * Create a constructor. This method simply records the input parameters in
	 * appropriately named fields.
	 * 
	 * @param
	 * @param classes
	 */
    private void createConstructor(CtClass concreteClass, CtClass usageMBeanInterface, CtClass notificationSource,CtClass usageComponent)
            throws Exception {

        CtConstructor ctCons = new CtConstructor(new CtClass[]{usageMBeanInterface,notificationSource,usageComponent}, concreteClass);
        ctCons.setBody("{ super($1,$2,$3); }");
        concreteClass.addConstructor(ctCons);

    }
    
    public void generateConcreteUsageNotificationManagerMBean(
            ) throws Exception {
    	
    	Class<?> interfaceClass = component.getUsageParametersInterface();
        if (interfaceClass == null)
            return;
        
        ClassPool classPool = component.getClassPool();
       
        String usageParamInterfaceName = interfaceClass.getName();
        CtClass usageParamInterface = classPool.get(usageParamInterfaceName);
        
        CtClass usageMBeanInterface = classPool.get(UsageNotificationManagerMBean.class
                .getName());
        CtClass usageMBeanImplClass = classPool.get(UsageNotificationManagerMBeanImpl.class
                .getName());
                	
        // generate the mbean interface
        String concreteMBeanInterfaceName = usageParamInterfaceName + "NotificationManagerMBean";
        CtClass ctInterface = classPool.makeInterface(concreteMBeanInterfaceName);
		ctInterface.addInterface(usageMBeanInterface);
        // generate the mbean class
		String concreteMBeanClassName = usageParamInterfaceName + "NotificationManagerMBeanImpl";
        CtClass ctClass = classPool.makeClass(concreteMBeanClassName);
        ctClass.setSuperclass(usageMBeanImplClass);
        ConcreteClassGeneratorUtils.createInterfaceLinks(ctClass,new CtClass[]{ctInterface});
        
        if (logger.isDebugEnabled()) {
        	logger.debug("generating "+concreteMBeanInterfaceName+" and "+concreteMBeanClassName);
        }
        
        // create constructor
        this.createConstructor(ctClass, classPool.get(Class.class.getName()),classPool.get(NotificationSource.class
                .getName()),classPool.get(SleeComponentWithUsageParametersInterface.class
                        .getName()));
                
        CtMethod[] methods = usageParamInterface.getMethods();

        Set<String> userParams = new HashSet<String>();
        for (CtMethod ctMethod : methods) {
        	if (!ctMethod.getDeclaringClass().getName().equals(Object.class.getName())) {
        		if (ctMethod.getName().startsWith("increment")) {
        			userParams.add(ctMethod.getName().substring("increment".length()));
        		}
        		else if (ctMethod.getName().startsWith("sample")) {
        			userParams.add(ctMethod.getName().substring("sample".length()));
        		}
        		else if (ctMethod.getName().startsWith("get")) {
        			userParams.add(ctMethod.getName().substring("get".length()));
        		}            
        	}
        }
        
        for(String userParam : userParams) {        	
        	generateInterfaceMethod(ctInterface, userParam);
            generateConcreteMethod(ctClass, userParam);           
        }

        String deploymentPathStr = component.getDeploymentDir().getAbsolutePath();
        ctInterface.writeFile(deploymentPathStr);
        logger.debug("Writing file " + concreteMBeanInterfaceName);
        ctClass.writeFile(deploymentPathStr);
        logger.debug("Writing file " + concreteMBeanClassName);
                
        component.setUsageNotificationManagerMBeanConcreteInterface(Thread.currentThread().getContextClassLoader().loadClass(concreteMBeanInterfaceName));
        component.setUsageNotificationManagerMBeanImplConcreteClass(Thread.currentThread().getContextClassLoader().loadClass(concreteMBeanClassName));
       
        ctInterface.defrost();
        ctClass.defrost();
    }

    private void generateConcreteMethod(CtClass ctClass, String userParamName) throws Exception{
        
        String userParamNameLowerCase = Introspector.decapitalize(userParamName);
        
        String getterBody = "public boolean get" + userParamName + "NotificationsEnabled"
                    + "() throws " + ManagementException.class.getName() + " {"
                    + "return getNotificationsEnabled(\""+userParamNameLowerCase+"\");"
                    + "}";
        if ( logger.isDebugEnabled())
        	logger.debug("getNotificationsEnabled method for user param "+userParamName+" :\n" + getterBody);
        CtMethod getterMethod = CtNewMethod.make(getterBody, ctClass);
        ctClass.addMethod(getterMethod);

        String setterBody = "public void set" + userParamName + "NotificationsEnabled"
        + "(boolean enabled) throws " + ManagementException.class.getName() + " {"
        + " setNotificationsEnabled(\""+userParamNameLowerCase+"\",enabled);"
        + "}";
        if ( logger.isDebugEnabled())
        	logger.debug("setNotificationsEnabled method for user param "+userParamName+" :\n" + setterBody);
        CtMethod setterMethod = CtNewMethod.make(setterBody, ctClass);
        ctClass.addMethod(setterMethod);
    }
    
    private void generateInterfaceMethod(CtClass ctClass,  String userParamName)
            throws Exception {
            	
        ClassPool classPool = component.getClassPool();
        CtClass managementExceptionClass = classPool.get(ManagementException.class.getName());

       // add the getter method
       CtClass[] getterParameters = {};
       CtClass[] getterExceptions = {managementExceptionClass};
       CtMethod getterMethod = CtNewMethod.abstractMethod(CtClass.booleanType, 
                    "get" + userParamName + "NotificationsEnabled", 
                    getterParameters, 
                    getterExceptions, 
                    ctClass); 
       ctClass.addMethod(getterMethod);
       // add the setter method
       CtClass[] setterParameters = {CtClass.booleanType};
       CtClass[] setterExceptions = {managementExceptionClass};
       CtMethod setterMethod = CtNewMethod.abstractMethod(CtClass.voidType, 
                    "set" + userParamName + "NotificationsEnabled", 
                    setterParameters, 
                    setterExceptions, 
                    ctClass); 
       ctClass.addMethod(setterMethod);
    }
    
    
}

