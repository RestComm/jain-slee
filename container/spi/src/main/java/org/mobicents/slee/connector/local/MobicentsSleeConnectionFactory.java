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
package org.mobicents.slee.connector.local;

import javax.slee.connection.SleeConnectionFactory;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * Interface for Restcomm SleeConnectionFactory to comply with. 
 * @author baranowb
 *
 */
public interface MobicentsSleeConnectionFactory extends SleeConnectionFactory, SleeContainerModule {

	/**
	 * Sets JNDI name to be used while binding this factory. Example value:
	 * <ul>
	 * <li>/SleeConnectionFactory</li>
	 * <li>java:comp/PrivateSleeConnectionFactory</li>
	 * </ul>
	 * @param name
	 */
	public void setJNDIName(String name);
	public String getJNDIName();
	
}
