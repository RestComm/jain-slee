/*
* The Open SLEE project
*
* The source code contained in this file is in in the public domain.
* It can be used in any project or product without prior permission,
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/
package org.mobicents.slee.container.component.deployment;

import java.io.FileInputStream;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptorInternal;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptorInternalImpl;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptor;
import org.mobicents.slee.container.management.xml.*;

/**
 * @author Emil Ivov
 */

public class EventTypeDeploymentDescriptorParser extends AbstractDeploymentDescriptorParser {
    
    /**
     * Parse file containing the standard event types. This is called on slee startup.
     * 
     * @param sourceUrl
     */

    public static List parseStandardEvents(String urlForEventJar)
            throws Exception {
        FileInputStream reader;
        byte[] buffer;
        String eventJarXML = new String();

        reader = new FileInputStream(urlForEventJar);
        buffer = new byte[reader.available()];
        reader.read(buffer);
        eventJarXML = new String(buffer);
        reader.close();

        EventTypeDeploymentDescriptorParser parser = new EventTypeDeploymentDescriptorParser();
        InputSource eventJarSource = new InputSource(new StringReader(
                eventJarXML));
        Document eventJarDocument = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        builder = factory.newDocumentBuilder();
        eventJarDocument = builder.parse(eventJarSource);

        List sbbNodes = XMLUtils.getAllChildElements(eventJarDocument
                .getDocumentElement(), "event-definition");
        LinkedList retval = new LinkedList();
        for (int i = 0; i < sbbNodes.size(); i++) {
        	MobicentsEventTypeDescriptorInternalImpl descriptorImpl = new MobicentsEventTypeDescriptorInternalImpl();
            Element eventDefinitionNode = (Element) sbbNodes.get(i);

            parser.parse(eventDefinitionNode, descriptorImpl);

            retval.add(descriptorImpl);
        }
        return retval;

    }

    public MobicentsEventTypeDescriptor parse(Element eventDefinitionNode, MobicentsEventTypeDescriptor descriptorImpl)
        throws XMLException
    {
        ComponentKey eventTypeKey = createKey(eventDefinitionNode, XMLConstants.EVENT_TYPE_PREFIX);
        ((MobicentsEventTypeDescriptorInternal)descriptorImpl).setComponentKey(eventTypeKey);

        String eventClassName = XMLUtils.getElementTextValue(
                     eventDefinitionNode, XMLConstants.EVENT_CLASS_NAME_ND);

        ((MobicentsEventTypeDescriptorInternal)descriptorImpl).setEventClassName(eventClassName);
        return descriptorImpl;
    }
}
