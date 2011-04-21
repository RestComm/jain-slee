package org.mobicents.slee.resource.jdbc.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.slee.EventTypeID;

/**
 * An event which provides the results for the execution of query in a
 * {@link PreparedStatement}, a {@link ResultSet}.
 * 
 * @author martins
 * 
 */
public interface PreparedStatementResultSetEvent extends
		PreparedStatementEvent {

	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID(
			PreparedStatementResultSetEvent.class.getSimpleName(),
			"org.mobicents", "1.0");
	
	/**
	 * Retrieves the executed query's result set.
	 * 
	 * @return
	 */
	public ResultSet getResultSet();

}
