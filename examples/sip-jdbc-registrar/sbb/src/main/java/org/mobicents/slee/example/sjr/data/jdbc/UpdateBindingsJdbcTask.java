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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.sip.header.ContactHeader;
import javax.sip.message.Response;
import javax.slee.facilities.Tracer;
import javax.slee.transaction.SleeTransaction;

import org.mobicents.slee.example.sjr.data.DataSourceParentSbbLocalInterface;
import org.mobicents.slee.example.sjr.data.RegistrationBinding;
import org.mobicents.slee.resource.jdbc.task.JdbcTaskContext;

/**
 * 
 * @author martins
 *
 */
public class UpdateBindingsJdbcTask extends DataSourceJdbcTask {

	private int resultCode = Response.OK;
	private List<RegistrationBinding> currentBindings = null;
	private List<RegistrationBinding> updatedBindings = null;
	private List<RegistrationBinding> removedBindings = null;

	private final String address;
	private final String callId;
	private final long cSeq;
	private final List<ContactHeader> contacts;

	private final Tracer tracer;

	public UpdateBindingsJdbcTask(String address, String callId, long cSeq,
			List<ContactHeader> contacts, Tracer tracer) {
		this.address = address;
		this.callId = callId;
		this.cSeq = cSeq;
		this.contacts = contacts;
		this.tracer = tracer;
	}

	@Override
	public Object executeSimple(JdbcTaskContext taskContext) {

		// fetch those values, yes, we do some things twice, its done to
		// avoid pushing everythin into JDBC execute.

		// final ExpiresHeader expiresHeader =
		// super.event.getRequest().getExpires();
		ListIterator<ContactHeader> li = this.contacts.listIterator();
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

			// lets have it as map, it will be easier to manipulate in this
			// case.
			Map<String, RegistrationBinding> bindings = DataSourceSchemaInfo
					.getBindingsAsMap(address, resultSet);
			removedBindings = new ArrayList<RegistrationBinding>();
			updatedBindings = new ArrayList<RegistrationBinding>();

			while (li.hasNext()) {
				ContactHeader contact = li.next();

				//
				// get expires value, either in header or default
				// do min-expires etc
				long requestedExpires = contact.getExpires();

				float q = 0;
				if (contact.getQValue() != -1)
					q = contact.getQValue();

				// Find existing binding
				String contactAddress = contact.getAddress().getURI()
						.toString();

				RegistrationBinding binding = (RegistrationBinding) bindings
						.get(contactAddress);

				if (binding != null) { // Update this binding

					if (this.callId.equals(binding.getCallId())) {
						if (this.cSeq <= binding.getCSeq()) {
							resultCode = Response.BAD_REQUEST;
							return this;
						}
					}

					if (requestedExpires == 0) {
						bindings.remove(contactAddress);
						removedBindings.add(binding);
						preparedStatement = connection
								.prepareStatement(DataSourceSchemaInfo._QUERY_DELETE);
						preparedStatement.setString(1, address);
						preparedStatement.setString(2,
								binding.getContactAddress());
						preparedStatement.execute();
						if (this.tracer.isInfoEnabled()) {
							this.tracer.info("Removed binding: " + address
									+ " -> " + contactAddress);
						}
					} else {
						// udpate binding in map, it will be sent back
						binding.setCallId(callId);
						binding.setExpires(requestedExpires);
						binding.setRegistrationDate(System.currentTimeMillis());
						binding.setCSeq(this.cSeq);
						binding.setQValue(q);
						updatedBindings.add(binding);
						// udpate DB
						preparedStatement = connection
								.prepareStatement(DataSourceSchemaInfo._QUERY_UPDATE);
						preparedStatement.setString(1, binding.getCallId());
						preparedStatement.setLong(2, binding.getCSeq());
						preparedStatement.setLong(3, binding.getExpires());
						preparedStatement.setFloat(4, binding.getQValue());
						preparedStatement.setLong(5,
								binding.getRegistrationDate());

						preparedStatement.setString(6, address);
						preparedStatement.setString(7,
								binding.getContactAddress());
						preparedStatement.execute();
						if (this.tracer.isInfoEnabled()) {
							this.tracer.info("Updated binding: " + address
									+ " -> " + contactAddress);
						}
					}

				} else {

					// Create new binding
					if (requestedExpires != 0) {
						RegistrationBinding newRegistrationBinding = new RegistrationBinding(
								address, contactAddress, requestedExpires,
								System.currentTimeMillis(), q, callId,
								this.cSeq);
						// put in bindings
						bindings.put(
								newRegistrationBinding.getContactAddress(),
								newRegistrationBinding);
						updatedBindings.add(newRegistrationBinding);
						// update DB
						preparedStatement = connection
								.prepareStatement(DataSourceSchemaInfo._QUERY_INSERT);
						preparedStatement.setString(1,
								newRegistrationBinding.getCallId());
						preparedStatement.setLong(2,
								newRegistrationBinding.getCSeq());
						preparedStatement.setLong(3,
								newRegistrationBinding.getExpires());
						preparedStatement.setFloat(4,
								newRegistrationBinding.getQValue());
						preparedStatement.setLong(5,
								newRegistrationBinding.getRegistrationDate());

						preparedStatement.setString(6, address);
						preparedStatement.setString(7,
								newRegistrationBinding.getContactAddress());
						preparedStatement.execute();
						if (this.tracer.isInfoEnabled()) {
							this.tracer.info("Added new binding: " + address
									+ " -> " + contactAddress);
						}
					}
				}
			}
			// now lets push current bindings
			currentBindings = new ArrayList<RegistrationBinding>(
					bindings.values());
			tx.commit();
			tx = null;

		} catch (Exception e) {
			tracer.severe("Failed to execute jdbc task.", e);
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
		parent.updateBindingsResult(Response.SERVER_INTERNAL_ERROR,
				EMPTY_BINDINGS_LIST, EMPTY_BINDINGS_LIST, EMPTY_BINDINGS_LIST);
	}

	@Override
	public void callBackParentOnResult(DataSourceParentSbbLocalInterface parent) {
		if (resultCode > 299) {
			parent.updateBindingsResult(resultCode, EMPTY_BINDINGS_LIST,
					EMPTY_BINDINGS_LIST, EMPTY_BINDINGS_LIST);
		} else {
			parent.updateBindingsResult(resultCode, currentBindings,
					updatedBindings, removedBindings);
		}
	}

}
