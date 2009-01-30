package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype;

/**
 * 
 * MActivityType.java
 *
 * <br>Project:  mobicents
 * <br>5:43:59 PM Jan 21, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MActivityType
{
  private org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ActivityType activityType10;
  private org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ActivityType activityType11;
  
  private String description;
  private String activityTypeName;
  
  public MActivityType(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ActivityType activityType10)
  {
    super();
    
    this.activityType10 = activityType10;

    this.description = activityType10.getDescription() == null ? null : activityType10.getDescription().getvalue();
    this.activityTypeName = activityType10.getActivityTypeName().getvalue();
  }

  public MActivityType(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ActivityType activityType11)
  {
    super();
    
    this.activityType11 = activityType11;

    this.description = activityType11.getDescription() == null ? null : activityType11.getDescription().getvalue();
    this.activityTypeName = activityType11.getActivityTypeName().getvalue();
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getActivityTypeName()
  {
    return activityTypeName;
  }

}
