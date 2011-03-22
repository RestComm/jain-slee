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

import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorJarXML extends DTDXML {

  public static final String QUALIFIED_NAME = "resource-adaptor-jar";
  public static final String PUBLIC_ID_1_0 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.0//EN";
  public static final String SYSTEM_ID_1_0 = "http://java.sun.com/dtd/slee-resource-adaptor-jar_1_0.dtd";

  public static final String PUBLIC_ID_1_1 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.1//EN";
  public static final String SYSTEM_ID_1_1 = "http://java.sun.com/dtd/slee-resource-adaptor-jar_1_1.dtd";

  public static final String PUBLIC_ID = PUBLIC_ID_1_1;
  public static final String SYSTEM_ID = SYSTEM_ID_1_1;

  public ResourceAdaptorJarXML(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
    super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
    readDTDVia(resolver, dummyXML);
  }

  /**
   * Parse the provided InputStream as though it contains JAIN SLEE Profile Specification XML Data.
   * @param stream
   */

  public ResourceAdaptorJarXML(InputStream stream, EntityResolver resolver, InputSource dummyXML) throws SAXException, IOException, ParserConfigurationException {
    super(stream, resolver);			

    // Verify that this is really an ra-jar XML file.
    if (!getRoot().getNodeName().equals(QUALIFIED_NAME))
      throw new SAXException("This was not a resource adaptor jar XML file.");

    readDTDVia(resolver, dummyXML);
  }


  public ResourceAdaptorXML[] getResourceAdaptors() {
    Element elements[] = getNodes("resource-adaptor-jar/resource-adaptor");
    ResourceAdaptorXML ras[] = new ResourceAdaptorXML[elements.length];
    for (int i = 0; i < elements.length; i++)
      ras[i] = new ResourceAdaptorXML(document, elements[i], dtd);

    return ras;		
  }

  public ResourceAdaptorXML getResourceAdaptor(String name, String vendor, String version) throws ComponentNotFoundException {
    ResourceAdaptorXML ras[] = getResourceAdaptors();
    for (int i = 0; i < ras.length; i++) {
      ResourceAdaptorXML ra = ras[i];

      if (name.equals(ra.getName())
          && vendor.equals(ra.getVendor())
          && version.equals(ra.getVersion()))
        return ra;
    }

    throw new ComponentNotFoundException("Unable to find specified Resource Adaptor.");

  }

  public ResourceAdaptorXML addResourceAdaptor() {	
    Element child = addElement(getRoot(), "resource-adaptor");
    return new ResourceAdaptorXML(document, child, dtd);
  }

  public void removeResourceAdaptor(ResourceAdaptorXML ra) {
    ra.getRoot().getParentNode().removeChild(ra.getRoot());
  }

  public String toString() {
    String output = "";
    ResourceAdaptorXML ras[] = getResourceAdaptors();
    for (int i = 0; i < ras.length; i++) {
      if (i > 0)
        output += ", ";
      output += "[" + ras[i].toString() + "]";
    }
    return output;
  }

}
