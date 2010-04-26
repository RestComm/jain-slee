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
