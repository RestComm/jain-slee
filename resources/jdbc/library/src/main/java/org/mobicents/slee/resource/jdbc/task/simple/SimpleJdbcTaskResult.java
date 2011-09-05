package org.mobicents.slee.resource.jdbc.task.simple;

import javax.slee.EventTypeID;

import org.mobicents.slee.resource.jdbc.task.JdbcTaskResult;

public class SimpleJdbcTaskResult implements JdbcTaskResult {

	private final SimpleJdbcTaskResultEvent eventObject;

	public SimpleJdbcTaskResult(SimpleJdbcTaskResultEvent eventObject) {
		this.eventObject = eventObject;
	}

	@Override
	public Object getEventObject() {
		return eventObject;
	}

	@Override
	public EventTypeID getEventTypeID() {
		return SimpleJdbcTaskResultEvent.EVENT_TYPE_ID;
	}

}
