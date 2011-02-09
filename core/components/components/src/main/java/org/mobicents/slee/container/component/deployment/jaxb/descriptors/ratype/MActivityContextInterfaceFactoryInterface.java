package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype;

/**
 * 
 * MActivityContextInterfaceFactoryInterface.java
 *
 * <br>Project:  mobicents
 * <br>5:32:07 PM Jan 21, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 */
public class MActivityContextInterfaceFactoryInterface
{
  
  private String description;
  private String activityContextInterfaceFactoryInterfaceName;

  public MActivityContextInterfaceFactoryInterface(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ActivityContextInterfaceFactoryInterface activityContextInterfaceFactoryInterface10)
  { 
    this.description = activityContextInterfaceFactoryInterface10.getDescription() == null ? null : activityContextInterfaceFactoryInterface10.getDescription().getvalue();
    this.activityContextInterfaceFactoryInterfaceName = activityContextInterfaceFactoryInterface10.getActivityContextInterfaceFactoryInterfaceName().getvalue(); 
  }

  public MActivityContextInterfaceFactoryInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ActivityContextInterfaceFactoryInterface activityContextInterfaceFactoryInterface11)
  {    
    this.description = activityContextInterfaceFactoryInterface11.getDescription() == null ? null : activityContextInterfaceFactoryInterface11.getDescription().getvalue();
    this.activityContextInterfaceFactoryInterfaceName = activityContextInterfaceFactoryInterface11.getActivityContextInterfaceFactoryInterfaceName().getvalue(); 
  }

  public String getDescription()
  {
    return description;
  }
  
  public String getActivityContextInterfaceFactoryInterfaceName()
  {
    return activityContextInterfaceFactoryInterfaceName;
  }
}
