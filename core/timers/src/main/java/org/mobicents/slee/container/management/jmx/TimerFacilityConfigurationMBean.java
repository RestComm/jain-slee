/**
 * 
 */
package org.mobicents.slee.container.management.jmx;

/**
 * JMX interface for the SLEE timer facility configuration.
 * @author martins
 *
 */
public interface TimerFacilityConfigurationMBean {

	/**
	 * Retrieves the number of threads used by timers.
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
	public void setTimerThreads(int value);
	
}
