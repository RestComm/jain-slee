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

package org.mobicents.slee.container.deployment;

import javax.slee.management.DeploymentException;
import java.net.URL;

/**
 * The interface for the deployer inside the SLEE Container.
 * 
 * @author martins
 * 
 */
public interface InternalDeployer {

    /**
     * Sets the external deployer.
     *
     * @param deployer
     */
	void setExternalDeployer(ExternalDeployer deployer);

    /**
     *
     * @param deployableUnitURL
     * @return
     */
    boolean accepts(URL deployableUnitURL, String deployableUnitName);

    /**
     *
     * @param deployableUnitURL
     * @throws DeploymentException
     */
    void init(URL deployableUnitURL, String deployableUnitName) throws DeploymentException;

    /**
     *
     * @param deployableUnitURL
     * @throws DeploymentException
     */
    void start(URL deployableUnitURL, String deployableUnitName) throws DeploymentException;

    /**
     *
     * @param deployableUnitURL
     * @throws DeploymentException
     */
    void stop(URL deployableUnitURL, String deployableUnitName) throws DeploymentException;

}
