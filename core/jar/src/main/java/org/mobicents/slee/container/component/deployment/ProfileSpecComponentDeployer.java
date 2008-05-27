package org.mobicents.slee.container.component.deployment;

import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.management.xml.XMLConstants;
import org.mobicents.slee.container.management.xml.XMLUtils;

import javax.slee.management.*;

import java.io.IOException;
import java.util.jar.*;

import org.w3c.dom.*;
import org.jboss.logging.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Emil Ivov
 * @version 1.0
 */

class ProfileSpecComponentDeployer extends AbstractComponentDeployer {

    private Logger logger;
    private String deploymentDirectory;
    private static byte buffer[] = new byte[8192];
    private ProfileSpecificationDescriptorParser parser;    
    private DeployableUnitIDImpl deployableUnitID;
    private JarEntry ddXmlEntry;
    	
    public ProfileSpecComponentDeployer(DeployableUnitIDImpl deployableUnitID, JarEntry ddXmlEntry) {
        Logger.setPluginClassName(Log4jLoggerPlugin.class.getName());
        logger = Logger.getLogger(DeploymentManager.class);
        this.deployableUnitID = deployableUnitID;
        this.ddXmlEntry = ddXmlEntry;

        parser = new ProfileSpecificationDescriptorParser();
    }

   /**
     * Parses the specified document into the corresponding ComponentDescriptor.
     *
     * @param document the deployment descriptor of the component
     */
    protected List parseComponentDescriptors() 
    	throws Exception{
        
        logger.debug("Parsing a Profile Spec from " + getJar().getName());
        JarFile jar = getJar();

        //Get the Sbb deployment Descriptor;
        if(ddXmlEntry == null){
            throw new DeploymentException(
                "No ProfileSpecificationDescriptor descriptor "
                +"(META-INF/profile-spec-jar.xml) was found in "
                + jar.getName());
        }
               
        //Parse the descriptor
        Document doc = null;
        try {
            doc = XMLUtils.parseDocument(jar.getInputStream(ddXmlEntry), true);
        }
        catch (IOException ex) {
            throw new DeploymentException("Failed to extract the Profile Specification"
                +" descriptor from " + jar.getName());
        }

        Element profileJarNode = doc.getDocumentElement();

        //Get a list of the profile specifications in the deployable unit.
        List profileSpecNodes = XMLUtils.getAllChildElements(profileJarNode, XMLConstants.PROFILE_SPEC_ND);
        if(profileSpecNodes.size() == 0){
            throw new DeploymentException("The "+ jar.getName()
                             + " deployment descriptor contains no profile-spec definitions");
        }
                      
        
        //First we parse the sbb elements from sbb-jar.xml
        HashMap descriptors = new HashMap();
        for (int i = profileSpecNodes.size() - 1; i >= 0; i--) {
            Element profileSpecNode = (Element) profileSpecNodes.get(i);

            ProfileSpecificationDescriptorImpl descriptor = 
                parser.parseProfileComponent(profileSpecNode, new ProfileSpecificationDescriptorImpl());
            descriptor.setDeployableUnit(deployableUnitID);
            
            descriptors.put(descriptor.getID(), descriptor);            
        }               
        
        logger.debug("Done.");
        return new ArrayList(descriptors.values());
    }

}
