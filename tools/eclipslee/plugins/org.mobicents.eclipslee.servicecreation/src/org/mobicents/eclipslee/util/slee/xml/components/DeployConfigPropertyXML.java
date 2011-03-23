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
public class DeployConfigPropertyXML extends DTDXML {

	protected DeployConfigPropertyXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
  public String getName() {
    return getRoot().getAttribute("name");   
  }
  
  public void setName(String name) {
    getRoot().setAttribute("name", name);
  }
  
  public String getType() {
    return getRoot().getAttribute("type");   
  }
  
  public void setType(String type) {
    getRoot().setAttribute("type", type);
  }
  
  public String getValue() {
    return getRoot().getAttribute("value");   
  }
  
  public void setValue(String value) {
    if (value == null) {
      getRoot().removeAttribute(value);
    }
    else {
      getRoot().setAttribute("value", value);
    }
  }
  
}
