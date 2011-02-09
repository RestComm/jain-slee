package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.library.MLibrary;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.library.MLibraryJar;
import org.mobicents.slee.container.component.library.LibraryDescriptorFactory;

/**
 * 
 * Factory to build {@link LibraryDescriptorImpl} objects.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class LibraryDescriptorFactoryImpl extends AbstractDescriptorFactory implements LibraryDescriptorFactory {

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.core.component.library.LibraryDescriptorFactory#parse(java.io.InputStream)
   */
  public List<LibraryDescriptorImpl> parse(InputStream inputStream) throws DeploymentException {

    Object jaxbPojo = buildJAXBPojo(inputStream);

    List<LibraryDescriptorImpl> result = new ArrayList<LibraryDescriptorImpl>();

    // Only exists in JAIN SLEE 1.1
    boolean isSlee11 = true;

    MLibraryJar mLibraryJar = null;
    if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.library.LibraryJar )
    {
      mLibraryJar = new MLibraryJar((org.mobicents.slee.container.component.deployment.jaxb.slee11.library.LibraryJar)jaxbPojo);
    } 
    else {
      throw new SLEEException("unexpected class of jaxb pojo built: "+(jaxbPojo != null ? jaxbPojo.getClass() : null));
    }

    MSecurityPermissions securityPermissions = mLibraryJar.getSecurityPermissions();
    for (MLibrary mLibrary : mLibraryJar.getLibrary())
    {
      result.add(new LibraryDescriptorImpl(mLibrary, securityPermissions, isSlee11));
    }
    
    return result;
  }
}
