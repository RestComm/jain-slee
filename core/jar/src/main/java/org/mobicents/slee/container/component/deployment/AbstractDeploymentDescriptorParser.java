/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.component.deployment;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.management.*;
import org.w3c.dom.*;
import org.mobicents.slee.container.management.xml.*;

/**
 * @author Emil Ivov
 * @version 1.0
 */

public class AbstractDeploymentDescriptorParser
{
    public AbstractDeploymentDescriptorParser()
    {
    }

    /**
     * Creates a component key from the specified XML node and componentPrefix.
     * @param node the node that represented the component
     * @param componentPrefix
     * @return a unique component key for the specified note
     */
    protected ComponentKey createKey(Element node, String componentPrefix)
        throws XMLException
    {
        /** @todo replace name verion and vendor with static strings */
        String name = XMLUtils.getElementTextValue( node, componentPrefix + "-name");
        String vendor = XMLUtils.getElementTextValue( node, componentPrefix + "-vendor");
        String version = XMLUtils.getElementTextValue( node, componentPrefix + "-version");
        ComponentKey key = new ComponentKey(name, vendor, version);

        return key;
    }


    /**
     * Creates a component key from the specified XML node and uses the name of
     * <code>node</code> as a component prefix.
     * @param node the node that represented the component
     * @return a unique component key for the specified note
     */
    protected ComponentKey createKey(Element node)
        throws XMLException
    {
        return createKey(node, node.getNodeName());
    }


    protected final void assertNonZeroLength(String value, String elementName)
        throws XMLException
    {
        if (value != null && value.length() == 0)
        {
            throw new XMLException(elementName + " had zero length");
        }
    }

}
