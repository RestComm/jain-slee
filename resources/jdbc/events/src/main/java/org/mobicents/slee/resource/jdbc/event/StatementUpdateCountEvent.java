package org.mobicents.slee.resource.jdbc.event;

import java.sql.Statement;

import javax.slee.EventTypeID;

/**
 * An event which provides the results for the execution of update in a
 * {@link Statement}, a update count.
 * 
 * @author martins
 * 
 */
public interface StatementUpdateCountEvent extends StatementEvent {

	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID(
			StatementUpdateCountEvent.class.getSimpleName(),
			"org.mobicents", "1.0");
	
	/**
	 * Retrieves the executed SQL update count.
	 * 
	 * @return
	 */
	public int getUpdateCount();

}
