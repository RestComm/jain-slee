package org.mobicents.slee.resource.sip.wrappers;

import java.util.logging.Logger;

import javax.sip.Dialog;

import org.mobicents.slee.resource.sip.SipActivityHandle;



public abstract class SuperTransactionWrapper implements WrapperSuperInterface{

	
	protected DialogWrapper dialogWrapper=null; 
	protected Object applicationObject = null;
	protected SipActivityHandle sipActivityHandle;
	protected Logger logger=Logger.getLogger(this.getClass().getCanonicalName());
	/**
	 * 
	 * @param d
	 */
	public  boolean updateDialog(DialogWrapper d)
	{
		
		logger.info("000 UPDATE DIALOG ON["+this+"]DD["+this.dialogWrapper+"] D["+d+"]");
		if(this.dialogWrapper==null)
		{
			this.dialogWrapper=d;
			if(!this.getActivityHandle().getID().endsWith("CANCEL"))
				this.dialogWrapper.addOngoingTransaction(this);
			return true;
		}else
		{
			return false;
		}
	}
	
	
	public Object getApplicationData() {
		return this.applicationObject;
	}

	public void setApplicationData(Object arg0) {
		this.applicationObject = arg0;

	}
	
	public Dialog getDialog() {

		return dialogWrapper;
	}
	
	public SipActivityHandle getActivityHandle() {
		return this.sipActivityHandle;
	}


}
