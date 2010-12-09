package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParametersInterface;

/**
 * 
 * MResourceAdaptorClasses.java
 *
 * <br>Project:  mobicents
 * <br>6:06:26 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorClasses {

  // For JAIN SLEE 1.0
  private String resourceAdaptorClasses;
  
  // For JAIN SLEE 1.1
  private String description;
  private MResourceAdaptorClass resourceAdaptorClass;
  private MUsageParametersInterface resourceAdaptorUsageParametersInterface;

  public MResourceAdaptorClasses(org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptorClasses resourceAdaptorClasses10)
  {    
    this.resourceAdaptorClasses = resourceAdaptorClasses10.getvalue();
  }
  
  public MResourceAdaptorClasses(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorClasses resourceAdaptorClasses11)
  {
    this.description = resourceAdaptorClasses11.getDescription() == null ? null : resourceAdaptorClasses11.getDescription().getvalue();
    this.resourceAdaptorClass = new MResourceAdaptorClass(resourceAdaptorClasses11.getResourceAdaptorClass());
    this.resourceAdaptorUsageParametersInterface = resourceAdaptorClasses11.getResourceAdaptorUsageParametersInterface() == null ? null : new MUsageParametersInterface(resourceAdaptorClasses11.getResourceAdaptorUsageParametersInterface());
  }
  
  public String getResourceAdaptorClasses()
  {
    return resourceAdaptorClasses;
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public MResourceAdaptorClass getResourceAdaptorClass()
  {
    return resourceAdaptorClass;
  }
  
  public MUsageParametersInterface getResourceAdaptorUsageParametersInterface()
  {
    return resourceAdaptorUsageParametersInterface;
  }
}
