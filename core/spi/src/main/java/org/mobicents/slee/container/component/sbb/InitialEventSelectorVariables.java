/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.component.sbb;

import javax.slee.InitialEventSelector;

/**
 * The variables for an {@link InitialEventSelector}.
 * 
 * @author martins
 *
 */
public interface InitialEventSelectorVariables {

	/**
	 * 
	 * @return
	 */
	public InitialEventSelectorVariables clone();

	/**
	 * 
	 * @return
	 */
	public boolean isActivityContextOnlySelected();

	/**
	 * 
	 * @return
	 */
	public boolean isActivityContextSelected();

	/**
	 * 
	 * @return
	 */
	public boolean isAddressProfileSelected();

	/**
	 * 
	 * @return
	 */
	public boolean isAddressSelected();

	/**
	 * 
	 * @return
	 */
	public boolean isEventSelected();

	/**
	 * 
	 * @return
	 */
	public boolean isEventTypeSelected();

	/**
	 * 
	 * @param activityContextSelected
	 */
	public void setActivityContextSelected(boolean activityContextSelected);

	/**
	 * 
	 * @param addressProfileSelected
	 */
	public void setAddressProfileSelected(boolean addressProfileSelected);

	/**
	 * 
	 * @param addressSelected
	 */
	public void setAddressSelected(boolean addressSelected);

	/**
	 * 
	 * @param eventSelected
	 */
	public void setEventSelected(boolean eventSelected);

	/**
	 * 
	 * @param eventTypeSelected
	 */
	public void setEventTypeSelected(boolean eventTypeSelected);

}
