/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileVerifierTest.java
 * 
 * Created on Oct 10, 2004
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
public class ProfileVerifierTest extends TestCase {
    private Logger logger=null;
    private ProfileVerifier profileVerifier=null;
    private ProfileSpecificationDescriptorImpl profileSpecificationDescriptor=null;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        BasicConfigurator.configure();
		Logger.setPluginClassName(Log4jLoggerPlugin.class.getName());
		logger = Logger.getLogger(ProfileVerifierTest.class);
		profileSpecificationDescriptor=new ProfileSpecificationDescriptorImpl();
		profileSpecificationDescriptor.setCMPInterfaceName(
		    "org.mobicents.slee.container.profile.FooProfileCMP");
		profileSpecificationDescriptor.setManagementInterfaceName(
        	"org.mobicents.slee.container.profile.FooProfileManagement");
		profileSpecificationDescriptor.setManagementAbstractClassName(
		    "org.mobicents.slee.container.profile.FooProfileManagementImpl");
        
		profileVerifier=new ProfileVerifier(profileSpecificationDescriptor);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        profileVerifier=null;
    }

    public void testVerifyProfileSpecification() {
        
        boolean result=profileVerifier.verifyProfileSpecification();
        assertEquals("Profile Specification is not safe",result,true);
    }

}
