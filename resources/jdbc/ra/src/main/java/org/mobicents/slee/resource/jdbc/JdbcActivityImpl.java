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

package org.mobicents.slee.resource.jdbc;

import javax.slee.resource.ActivityHandle;
import org.mobicents.slee.resource.jdbc.task.JdbcTask;

/**
 * Implementation of the JDBC RA activity object.
 * 
 * @author martins
 * 
 */
public class JdbcActivityImpl implements JdbcActivity, ActivityHandle {

	/**
	 * 
	 */
	private final JdbcResourceAdaptor ra;

	/**
	 * 
	 */
	private final String id;

	/**
	 * 
	 * @param ra
	 */
	public JdbcActivityImpl(JdbcResourceAdaptor ra, String id) {
		this.ra = ra;
		this.id = id;
	}

    public String getRaEntityName() {
        return ra.getContext().getEntityName();
    }

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final JdbcActivityImpl other = (JdbcActivityImpl) obj;
		
        if (!this.id.equals(other.id)) {
            return false;
	}
        if (!this.getRaEntityName().equals(other.getRaEntityName())) {
            return false;
        }
        return true;
	}

	// activity interface

	@Override
	public void execute(JdbcTask task) {
		ra.execute(task, this);
	}
		
	@Override
	public void endActivity() {
		ra.endActivity(this);
	}

}
