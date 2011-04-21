package org.mobicents.slee.resource.jdbc.event;

import java.sql.ResultSet;
import java.sql.Statement;

import javax.slee.EventTypeID;

/**
 * An event which provides the results for the execution of query in a
 * {@link Statement}, a {@link ResultSet}.
 * 
 * @author martins
 * 
 */
public interface StatementResultSetEvent extends
		StatementEvent {

	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID(
			StatementResultSetEvent.class.getSimpleName(),
			"org.mobicents", "1.0");
	
	/**
	 * Retrieves the executed query's result set.
	 * 
	 * @return
	 */
	public ResultSet getResultSet();
		
}
