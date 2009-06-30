package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

/**
 * 
 * MConfigProperty.java
 *
 * <br>Project:  mobicents
 * <br>12:42:23 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MConfigProperty {
  
  private String description;
  private String configPropertyName;
  private String configPropertyType;
  private String configPropertyValue;
  
  public MConfigProperty(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ConfigProperty configProperty11)
  {    
    this.description = configProperty11.getDescription() == null ? null : configProperty11.getDescription().getvalue();
    
    this.configPropertyName = configProperty11.getConfigPropertyName().getvalue();
    this.configPropertyType = configProperty11.getConfigPropertyType().getvalue();
    this.configPropertyValue = configProperty11.getConfigPropertyValue() == null ? null : configProperty11.getConfigPropertyValue().getvalue();
  }

  public String getDescription()
  {
    return description;
  }
  
  public String getConfigPropertyName()
  {
    return configPropertyName;
  }
  
  public String getConfigPropertyType()
  {
    return configPropertyType;
  }
  
  public String getConfigPropertyValue()
  {
    return configPropertyValue;
  }
}
