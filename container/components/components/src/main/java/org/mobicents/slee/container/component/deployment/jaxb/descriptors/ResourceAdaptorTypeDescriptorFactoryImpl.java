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
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorType;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorTypeJar;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeDescriptorFactory;

/**
 * 
 * Factory to build {@link ResourceAdaptorTypeDescriptorImpl} objects.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ResourceAdaptorTypeDescriptorFactoryImpl extends AbstractDescriptorFactory implements ResourceAdaptorTypeDescriptorFactory {

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.core.component.ratype.ResourceAdaptorTypeDescriptorFactory#parse(java.io.InputStream)
   */
  public List<ResourceAdaptorTypeDescriptorImpl> parse(InputStream inputStream) throws DeploymentException
  {
    Object jaxbPojo = buildJAXBPojo(inputStream);

    List<ResourceAdaptorTypeDescriptorImpl> result = new ArrayList<ResourceAdaptorTypeDescriptorImpl>();

    boolean isSlee11 = false;
    MResourceAdaptorTypeJar mResourceAdaptorTypeJar = null;
    if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorTypeJar)
    {
      mResourceAdaptorTypeJar = new MResourceAdaptorTypeJar((org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorTypeJar)jaxbPojo);
    }
    else if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorTypeJar )
    {
      mResourceAdaptorTypeJar = new MResourceAdaptorTypeJar((org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorTypeJar)jaxbPojo);
      isSlee11 = true;
    } 
    else {
      throw new SLEEException("unexpected class of jaxb pojo built: "+(jaxbPojo != null ? jaxbPojo.getClass() : null));
    }

    for (MResourceAdaptorType mResourceAdaptorType : mResourceAdaptorTypeJar.getResourceAdaptorType())
    {
      result.add(new ResourceAdaptorTypeDescriptorImpl(mResourceAdaptorType, isSlee11));
    }

    return result;
  }
}
