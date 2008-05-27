/*
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.management.xml;

import junit.framework.*;
import java.io.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.util.*;
import javax.xml.parsers.*;

/**
 * @author Emil Ivov
 * @version 1.0
 */

public class XMLUtilsTest extends TestCase
{
    private XMLUtils xmlUtils  = null;
    private Document sbbJarDocument  = null;

    public XMLUtilsTest(String name) {
        super(name);
    }

    /**
     * Initialize the fixture.
     * @throws Exception if setup fails
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        xmlUtils = new XMLUtils();
        InputSource sbbJarSource = new InputSource(new StringReader(XMLDescriptorStringsFixture.SBB_JAR_XML));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        sbbJarDocument =  builder.parse(sbbJarSource);
    }

    protected void tearDown() throws Exception
    {
        xmlUtils = null;
        super.tearDown();
    }

    public void testGetAllChildElements() throws IllegalArgumentException
    {
        Element parent = sbbJarDocument.getDocumentElement();
        String name = "sbb";

        List actualReturn = xmlUtils.getAllChildElements(parent, name);

        assertEquals("Failed retriving node children", "sbb", ((Node)actualReturn.get(0)).getNodeName());
        assertEquals("Failed retriving node children", "sbb", ((Node)actualReturn.get(1)).getNodeName());
    }

    public void testGetChildElement() throws IllegalArgumentException,
        XMLException
    {
        Element parent = sbbJarDocument.getDocumentElement();
        String name = "sbb";
        boolean assertUnique = false;

        Element actualReturn = xmlUtils.getChildElement(parent, name,
            assertUnique);
        assertEquals("getChildElement did not return the expected node",
                     name,
                     actualReturn.getNodeName());

        assertUnique = true;
        try
        {
            actualReturn = xmlUtils.getChildElement(parent, name,
                assertUnique);
            fail("getChildElement did not properly assert node uniqueness");
        }
        catch (Exception ex)
        {
        }
    }

    public void testGetElementTextValue() throws IllegalArgumentException,
        XMLException
    {
        Element sbbNode = xmlUtils.getChildElement(sbbJarDocument.getDocumentElement(), "sbb", false);
        Element descriptionNode = xmlUtils.getChildElement(sbbNode, "description", true);

        String expectedReturn = "JCC Call Forwarding SBB for JCC 1.0a";
        String actualReturn = xmlUtils.getElementTextValue(descriptionNode);
        assertEquals("XML Node text value was not properly extracted",
                     expectedReturn,
                     actualReturn);
    }

    public void testGetElementTextValue1() throws XMLException
    {
        Element sbbNode = xmlUtils.getChildElement(sbbJarDocument.getDocumentElement(), "sbb", false);

        String expectedReturn = "JCC Call Forwarding SBB for JCC 1.0a";
        String actualReturn = xmlUtils.getElementTextValue(sbbNode, "description");
        assertEquals("XML Node text value was not properly extracted",
                     expectedReturn,
                     actualReturn);
    }

}
