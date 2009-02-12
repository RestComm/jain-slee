package org.mobicents.slee.container.component.deployment.jaxb.descriptors.service;

/**
 * Start time:11:40:22 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MServiceXML {

  private org.mobicents.slee.container.component.deployment.jaxb.slee.service.ServiceXml serviceXML10;
  private org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml serviceXML11;
  
  private String description;
	
  public MServiceXML(org.mobicents.slee.container.component.deployment.jaxb.slee.service.ServiceXml serviceXML10)
  {
    this.serviceXML10 = serviceXML10;
    
    this.description = serviceXML10.getDescription() == null ? null : serviceXML10.getDescription().getvalue();
  }
	
  public MServiceXML(org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml serviceXML11)
  {
    this.serviceXML11 = serviceXML11;
    
    this.description = serviceXML11.getDescription() == null ? null : serviceXML11.getDescription().getvalue();
  }
}
