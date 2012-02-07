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

package org.mobicents.slee.resource.map.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author baranowb
 *
 */
public class MAPServiceSmsWrapper implements MAPServiceSms {

	protected MAPServiceSms wrappedSMS;
	protected MAPProviderWrapper mapProviderWrapper;

	/**
	 * @param mapServiceSupplementary
	 */
	public MAPServiceSmsWrapper(MAPProviderWrapper mapProviderWrapper, MAPServiceSms mapServiceSupplementary) {
		this.wrappedSMS = mapServiceSupplementary;
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
		return this.wrappedSMS.isActivated();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceBase#isActivated()
	 */
	@Override
	public ServingCheckData isServingService(MAPApplicationContext mapapplicationcontext) {
		return this.wrappedSMS.isServingService(mapapplicationcontext);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSms#addMAPServiceListener(org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener)
	 */
	@Override
	public void addMAPServiceListener(MAPServiceSmsListener mapservicesmslistener) {
		throw new UnsupportedOperationException();

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSms#createNewDialog(org.mobicents.protocols.ss7.map.api.MAPApplicationContext, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.map.api.primitives.AddressString, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.map.api.primitives.AddressString)
	 */
	@Override
	public MAPDialogSms createNewDialog(MAPApplicationContext mapapplicationcontext, SccpAddress sccpaddress, AddressString addressstring,
			SccpAddress sccpaddress1, AddressString addressstring1) throws MAPException {
		try {
			MAPDialogSms mapDialog = this.wrappedSMS.createNewDialog(mapapplicationcontext, sccpaddress, addressstring, sccpaddress1, addressstring1);
			mapProviderWrapper.ra.createActivity(mapDialog);
			return mapDialog;
		} catch (Exception e) {
			throw new MAPException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSms#removeMAPServiceListener(org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener)
	 */
	@Override
	public void removeMAPServiceListener(MAPServiceSmsListener mapservicesmslistener) {
		throw new UnsupportedOperationException();

	}

}
