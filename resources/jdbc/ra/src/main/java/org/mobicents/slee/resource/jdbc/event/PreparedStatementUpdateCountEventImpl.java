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

/**
 * 
 * @author martins
 * 
 */
public class PreparedStatementUpdateCountEventImpl extends
		AbstractPreparedStatementEvent implements
		PreparedStatementUpdateCountEvent {

	/**
	 * 
	 */
	private final int updateCount;

	/**
	 * 
	 * @param preparedStatement
	 * @param updateCount
	 */
	public PreparedStatementUpdateCountEventImpl(
			PreparedStatement preparedStatement, int updateCount) {
		super(preparedStatement);
		this.updateCount = updateCount;
	}

	@Override
	public int getUpdateCount() {
		return updateCount;
	}

}
