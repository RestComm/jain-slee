package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype;

/**
 * 
 * MEventTypeRef.java
 *
 * <br>Project:  mobicents
 * <br>1:26:49 AM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MEventTypeRef {
  
  private org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.EventTypeRef eventTypeRef10;
  private org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.EventTypeRef eventTypeRef11;
  
  private String eventTypeName;
  private String eventTypeVendor;
  private String eventTypeVersion;

  public MEventTypeRef(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.EventTypeRef eventTypeRef10)
  {
    super();
    
    this.eventTypeRef10 = eventTypeRef10;
    
    this.eventTypeName = eventTypeRef10.getEventTypeName().getvalue();
    this.eventTypeVendor = eventTypeRef10.getEventTypeVendor().getvalue();
    this.eventTypeVersion = eventTypeRef10.getEventTypeVersion().getvalue();
  }

  public MEventTypeRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.EventTypeRef eventTypeRef11)
  {
    super();
    
    this.eventTypeRef11 = eventTypeRef11;
    
    this.eventTypeName = eventTypeRef11.getEventTypeName().getvalue();
    this.eventTypeVendor = eventTypeRef11.getEventTypeVendor().getvalue();
    this.eventTypeVersion = eventTypeRef11.getEventTypeVersion().getvalue();
  }

  public String getEventTypeName()
  {
    return eventTypeName;
  }
  
  public String getEventTypeVendor()
  {
    return eventTypeVendor;
  }
  
  public String getEventTypeVersion()
  {
    return eventTypeVersion;
  }
}
