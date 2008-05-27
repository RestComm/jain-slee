package org.mobicents.slee.container.component.deployment;

import org.mobicents.slee.container.component.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.management.xml.XMLConstants;
import org.mobicents.slee.container.management.xml.XMLUtils;
import org.mobicents.slee.resource.ResourceAdaptorDescriptorImpl;

import java.io.IOException;
import java.util.jar.*;
import javax.slee.management.*;

import org.jboss.logging.Logger;
import org.w3c.dom.*;
import java.util.*;

/** Resource Adaptor Component Deployer.
 * 
 * @author M. Ranganathan
 */

class ResourceAdaptorComponentDeployer extends AbstractComponentDeployer {
    
    private static Logger logger;
    private ResourceAdaptorDescriptorParser parser;
    private DeployableUnitIDImpl deployableUnitID;
    private JarEntry ddXmlEntry;
    private DeployableUnitDescriptorImpl duDescriptor;
    
    static {
        logger = Logger.getLogger(EventTypeComponentDeployer.class);
    }
    public ResourceAdaptorComponentDeployer(DeployableUnitIDImpl deployableUnitID, JarEntry ddXmlEntry) {
        parser = new ResourceAdaptorDescriptorParser();
        this.deployableUnitID = deployableUnitID;
        this.ddXmlEntry = ddXmlEntry;
        this.duDescriptor = deployableUnitID.getDescriptor();
    }

    

    /**
     * Parses the specified document into the corresponding ComponentDescriptor.
     *
     * @param document the deployment descriptor of the component
     */
    protected List parseComponentDescriptors()  throws Exception {
        logger.debug("Parsing an RA Jar from " + getJar().getName());
        JarFile jar = getJar();

        //Get the Sbb deployment Descriptor;
        if(ddXmlEntry == null){
            throw new DeploymentException(
                "No SbbDeploymentDescriptor descriptor "
                +"(META-INF/resource-adaptor-jar.xml) was found in "
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
        for ( Iterator it = XMLUtils.getAllChildElements(doc.getDocumentElement(),XMLConstants.RESOURCE_ADAPTOR).iterator();
        	it.hasNext();	 ) {
            Element raNode = (Element)it.next();
            ResourceAdaptorDescriptorImpl raDescriptor = new ResourceAdaptorDescriptorImpl ();
            parser.parseResourceAdaptorDescriptor(raNode,raDescriptor );
            // Back pointer to the deployable unit from where this came.
            raDescriptor.setDeployableUnitID(deployableUnitID);
            retval.add(raDescriptor);
        }
       
        return retval;
    }



}
