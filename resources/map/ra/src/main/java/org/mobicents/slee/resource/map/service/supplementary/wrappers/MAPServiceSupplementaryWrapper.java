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

package org.mobicents.slee.resource.map.service.supplementary.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.wrappers.MAPProviderWrapper;

/**
 * @author baranowb
 * @author amit bhayani
 * 
 */
public class MAPServiceSupplementaryWrapper implements MAPServiceSupplementary {

	protected MAPServiceSupplementary wrappedUSSD;
	protected MAPProviderWrapper mapProviderWrapper;

	/**
	 * @param mapServiceSupplementary
	 */
	public MAPServiceSupplementaryWrapper(MAPProviderWrapper mapProviderWrapper,
			MAPServiceSupplementary mapServiceSupplementary) {
		this.wrappedUSSD = mapServiceSupplementary;
		this.mapProviderWrapper = mapProviderWrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceBase#acivate()
	 */
	public void acivate() {
		throw new UnsupportedOperationException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceBase#deactivate()
	 */
	public void deactivate() {
		throw new UnsupportedOperationException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceBase#getMAPProvider()
	 */
	public MAPProvider getMAPProvider() {
		return this.mapProviderWrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceBase#isActivated()
	 */
	public boolean isActivated() {
		return this.wrappedUSSD.isActivated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceBase#isServingService(org
	 * .mobicents.protocols.ss7.map.api.MAPApplicationContext)
	 */
	public ServingCheckData isServingService(MAPApplicationContext mapapplicationcontext) {
		return this.wrappedUSSD.isServingService(mapapplicationcontext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.
	 * MAPServiceSupplementary
	 * #addMAPServiceListener(org.mobicents.protocols.ss7.
	 * map.api.service.supplementary.MAPServiceSupplementaryListener)
	 */
	public void addMAPServiceListener(MAPServiceSupplementaryListener mapservicesupplementarylistener) {
		throw new UnsupportedOperationException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.
	 * MAPServiceSupplementary
	 * #createNewDialog(org.mobicents.protocols.ss7.map.api
	 * .MAPApplicationContext,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.map.api.primitives.AddressString,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.map.api.primitives.AddressString)
	 */
	public MAPDialogSupplementary createNewDialog(MAPApplicationContext mapapplicationcontext, SccpAddress sccpaddress,
			AddressString addressstring, SccpAddress sccpaddress1, AddressString addressstring1) throws MAPException {

		MAPDialogSupplementary mapDialog = this.wrappedUSSD.createNewDialog(mapapplicationcontext, sccpaddress,
				addressstring, sccpaddress1, addressstring1);
		MAPDialogActivityHandle activityHandle = new MAPDialogActivityHandle(mapDialog.getDialogId());
		MAPDialogSupplementaryWrapper dw = new MAPDialogSupplementaryWrapper(mapDialog, activityHandle,
				this.mapProviderWrapper.getRa());
		mapDialog.setUserObject(dw);

		try {
			this.mapProviderWrapper.getRa().startSuspendedActivity(dw);
		} catch (Exception e) {
			throw new MAPException(e);
		}

		return dw;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.
	 * MAPServiceSupplementary
	 * #removeMAPServiceListener(org.mobicents.protocols.ss7
	 * .map.api.service.supplementary.MAPServiceSupplementaryListener)
	 */
	public void removeMAPServiceListener(MAPServiceSupplementaryListener mapservicesupplementarylistener) {
		throw new UnsupportedOperationException();

	}

}
