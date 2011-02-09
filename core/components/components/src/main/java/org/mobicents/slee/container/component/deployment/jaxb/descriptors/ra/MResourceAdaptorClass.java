package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

/**
 * 
 * MResourceAdaptorClass.java
 *
 * <br>Project:  mobicents
 * <br>4:47:51 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorClass {
  
  protected boolean supportsActiveReconfiguration = false;
  protected String description;
  protected String resourceAdaptorClassName;

  public MResourceAdaptorClass(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorClass resourceAdaptorClass11)
  {    
    if (resourceAdaptorClass11.getSupportsActiveReconfiguration() != null) {
    	this.supportsActiveReconfiguration = Boolean.parseBoolean(resourceAdaptorClass11.getSupportsActiveReconfiguration());
    }
    
    this.resourceAdaptorClassName = resourceAdaptorClass11.getResourceAdaptorClassName().getvalue();
  }
  
  public boolean getSupportsActiveReconfiguration()
  {
    return supportsActiveReconfiguration;
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getResourceAdaptorClassName()
  {
    return resourceAdaptorClassName;
  }
}
