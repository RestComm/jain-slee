/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileSpecificationDescriptorParser.java
 * 
 * Created on 8 déc. 2004
 *
 */
package org.mobicents.slee.container.component.deployment;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.management.xml.XMLConstants;
import org.mobicents.slee.container.management.xml.XMLUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * 
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 */
public class ProfileSpecificationDescriptorParser
	extends AbstractDeploymentDescriptorParser{

    /**
     * Parse file containing the standard event types. This is called on slee startup.
     * 
     * @param sourceUrl
     */

    public static List parseStandardProfileSpecifications(String urlForEventJar)
            throws Exception {
        FileInputStream reader;
        byte[] buffer;
        String profileSpecificationJarXML = new String();

        reader = new FileInputStream(urlForEventJar);
        buffer = new byte[reader.available()];
        reader.read(buffer);
        profileSpecificationJarXML = new String(buffer);
        reader.close();

        ProfileSpecificationDescriptorParser parser = new ProfileSpecificationDescriptorParser();
        InputSource profileSpecificationJarSource = new InputSource(new StringReader(
                profileSpecificationJarXML));
        Document profileSpecificationJarDocument = XMLUtils.parseDocument(profileSpecificationJarSource,true);
       
        
        
        /**
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        builder = factory.newDocumentBuilder();
        profileSpecificationJarDocument = builder.parse(profileSpecificationJarSource);
        **/

        List profileSpecNodes = XMLUtils.getAllChildElements(profileSpecificationJarDocument
                .getDocumentElement(), XMLConstants.PROFILE_SPEC_ND);
        LinkedList retval = new LinkedList();
        for (int i = 0; i < profileSpecNodes.size(); i++) {
            ProfileSpecificationDescriptorImpl descriptorImpl = 
                new ProfileSpecificationDescriptorImpl();
            Element profileSpecNode = (Element) profileSpecNodes.get(i);

            parser.parseProfileComponent(profileSpecNode, descriptorImpl);

            retval.add(descriptorImpl);
        }
        return retval;

    }
    
    public ProfileSpecificationDescriptorImpl parseProfileComponent(
            Element profileNode, 
            ProfileSpecificationDescriptorImpl profileDescriptorImpl)
    		throws IOException, Exception{
        //Creates the component key from the profile-spec-name,
        //profile-spec-vendor and profile-spec-version
        ComponentKey sbbKey = createKey(profileNode);
        profileDescriptorImpl.setComponentKey(sbbKey);
        //set the description of the profile
        String description = XMLUtils.getElementTextValue(profileNode, XMLConstants.DESCRIPTION_ND);
        profileDescriptorImpl.setDescription(description);
        //Handle "profile-classes" node
        Element profileClassesNode = XMLUtils.getChildElement(profileNode,
                                        XMLConstants.PROFILE_CLASSES_ND);
                    
        String  profileDescriptionStr = XMLUtils.getElementTextValue(
                profileClassesNode , XMLConstants.PROFILE_CLASSES_DESCRIPTION_ND);
        String  profileCMPInterfaceStr   = XMLUtils.getElementTextValue(
                profileClassesNode , XMLConstants.PROFILE_CLASSES_CMP_INTERFACE_NAME_ND);
        String  profileManagementInterfaceNameStr  = XMLUtils.getElementTextValue(
                profileClassesNode , XMLConstants.PROFILE_CLASSES_MANAGEMENT_INTERFACE_NAME_ND);
        String  profileManagementAbstractClassNameStr    = XMLUtils.getElementTextValue(
                profileClassesNode , XMLConstants.PROFILE_CLASSES_MANAGEMENT_ABSTRACT_CLASS_NAME_ND);
        //  Set the profile classes in the sbb descriptor.
        profileDescriptorImpl.setCMPInterfaceName(profileCMPInterfaceStr);
        profileDescriptorImpl.setManagementInterfaceName(profileManagementInterfaceNameStr);
        profileDescriptorImpl.setManagementAbstractClassName(profileManagementAbstractClassNameStr);
        profileDescriptorImpl.setClassesDescription(profileDescriptionStr);
        //Handle "profile-hints" node
        Element profileHintsNode = XMLUtils.getChildElement(profileNode,
                XMLConstants.PROFILE_HINTS_ND);
        boolean profileHintsSingleProfile=
            new Boolean(profileNode.getAttribute(XMLConstants.PROFILE_HINTS_SINGLE_PROFILE_AT)).booleanValue();
        profileDescriptorImpl.setSingleProfile(profileHintsSingleProfile);
        
        //Handle "profile-index" nodes
        List profileIndexNodes = XMLUtils.getAllChildElements(profileNode,
                XMLConstants.PROFILE_INDEX_ND);
        Iterator it=profileIndexNodes.iterator();
        HashMap profileIndexes= new HashMap();
        while(it.hasNext()){
            Element profileIndexNode= (Element)it.next();
            String attributeName=XMLUtils.getElementTextValue(profileIndexNode);
            profileIndexes.put(
                    attributeName,
                    new Boolean(profileIndexNode.getAttribute(XMLConstants.PROFILE_INDEX_UNIQUE_AT)));
        }
        profileDescriptorImpl.setProfileIndexes(profileIndexes);
        
        return profileDescriptorImpl;
    }
}
