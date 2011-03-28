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
public class ResourceAdaptorClassXML extends DTDXML {

  protected ResourceAdaptorClassXML(Document document, Element root, DTDHandler dtd) {
    super(document, root, dtd);
  }

  public void setDescription(String desc) {
    setChildText(root, "description", desc);
  }

  public String getDescription() {
    return getChildText(root, "description");
  }

  public void setSupportsActiveReconfiguration(Boolean supports) {
    Element child = getChild(root, "supports-active-reconfiguration");
    if (supports == null) {
      if (child != null)
        child.getParentNode().removeChild(child);
      return;     
    }

    if (child == null)
      child = addElement(root, "supports-active-reconfiguration");

    setChildText(child, "supports-active-reconfiguration", supports.toString());
  }

  public Boolean getSupportsActiveReconfiguration() {   
    Element child = getChild(root, "supports-active-reconfiguration");
    if (child == null)
      return null;

    return Boolean.valueOf(getChildText(child, "supports-active-reconfiguration"));
  }

  public void setResourceAdaptorClassName(String name) {
    Element child = getChild(root, "resource-adaptor-class-name");
    if (name == null) {
      if (child != null)
        child.getParentNode().removeChild(child);
      return;     
    }

    if (child == null)
      child = addElement(root, "resource-adaptor-class-name");

    setChildText(root, "resource-adaptor-class-name", name);
  }

  public String getResourceAdaptorClassName() {   
    Element child = getChild(root, "resource-adaptor-class-name");
    if (child == null)
      return null;

    return getChildText(child, "resource-adaptor-class-name");
  }

}
