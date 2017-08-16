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
package org.mobicents.slee.container.facilities.nullactivity;

import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.nullactivity.NullActivity;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 *
 */
public interface NullActivityFactory extends SleeContainerModule, javax.slee.nullactivity.NullActivityFactory {
	
	/**
	 * Creates a null activity handle that can later be used to build a null activity.
	 * 
	 * @return
	 */
	public NullActivityHandle createNullActivityHandle();

	/**
	 * Creates a new null activity for the specified handle.
	 * 
	 * @param nullActivityHandle
	 * @param mandateTransaction
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws FactoryException
	 */
	public NullActivity createNullActivity(NullActivityHandle nullActivityHandle,boolean mandateTransaction) throws TransactionRequiredLocalException, FactoryException;

}
