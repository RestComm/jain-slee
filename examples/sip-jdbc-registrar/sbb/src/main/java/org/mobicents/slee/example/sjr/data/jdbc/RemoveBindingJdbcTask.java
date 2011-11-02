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

import java.sql.PreparedStatement;

import javax.slee.facilities.Tracer;

import org.mobicents.slee.example.sjr.data.DataSourceParentSbbLocalInterface;
import org.mobicents.slee.resource.jdbc.task.JdbcTaskContext;

/**
 * 
 * @author martins
 *
 */
public class RemoveBindingJdbcTask extends DataSourceJdbcTask {

	private final String address;
	private final String contact;
	private final Tracer tracer;

	public RemoveBindingJdbcTask(String address, String contact, Tracer tracer) {
		this.address = address;
		this.contact = contact;
		this.tracer = tracer;
	}

	@Override
	public Object executeSimple(JdbcTaskContext taskContext) {
		try {
			PreparedStatement preparedStatement = taskContext.getConnection()
					.prepareStatement(DataSourceSchemaInfo._QUERY_DELETE);
			preparedStatement.setString(1, address);
			preparedStatement.setString(2, contact);
			preparedStatement.execute();
			if (this.tracer.isInfoEnabled()) {
				this.tracer.info("Removed binding: " + address
						+ " -> " + contact);
			}
		} catch (Exception e) {
			tracer.severe("failed to remove binding", e);
		}
		return this;
	}

	@Override
	public void callBackParentOnException(
			DataSourceParentSbbLocalInterface parent) {
		// nothing to call back
	}

	@Override
	public void callBackParentOnResult(DataSourceParentSbbLocalInterface parent) {
		// nothing to call back
	}
}
