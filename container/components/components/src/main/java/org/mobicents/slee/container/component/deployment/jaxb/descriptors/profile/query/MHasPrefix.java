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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import org.mobicents.slee.container.component.profile.query.HasPrefixDescriptor;

/**
 * Start time:11:51:45 2009-01-29<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MHasPrefix implements HasPrefixDescriptor {

  private String attributeName;
  private String value;
  private String parameter;
  private String collatorRef;

  public MHasPrefix(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.HasPrefix hasPrefix11)
  {    
    this.attributeName = hasPrefix11.getAttributeName();
    this.value = hasPrefix11.getValue();
    this.parameter = hasPrefix11.getParameter();
    this.collatorRef = hasPrefix11.getCollatorRef();
  }

  public String getAttributeName()
  {
    return attributeName;
  }

  public String getValue()
  {
    return value;
  }

  public String getParameter()
  {
    return parameter;
  }

  public String getCollatorRef()
  {
    return collatorRef;
  }
}
