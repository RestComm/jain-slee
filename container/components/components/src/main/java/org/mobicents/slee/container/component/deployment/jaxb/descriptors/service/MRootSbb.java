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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.service;

/**
 * 
 * MRootSbb.java
 *
 * <br>Project:  mobicents
 * <br>11:55:41 AM Feb 12, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MRootSbb {
    
  private String description;
  
  private String sbbName;
  private String sbbVendor;
  private String sbbVersion;
  
  public MRootSbb(org.mobicents.slee.container.component.deployment.jaxb.slee.service.RootSbb rootSbb10)
  {  
    this.description = rootSbb10.getDescription() == null ? null : rootSbb10.getDescription().getvalue();
    
    this.sbbName = rootSbb10.getSbbName().getvalue();
    this.sbbVendor = rootSbb10.getSbbVendor().getvalue();
    this.sbbVersion = rootSbb10.getSbbVersion().getvalue();
  }

  public MRootSbb(org.mobicents.slee.container.component.deployment.jaxb.slee11.service.RootSbb rootSbb11)
  {
    this.description = rootSbb11.getDescription() == null ? null : rootSbb11.getDescription().getvalue();
    
    this.sbbName = rootSbb11.getSbbName().getvalue();
    this.sbbVendor = rootSbb11.getSbbVendor().getvalue();
    this.sbbVersion = rootSbb11.getSbbVersion().getvalue();    
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getSbbName()
  {
    return sbbName;
  }
  
  public String getSbbVendor()
  {
    return sbbVendor;
  }
  
  public String getSbbVersion()
  {
    return sbbVersion;
  }

}
