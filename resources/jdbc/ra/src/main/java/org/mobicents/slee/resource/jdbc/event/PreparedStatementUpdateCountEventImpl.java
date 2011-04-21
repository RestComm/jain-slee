package org.mobicents.slee.resource.jdbc.event;

import java.sql.PreparedStatement;

/**
 * 
 * @author martins
 * 
 */
public class PreparedStatementUpdateCountEventImpl extends
		AbstractPreparedStatementEvent implements
		PreparedStatementUpdateCountEvent {

	/**
	 * 
	 */
	private final int updateCount;

	/**
	 * 
	 * @param preparedStatement
	 * @param updateCount
	 */
	public PreparedStatementUpdateCountEventImpl(
			PreparedStatement preparedStatement, int updateCount) {
		super(preparedStatement);
		this.updateCount = updateCount;
	}

	@Override
	public int getUpdateCount() {
		return updateCount;
	}

}
