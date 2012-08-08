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

import org.mobicents.slee.container.component.sbb.EjbRefDescriptor;

/**
 * Start time:15:15:53 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MEjbRef implements EjbRefDescriptor {

  private String description;

  private String ejbRefName;
  private String ejbRefType;

  private String home;
  private String remote;

  private String ejbLink;

  public MEjbRef(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.EjbRef ejbRef10)
  {
    this.description = ejbRef10.getDescription() == null ? null : ejbRef10.getDescription().getvalue();

    this.ejbRefName = ejbRef10.getEjbRefName().getvalue();
    this.ejbRefType = ejbRef10.getEjbRefType().getvalue();

    this.home = ejbRef10.getHome().getvalue();
    this.remote = ejbRef10.getRemote().getvalue();

    //Optional, removed in 1.1
    this.ejbLink = ejbRef10.getEjbLink() == null ? null : ejbRef10.getEjbLink().getvalue();
  }

  public MEjbRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EjbRef ejbRef11)
  {
    this.description = ejbRef11.getDescription() == null ? null : ejbRef11.getDescription().getvalue();

    this.ejbRefName = ejbRef11.getEjbRefName().getvalue();
    this.ejbRefType = ejbRef11.getEjbRefType().getvalue();

    this.home = ejbRef11.getHome().getvalue();
    this.remote = ejbRef11.getRemote().getvalue();

  }

  public String getDescription()
  {
    return description;
  }

  public String getEjbRefName()
  {
    return ejbRefName;
  }

  public String getEjbRefType()
  {
    return ejbRefType;
  }

  public String getHome()
  {
    return home;
  }

  public String getRemote()
  {
    return remote;
  }

  public String getEjbLink()
  {
    return ejbLink;
  }

}
