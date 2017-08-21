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
