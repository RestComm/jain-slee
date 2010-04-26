package org.mobicents.slee.container.management.jmx;

import javax.slee.management.ManagementException;

import org.jboss.system.ServiceMBean;

public interface MobicentsManagementMBean extends ServiceMBean {

	/**
	 * 
	 * @return
	 */
	public double getEntitiesRemovalDelay();

	/**
	 * 
	 * @param entitiesRemovalDelay
	 */
	public void setEntitiesRemovalDelay(double entitiesRemovalDelay);
		
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
