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
	@Override
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
	@Override
	public void setSleeContainer(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.SleeContainerModule#sleeInitialization()
	 */
	@Override
	public void sleeInitialization() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.SleeContainerModule#sleeStarting()
	 */
	@Override
	public void sleeStarting() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.SleeContainerModule#sleeRunning()
	 */
	@Override
	public void sleeRunning() {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.SleeContainerModule#sleeStopping()
	 */
	@Override
	public void sleeStopping() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.SleeContainerModule#sleeStopped()
	 */
	@Override
	public void sleeStopped() {		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.SleeContainerModule#sleeShutdown()
	 */
	@Override
	public void sleeShutdown() {
	}

}
