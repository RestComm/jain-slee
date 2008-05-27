/*
* The Open SLEE project
*
* The source code contained in this file is in in the public domain.
* It can be used in any project or product without prior permission,
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/
package org.mobicents.slee.container.component.deployment;

import junit.framework.*;

import org.mobicents.slee.container.component.MobicentsEventTypeDescriptorInternalImpl;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptor;
import org.mobicents.slee.container.management.xml.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.util.*;
import java.io.*;
import org.mobicents.slee.container.*;

/**
 * @author Emil Ivov
 */

public class EventTypeDeploymentDescriptorParserTest extends TestCase {
    private EventTypeDeploymentDescriptorParser eventTypeDeploymentDescriptorParser = null;
    private Document                            eventJarDocument    = null;
    private Element                             eventDefinitionNode = null;
    private MobicentsEventTypeDescriptor             descriptorImpl = null;

    protected void setUp() throws Exception {
        super.setUp();
        eventTypeDeploymentDescriptorParser = new EventTypeDeploymentDescriptorParser();

        InputSource eventJarSource = new InputSource(new StringReader(XMLDescriptorStringsFixture.EVENT_JAR_XML));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        eventJarDocument =  builder.parse(eventJarSource);
        List sbbNodes = XMLUtils.getAllChildElements(eventJarDocument.getDocumentElement(), "event-definition");
        eventDefinitionNode = (Element)sbbNodes.get(0);

        descriptorImpl = new MobicentsEventTypeDescriptorInternalImpl();
    }

    protected void tearDown() throws Exception {
        eventTypeDeploymentDescriptorParser = null;
        super.tearDown();
    }

    public void testParse() throws XMLException {

        eventTypeDeploymentDescriptorParser.parse(eventDefinitionNode, descriptorImpl);

        assertEquals("Event type name didn't match", descriptorImpl.getName(), "com.foobar.event.HelpRequestedEvent");
        assertEquals("Event type vendor didn't match", descriptorImpl.getVendor(), "com.foobar");
        assertEquals("Event type version didn't match", descriptorImpl.getVersion(), "1.0a");

        assertEquals("Event class name didn't match", descriptorImpl.getEventClassName(), "com.foobar.event.HelpRequestedEvent");
    }

}
