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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptorJar;
import org.mobicents.slee.container.component.ra.ResourceAdaptorDescriptorFactory;

/**
 * 
 * Factory to build {@link ResourceAdaptorDescriptorImpl} objects.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ResourceAdaptorDescriptorFactoryImpl extends AbstractDescriptorFactory implements ResourceAdaptorDescriptorFactory {

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.core.component.ra.ResourceAdaptorDescriptorFactory#parse(java.io.InputStream)
   */
  public List<ResourceAdaptorDescriptorImpl> parse(InputStream inputStream) throws DeploymentException
  {
    Object jaxbPojo = buildJAXBPojo(inputStream);

    List<ResourceAdaptorDescriptorImpl> result = new ArrayList<ResourceAdaptorDescriptorImpl>();

    boolean isSlee11 = false;
    MResourceAdaptorJar mResourceAdaptorJar = null;
    if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptorJar)
    {
      mResourceAdaptorJar = new MResourceAdaptorJar((org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptorJar)jaxbPojo);
    }
    else if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorJar )
    {
      mResourceAdaptorJar = new MResourceAdaptorJar((org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorJar)jaxbPojo);
      isSlee11 = true;
    } 
    else {
      throw new SLEEException("unexpected class of jaxb pojo built: "+(jaxbPojo != null ? jaxbPojo.getClass() : null));
    }

    MSecurityPermissions securityPermissions = mResourceAdaptorJar.getSecurityPermissions();
    for (MResourceAdaptor mResourceAdaptor : mResourceAdaptorJar.getResourceAdaptor())
    {
      result.add(new ResourceAdaptorDescriptorImpl(mResourceAdaptor, securityPermissions, isSlee11));
    }

    return result;
  }
}
