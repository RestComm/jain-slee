/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.du;

import java.util.ArrayList;
import java.util.List;

import javax.slee.management.DeploymentException;

/**
 * 
 * MDeployableUnit.java
 *
 * <br>Project:  mobicents
 * <br>7:43:59 PM Jan 29, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MDeployableUnit {

  private String description;

  private List<String> jarEntries = new ArrayList<String>();
  private List<String> serviceXmlEntries = new ArrayList<String>();

  private List<String> sortedEntries = new ArrayList<String>();
  
  public MDeployableUnit(org.mobicents.slee.container.component.deployment.jaxb.slee.du.DeployableUnit deployableUnit10) throws DeploymentException
  {

    this.description = deployableUnit10.getDescription() == null ? null : deployableUnit10.getDescription().getvalue();

    for(Object entryObject : deployableUnit10.getJarOrServiceXml())
    {
      if(entryObject instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.du.Jar)
      {
        org.mobicents.slee.container.component.deployment.jaxb.slee.du.Jar jarEntry = (org.mobicents.slee.container.component.deployment.jaxb.slee.du.Jar)entryObject;
        
        String jarEntryString = jarEntry.getvalue();
        
        this.jarEntries.add(jarEntryString);
        this.sortedEntries.add(jarEntryString);
      }
      else if(entryObject instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.du.ServiceXml)
      {
        org.mobicents.slee.container.component.deployment.jaxb.slee.du.ServiceXml serviceXmlEntry = (org.mobicents.slee.container.component.deployment.jaxb.slee.du.ServiceXml)entryObject;

        String serviceXmlEntryString = serviceXmlEntry.getvalue();
        
        this.serviceXmlEntries.add(serviceXmlEntryString);
        this.sortedEntries.add(serviceXmlEntryString);
      }
      else
      {
        throw new DeploymentException("Unknown type of entry in deployable unit " + deployableUnit10.getId() + ": " + entryObject.getClass().getName() );
      }
    }
  }

  public MDeployableUnit(org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit deployableUnit11) throws DeploymentException
  {

    this.description = deployableUnit11.getDescription() == null ? null : deployableUnit11.getDescription().getvalue();

    for(Object entryObject : deployableUnit11.getJarOrServiceXml())
    {
      if(entryObject instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.du.Jar)
      {
        org.mobicents.slee.container.component.deployment.jaxb.slee11.du.Jar jarEntry = (org.mobicents.slee.container.component.deployment.jaxb.slee11.du.Jar)entryObject;

        String jarEntryString = jarEntry.getvalue();
        
        this.jarEntries.add(jarEntryString);
        this.sortedEntries.add(jarEntryString);
      }
      else if(entryObject instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.du.ServiceXml)
      {
        org.mobicents.slee.container.component.deployment.jaxb.slee11.du.ServiceXml serviceXmlEntry = (org.mobicents.slee.container.component.deployment.jaxb.slee11.du.ServiceXml)entryObject;
        
        String serviceXmlEntryString = serviceXmlEntry.getvalue();
        
        this.serviceXmlEntries.add(serviceXmlEntryString);
        this.sortedEntries.add(serviceXmlEntryString);
      }
      else
      {
        throw new DeploymentException("Unknown type of entry in deployable unit " + deployableUnit11.getId() + ": " + entryObject.getClass().getName() );
      }
    }
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public List<String> getJarEntries()
  {
    return jarEntries;
  }
  
  public List<String> getServiceXmlEntries()
  {
    return serviceXmlEntries;
  }
  
  public List<String> getSortedEntries()
  {
    return sortedEntries;
  }

}
