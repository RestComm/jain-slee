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

	/**
	 * 
	 * @param ra
	 */
	public JdbcResourceAdaptorSbbInterfaceImpl(JdbcResourceAdaptor ra) {
		this.ra = ra;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.resource.jdbc.JdbcResourceAdaptorSbbInterface#
	 * createActivity()
	 */
	@Override
	public JdbcActivity createActivity() {
		return ra.createActivity();
	}

	private DataSource getDataSource() {
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
