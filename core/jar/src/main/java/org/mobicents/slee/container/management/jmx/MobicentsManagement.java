package org.mobicents.slee.container.management.jmx;

import org.jboss.system.ServiceMBeanSupport;

public class MobicentsManagement extends ServiceMBeanSupport implements MobicentsManagementMBean {
  
  // Number of minutes after lingering entities of inactive service will be removed.
  public static double entitiesRemovalDelay = 1;
  
  public double getEntitiesRemovalDelay()
  {
    return MobicentsManagement.entitiesRemovalDelay;
  }

  public void setEntitiesRemovalDelay(double entitiesRemovalDelay)
  {
    MobicentsManagement.entitiesRemovalDelay = entitiesRemovalDelay;
  }

}
