/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
