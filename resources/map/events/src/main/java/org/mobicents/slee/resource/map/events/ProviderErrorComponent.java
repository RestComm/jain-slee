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

package org.mobicents.slee.resource.map.events;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;

/**
 * @author baranowb
 * 
 */
public class ProviderErrorComponent extends ComponentEvent {

	private static final String EVENT_TYPE_NAME = "ss7.map.PROVIDER_ERROR_COMPONENT";

	protected MAPProviderError mapProviderError;

	/**
	 * @param mapDialogWrapper
	 * @param invokeId
	 */
	public ProviderErrorComponent(MAPDialog mAPDialog, Long invokeId, MAPProviderError mapProviderError) {
		super(mAPDialog, invokeId, EVENT_TYPE_NAME);
		this.mapProviderError = mapProviderError;
	}

	public MAPProviderError getMAPProviderError() {
		return this.mapProviderError;
	}

	@Override
	public String toString() {
		return "ProviderErrorComponent [mapProviderError=" + mapProviderError + ", invokeId=" + invokeId
				+ ", mapDialogWrapper=" + mapDialogWrapper + "]";
	}

}
