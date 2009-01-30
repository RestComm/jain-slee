package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * MResourceAdaptorUsageParametersInterface.java
 *
 * <br>Project:  mobicents
 * <br>5:42:05 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorUsageParametersInterface {

  private org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorUsageParametersInterface resourceAdaptorUsageParametersInterface11;
  
  private String description;
  private String resourceAdaptorUsageParametersInterfaceName;
  private List<MUsageParameter> usageParameter = new ArrayList<MUsageParameter>();

  public MResourceAdaptorUsageParametersInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorUsageParametersInterface resourceAdaptorUsageParametersInterface11)
  {
    this.resourceAdaptorUsageParametersInterface11 = resourceAdaptorUsageParametersInterface11;
    
    this.description = resourceAdaptorUsageParametersInterface11.getDescription().getvalue();
    this.resourceAdaptorUsageParametersInterfaceName = resourceAdaptorUsageParametersInterface11.getResourceAdaptorUsageParametersInterfaceName().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.UsageParameter usageParameter11 : resourceAdaptorUsageParametersInterface11.getUsageParameter())
    {
      usageParameter.add( new MUsageParameter(usageParameter11) );
    }
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getResourceAdaptorUsageParametersInterfaceName()
  {
    return resourceAdaptorUsageParametersInterfaceName;
  }
  
  public List<MUsageParameter> getUsageParameter()
  {
    return usageParameter;
  }
}
