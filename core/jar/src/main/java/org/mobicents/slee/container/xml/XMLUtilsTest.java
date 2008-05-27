/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/


/*
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.xml;

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

    private String   sbbJarXml =
         "<?xml version=\"1.0\"?>"
        +"<!DOCTYPE sbb-jar PUBLIC \"-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN\" \"http://java.sun.com/dtd/slee-sbb-jar_1_0.dtd\">"
        +"<sbb-jar>"
        +"    <sbb>"
        +"        <description>JCC Call Forwarding SBB for JCC 1.0a</description>"
        +"        <sbb-name>JCC Call Forwarding SBB</sbb-name>"
        +"        <sbb-vendor>The Open Source Community</sbb-vendor>"
        +"        <sbb-version>1.0</sbb-version>"
        +""
        +"        <profile-spec-ref>"
        +"            <profile-spec-name>CallForwardingProfile</profile-spec-name>"
        +"            <profile-spec-vendor>The Open Source Community</profile-spec-vendor>"
        +"            <profile-spec-version>1.0</profile-spec-version>"
        +"            <profile-spec-alias>CFP</profile-spec-alias>"
        +"        </profile-spec-ref>"
        +""
        +"        <sbb-classes>"
        +"            <sbb-abstract-class>"
        +"                <sbb-abstract-class-name>com.opencloud.slee.services.jcc.callforwarding.CallForwardingSbb</sbb-abstract-class-name>"
        +"                <get-profile-cmp-method>"
        +"                    <profile-spec-alias-ref>CFP</profile-spec-alias-ref>"
        +"                    <get-profile-cmp-method-name>getProfile</get-profile-cmp-method-name>"
        +"                </get-profile-cmp-method>"
        +"            </sbb-abstract-class>"
        +"        </sbb-classes>"
        +""
        +"        <event event-direction=\"Receive\" initial-event=\"True\">"
        +"            <event-name>CallDeliveryEvent</event-name>"
        +"            <event-type-ref>"
        +"                <event-type-name>javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT</event-type-name>"
        +"                <event-type-vendor>javax.csapi.cc.jcc</event-type-vendor>"
        +"                <event-type-version>1.1</event-type-version>"
        +"            </event-type-ref>"
        +"            <initial-event-select variable=\"AddressProfile\" />"
        +"            <event-resource-option>block</event-resource-option>"
        +"        </event>"
        +"    </sbb>"
        +"    <sbb>"
        +"        <description>JCC Call Blocking SBB for JCC 1.0a</description>"
        +"        <sbb-name>JCC Call Blocking SBB</sbb-name>"
        +"        <sbb-vendor>The Open Source Community</sbb-vendor>"
        +"        <sbb-version>1.0</sbb-version>"
        +""
        +"        <profile-spec-ref>"
        +"            <profile-spec-name>CallBlockingProfile</profile-spec-name>"
        +"            <profile-spec-vendor>The Open Source Community</profile-spec-vendor>"
        +"            <profile-spec-version>1.0</profile-spec-version>"
        +"            <profile-spec-alias>CBP</profile-spec-alias>"
        +"        </profile-spec-ref>"
        +""
        +"        <sbb-classes>"
        +"            <sbb-abstract-class>"
        +"                <sbb-abstract-class-name>com.opencloud.slee.services.jcc.callblocking.CallBlockingSbb</sbb-abstract-class-name>"
        +"                <get-profile-cmp-method>"
        +"                    <profile-spec-alias-ref>CBP</profile-spec-alias-ref>"
        +"                    <get-profile-cmp-method-name>getProfile</get-profile-cmp-method-name>"
        +"                </get-profile-cmp-method>"
        +"            </sbb-abstract-class>"
        +"        </sbb-classes>"
        +""
        +"        <event event-direction=\"Receive\" initial-event=\"True\">"
        +"            <event-name>CallDeliveryEvent</event-name>"
        +"            <event-type-ref>"
        +"                <event-type-name>javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT</event-type-name>"
        +"                <event-type-vendor>javax.csapi.cc.jcc</event-type-vendor>"
        +"                <event-type-version>1.1</event-type-version>"
        +"            </event-type-ref>"
        +"            <initial-event-select variable=\"AddressProfile\" />"
        +"            <event-resource-option>block</event-resource-option>"
        +"        </event>"
        +"    </sbb>"
        +"</sbb-jar>";

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
        InputSource sbbJarSource = new InputSource(new StringReader(sbbJarXml));

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
