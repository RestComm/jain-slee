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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DeployConfigXML extends DTDXML {

  public static final String QUALIFIED_NAME = "deploy-config";

  public static final String PUBLIC_ID = "-//Red Hat, Inc.//DTD JAIN SLEE Deploy Config 1.1//EN";
  public static final String SYSTEM_ID = "http://www.mobicents.org/slee/dtd/deploy-config_1_1.dtd";

  public DeployConfigXML(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
    super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
    readDTDVia(resolver, dummyXML);
  }

  /**
   * Parse the provided InputStream as though it contains JAIN SLEE Deploy Config XML Data.
   * @param stream
   */
  public DeployConfigXML(InputStream stream, EntityResolver resolver, InputSource dummyXML) throws SAXException, IOException, ParserConfigurationException {
    super(stream, resolver);
    
    // Verify that this is really a deploy config XML file.
    if (!getRoot().getNodeName().equals(QUALIFIED_NAME))
      throw new SAXException("This was not a service XML file.");   
    
    readDTDVia(resolver, dummyXML);
  }

  protected DeployConfigXML(Document document, Element root, DTDHandler dtd) {
    super(document, root, dtd);		
  }

  public DeployConfigRaEntityXML addResourceAdaptorEntity(String raName, String raVendor, String raVersion, String entityName) {
    Element entity = addElement(getRoot(), "ra-entity");
    entity.setAttribute("resource-adaptor-id", "ResourceAdaptorID[name=" + raName + ",vendor=" + raVendor + ",version=" + raVersion + "]");
    entity.setAttribute("entity-name", entityName);

    return new DeployConfigRaEntityXML(document, entity, dtd);
  }

  public void removeResourceAdaptorEntity(DeployConfigRaEntityXML entity) {
    entity.getRoot().getParentNode().removeChild(entity.getRoot());
  }

  public DeployConfigRaEntityXML[] getResourceAdaptorEntities() {
    Element[] entities = getNodes(getRoot(), "ra-entity");
    if (entities == null)
      return new DeployConfigRaEntityXML[0];

    DeployConfigRaEntityXML deployConfigRaEntities[] = new DeployConfigRaEntityXML[entities.length];
    for (int i = 0; i < entities.length; i++)
      deployConfigRaEntities[i] = new DeployConfigRaEntityXML(document, entities[i], dtd);

    return deployConfigRaEntities;
  }

  public DeployConfigRaEntityXML getResourceAdaptorEntity(String raName, String raVendor, String raVersion, String entityName) {
    DeployConfigRaEntityXML deployConfigRaEntities[] = getResourceAdaptorEntities();
    String raId = "ResourceAdaptorID[name=" + raName + ",vendor=" + raVendor + ",version=" + raVersion + "]";
    for (DeployConfigRaEntityXML deployConfigRaEntity : deployConfigRaEntities) {
      if(deployConfigRaEntity.getResourceAdaptorId().equals(raId)) {
        return deployConfigRaEntity;
      }
    }

    return null;
  }

}
