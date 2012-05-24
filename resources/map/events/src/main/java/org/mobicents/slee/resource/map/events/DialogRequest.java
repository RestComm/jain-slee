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
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * @author amit bhayani
 * 
 */
public class DialogRequest extends MAPEvent {

	private static final String EVENT_TYPE_NAME = "ss7.map.DIALOG_REQUEST";

	private boolean eriStyle = false;
	private final AddressString destReference;
	private final AddressString origReference;
	private final MAPExtensionContainer extensionContainer;
	private final IMSI eriImsi;
	private final AddressString eriVlrNo;

	/**
	 * @param mapDialogWrapper
	 */
	public DialogRequest(MAPDialog mapDialogWrapper, AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer, IMSI eriImsi, AddressString eriVlrNo) {
		super(mapDialogWrapper, EVENT_TYPE_NAME, null);
		this.destReference = destReference;
		this.origReference = origReference;
		this.extensionContainer = extensionContainer;
		this.eriImsi = eriImsi;
		this.eriVlrNo = eriVlrNo;
	}

	public AddressString getDestReference() {
		return destReference;
	}

	public AddressString getOrigReference() {
		return origReference;
	}

	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	public boolean isEriStyle() {
		return eriStyle;
	}

	public IMSI getEriImsi() {
		return eriImsi;
	}

	public AddressString getEriVlrNo() {
		return eriVlrNo;
	}

	@Override
	public String toString() {
		return "DialogRequest [eriStyle=" + eriStyle + ", destReference=" + destReference + ", origReference="
				+ origReference + ", extensionContainer=" + extensionContainer + ", eriImsi=" + eriImsi + ", eriVlrNo="
				+ eriVlrNo + ", " + this.mapDialogWrapper + "]";
	}

}
