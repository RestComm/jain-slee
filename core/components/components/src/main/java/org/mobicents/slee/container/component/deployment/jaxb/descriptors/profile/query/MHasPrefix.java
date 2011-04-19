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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import org.mobicents.slee.container.component.profile.query.HasPrefixDescriptor;

/**
 * Start time:11:51:45 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
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
