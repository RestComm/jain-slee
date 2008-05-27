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

import java.io.File;
import java.util.HashSet;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ManagementException;
import javax.slee.usage.SampleStatistics;
import javax.slee.usage.SbbUsageMBean;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.InstalledUsageParameterSet;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.management.jmx.SbbUsageMBeanImpl;

/**
 * Generator for the concrete usage usage parameter mbean.
 * @author F.Moggia
 * @author <a href="mailto:michele.laporta@gmail.com">Michele La Porta</a>
 */
public class ConcreteUsageParameterMBeanInterfaceGenerator {

    private static Logger logger;

    private ClassPool classPool;

    private HashSet generatedFields;

    private String usageParameterFieldName;

    private String mbeanInterfaceName;
    
    private MobicentsSbbDescriptor sbbDescriptor;

    static {
        logger = Logger.getLogger(ConcreteUsageParameterMBeanInterfaceGenerator.class);
        
    }

    public ConcreteUsageParameterMBeanInterfaceGenerator(MobicentsSbbDescriptor sbbDescriptor) {
        classPool = ((DeployableUnitIDImpl)sbbDescriptor.getDeployableUnit()).getDUDeployer().getClassPool();
        this.sbbDescriptor = sbbDescriptor;
        try {
        	File mobicentsSar = new File(SbbDeployer.getLibPath());
        	List filesJars = ConcreteClassGeneratorUtils.getJarsFileListing(mobicentsSar);

			for (int i = 0; i < filesJars.size(); i++) {
				File jar = (File) filesJars.get(i);
				classPool.appendClassPath(SbbDeployer.getLibPath()+ File.separatorChar + jar.getName());
			}
        	
        } catch (Exception e) {
            throw new RuntimeException("Could not find library!", e);
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
        constructorBody += "super($1, $2, $3, $4);";
        for (int i = 0; i < parameters.length; i++) {
            String parameterName = paramNameFromClassName(parameters[i]);
            //(ranga) FIXME -- this is fragile. Dont rely on the integers 4 and 3!
            if(i==4) this.usageParameterFieldName = parameterName ;
            if(i!= 3 ){
            try {
                CtField ctField = new CtField(parameters[i], parameterName,
                        concreteClass);
                ctField.setModifiers(Modifier.PRIVATE);
                concreteClass.addField(ctField);
            } catch (CannotCompileException cce) {
                cce.printStackTrace();
            }
            int paramNumber = i + 1;
            constructorBody += parameterName + "=$" + paramNumber + ";";
            }

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

    public Class generateConcreteUsageParameterMBeanInterface(
            ) throws Exception {
        String usageParamInterfaceName = sbbDescriptor
                .getUsageParametersInterface();
        if (usageParamInterfaceName == null)
            return null;
        ClassPath classPath = classPool.appendClassPath( sbbDescriptor.getDeploymentPath());
        String concreteMBeanInterfaceName = usageParamInterfaceName + "MBean";
        String concreteMBeanClassName = usageParamInterfaceName + "MBeanImpl";
        CtClass usageParamInterface = classPool.get(usageParamInterfaceName);
        CtClass usageMBeanInterface = classPool.get(SbbUsageMBean.class
                .getName());
        CtClass usageParamConcreteClass = classPool.get(usageParamInterfaceName+"Impl");
        CtClass[] parameter = {
                classPool.get(ServiceID.class.getName()), 
                classPool.get(SbbID.class.getName()), 
                classPool.get(String.class.getName()),
                classPool.get(String.class.getName()),
                classPool.get(sbbDescriptor.getUsageParameterClass().getName())};
        
        CtClass ctInterface=null;
        try{
        ctInterface=classPool.get(concreteMBeanInterfaceName).getClassPool().makeInterface(concreteMBeanInterfaceName);
        }catch(NotFoundException nfe)
        {
        	ctInterface= classPool.makeInterface(concreteMBeanInterfaceName);
        }
        ctInterface.addInterface(usageMBeanInterface);
        /*ConcreteClassGeneratorUtils.createInheritanceLink(ctInterface,
                this.classPool.get("java.lang.Object"));*/
        CtClass ctClass = null;
        try{
        ctClass= classPool.get(concreteMBeanClassName).getClassPool().makeClass(concreteMBeanClassName);
        }catch(NotFoundException nfe)
        {
        	ctClass= classPool.makeClass(concreteMBeanClassName);
        }
        ConcreteClassGeneratorUtils.createInheritanceLink(ctClass,
                classPool.get(SbbUsageMBeanImpl.class.getName()));
        
        ConcreteClassGeneratorUtils.createInterfaceLinks(ctClass,new CtClass[]{ctInterface});
        this.createConstructor(ctClass, parameter);
        
        
        CtClass implClassInterface = classPool
                .get(InstalledUsageParameterSet.class.getName());
        CtMethod[] methods = usageParamInterface.getMethods();

        for (int i = 0; i < methods.length; i++) {
            // Generate the concrete method.
            generateAbstractMethod(ctInterface, methods[i]);
            generateConcreteMethod(ctClass, methods[i]);
        }

        String sbbDeploymentPathStr = sbbDescriptor.getDeploymentPath();
//    	@@2.4+ -> 3.4+
        //classPool.writeFile(concreteMBeanInterfaceName, sbbDeploymentPathStr);
        classPool.get(concreteMBeanInterfaceName).writeFile(sbbDeploymentPathStr);
        classPool.get(concreteMBeanInterfaceName).detach();
        logger.debug("Writing file " + concreteMBeanInterfaceName);
//    	@@2.4+ -> 3.4+
        //classPool.writeFile(concreteMBeanClassName, sbbDeploymentPathStr);
        classPool.get(concreteMBeanClassName).writeFile(sbbDeploymentPathStr);
        classPool.get(concreteMBeanClassName).detach();
        logger.debug("Writing file " + concreteMBeanClassName);
        
        Class retval = Thread.currentThread().getContextClassLoader().loadClass(concreteMBeanInterfaceName);
        ctInterface.defrost();
        ctClass.defrost();
        classPool.removeClassPath(classPath);
        
        return retval;
        //return ctClass.toClass();

    }

    private void generateConcreteMethod(CtClass ctClass, CtMethod method) throws Exception{
        String methodName = method.getName();
        String body = "";
        
        if (methodName.startsWith("increment")) {
            body += "public long get" + methodName.substring("increment".length())
                    + "( boolean  reset ) throws " + ManagementException.class.getName() + " {"
                    + "return this."+this.usageParameterFieldName+".get" + methodName.substring("increment".length()
                            ) + "(reset);"
                    + "}";
            if ( logger.isDebugEnabled())
                 logger.debug("METHOD BODY " + body);
            CtMethod newmethod = CtNewMethod.make(body, ctClass);
            ctClass.addMethod(newmethod);
        } else if (methodName.startsWith("sample")) {
            body += "public " + SampleStatistics.class.getName() + " get" + methodName.substring("sample".length())
            + "( boolean  reset ) throws " + ManagementException.class.getName()+ " {"
            + "return this."+this.usageParameterFieldName+".get" + methodName.substring("sample".length()) + "(reset);"
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
        CtClass managementExceptionClass = this.classPool.get(ManagementException.class.getName());
        CtClass sampleStatisticsClass = this.classPool.get(SampleStatistics.class.getName());
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

