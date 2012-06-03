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

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFactory;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSms;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.MAPResourceAdaptor;
import org.mobicents.slee.resource.map.service.lsm.wrappers.MAPServiceLsmWrapper;
import org.mobicents.slee.resource.map.service.mobility.wrappers.MAPServiceMobilityWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.MAPServiceSmsWrapper;
import org.mobicents.slee.resource.map.service.supplementary.wrappers.MAPServiceSupplementaryWrapper;

/**
 * @author baranowb
 * @author amit bhayani
 * 
 */
public class MAPProviderWrapper implements MAPProvider {

	// //////////////////////////////
	// Wrappers for MAP specifics //
	// //////////////////////////////
	private MAPProvider wrappedProvider;
	private MAPServiceSupplementaryWrapper wrappedUSSD;
	private MAPServiceLsmWrapper wrappedLSM;
	private MAPServiceSmsWrapper wrappedSMS;
	private MAPServiceMobilityWrapper wrappedMAPServiceMobility;

	private final MAPResourceAdaptor ra;

	/**
	 * @param wrappedProvider
	 * @param ra
	 */
	public MAPProviderWrapper(MAPResourceAdaptor ra) {
		super();

		this.ra = ra;

		// now create service wrappers

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPProvider#addMAPDialogListener(
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener)
	 */
	public void addMAPDialogListener(MAPDialogListener mapdialoglistener) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPProvider#removeMAPDialogListener
	 * (org.mobicents.protocols.ss7.map.api.MAPDialogListener)
	 */
	public void removeMAPDialogListener(MAPDialogListener mapdialoglistener) {
		throw new UnsupportedOperationException();

	}

	public MAPParameterFactory getMAPParameterFactory() {
		if (this.wrappedProvider == null) {
			throw new IllegalStateException("RA is has not been activated.");
		}
		return this.wrappedProvider.getMAPParameterFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPProvider#getMAPErrorMessageFactory
	 * ()
	 */
	public MAPErrorMessageFactory getMAPErrorMessageFactory() {
		if (this.wrappedProvider == null) {
			throw new IllegalStateException("RA is has not been activated.");
		}
		return this.wrappedProvider.getMAPErrorMessageFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPProvider#getMAPDialog(java.lang
	 * .Long)
	 */
	public MAPDialog getMAPDialog(Long long1) {
		if (this.wrappedProvider == null) {
			throw new IllegalStateException("RA is has not been activated.");
		}
		MAPDialogActivityHandle ah = new MAPDialogActivityHandle(long1);
		return (MAPDialog) this.ra.getActivity(ah);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPProvider#getMAPServiceSupplementary
	 * ()
	 */
	public MAPServiceSupplementary getMAPServiceSupplementary() {
		if (this.wrappedProvider == null) {
			throw new IllegalStateException("RA is has not been activated.");
		}
		return this.wrappedUSSD;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPProvider#getMAPServiceSms()
	 */
	public MAPServiceSms getMAPServiceSms() {
		if (this.wrappedProvider == null) {
			throw new IllegalStateException("RA is has not been activated.");
		}
		return this.wrappedSMS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPProvider#getMAPServiceLsm()
	 */
	public MAPServiceLsm getMAPServiceLsm() {
		if (this.wrappedProvider == null) {
			throw new IllegalStateException("RA is has not been activated.");
		}
		return this.wrappedLSM;
	}

	public MAPServiceMobility getMAPServiceMobility() {
		if (this.wrappedProvider == null) {
			throw new IllegalStateException("RA is has not been activated.");
		}
		return this.wrappedMAPServiceMobility;
	}

	public void setWrappedProvider(MAPProvider wrappedProvider) {
		this.wrappedProvider = wrappedProvider;
		this.wrappedUSSD = new MAPServiceSupplementaryWrapper(this, wrappedProvider.getMAPServiceSupplementary());
		this.wrappedLSM = new MAPServiceLsmWrapper(this, wrappedProvider.getMAPServiceLsm());
		this.wrappedSMS = new MAPServiceSmsWrapper(this, wrappedProvider.getMAPServiceSms());
		this.wrappedMAPServiceMobility = new MAPServiceMobilityWrapper(this, wrappedProvider.getMAPServiceMobility());
	}

	public MAPResourceAdaptor getRa() {
		return ra;
	}

}
