/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Nov 25, 2004
 *
 * MobicentsSbbDeploymentDescriptorParser.java
 */
package org.mobicents.slee.container.component.deployment;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EJBReference;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.management.xml.XMLConstants;

import org.mobicents.slee.container.management.xml.XMLUtils;

import java.io.IOException;

import java.util.Map;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.slee.SbbID;

import org.jboss.deployment.DeploymentException;
import org.w3c.dom.Element;

import org.jboss.logging.Logger;

/**
 * Parses Mobicents Sbb Deployment Descriptors into Sbb Components
 * I.e. parses the mobicents-sbb-jar.xml file
 * 
 * @author Tim
 * 
 */
public class MCSbbDeploymentDescriptorParser extends
        AbstractDeploymentDescriptorParser {
    
    private static Logger log = Logger.getLogger(MCSbbDeploymentDescriptorParser.class);
    
    public void parseSbbComponent(Element sbbNode, Map descriptors)
    	throws IOException, Exception {
        
        log.debug("Parsing mobicents-sbb-jar.xml");

	    ComponentKey sbbKey = createKey(sbbNode);
	    SbbIDImpl sbbId = new SbbIDImpl(sbbKey);
	    
	    MobicentsSbbDescriptor descriptor = (MobicentsSbbDescriptor)descriptors.get(sbbId);
	    
	    if (descriptor == null) {	        
	        throw new DeploymentException("sbb with id:" + sbbId + " in mobicents-sbb-jar.xml but not in sbb-jar.xml");
	    }
		    
	    List ejbRefNodeList = XMLUtils.getAllChildElements(sbbNode, XMLConstants.MC_EJB_REF_ND);
	    HashSet ejbRefs = new HashSet ();
	    for (Iterator iter = ejbRefNodeList.iterator(); iter.hasNext();) {
	        Element ejbRefNode = (Element) iter.next();
	       
	        String ejbRefName = XMLUtils.getElementTextValue(ejbRefNode,XMLConstants.MC_EJB_REF_NAME);
	        String jndiName = XMLUtils.getElementTextValue(ejbRefNode,XMLConstants.MC_JNDI_NAME);
	        	        
	        log.debug("Number of ejb-refs:" + descriptor.getEjbRefs().size());
	        Iterator iter2 = descriptor.getEjbRefs().iterator();
	        boolean found = false;
	        while (iter2.hasNext()) {
	            EJBReference ejbRef = (EJBReference)iter2.next();
	            log.debug("ejbrefname is: " + ejbRef.getEjbRefName());
	            if (ejbRef.getEjbRefName().equals(ejbRefName)) {
	                ejbRef.setJndiName(jndiName);
	                found = true;
	                break;
	            }
	        }
	        if (!found) {	            
	            throw new DeploymentException("ejb-ref with name " + ejbRefName +
	                    " found in mobicents-sbb-jar.xml but not found in sbb-jar.xml");
	        }
	    }
	}
}
