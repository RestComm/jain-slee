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
