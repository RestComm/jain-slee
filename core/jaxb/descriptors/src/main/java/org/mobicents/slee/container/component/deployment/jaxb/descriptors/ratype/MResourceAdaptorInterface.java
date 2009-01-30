package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype;

/**
 * 
 * MResourceAdaptorInterface.java
 *
 * <br>Project:  mobicents
 * <br>5:33:32 PM Jan 21, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorInterface {

  private org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorInterface resourceAdaptorInterface10 = null;
  private org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorInterface resourceAdaptorInterface11 = null;
  
  private String description;
  private String resourceAdaptorInterfaceName;

  public MResourceAdaptorInterface(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorInterface resourceAdaptorInterface10)
  {
    super();
    
    this.resourceAdaptorInterface10 = resourceAdaptorInterface10;
    
    this.description = resourceAdaptorInterface10.getDescription() == null ? null : resourceAdaptorInterface10.getDescription().getvalue();
    this.resourceAdaptorInterfaceName = resourceAdaptorInterface10.getResourceAdaptorInterfaceName().getvalue(); 
  }
  
  public MResourceAdaptorInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorInterface resourceAdaptorInterface11)
  {
    super();
    
    this.resourceAdaptorInterface11 = resourceAdaptorInterface11;
    
    this.description = resourceAdaptorInterface11.getDescription() == null ? null : resourceAdaptorInterface11.getDescription().getvalue();
    this.resourceAdaptorInterfaceName = resourceAdaptorInterface11.getResourceAdaptorInterfaceName().getvalue(); 
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getResourceAdaptorInterfaceName()
  {
    return resourceAdaptorInterfaceName;
  }

}
