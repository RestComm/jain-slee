package org.mobicents.slee.container.component.deployment.jaxb.descriptors.library;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;

/**
 * 
 * MLibrary.java
 *
 * <br>Project:  mobicents
 * <br>3:19:02 AM Jan 30, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MLibrary {

  private String description;

  private List<MLibraryRef> libraryRef = new ArrayList<MLibraryRef>();

  private String libraryName;
  private String libraryVendor;
  private String libraryVersion;

  private List<MJar> jar = new ArrayList<MJar>();

  public MLibrary(org.mobicents.slee.container.component.deployment.jaxb.slee11.library.Library library11)
  {

    this.description = library11.getDescription() == null ? null : library11.getDescription().getvalue();

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.library.LibraryRef libraryRef11 : library11.getLibraryRef())
    {
      this.libraryRef.add( new MLibraryRef(libraryRef11) );
    }

    this.libraryName = library11.getLibraryName().getvalue();
    this.libraryVendor = library11.getLibraryVendor().getvalue();
    this.libraryVersion = library11.getLibraryVersion().getvalue();

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.library.Jar jar11 : library11.getJar())
    {
      this.jar.add( new MJar(jar11) );
    }
  }

  public String getDescription()
  {
    return description;
  }

  public List<MLibraryRef> getLibraryRef()
  {
    return libraryRef;
  }

  public String getLibraryName()
  {
    return libraryName;
  }

  public String getLibraryVendor()
  {
    return libraryVendor;
  }

  public String getLibraryVersion()
  {
    return libraryVersion;
  }
  
  public List<MJar> getJar()
  {
    return jar;
  }

}
