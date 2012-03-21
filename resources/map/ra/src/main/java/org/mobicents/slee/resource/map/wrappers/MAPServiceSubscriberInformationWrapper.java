/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.map.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MAPDialogSubscriberInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MAPServiceSubscriberInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MAPServiceSubscriberInformationListener;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author abhayani
 * 
 */
public class MAPServiceSubscriberInformationWrapper implements MAPServiceSubscriberInformation {

	protected MAPServiceSubscriberInformation wrappedSubsInfo;
	protected MAPProviderWrapper mapProviderWrapper;

	/**
	 * @param wrappedSubsInfo
	 * @param mapProviderWrapper
	 */
	public MAPServiceSubscriberInformationWrapper(MAPProviderWrapper mapProviderWrapper, MAPServiceSubscriberInformation wrappedSubsInfo) {
		super();
		this.wrappedSubsInfo = wrappedSubsInfo;
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
		return this.wrappedSubsInfo.isActivated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceBase#isServingService(org
	 * .mobicents.protocols.ss7.map.api.MAPApplicationContext)
	 */
	@Override
	public ServingCheckData isServingService(MAPApplicationContext mapapplicationcontext) {
		return this.wrappedSubsInfo.isServingService(mapapplicationcontext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * MAPServiceSubscriberInformation
	 * #addMAPServiceListener(org.mobicents.protocols
	 * .ss7.map.api.service.subscriberInformation
	 * .MAPServiceSubscriberInformationListener)
	 */
	@Override
	public void addMAPServiceListener(MAPServiceSubscriberInformationListener arg0) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * MAPServiceSubscriberInformation
	 * #createNewDialog(org.mobicents.protocols.ss7
	 * .map.api.MAPApplicationContext,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.map.api.primitives.AddressString,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.map.api.primitives.AddressString)
	 */
	@Override
	public MAPDialogSubscriberInformation createNewDialog(MAPApplicationContext mapapplicationcontext, SccpAddress sccpaddress, AddressString addressstring,
			SccpAddress sccpaddress1, AddressString addressstring1) throws MAPException {
		try {
			MAPDialogSubscriberInformation mapDialog = this.wrappedSubsInfo.createNewDialog(mapapplicationcontext, sccpaddress, addressstring, sccpaddress1,
					addressstring1);
			mapProviderWrapper.ra.createActivity(mapDialog);
			return mapDialog;
		} catch (Exception e) {
			throw new MAPException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * MAPServiceSubscriberInformation
	 * #removeMAPServiceListener(org.mobicents.protocols
	 * .ss7.map.api.service.subscriberInformation
	 * .MAPServiceSubscriberInformationListener)
	 */
	@Override
	public void removeMAPServiceListener(MAPServiceSubscriberInformationListener arg0) {
		throw new UnsupportedOperationException();
	}

}
