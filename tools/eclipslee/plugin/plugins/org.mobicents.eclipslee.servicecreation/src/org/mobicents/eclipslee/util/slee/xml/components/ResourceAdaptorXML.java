/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors by the
 * @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.eclipslee.util.slee.xml.components;

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorXML extends DTDXML {

  public ResourceAdaptorXML(Document document, Element root, DTDHandler dtd) {
    super(document, root, dtd);
  }

  public void setDescription(String desc) {
    setChildText(root, "description", desc);
  }

  public String getDescription() {
    return getChildText(root, "description");
  }

  public void setName(String name) {
    setChildText(root, "resource-adaptor-name", name);
  }

  public String getName() {
    return getChildText(root, "resource-adaptor-name");
  }

  public void setVendor(String vendor) {
    setChildText(root, "resource-adaptor-vendor", vendor);
  }

  public String getVendor() {
    return getChildText(root, "resource-adaptor-vendor");
  }

  public void setVersion(String version) {
    setChildText(root, "resource-adaptor-version", version);
  }

  public String getVersion() {
    return getChildText(root, "resource-adaptor-version");
  }

  public ResourceAdaptorClassesXML addResourceAdaptorClasses() {
    Element child = addElement(root, "resource-adaptor-classes");
    return new ResourceAdaptorClassesXML(document, child, dtd);		
  }

  public ResourceAdaptorClassesXML getResourceAdaptorClasses() {
    Element nodes[] = getNodes("resource-adaptor/resource-adaptor-classes");
    return new ResourceAdaptorClassesXML(document, nodes[0], dtd);
  }

  public void removeResourceAdaptorClasses(ResourceAdaptorClassesXML xml) {
    xml.getRoot().getParentNode().removeChild(xml.getRoot());
  }

  public ResourceAdaptorResourceAdaptorTypeXML addResourceAdaptorType(ResourceAdaptorTypeXML raType) {
    Element child = addElement(root, "resource-adaptor-type-ref");
    setChildText(child, "resource-adaptor-type-name", raType.getName());
    setChildText(child, "resource-adaptor-type-vendor", raType.getVendor());
    setChildText(child, "resource-adaptor-type-version", raType.getVersion());
    return new ResourceAdaptorResourceAdaptorTypeXML(document, child, dtd);   
  }

  public ResourceAdaptorResourceAdaptorTypeXML addResourceAdaptorType(String name, String vendor, String version) {
    Element child = addElement(root, "resource-adaptor-type-ref");
    setChildText(child, "resource-adaptor-type-name", name.trim());
    setChildText(child, "resource-adaptor-type-vendor",vendor.trim());
    setChildText(child, "resource-adaptor-type-version",version.trim());
    return new ResourceAdaptorResourceAdaptorTypeXML(document, child, dtd);   
  }

  public ResourceAdaptorResourceAdaptorTypeXML[] getResourceAdaptorTypes() {
    Element nodes[] = getNodes("resource-adaptor/resource-adaptor-type-ref");
    ResourceAdaptorResourceAdaptorTypeXML xml[] = new ResourceAdaptorResourceAdaptorTypeXML[nodes.length];
    for (int i = 0; i < nodes.length; i++)
      xml[i] = new ResourceAdaptorResourceAdaptorTypeXML(document, nodes[i], dtd);    
    return xml;
  }

  public void removeResourceAdaptorType(ResourceAdaptorResourceAdaptorTypeXML xml) {
    xml.getRoot().getParentNode().removeChild(xml.getRoot());
  }

  public ResourceAdaptorResourceAdaptorTypeXML getResourceAdaptorType(String name, String vendor, String version) {
    Element nodes[] = getNodes("resource-adaptor/resource-adaptor-type-ref");
    ResourceAdaptorResourceAdaptorTypeXML xml[] = new ResourceAdaptorResourceAdaptorTypeXML[nodes.length];
    for (int i = 0; i < nodes.length; i++){ 
      xml[i] = new ResourceAdaptorResourceAdaptorTypeXML(document, nodes[i], dtd);    
      if(xml[i].getName().equals(name) && xml[i].getVendor().equals(vendor) && xml[i].getVersion().equals(version)){
        return xml[i];
      }
    }
    return null;
  }

  public LibraryRefXML[] getLibraryRefs() {
    Element nodes[] = getNodes("resource-adaptor/library-ref");
    LibraryRefXML xml[] = new LibraryRefXML[nodes.length];
    for (int i = 0; i < nodes.length; i++)
      xml[i] = new LibraryRefXML(document, nodes[i], dtd);
    return xml;
  }

  public LibraryRefXML addLibraryRef(LibraryXML library) {
    Element ele = addElement(getRoot(), "library-ref");
    LibraryRefXML xml = new LibraryRefXML(document, ele, dtd);

    xml.setName(library.getName());
    xml.setVendor(library.getVendor());
    xml.setVersion(library.getVersion());

    return xml;
  }

  public void removeLibraryRef(LibraryRefXML libraryRef) {
    libraryRef.getRoot().getParentNode().removeChild(libraryRef.getRoot());
  }

  public ResourceAdaptorConfigPropertyXML addConfigProperty(String name, String type, String value) {
    Element child = addElement(root, "config-property");
    setChildText(child, "config-property-name", name.trim());
    setChildText(child, "config-property-type", type.trim());
    setChildText(child, "config-property-value", value.trim());
    return new ResourceAdaptorConfigPropertyXML(document, child, dtd);   
  }

  public ResourceAdaptorConfigPropertyXML[] getConfigProperties() {
    Element nodes[] = getNodes("resource-adaptor/config-property");
    ResourceAdaptorConfigPropertyXML xml[] = new ResourceAdaptorConfigPropertyXML[nodes.length];
    for (int i = 0; i < nodes.length; i++)
      xml[i] = new ResourceAdaptorConfigPropertyXML(document, nodes[i], dtd);    
    return xml;
  }

  public void removeConfigProperty(ResourceAdaptorConfigPropertyXML xml) {
    xml.getRoot().getParentNode().removeChild(xml.getRoot());
  }

  public ResourceAdaptorConfigPropertyXML getConfigProperty(String name) {
    Element nodes[] = getNodes("resource-adaptor/config-property");
    ResourceAdaptorConfigPropertyXML xml[] = new ResourceAdaptorConfigPropertyXML[nodes.length];
    for (int i = 0; i < nodes.length; i++){ 
      xml[i] = new ResourceAdaptorConfigPropertyXML(document, nodes[i], dtd);    
      if(xml[i].getName().equals(name)){
        return xml[i];
      }
    }
    return null;
  }

}
