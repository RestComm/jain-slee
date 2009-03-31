package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MEventTypeRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;

/**
 * 
 * MResourceAdaptorType.java
 *
 * <br>Project:  mobicents
 * <br>11:50:43 AM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorType {

  private String description;
  private String resourceAdaptorTypeName;
  private String resourceAdaptorTypeVendor;
  private String resourceAdaptorTypeVersion;
  private List<MLibraryRef> libraryRef = new ArrayList<MLibraryRef>();;
  private MResourceAdaptorTypeClasses resourceAdaptorTypeClasses;
  private List<MEventTypeRef> eventTypeRef = new ArrayList<MEventTypeRef>();
  
  public MResourceAdaptorType(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorType resourceAdaptorType10)
  {
    
    this.description = resourceAdaptorType10.getDescription() == null ? null : resourceAdaptorType10.getDescription().getvalue();
    
    this.resourceAdaptorTypeName = resourceAdaptorType10.getResourceAdaptorTypeName().getvalue();
    this.resourceAdaptorTypeVendor = resourceAdaptorType10.getResourceAdaptorTypeVendor().getvalue();
    this.resourceAdaptorTypeVersion = resourceAdaptorType10.getResourceAdaptorTypeVersion().getvalue();
        
    this.resourceAdaptorTypeClasses = new MResourceAdaptorTypeClasses(resourceAdaptorType10.getResourceAdaptorTypeClasses());
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.EventTypeRef eventTypeRef10 : resourceAdaptorType10.getEventTypeRef())
    {
      this.eventTypeRef.add( new MEventTypeRef(eventTypeRef10) );
    }
  }
  
  public MResourceAdaptorType(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorType resourceAdaptorType11)
  {
    
    this.description = resourceAdaptorType11.getDescription() == null ? null : resourceAdaptorType11.getDescription().getvalue();
    
    this.resourceAdaptorTypeName = resourceAdaptorType11.getResourceAdaptorTypeName().getvalue();
    this.resourceAdaptorTypeVendor = resourceAdaptorType11.getResourceAdaptorTypeVendor().getvalue();
    this.resourceAdaptorTypeVersion = resourceAdaptorType11.getResourceAdaptorTypeVersion().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.LibraryRef libraryRef11 : resourceAdaptorType11.getLibraryRef())
    {
      this.libraryRef.add( new MLibraryRef(libraryRef11) );
    }
    
    this.resourceAdaptorTypeClasses = new MResourceAdaptorTypeClasses(resourceAdaptorType11.getResourceAdaptorTypeClasses());
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.EventTypeRef eventTypeRef11 : resourceAdaptorType11.getEventTypeRef())
    {
      this.eventTypeRef.add( new MEventTypeRef(eventTypeRef11) );
    }
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getResourceAdaptorTypeName()
  {
    return resourceAdaptorTypeName;
  }
  
  public String getResourceAdaptorTypeVendor()
  {
    return resourceAdaptorTypeVendor;
  }
  
  public String getResourceAdaptorTypeVersion()
  {
    return resourceAdaptorTypeVersion;
  }
  
  public List<MLibraryRef> getLibraryRef()
  {
    return libraryRef;
  }
  
  public MResourceAdaptorTypeClasses getResourceAdaptorTypeClasses()
  {
    return resourceAdaptorTypeClasses;
  }
  
  public List<MEventTypeRef> getEventTypeRef()
  {
    return eventTypeRef;
  }
  
}
