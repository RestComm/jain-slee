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

package org.mobicents.slee;

/**
 * This exception can be thrown if the configuration properties supplied by the Administrator are not
 * validated by a JAIN SLEE component. 
 */
public class InvalidConfigurationException extends Exception {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = -8461652778978593342L;

	/**
     * Creates an <code>InvalidConfigurationException</code> with a detail message.
     * @param message the detail message.
     */
    public InvalidConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates an <code>InvalidConfigurationException</code> with a detail message
     * and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
