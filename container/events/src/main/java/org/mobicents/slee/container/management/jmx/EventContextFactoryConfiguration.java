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
