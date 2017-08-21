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

package org.mobicents.slee.resource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.management.ResourceManagementImpl;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;

/**
 * Implementation of the abstract resource adaptor aci factory code.
 * 
 * @author martins
 * 
 */
public class AbstractActivityContextInterfaceFactory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private transient SleeContainer sleeContainer;
	
	/**
	 * 
	 */
	private final ResourceAdaptorTypeID resourceAdaptorTypeID;

	/**
	 * 
	 * @param sleeContainer
	 * @param resourceAdaptorTypeID
	 */
	protected AbstractActivityContextInterfaceFactory(
			SleeContainer sleeContainer,
			ResourceAdaptorTypeID resourceAdaptorTypeID) {
		this.sleeContainer = sleeContainer;
		this.resourceAdaptorTypeID = resourceAdaptorTypeID;
	}

	/**
	 * This method returns the aci for the specified activity, if exists, it
	 * should be invoked by each impl of methods of an ra type aci factory.
	 * 
	 * @param activity
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedActivityException
	 * @throws FactoryException
	 */
	protected ActivityContextInterface getACI(Object activity)
			throws NullPointerException, UnrecognizedActivityException,
			FactoryException {
		if (activity == null) {
			throw new NullPointerException("null activity object");
		}
		ActivityHandle handle = null;
		for (ResourceAdaptorEntity raEntity : sleeContainer
				.getResourceManagement().getResourceAdaptorEntitiesPerType(resourceAdaptorTypeID)) {
			handle = raEntity.getResourceAdaptorObject().getActivityHandle(
					activity);
			if (handle != null) {
				ActivityContextHandle ach = new ResourceAdaptorActivityContextHandleImpl(raEntity, handle);
				ActivityContext ac = sleeContainer.getActivityContextFactory()
						.getActivityContext(ach);
				if (ac != null) {
					return ac.getActivityContextInterface();
				}
				break;
			}
		}

		throw new UnrecognizedActivityException(activity.toString());
	}

	@Override
	public int hashCode() {
		return resourceAdaptorTypeID.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((AbstractActivityContextInterfaceFactory) obj).resourceAdaptorTypeID
					.equals(this.resourceAdaptorTypeID);
		} else {
			return false;
		}
	}

	private void readObject(ObjectInputStream is)
			throws ClassNotFoundException, IOException {
		is.defaultReadObject();
		sleeContainer = ResourceManagementImpl.getInstance().getSleeContainer();
	}

}
