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
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbb;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbJar;
import org.mobicents.slee.container.component.sbb.SbbDescriptorFactory;

/**
 * 
 * Factory to build {@link SbbDescriptorImpl} objects.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:emmartins@gmail.com"> Eduardo Martins </a> 
 */
public class SbbDescriptorFactoryImpl extends AbstractDescriptorFactory implements SbbDescriptorFactory {

  /**
   * Builds a list of {@link SbbDescriptorImpl} objects, from an {@link InputStream} containing the sbb jar xml.
   * @param inputStream
   * @return
   * @throws DeploymentException
   */
  public List<SbbDescriptorImpl> parse(InputStream inputStream) throws DeploymentException
  {
    Object jaxbPojo = buildJAXBPojo(inputStream);

    List<SbbDescriptorImpl> result = new ArrayList<SbbDescriptorImpl>();

    boolean isSlee11 = false;
    MSbbJar mSbbJar = null;
    if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbJar)
    {
      mSbbJar = new MSbbJar((org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbJar)jaxbPojo);
    }
    else if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbJar)
    {
      mSbbJar = new MSbbJar((org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbJar)jaxbPojo);
      isSlee11 = true;
    } 
    else {
      throw new SLEEException("unexpected class of jaxb pojo built: "+(jaxbPojo != null ? jaxbPojo.getClass() : null));
    }

    MSecurityPermissions mSbbJarSecurityPermissions = mSbbJar.getSecurityPermissions();
    for (MSbb mSbb : mSbbJar.getSbb())
    {
      result.add(new SbbDescriptorImpl(mSbb, mSbbJarSecurityPermissions, isSlee11));
    }

    return result;
  }
}
