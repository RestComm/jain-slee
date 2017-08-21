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
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpec;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpecJar;
import org.mobicents.slee.container.component.profile.ProfileSpecificationDescriptorFactory;

/**
 * Factory to build {@link ProfileSpecificationDescriptorImpl} objects.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ProfileSpecificationDescriptorFactoryImpl extends AbstractDescriptorFactory implements ProfileSpecificationDescriptorFactory {

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptorFactory#parse(java.io.InputStream)
   */
  public List<ProfileSpecificationDescriptorImpl> parse(InputStream inputStream) throws DeploymentException
  {
    Object jaxbPojo = buildJAXBPojo(inputStream);

    List<ProfileSpecificationDescriptorImpl> result = new ArrayList<ProfileSpecificationDescriptorImpl>();

    boolean isSlee11 = false;
    MProfileSpecJar mProfileSpecJar = null;
    if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileSpecJar)
    {
      mProfileSpecJar = new MProfileSpecJar((org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileSpecJar)jaxbPojo);
    }
    else if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecJar)
    {
      mProfileSpecJar = new MProfileSpecJar((org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecJar)jaxbPojo);
      isSlee11 = true;
    } 
    else {
      throw new SLEEException("unexpected class of jaxb pojo built: "+(jaxbPojo != null ? jaxbPojo.getClass() : null));
    }

    MSecurityPermissions securityPermissions = mProfileSpecJar.getSecurityPermissions();
    for (MProfileSpec mProfileSpec : mProfileSpecJar.getProfileSpec())
    {
      result.add(new ProfileSpecificationDescriptorImpl(mProfileSpec, securityPermissions, isSlee11));
    }

    return result;
  }
}
