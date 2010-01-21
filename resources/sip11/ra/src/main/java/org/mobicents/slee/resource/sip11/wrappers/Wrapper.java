package org.mobicents.slee.resource.sip11.wrappers;

import java.io.Serializable;

import javax.sip.Dialog;
import javax.slee.Address;

import org.mobicents.slee.resource.sip11.SipActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

public abstract class Wrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected SipActivityHandle activityHandle;
	protected transient SipResourceAdaptor ra;
	
	protected boolean ending;
	
	/**
	 * 
	 * @param activityHandle
	 */
	public Wrapper(SipActivityHandle activityHandle) {
		setActivityHandle(activityHandle);
	}

	/**
	 * Sets the resource adaptor object, which owns the wrapper.
	 * @param ra
	 */
	public void setResourceAdaptor(SipResourceAdaptor ra) {
		this.ra = ra;
	}
	
	/**
	 * Indicates if the wrapper is an activity.
	 * @return
	 */
	public abstract boolean isActivity();
	
	/**
	 * Indicates if the wrapper is a {@link Dialog}
	 * @return
	 */
	public abstract boolean isDialog();

	/**
	 * Indicates if the wrapper is an ack dummy transaction.
	 * @return
	 */
	public abstract boolean isAckTransaction();
	
	/**
	 * Retrieves the handle associated with the activity.
	 * @return
	 */
	public SipActivityHandle getActivityHandle() {
		return activityHandle;
	}
	
	private void setActivityHandle(SipActivityHandle activityHandle) {
		this.activityHandle = activityHandle;
		activityHandle.setActivity(this);
	}
	
	/**
	 * Retrieves the slee {@link Address} where events on this resource are fired.
	 * @return
	 */
	public abstract Address getEventFiringAddress();
	
	/**
	 * In case the wrapper is an activity, it indicates if it is ending, otherwise it doesn't have a meaning.
	 * @return
	 */
	public boolean isEnding() {
		return ending;
	}
	
	/**
	 * Indicates this activity is ending.
	 */
	public void ending() {
		this.ending = true;
	}	
	
	/**
	 * Operation forbidden in the sip interfaces implemented by concrete wrappers.
	 * @return
	 */
	public Object getApplicationData() {
		throw new SecurityException();
	}

	/**
	 * Operation forbidden in the sip interfaces implemented by concrete wrappers.
	 * @param arg0
	 */
	public void setApplicationData(Object arg0) {
		throw new SecurityException();
	}
	
	public void clear() {
		activityHandle.setActivity(null);
		activityHandle = null;
		this.ra = null;
	}
	
}
