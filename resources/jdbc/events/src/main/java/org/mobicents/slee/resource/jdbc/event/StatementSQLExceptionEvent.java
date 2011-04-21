package org.mobicents.slee.resource.jdbc.event;

import java.sql.SQLException;
import java.sql.Statement;

import javax.slee.EventTypeID;

/**
 * An event which provides the exception which occurred when executing a
 * {@link Statement}.
 * 
 * @author martins
 * 
 */
public interface StatementSQLExceptionEvent extends StatementEvent {

	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID(
			StatementSQLExceptionEvent.class.getSimpleName(),
			"org.mobicents", "1.0");
	
	/**
	 * Retrieves the executed query's sql exception.
	 * 
	 * @return
	 */
	public SQLException getSQLException();

}
