package org.mobicents.slee.container.component.deployment.jaxb.descriptors.library;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;

/**
 * 
 * MLibraryJar.java
 *
 * <br>Project:  mobicents
 * <br>3:20:40 AM Jan 30, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MLibraryJar {

  private String description;
  
  private List<MLibrary> library = new ArrayList<MLibrary>();

  private MSecurityPermissions securityPermissions;

  public MLibraryJar(org.mobicents.slee.container.component.deployment.jaxb.slee11.library.LibraryJar libraryJar11)
  {
    this.description = libraryJar11.getDescription() == null ? null : libraryJar11.getDescription().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.library.Library library11 : libraryJar11.getLibrary())
    {
      this.library.add( new MLibrary(library11) );
    }
    
    this.securityPermissions = libraryJar11.getSecurityPermissions() == null ? null : new MSecurityPermissions(libraryJar11.getSecurityPermissions());
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public List<MLibrary> getLibrary()
  {
    return library;
  }
  
  public MSecurityPermissions getSecurityPermissions()
  {
    return securityPermissions;
  }
}
