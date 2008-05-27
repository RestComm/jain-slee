/*
 * ProfileDeployerTest.java
 * 
 * Created on 14 déc. 2004
 *
 */
package org.mobicents.slee.container.profile;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.deployment.ProfileSpecificationDescriptorParser;
import org.mobicents.slee.container.management.xml.XMLUtils;

import org.apache.log4j.BasicConfigurator;
import org.jboss.logging.Log4jLoggerPlugin;
import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

/**
 * This code is the property of Atos Origin
 * 
 * @author <a href="mailto:Jean.Deruelle@atosorigin.com">Jean Deruelle</a>
 */
public class ProfileDeployerTest extends TestCase {
    private ProfileSpecificationDescriptorParser profileSpecificationDescriptorParser;
    private Document profileJarDocument;
    private Element profileNode;
    private ProfileSpecificationDescriptorImpl profileSpecificationDescriptorImpl;
    public static String PROFILE_SPECIFICATION_XML =
        "<?xml version=\"1.0\"?>" + "\n" +
        "<profile-spec-jar>" + "\n" +
        "<description>" + "\n" +
        "    This XML file defines a non-standard profile specification described" + "\n" +
        "    in the SLEE specification." + "\n" +
        "</description>" + "\n" +
        "<profile-spec>" + "\n" +
        "    <description>" + "\n" +
        "        Profile specification for the profile test FooProfile" + "\n" +
        "    </description>" + "\n" +
        "    <profile-spec-name>FooProfileSpec</profile-spec-name>" + "\n" +
        "    <profile-spec-vendor>org.mobicents.slee.container.profile</profile-spec-vendor>" + "\n" +
        "    <profile-spec-version>1.0</profile-spec-version>" + "\n" +
        "    <profile-classes>" + "\n" +
        "        <profile-cmp-interface-name>" + "\n" +
        "            org.mobicents.slee.container.profile.FooProfileCMP" + "\n" +
        "        </profile-cmp-interface-name>" + "\n" +
        "        <profile-management-interface-name>" + "\n" +
        "            org.mobicents.slee.container.profile.FooProfileManagement" + "\n" +
        "        </profile-management-interface-name>" + "\n" +
        "        <profile-management-abstract-class-name>" + "\n" +
        "            org.mobicents.slee.container.profile.FooProfileManagementImpl" + "\n" +
        "        </profile-management-abstract-class-name>" + "\n" +
        "    </profile-classes>" + "\n" +
        "    <profile-index unique=\"True\" >subscriberName</profile-index>" + "\n" +
        "</profile-spec>" + "\n" +
        "</profile-spec-jar>" + "\n";    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        BasicConfigurator.configure();
        Logger.setPluginClassName(Log4jLoggerPlugin.class.getName());
        profileSpecificationDescriptorParser = new ProfileSpecificationDescriptorParser();
        InputSource profileJarSource = new InputSource(new StringReader(
                PROFILE_SPECIFICATION_XML));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        profileJarDocument =  builder.parse(profileJarSource);
        List profileNodes = XMLUtils.getAllChildElements(profileJarDocument.getDocumentElement(), "profile-spec");
        profileNode = (Element)profileNodes.get(0);

        profileSpecificationDescriptorImpl = new ProfileSpecificationDescriptorImpl();          
        profileSpecificationDescriptorImpl = profileSpecificationDescriptorParser.
        	parseProfileComponent(profileNode, profileSpecificationDescriptorImpl);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        profileSpecificationDescriptorParser = null;
        super.tearDown();
    }

    /**
     * Constructor for ProfileDeployerTest.
     * @param arg0
     */
    public ProfileDeployerTest(String arg0) {
        super(arg0);
    }

    public void testDeployProfile() {
        try {
            ProfileDeployer profileDeployer=new ProfileDeployer();
            boolean result=profileDeployer.deployProfile(profileSpecificationDescriptorImpl);            
            assertEquals("Deployment failed",true,result);
            } catch (Exception ex ) {
                ex.printStackTrace();
            }
    }

}
