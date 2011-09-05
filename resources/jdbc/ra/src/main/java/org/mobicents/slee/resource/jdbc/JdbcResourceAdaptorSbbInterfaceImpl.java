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

import javax.sql.DataSource;

/**
 * Implementation of the JDBC RA Sbb Interface.
 * 
 * @author martins
 * 
 */
public class JdbcResourceAdaptorSbbInterfaceImpl implements
		JdbcResourceAdaptorSbbInterface {

	/**
	 * 
	 */
	private final JdbcResourceAdaptor ra;

	private boolean raSbbInterfaceConnectionGettersOn;
	
	/**
	 * 
	 * @param ra
	 */
	public JdbcResourceAdaptorSbbInterfaceImpl(JdbcResourceAdaptor ra) {
		this.ra = ra;
	}
	
	public void setRaSbbInterfaceConnectionGettersOn(
			boolean raSbbInterfaceConnectionGettersOn) {
		this.raSbbInterfaceConnectionGettersOn = raSbbInterfaceConnectionGettersOn;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.resource.jdbc.JdbcResourceAdaptorSbbInterface#
	 * createActivity()
	 */
	@Override
	public JdbcActivity createActivity() {
		if (ra.getDatasource() == null) {
			throw new IllegalStateException("RA entity not active");
		}
		return ra.createActivity();
	}

	private DataSource getDataSource() {
		if(!raSbbInterfaceConnectionGettersOn) {
			throw new SecurityException("RA configurations does not permits connection retrieval");
		}
		final DataSource dataSource = ra.getDatasource();
		if (dataSource == null) {
			throw new IllegalStateException("RA entity not active");
		}
		return dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.resource.jdbc.JdbcResourceAdaptorSbbInterface#
	 * getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.resource.jdbc.JdbcResourceAdaptorSbbInterface#
	 * getConnection(java.lang.String, java.lang.String)
	 */
	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return getDataSource().getConnection(username, password);
	}

}
