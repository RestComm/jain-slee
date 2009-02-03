package org.mobicents.slee.container.component.deployment.jaxb.descriptors.library;

/**
 * 
 * MJar.java
 *
 * <br>Project:  mobicents
 * <br>3:34:29 AM Jan 30, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MJar {

  private org.mobicents.slee.container.component.deployment.jaxb.slee11.library.Jar jar11;
  
  private String description;
  
  private String jarName;
  
  private MSecurityPermissions securityPermissions;
  
  public MJar(org.mobicents.slee.container.component.deployment.jaxb.slee11.library.Jar jar11)
  {
    this.jar11 = jar11;
    
    this.description = jar11.getDescription() == null ? null : jar11.getDescription().getvalue();
    
    this.jarName = jar11.getJarName().getvalue();
    
    this.securityPermissions = new MSecurityPermissions(jar11.getSecurityPermissions());
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getJarName()
  {
    return jarName;
  }
  
  public MSecurityPermissions getSecurityPermissions()
  {
    return securityPermissions;
  }
  
}
