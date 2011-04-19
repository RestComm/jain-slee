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

package org.mobicents.slee.management;

import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedSbbException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceManagementMBean;

import org.mobicents.slee.ConfigProperties;
import org.mobicents.slee.InvalidConfigurationException;
import org.mobicents.slee.SbbExt;

/**
 * Extends {@link ServiceManagementMBean}, adding capability to retrieve and
 * update a Service's Sbb configuration.
 * 
 * @author Eduardo Martins
 * 
 */
public interface ServiceManagementExtMBean extends ServiceManagementMBean {

	/**
	 * Get the configuration properties, and their default values if any, for
	 * the specified Service's SBB.
	 * 
	 * @param serviceID
	 *            the identifier of the service.
	 * @param sbbID
	 *            the identifier of the SBB.
	 * @return a <code>ConfigProperties</code> object containing the
	 *         configuration properties of the Service's SBB.
	 * @throws NullPointerException
	 *             if either argument is <code>null</code>.
	 * @throws UnrecognizedSbbException
	 *             if <code>sbbID</code> is not a recognizable
	 *             <code>SbbID</code> for the SLEE, does not correspond with an
	 *             SBB installed in the SLEE, or is not an SBB that participates
	 *             in the Service identified by <code>serviceID</code>.
	 * @throws UnrecognizedServiceException
	 *             if either argument is not a recognizable
	 *             <code>ServiceID</code> for the SLEE or does not correspond
	 *             with a Service installed in the SLEE.
	 * @throws ManagementException
	 *             if the configuration properties could not be obtained due to
	 *             a system-level failure.
	 */
	public ConfigProperties getConfigurationProperties(ServiceID serviceID,
			SbbID sbbID) throws NullPointerException, UnrecognizedSbbException,
			UnrecognizedServiceException, ManagementException;

	/**
	 * Reconfigure a service with new configuration properties. Properties are
	 * specified using a <code>ConfigProperties</code> object.
	 * 
	 * <p>
	 * Only properties that need updating need to be included in the
	 * <code>ConfigProperties</code> object.
	 * <p>
	 * 
	 * The management client must ensure that all properties included in the
	 * <code>ConfigProperties</code> object have a non-null value before passing
	 * it to this method. The
	 * {@link SbbExt#sbbVerifyConfiguration(ConfigProperties)
	 * sbbVerifyConfiguration} method is invoked on a SBB object to test
	 * validity of configuration.
	 * <p>
	 * An org.mobicents.slee.ServiceConfigurationUpdateEvent#org.mobicents.slee#
	 * 1.1 event is fired in the Service's Activity Context, which may be used
	 * to apply some service logic.
	 * <p>
	 * 
	 * @param serviceID
	 *            the identifier of the service.
	 * @param sbbID
	 *            the identifier of the SBB.
	 * @param properties
	 *            the values of configuration properties to be updated for the
	 *            Service's SBB.
	 * @throws NullPointerException
	 *             if either argument is <code>null</code>.
	 * @throws UnrecognizedSbbException
	 *             if <code>sbbID</code> is not a recognizable
	 *             <code>SbbID</code> for the SLEE, does not correspond with an
	 *             SBB installed in the SLEE, or is not an SBB that participates
	 *             in the Service identified by <code>serviceID</code>.
	 * @throws UnrecognizedServiceException
	 *             if either argument is not a recognizable
	 *             <code>ServiceID</code> for the SLEE or does not correspond
	 *             with a Service installed in the SLEE.
	 * @throws InvalidConfigurationException
	 *             if one or more of the configuration properties has a
	 *             <code>null</code> value, or if the configuration properties
	 *             were not valid for the SBB as determined by the
	 *             {@link SbbExt#sbbVerifyConfiguration(ConfigProperties)
	 *             sbbVerifyConfiguration} method.
	 * @throws ManagementException
	 *             if the Service's SBB configuration properties could not be
	 *             updated due to a system-level failure.
	 */
	public void updateConfigurationProperties(ServiceID serviceID, SbbID sbbID,
			ConfigProperties properties) throws NullPointerException,
			UnrecognizedSbbException, UnrecognizedServiceException,
			InvalidConfigurationException, ManagementException;

}
