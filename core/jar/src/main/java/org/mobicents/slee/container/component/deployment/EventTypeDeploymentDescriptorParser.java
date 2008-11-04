/*
* The Open SLEE project
*
* The source code contained in this file is in in the public domain.
* It can be used in any project or product without prior permission,
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/
package org.mobicents.slee.container.component.deployment;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptor;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptorInternal;
import org.mobicents.slee.container.management.xml.XMLConstants;
import org.mobicents.slee.container.management.xml.XMLException;
import org.mobicents.slee.container.management.xml.XMLUtils;
import org.w3c.dom.Element;

/**
 * @author Emil Ivov
 */

public class EventTypeDeploymentDescriptorParser extends AbstractDeploymentDescriptorParser {
    
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
