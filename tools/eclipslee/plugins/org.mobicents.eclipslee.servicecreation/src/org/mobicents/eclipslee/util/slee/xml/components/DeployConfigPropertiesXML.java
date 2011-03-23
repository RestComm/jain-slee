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
public class DeployConfigPropertiesXML extends DTDXML {

  protected DeployConfigPropertiesXML(Document document, Element root, DTDHandler dtd) {
    super(document, root, dtd);		
  }

  public DeployConfigPropertyXML addProperty(String name, String type, String value) {
    Element raEntity = addElement(getRoot(), "property");
    raEntity.setAttribute("name", name);
    raEntity.setAttribute("type", name);

    if (value != null) {
      raEntity.setAttribute("value", name);
    }

    return new DeployConfigPropertyXML(document, raEntity, dtd);
  }

  public DeployConfigPropertyXML[] getProperties() {
    Element props[] = getNodes(getRoot(), "properties/property");
    DeployConfigPropertyXML properties[] = new DeployConfigPropertyXML[props.length];
    for (int i = 0; i < props.length; i++) {
      properties[i] = new DeployConfigPropertyXML(document, props[i], dtd); 
    }

    return properties;
  }

  public DeployConfigPropertyXML getProperty(String name) {
    DeployConfigPropertyXML[] properties = getProperties();
    for(DeployConfigPropertyXML property : properties) {
      if(property.getName().equals(name)) {
        return property;
      }
    }

    return null;
  }

  public void removeProperty(DeployConfigPropertyXML property) {
    property.getRoot().getParentNode().removeChild(property.getRoot());
  }
}
