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
