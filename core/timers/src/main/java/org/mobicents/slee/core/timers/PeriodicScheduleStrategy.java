package org.mobicents.slee.core.timers;


/**
 * Different strategies to use when scheduling a {@link Runnable} on a {@link FaultTolerantScheduler}
 * @author martins
 *
 */
public enum PeriodicScheduleStrategy {

	/**
	 * Periodic action that becomes enabled first
     * after the given initial delay, and subsequently with the given
     * period; that is executions will commence after
     * <tt>initialDelay</tt> then <tt>initialDelay+period</tt>, then
     * <tt>initialDelay + 2 * period</tt>, and so on.
	 */
	atFixedRate, 
	
	/**
	 * Periodic action that becomes enabled first
     * after the given initial delay, and subsequently with the
     * given delay between the termination of one execution and the
     * commencement of the next.
	 */
	withFixedDelay
}
