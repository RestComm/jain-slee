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

package org.mobicents.slee.resource.jdbc.task;

import java.sql.Connection;
import java.sql.SQLException;

import javax.slee.transaction.SleeTransactionManager;

/**
 * The context provided by the JDBC RA to a {@link JdbcTask}, for its logic
 * execution.
 * 
 * @author martins
 * 
 */
public interface JdbcTaskContext {

	/**
	 * Sets the credentials used to retrieve the JDBC {@link Connection}. Note
	 * these must be set before using {@link JdbcTaskContext}
	 * {@link #getConnection()}.
	 * 
	 * @param username
	 * @param password
	 */
	public void setConnectionCredentials(String username, String password);

	/**
	 * Retrieves the connection to be used by the task. The connection will be
	 * closed by the RA once the task execution ends.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException;

	/**
	 * Retrieves the JDBC RA {@link SleeTransactionManager}, which a
	 * {@link JdbcTask} may use to create and manage Java transactions.
	 * 
	 * @return
	 */
	public SleeTransactionManager getSleeTransactionManager();

}
