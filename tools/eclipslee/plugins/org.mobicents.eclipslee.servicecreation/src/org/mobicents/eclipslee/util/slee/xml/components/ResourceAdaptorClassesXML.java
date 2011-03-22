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
public class ResourceAdaptorClassesXML extends DTDXML {

  protected ResourceAdaptorClassesXML(Document document, Element root, DTDHandler dtd) {
    super(document, root, dtd);
  }

  public ResourceAdaptorClassXML[] getResourceAdaptorClasses() {
    Element elements[] = getNodes("resource-adaptor-classes/resource-adaptor-class");
    ResourceAdaptorClassXML raClasses[] = new ResourceAdaptorClassXML[elements.length];
    for (int i = 0; i < elements.length; i++)
      raClasses[i] = new ResourceAdaptorClassXML(document, elements[i], dtd);

    return raClasses;   
  }

  public ResourceAdaptorClassXML addResourceAdaptorClass(boolean supportsActiveReconfiguration) {  
    Element child = addElement(getRoot(), "resource-adaptor-class");
    child.setAttribute("supports-active-reconfiguration", supportsActiveReconfiguration ? "True" : "False");
    return new ResourceAdaptorClassXML(document, child, dtd);
  }

  public void removeResourceAdaptor(ResourceAdaptorClassXML raClass) {
    raClass.getRoot().getParentNode().removeChild(raClass.getRoot());
  }

}
