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
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.ClassPool;
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
        
        if (logger.isDebugEnabled()) {
        	logger.debug("generating "+concreteMBeanInterfaceName+" and "+concreteMBeanClassName);
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
        logger.debug("Writing file " + concreteMBeanInterfaceName);
        ctClass.writeFile(deploymentPathStr);
        logger.debug("Writing file " + concreteMBeanClassName);
                
        component.setUsageParametersMBeanConcreteInterface(Thread.currentThread().getContextClassLoader().loadClass(concreteMBeanInterfaceName));
        component.setUsageParametersMBeanImplConcreteClass(Thread.currentThread().getContextClassLoader().loadClass(concreteMBeanClassName));
       
        ctInterface.defrost();
        ctClass.defrost();
        
        // slee 1.1. components also need a notification manager
        if (component.isSlee11()) {
        	new ConcreteUsageNotificationManagerMBeanGenerator(component).generateConcreteUsageNotificationManagerMBean();
        }
    }

    private void generateConcreteMethod(CtClass ctClass, CtMethod method) throws Exception{
        String methodName = method.getName();
        String body = "";
        
        if (methodName.startsWith("increment")) {
            body += "public long get" + methodName.substring("increment".length())
                    + "( boolean  reset ) throws " + ManagementException.class.getName() + " {"
                    + "return (("+component.getUsageParametersConcreteClass().getName()+")getUsageParameter()).get" + methodName.substring("increment".length()
                            ) + "(reset);"
                    + "}";
            if ( logger.isDebugEnabled())
                 logger.debug("METHOD BODY " + body);
            CtMethod newmethod = CtNewMethod.make(body, ctClass);
            ctClass.addMethod(newmethod);
        } else if (methodName.startsWith("sample")) {
            body += "public " + SampleStatistics.class.getName() + " get" + methodName.substring("sample".length())
            + "( boolean  reset ) throws " + ManagementException.class.getName()+ " {"
            + "return (("+component.getUsageParametersConcreteClass().getName()+")getUsageParameter()).get" + methodName.substring("sample".length()) + "(reset);"
            + "}";
            logger.debug("METHOD BODY " + body);
            CtMethod newmethod = CtNewMethod.make(body, ctClass);
            ctClass.addMethod(newmethod);
            
        } else {
            return;
        }
    }
    
    private void generateAbstractMethod(CtClass ctClass, CtMethod method)
            throws Exception {

        String methodName = method.getName();
        String body = "";
        
        ClassPool classPool = component.getClassPool();
        CtClass managementExceptionClass = classPool.get(ManagementException.class.getName());
        CtClass sampleStatisticsClass = classPool.get(SampleStatistics.class.getName());
        CtMethod newMethod = null;
        

        if (methodName.startsWith("increment")) {
            CtClass[] parameters = {CtClass.booleanType};
            CtClass[] exceptions = {managementExceptionClass};
            newMethod = CtNewMethod.abstractMethod(CtClass.longType, 
                    "get" + methodName.substring("increment".length()), 
                    parameters, 
                    exceptions, 
                    ctClass); 
            /*
             * body += "public long get" + methodName.substring(9) + "( boolean
             * reset ) throws " + ManagementException.class+ "";
             */
        } else if (methodName.startsWith("sample")) {
            CtClass[] parameters = {CtClass.booleanType};
            CtClass[] exceptions = {managementExceptionClass};
            newMethod = CtNewMethod.abstractMethod(sampleStatisticsClass,
                    "get" + methodName.substring("sample".length()), 
                    parameters, 
                    exceptions, 
                    ctClass);    
            
            body += "public " + SampleStatistics.class.getName() + " " + methodName.substring("sample".length())
            + "( boolean  reset ) throws " + ManagementException.class+ ";";
            
        } else {
            return;
        }

        ctClass.addMethod(newMethod);

    }
    
    
}

