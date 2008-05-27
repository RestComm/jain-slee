package org.mobicents.slee.container.component.deployment;

import junit.framework.*;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.management.xml.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import java.io.*;
import javax.xml.parsers.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Emil Ivov
 * @version 1.0
 */

public class AbstractDeploymentDescriptorParserTest extends TestCase {

    private AbstractDeploymentDescriptorParser abstractDeploymentDescriptorParser = null;
    private Document                           sbbJarDocument = null;
    private Element                            sbbNode        = null;

    protected void setUp() throws Exception {
        super.setUp();
        abstractDeploymentDescriptorParser = new AbstractDeploymentDescriptorParser();
        InputSource sbbJarSource = new InputSource(new StringReader(XMLDescriptorStringsFixture.SBB_JAR_XML));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        sbbJarDocument =  builder.parse(sbbJarSource);
        List sbbNodes = XMLUtils.getAllChildElements(sbbJarDocument.getDocumentElement(), "sbb");
        sbbNode = (Element)sbbNodes.get(0);
    }

    protected void tearDown() throws Exception {
        abstractDeploymentDescriptorParser = null;
        sbbJarDocument = null;
        sbbNode = null;
        super.tearDown();
    }

    //----------------------------- UNIT TESTS ---------------------------------
    public void testAssertNonZeroLength() throws XMLException {
        String value = "";
        String elementName = "ELEMENT";

        try {
            abstractDeploymentDescriptorParser.assertNonZeroLength(value,
                elementName);
            fail("assertNonZeroLength did not throw an exception for a 0 length argument");
        }
        catch (XMLException ex) {
        }

        value="asdf";
        try {
            abstractDeploymentDescriptorParser.assertNonZeroLength(value,
                elementName);
        }
        catch (XMLException ex) {
            fail("assertNonZeroLength threw an exception for a non-0 length argument");
        }
    }

    public void testCreateKey() throws XMLException {
        Element node = sbbNode;
        ComponentKey expectedReturn = new ComponentKey("JCC Call Forwarding SBB",
                										"The Open Source Community",
                										"1.0"
                                                       );
        ComponentKey actualReturn = abstractDeploymentDescriptorParser.createKey(node);
        assertEquals("Malformed component key", expectedReturn, actualReturn);
    }

    public void testCreateKey1() throws XMLException {
        Element node = sbbNode;
        String componentPrefix = "sbb";
        ComponentKey expectedReturn = new ComponentKey("JCC Call Forwarding SBB",
                										"The Open Source Community",
                										"1.0"
               											);
        ComponentKey actualReturn = abstractDeploymentDescriptorParser.createKey(node, componentPrefix);
        assertEquals("return value", expectedReturn, actualReturn);
    }

}
