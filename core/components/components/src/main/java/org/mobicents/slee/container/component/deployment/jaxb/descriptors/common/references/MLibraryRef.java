package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references;

import javax.slee.management.LibraryID;

/**
 * 
 * MLibraryRef.java
 *
 * <br>Project:  mobicents
 * <br>11:24:43 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MLibraryRef {

  private String description;
  private String libraryName;
  private String libraryVendor;
  private String libraryVersion;

  private LibraryID libraryID;
  
  public MLibraryRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.event.LibraryRef libraryRef11)
  {
    this.description = libraryRef11.getDescription() == null ? null : libraryRef11.getDescription().getvalue();
    this.libraryName = libraryRef11.getLibraryName().getvalue();
    this.libraryVendor = libraryRef11.getLibraryVendor().getvalue();
    this.libraryVersion = libraryRef11.getLibraryVersion().getvalue();
    
    this.libraryID = new LibraryID(this.libraryName, this.libraryVendor, this.libraryVersion);
  }
  
  public MLibraryRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.LibraryRef libraryRef11)
  {
    this.description = libraryRef11.getDescription() == null ? null : libraryRef11.getDescription().getvalue();
    this.libraryName = libraryRef11.getLibraryName().getvalue();
    this.libraryVendor = libraryRef11.getLibraryVendor().getvalue();
    this.libraryVersion = libraryRef11.getLibraryVersion().getvalue();

    this.libraryID = new LibraryID(this.libraryName, this.libraryVendor, this.libraryVersion);
  }
  
  public MLibraryRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.LibraryRef libraryRef11)
  {
    this.description = libraryRef11.getDescription() == null ? null : libraryRef11.getDescription().getvalue();
    this.libraryName = libraryRef11.getLibraryName().getvalue();
    this.libraryVendor = libraryRef11.getLibraryVendor().getvalue();
    this.libraryVersion = libraryRef11.getLibraryVersion().getvalue();

    this.libraryID = new LibraryID(this.libraryName, this.libraryVendor, this.libraryVersion);
  }

  public MLibraryRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.LibraryRef libraryRef11)
  {
    this.description = libraryRef11.getDescription() == null ? null : libraryRef11.getDescription().getvalue();
    this.libraryName = libraryRef11.getLibraryName().getvalue();
    this.libraryVendor = libraryRef11.getLibraryVendor().getvalue();
    this.libraryVersion = libraryRef11.getLibraryVersion().getvalue();

    this.libraryID = new LibraryID(this.libraryName, this.libraryVendor, this.libraryVersion);
  }

  public MLibraryRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.library.LibraryRef libraryRef11)
  {
    this.description = libraryRef11.getDescription() == null ? null : libraryRef11.getDescription().getvalue();
    this.libraryName = libraryRef11.getLibraryName().getvalue();
    this.libraryVendor = libraryRef11.getLibraryVendor().getvalue();
    this.libraryVersion = libraryRef11.getLibraryVersion().getvalue();

    this.libraryID = new LibraryID(this.libraryName, this.libraryVendor, this.libraryVersion);
  }

  public MLibraryRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.LibraryRef libraryRef11)
  {
    this.description = libraryRef11.getDescription() == null ? null : libraryRef11.getDescription().getvalue();
    this.libraryName = libraryRef11.getLibraryName().getvalue();
    this.libraryVendor = libraryRef11.getLibraryVendor().getvalue();
    this.libraryVersion = libraryRef11.getLibraryVersion().getvalue();

    this.libraryID = new LibraryID(this.libraryName, this.libraryVendor, this.libraryVersion);
  }

  public String getDescription()
  {
    return description;
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
  
  public LibraryID getComponentID()
  {
    return this.libraryID;
  }
  
}
