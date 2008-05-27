/*
 * Created on Dec 3, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.component.deployment;

import org.mobicents.slee.container.component.MobicentsEventTypeDescriptorInternalImpl;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptor;
import org.mobicents.slee.container.management.xml.XMLConstants;
import org.mobicents.slee.container.management.xml.XMLException;
import org.mobicents.slee.container.management.xml.XMLUtils;

import javax.slee.management.*;

import java.io.IOException;
import java.util.jar.*;

import org.jboss.logging.Logger;
import org.w3c.dom.*;
import java.util.*;

/** Deployer for the event jar.
 * 
 *@author M. Ranganathan
 *
 */

class EventTypeComponentDeployer extends AbstractComponentDeployer {
    private static Logger logger;
    private EventTypeDeploymentDescriptorParser parser;
    private DeployableUnitIDImpl deployableUnitID;
    private JarEntry ddXmlEntry;
    static {
        logger = Logger.getLogger(EventTypeComponentDeployer.class);
    }
    public EventTypeComponentDeployer(DeployableUnitIDImpl deployableUnitID, JarEntry ddXmlEntry) {
        if ( deployableUnitID == null ) throw new NullPointerException ("null deployable unit!");
        parser = new EventTypeDeploymentDescriptorParser();
        this.deployableUnitID = deployableUnitID;
        this.ddXmlEntry = ddXmlEntry;
    }

    /**
     * Parses the specified document into the corresponding ComponentDescriptor.
     *
     * @param document the deployment descriptor of the component
     */
    protected List parseComponentDescriptors()  throws DeploymentException {
        LinkedList retval = new LinkedList();
        logger.debug("parsing event-jar from " + getJar().getName());
        JarFile jar = getJar();
       
        //Parse the descriptor
        Document doc = null;
        try {
            doc = XMLUtils.parseDocument(jar.getInputStream(ddXmlEntry), true);
          
        }
        catch (IOException ex) {
            throw new DeploymentException("Failed to extract the SBB deployment"
                +" descriptor from " + jar.getName());
        }
        
        Element docElement = doc.getDocumentElement();
        try {
            String description = XMLUtils.getElementTextValue(docElement,XMLConstants.DESCRIPTION_ND);
            deployableUnitID.setDescription(description);
        } catch (XMLException e) {
            e.printStackTrace();
        }
        List nodes = XMLUtils.getAllChildElements(docElement, XMLConstants.EVENT_DEFINITION_ND);
        for (int i = 0; i < nodes.size(); i++) {
        	MobicentsEventTypeDescriptorInternalImpl descriptorImpl = new MobicentsEventTypeDescriptorInternalImpl();
            Element eventDefinitionNode = (Element) nodes.get(i);
            try {
                parser.parse(eventDefinitionNode, descriptorImpl);
            } catch (XMLException e2) {
                throw new DeploymentException(
                        "Error Parsing Event file " + jar.getName(), e2);
            }
            descriptorImpl.setDeployableUnit(deployableUnitID);
            retval.add(descriptorImpl);
        }


        return retval;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.management.AbstractComponentDeployer#checkDeployment()
     */
    protected void checkDeployment() throws Exception {
        //TODO -- check to see if the event classes are there.
        
    }

}
