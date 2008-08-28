package org.mobicents.slee.container.management.jmx;

import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.Version;

public class MobicentsManagement extends ServiceMBeanSupport implements MobicentsManagementMBean {
  
  // Number of minutes after lingering entities of inactive service will be removed.
  public static double entitiesRemovalDelay = 1;
  // mobicents version
  private String mobicentsVersion = Version.instance.toString();
  
  public double getEntitiesRemovalDelay()
  {
    return MobicentsManagement.entitiesRemovalDelay;
  }

  public void setEntitiesRemovalDelay(double entitiesRemovalDelay)
  {
    MobicentsManagement.entitiesRemovalDelay = entitiesRemovalDelay;
  }

  public String getVersion() {
	return mobicentsVersion;
  }
}
