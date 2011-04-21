package org.mobicents.slee.resource.jdbc.event;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * @author martins
 * 
 */
public class PreparedStatementSQLExceptionEventImpl extends
		AbstractPreparedStatementEvent implements
		PreparedStatementSQLExceptionEvent {

	/**
	 * 
	 */
	private final SQLException sqlException;

	/**
	 * 
	 * @param preparedStatement
	 * @param resultSet
	 */
	public PreparedStatementSQLExceptionEventImpl(
			PreparedStatement preparedStatement, SQLException sqlException) {
		super(preparedStatement);
		this.sqlException = sqlException;
	}

	@Override
	public SQLException getSQLException() {
		return sqlException;
	}

}
