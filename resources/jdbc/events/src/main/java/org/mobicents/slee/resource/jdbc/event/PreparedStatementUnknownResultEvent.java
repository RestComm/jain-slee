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

package org.mobicents.slee.resource.jdbc.event;

import java.sql.PreparedStatement;

import javax.slee.EventTypeID;

/**
 * An event which provides the results for the execution of unknown sql in a
 * {@link PreparedStatement}. Concrete results should be retrieved from the
 * {@link PreparedStatement} object, through
 * {@link PreparedStatement#getResultSet()},
 * {@link PreparedStatement#getUpdateCount()} and
 * {@link PreparedStatement#getMoreResults()}
 * 
 * @author martins
 * 
 */
public interface PreparedStatementUnknownResultEvent extends
		PreparedStatementEvent {

	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID(
			PreparedStatementUnknownResultEvent.class.getSimpleName(),
			"org.mobicents", "1.0");
	
	/**
	 * Retrieves the statement sql execution result.
	 * 
	 * @return <code>true</code> if the first result is a <code>ResultSet</code>
	 *         object; <code>false</code> if it is an update count or there are
	 *         no results
	 */
	public boolean getExecutionResult();

}
