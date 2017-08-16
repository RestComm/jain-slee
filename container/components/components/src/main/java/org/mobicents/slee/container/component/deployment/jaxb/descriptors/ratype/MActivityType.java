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
