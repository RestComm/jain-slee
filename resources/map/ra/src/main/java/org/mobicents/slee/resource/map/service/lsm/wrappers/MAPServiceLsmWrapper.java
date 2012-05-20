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

package org.mobicents.slee.resource.map.service.lsm.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.wrappers.MAPProviderWrapper;

/**
 * @author baranowb
 *
 */
public class MAPServiceLsmWrapper implements MAPServiceLsm {
	
	protected MAPServiceLsm wrappedLSM;
	protected MAPProviderWrapper mapProviderWrapper;

	/**
	 * @param mapServiceSupplementary
	 */
	public MAPServiceLsmWrapper(MAPProviderWrapper mapProviderWrapper, MAPServiceLsm mapServiceSupplementary) {
		this.wrappedLSM = mapServiceSupplementary;
		this.mapProviderWrapper = mapProviderWrapper;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceBase#acivate()
	 */
	@Override
	public void acivate() {
		throw new UnsupportedOperationException();

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceBase#deactivate()
	 */
	@Override
	public void deactivate() {
		throw new UnsupportedOperationException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceBase#getMAPProvider()
	 */
	@Override
	public MAPProvider getMAPProvider() {
		return this.mapProviderWrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceBase#isActivated()
	 */
	@Override
	public boolean isActivated() {
		return this.wrappedLSM.isActivated();
	}


	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceBase#isServingService(org.mobicents.protocols.ss7.map.api.MAPApplicationContext)
	 */
	@Override
	public ServingCheckData isServingService(MAPApplicationContext mapapplicationcontext) {
		return this.wrappedLSM.isServingService(mapapplicationcontext);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm#addMAPServiceListener(org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener)
	 */
	@Override
	public void addMAPServiceListener(MAPServiceLsmListener mapservicelsmlistener) {
		throw new UnsupportedOperationException();

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm#createNewDialog(org.mobicents.protocols.ss7.map.api.MAPApplicationContext, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.map.api.primitives.AddressString, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.map.api.primitives.AddressString)
	 */
	@Override
	public MAPDialogLsm createNewDialog(MAPApplicationContext mapapplicationcontext, SccpAddress sccpaddress, AddressString addressstring,
			SccpAddress sccpaddress1, AddressString addressstring1) throws MAPException {

		MAPDialogLsm mapDialogLsm = this.wrappedLSM.createNewDialog(mapapplicationcontext, sccpaddress, addressstring, sccpaddress1, addressstring1);
		MAPDialogActivityHandle activityHandle = new MAPDialogActivityHandle(mapDialogLsm.getDialogId());
		MAPDialogLsmWrapper dw = new MAPDialogLsmWrapper(mapDialogLsm, activityHandle, this.mapProviderWrapper.getRa());
		mapDialogLsm.setUserObject(dw);
		
		try {
			this.mapProviderWrapper.getRa().startSuspendedActivity(dw);
		} catch (Exception e) {
			throw new MAPException(e);
		}
		
		return dw;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm#removeMAPServiceListener(org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener)
	 */
	@Override
	public void removeMAPServiceListener(MAPServiceLsmListener mapservicelsmlistener) {
		throw new UnsupportedOperationException();
	}

}
