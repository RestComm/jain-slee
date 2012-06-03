package org.mobicents.slee.resource.map.service.mobility.authentication.wrapper;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.slee.resource.map.service.mobility.wrappers.MAPDialogMobilityWrapper;
import org.mobicents.slee.resource.map.service.mobility.wrappers.MobilityMessageWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class SendAuthenticationInfoRequestWrapper extends MobilityMessageWrapper<SendAuthenticationInfoRequest>
		implements SendAuthenticationInfoRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.mobility.authentication.SEND_AUTHENTICATION_INFO_REQUEST";

	/**
	 * @param mAPDialog
	 */
	public SendAuthenticationInfoRequestWrapper(MAPDialogMobilityWrapper mAPDialog, SendAuthenticationInfoRequest req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	public boolean getAdditionalVectorsAreForEPS() {
		return this.wrappedEvent.getAdditionalVectorsAreForEPS();
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	public boolean getImmediateResponsePreferred() {
		return this.wrappedEvent.getImmediateResponsePreferred();
	}

	public IMSI getImsi() {
		return this.wrappedEvent.getImsi();
	}

	public long getMapProtocolVersion() {
		return this.wrappedEvent.getMapProtocolVersion();
	}

	public Integer getNumberOfRequestedAdditionalVectors() {
		return this.wrappedEvent.getNumberOfRequestedAdditionalVectors();
	}

	public int getNumberOfRequestedVectors() {
		return this.wrappedEvent.getNumberOfRequestedVectors();
	}

	public ReSynchronisationInfo getReSynchronisationInfo() {
		return this.wrappedEvent.getReSynchronisationInfo();
	}

	public RequestingNodeType getRequestingNodeType() {
		return this.wrappedEvent.getRequestingNodeType();
	}

	public PlmnId getRequestingPlmnId() {
		return this.wrappedEvent.getRequestingPlmnId();
	}

	public boolean getSegmentationProhibited() {
		return this.wrappedEvent.getSegmentationProhibited();
	}

	@Override
	public String toString() {
		return "SendAuthenticationInfoRequestWrapper [wrappedEvent=" + wrappedEvent + "]";
	}

}
