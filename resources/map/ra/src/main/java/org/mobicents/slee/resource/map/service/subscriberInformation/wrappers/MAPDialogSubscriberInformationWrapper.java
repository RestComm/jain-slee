package org.mobicents.slee.resource.map.service.subscriberInformation.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MAPDialogSubscriberInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.RequestedInfo;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.MAPResourceAdaptor;
import org.mobicents.slee.resource.map.wrappers.MAPDialogWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPDialogSubscriberInformationWrapper extends MAPDialogWrapper<MAPDialogSubscriberInformation> implements
		MAPDialogSubscriberInformation {

	public MAPDialogSubscriberInformationWrapper(MAPDialogSubscriberInformation wrappedDialog,
			MAPDialogActivityHandle activityHandle, MAPResourceAdaptor ra) {
		super(wrappedDialog, activityHandle, ra);
	}

	@Override
	public long addAnyTimeInterrogationRequest(SubscriberIdentity arg0, RequestedInfo arg1, ISDNAddressString arg2,
			MAPExtensionContainer arg3) throws MAPException {
		return this.wrappedDialog.addAnyTimeInterrogationRequest(arg0, arg1, arg2, arg3);
	}

	@Override
	public long addAnyTimeInterrogationRequest(long arg0, SubscriberIdentity arg1, RequestedInfo arg2,
			ISDNAddressString arg3, MAPExtensionContainer arg4) throws MAPException {
		return this.wrappedDialog.addAnyTimeInterrogationRequest(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public long addAnyTimeInterrogationResponse(long arg0) throws MAPException {
		return this.wrappedDialog.addAnyTimeInterrogationResponse(arg0);
	}

	@Override
	public MAPDialogSubscriberInformation getWrappedDialog() {
		return this.wrappedDialog;
	}

	@Override
	public String toString() {
		return "MAPDialogSubscriberInformationWrapper [wrappedDialog=" + wrappedDialog + "]";
	}

}
