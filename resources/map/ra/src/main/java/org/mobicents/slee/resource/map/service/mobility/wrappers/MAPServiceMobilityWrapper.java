package org.mobicents.slee.resource.map.service.mobility.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.wrappers.MAPProviderWrapper;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPServiceMobilityWrapper implements MAPServiceMobility {

	protected MAPServiceMobility wrappedMobility;
	protected MAPProviderWrapper mapProviderWrapper;

	/**
	 * @param mapServiceSupplementary
	 */
	public MAPServiceMobilityWrapper(MAPProviderWrapper mapProviderWrapper, MAPServiceMobility mapServiceSupplementary) {
		this.wrappedMobility = mapServiceSupplementary;
		this.mapProviderWrapper = mapProviderWrapper;
	}

	public void acivate() {
		throw new UnsupportedOperationException();
	}

	public void deactivate() {
		throw new UnsupportedOperationException();
	}

	public MAPProvider getMAPProvider() {
		return this.mapProviderWrapper;
	}

	public boolean isActivated() {
		return this.wrappedMobility.isActivated();
	}

	public ServingCheckData isServingService(MAPApplicationContext mapapplicationcontext) {
		return this.wrappedMobility.isServingService(mapapplicationcontext);
	}

	public void addMAPServiceListener(MAPServiceMobilityListener arg0) {
		throw new UnsupportedOperationException();

	}

	public MAPDialogMobility createNewDialog(MAPApplicationContext mapapplicationcontext, SccpAddress sccpaddress,
			AddressString addressstring, SccpAddress sccpaddress1, AddressString addressstring1) throws MAPException {
		MAPDialogMobility mapDialog = this.wrappedMobility.createNewDialog(mapapplicationcontext, sccpaddress,
				addressstring, sccpaddress1, addressstring1);
		MAPDialogActivityHandle activityHandle = new MAPDialogActivityHandle(mapDialog.getDialogId());
		MAPDialogMobilityWrapper dw = new MAPDialogMobilityWrapper(mapDialog, activityHandle,
				this.mapProviderWrapper.getRa());
		mapDialog.setUserObject(dw);

		try {
			this.mapProviderWrapper.getRa().startSuspendedActivity(dw);
		} catch (Exception e) {
			throw new MAPException(e);
		}

		return dw;
	}

	public void removeMAPServiceListener(MAPServiceMobilityListener arg0) {
		throw new UnsupportedOperationException();
	}

}
