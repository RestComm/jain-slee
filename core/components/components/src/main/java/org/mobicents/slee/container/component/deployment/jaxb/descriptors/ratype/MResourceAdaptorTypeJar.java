package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * MResourceAdaptorTypeJar.java
 *
 * <br>Project:  mobicents
 * <br>12:23:09 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorTypeJar {

  private String description;
  private List<MResourceAdaptorType> resourceAdaptorType = new ArrayList<MResourceAdaptorType>();

  public MResourceAdaptorTypeJar(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorTypeJar resourceAdaptorTypeJar10)
  {    
    this.description = resourceAdaptorTypeJar10.getDescription() == null ? null : resourceAdaptorTypeJar10.getDescription().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorType resourceAdaptorType10 : resourceAdaptorTypeJar10.getResourceAdaptorType())
    {
      this.resourceAdaptorType.add( new MResourceAdaptorType(resourceAdaptorType10) );
    }
  }
  
  public MResourceAdaptorTypeJar(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorTypeJar resourceAdaptorTypeJar11)
  {    
    this.description = resourceAdaptorTypeJar11.getDescription() == null ? null : resourceAdaptorTypeJar11.getDescription().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorType resourceAdaptorType11 : resourceAdaptorTypeJar11.getResourceAdaptorType())
    {
      this.resourceAdaptorType.add( new MResourceAdaptorType(resourceAdaptorType11) );
    }
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public List<MResourceAdaptorType> getResourceAdaptorType()
  {
    return resourceAdaptorType;
  }
  
}
