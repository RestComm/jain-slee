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

package net.java.slee.resource.mgcp.event;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;

import java.io.Serializable;
import java.util.UUID;

/**
 * Activity timeout event.
 * 
 * @author eduardomartins
 * @author amit bhayani
 * 
 */
public final class TransactionTimeout implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6178141966339265518L;

	private final String id;
	private JainMgcpCommandEvent event;

	public TransactionTimeout(JainMgcpCommandEvent event) {
		id = UUID.randomUUID().toString();
		this.event = event;
	}

	protected String getId() {
		return id;
	}

	public int hashCode() {
		return getId().hashCode();
	}

	public JainMgcpCommandEvent getJainMgcpCommandEvent() {
		return this.event;
	}

	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((TransactionTimeout) obj).getId() == getId();
		} else {
			return false;
		}
	}

}
