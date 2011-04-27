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

package org.mobicents.slee.resources.smpp;

import javax.slee.resource.ActivityHandle;

/**
 * 
 * @author amit bhayani
 *
 */
public class SmppTransactionHandle implements ActivityHandle {
	
	private final SmppTransactionType type;
	private final long seqNumber;

	public SmppTransactionHandle(long seqNum, SmppTransactionType type) {
		this.seqNumber = seqNum;
		this.type = type;
	}

	protected long getSeqNumber() {
		return this.seqNumber;
	}

	public SmppTransactionType getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
		return (int) seqNumber * 31 + type.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SmppTransactionHandle other = (SmppTransactionHandle) obj;
		if (seqNumber != other.seqNumber)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SmppTransactionHandle[seqNumber=" + seqNumber + ",type="+type+"]";
	}

}
