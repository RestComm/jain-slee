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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParametersInterface;

/**
 * 
 * MSbbClasses.java
 *
 * <br>Project:  mobicents
 * <br>3:13:26 PM Feb 16, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:emmartins@gmail.com"> Eduardo Martins </a>
 */
public class MSbbClasses {
  
  private String description;
  
  private MSbbAbstractClass sbbAbstractClass;
  private MSbbLocalInterface sbbLocalInterface;
  private MSbbActivityContextInterface sbbActivityContextInterface;
  
  private MUsageParametersInterface sbbUsageParametersInterface;
  
  public MSbbClasses(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbClasses sbbClasses10)
  {
    this.description = sbbClasses10.getDescription() == null ? null : sbbClasses10.getDescription().getvalue();
    
    this.sbbAbstractClass = new MSbbAbstractClass(sbbClasses10.getSbbAbstractClass());
    this.sbbLocalInterface = sbbClasses10.getSbbLocalInterface() == null ? null : new MSbbLocalInterface(sbbClasses10.getSbbLocalInterface());
    this.sbbActivityContextInterface = sbbClasses10.getSbbActivityContextInterface() == null ? null : new MSbbActivityContextInterface(sbbClasses10.getSbbActivityContextInterface());
    this.sbbUsageParametersInterface = sbbClasses10.getSbbUsageParametersInterface() == null ? null : new MUsageParametersInterface(sbbClasses10.getSbbUsageParametersInterface());
  }
  
  public MSbbClasses(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbClasses sbbClasses11)
  {
    this.description = sbbClasses11.getDescription() == null ? null : sbbClasses11.getDescription().getvalue();
    
    this.sbbAbstractClass = new MSbbAbstractClass(sbbClasses11.getSbbAbstractClass());
    this.sbbLocalInterface = sbbClasses11.getSbbLocalInterface() == null ? null : new MSbbLocalInterface(sbbClasses11.getSbbLocalInterface());
    this.sbbActivityContextInterface = sbbClasses11.getSbbActivityContextInterface() == null ? null : new MSbbActivityContextInterface(sbbClasses11.getSbbActivityContextInterface());
    this.sbbUsageParametersInterface = sbbClasses11.getSbbUsageParametersInterface() == null ? null : new MUsageParametersInterface(sbbClasses11.getSbbUsageParametersInterface());
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public MSbbAbstractClass getSbbAbstractClass()
  {
    return sbbAbstractClass;
  }
  
  public MSbbLocalInterface getSbbLocalInterface()
  {
    return sbbLocalInterface;
  }
  
  public MSbbActivityContextInterface getSbbActivityContextInterface()
  {
    return sbbActivityContextInterface;
  }
  
  public MUsageParametersInterface getSbbUsageParametersInterface()
  {
    return sbbUsageParametersInterface;
  }
}
