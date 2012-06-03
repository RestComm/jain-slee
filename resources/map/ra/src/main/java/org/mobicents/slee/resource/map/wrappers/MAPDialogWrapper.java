package org.mobicents.slee.resource.map.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPServiceBase;
import org.mobicents.protocols.ss7.map.api.dialog.MAPDialogState;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.MAPResourceAdaptor;

/**
 * 
 * @author amit bhayani
 *
 */
public abstract class MAPDialogWrapper<T extends MAPDialog> implements MAPDialog {
	
	protected MAPDialogActivityHandle activityHandle;
	protected final MAPResourceAdaptor ra;
	protected T wrappedDialog;

	public MAPDialogWrapper(T wrappedDialog, MAPDialogActivityHandle activityHandle, MAPResourceAdaptor ra) {
		this.wrappedDialog = wrappedDialog;
		this.activityHandle = activityHandle;
		this.activityHandle.setActivity(this);
		this.ra = ra;
	}
	
    public SccpAddress getLocalAddress(){
    	return this.wrappedDialog.getLocalAddress();
    }
    
    public SccpAddress getRemoteAddress(){
    	return this.wrappedDialog.getRemoteAddress();
    }

	public void abort(MAPUserAbortChoice arg0) throws MAPException {
		this.wrappedDialog.abort(arg0);
	}

	public void addEricssonData(IMSI arg0, AddressString arg1) {
		this.wrappedDialog.addEricssonData(arg0, arg1);
	}

	public boolean cancelInvocation(Long arg0) throws MAPException {
		return this.wrappedDialog.cancelInvocation(arg0);
	}

	public void close(boolean arg0) throws MAPException {
		this.wrappedDialog.close(arg0);
	}

	public MAPApplicationContext getApplicationContext() {
		return this.wrappedDialog.getApplicationContext();
	}

	public Long getDialogId() {
		return this.wrappedDialog.getDialogId();
	}

	public int getMaxUserDataLength() {
		return this.wrappedDialog.getMaxUserDataLength();
	}

	public int getMessageUserDataLengthOnClose(boolean arg0) throws MAPException {
		return this.wrappedDialog.getMessageUserDataLengthOnClose(arg0);
	}

	public int getMessageUserDataLengthOnSend() throws MAPException {
		return this.wrappedDialog.getMessageUserDataLengthOnSend();
	}

	public MAPServiceBase getService() {
		throw new UnsupportedOperationException();
	}

	public MAPDialogState getState() {
		return this.wrappedDialog.getState();
	}

	public Object getUserObject() {
		throw new UnsupportedOperationException();
	}

	public void keepAlive() {
		this.wrappedDialog.keepAlive();
	}

	public void refuse(Reason arg0) throws MAPException {
		this.wrappedDialog.refuse(arg0);
	}

	public void release() {
		this.wrappedDialog.release();
	}

	public void resetInvokeTimer(Long arg0) throws MAPException {
		this.wrappedDialog.resetInvokeTimer(arg0);
	}

	public void send() throws MAPException {
		this.wrappedDialog.send();
	}

	public void sendErrorComponent(Long arg0, MAPErrorMessage arg1) throws MAPException {
		this.wrappedDialog.sendErrorComponent(arg0, arg1);
	}

	public void sendInvokeComponent(Invoke arg0) throws MAPException {
		this.wrappedDialog.sendInvokeComponent(arg0);
	}

	public void sendRejectComponent(Long arg0, Problem arg1) throws MAPException {
		this.wrappedDialog.sendRejectComponent(arg0, arg1);
	}

	public void sendReturnResultComponent(ReturnResult arg0) throws MAPException {
		this.wrappedDialog.sendReturnResultComponent(arg0);
	}

	public void sendReturnResultLastComponent(ReturnResultLast arg0) throws MAPException {
		this.wrappedDialog.sendReturnResultLastComponent(arg0);
	}

	public void setExtentionContainer(MAPExtensionContainer arg0) {
		this.wrappedDialog.setExtentionContainer(arg0);
	}

	public void setUserObject(Object arg0) {
		throw new UnsupportedOperationException();
	}
	
	public void setReturnMessageOnError(boolean val){
		this.wrappedDialog.setReturnMessageOnError(val);
	}

	public boolean getReturnMessageOnError(){
		return this.wrappedDialog.getReturnMessageOnError();
	}

	public MAPDialogActivityHandle getActivityHandle() {
		return activityHandle;
	}
	
	public void clear() {
		//TODO Any more cleaning here?
		if (this.activityHandle != null) {
			this.activityHandle.setActivity(null);
			this.activityHandle = null;
		}
		
		if(this.wrappedDialog != null){
			this.wrappedDialog.setUserObject(null);
			this.wrappedDialog = null;
		}
	}

	public MAPResourceAdaptor getRa() {
		return ra;
	}

	
	public abstract T getWrappedDialog();
	
}
