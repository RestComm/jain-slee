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
package org.mobicents.slee.container.management;

import javax.naming.NamingException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.sbb.SbbObjectPool;

/**
 * @author martins
 * TODO redo interface
 */
public interface SbbManagement extends SleeContainerModule {

	/**
	 * Deploys an SBB. This generates the code to convert abstract to concrete
	 * class and registers the component in the component table and creates an
	 * object pool for the sbb id.
	 * 
	 * @param mobicentsSbbDescriptor
	 *            the descriptor of the sbb to install
	 * @throws Exception
	 */
	public void installSbb(SbbComponent sbbComponent) throws Exception;

	/**
	 * 
	 * @param sbbComponent
	 * @throws SystemException
	 * @throws Exception
	 * @throws NamingException
	 */
	public void uninstallSbb(final SbbComponent sbbComponent)
			throws SystemException, Exception, NamingException;
	
	/**
	 * @param serviceComponent
	 */
	public void serviceUninstall(ServiceComponent serviceComponent);

	/**
	 * @param serviceComponent
	 */
	public void serviceInstall(ServiceComponent serviceComponent);
	
	/**
	 * 
	 * @return
	 */
	public SbbObjectPool getObjectPool(ServiceID serviceID, SbbID sbbID);

}
