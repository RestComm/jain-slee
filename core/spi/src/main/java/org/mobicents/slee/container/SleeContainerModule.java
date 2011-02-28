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
	void setSleeContainer(SleeContainer sleeContainer);
	
	/**
	 * 
	 */
	void sleeInitialization();
	
	/**
	 * 
	 */
	void sleeStarting();
		
	/**
	 * 
	 */
	void sleeStopping();
	
	/**
	 * 
	 */
	void sleeShutdown();
	
}
