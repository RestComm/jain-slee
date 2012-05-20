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

	@Override
	public void abort(MAPUserAbortChoice arg0) throws MAPException {
		this.wrappedDialog.abort(arg0);
	}

	@Override
	public void addEricssonData(IMSI arg0, AddressString arg1) {
		this.wrappedDialog.addEricssonData(arg0, arg1);
	}

	@Override
	public boolean cancelInvocation(Long arg0) throws MAPException {
		return this.wrappedDialog.cancelInvocation(arg0);
	}

	@Override
	public void close(boolean arg0) throws MAPException {
		this.wrappedDialog.close(arg0);
	}

	@Override
	public MAPApplicationContext getApplicationContext() {
		return this.wrappedDialog.getApplicationContext();
	}

	@Override
	public Long getDialogId() {
		return this.wrappedDialog.getDialogId();
	}

	@Override
	public int getMaxUserDataLength() {
		return this.wrappedDialog.getMaxUserDataLength();
	}

	@Override
	public int getMessageUserDataLengthOnClose(boolean arg0) throws MAPException {
		return this.wrappedDialog.getMessageUserDataLengthOnClose(arg0);
	}

	@Override
	public int getMessageUserDataLengthOnSend() throws MAPException {
		return this.wrappedDialog.getMessageUserDataLengthOnSend();
	}

	@Override
	public MAPServiceBase getService() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MAPDialogState getState() {
		return this.wrappedDialog.getState();
	}

	@Override
	public Object getUserObject() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void keepAlive() {
		this.wrappedDialog.keepAlive();
	}

	@Override
	public void refuse(Reason arg0) throws MAPException {
		this.wrappedDialog.refuse(arg0);
	}

	@Override
	public void release() {
		this.wrappedDialog.release();
	}

	@Override
	public void resetInvokeTimer(Long arg0) throws MAPException {
		this.wrappedDialog.resetInvokeTimer(arg0);
	}

	@Override
	public void send() throws MAPException {
		this.wrappedDialog.send();
	}

	@Override
	public void sendErrorComponent(Long arg0, MAPErrorMessage arg1) throws MAPException {
		this.wrappedDialog.sendErrorComponent(arg0, arg1);
	}

	@Override
	public void sendInvokeComponent(Invoke arg0) throws MAPException {
		this.wrappedDialog.sendInvokeComponent(arg0);
	}

	@Override
	public void sendRejectComponent(Long arg0, Problem arg1) throws MAPException {
		this.wrappedDialog.sendRejectComponent(arg0, arg1);
	}

	@Override
	public void sendReturnResultComponent(ReturnResult arg0) throws MAPException {
		this.wrappedDialog.sendReturnResultComponent(arg0);
	}

	@Override
	public void sendReturnResultLastComponent(ReturnResultLast arg0) throws MAPException {
		this.wrappedDialog.sendReturnResultLastComponent(arg0);
	}

	@Override
	public void setExtentionContainer(MAPExtensionContainer arg0) {
		this.wrappedDialog.setExtentionContainer(arg0);
	}

	@Override
	public void setUserObject(Object arg0) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setReturnMessageOnError(boolean val){
		this.wrappedDialog.setReturnMessageOnError(val);
	}

	@Override
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
