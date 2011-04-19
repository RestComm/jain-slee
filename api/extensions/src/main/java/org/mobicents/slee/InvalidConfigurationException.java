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
