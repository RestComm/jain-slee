package org.mobicents.slee.resource.jdbc.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 
 * @author martins
 * 
 */
public class PreparedStatementResultSetEventImpl extends
		AbstractPreparedStatementEvent implements
		PreparedStatementResultSetEvent {

	/**
	 * 
	 */
	private final ResultSet resultSet;

	/**
	 * 
	 * @param preparedStatement
	 * @param resultSet
	 */
	public PreparedStatementResultSetEventImpl(
			PreparedStatement preparedStatement, ResultSet resultSet) {
		super(preparedStatement);
		this.resultSet = resultSet;
	}

	@Override
	public ResultSet getResultSet() {
		return resultSet;
	}

}
