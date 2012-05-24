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

/**
 * 
 */
package org.mobicents.slee.resource.map.events;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPMessage;

/**
 * @author amit bhayani
 * 
 */
public abstract class MAPEvent<T extends MAPMessage> {
	protected final MAPDialog mapDialogWrapper;
	private final String eventTypeName;
	protected final T wrappedEvent;

	public MAPEvent(MAPDialog mAPDialog, String eventTypeName, T wrappedEvent) {
		this.mapDialogWrapper = mAPDialog;
		this.eventTypeName = eventTypeName;
		this.wrappedEvent = wrappedEvent;
	}

	public MAPDialog getMAPDialog() {
		return mapDialogWrapper;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setMAPDialog(MAPDialog arg0) {
		throw new UnsupportedOperationException();
	}
	
	public T getWrappedEvent(){
		return this.wrappedEvent;
	}

	@Override
	public String toString() {
		return "MAPEvent [MAPDialog=" + mapDialogWrapper + "]";
	}
}
