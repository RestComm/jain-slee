/*
 * Created on Mar 7, 2005
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.resource.sip;

import javax.slee.resource.ActivityHandle;



/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class SipActivityHandle implements ActivityHandle,
		Comparable<SipActivityHandle> {
	private boolean isDialog = false;
	String transactionId;

	public SipActivityHandle(String transactionId) {
		this.transactionId = transactionId.intern();
	}

	public SipActivityHandle(String transactionId, boolean isDialog) {
		this.transactionId = transactionId.intern();
		this.isDialog = true;
	}

	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return this.toString().equals(obj.toString());
		} else
			return false;
	}

	public int hashCode() {
		if (!this.isDialog)
			return transactionId.hashCode();
		else
			return super.hashCode();
	}

	public String toString() {
		return this.transactionId;
	}

	public int compareTo(SipActivityHandle o) {

		if (o == null)
			return 1;
		else
			return this.transactionId.compareTo(o.transactionId);

	}

	public void update(String updateID) {

		this.transactionId = updateID.intern();

	}

	public String getID() {
		return transactionId;
	}

	public boolean isDialogHandle() {
		return this.isDialog;
	}

}
