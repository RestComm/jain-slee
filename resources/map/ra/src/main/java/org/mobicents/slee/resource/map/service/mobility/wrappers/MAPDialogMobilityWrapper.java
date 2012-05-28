package org.mobicents.slee.resource.map.service.mobility.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.MAPResourceAdaptor;
import org.mobicents.slee.resource.map.wrappers.MAPDialogWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPDialogMobilityWrapper extends MAPDialogWrapper<MAPDialogMobility> implements MAPDialogMobility {

	public MAPDialogMobilityWrapper(MAPDialogMobility wrappedDialog, MAPDialogActivityHandle activityHandle,
			MAPResourceAdaptor ra) {
		super(wrappedDialog, activityHandle, ra);
	}

	@Override
	public Long addSendAuthenticationInfoRequest(long mapProtocolVersion, IMSI imsi, int numberOfRequestedVectors,
			boolean segmentationProhibited, boolean immediateResponsePreferred,
			ReSynchronisationInfo reSynchronisationInfo, MAPExtensionContainer extensionContainer,
			RequestingNodeType requestingNodeType, PlmnId requestingPlmnId, Integer numberOfRequestedAdditionalVectors,
			boolean additionalVectorsAreForEPS) throws MAPException {
		return this.wrappedDialog.addSendAuthenticationInfoRequest(mapProtocolVersion, imsi, numberOfRequestedVectors,
				segmentationProhibited, immediateResponsePreferred, reSynchronisationInfo, extensionContainer,
				requestingNodeType, requestingPlmnId, numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS);
	}

	@Override
	public Long addSendAuthenticationInfoRequest(int customInvokeTimeout, long mapProtocolVersion, IMSI imsi,
			int numberOfRequestedVectors, boolean segmentationProhibited, boolean immediateResponsePreferred,
			ReSynchronisationInfo reSynchronisationInfo, MAPExtensionContainer extensionContainer,
			RequestingNodeType requestingNodeType, PlmnId requestingPlmnId, Integer numberOfRequestedAdditionalVectors,
			boolean additionalVectorsAreForEPS) throws MAPException {
		return this.wrappedDialog.addSendAuthenticationInfoRequest(customInvokeTimeout, mapProtocolVersion, imsi,
				numberOfRequestedVectors, segmentationProhibited, immediateResponsePreferred, reSynchronisationInfo,
				extensionContainer, requestingNodeType, requestingPlmnId, numberOfRequestedAdditionalVectors,
				additionalVectorsAreForEPS);
	}

	@Override
	public void addSendAuthenticationInfoResponse(long invokeId, long mapProtocolVersion,
			AuthenticationSetList authenticationSetList, MAPExtensionContainer extensionContainer,
			EpsAuthenticationSetList epsAuthenticationSetList) throws MAPException {
		this.addSendAuthenticationInfoResponse(invokeId, mapProtocolVersion, authenticationSetList, extensionContainer,
				epsAuthenticationSetList);
	}

	@Override
	public MAPDialogMobility getWrappedDialog() {
		return this.wrappedDialog;
	}

}
