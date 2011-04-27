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

package org.mobicents.slee.resource.jdbc;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * The {@link ActivityContextInterface} factory interface for a JDBC Resource
 * Adaptor.
 * 
 * @author martins
 * 
 */
public interface JdbcActivityContextInterfaceFactory {

	/**
	 * the ID of the RA Type
	 */
	public static final ResourceAdaptorTypeID RATYPE_ID = JdbcResourceAdaptorSbbInterface.RATYPE_ID;
	
	/**
	 * Retrieves the {@link ActivityContextInterface} associated with the
	 * provided activity.
	 * 
	 * @param activity
	 * @return
	 * @throws UnrecognizedActivityException
	 * @throws FactoryException
	 */
	public ActivityContextInterface getActivityContextInterface(
			JdbcActivity activity) throws UnrecognizedActivityException,
			FactoryException;

}
