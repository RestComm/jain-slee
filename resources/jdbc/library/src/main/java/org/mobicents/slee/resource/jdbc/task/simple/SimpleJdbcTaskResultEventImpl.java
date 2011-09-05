package org.mobicents.slee.resource.jdbc.task.simple;

import org.mobicents.slee.resource.jdbc.task.JdbcTask;

/**
 * 
 * @author martins
 *
 */
public class SimpleJdbcTaskResultEventImpl implements SimpleJdbcTaskResultEvent {

	private final Object result;
	private final JdbcTask task;
	
	public SimpleJdbcTaskResultEventImpl(Object result, JdbcTask task) {
		this.result = result;
		this.task = task;
	}

	/**
	 * Retrieves the result returned from the task execution. 
	 * @return
	 */
	public Object getResult() {
		return result;
	}
	
	/**
	 * Retrieves the executed task.
	 * @return
	 */
	public JdbcTask getTask() {
		return task;
	}
	
}
