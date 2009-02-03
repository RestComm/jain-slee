package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * MResourceAdaptorTypeClasses.java
 *
 * <br>Project:  mobicents
 * <br>6:15:52 PM Jan 21, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorTypeClasses
{
  private org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorTypeClasses resourceAdaptorTypeClasses10;
  private org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorTypeClasses resourceAdaptorTypeClasses11;
  
  private String description;
  private List<MActivityType> activityType;
  private MActivityContextInterfaceFactoryInterface activityContextInterfaceFactoryInterface;
  private MResourceAdaptorInterface resourceAdaptorInterface;

  public MResourceAdaptorTypeClasses(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorTypeClasses resourceAdaptorTypeClasses10)
  {
    this.resourceAdaptorTypeClasses10 = resourceAdaptorTypeClasses10;
    
    this.description = resourceAdaptorTypeClasses10.getDescription() == null ? null : resourceAdaptorTypeClasses10.getDescription().getvalue();
    this.activityType = new ArrayList<MActivityType>();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ActivityType aT : resourceAdaptorTypeClasses10.getActivityType())
    {
      this.activityType.add( new MActivityType(aT) );
    }
    
    this.resourceAdaptorInterface = resourceAdaptorTypeClasses10.getResourceAdaptorInterface() == null ? null : new MResourceAdaptorInterface(resourceAdaptorTypeClasses10.getResourceAdaptorInterface()); 
  }

  public MResourceAdaptorTypeClasses(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorTypeClasses resourceAdaptorTypeClasses11)
  {
    this.resourceAdaptorTypeClasses11 = resourceAdaptorTypeClasses11;
    
    this.description = resourceAdaptorTypeClasses11.getDescription() == null ? null : resourceAdaptorTypeClasses11.getDescription().getvalue();
    this.activityType = new ArrayList<MActivityType>();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ActivityType aT : resourceAdaptorTypeClasses11.getActivityType())
    {
      this.activityType.add( new MActivityType(aT) );
    }
    
    this.resourceAdaptorInterface = resourceAdaptorTypeClasses11.getResourceAdaptorInterface() == null ? null : new MResourceAdaptorInterface(resourceAdaptorTypeClasses11.getResourceAdaptorInterface()); 
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public List<MActivityType> getActivityType()
  {
    return activityType;
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
