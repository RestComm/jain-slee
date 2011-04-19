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

package org.mobicents.slee;

import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.Sbb;
import javax.slee.TransactionRequiredLocalException;

/**
 * Extension for {@link ChildRelation}. Adds the concept that childs are mapped
 * to names.
 * 
 * @author martins
 * 
 */
public interface ChildRelationExt extends ChildRelation {

	/**
	 * the proposed default name for a child sbb, useful when using this
	 * extension to handle a single child.
	 */
	public static final String DEFAULT_CHILD_NAME = "0";

	/**
	 * Create a new SBB entity of the SBB type associated with the relation,
	 * with the specified name. The new SBB entity is automatically added to the
	 * relationship collection. The returned object may be cast to the required
	 * local interface type using the normal Java typecast mechanism.
	 * <p>
	 * This method is a mandatory transactional method.
	 * 
	 * @param name
	 *            the name to apply to the object created.
	 * @return a local object reference to the created SBB entity.
	 * @throws CreateException
	 *             if an SBB entity could not be created. This exception may
	 *             have propagated from the SBB object's {@link Sbb#sbbCreate}
	 *             or {@link Sbb#sbbPostCreate}, or may have been thrown by the
	 *             SLEE.
	 * @throws IllegalArgumentException
	 *             if the specified name is the empty string.
	 * @throws NullPointerException
	 *             if the specified name is null.
	 * @throws TransactionRequiredLocalException
	 *             if this method is invoked without a valid transaction
	 *             context.
	 * @throws SLEEException
	 *             if the child SBB could not be created due to a system-level
	 *             failure.
	 */
	public SbbLocalObjectExt create(String name) throws CreateException,
			IllegalArgumentException, NullPointerException,
			TransactionRequiredLocalException, SLEEException;

	/**
	 * Retrieves the sbb entity associated with the child relation, with the
	 * specified name.
	 * <p>
	 * This method is a mandatory transactional method.
	 * 
	 * @param name
	 *            the name of the object's sbb entity to retrieve.
	 * @return null if there is no such sbb entity.
	 * @throws IllegalArgumentException
	 *             if the specified name is the empty string.
	 * @throws NullPointerException
	 *             if the specified name is null.
	 * @throws TransactionRequiredLocalException
	 *             if this method is invoked without a valid transaction
	 *             context.
	 * @throws SLEEException
	 *             if the child SBB could not be retrieved due to a system-level
	 *             failure.
	 */
	public SbbLocalObjectExt get(String name) throws IllegalArgumentException,
			NullPointerException, TransactionRequiredLocalException,
			SLEEException;

}
