package org.mobicents.slee.resource.map.service.mobility.authentication.wrapper;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.slee.resource.map.service.mobility.wrappers.MAPDialogMobilityWrapper;
import org.mobicents.slee.resource.map.service.mobility.wrappers.MobilityMessageWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class SendAuthenticationInfoResponseWrapper extends MobilityMessageWrapper<SendAuthenticationInfoResponse>
		implements SendAuthenticationInfoResponse {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.mobility.authentication.SEND_AUTHENTICATION_INFO_RESPONSE";

	/**
	 * @param mAPDialog
	 */
	public SendAuthenticationInfoResponseWrapper(MAPDialogMobilityWrapper mAPDialog, SendAuthenticationInfoResponse res) {
		super(mAPDialog, EVENT_TYPE_NAME, res);
	}

	public AuthenticationSetList getAuthenticationSetList() {
		return this.wrappedEvent.getAuthenticationSetList();
	}

	public EpsAuthenticationSetList getEpsAuthenticationSetList() {
		return this.wrappedEvent.getEpsAuthenticationSetList();
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	public long getMapProtocolVersion() {
		return this.wrappedEvent.getMapProtocolVersion();
	}

	@Override
	public String toString() {
		return "SendAuthenticationInfoResponseWrapper [wrappedEvent=" + wrappedEvent + "]";
	}

}
