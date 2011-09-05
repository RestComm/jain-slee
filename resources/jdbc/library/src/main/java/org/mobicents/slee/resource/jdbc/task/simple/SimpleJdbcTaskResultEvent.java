package org.mobicents.slee.resource.jdbc.task.simple;

import javax.slee.EventTypeID;

import org.mobicents.slee.resource.jdbc.task.JdbcTask;

/**
 * 
 * @author martins
 * 
 */
public interface SimpleJdbcTaskResultEvent {

	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID(
			SimpleJdbcTaskResultEvent.class.getSimpleName(), "org.mobicents",
			"1.0");

	/**
	 * Retrieves the result returned from the task execution.
	 * 
	 * @return
	 */
	public Object getResult();

	/**
	 * Retrieves the executed task.
	 * 
	 * @return
	 */
	public JdbcTask getTask();

}
