/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ConcreteProfileManagementGeneratorTest.java
 * 
 * Created on Oct 4, 2004
 *
 */
package org.mobicents.slee.container.profile;

import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;

import org.apache.log4j.BasicConfigurator;
import org.jboss.logging.Log4jLoggerPlugin;
import org.jboss.logging.Logger;

import junit.framework.TestCase;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public class ConcreteProfileManagementGeneratorTest extends TestCase {
    private Logger logger=Logger.getLogger(ConcreteProfileManagementGeneratorTest.class);;
    private ConcreteProfileManagementGenerator concreteProfileManagementGenerator=null;
    private ProfileSpecificationDescriptorImpl profileSpecificationDescriptor=null;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        BasicConfigurator.configure();
		Logger.setPluginClassName(Log4jLoggerPlugin.class.getName());
		profileSpecificationDescriptor=new ProfileSpecificationDescriptorImpl();
		profileSpecificationDescriptor.setCMPInterfaceName("org.mobicents.slee.container.profile.FooProfileCMP");
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        concreteProfileManagementGenerator=null;
    }

    /**
     * Constructor for ConcreteProfileManagementGeneratorTest.
     * @param arg0
     */
    public ConcreteProfileManagementGeneratorTest(String arg0) {
        super(arg0);
    }

    /*public void testGenerateConcreteProfileManagementClassFomCMPInterface() {
        concreteProfileManagementGenerator=
            new ConcreteProfileManagementGenerator(profileSpecificationDescriptor);
        Class clazz=concreteProfileManagementGenerator.
        	generateConcreteProfileCMP();        
        assertNotNull("Failed creating Profile", clazz);
    }*/
    
    public void testGenerateConcreteMBean() {
        profileSpecificationDescriptor.setManagementAbstractClassName("org.mobicents.slee.container.profile.FooProfileManagementImpl");
        profileSpecificationDescriptor.setManagementInterfaceName("org.mobicents.slee.container.profile.FooProfileManagement");
        concreteProfileManagementGenerator=
            new ConcreteProfileManagementGenerator(profileSpecificationDescriptor);
        Class interfaceClazz=concreteProfileManagementGenerator.
        	generateProfileMBeanInterface();
        assertNotNull("Failed creating ProfileMBean Interface", interfaceClazz);
        Class clazz=concreteProfileManagementGenerator.
    		generateConcreteProfileMBean();
        assertNotNull("Failed creating ProfileMBean Class", clazz);
        
    }

    public void testGenerateConcreteProfileManagementClassFomProfileManagementAbstractClass() {
        profileSpecificationDescriptor.setManagementAbstractClassName("org.mobicents.slee.container.profile.FooProfileManagementImpl");
        profileSpecificationDescriptor.setManagementInterfaceName("org.mobicents.slee.container.profile.FooProfileManagement");
        concreteProfileManagementGenerator=
            new ConcreteProfileManagementGenerator(profileSpecificationDescriptor);
        Class clazz=concreteProfileManagementGenerator.
        	generateConcreteProfileCMP();
            assertNotNull("Failed creating Profile", clazz);
    }

}
