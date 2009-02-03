package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

/**
 * 
 * MResourceAdaptorTypeRef.java
 *
 * <br>Project:  mobicents
 * <br>6:19:43 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorTypeRef {

  private org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptorTypeRef resourceAdaptorTypeRef10;
  private org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorTypeRef resourceAdaptorTypeRef11;
  
  protected String description;
  protected String resourceAdaptorTypeName;
  protected String resourceAdaptorTypeVendor;
  protected String resourceAdaptorTypeVersion;
  
  public MResourceAdaptorTypeRef(org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptorTypeRef resourceAdaptorTypeRef10)
  {
    this.resourceAdaptorTypeRef10 = resourceAdaptorTypeRef10;
    
    this.description = resourceAdaptorTypeRef10.getDescription() == null ? null : resourceAdaptorTypeRef10.getDescription().getvalue();
    
    this.resourceAdaptorTypeName = resourceAdaptorTypeRef10.getResourceAdaptorTypeName().getvalue();
    this.resourceAdaptorTypeVendor = resourceAdaptorTypeRef10.getResourceAdaptorTypeVendor().getvalue();
    this.resourceAdaptorTypeVersion = resourceAdaptorTypeRef10.getResourceAdaptorTypeVersion().getvalue();
  }
  
  public MResourceAdaptorTypeRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorTypeRef resourceAdaptorTypeRef11)
  {
    this.resourceAdaptorTypeRef11 = resourceAdaptorTypeRef11;
    
    this.description = resourceAdaptorTypeRef11.getDescription() == null ? null : resourceAdaptorTypeRef11.getDescription().getvalue();
    
    this.resourceAdaptorTypeName = resourceAdaptorTypeRef11.getResourceAdaptorTypeName().getvalue();
    this.resourceAdaptorTypeVendor = resourceAdaptorTypeRef11.getResourceAdaptorTypeVendor().getvalue();
    this.resourceAdaptorTypeVersion = resourceAdaptorTypeRef11.getResourceAdaptorTypeVersion().getvalue();
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getResourceAdaptorTypeName()
  {
    return resourceAdaptorTypeName;
  }
  
  public String getResourceAdaptorTypeVendor()
  {
    return resourceAdaptorTypeVendor;
  }
  
  public String getResourceAdaptorTypeVersion()
  {
    return resourceAdaptorTypeVersion;
  }

}
