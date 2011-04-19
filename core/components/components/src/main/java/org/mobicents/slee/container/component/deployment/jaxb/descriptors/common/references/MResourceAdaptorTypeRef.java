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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references;

import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * 
 * MResourceAdaptorTypeRef.java
 *
 * <br>Project:  mobicents
 * <br>6:19:43 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorTypeRef {

  protected String description;
  protected String resourceAdaptorTypeName;
  protected String resourceAdaptorTypeVendor;
  protected String resourceAdaptorTypeVersion;
  
  private ResourceAdaptorTypeID resourceAdaptorTypeID;
  
  public MResourceAdaptorTypeRef(org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptorTypeRef resourceAdaptorTypeRef10)
  {
    this.description = resourceAdaptorTypeRef10.getDescription() == null ? null : resourceAdaptorTypeRef10.getDescription().getvalue();
    
    this.resourceAdaptorTypeName = resourceAdaptorTypeRef10.getResourceAdaptorTypeName().getvalue();
    this.resourceAdaptorTypeVendor = resourceAdaptorTypeRef10.getResourceAdaptorTypeVendor().getvalue();
    this.resourceAdaptorTypeVersion = resourceAdaptorTypeRef10.getResourceAdaptorTypeVersion().getvalue();
    
    this.resourceAdaptorTypeID = new ResourceAdaptorTypeID(this.resourceAdaptorTypeName, this.resourceAdaptorTypeVendor, this.resourceAdaptorTypeVersion);
  }
  
  public MResourceAdaptorTypeRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorTypeRef resourceAdaptorTypeRef11)
  {
    this.description = resourceAdaptorTypeRef11.getDescription() == null ? null : resourceAdaptorTypeRef11.getDescription().getvalue();
    
    this.resourceAdaptorTypeName = resourceAdaptorTypeRef11.getResourceAdaptorTypeName().getvalue();
    this.resourceAdaptorTypeVendor = resourceAdaptorTypeRef11.getResourceAdaptorTypeVendor().getvalue();
    this.resourceAdaptorTypeVersion = resourceAdaptorTypeRef11.getResourceAdaptorTypeVersion().getvalue();

    this.resourceAdaptorTypeID = new ResourceAdaptorTypeID(this.resourceAdaptorTypeName, this.resourceAdaptorTypeVendor, this.resourceAdaptorTypeVersion);
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getResourceAdaptorTypeName()
  {
    return resourceAdaptorTypeName;
  }
  
  public String getResourceAdaptorTypeVendor()
  {
    return resourceAdaptorTypeVendor;
  }
  
  public String getResourceAdaptorTypeVersion()
  {
    return resourceAdaptorTypeVersion;
  }

  public ResourceAdaptorTypeID getComponentID()
  {
    return this.resourceAdaptorTypeID;
  }
}
