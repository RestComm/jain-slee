package org.mobicents.slee.container.management.jmx;

/**
 * JMX Interface for the configuration of the congestion control module.
 * 
 * @author martins
 * 
 */
public interface CongestionControlConfigurationMBean {

	/**
	 * Retrieves the minimum memory (percentage) that should be available to
	 * turn on congestion control.
	 * 
	 * @return
	 */
	public int getMinFreeMemoryToTurnOn();

	/**
	 * Sets the minimum memory (percentage) that should be available to turn on
	 * congestion control.
	 * 
	 * @param minFreeMemoryToTurnOn
	 */
	public void setMinFreeMemoryToTurnOn(int minFreeMemoryToTurnOn);

	/**
	 * Retrieves the minimum memory (percentage) that should be available to
	 * turn off congestion control.
	 * 
	 * @return
	 */
	public int getMinFreeMemoryToTurnOff();

	/**
	 * Sets the minimum memory (percentage) that should be available to turn off
	 * congestion control. This value should be set with respect to the value on
	 * getMinFreeMemoryToTurnOn(), a step higher so congestion control is not
	 * always turning on and off.
	 * 
	 * @param minFreeMemoryToTurnOff
	 */
	public void setMinFreeMemoryToTurnOff(int minFreeMemoryToTurnOff);

	/**
	 * Retrieves the period in seconds to check if congestion control state
	 * should change. 0 means congestion control is off.
	 * 
	 * @return
	 */
	public int getPeriodBetweenChecks();

	/**
	 * Sets the period in seconds to check if congestion control state should
	 * change. Use 0 to turn off congestion control.
	 * 
	 * @param periodBetweenChecks
	 */
	public void setPeriodBetweenChecks(int periodBetweenChecks);

	/**
	 * Indicates if the start of activity should be refused, when congestion
	 * control is on.
	 * 
	 * @return
	 */
	public boolean isRefuseStartActivity();

	/**
	 * Defines if the start of activity should be refused, when congestion
	 * control is on.
	 * 
	 * @param refuseStartActivity
	 */
	public void setRefuseStartActivity(boolean refuseStartActivity);

	/**
	 * Indicates if the firing of an event should be refused, when congestion
	 * control is on.
	 * 
	 * @return
	 */
	public boolean isRefuseFireEvent();

	/**
	 * Defines if the firing of an event should be refused, when congestion
	 * control is on.
	 * 
	 * @param refuseFireEvent
	 */
	public void setRefuseFireEvent(boolean refuseFireEvent);

}
