package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

import java.util.ArrayList;
import java.util.List;

import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MResourceAdaptorTypeRef;

/**
 * 
 * MResourceAdaptor.java
 *
 * <br>Project:  mobicents
 * <br>6:31:05 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptor {

  private String description;
  private String resourceAdaptorName;
  private String resourceAdaptorVendor;
  private String resourceAdaptorVersion;
  protected List<MResourceAdaptorTypeRef> resourceAdaptorTypeRef = new ArrayList<MResourceAdaptorTypeRef>();
  private MResourceAdaptorClasses resourceAdaptorClasses;

  // For JAIN SLEE 1.1 Only
  protected boolean ignoreRaTypeEventTypeCheck = false;
  private List<LibraryID> libraryRefs = new ArrayList<LibraryID>();
  protected List<MProfileSpecRef> profileSpecRef = new ArrayList<MProfileSpecRef>();
  protected List<MConfigProperty> configProperty = new ArrayList<MConfigProperty>();
  
  public MResourceAdaptor(org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptor resourceAdaptor10)
  {    
    this.description = resourceAdaptor10.getDescription() == null ? null : resourceAdaptor10.getDescription().getvalue();
    
    this.resourceAdaptorName = resourceAdaptor10.getResourceAdaptorName().getvalue();
    this.resourceAdaptorVendor = resourceAdaptor10.getResourceAdaptorVendor().getvalue();
    this.resourceAdaptorVersion = resourceAdaptor10.getResourceAdaptorVersion().getvalue();
    
    this.resourceAdaptorTypeRef.add( new MResourceAdaptorTypeRef(resourceAdaptor10.getResourceAdaptorTypeRef()) );
    
    this.resourceAdaptorClasses = new MResourceAdaptorClasses(resourceAdaptor10.getResourceAdaptorClasses());
  }
  
  public MResourceAdaptor(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptor resourceAdaptor11)
  {    
    this.description = resourceAdaptor11.getDescription() == null ? null : resourceAdaptor11.getDescription().getvalue();
    
    this.resourceAdaptorName = resourceAdaptor11.getResourceAdaptorName().getvalue();
    this.resourceAdaptorVendor = resourceAdaptor11.getResourceAdaptorVendor().getvalue();
    this.resourceAdaptorVersion = resourceAdaptor11.getResourceAdaptorVersion().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorTypeRef resourceAdaptorTypeRef11 : resourceAdaptor11.getResourceAdaptorTypeRef())
    {
      this.resourceAdaptorTypeRef.add( new MResourceAdaptorTypeRef(resourceAdaptorTypeRef11) );
    }
    
    if (resourceAdaptor11.getIgnoreRaTypeEventTypeCheck() != null) {
    	this.ignoreRaTypeEventTypeCheck = Boolean.parseBoolean( resourceAdaptor11.getIgnoreRaTypeEventTypeCheck() );
    }
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.LibraryRef libraryRef11 : resourceAdaptor11.getLibraryRef())
    {
    	this.libraryRefs.add(new LibraryID(libraryRef11.getLibraryName()
    			.getvalue(), libraryRef11.getLibraryVendor().getvalue(),
    			libraryRef11.getLibraryVersion().getvalue()));
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ConfigProperty configProperty11 : resourceAdaptor11.getConfigProperty())
    {
      this.configProperty.add( new MConfigProperty(configProperty11) );
    }
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ProfileSpecRef profileSpecRef : resourceAdaptor11.getProfileSpecRef())
    {
      this.profileSpecRef.add(new MProfileSpecRef(profileSpecRef));
    }
    
    this.resourceAdaptorClasses = new MResourceAdaptorClasses(resourceAdaptor11.getResourceAdaptorClasses());

  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getResourceAdaptorName()
  {
    return resourceAdaptorName;
  }
  
  public String getResourceAdaptorVendor()
  {
    return resourceAdaptorVendor;
  }
  
  public String getResourceAdaptorVersion()
  {
    return resourceAdaptorVersion;
  }
  
  public List<MResourceAdaptorTypeRef> getResourceAdaptorTypeRefs()
  {
    return resourceAdaptorTypeRef;
  }
  
  public MResourceAdaptorTypeRef getResourceAdaptorTypeRef()
  {
    return resourceAdaptorTypeRef.get(0);
  }
  
  public MResourceAdaptorClasses getResourceAdaptorClasses()
  {
    return resourceAdaptorClasses;
  }
  
  public boolean getIgnoreRaTypeEventTypeCheck()
  {
    return ignoreRaTypeEventTypeCheck;
  }
  
  public List<LibraryID> getLibraryRefs()
  {
    return libraryRefs;
  }
  
  public List<MProfileSpecRef> getProfileSpecRef()
  {
    return profileSpecRef;
  }
  
  public List<MConfigProperty> getConfigProperty()
  {
    return configProperty;
  }
}
