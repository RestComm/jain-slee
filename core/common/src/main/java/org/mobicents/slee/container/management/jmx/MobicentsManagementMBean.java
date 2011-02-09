package org.mobicents.slee.container.management.jmx;

import javax.slee.management.ManagementException;

import org.jboss.system.ServiceMBean;

public interface MobicentsManagementMBean extends ServiceMBean {

    public final String OBJECT_NAME = "org.mobicents.slee:service=MobicentsManagement";

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
	
    public String getLoggingConfiguration(String configuration) throws ManagementException;

    public void setLoggingConfiguration(String configuration, String contents) throws ManagementException;
    
    public void switchLoggingConfiguration(String newProfile) throws ManagementException;
}
