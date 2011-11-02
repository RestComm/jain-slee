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

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mobicents.slee.example.sjr.data.RegistrationBinding;

/**
 * 
 * @author martins
 *
 */
public class DataSourceSchemaInfo {

	// lets define some statics for table and queries in one place.
	public static final String _TABLE_NAME = "SIP_REGISTRAR";
	public static final String _COLUMN_SIP_ADDRESS = "SIP_ADDRESS";
	public static final String _COLUMN_CONTACT = "CONTACT";
	public static final String _COLUMN_EXPIRES = "EXPIRES";
	public static final String _COLUMN_REGISTER_DATE = "REGISTER_DATE";
	public static final String _COLUMN_Q = "Q"; // not a best name, but if we
												// add "VALUE" suffix, we will
												// have to add to all...
	public static final String _COLUMN_CALL_ID = "CALL_ID";
	public static final String _COLUMN_C_SEQ = "C_SEQ";

	// SQL queries.
	// drop table
	public static final String _QUERY_DROP = "DROP TABLE IF EXISTS "
			+ _TABLE_NAME + ";";
	// create table, use contact as PK, since it will be unique ?
	// | CONTACT (PK) | SIP_ADDRESS (PK) | Q | EXPIRES | REGISTER_DATE | CALL_ID
	// | C_SEQ |
	public static final String _QUERY_CREATE = "CREATE TABLE " + _TABLE_NAME
			+ " (" + _COLUMN_CONTACT + " VARCHAR(255) NOT NULL, "
			+ _COLUMN_SIP_ADDRESS + " VARCHAR(255) NOT NULL, " + _COLUMN_Q
			+ " FLOAT NOT NULL, " + _COLUMN_EXPIRES + " BIGINT NOT NULL, "
			+ _COLUMN_REGISTER_DATE + " BIGINT NOT NULL, " + _COLUMN_C_SEQ
			+ " BIGINT NOT NULL, " + _COLUMN_CALL_ID
			+ " VARCHAR(255) NOT NULL, " + "PRIMARY KEY(" + _COLUMN_CONTACT
			+ "," + _COLUMN_SIP_ADDRESS + ")" + ");";

	// select all contacts for AOR
	public static final String _QUERY_SELECT = "SELECT " + _COLUMN_CONTACT
			+ "," + _COLUMN_CALL_ID + ", " + _COLUMN_C_SEQ + ", "
			+ _COLUMN_EXPIRES + ", " + _COLUMN_Q + ", " + _COLUMN_REGISTER_DATE
			+ " FROM " + _TABLE_NAME + " WHERE " + _COLUMN_SIP_ADDRESS + "=?;";
	// delete row for AOR and contact
	public static final String _QUERY_DELETE = "DELETE FROM " + _TABLE_NAME
			+ " WHERE " + _COLUMN_SIP_ADDRESS + "=? AND " + _COLUMN_CONTACT
			+ "=?;";
	// update row for AOR and contact
	public static final String _QUERY_UPDATE = "UPDATE " + _TABLE_NAME + " "
			+ "SET " + _COLUMN_CALL_ID + "=?, " + _COLUMN_C_SEQ + "=?, "
			+ _COLUMN_EXPIRES + "=?, " + _COLUMN_Q + "=?, "
			+ _COLUMN_REGISTER_DATE + "=? " + "WHERE " + _COLUMN_SIP_ADDRESS
			+ "=? AND " + _COLUMN_CONTACT + "=?;";
	// inser new row(contact)
	public static final String _QUERY_INSERT = "INSERT INTO " + _TABLE_NAME
			+ " ( " + " " + _COLUMN_CALL_ID + ", " + _COLUMN_C_SEQ + ", "
			+ _COLUMN_EXPIRES + ", " + _COLUMN_Q + ", " + _COLUMN_REGISTER_DATE
			+ "," + _COLUMN_SIP_ADDRESS + ", " + _COLUMN_CONTACT + ") "
			+ "VALUES (?,?,?,?,?,?,?);";

	public static List<RegistrationBinding> getBindingsAsList(String address,
			ResultSet resultSet) throws Exception {
		// _COLUMN_CONTACT
		// _COLUMN_CALL_ID
		// _COLUMN_C_SEQ
		// _COLUMN_EXPIRES
		// _COLUMN_Q
		// _COLUMN_REGISTER_DATE
		List<RegistrationBinding> resultBindings = new ArrayList<RegistrationBinding>();
		while (resultSet.next()) {
			String contact = resultSet
					.getString(DataSourceSchemaInfo._COLUMN_CONTACT);
			String callId = resultSet
					.getString(DataSourceSchemaInfo._COLUMN_CALL_ID);
			long cSeq = resultSet.getLong(DataSourceSchemaInfo._COLUMN_C_SEQ);
			long expires = resultSet
					.getLong(DataSourceSchemaInfo._COLUMN_EXPIRES);
			float q = resultSet.getLong(DataSourceSchemaInfo._COLUMN_Q);
			long registerDate = resultSet
					.getLong(DataSourceSchemaInfo._COLUMN_REGISTER_DATE);
			RegistrationBinding binding = new RegistrationBinding(address,
					contact, expires, registerDate, q, callId, cSeq);
			resultBindings.add(binding);
		}

		return resultBindings;
	}

	public static Map<String, RegistrationBinding> getBindingsAsMap(
			String address, ResultSet resultSet) throws Exception {
		Map<String, RegistrationBinding> resultBindings = new HashMap<String, RegistrationBinding>();
		while (resultSet.next()) {
			String contact = resultSet
					.getString(DataSourceSchemaInfo._COLUMN_CONTACT);
			String callId = resultSet
					.getString(DataSourceSchemaInfo._COLUMN_CALL_ID);
			long cSeq = resultSet.getLong(DataSourceSchemaInfo._COLUMN_C_SEQ);
			long expires = resultSet
					.getLong(DataSourceSchemaInfo._COLUMN_EXPIRES);
			float q = resultSet.getLong(DataSourceSchemaInfo._COLUMN_Q);
			long registerDate = resultSet
					.getLong(DataSourceSchemaInfo._COLUMN_REGISTER_DATE);
			RegistrationBinding binding = new RegistrationBinding(address,
					contact, expires, registerDate, q, callId, cSeq);
			resultBindings.put(binding.getContactAddress(), binding);
		}

		return resultBindings;
	}
}
