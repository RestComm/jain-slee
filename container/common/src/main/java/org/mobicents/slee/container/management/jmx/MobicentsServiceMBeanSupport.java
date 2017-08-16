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
package org.mobicents.slee.container.management.jmx;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

import org.mobicents.slee.container.SleeContainer;

/**
 * @author martins
 *
 */
public abstract class MobicentsServiceMBeanSupport extends NotificationBroadcasterSupport implements MBeanRegistration {

	private ObjectName objectName;
	protected MBeanServer server;
	protected SleeContainer sleeContainer;
	
	/**
	 * 
	 */
	public MobicentsServiceMBeanSupport() {
		this(null);
	}
	
	/**
	 * 
	 */
	public MobicentsServiceMBeanSupport(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
	}
		
	/**
	 * Retrieves 
	 * @return the sleeContainer
	 */
	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}
	
	/**
	 * 
	 * @return
	 */
	public ObjectName getObjectName() {
		return objectName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#preRegister(javax.management.MBeanServer, javax.management.ObjectName)
	 */
	public ObjectName preRegister(MBeanServer mbs, ObjectName oname) throws Exception {
		this.objectName = oname;
		this.server = mbs;
		return oname;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#postRegister(java.lang.Boolean)
	 */
	@Override
	public void postRegister(Boolean arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#preDeregister()
	 */
	@Override
	public void preDeregister() throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#postDeregister()
	 */
	@Override
	public void postDeregister() {
	
	}
	
}
