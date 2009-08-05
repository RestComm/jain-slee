package org.mobicents.slee.container.management.jmx;

import javax.slee.management.ManagementException;

import org.jboss.system.ServiceMBean;

public interface MobicentsManagementMBean extends ServiceMBean {

	public double getEntitiesRemovalDelay();

	public void setEntitiesRemovalDelay(double entitiesRemovalDelay);

	/**
	 * Defines if the event router should monitor or not uncommitted modifications of AC attaches.
	 * 
	 * @see MobicentsManagementMBean#setMonitoringUncommittedAcAttachs(boolean)
	 * @return
	 */
	public boolean isMonitoringUncommittedAcAttachs();

	/**
	 * Flag that turns on or off the monitoring of uncommitted modifications of
	 * AC attaches in the container event router. When this flag is true, which means monitoring is on, if
	 * exist uncommitted attaches/detaches on the activity where one event is
	 * about to be routed, then that event (and others in queue) waits until no
	 * uncommitted modifications exist. If your apps don't suffer any
	 * concurrency issues on attach/detaches, and don't miss response events on
	 * activities after their creation,then turn this off to get more
	 * performance turn it off.
	 * 
	 * Note that setting a different value for this method will only be effective on server (re)start.
	 * 
	 * @param monitorUncommittedAcAttachs
	 */
	public void setMonitoringUncommittedAcAttachs(
			boolean monitorUncommittedAcAttachs);
	
	/**
	 * Retrieves the number of executors (threads) of the container's event router
	 * 
	 * @return
	 */
	public int getEventRouterExecutors();

	/**
	 * 
	 * Sets the number of executors (threads) of the container's event router.
	 * 
	 * Note that setting a different value for this method will only be effective on server (re)start.
	 * 
	 * @param eventRouterExecutors
	 */
	public void setEventRouterExecutors(
			int eventRouterExecutors);
	
	/**
	 * Retrieves the flag to indicate if profiles should be persisted between server restart.
	 * 
	 * @return
	 */
  public boolean getPersistProfiles();
  
	/**
	 * 
	 * Defines if profiles should be persisted between server restart.
	 * 
	 * @param persist
	 * @return
	 */
	public void setPersistProfiles(boolean persist);
	
	/**
	 * 
	 * @return string representation of container's version.
	 */
	public String getVersion();

	/**
	 * Dumps the container state, useful for a quick check up of leaks. 
	 * 
	 * @return
	 * @throws ManagementException 
	 */
	public String dumpState() throws ManagementException;
}
