package org.mobicents.slee.container.management.jmx;

import javax.slee.management.ManagementException;

import org.jboss.system.ServiceMBean;

public interface MobicentsManagementMBean extends ServiceMBean {

	public double getEntitiesRemovalDelay();

	public void setEntitiesRemovalDelay(double entitiesRemovalDelay);

	/**
	 * Retrieves the number of threads used by timers.
	 * 
	 * @return
	 */
	public int getTimerThreads();

	/**
	 * Sets the number of threads used by timers.
	 * 
	 * Note that setting a different value for this method will only be effective on server (re)start.
	 * 
	 * @param value
	 */
	public void setTimerThreads(
			int value);
	
	/**
	 * Retrieves the number of event router threads.
	 * 
	 * @return
	 */
	public int getEventRouterThreads();

	/**
	 * 
	 * Sets the number of event router threads.
	 * 
	 * Note that setting a different value for this method will only be effective on server (re)start.
	 * 
	 * @param value
	 */
	public void setEventRouterThreads(
			int value);
	
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
