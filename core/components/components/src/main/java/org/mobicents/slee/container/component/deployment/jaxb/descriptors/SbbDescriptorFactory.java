package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbb;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbJar;

/**
 * 
 * Factory to build {@link SbbDescriptorImpl} objects.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:emmartins@gmail.com"> Eduardo Martins </a> 
 */
public class SbbDescriptorFactory extends AbstractDescriptorFactory {

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
