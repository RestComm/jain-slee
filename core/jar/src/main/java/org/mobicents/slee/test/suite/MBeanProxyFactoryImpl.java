/*
 * MBeanProxyFactoryImpl.java
 * 
 * Created on Dec 13, 2004
 * 
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
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

package org.mobicents.slee.test.suite;

import java.lang.reflect.Constructor;
import java.util.StringTokenizer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javax.management.ObjectName;

import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.testutils.jmx.AlarmMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.DeploymentMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.MBeanProxyFactory;
import com.opencloud.sleetck.lib.testutils.jmx.ProfileMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.ProfileProvisioningMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.SbbUsageMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.ServiceManagementMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.ServiceUsageMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.SleeManagementMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.TraceMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.impl.AlarmMBeanProxyImpl;

/**
 * Generate the code for a deployment mbean proxy.
 *  
 */
public class MBeanProxyFactoryImpl implements MBeanProxyFactory {
    private static ClassPool pool;

    private static String packagePrefix = "org.mobicents.slee.test.suite";

    private static String directory = "classes";

    private JUnitSleeTestUtils utils;

    private static Class deploymentMBeanProxy;

    private static Class serviceManagementMBeanProxy;

    private static Class sleeManagementMBeanProxy;

    private static Class traceMBeanProxy;

    private static Class profileProvisioningMBeanProxy;

    private static Class profileMBeanProxy;

    private static Class serviceUsageMBeanProxy;
    
    private static Class sbbUsageMBeanProxy;
    
    private static Class alarmMBeanProxy;
    
    private static Class mbeanFacade;

    static {
        try {
            pool = ClassPool.getDefault();
            pool.appendClassPath("classes");
            pool.appendClassPath("lib/slee_1_1.jar");
            StringTokenizer st = new StringTokenizer(packagePrefix, ".");

            while (st.hasMoreTokens()) {
                String dirElement = st.nextToken(".");
                directory += "/" + dirElement;
            }
            // System.out.println("Directory " + directory);

        } catch (NotFoundException ex) {
            pool = null;
        }
    }

    public MBeanProxyFactoryImpl(JUnitSleeTestUtils utils) {
        this.utils = utils;

    }

    private static String getImplClassName(Class interfaceClass) {
        return packagePrefix
                + "."
                + interfaceClass.getName().substring(
                        interfaceClass.getName().lastIndexOf(".") + 1) + "Impl";
    }

    public DeploymentMBeanProxy createDeploymentMBeanProxy(ObjectName objectName) {
        try {
            if (deploymentMBeanProxy == null) {
                deploymentMBeanProxy = generateMBeanProxy(packagePrefix,
                        DeploymentMBeanProxy.class);
//            	@@2.4+ -> 3.4+
                //pool.writeFile(getImplClassName(DeploymentMBeanProxy.class),directory);
                pool.get(getImplClassName(DeploymentMBeanProxy.class)).writeFile(directory);
                pool.get(getImplClassName(DeploymentMBeanProxy.class)).detach();
            }

            Constructor cons = deploymentMBeanProxy.getConstructor(new Class[] {
                    ObjectName.class, JUnitSleeTestUtils.class });
            return (DeploymentMBeanProxy) cons.newInstance(new Object[] {
                    objectName, utils });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ServiceManagementMBeanProxy createServiceManagementMBeanProxy(
            ObjectName objectName) {
        try {
            if (serviceManagementMBeanProxy == null) {
                serviceManagementMBeanProxy = generateMBeanProxy(packagePrefix,
                        ServiceManagementMBeanProxy.class);
//            	@@2.4+ -> 3.4+
                //pool.writeFile(getImplClassName(ServiceManagementMBeanProxy.class), directory);
                pool.get(getImplClassName(ServiceManagementMBeanProxy.class)).writeFile(directory);
                pool.get(getImplClassName(ServiceManagementMBeanProxy.class)).detach();

            }

            Constructor cons = serviceManagementMBeanProxy
                    .getConstructor(new Class[] { ObjectName.class,
                            JUnitSleeTestUtils.class });
            return (ServiceManagementMBeanProxy) cons.newInstance(new Object[] {
                    objectName, utils });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SleeManagementMBeanProxy createSleeManagementMBeanProxy(
            ObjectName objectName) {
        try {
            if (sleeManagementMBeanProxy == null) {
                sleeManagementMBeanProxy = generateMBeanProxy(packagePrefix,
                        SleeManagementMBeanProxy.class);
//            	@@2.4+ -> 3.4+
                //pool.writeFile(getImplClassName(SleeManagementMBeanProxy.class), directory);
                pool.get(getImplClassName(SleeManagementMBeanProxy.class)).writeFile(directory);
                pool.get(getImplClassName(SleeManagementMBeanProxy.class)).detach();
            }

            Constructor cons = sleeManagementMBeanProxy
                    .getConstructor(new Class[] { ObjectName.class,
                            JUnitSleeTestUtils.class });
            return (SleeManagementMBeanProxy) cons.newInstance(new Object[] {
                    objectName, utils });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public TraceMBeanProxy createTraceMBeanProxy(ObjectName objectName) {
        try {
            if (traceMBeanProxy == null) {
                traceMBeanProxy = generateMBeanProxy(packagePrefix,
                        TraceMBeanProxy.class);
//            	@@2.4+ -> 3.4+
                //pool.writeFile(getImplClassName(TraceMBeanProxy.class),directory);
                pool.get(getImplClassName(TraceMBeanProxy.class)).writeFile(directory);
                pool.get(getImplClassName(TraceMBeanProxy.class)).detach();
            }

            Constructor cons = traceMBeanProxy.getConstructor(new Class[] {
                    ObjectName.class, JUnitSleeTestUtils.class });
            return (TraceMBeanProxy) cons.newInstance(new Object[] {
                    objectName, utils });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

   /* public MBeanFacade createMBeanFacade( ObjectName objectName ) {
        try { 
            if ( mbeanFacade == null ) {
                mbeanFacade = generateMBeanProxy( packagePrefix, MBeanFacade.class );
                pool.writeFile( getImplClassName(MBeanFacade.class),directory);
            }
            Constructor cons = mbeanFacade.getConstructor(new Class[] {
                    ObjectName.class, JUnitSleeTestUtils.class });
            return (MBeanFacade) cons.newInstance(new Object[] {
                    objectName, utils });
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }*/
    /**
     * @param name
     * @return
     */
    public ProfileProvisioningMBeanProxy createProfileProvisioningMBeanProxy(
            ObjectName objectName) {
        try {
            if (profileProvisioningMBeanProxy == null) {
                profileProvisioningMBeanProxy = generateMBeanProxy(
                        packagePrefix, ProfileProvisioningMBeanProxy.class);
//            	@@2.4+ -> 3.4+
                //pool.writeFile(getImplClassName(ProfileProvisioningMBeanProxy.class),directory);
                pool.get(getImplClassName(ProfileProvisioningMBeanProxy.class)).writeFile(directory);
                pool.get(getImplClassName(ProfileProvisioningMBeanProxy.class)).detach();
            }

            Constructor cons = profileProvisioningMBeanProxy
                    .getConstructor(new Class[] { ObjectName.class,
                            JUnitSleeTestUtils.class });
            return (ProfileProvisioningMBeanProxy) cons
                    .newInstance(new Object[] { objectName, utils });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param name
     * @return
     */
    public ProfileMBeanProxy createProfileMBeanProxy(ObjectName objectName) {
        try {
            if (profileMBeanProxy == null) {
                profileMBeanProxy = generateMBeanProxy(packagePrefix,
                        ProfileMBeanProxy.class);
//            	@@2.4+ -> 3.4+
                //pool.writeFile(getImplClassName(ProfileMBeanProxy.class), directory);
                pool.get(getImplClassName(ProfileMBeanProxy.class)).writeFile(directory);
                pool.get(getImplClassName(ProfileMBeanProxy.class)).detach();
            }

            Constructor cons = profileMBeanProxy.getConstructor(new Class[] {
                    ObjectName.class, JUnitSleeTestUtils.class });
            return (ProfileMBeanProxy) cons.newInstance(new Object[] {
                    objectName, utils });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Generate a proxy for the Mbean.
     */
    private Class generateMBeanProxy(String destinationPackageName,
            Class mbeanProxyInterfaceClass) throws Exception {

        if (pool == null)
            throw new Exception("Could not find class path");
        String classNameToBeGenerated = getImplClassName(mbeanProxyInterfaceClass);
        
        CtClass ctClass = null;
        try
        {
        	ctClass=pool.get(classNameToBeGenerated).getClassPool().makeClass(classNameToBeGenerated);
        }catch(NotFoundException nfe)
        {
        	ctClass =  pool.makeClass(classNameToBeGenerated);
        }
        CtClass[] params = new CtClass[] {
                pool.get(ObjectName.class.getName()),
                pool.get(JUnitSleeTestUtils.class.getName()) };
        createConstructor(ctClass, params);
        CtClass interfaceClass = pool.get(mbeanProxyInterfaceClass.getName());
        // The return class should implement the given interface.
        ctClass.addInterface(interfaceClass);
        CtMethod[] methods = interfaceClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (!methods[i].getName().equals("equals")
                    && !methods[i].getName().equals("clone")
                    && !methods[i].getName().equals("toString")
                    && !methods[i].getName().equals("finalize")
                    && !methods[i].getName().equals("hashCode")
                    && !methods[i].getName().equals("getClass")
                    && !methods[i].getName().equals("wait")
                    && !methods[i].getName().equals("notify")
                    && !methods[i].getName().equals("notifyAll"))
                genMethod(methods[i], ctClass);
        }
        return ctClass.toClass();
    }

    /**
     * Generate a method that throws the right exception type
     * 
     * @param method
     * @param ctClass
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    private void genMethod(CtMethod method, CtClass ctClass)
            throws NotFoundException, CannotCompileException {
       
        CtClass[] exceptions = method.getExceptionTypes();
        CtClass returnType = method.getReturnType();
        CtClass[] parameterTypes = method.getParameterTypes();

        String methodName = method.getName();
        String utils = paramNameFromClassName(pool.get(JUnitSleeTestUtils.class
                .getName()));
        String oname = paramNameFromClassName(pool.get(ObjectName.class
                .getName()));

        String body = "public " + returnType.getName() + " " + method.getName()
                + "(";
        for (int i = 0; i < parameterTypes.length; i++) {
            CtClass param = parameterTypes[i];
            String className = param.getName();
            body += className;
            body += " ";
            body += paramNameFromClassName(param, i);
            if (i != parameterTypes.length - 1) {
                body += ",";
            }
        }
        body += ") \n";
        if (exceptions.length != 0) {

            body += " throws ";
            for (int i = 0; i < exceptions.length; i++) {
                body += exceptions[i].getName();
                if (i != exceptions.length - 1) {
                    body += " , ";
                } else
                    body += "\n ";
            }
        }

        body += " {\n";
       // if (exceptions.length != 0)
            body += "try { \n";
        if (parameterTypes.length == 0) {
            body += "Object retval = ";
            body += utils + ".invokeOperation(" + oname + ",\"" + methodName
                    + "\",(Object[])null,(String[])null);  \n";
        } else {
            body += "String[] sig =  new String[" + parameterTypes.length
                    + "];";
            for (int i = 0; i < parameterTypes.length; i++) {
                if (!parameterTypes[i].isArray()) {
                    body += "sig[";
                    body += i;
                    body += "] =";
                    body += "\"";
                    body += parameterTypes[i].getName();
                    body += "\";\n";
                } else {
                    body += "sig[";
                    body += i;
                    body += "] =";
                    body += "\"[L";
                    body += parameterTypes[i].getComponentType().getName();
                    body += ";\";\n";
                }

                //parameterTypes[i].getComponentType().
            }

            body += " Object[] args = new Object[" + parameterTypes.length
                    + "];";

            for (int i = 0; i < parameterTypes.length; i++) {
                int parmNumber = i + 1;
                body += "args[";
                body += i;
                body += "] = ";
                // body += "$" + parmNumber;
                body += paramNameFromClassName(parameterTypes[i], i);
                body += "; \n";

            }

            body += "Object retval = ";
            body += utils + ".invokeOperation(" + oname + "," + "\""
                    + methodName + "\"," + "args, sig);\n";

        }
        if (returnType != CtClass.voidType) {
            if (returnType == CtClass.booleanType)
                body += " return ((Boolean) retval).booleanValue();";
            else if (returnType == CtClass.intType)
                body += " return ((Integer) retval).intValue();";
            else if (returnType == CtClass.longType)
                body += "return ((Long) retval).longValue();";
            else if (returnType == CtClass.byteType)
                body += "return ((Byte) retval).byteValue();";
            else if (returnType == CtClass.floatType)
                body += "return ((Float) retval).floatValue();";
            body += " return " + "(" + returnType.getName() + ") retval; \n ";
        }
       // if (exceptions.length != 0) 
            body += " }\n ";
        body += " catch ( Exception ex ) {\n";
       // body += "ex.printStackTrace(); \n";
        body += " Exception cause = (Exception) ex.getCause();\n";
        for (int i = 0; i < exceptions.length; i++) {
            body += " if  ( cause instanceof " + exceptions[i].getName() + ")";
            body += "throw  (" + exceptions[i].getName() + ") (cause);\n";

        }
        body += "throw new " + TCKTestErrorException.class.getName()
                + "(\"unexpected exception\",cause);\n";

        body += " } \n";

        if (returnType != CtClass.voidType) {
            if (returnType == CtClass.booleanType)
                body += "return false;";
            else if (returnType == CtClass.byteType
                    || returnType == CtClass.charType
                    || returnType == CtClass.doubleType
                    || returnType == CtClass.longType
                    || returnType == CtClass.intType)
                body += "return 0;";
            else
                body += "return null;";
        }
        body += "} \n";
        // System.out.println("body = " + body);

        try {
        CtMethod newMethod = CtNewMethod.make(body, ctClass);

        //CtMethod newMethod =
        // CtNewMethod.make(method.getReturnType(),method.getName(),
        // method.getParameterTypes(),method.getExceptionTypes(),body,ctClass);

        ctClass.addMethod(newMethod);
        } catch ( CannotCompileException ex ) {
            System.out.println("body = " + body);
            throw ex;
        }

    }

    private void createConstructor(CtClass proxy, CtClass[] parameters)
            throws Exception {
        CtConstructor ctCons = new CtConstructor(parameters, proxy);
        String constructorBody = "{";
        for (int i = 0; i < parameters.length; i++) {
            String parameterName = paramNameFromClassName(parameters[i]);

            try {
                CtField ctField = new CtField(parameters[i], parameterName,
                        proxy);
                ctField.setModifiers(Modifier.PRIVATE);
                proxy.addField(ctField);
            } catch (CannotCompileException cce) {
                cce.printStackTrace();
            }
            int paramNumber = i + 1;
            constructorBody += parameterName + "=$" + paramNumber + ";";

        }
        constructorBody += "}";
        ctCons.setBody(constructorBody);
        proxy.addConstructor(ctCons);

    }

    private static String paramNameFromClassName(CtClass parameter, int index) {
        return paramNameFromClassName(parameter) + index;
    }

    /**
     * @param class1
     * @return
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see com.opencloud.sleetck.lib.testutils.jmx.MBeanProxyFactory#createAlarmMBeanProxy(javax.management.ObjectName)
     */
    public AlarmMBeanProxy createAlarmMBeanProxy(ObjectName alarmMBeanName) {
        
        AlarmMBeanProxy alarmMBeanProxy = new AlarmMBeanProxyImpl(alarmMBeanName, utils.getMBeanFacade()); 
        return alarmMBeanProxy;
        /*
        try {
            if (alarmMBeanProxy == null) {
                alarmMBeanProxy = generateMBeanProxy(packagePrefix,
                        AlarmMBeanProxy.class);
                pool.writeFile(
                        getImplClassName(AlarmMBeanProxy.class),
                        directory);

            }

            Constructor cons = alarmMBeanProxy
                    .getConstructor(new Class[] { ObjectName.class,
                            JUnitSleeTestUtils.class });
            return (AlarmMBeanProxy) cons.newInstance(new Object[] {
                    objectName, utils });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        */
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.opencloud.sleetck.lib.testutils.jmx.MBeanProxyFactory#createSbbUsageMBeanProxy(javax.management.ObjectName)
     */
    public SbbUsageMBeanProxy createSbbUsageMBeanProxy(ObjectName objectName){
        
        try {
            if (sbbUsageMBeanProxy == null) {
                sbbUsageMBeanProxy = generateMBeanProxy(packagePrefix,
                        SbbUsageMBeanProxy.class);
//            	@@2.4+ -> 3.4+
                //pool.writeFile(getImplClassName(SbbUsageMBeanProxy.class),directory);
                pool.get(getImplClassName(SbbUsageMBeanProxy.class)).writeFile(directory);
                pool.get(getImplClassName(SbbUsageMBeanProxy.class)).detach();
            }

            Constructor cons = sbbUsageMBeanProxy
                    .getConstructor(new Class[] { ObjectName.class,
                            JUnitSleeTestUtils.class });
            return (SbbUsageMBeanProxy) cons.newInstance(new Object[] {
                    objectName, utils });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    
    
    
    

    /*
     * (non-Javadoc)
     * 
     * @see com.opencloud.sleetck.lib.testutils.jmx.MBeanProxyFactory#createServiceUsageMBeanProxy(javax.management.ObjectName)
     */
    public ServiceUsageMBeanProxy createServiceUsageMBeanProxy(
            ObjectName objectName) {
       
        try {
            if (serviceUsageMBeanProxy == null) {
                serviceUsageMBeanProxy = generateMBeanProxy(packagePrefix,
                        ServiceUsageMBeanProxy.class);
//            	@@2.4+ -> 3.4+
                //pool.writeFile(getImplClassName(ServiceUsageMBeanProxy.class),directory);
                pool.get(getImplClassName(ServiceUsageMBeanProxy.class)).writeFile(directory);
                pool.get(getImplClassName(ServiceUsageMBeanProxy.class)).detach();
            }

            Constructor cons = serviceUsageMBeanProxy
                    .getConstructor(new Class[] { ObjectName.class,
                            JUnitSleeTestUtils.class });
            return (ServiceUsageMBeanProxy) cons.newInstance(new Object[] {
                    objectName, utils });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}

