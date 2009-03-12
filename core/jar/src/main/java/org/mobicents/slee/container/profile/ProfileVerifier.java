/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileVerifier.java
 * 
 * Created on 30 sept. 2004
 *
 */
package org.mobicents.slee.container.profile;

import java.util.Set;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javax.slee.profile.ProfileSpecificationDescriptor;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;

/**
 * Class verifying a profile
 * @deprecated
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 *  
 */
public class ProfileVerifier {
    /**
     * Logger to log information
     */
    private static Logger logger = Logger.getLogger(ProfileVerifier.class);

    /**
     * Pool to generate or read classes with javassist
     */
    private ClassPool pool = null;

    /**
     * CMPProfile Interface
     */
    private CtClass cmpProfileInterface = null;

    /**
     * profile Management Interface
     */
    private CtClass profileManagementInterface = null;

    /**
     * profile Management Abstract Class
     */
    private CtClass profileManagementAbstractClass = null;

    /**
     * javax.slee.profile.ProfileManagementInterface's javasist representation
     */
    private CtClass sleeProfileManagementInterface = null;

    /**
     * CMP Profile Interface name
     */
    private String cmpProfileInterfaceName = null;

    /**
     * Profile Management Interface name
     */
    private String profileManagementInterfaceName = null;

    /**
     * Profile Management abstract class name
     */
    private String managementAbstractClassName = null;

    /**
     * Profile Specification combination from the JSLEE spec 1.0 section 10.5.2
     */
    private int combination = -1;

    public ProfileVerifier(
            ProfileSpecificationDescriptor profileSpecificationDescriptor) {

        pool = ((DeployableUnitIDImpl) ((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
                    .getDeployableUnit()).getDUDeployer().getClassPool();
        
        cmpProfileInterfaceName = ((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
                .getCMPInterfaceName();
        profileManagementInterfaceName = ((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
                .getManagementInterfaceName();
        managementAbstractClassName = ((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
                .getManagementAbstractClassName();

        //check the combination from the JSLEE spec 1.0 section 10.5.2
        combination = checkCombination(cmpProfileInterfaceName,
                profileManagementInterfaceName, managementAbstractClassName);
    }

    /**
     * Verify that a profile specification provided by a sbb developer is
     * following a set of constraints.
     * 
     * @return true is the profile specification is safe
     */
    public boolean verifyProfileSpecification() {
        boolean verified = verifyCMPProfileInterface();

        if (!verified)
            return false;

        if (profileManagementInterfaceName != null) {

            verified = verifyManagementInterface();
            if (!verified)
                return false;
 
        }
        if (managementAbstractClassName != null) {
            verified = verifyManagementAbstractClass();

            if (!verified)
                return false;

        }
        return true;
    }

    /**
     * Check constraints specified in the section 10.6 of the JSLEE
     * specification
     * 
     * @return true if the CMP Profile Interface provided by a Sbb developer is
     *         safe
     */
    private boolean verifyCMPProfileInterface() {

        if ( logger.isDebugEnabled()) 
            logger.debug("classPool = " + pool);

        if (cmpProfileInterfaceName == null)
            return false;

        //Load the CMPProfile interface for reflective verification
        try {

            cmpProfileInterface = pool.get(cmpProfileInterfaceName);
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            return false;
        }

        if (!cmpProfileInterface.isInterface()) {
            logger.error(cmpProfileInterfaceName + " is not an interface.");
            return false;
        }

        if (!Modifier.isPublic(cmpProfileInterface.getModifiers())) {
            logger.error(cmpProfileInterfaceName + " is not public.");
            return false;
        }

        // make sure that all methods represent valid full access properties
        Set properties = ClassUtils.getCMPFields(cmpProfileInterface);
        

        //@@2.4+ -> 3.4+
        //int objectMethodsCount = Object.class.getMethods().length;
        CtClass oClass=null;
        try {
        	oClass=pool.get("java.lang.Object");
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
        //if ((properties.size() * 2) != (cmpProfileInterface.getMethods().length - objectMethodsCount-2)) return false;
        if ((properties.size() * 2) != (cmpProfileInterface.getMethods().length - oClass.getMethods().length)) return false;

        return true;
    }

    /**
     * Check constraints specified in the section 10.7 of the JSLEE
     * specification
     * 
     * @return true if the Management Interface provided by a Sbb developer is
     *         safe
     */
    private boolean verifyManagementInterface() {
        //TODO add more check for Management interfaces extending other
        // Management Interfaces
        if (profileManagementInterfaceName == null) {
            logger.error("Profile Management Interface name is null");
            return false;
        }
        //Load the Mangement Interface interface for reflective verification
        try {
            profileManagementInterface = pool
                    .get(profileManagementInterfaceName);
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            return false;
        }
        if (!profileManagementInterface.isInterface()) {
            logger.error(profileManagementInterfaceName
                    + " is not an interface.");
            return false;
        }
        if (!Modifier.isPublic(profileManagementInterface.getModifiers())) {
            logger.error(profileManagementInterfaceName + " is not public.");
            return false;
        }
        CtMethod[] managementMethods = profileManagementInterface
                .getDeclaredMethods();
        CtMethod[] cmpProfileMethods = cmpProfileInterface.getDeclaredMethods();
        for (int i = 0; i < managementMethods.length; i++) {
            //none of the method name should start with profile
            String methodName = managementMethods[i].getName();
            if (methodName.startsWith("profile")) {
                logger.error("Method " + methodName + " starts with profile.");
                return false;
            }
            //check the accessors if they have the same name that in the CMP
            // Profile
            //interface, they must have the signature
            if (methodName.startsWith("get") || methodName.startsWith("set")) {
                for (int j = 0; j < cmpProfileMethods.length; j++) {
                    String cmpProfileMethodName = cmpProfileMethods[j]
                            .getName();
                    if (cmpProfileMethodName.equals(methodName)) {
                        if (!cmpProfileMethods[j].equals(managementMethods[i])) {
                            logger
                                    .error("Method "
                                            + methodName
                                            + " "
                                            + "already present in the CMP Profile interface "
                                            + cmpProfileInterfaceName
                                            + " with a different signature.");
                            return false;
                        } else {
                            //check throws clause
                            try {
                                CtClass[] exceptionTypes = managementMethods[i]
                                        .getExceptionTypes();
                                for (int k = 0; k < exceptionTypes.length; k++) {
                                    logger.debug(exceptionTypes[k].getName());
                                }
                                if (exceptionTypes.length > 0) {
                                    logger.error("Method " + methodName
                                            + " has a throws clause.");
                                    return false;
                                }
                            } catch (NotFoundException nfe) {
                            }
                        }
                    }
                }
            }
            //check that any of the methods defined in the management interface
            //is not also defined in the javax.slee.profile.ProfileMBean
            CtClass profileMBean = null;
            try {
                profileMBean = pool.get("javax.slee.profile.ProfileMBean");
            } catch (NotFoundException nfe) {
                nfe.printStackTrace();
                return false;
            }
            CtMethod[] profileMBeanMethods = profileMBean.getDeclaredMethods();
            for (int j = 0; j < profileMBeanMethods.length; j++) {
                if (managementMethods[i].equals(profileMBeanMethods[j])) {
                    logger
                            .error("Method "
                                    + methodName
                                    + " already defined in javax.slee.profile.ProfileMBean");
                    return false;
                }

            }
            //check that any of the methods defined in the anagement interface
            //is not also defined in the javax.management.DynamicMBean
            CtClass dynamicMBean = null;
            try {
                dynamicMBean = pool.get("javax.management.DynamicMBean");
            } catch (NotFoundException nfe) {
                nfe.printStackTrace();
                return false;
            }
            CtMethod[] dynamicMBeanMethods = dynamicMBean.getDeclaredMethods();
            for (int j = 0; j < dynamicMBeanMethods.length; j++) {
                if (managementMethods[i].equals(dynamicMBeanMethods[j])) {
                    logger
                            .error("Method "
                                    + methodName
                                    + " already defined in javax.management.DynamicMBean");
                    return false;
                }

            }
            //check that any of the methods defined in the anagement interface
            //is not also defined in the javax.management.MBeanRegistration
            CtClass registrationMBean = null;
            try {
                registrationMBean = pool
                        .get("javax.management.MBeanRegistration");
            } catch (NotFoundException nfe) {
                nfe.printStackTrace();
                return false;
            }
            CtMethod[] registrationMBeanMethods = registrationMBean
                    .getDeclaredMethods();
            for (int j = 0; j < registrationMBeanMethods.length; j++) {
                if (managementMethods[i].equals(registrationMBeanMethods[j])) {
                    logger
                            .error("Method "
                                    + methodName
                                    + " already defined in javax.management.MBeanRegistration");
                    return false;
                }

            }
        }

        return true;
    }

    /**
     * Check constraints specified in the section 10.7 of the JSLEE
     * specification
     * 
     * @return true if the Management Abstract class provided by a Sbb developer
     *         is safe
     */
    private boolean verifyManagementAbstractClass() {
        //TODO add more check for Management abstract class extending other
        // Management abstract classes
        if (managementAbstractClassName == null) {
            logger.error("Profile Management Abstract class name is null");
            return false;
        }
        //Load the Mangement Abstract Class for reflective verification
        try {
            profileManagementAbstractClass = pool
                    .get(managementAbstractClassName);
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            return false;
        }
        if (!Modifier.isAbstract(profileManagementAbstractClass.getModifiers())) {
            logger.error(managementAbstractClassName
                    + " is not an abstract class.");
            return false;
        }
        if (!Modifier.isPublic(profileManagementAbstractClass.getModifiers())) {
            logger.error(managementAbstractClassName + " is not public.");
            return false;
        }
        CtClass[] interfaces = null;
        try {
            interfaces = profileManagementAbstractClass.getInterfaces();
            boolean cmpProfileInterfaceFound = false;
            boolean managementInterfaceFound = false;
            boolean sleeManagementInterfaceFound = false;
            for (int i = 0; i < interfaces.length; i++) {
                if (interfaces[i].getName().equals(
                        "javax.slee.profile.ProfileManagement"))
                    sleeManagementInterfaceFound = true;
                if (interfaces[i].getName().equals(cmpProfileInterfaceName))
                    cmpProfileInterfaceFound = true;
                if (profileManagementInterfaceName != null
                        && interfaces[i].getName().equals(
                                profileManagementInterfaceName)) {

                    managementInterfaceFound = true;
                }
            }

            if (!cmpProfileInterfaceFound || !sleeManagementInterfaceFound) {
                logger.error("A required interface is missing in the Profile "
                        + "management abstract class "
                        + managementAbstractClassName);
                return false;
            }
            if (profileManagementInterfaceName != null
                    && !managementInterfaceFound) {
                logger
                        .error("The profileManagement Interface "
                                + profileManagementInterfaceName
                                + " defined in the descriptor muste be implemented by Profile "
                                + "management abstract class "
                                + managementAbstractClassName);
                return false;
            }
            if (managementInterfaceFound) {
                CtMethod[] managementMethods = profileManagementInterface
                        .getDeclaredMethods();
                CtMethod[] methods = profileManagementAbstractClass
                        .getDeclaredMethods();
                CtMethod[] cmpProfileMethods = cmpProfileInterface
                        .getDeclaredMethods();
                for (int i = 0; i < managementMethods.length; i++) {

                    boolean methodFound = false;
                    int j = 0;
                    while (!methodFound && j < methods.length) {
                        if (ClassUtils.isValidOverride(managementMethods[i], methods[j])) {
                            methodFound = true;
                        } else {
                            j++;
                        }
                    }
                    if (!methodFound) {
                        boolean methodInCMPProfileFound = false;
                        int k = 0;
                        while (!methodInCMPProfileFound
                                && k < cmpProfileMethods.length) {
                            if (ClassUtils.isValidOverride(cmpProfileMethods[k], managementMethods[i])) {
                                methodInCMPProfileFound = true;
                            } else {
                                k++;
                            }
                        }
                        if (!methodInCMPProfileFound) {
                            logger
                                    .error("the profile management method "
                                            + managementMethods[i].getName()
                                            + " defined in the management interface "
                                            + profileManagementInterfaceName
                                            + " is not implemented in the abstract class "
                                            + managementAbstractClassName);
                            return false;
                        }
                    } else {
                        int modifiers = methods[j].getModifiers();
                        if (Modifier.isAbstract(modifiers)
                                || Modifier.isFinal(modifiers)
                                || Modifier.isStatic(modifiers)) {

                            logger
                                    .error("the implementation management method "
                                            + methods[j]
                                            + " cannot be declared abstract, final or static");
                            return false;
                        }
                        if (!Modifier.isPublic(modifiers)) {
                            logger
                                    .error("the implementation management method "
                                            + methods[j]
                                            + " must be declared public");
                            return false;
                        }
                    }
                }
            }
        } catch (NotFoundException nfe) {
            logger.error("The abstract class " + managementAbstractClassName
                    + " does not implements an interface");
            return false;
        }

        CtMethod[] implementationMethods = profileManagementAbstractClass
                .getDeclaredMethods();
        try {
            sleeProfileManagementInterface = pool
                    .get("javax.slee.profile.ProfileManagement");

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            return false;
        }
        CtMethod[] sleeManagementMethods = sleeProfileManagementInterface
                .getDeclaredMethods();
        CtMethod[] methods = profileManagementAbstractClass
                .getDeclaredMethods();
        for (int i = 0; i < sleeManagementMethods.length; i++) {
            String methodName = sleeManagementMethods[i].getName();

            boolean methodFound = false;
            int j = 0;
            while (!methodFound && j < methods.length) {
                if (ClassUtils.isValidOverride(sleeManagementMethods[i], methods[j])) {
                    methodFound = true;
                } else {
                    j++;
                }
            }
            if (!methodFound) {
                if (methodName.equals("profileInitialize")
                        || methodName.equals("profileLoad")
                        || methodName.equals("profileStore")
                        || methodName.equals("profileVerify")) {

                    logger.error(sleeManagementMethods[i].getName()
                            + " not implemented in "
                            + "profile management abstract class "
                            + managementAbstractClassName);
                    return false;
                }
            } else {
                int modifiers = methods[j].getModifiers();

                if (methodName.equals("profileInitialize")
                        || methodName.equals("profileLoad")
                        || methodName.equals("profileStore")
                        || methodName.equals("profileVerify")) {

                    if (Modifier.isAbstract(modifiers)
                            || Modifier.isFinal(modifiers)
                            || Modifier.isStatic(modifiers)) {

                        logger
                                .error("the implementation management method "
                                        + methods[j].getName()
                                        + " cannot be declared abstract, final or static");
                        return false;
                    }
                    if (!Modifier.isPublic(modifiers)) {
                        logger.error("the implementation management method "
                                + methods[j].getName()
                                + " must be declared public");
                        return false;
                    }
                } else {
                    if (!Modifier.isAbstract(modifiers)) {
                        logger.error("the implementation management method "
                                + methods[j].getName()
                                + " must be declared abstract");
                        return false;
                    }
                    if (!Modifier.isPublic(modifiers)) {
                        logger.error("the implementation management method "
                                + methods[j].getName()
                                + " must be declared public");
                        return false;
                    }

                }
            }
        }

        CtMethod[] cmpProfileMethods = cmpProfileInterface.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();
            boolean methodFound = false;
            int j = 0;
            while (!methodFound && j < cmpProfileMethods.length) {
                if (ClassUtils.isValidOverride(cmpProfileMethods[j], methods[i])) {
                    methodFound = true;
                } else {
                    j++;
                }
            }

            if (methodFound) {
                int modifiers = cmpProfileMethods[j].getModifiers();
                if (!Modifier.isAbstract(modifiers)) {
                    logger.error("the implementation management method "
                            + cmpProfileMethods[j].getName()
                            + " must be declared abstract");
                    return false;
                }
                if (!Modifier.isPublic(modifiers)) {
                    logger.error("the implementation management method "
                            + cmpProfileMethods[j].getName()
                            + " must be declared public");
                    return false;
                }
            }

        }

        return true;
    }

    /**
     * Check which combination (see JSLEE 1.0 spec section 10.5.2) matches the
     * Sbb Developer's Profile Specification
     * 
     * @param profileCMPInterfaceName
     *            name of the Profile CMP interface
     * @param profileManagementInterfaceName
     *            name of the Profile Management interface
     * @param profileManagementAbstractClassName
     *            name of the Profile Management Abstract class
     * @return the number of the combination (see JSLEE 1.0 spec section
     *         10.5.2), -1 if it doesn't match no combination
     */
    public static int checkCombination(String profileCMPInterfaceName,
            String profileManagementInterfaceName,
            String profileManagementAbstractClassName) {

        //if the Profile Specification has no Profile CMP interface, it is
        // incorrect
        if (profileCMPInterfaceName == null)
            return -1;

        if (profileManagementInterfaceName == null
                && profileManagementAbstractClassName == null) {
            if (logger.isDebugEnabled()) {
            	logger
                    .debug("The Profile Specification provided by the Sbb Developer "
                            + "is the combination 1 of JSLEE 1.0 spec section 10.5.2");
            }
            return 1;
        }
        if (profileManagementInterfaceName != null
                && profileManagementAbstractClassName == null) {
        	if (logger.isDebugEnabled()) {
        		logger
                    .debug("The Profile Specification provided by the Sbb Developper "
                            + "is the combination 2 of JSLEE 1.0 spec section 10.5.2");
        	}
            return 2;
        }
        if (profileManagementInterfaceName == null
                && profileManagementAbstractClassName != null) {
        	if (logger.isDebugEnabled()) {
        		logger
                    .debug("The Profile Specification provided by the Sbb Developper "
                            + "is the combination 3 of JSLEE 1.0 spec section 10.5.2");
        	}
            return 3;
        }
        if (profileManagementInterfaceName != null
                && profileManagementAbstractClassName != null) {
        	if (logger.isDebugEnabled()) {
        		logger
                    .debug("The Profile Specification provided by the Sbb Developper "
                            + "is the combination 4 of JSLEE 1.0 spec section 10.5.2");
        	}
            return 4;
        }
        return -1;
    }
}