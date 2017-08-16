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

package org.mobicents.slee.container.management.jmx;

import javax.slee.EventContext;

import org.mobicents.slee.container.event.EventContextFactory;

/**
 * Interface to the {@link EventContextFactory} configuration.
 * 
 * @author martins
 * 
 */
public interface EventContextFactoryConfigurationMBean {

	public static final String OBJECT_NAME = "org.mobicents.slee:name=EventContextFactoryConfiguration";
	
	/**
	 * Retrieves the default timeout applied when suspending delivery of an
	 * {@link EventContext}, that is, the timeout used on
	 * {@link EventContext#suspendDelivery()}.
	 * 
	 * @return
	 */
	public int getDefaultEventContextSuspensionTimeout();

	/**
	 * Sets the default timeout applied when suspending delivery of an
	 * {@link EventContext}, that is, the timeout used on
	 * {@link EventContext#suspendDelivery()}.
	 * 
	 * @param i
	 *            the default timeout, in milliseconds
	 * @throws IllegalArgumentException
	 *             if the default timeout is lower than 1.
	 */
	public void setDefaultEventContextSuspensionTimeout(int i)
			throws IllegalArgumentException;

}
