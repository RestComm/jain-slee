package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MActivityContextInterfaceFactoryInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MActivityType;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MEventTypeRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorType;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorTypeClasses;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorTypeJar;
import org.w3c.dom.Document;

/**
 * 
 * ResourceAdaptorTypeDescriptorImpl.java
 *
 * <br>Project:  mobicents
 * <br>5:24:59 PM Jan 23, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ResourceAdaptorTypeDescriptorImpl extends JAXBBaseUtilityClass {

  private MResourceAdaptorTypeJar resourceAdaptorTypeJar;
  
  private int index;
  
  private MResourceAdaptorType raType;

  private String description;
  
  private List<MEventTypeRef> eventTypeRefs;
  private List<MActivityType> activityTypes;
  private List<MLibraryRef> libraryRefs;
  
  private MActivityContextInterfaceFactoryInterface activityContextInterfaceFactoryInterface;
  private MResourceAdaptorInterface resourceAdaptorInterface;
  
  public ResourceAdaptorTypeDescriptorImpl(Document doc)
  {
    super(doc);
  }
  
  /**
   * Constructor for JAIN SLEE 1.0 RA Type
   * 
   * @param doc
   * @param resourceAdaptorTypeJar10
   * @param index
   */
  public ResourceAdaptorTypeDescriptorImpl(Document doc, org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorTypeJar resourceAdaptorTypeJar10, int index)
  {
    super(doc);
    
    this.resourceAdaptorTypeJar = new MResourceAdaptorTypeJar(resourceAdaptorTypeJar10);
    this.index = index;
    
    this.buildDescriptionMap();
  }

  /**
   * Constructor for JAIN SLEE 1.1 RA Type
   * 
   * @param doc
   * @param resourceAdaptorTypeJar11
   * @param index
   */
  public ResourceAdaptorTypeDescriptorImpl(Document doc, org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorTypeJar resourceAdaptorTypeJar11, int index)
  {
    super(doc);
    
    this.resourceAdaptorTypeJar = new MResourceAdaptorTypeJar(resourceAdaptorTypeJar11);
    this.index = index;

    this.buildDescriptionMap();
  }

  @Override
  public void buildDescriptionMap()
  {
    this.raType = this.resourceAdaptorTypeJar.getResourceAdaptorType().get(index);
    this.description = this.raType.getDescription();
    
    MResourceAdaptorTypeClasses resourceAdaptorTypeClasses = this.raType.getResourceAdaptorTypeClasses(); 
    
    this.eventTypeRefs = this.raType.getEventTypeRef();
    this.activityTypes = resourceAdaptorTypeClasses.getActivityType();
    this.libraryRefs = this.raType.getLibraryRef();
    
    this.activityContextInterfaceFactoryInterface = resourceAdaptorTypeClasses.getActivityContextInterfaceFactoryInterface();
    this.resourceAdaptorInterface = resourceAdaptorTypeClasses.getResourceAdaptorInterface();
  }

  @Override
  public Object getJAXBDescriptor()
  {
    return resourceAdaptorTypeJar;
  }
  
  public MResourceAdaptorType getRaType()
  {
    return raType;
  }
  
  public String getDescription()
  {
    return description;
  }

  public List<MEventTypeRef> getEventTypeRefs()
  {
    return eventTypeRefs;
  }
  
  public List<MActivityType> getActivityTypes()
  {
    return activityTypes;
  }
  
  public List<MLibraryRef> getLibraryRefs()
  {
    return libraryRefs;
  }
  
  public MActivityContextInterfaceFactoryInterface getActivityContextInterfaceFactoryInterface()
  {
    return activityContextInterfaceFactoryInterface;
  }
  
  public MResourceAdaptorInterface getResourceAdaptorInterface()
  {
    return resourceAdaptorInterface;
  }
}
