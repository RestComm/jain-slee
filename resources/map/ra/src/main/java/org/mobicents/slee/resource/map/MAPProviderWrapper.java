package org.mobicents.slee.resource.map;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPProviderWrapper implements MAPProvider {

	private org.mobicents.protocols.ss7.map.api.MAPProvider mapProvider = null;
	private MAPResourceAdaptor mapRsrcAdap = null;

	protected MAPProviderWrapper(org.mobicents.protocols.ss7.map.api.MAPProvider mapProvider,
			MAPResourceAdaptor mapRsrcAdap) {
		this.mapProvider = mapProvider;
		this.mapRsrcAdap = mapRsrcAdap;
	}

	public void addMAPDialogListener(MAPDialogListener arg0) {
		throw new UnsupportedOperationException();
	}

	public void addMAPServiceListener(MAPServiceListener arg0) {
		throw new UnsupportedOperationException();
	}

	public MAPDialog createNewDialog(MAPApplicationContext appCntx, SccpAddress destAddress,
			AddressString destReference, SccpAddress origAddress, AddressString origReference) throws MAPException {
		MAPDialog mapDialog = mapProvider.createNewDialog(appCntx, destAddress, destReference, origAddress,
				origReference);
		return new MAPDialogWrapper(this.mapRsrcAdap, mapDialog);
	}

	public MapServiceFactory getMapServiceFactory() {
		return mapProvider.getMapServiceFactory();
	}

	public void removeMAPDialogListener(MAPDialogListener arg0) {
		throw new UnsupportedOperationException();
	}

	public void removeMAPServiceListener(MAPServiceListener arg0) {
		throw new UnsupportedOperationException();
	}

	public MAPDialog getMAPDialog(Long dialogId) {
		return mapProvider.getMAPDialog(dialogId);
	}

}
