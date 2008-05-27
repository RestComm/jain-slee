/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileSpecificationDescriptorParserTest.java
 * 
 * Created on Dec 9, 2004
 *
 */
package org.mobicents.slee.container.component.deployment;

import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.management.xml.XMLDescriptorStringsFixture;
import org.mobicents.slee.container.management.xml.XMLUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.BasicConfigurator;
import org.jboss.logging.Log4jLoggerPlugin;
import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public class ProfileSpecificationDescriptorParserTest extends TestCase {

    private ProfileSpecificationDescriptorParser profileSpecificationDescriptorParser;
    private Document profileJarDocument;
    private Element profileNode;
    private ProfileSpecificationDescriptorImpl profileSpecificationDescriptorImpl;
    
    private static Logger logger = Logger.getLogger(ProfileSpecificationDescriptorParserTest.class);

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        BasicConfigurator.configure();
        Logger.setPluginClassName(Log4jLoggerPlugin.class.getName());
        profileSpecificationDescriptorParser = new ProfileSpecificationDescriptorParser();
        InputSource profileJarSource = new InputSource(new StringReader(XMLDescriptorStringsFixture.PROFILE_SPECIFICATION_XML));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        profileJarDocument =  builder.parse(profileJarSource);
        List profileNodes = XMLUtils.getAllChildElements(profileJarDocument.getDocumentElement(), "profile-spec");
        profileNode = (Element)profileNodes.get(0);

        profileSpecificationDescriptorImpl = new ProfileSpecificationDescriptorImpl();        
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        profileSpecificationDescriptorParser = null;
        super.tearDown();
    }

    /**
     * Constructor for ProfileSpecificationDescriptorParserTest.
     * @param arg0
     */
    public ProfileSpecificationDescriptorParserTest(String arg0) {
        super(arg0);
    }

    public void testParseProfileComponent() throws IOException, Exception {        
        profileSpecificationDescriptorImpl= 
            this.profileSpecificationDescriptorParser.parseProfileComponent(
                    profileNode,
                    profileSpecificationDescriptorImpl);                
        assertEquals("failed",profileSpecificationDescriptorImpl.getName(),"AddressProfileSpec");
        assertEquals("failed",profileSpecificationDescriptorImpl.getVendor(),"javax.slee");
        assertEquals("failed",profileSpecificationDescriptorImpl.getVersion(),"1.0");
        assertEquals("failed",profileSpecificationDescriptorImpl.getCMPInterfaceName(),"javax.slee.profile.AddressProfileCMP");
        //assertEquals("failed",profileSpecificationDescriptorImpl.getManagementInterfaceName(),"AddressProfileSpec");
        //assertEquals("failed",profileSpecificationDescriptorImpl.getManagementAbstractClassName(),"AddressProfileSpec");
        Map profileIndexes=profileSpecificationDescriptorImpl.getProfileIndexes();
        Boolean isUnique=(Boolean)profileIndexes.get("addresses");        
        assertNotNull("failed",isUnique);
        assertTrue(isUnique.booleanValue());
        logger.info("testParseProfileComponent Passed");        
    }

}
