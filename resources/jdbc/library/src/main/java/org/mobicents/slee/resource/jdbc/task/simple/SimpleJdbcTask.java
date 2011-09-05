package org.mobicents.slee.resource.jdbc.task.simple;

import org.mobicents.slee.resource.jdbc.task.JdbcTask;
import org.mobicents.slee.resource.jdbc.task.JdbcTaskContext;
import org.mobicents.slee.resource.jdbc.task.JdbcTaskResult;

/**
 * An abstract implementation of {@link JdbcTask}, which will fire a
 * {@link SimpleJdbcTaskResultEvent} event. Aims to simplify usage of
 * {@link JdbcTask} when there is no need for custom event types.
 * 
 * @author martins
 * 
 */
public abstract class SimpleJdbcTask implements JdbcTask {

	@Override
	public JdbcTaskResult execute(JdbcTaskContext taskContext) {
		Object result = executeSimple(taskContext);
		if (result == null) {
			return null;
		}
		return new SimpleJdbcTaskResult(new SimpleJdbcTaskResultEventImpl(result,
				this));
	}

	/**
	 * Method to be overridden by concrete task implementations, return an
	 * "opaque" object as result of the task execution.
	 * 
	 * @param taskContext
	 * @return
	 */
	public abstract Object executeSimple(JdbcTaskContext taskContext);

}
