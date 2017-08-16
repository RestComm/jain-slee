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

/**
 * The SLEE container configuration with respect {@link EventContext}.
 * 
 * @author martins
 * 
 */
public class EventContextFactoryConfiguration implements
		EventContextFactoryConfigurationMBean {

	private int defaultEventContextSuspensionTimeout = 10000;

	@Override
	public int getDefaultEventContextSuspensionTimeout() {
		return defaultEventContextSuspensionTimeout;
	}

	@Override
	public void setDefaultEventContextSuspensionTimeout(int i)
			throws IllegalArgumentException {
		if (i < 1) {
			throw new IllegalArgumentException(
					"default timeout must not be lower than 1");
		}
		this.defaultEventContextSuspensionTimeout = i;
	}

}
