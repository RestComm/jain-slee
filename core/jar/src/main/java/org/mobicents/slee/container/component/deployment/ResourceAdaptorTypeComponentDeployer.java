
/*
 *ResourceAdaptorTypeComponentDeployer
 *
 * Created on December, 2004, 1:36 PM
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 *
 */

package org.mobicents.slee.container.component.deployment;




import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.management.xml.XMLConstants;
import org.mobicents.slee.container.management.xml.XMLUtils;
import org.mobicents.slee.resource.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.resource.ResourceAdaptorTypeDescriptorImpl;

import java.io.IOException;
import java.util.jar.*;

import javax.slee.management.*;
import javax.slee.resource.ResourceAdaptorDescriptor;

import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.*;

/** Component deployer for Resource Adaptor Component type.
 * 
 *@author M. Ranganathan.
 */


class ResourceAdaptorTypeComponentDeployer extends AbstractComponentDeployer{
    
    private DeployableUnitIDImpl deployableUnitID;
    private JarEntry ddXmlEntry;
    private ResourceAdaptorTypeDescriptorParser parser;
    private static Logger logger;
    
    
    
    static {
        logger = Logger.getLogger(ResourceAdaptorTypeComponentDeployer.class);
    }
    
    
    public ResourceAdaptorTypeComponentDeployer(DeployableUnitIDImpl deployableUnitID, JarEntry ddXmlEntry) {
        this.deployableUnitID = deployableUnitID;
        this.ddXmlEntry = ddXmlEntry;
        parser = new ResourceAdaptorTypeDescriptorParser();
    }

    /**
     * Parses the specified document into the corresponding ComponentDescriptor.
     *
     * @param document the deployment descriptor of the component
     */
    protected List parseComponentDescriptors() throws Exception {
      
        logger.debug("Parsing a Resource Adaptor from " + getJar().getName());
        JarFile jar = getJar();

        //Get the Sbb deployment Descriptor;
        if(ddXmlEntry == null){
            throw new DeploymentException(
                "No SbbDeploymentDescriptor descriptor "
                +"(META-INF/resource-adaptor-type-jar.xml) was found in "
                + jar.getName());
        }
               
        //Parse the descriptor
        Document doc = null;
        try {
            doc = XMLUtils.parseDocument(jar.getInputStream(ddXmlEntry), true);
        }
        catch (IOException ex) {
            throw new DeploymentException("Failed to extract the SBB deployment"
                +" descriptor from " + jar.getName());
        }

        Element raJarNode = doc.getDocumentElement();
        ArrayList retval = new ArrayList();

        //Get a list of the jars and services in the deployable unit.
        List raTypeNodes = XMLUtils.getAllChildElements(raJarNode, XMLConstants.RESOURCE_ADAPTOR_TYPE_ND);
        String description = XMLUtils.getElementTextValue(raJarNode, XMLConstants.DESCRIPTION_ND);
        if ( raTypeNodes != null ) {
            for ( Iterator it  = raTypeNodes.iterator(); it.hasNext(); ) {
                ResourceAdaptorTypeDescriptorImpl raTypeDescriptor = new ResourceAdaptorTypeDescriptorImpl();
                parser.parseResourceAdaptorTypeDescriptor((Element)it.next(),raTypeDescriptor);
                raTypeDescriptor.setDeployableUnit(this.deployableUnitID);
                this.deployableUnitID.setDescription(description);
                retval.add(raTypeDescriptor);
            }
        }
        return retval;
    }
}
