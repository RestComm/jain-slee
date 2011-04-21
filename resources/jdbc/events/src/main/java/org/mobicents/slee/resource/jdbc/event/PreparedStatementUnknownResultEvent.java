package org.mobicents.slee.resource.jdbc.event;

import java.sql.PreparedStatement;

import javax.slee.EventTypeID;

/**
 * An event which provides the results for the execution of unknown sql in a
 * {@link PreparedStatement}. Concrete results should be retrieved from the
 * {@link PreparedStatement} object, through
 * {@link PreparedStatement#getResultSet()},
 * {@link PreparedStatement#getUpdateCount()} and
 * {@link PreparedStatement#getMoreResults()}
 * 
 * @author martins
 * 
 */
public interface PreparedStatementUnknownResultEvent extends
		PreparedStatementEvent {

	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID(
			PreparedStatementUnknownResultEvent.class.getSimpleName(),
			"org.mobicents", "1.0");
	
	/**
	 * Retrieves the statement sql execution result.
	 * 
	 * @return <code>true</code> if the first result is a <code>ResultSet</code>
	 *         object; <code>false</code> if it is an update count or there are
	 *         no results
	 */
	public boolean getExecutionResult();

}
