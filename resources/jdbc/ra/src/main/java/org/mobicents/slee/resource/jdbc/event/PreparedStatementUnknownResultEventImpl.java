package org.mobicents.slee.resource.jdbc.event;

import java.sql.PreparedStatement;

/**
 * 
 * @author martins
 * 
 */
public class PreparedStatementUnknownResultEventImpl extends
		AbstractPreparedStatementEvent implements
		PreparedStatementUnknownResultEvent {

	/**
	 * 
	 */
	private final boolean executionResult;

	/**
	 * 
	 * @param preparedStatement
	 * @param resultSet
	 */
	public PreparedStatementUnknownResultEventImpl(
			PreparedStatement preparedStatement, boolean executionResult) {
		super(preparedStatement);
		this.executionResult = executionResult;
	}

	@Override
	public boolean getExecutionResult() {
		return executionResult;
	}

}
