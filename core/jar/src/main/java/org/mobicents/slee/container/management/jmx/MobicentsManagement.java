package org.mobicents.slee.container.management.jmx;

import javax.slee.management.ManagementException;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.Version;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class MobicentsManagement extends ServiceMBeanSupport implements MobicentsManagementMBean {
  
  private static final Logger logger = Logger.getLogger(MobicentsManagement.class);
	
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
  
  public String dumpState() throws ManagementException {
	 
	 SleeContainer sleeContainer =  SleeContainer.lookupFromJndi();
	 SleeTransactionManager txManager = sleeContainer.getTransactionManager();
	 try {
		 txManager.begin();
		 return sleeContainer.dumpState();
	 }
	 catch (Exception e) {
		 throw new ManagementException("Failed to get container state",e);
	 }
	 finally {
		 try {
			 txManager.commit();
		 }
		 catch (Exception e) {
			 throw new ManagementException("Failed to get container state",e);
		 }
	 }
  }
  
  public static Integer timerThreads;
  
  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.container.management.jmx.MobicentsManagementMBean#getTimerThreads()
   */
  public int getTimerThreads() {
	  return MobicentsManagement.timerThreads;
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.container.management.jmx.MobicentsManagementMBean#setTimerThreads(int)
   */
  public void setTimerThreads(
		  int value) {
	  if (MobicentsManagement.timerThreads != null) {
		  logger.warn("Setting event router monitoring of uncommitted activity context attaches to "+value+". If called with server running a stop and start is need to apply changes.");
	  }	  
	  MobicentsManagement.timerThreads = value;
  }

  public static Integer eventRouterThreads;
  
  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.container.management.jmx.MobicentsManagementMBean#getEventRouterThreads()
   */
  public int getEventRouterThreads() {
	return MobicentsManagement.eventRouterThreads;
  }
  
  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.container.management.jmx.MobicentsManagementMBean#setEventRouterThreads(int)
   */
  public void setEventRouterThreads(int value) {
	  if (MobicentsManagement.eventRouterThreads != null) {
		  logger.warn("Setting event router threads to "+value+". If called with server running a stop and start is need to apply changes.");
	  }	  
	  MobicentsManagement.eventRouterThreads = value;
  }
  
  public static boolean loadClassesFirstFromAS = true;
  
  public boolean getLoadClassesFirstFromAS() {
	  return MobicentsManagement.loadClassesFirstFromAS;
  }

  public void setLoadClassesFirstFromAS(boolean value) {
	  MobicentsManagement.loadClassesFirstFromAS = value;
  }
  
}
