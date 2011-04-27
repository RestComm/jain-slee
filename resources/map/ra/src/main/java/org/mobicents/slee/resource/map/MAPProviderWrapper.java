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

package org.mobicents.slee.resource.map;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPProviderWrapper implements MAPProvider {

	private org.mobicents.protocols.ss7.map.api.MAPProvider mapProvider = null;
	private MAPResourceAdaptor mapRsrcAdap = null;

	protected MAPProviderWrapper(MAPResourceAdaptor mapRsrcAdap) {
		this.mapRsrcAdap = mapRsrcAdap;
	}

	public void setMapProvider(
			org.mobicents.protocols.ss7.map.api.MAPProvider mapProvider) {
		this.mapProvider = mapProvider;
	}
	
	public void addMAPDialogListener(MAPDialogListener arg0) {
		throw new UnsupportedOperationException();
	}

	public void addMAPServiceListener(MAPServiceListener arg0) {
		throw new UnsupportedOperationException();
	}

	public MAPDialog createNewDialog(MAPApplicationContext appCntx, SccpAddress destAddress,
			AddressString destReference, SccpAddress origAddress, AddressString origReference) throws MAPException {
		MAPDialog mapDialog = mapProvider.createNewDialog(appCntx, destAddress, destReference, origAddress,
				origReference);
		return new MAPDialogWrapper(this.mapRsrcAdap, mapDialog);
	}

	public MapServiceFactory getMapServiceFactory() {
		return mapProvider.getMapServiceFactory();
	}

	public void removeMAPDialogListener(MAPDialogListener arg0) {
		throw new UnsupportedOperationException();
	}

	public void removeMAPServiceListener(MAPServiceListener arg0) {
		throw new UnsupportedOperationException();
	}

	public MAPDialog getMAPDialog(Long dialogId) {
		return mapProvider.getMAPDialog(dialogId);
	}

}
