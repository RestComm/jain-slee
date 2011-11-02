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

/**
 * 
 * @author martins
 *
 */
package org.mobicents.slee.example.sjr.data.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sip.message.Response;
import javax.slee.facilities.Tracer;
import javax.slee.transaction.SleeTransaction;

import org.mobicents.slee.example.sjr.data.DataSourceParentSbbLocalInterface;
import org.mobicents.slee.example.sjr.data.RegistrationBinding;
import org.mobicents.slee.resource.jdbc.task.JdbcTaskContext;

public class RemoveBindingsJdbcTask extends DataSourceJdbcTask {

	private int resultCode = Response.OK;
	private List<RegistrationBinding> currentBindings = null;
	private List<RegistrationBinding> removedBindings = null;

	private final String address;
	private final String callId;
	private final long cSeq;

	private final Tracer tracer;

	public RemoveBindingsJdbcTask(String address, String callId, long cSeq,
			Tracer tracer) {
		this.address = address;
		this.callId = callId;
		this.cSeq = cSeq;
		this.tracer = tracer;
	}

	@Override
	public Object executeSimple(JdbcTaskContext taskContext) {

		SleeTransaction tx = null;
		try {
			tx = taskContext.getSleeTransactionManager().beginSleeTransaction();

			Connection connection = taskContext.getConnection();
			// static value of query string, since its widely used :)
			PreparedStatement preparedStatement = connection
					.prepareStatement(DataSourceSchemaInfo._QUERY_SELECT);
			preparedStatement.setString(1, address);

			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();
			// IMPORTANT: we need both - currently present bindings and removed
			// ones
			// so SBB can update timers
			currentBindings = DataSourceSchemaInfo.getBindingsAsList(address, resultSet);
			List<RegistrationBinding> removedBindings = new ArrayList<RegistrationBinding>();
			Iterator<RegistrationBinding> it = currentBindings.iterator();

			while (it.hasNext()) {
				RegistrationBinding binding = it.next();
				if (callId.equals(binding.getCallId())) {
					if (cSeq > binding.getCSeq()) {
						it.remove();
						removedBindings.add(binding);
						preparedStatement = connection
								.prepareStatement(DataSourceSchemaInfo._QUERY_DELETE);
						preparedStatement.setString(1, address);
						preparedStatement.setString(2,
								binding.getContactAddress());
						preparedStatement.execute();
						if (this.tracer.isInfoEnabled()) {
							this.tracer.info("Removed binding: " + address
									+ " -> " + binding.getContactAddress());
						}

					} else {
						resultCode = Response.BAD_REQUEST;
						return this;
					}
				} else {
					removedBindings.add(binding);
					preparedStatement = connection
							.prepareStatement(DataSourceSchemaInfo._QUERY_DELETE);
					preparedStatement.setString(1, address);
					preparedStatement.setString(2, binding.getContactAddress());
					preparedStatement.execute();
					if (this.tracer.isInfoEnabled()) {
						this.tracer.info("Removed binding: " + address
								+ " -> " + binding.getContactAddress());
					}
				}
			}

			tx.commit();
			tx = null;

		} catch (Exception e) {
			tracer.severe("Failed to execute task", e);
			resultCode = Response.SERVER_INTERNAL_ERROR;
		} finally {

			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception f) {
					tracer.severe("failed to rollback tx", f);
				}
			}
		}

		return this;
	}

	@Override
	public void callBackParentOnException(
			DataSourceParentSbbLocalInterface parent) {
		parent.removeBindingsResult(Response.SERVER_INTERNAL_ERROR,
				EMPTY_BINDINGS_LIST, EMPTY_BINDINGS_LIST);
	}

	@Override
	public void callBackParentOnResult(DataSourceParentSbbLocalInterface parent) {
		if (resultCode > 299) {
			parent.removeBindingsResult(resultCode, EMPTY_BINDINGS_LIST,
					 EMPTY_BINDINGS_LIST);
		} else {
			parent.removeBindingsResult(resultCode, currentBindings,
					removedBindings);
		}
	}

}
