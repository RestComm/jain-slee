/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
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

package org.mobicents.slee.container.deployment.jboss;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * JAIN SLEE Deployable Unit MetaData for JBoss AS 5.x Deployers
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SLEEDeploymentMetaData
{
  private static final Logger logger = Logger.getLogger(SLEEDeploymentMetaData.class);

  protected static enum ComponentType {

    UNKNOWN(-1),

    EVENT(1),
    SBB(2),
    PROFILE(3),
    RATYPE(4),
    RA(5),
    SERVICE(6),
    LIBRARY(7),
    DU(9);

    private ComponentType(int value) {
      this.value = value;
    }

    int value;

    public int getValue() {
      return this.value;
    }

  }

  // Variables -----------------------------------------------------

  protected ComponentType componentType = ComponentType.UNKNOWN;

  protected ArrayList<String> duContents = new ArrayList<String>();

  // Constructors --------------------------------------------------

  public SLEEDeploymentMetaData(DeploymentUnit du)
  {
    InputStream is = null;

    if((is = du.getResourceClassLoader().getResourceAsStream("META-INF/deployable-unit.xml")) != null) {
      // This is a SLEE DU
      this.componentType = ComponentType.DU;

      this.parseDUContents(is);
    }
    else if(du.getResourceClassLoader().getResourceAsStream("META-INF/event-jar.xml") != null) {
      this.componentType = ComponentType.EVENT;
    }
    else if(du.getResourceClassLoader().getResourceAsStream("META-INF/sbb-jar.xml") != null) {
      this.componentType = ComponentType.SBB;
    }
    else if(du.getResourceClassLoader().getResourceAsStream("META-INF/profile-spec-jar.xml") != null) {
      this.componentType = ComponentType.PROFILE;
    }
    else if(du.getResourceClassLoader().getResourceAsStream("META-INF/resource-adaptor-type-jar.xml") != null) {
      this.componentType = ComponentType.RATYPE;
    }
    else if(du.getResourceClassLoader().getResourceAsStream("META-INF/resource-adaptor-jar.xml") != null) {
      this.componentType = ComponentType.RA;
    }
    else if(du.getResourceClassLoader().getResourceAsStream("META-INF/library-jar.xml") != null) {
      this.componentType = ComponentType.LIBRARY;
    }
  }

  private void parseDUContents(InputStream is)
  {
    try {
      // Parse the DU to see which jars we should process
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

      // Set the Entity Resolver
      docBuilder.setEntityResolver(SLEEDeployerEntityResolver.INSTANCE);

      Document doc = docBuilder.parse (is);

      NodeList nodeList = doc.getDocumentElement().getChildNodes();

      for(int i = 0; i < nodeList.getLength(); i++) {
        if(nodeList.item(i) instanceof Element) {
          Element elem = (Element) nodeList.item(i);
          if(elem.getNodeName().equals("jar") || elem.getNodeName().equals("service-xml")) {
            duContents.add(elem.getTextContent());
          }
        }
      }
    }
    catch (Exception e) {
      logger.error("Error parsing Deployable Unit to build SLEE Deployer MetaData.", e);
    }    
  }
}
