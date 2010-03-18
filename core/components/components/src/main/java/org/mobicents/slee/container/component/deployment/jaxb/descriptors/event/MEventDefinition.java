package org.mobicents.slee.container.component.deployment.jaxb.descriptors.event;

/**
 * 
 * MEventDefinition.java
 *
 * <br>Project:  mobicents
 * <br>11:19:48 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MEventDefinition {
  
  private String description;
  private String eventTypeName;
  private String eventTypeVendor;
  private String eventTypeVersion;
  private String eventClassName;
  
  public MEventDefinition(org.mobicents.slee.container.component.deployment.jaxb.slee.event.EventDefinition eventDefinition10)
  {    
    this.description = eventDefinition10.getDescription() == null ? null : eventDefinition10.getDescription().getvalue();
    
    this.eventTypeName = eventDefinition10.getEventTypeName().getvalue();
    this.eventTypeVendor = eventDefinition10.getEventTypeVendor().getvalue();
    this.eventTypeVersion = eventDefinition10.getEventTypeVersion().getvalue();
    
    this.eventClassName = eventDefinition10.getEventClassName().getvalue();
  }

  public MEventDefinition(org.mobicents.slee.container.component.deployment.jaxb.slee11.event.EventDefinition eventDefinition11)
  {    
    this.description = eventDefinition11.getDescription() == null ? null : eventDefinition11.getDescription().getvalue();
    
    this.eventTypeName = eventDefinition11.getEventTypeName().getvalue();
    this.eventTypeVendor = eventDefinition11.getEventTypeVendor().getvalue();
    this.eventTypeVersion = eventDefinition11.getEventTypeVersion().getvalue();
    
    this.eventClassName = eventDefinition11.getEventClassName().getvalue();
  }
  
  public String getDescription()
  {
    return description;
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
  
  public String getEventClassName()
  {
    return eventClassName;
  }

}
