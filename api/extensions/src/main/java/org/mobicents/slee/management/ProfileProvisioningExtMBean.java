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

package org.mobicents.slee.management;

import javax.slee.management.ManagementException;
import javax.slee.management.ProfileProvisioningMBean;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.UnrecognizedProfileSpecificationException;

import org.mobicents.slee.ConfigProperties;
import org.mobicents.slee.InvalidConfigurationException;
import org.mobicents.slee.ProfileExt;

/**
 * Extends {@link ProfileProvisioningMBean}, adding capability to retrieve and
 * update a Profile Specification configuration.
 * 
 * @author Eduardo Martins
 * 
 */
public interface ProfileProvisioningExtMBean extends ProfileProvisioningMBean {

	/**
	 * Get the configuration properties, and their default values if any, for
	 * the specified Profile Specification.
	 * 
	 * @param profileSpecificationID
	 *            the identifier of the Profile Specification.
	 * @return a <code>ConfigProperties</code> object containing the
	 *         configuration properties of the Profile Specification.
	 * @throws NullPointerException
	 *             if the argument is <code>null</code>.
	 * @throws UnrecognizedProfileSpecificationException
	 *             if <code>profileSpecificationID</code> does not correspond
	 *             with a Profile Specification installed in the SLEE.
	 * @throws ManagementException
	 *             if the configuration properties could not be obtained due to
	 *             a system-level failure.
	 */
	public ConfigProperties getConfigurationProperties(
			ProfileSpecificationID profileSpecificationID)
			throws NullPointerException,
			UnrecognizedProfileSpecificationException, ManagementException;

	/**
	 * Reconfigure a Profile Specification with new configuration properties.
	 * Properties are specified using a <code>ConfigProperties</code> object.
	 * 
	 * <p>
	 * Only properties that need updating need to be included in the
	 * <code>ConfigProperties</code> object.
	 * <p>
	 * 
	 * The management client must ensure that all properties included in the
	 * <code>ConfigProperties</code> object have a non-null value before passing
	 * it to this method. The
	 * {@link ProfileExt#profileVerifyConfiguration(ConfigProperties)
	 * profileVerifyConfiguration} method is invoked on a Profile object to test
	 * validity of configuration.
	 * 
	 * @param profileSpecificationID
	 *            the identifier of the Profile Specification.
	 * @param properties
	 *            the values of configuration properties to be updated for the
	 *            ProfileSpecification.
	 * @throws NullPointerException
	 *             if either argument is <code>null</code>.
	 * @throws UnrecognizedProfileSpecificationException
	 *             if <code>profileSpecificationID</code> does not correspond
	 *             with a Profile Specification installed in the SLEE.
	 * @throws InvalidConfigurationException
	 *             if one or more of the configuration properties has a
	 *             <code>null</code> value, or if the configuration properties
	 *             were not valid for the Profile as determined by the
	 *             {@link ProfileExt#profileVerifyConfiguration(ConfigProperties)
	 *             profileVerifyConfiguration} method.
	 * @throws ManagementException
	 *             if the configuration properties could not be updated due to a
	 *             system-level failure.
	 */
	public void updateConfigurationProperties(
			ProfileSpecificationID profileSpecificationID,
			ConfigProperties properties) throws NullPointerException,
			UnrecognizedProfileSpecificationException,
			InvalidConfigurationException, ManagementException;
}
