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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype;

/**
 * 
 * MActivityType.java
 *
 * <br>Project:  mobicents
 * <br>5:43:59 PM Jan 21, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MActivityType
{
	
  private String description;
  private String activityTypeName;
  
  public MActivityType(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ActivityType activityType10)
  {
    super();
    
    this.description = activityType10.getDescription() == null ? null : activityType10.getDescription().getvalue();
    this.activityTypeName = activityType10.getActivityTypeName().getvalue();
  }

  public MActivityType(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ActivityType activityType11)
  {
    super();
    
    this.description = activityType11.getDescription() == null ? null : activityType11.getDescription().getvalue();
    this.activityTypeName = activityType11.getActivityTypeName().getvalue();
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getActivityTypeName()
  {
    return activityTypeName;
  }

}
