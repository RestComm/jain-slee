package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.DeploymentException;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MEventTypeRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MActivityContextInterfaceFactoryInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MActivityType;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorType;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorTypeClasses;

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
public class ResourceAdaptorTypeDescriptorImpl {

  private ResourceAdaptorTypeID resourceAdaptorTypeID;

  private String description;

  private List<MEventTypeRef> eventTypeRefs;
  private List<MActivityType> activityTypes;
  private List<MLibraryRef> libraryRefs;

  private MActivityContextInterfaceFactoryInterface activityContextInterfaceFactoryInterface;
  private MResourceAdaptorInterface resourceAdaptorInterface;

  private boolean isSlee11;

  private Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();

  /**
   * Constructor for JAIN SLEE RA Type
   * 
   * @param resourceAdaptorType
   * @param isSlee11
   */
  public ResourceAdaptorTypeDescriptorImpl( MResourceAdaptorType resourceAdaptorType, boolean isSlee11 ) throws DeploymentException
  {
    try
    {
      this.description = resourceAdaptorType.getDescription();

      this.resourceAdaptorTypeID = new ResourceAdaptorTypeID(resourceAdaptorType.getResourceAdaptorTypeName(), 
          resourceAdaptorType.getResourceAdaptorTypeVendor(), resourceAdaptorType.getResourceAdaptorTypeVersion());

      MResourceAdaptorTypeClasses resourceAdaptorTypeClasses = resourceAdaptorType.getResourceAdaptorTypeClasses(); 

      this.eventTypeRefs = resourceAdaptorType.getEventTypeRef();
      this.activityTypes = resourceAdaptorTypeClasses.getActivityType();
      this.libraryRefs = resourceAdaptorType.getLibraryRef();

      this.activityContextInterfaceFactoryInterface = resourceAdaptorTypeClasses.getActivityContextInterfaceFactoryInterface();
      this.resourceAdaptorInterface = resourceAdaptorTypeClasses.getResourceAdaptorInterface();

      buildDependenciesSet();
    }
    catch (Exception e) {
      throw new DeploymentException("Failed to build Resource Adaptot Type descriptor", e);
    }
  }


  private void buildDependenciesSet()
  {
    for(MEventTypeRef eventTypeRef : eventTypeRefs)
    {
      this.dependenciesSet.add( eventTypeRef.getComponentID() );
    }

    for(MLibraryRef libraryRef : libraryRefs)
    {
      this.dependenciesSet.add( libraryRef.getComponentID() );
    }
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

  public ResourceAdaptorTypeID getResourceAdaptorTypeID()
  {
    return resourceAdaptorTypeID;
  }

  public Set<ComponentID> getDependenciesSet()
  {
    return this.dependenciesSet;
  }

  public boolean isSlee11()
  {
    return this.isSlee11;
  }

}
