package org.mobicents.slee.resource.jdbc.event;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 
 * @author martins
 * 
 */
public class StatementResultSetEventImpl extends AbstractStatementEvent
		implements StatementResultSetEvent {

	/**
	 * 
	 */
	private final ResultSet resultSet;

	/**
	 * 
	 * @param sql
	 * @param statement
	 * @param resultSet
	 */
	public StatementResultSetEventImpl(String sql, Statement statement,
			ResultSet resultSet) {
		super(null, null, null, sql, statement);
		this.resultSet = resultSet;
	}

	@Override
	public ResultSet getResultSet() {
		return resultSet;
	}

}
