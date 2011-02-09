/**
 * 
 */
package org.mobicents.slee.container;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 * 
 */
public abstract class AbstractSleeContainerModule implements
		SleeContainerModule {

	protected SleeContainer sleeContainer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.SleeContainerModule#getSleeContainer()
	 */
	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.SleeContainerModule#setSleeContainer(org
	 * .mobicents.slee.container.SleeContainer)
	 */
	public void setSleeContainer(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.SleeContainerModule#sleeInit()
	 */
	public void sleeStarting() {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.SleeContainerModule#sleeShutdown()
	 */
	public void sleeShutdown() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.SleeContainerModule#sleeStart()
	 */
	public void beforeSleeRunning() {
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.SleeContainerModule#afterSleeRunning()
	 */
	public void afterSleeRunning() {		
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.SleeContainerModule#sleeStopping()
	 */
	public void sleeStopping() {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.SleeContainerModule#sleeStop()
	 */
	public void sleeStopped() {
	}
}
