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
package org.mobicents.slee.container.sbb;

import javax.slee.ServiceID;

import org.mobicents.slee.container.component.sbb.SbbComponent;

/**
 * @author martins
 * 
 */
public interface SbbObjectPool {

	/**
	 * 
	 * @return
	 * @throws java.lang.Exception
	 * @throws java.util.NoSuchElementException
	 * @throws java.lang.IllegalStateException
	 */
	public SbbObject borrowObject() throws java.lang.Exception,
			java.util.NoSuchElementException, java.lang.IllegalStateException;

	/**
	 * @return the sbbComponent
	 */
	public SbbComponent getSbbComponent();

	/**
	 * @return the serviceID
	 */
	public ServiceID getServiceID();

	/**
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void invalidateObject(SbbObject obj) throws Exception;

	/**
	 * 
	 * @param obj
	 * @throws java.lang.Exception
	 */
	public void returnObject(SbbObject obj) throws java.lang.Exception;

}
