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
  
  public static Boolean monitoringUncommittedAcAttachs;
  
  public boolean isMonitoringUncommittedAcAttachs() {
	  return MobicentsManagement.monitoringUncommittedAcAttachs;
  }

  public void setMonitoringUncommittedAcAttachs(
		  boolean monitoringUncommittedAcAttachs) {
	  if (MobicentsManagement.monitoringUncommittedAcAttachs != null) {
		  logger.warn("Setting event router monitoring of uncommitted activity context attaches to "+monitoringUncommittedAcAttachs+". If called with server running a stop and start is need to apply changes.");
	  }	  
	  MobicentsManagement.monitoringUncommittedAcAttachs = monitoringUncommittedAcAttachs;
  }

  public static Integer eventRouterExecutors;
  
  public int getEventRouterExecutors() {
	return MobicentsManagement.eventRouterExecutors;
  }
  
  public void setEventRouterExecutors(int eventRouterExecutors) {
	  if (MobicentsManagement.eventRouterExecutors != null) {
		  logger.warn("Setting event router executors to "+eventRouterExecutors+". If called with server running a stop and start is need to apply changes.");
	  }	  
	  MobicentsManagement.eventRouterExecutors = eventRouterExecutors;
	  
  }
  
  public static boolean persistProfiles;

  public boolean getPersistProfiles() {
    return persistProfiles;
  }

  public void setPersistProfiles(boolean persist) {
	  persistProfiles = persist;
  }
}
