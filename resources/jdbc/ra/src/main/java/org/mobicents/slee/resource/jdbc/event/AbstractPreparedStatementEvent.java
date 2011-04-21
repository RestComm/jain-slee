package org.mobicents.slee.resource.jdbc.event;

import java.sql.PreparedStatement;

/**
 * 
 * @author martins
 * 
 */
public abstract class AbstractPreparedStatementEvent implements
		PreparedStatementEvent {

	/**
	 * 
	 */
	private final PreparedStatement preparedStatement;

	/**
	 * 
	 * @param preparedStatement
	 */
	protected AbstractPreparedStatementEvent(PreparedStatement preparedStatement) {
		if (preparedStatement == null) {
			throw new NullPointerException("null preparedStatement");
		}
		this.preparedStatement = preparedStatement;		
	}

	@Override
	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

}
