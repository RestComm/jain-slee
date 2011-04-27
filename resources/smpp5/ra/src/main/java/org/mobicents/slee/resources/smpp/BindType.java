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

/**
 * 
 * @author amit bhayani
 *
 */
public enum BindType {
	TRANSMITTER("TRANSMITTER"), RECEIVER("RECEIVER"), TRANSCEIVER("TRANSCEIVER");

	private String type;

	private BindType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public static BindType getBindType(String type) {
		if (type == null) {
			return null;
		} else if (type.compareTo(TRANSMITTER.getType()) == 0) {
			return TRANSMITTER;
		} else if (type.compareTo(RECEIVER.getType()) == 0) {
			return RECEIVER;
		} else if (type.compareTo(TRANSCEIVER.getType()) == 0) {
			return TRANSCEIVER;
		}

		return null;
	}
}
