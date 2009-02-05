package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.UsageParameter;


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
public class MUsageParametersInterface {

  private org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorUsageParametersInterface resourceAdaptorUsageParametersInterface11;
  private SbbUsageParametersInterface sbbUsageParametersInterface=null;
  private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbUsageParametersInterface llSbbUsageParametersInterface=null;
  private ProfileUsageParametersInterface profileUsageParametersInterface=null;
  private String description;
  private String usageParametersInterfaceName;
  private List<MUsageParameter> usageParameter = new ArrayList<MUsageParameter>();

  public MUsageParametersInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorUsageParametersInterface resourceAdaptorUsageParametersInterface11)
  {
    this.resourceAdaptorUsageParametersInterface11 = resourceAdaptorUsageParametersInterface11;
    
    this.description = resourceAdaptorUsageParametersInterface11.getDescription().getvalue();
    this.usageParametersInterfaceName = resourceAdaptorUsageParametersInterface11.getResourceAdaptorUsageParametersInterfaceName().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.UsageParameter usageParameter11 : resourceAdaptorUsageParametersInterface11.getUsageParameter())
    {
      usageParameter.add( new MUsageParameter(usageParameter11) );
    }
  }
  
  
  public MUsageParametersInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbUsageParametersInterface llSbbUsageParametersInterface)
  {
	this.llSbbUsageParametersInterface=llSbbUsageParametersInterface;
    
    this.description = llSbbUsageParametersInterface.getDescription()==null?null:llSbbUsageParametersInterface.getDescription().getvalue();
    this.usageParametersInterfaceName = llSbbUsageParametersInterface.getSbbUsageParametersInterfaceName().getvalue();
    
    for(UsageParameter usageParameter11 : llSbbUsageParametersInterface.getUsageParameter())
    {
      usageParameter.add( new MUsageParameter(usageParameter11) );
    }
  }
  
  public MUsageParametersInterface(SbbUsageParametersInterface sbbUsageParametersInterface)
  {
    this.sbbUsageParametersInterface = sbbUsageParametersInterface;
    
    this.description = sbbUsageParametersInterface.getDescription()==null?null:sbbUsageParametersInterface.getDescription().getvalue();
    this.usageParametersInterfaceName = sbbUsageParametersInterface.getSbbUsageParametersInterfaceName().getvalue();
    
    //usage params are not present here
  }
  
  public MUsageParametersInterface(ProfileUsageParametersInterface profileUsageParametersInterface)
  {
    this.profileUsageParametersInterface = profileUsageParametersInterface;
    
    this.description = profileUsageParametersInterface.getDescription()==null?null:profileUsageParametersInterface.getDescription().getvalue();
    this.usageParametersInterfaceName = profileUsageParametersInterface.getProfileUsageParametersInterfaceName().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.UsageParameter usageParameter11 : profileUsageParametersInterface.getUsageParameter())
    {
      usageParameter.add( new MUsageParameter(usageParameter11) );
    }
  }
  
  
  public String getDescription()
  {
    return description;
  }
  
  public String getUsageParametersInterfaceName()
  {
    return usageParametersInterfaceName;
  }
  
  public List<MUsageParameter> getUsageParameter()
  {
    return usageParameter;
  }
}
