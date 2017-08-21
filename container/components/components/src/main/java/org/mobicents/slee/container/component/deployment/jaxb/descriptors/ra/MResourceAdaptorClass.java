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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

/**
 * 
 * MResourceAdaptorClass.java
 *
 * <br>Project:  mobicents
 * <br>4:47:51 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorClass {
  
  protected boolean supportsActiveReconfiguration = false;
  protected String description;
  protected String resourceAdaptorClassName;

  public MResourceAdaptorClass(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorClass resourceAdaptorClass11)
  {    
    if (resourceAdaptorClass11.getSupportsActiveReconfiguration() != null) {
    	this.supportsActiveReconfiguration = Boolean.parseBoolean(resourceAdaptorClass11.getSupportsActiveReconfiguration());
    }
    
    this.resourceAdaptorClassName = resourceAdaptorClass11.getResourceAdaptorClassName().getvalue();
  }
  
  public boolean getSupportsActiveReconfiguration()
  {
    return supportsActiveReconfiguration;
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getResourceAdaptorClassName()
  {
    return resourceAdaptorClassName;
  }
}
