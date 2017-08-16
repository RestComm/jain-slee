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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.service.MService;
import org.mobicents.slee.container.component.service.ServiceDescriptor;

/**
 * Start time:17:15:13 2009-01-20<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServiceDescriptorImpl extends AbstractComponentDescriptor implements ServiceDescriptor {

	private final ServiceID serviceID;
	private final SbbID rootSbbID;
	private final byte defaultPriority;
	private final String addressProfileTable;
	private final String resourceInfoProfileTable;
	
	public ServiceDescriptorImpl(MService mService) throws DeploymentException {
		super(mService.isSlee11());
		try {
			this.defaultPriority = mService.getDefaultPriority();
			this.serviceID = new ServiceID(mService.getServiceName(),mService.getServiceVendor(),mService.getServiceVersion());
			this.rootSbbID = new SbbID(mService.getRootSbb().getSbbName(),mService.getRootSbb().getSbbVendor(),mService.getRootSbb().getSbbVersion());
			this.addressProfileTable = mService.getAddressProfileTable();
			this.resourceInfoProfileTable = mService.getResourceInfoProfileTable();
			dependenciesSet.add(rootSbbID);
			// even if the root sbb does not declares it, all services depend on standard service started event(s)
			dependenciesSet.add(new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
			"1.0"));
			if (isSlee11()) {
				dependenciesSet.add(new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
				"1.1"));
			}
		} catch (Exception e) {
			throw new DeploymentException("failed to build service descriptor",e);
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.service.ServiceDescriptor#getDefaultPriority()
	 */
	public byte getDefaultPriority() {
		return defaultPriority;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.service.ServiceDescriptor#getRootSbbID()
	 */
	public SbbID getRootSbbID() {
		return rootSbbID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.service.ServiceDescriptor#getServiceID()
	 */
	public ServiceID getServiceID() {
		return serviceID;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.service.ServiceDescriptor#getAddressProfileTable()
	 */
	public String getAddressProfileTable() {
		return addressProfileTable;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.service.ServiceDescriptor#getResourceInfoProfileTable()
	 */
	public String getResourceInfoProfileTable() {
		return resourceInfoProfileTable;
	}
}
