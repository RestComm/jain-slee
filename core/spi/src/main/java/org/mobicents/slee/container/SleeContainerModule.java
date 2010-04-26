/**
 * 
 */
package org.mobicents.slee.container;

/**
 * @author martins
 *
 */
public interface SleeContainerModule {
	
	/**
	 * 
	 * @return
	 */
	public SleeContainer getSleeContainer();
	
	/**
	 * 
	 */
	public void setSleeContainer(SleeContainer sleeContainer);
	
	/**
	 * 
	 */
	public void sleeStarting();
	
	/**
	 * 
	 */
	public void afterSleeRunning();
	
	/**
	 * 
	 */
	public void beforeSleeRunning();
	
	/**
	 * 
	 */
	public void sleeStopping();
	
	/**
	 * 
	 */
	public void sleeStopped();
	
	/**
	 * 
	 */
	public void sleeShutdown();
	
}
