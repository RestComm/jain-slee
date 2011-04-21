package org.mobicents.slee.resource.jdbc.event;

import java.sql.PreparedStatement;

/**
 * An event which provides the results for the execution of a
 * {@link PreparedStatement}.
 * 
 * @author martins
 * 
 */
public interface PreparedStatementEvent {

	/**
	 * Retrieves the executed {@link PreparedStatement}.
	 * 
	 * @return
	 */
	public PreparedStatement getPreparedStatement();

}
