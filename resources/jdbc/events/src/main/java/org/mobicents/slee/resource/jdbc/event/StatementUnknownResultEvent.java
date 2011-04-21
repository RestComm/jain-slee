package org.mobicents.slee.resource.jdbc.event;

import java.sql.Statement;

import javax.slee.EventTypeID;

/**
 * An event which provides the results for the execution of unknown sql in a
 * {@link Statement}. Concrete results should be retrieved from the
 * {@link Statement} object, through {@link Statement#getResultSet()},
 * {@link Statement#getUpdateCount()} and {@link Statement#getMoreResults()}
 * 
 * @author martins
 * 
 */
public interface StatementUnknownResultEvent extends StatementEvent {

	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID(
			StatementUnknownResultEvent.class.getSimpleName(),
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
