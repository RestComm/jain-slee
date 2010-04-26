package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.List;

import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.library.MLibrary;
import org.mobicents.slee.container.component.library.JarDescriptor;
import org.mobicents.slee.container.component.library.LibraryDescriptor;

/**
 * 
 * LibraryDescriptorImpl.java
 *
 * <br>Project:  mobicents
 * <br>3:52:21 AM Jan 30, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class LibraryDescriptorImpl extends AbstractComponentWithLibraryRefsDescriptor implements LibraryDescriptor {

  private final LibraryID libraryID;
  private final List<JarDescriptor> jars;
  private final String securityPermissions;
  
  public LibraryDescriptorImpl(MLibrary library, MSecurityPermissions mSecurityPermissions, boolean isSlee11) throws DeploymentException {
	  super(isSlee11);
	  super.setLibraryRefs(library.getLibraryRef());
	  try {
		  this.jars = library.getJar();
		  this.securityPermissions = mSecurityPermissions == null ? null : mSecurityPermissions.getSecurityPermissionSpec();
		  this.libraryID = new LibraryID(library.getLibraryName(), library.getLibraryVendor(),library.getLibraryVersion());
	  }
	  catch (Exception e) {
		  throw new DeploymentException("Failed to build library descriptor", e);      
	  }
  }
  
  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.core.component.library.LibraryDescriptor#getJars()
   */
  public List<JarDescriptor> getJars() {
    return jars;
  }
  
  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.core.component.library.LibraryDescriptor#getLibraryID()
   */
  public LibraryID getLibraryID() {
	return libraryID;
  }
  
  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.core.component.library.LibraryDescriptor#getSecurityPermissions()
   */
  public String getSecurityPermissions() {
    return this.securityPermissions;
  }
  
}
