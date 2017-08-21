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
