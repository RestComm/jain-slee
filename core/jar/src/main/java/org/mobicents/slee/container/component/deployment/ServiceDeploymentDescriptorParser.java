/*
* The Open SLEE project
*
* The source code contained in this file is in in the public domain.
* It can be used in any project or product without prior permission,
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/
package org.mobicents.slee.container.component.deployment;

import org.w3c.dom.Element;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.MobicentsServiceDescriptorInternalImpl;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.management.xml.*;

/**
 * The parser class for Service Deployment Descriptors.
 *
 * @author Emil Ivov
 */
public class ServiceDeploymentDescriptorParser
    extends AbstractDeploymentDescriptorParser {


    public ServiceDeploymentDescriptorParser() {
    }


    public ServiceDescriptorImpl parseServiceComponent(Element serviceNode, ServiceDescriptorImpl descriptorImpl)
        throws XMLException
    {
        ComponentKey serviceKey = createKey(serviceNode);
        Element rootSbbNode = XMLUtils.getChildElement(serviceNode, XMLConstants.ROOT_SBB_ND);
      
        // EMIL -- I had to add sbb because the enclosing element is root-sbb
        ComponentKey rootSbb = createKey(rootSbbNode,"sbb");
        String priority = XMLUtils.getElementTextValue(serviceNode, XMLConstants.DEFAULT_PRIORITY_ND);
        byte defaultPriority = 0;
        try
        {
            defaultPriority = Byte.parseByte(priority);
        }
        catch(NumberFormatException nfe)
        {
            throw new XMLException("Default priority value of service must "
                                   +"be between -128 and 127 (inclusive): "
                                   + priority + " see section(8.5.7)", nfe);
        }

        String addressProfileTable = XMLUtils.getElementTextValue(serviceNode, XMLConstants.ADDRESS_PROFILE_TABLE_ND);
        assertNonZeroLength(addressProfileTable, XMLConstants.ADDRESS_PROFILE_TABLE_ND);
        String resourceInfoProfileTable = XMLUtils.getElementTextValue(serviceNode, XMLConstants.RESOURCE_INFO_PROFILE_TABLE_ND);
        assertNonZeroLength(resourceInfoProfileTable, XMLConstants.RESOURCE_INFO_PROFILE_TABLE_ND);

       
        ServiceIDImpl serviceId = new ServiceIDImpl(serviceKey);
        descriptorImpl.setServiceID(serviceId);
        
        
        //RANGA - I should give you the Sbb ComponentKey here (i.e. name version and vendor) and you should find your id at some point
	// EMIL -- lets discuss the above. the following line
	// was commented out (why?)

        descriptorImpl.setRootSbb(new SbbIDImpl(rootSbb));

        descriptorImpl.setDefaultPriority(defaultPriority);
        ((MobicentsServiceDescriptorInternalImpl)descriptorImpl).setAddressProfileTable(addressProfileTable);
        ((MobicentsServiceDescriptorInternalImpl)descriptorImpl).setResourceInfoProfileTable(resourceInfoProfileTable);

        return descriptorImpl;
    }

}
