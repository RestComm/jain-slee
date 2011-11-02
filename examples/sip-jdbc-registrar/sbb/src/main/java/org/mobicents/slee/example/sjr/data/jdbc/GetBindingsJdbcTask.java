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
import java.sql.ResultSet;
import java.util.List;

import javax.sip.message.Response;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.example.sjr.data.DataSourceParentSbbLocalInterface;
import org.mobicents.slee.example.sjr.data.RegistrationBinding;
import org.mobicents.slee.resource.jdbc.task.JdbcTaskContext;

/**
 * 
 * @author martins
 *
 */
public class GetBindingsJdbcTask extends DataSourceJdbcTask {

	private List<RegistrationBinding> bindings = null;

	private final String address;
	private final Tracer tracer;
	
	public GetBindingsJdbcTask(String address, Tracer tracer) {
		this.address = address;
		this.tracer = tracer;
	}

	@Override
	public Object executeSimple(JdbcTaskContext taskContext) {
		try {
			PreparedStatement preparedStatement = taskContext.getConnection()
					.prepareStatement(DataSourceSchemaInfo._QUERY_SELECT);
			preparedStatement.setString(1, address);
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();
			bindings = DataSourceSchemaInfo.getBindingsAsList(address, resultSet);
		} catch (Exception e) {
			tracer.severe("failed to execute task to get bindings of "+address,e);
		}
		return this;
	}

	@Override
	public void callBackParentOnException(
			DataSourceParentSbbLocalInterface parent) {
		parent.getBindingsResult(Response.SERVER_INTERNAL_ERROR,
				EMPTY_BINDINGS_LIST);

	}

	@Override
	public void callBackParentOnResult(DataSourceParentSbbLocalInterface parent) {
		if (bindings == null) {
			parent.getBindingsResult(Response.SERVER_INTERNAL_ERROR, EMPTY_BINDINGS_LIST);
		}
		else {
			parent.getBindingsResult(Response.OK, bindings);

		}
	}

}
