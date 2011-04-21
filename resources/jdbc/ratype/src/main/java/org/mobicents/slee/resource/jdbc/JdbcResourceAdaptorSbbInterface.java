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
