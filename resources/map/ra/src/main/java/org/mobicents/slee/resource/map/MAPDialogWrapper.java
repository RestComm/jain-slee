package org.mobicents.slee.resource.map;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPDialogWrapper implements MAPDialog {
	
	private MAPResourceAdaptor mapRa = null;
	private MAPDialog mapDialog = null;
	
	protected MAPDialogWrapper(MAPResourceAdaptor mapRa, MAPDialog mapDialog){
		this.mapRa = mapRa;
		this.mapDialog = mapDialog;
	}

	public void abort(MAPUserAbortChoice arg0) throws MAPException {
		this.mapRa.endActivity(this.mapDialog);
		this.mapDialog.abort(arg0);
		
	}

	public void addProcessUnstructuredSSRequest(byte arg0, USSDString arg1, AddressString arg2) throws MAPException {
		this.mapDialog.addProcessUnstructuredSSRequest(arg0, arg1, arg2);
		
	}

	public void addProcessUnstructuredSSResponse(long arg0, boolean arg1, byte arg2, USSDString arg3)
			throws MAPException {
		this.mapDialog.addProcessUnstructuredSSResponse(arg0, arg1, arg2, arg3);		
	}

	public void addUnstructuredSSRequest(byte arg0, USSDString arg1) throws MAPException {
		this.mapDialog.addUnstructuredSSRequest(arg0, arg1);		
	}

	public void addUnstructuredSSResponse(long arg0, boolean arg1, byte arg2, USSDString arg3) throws MAPException {
		this.mapDialog.addUnstructuredSSResponse(arg0, arg1, arg2, arg3);		
	}

	public void close(boolean arg0) throws MAPException {
		this.mapRa.endActivity(this.mapDialog);		
		this.mapDialog.close(arg0);
	}

	public Long getDialogId() {
		return this.mapDialog.getDialogId();
	}

	public void send() throws MAPException {
		this.mapDialog.send();		
	}

}
