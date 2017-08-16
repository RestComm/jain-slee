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

package org.mobicents.slee;

import javax.slee.NoSuchObjectLocalException;
import javax.slee.SLEEException;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;

/**
 * Extension for {@link SbbLocalObject}. Exposes more information about the
 * object and underlying Sbb entity.
 * 
 * @author martins
 * 
 */
public interface SbbLocalObjectExt extends SbbLocalObject {

	/**
	 * Retrieves the name of the child relation used to create this object.
	 * <p>
	 * This method is a mandatory transactional method.
	 * 
	 * @return null if this object is related with a root Sbb entity.
	 * @throws TransactionRequiredLocalException
	 *             if this method is invoked without a valid transaction
	 *             context.
	 * @throws NoSuchObjectLocalException
	 *             if the SBB entity referenced by this
	 *             <code>SbbLocalObject</code> is no longer valid.
	 * @throws SLEEException
	 *             if the SBB's child relation name could not be obtained due to
	 *             a system-level failure.
	 */
	public String getChildRelation() throws TransactionRequiredLocalException,
			SLEEException;

	/**
	 * Retrieves the name of this object.
	 * <p>
	 * This method is a mandatory transactional method.
	 * 
	 * @return
	 * @throws TransactionRequiredLocalException
	 *             if this method is invoked without a valid transaction
	 *             context.
	 * @throws NoSuchObjectLocalException
	 *             if the SBB entity referenced by this
	 *             <code>SbbLocalObject</code> is no longer valid.
	 * @throws SLEEException
	 *             if the SBB's name could not be obtained due to a system-level
	 *             failure.
	 */
	public String getName() throws NoSuchObjectLocalException,
			TransactionRequiredLocalException, SLEEException;

	/**
	 * Retrieves the parent Sbb object.
	 * <p>
	 * This method is a mandatory transactional method.
	 * 
	 * @return null if this object is related with a root Sbb entity.
	 * @throws TransactionRequiredLocalException
	 *             if this method is invoked without a valid transaction
	 *             context.
	 * @throws NoSuchObjectLocalException
	 *             if the SBB entity referenced by this
	 *             <code>SbbLocalObject</code> is no longer valid.
	 * @throws SLEEException
	 *             if the SBB's parent could not be obtained due to a
	 *             system-level failure.
	 */
	public SbbLocalObjectExt getParent() throws NoSuchObjectLocalException,
			TransactionRequiredLocalException, SLEEException;
}
