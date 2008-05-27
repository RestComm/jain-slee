/*
 * ResourceAdaptorDescriptorParser.java
 *
 * Created on Dec 16, 2004
 *
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
 *
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.slee.container.component.deployment;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.jxpath.util.TypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;

import org.mobicents.slee.resource.ConfigPropertyDescriptor;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.management.xml.XMLConstants;
import org.mobicents.slee.container.management.xml.XMLUtils;
import org.mobicents.slee.resource.RAClassesEntry;
import org.mobicents.slee.resource.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.resource.ResourceAdaptorTypeIDImpl;


/** Parses the deployment descriptor for the resource adaptor.
 *
 * @author M. Ranganathan
 *
 */
/*
 *
<!ELEMENT resource-adaptor-jar (description?, resource-adaptor+)>
 
<!--
The resource-adaptor element defines a resource adaptor component.  It
contains an optional description about the resource adaptor being defined, the
name, vendor, and version of the resource adaptor, a mandatory reference to
the resource adaptor type implemented by the resource adaptor, and optional
information about the resource adaptor classes and interfaces.
 
Used in: in resource-adaptor-jar
-->
<!ELEMENT resource-adaptor (description?, resource-adaptor-name,
          resource-adaptor-vendor, resource-adaptor-version,
          resource-adaptor-type-ref, resource-adaptor-classes?)>
 
<!--
The description element may contain any descriptive text about the parent
element.
 
Used in: resource-adaptor-jar, resource-adaptor, resource-adaptor-type-ref
-->
<!ELEMENT description (#PCDATA)>
 
<!--
The resource-adaptor-name element contains the name of the resource adaptor
component.
 
Used in: resource-adaptor
 
Example:
    <resource-adaptor-name>MyJccImplementation</resource-adaptor-name>
-->
<!ELEMENT resource-adaptor-name (#PCDATA)>
 
<!--
The resource-adaptor-vendor element contains the vendor of the resource
adaptor component.
 
Used in: resource-adaptor
 
Example:
    <resource-adaptor-vendor>MyCompany, Inc.</resource-adaptor-vendor>
-->
<!ELEMENT resource-adaptor-vendor (#PCDATA)>
 
<!--
The resource-adaptor-version element contains the version of the resource
adaptor component.  The version number typically should have the general form
"major-version.minor-version".
 
Used in: resource-adaptor
 
Example:
    <resource-adaptor-version>1.1</resource-adaptor-version>
-->
<!ELEMENT resource-adaptor-version (#PCDATA)>
 
<!--
The resource-adaptor-type-ref element declares the resource adaptor type the
resource adaptor implements.  It contains an optional description and the
name, vendor, and version of the resource adaptor type.
 
Used in: resource-adaptor
-->
<!ELEMENT resource-adaptor-type-ref (description?, resource-adaptor-type-name,
          resource-adaptor-type-vendor, resource-adaptor-type-version)>
 
<!--
The resource-adaptor-type-name element contains the name of the resource
adaptor type component implemented by the resource adaptor.
 
Used in: resource-adaptor-type-ref
 
Example:
    <resource-adaptor-type-name>JCC</resource-adaptor-type-name>
-->
<!ELEMENT resource-adaptor-type-name (#PCDATA)>
 
<!--
The resource-adaptor-type-vendor element contains the vendor of the resource
adaptor type component implemented by the resource adaptor.
 
Used in: resource-adaptor-type-ref
 
Example:
    <resource-adaptor-type-vendor>
        javax.csapi.cc.jcc
    </resource-adaptor-type-vendor>
-->
<!ELEMENT resource-adaptor-type-vendor (#PCDATA)>
 
<!--
The resource-adaptor-type-version element contains the version of the resource
adaptor type component implemented by the resource adaptor.
 
Used in: resource-adaptor-type-ref
 
Example:
    <resource-adaptor-type-version>1.1</resource-adaptor-type-version>
-->
<!ELEMENT resource-adaptor-type-version (#PCDATA)>
 
<!--
The resource-adaptor-classes element contents is undefined in the
specification.  A SLEE implementation may specify the contents required or may
use the ID mechanism to allow additional information to be specified in a
seperate deployment descriptor.
 
Used in: resource-adaptor
-->
<!ELEMENT resource-adaptor-classes (#PCDATA)>
 
 
<!--
The ID mechanism is to allow tools that produce additional deployment
information (ie. information beyond that contained by the standard SLEE
deployment descriptors) to store the non-standard information in a separate
file, and easily refer from those tools-specific files to the information in
the standard deployment descriptor.  The SLEE architecture does not allow the
tools to add the non-standard information into the SLEE-defined deployment
descriptors.
-->
<!ATTLIST resource-adaptor-jar id ID #IMPLIED>
<!ATTLIST resource-adaptor id ID #IMPLIED>
<!ATTLIST description id ID #IMPLIED>
<!ATTLIST resource-adaptor-name id ID #IMPLIED>
<!ATTLIST resource-adaptor-vendor id ID #IMPLIED>
<!ATTLIST resource-adaptor-version id ID #IMPLIED>
<!ATTLIST resource-adaptor-type-ref id ID #IMPLIED>
<!ATTLIST resource-adaptor-type-name id ID #IMPLIED>
<!ATTLIST resource-adaptor-type-vendor id ID #IMPLIED>
<!ATTLIST resource-adaptor-type-version id ID #IMPLIED>
<!ATTLIST resource-adaptor-classes id ID #IMPLIED>
 
 
 *
 */
public class ResourceAdaptorDescriptorParser extends
        AbstractDeploymentDescriptorParser {
    private static Logger logger = Logger.getLogger(ResourceAdaptorDescriptorParser.class);
    
    public ResourceAdaptorDescriptorImpl
            parseResourceAdaptorDescriptor(Element topLevel,
            ResourceAdaptorDescriptorImpl raDescriptor	 ) throws Exception {
        logger.debug("Enter parseResourceAdaptorDescriptor");
        String description = XMLUtils.getElementTextValue(topLevel,XMLConstants.DESCRIPTION_ND);
        raDescriptor.setDescription(description);
        String name = XMLUtils.getElementTextValue( topLevel, XMLConstants.RESOURCE_ADAPTOR_NAME);
        String vendor = XMLUtils.getElementTextValue(topLevel, XMLConstants.RESOURCE_ADAPTOR_VENDOR);
        String version = XMLUtils.getElementTextValue(topLevel,XMLConstants.RESOURCE_ADAPTOR_VERSION);
        ResourceAdaptorIDImpl resourceAdaptorID = new ResourceAdaptorIDImpl( new ComponentKey( name, vendor,version));
        raDescriptor.setID(resourceAdaptorID);
        
        Element typerefNd = XMLUtils.getChildElement(topLevel,XMLConstants.RESOURCE_ADAPTOR_TYPE_REF);
        String typeDesc = XMLUtils.getElementTextValue(typerefNd,XMLConstants.DESCRIPTION_ND);
        String typeVersion = XMLUtils.getElementTextValue(typerefNd,XMLConstants.RESOURCE_ADAPTOR_TYPE_VERSION);
        String typeName = XMLUtils.getElementTextValue(typerefNd,XMLConstants.RESOURCE_ADAPTOR_TYPE_NAME);
        String typeVendor = XMLUtils.getElementTextValue(typerefNd,XMLConstants.RESOURCE_ADAPTOR_TYPE_VENDOR);
        
        ResourceAdaptorTypeIDImpl raTypeID = new ResourceAdaptorTypeIDImpl( new ComponentKey( typeName, typeVendor, typeVersion) );
        raTypeID.setDescription(typeDesc);
        if ( logger.isDebugEnabled()) {
            logger.debug("raTypeID = " + raTypeID);
        }
        
        raDescriptor.setResourceAdaptorType(raTypeID);
        
        for ( Iterator it1 = XMLUtils.getAllChildElements(topLevel,XMLConstants.RESOURCE_ADAPTOR_CLASSES).iterator(); it1.hasNext();	) {
            Element raClassNode = (Element) it1.next();
            Element raClass = XMLUtils.getChildElement(raClassNode,XMLConstants.RESOURCE_ADAPTOR_CLASS);
            Element raClassName = XMLUtils.getChildElement(raClass,XMLConstants.RESOURCE_ADAPTOR_CLASS_NAME);
            String raClassNameStr = XMLUtils.getElementTextValue(raClassName);
            RAClassesEntry raClassEntry = new RAClassesEntry(raClassNameStr);
            raDescriptor.setResourceAdaptorClasses( raClassEntry);
        }
        
        //Parse config properties
        logger.debug("Parsing configuration properties");
        
        Collection props = XMLUtils.getAllChildElements(topLevel,
                XMLConstants.RESOURCE_ADAPTOR_CONFIG_PROPERTY);
        
        Iterator list = props.iterator();
        while (list.hasNext()) {
            Element configPropertyElement = (Element) list.next();
            
            Element configPropertyNameElement = XMLUtils.getChildElement(configPropertyElement, XMLConstants.RESOURCE_ADAPTOR_CONFIG_PROPERTY_NAME);
            Element configPropertyTypeElement = XMLUtils.getChildElement(configPropertyElement, XMLConstants.RESOURCE_ADAPTOR_CONFIG_PROPERTY_TYPE);
            Element configPropertyValueElement = XMLUtils.getChildElement(configPropertyElement, XMLConstants.RESOURCE_ADAPTOR_CONFIG_PROPERTY_VALUE);
            
            String propName = XMLUtils.getElementTextValue(configPropertyNameElement);
            String propType = XMLUtils.getElementTextValue(configPropertyTypeElement);
            String lexicalValue = XMLUtils.getElementTextValue(configPropertyValueElement);
            
            Class clazz = getClass().forName(propType);
            Object value = TypeUtils.convert(lexicalValue, clazz);
            
            ConfigPropertyDescriptor configProperty = new ConfigPropertyDescriptor(
                    propName, propType, value);
            
            raDescriptor.add(configProperty);
            logger.debug("Append to descriptor: " + configProperty);
        }
        
        return raDescriptor;
    }
    
    
}

