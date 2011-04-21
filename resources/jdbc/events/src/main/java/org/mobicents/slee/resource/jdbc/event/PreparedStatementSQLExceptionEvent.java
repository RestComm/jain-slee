package org.mobicents.slee.resource.jdbc.event;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.slee.EventTypeID;

/**
 * An event which provides the exception which occurred when executing a
 * {@link PreparedStatement}.
 * 
 * @author martins
 * 
 */
public interface PreparedStatementSQLExceptionEvent extends
		PreparedStatementEvent {

	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID(
			PreparedStatementSQLExceptionEvent.class.getSimpleName(),
			"org.mobicents", "1.0");
	
	/**
	 * Retrieves the executed query's sql exception.
	 * 
	 * @return
	 */
	public SQLException getSQLException();

}
