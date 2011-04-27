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

package org.mobicents.slee.resource.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * JDBC RA Interface for a SBB.
 * 
 * @author martins
 * 
 */
public interface JdbcResourceAdaptorSbbInterface {

	/**
	 * the ID of the RA Type
	 */
	public static final ResourceAdaptorTypeID RATYPE_ID = new ResourceAdaptorTypeID(
			"JDBCResourceAdaptorType", "org.mobicents", "1.0");

	/**
	 * Creates a new {@link JdbcActivity}.
	 * 
	 * @return
	 */
	public JdbcActivity createActivity();

	/**
	 * <p>
	 * Attempts to establish a connection with the data source that this
	 * <code>DataSource</code> object represents.
	 * 
	 * @return a connection to the data source
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	Connection getConnection() throws SQLException;

	/**
	 * <p>
	 * Attempts to establish a connection with the data source that this
	 * <code>DataSource</code> object represents.
	 * 
	 * @param username
	 *            the database user on whose behalf the connection is being made
	 * @param password
	 *            the user's password
	 * @return a connection to the data source
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.4
	 */
	Connection getConnection(String username, String password)
			throws SQLException;

}
