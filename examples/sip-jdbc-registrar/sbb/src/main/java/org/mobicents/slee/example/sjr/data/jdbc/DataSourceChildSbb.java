/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.example.sjr.data.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sip.header.ContactHeader;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.example.sjr.data.DataSourceChildSbbLocalInterface;
import org.mobicents.slee.example.sjr.data.DataSourceParentSbbLocalInterface;
import org.mobicents.slee.resource.jdbc.JdbcActivity;
import org.mobicents.slee.resource.jdbc.JdbcActivityContextInterfaceFactory;
import org.mobicents.slee.resource.jdbc.JdbcResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.jdbc.event.JdbcTaskExecutionThrowableEvent;
import org.mobicents.slee.resource.jdbc.task.simple.SimpleJdbcTaskResultEvent;

/**
 * 
 * @author martins
 *
 */
public abstract class DataSourceChildSbb implements Sbb,
		DataSourceChildSbbLocalInterface {

	/**
	 * the SBB object context
	 */
	private SbbContextExt sbbContextExt;

	/**
	 * the SBB logger
	 */
	private static Tracer tracer;

	// JDBC RA
	private static final ResourceAdaptorTypeID jdbcRATypeID = JdbcResourceAdaptorSbbInterface.RATYPE_ID;
	private static final String jdbcRALink = "JDBCRA";
	private JdbcResourceAdaptorSbbInterface jdbcRA;
	private JdbcActivityContextInterfaceFactory jdbcACIF;

	// local interface

	@Override
	public void init() {
		// create db schema if needed
		Connection connection = null;
		try {
			connection = jdbcRA.getConnection();
			
			connection.createStatement().execute(
					DataSourceSchemaInfo._QUERY_DROP);
			
			connection.createStatement().execute(
					DataSourceSchemaInfo._QUERY_CREATE);
		} catch (SQLException e) {
			tracer.warning("failed to create db schema", e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				tracer.severe("failed to close db connection", e);
			}
		}
	}

	@Override
	public void getBindings(String address) {
		executeTask(new GetBindingsJdbcTask(address, tracer));
	}

	@Override
	public void removeBinding(String contact, String address) {
		executeTask(new RemoveBindingJdbcTask(address, contact, tracer));
	}

	@Override
	public void removeBindings(String address, String callId, long cSeq) {
		executeTask(new RemoveBindingsJdbcTask(address, callId, cSeq, tracer));
	}

	@Override
	public void updateBindings(String address, String callId, long cSeq,
			List<ContactHeader> contacts) {
		executeTask(new UpdateBindingsJdbcTask(address, callId, cSeq, contacts, tracer));
	}

	/**
	 * Simple method to create JDBC activity and execute given task.
	 * 
	 * @param queryJDBCTask
	 */
	private void executeTask(DataSourceJdbcTask jdbcTask) {
		JdbcActivity jdbcActivity = jdbcRA.createActivity();
		ActivityContextInterface jdbcACI = jdbcACIF
				.getActivityContextInterface(jdbcActivity);
		jdbcACI.attach(sbbContextExt.getSbbLocalObject());
		jdbcActivity.execute(jdbcTask);
	}

	// events

	/**
	 * Event handler for {@link JdbcTaskExecutionThrowableEvent}.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onJdbcTaskExecutionThrowableEvent(
			JdbcTaskExecutionThrowableEvent event, ActivityContextInterface aci) {
		if (tracer.isWarningEnabled()) {
			tracer.warning(
					"Received a JdbcTaskExecutionThrowableEvent, as result of executed task "
							+ event.getTask(), event.getThrowable());
		}
		// end jdbc activity
		final JdbcActivity activity = (JdbcActivity) aci.getActivity();
		activity.endActivity();
		// call back parent
		final DataSourceParentSbbLocalInterface parent = (DataSourceParentSbbLocalInterface) sbbContextExt
				.getSbbLocalObject().getParent();
		final DataSourceJdbcTask jdbcTask = (DataSourceJdbcTask) event
				.getTask();
		jdbcTask.callBackParentOnException(parent);
	}

	public void onSimpleJdbcTaskResultEvent(SimpleJdbcTaskResultEvent event,
			ActivityContextInterface aci) {
		if (tracer.isFineEnabled()) {
			tracer.fine("Received a SimpleJdbcTaskResultEvent, as result of executed task "
					+ event.getTask());
		}
		// end jdbc activity
		final JdbcActivity activity = (JdbcActivity) aci.getActivity();
		activity.endActivity();
		// call back parent
		final DataSourceParentSbbLocalInterface parent = (DataSourceParentSbbLocalInterface) sbbContextExt
				.getSbbLocalObject().getParent();
		final DataSourceJdbcTask jdbcTask = (DataSourceJdbcTask) event
				.getTask();
		jdbcTask.callBackParentOnResult(parent);
	}

	// sbb life cycle

	@Override
	public void sbbActivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sbbCreate() throws CreateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sbbLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sbbPassivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sbbPostCreate() throws CreateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sbbRemove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sbbRolledBack(RolledBackContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sbbStore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSbbContext(SbbContext arg0) {
		sbbContextExt = (SbbContextExt) arg0;
		if (tracer == null)
			tracer = sbbContextExt.getTracer(getClass().getSimpleName());
		jdbcRA = (JdbcResourceAdaptorSbbInterface) this.sbbContextExt
				.getResourceAdaptorInterface(jdbcRATypeID, jdbcRALink);
		jdbcACIF = (JdbcActivityContextInterfaceFactory) this.sbbContextExt
				.getActivityContextInterfaceFactory(jdbcRATypeID);
	}

	@Override
	public void unsetSbbContext() {
		sbbContextExt = null;
		jdbcRA = null;
		jdbcACIF = null;
	}
}
