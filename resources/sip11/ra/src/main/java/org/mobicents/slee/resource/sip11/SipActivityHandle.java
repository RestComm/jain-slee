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
package org.mobicents.slee.resource.sip11;

import javax.slee.resource.ActivityHandle;



/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class SipActivityHandle implements ActivityHandle,
		Comparable<SipActivityHandle> {
	
	private String id;
	
	public SipActivityHandle(String id) {
		if (id == null) {
			throw new NullPointerException("null sip activity handle id");
		}
		this.id = id;
	}

	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return this.toString().equals(obj.toString());
		} else
			return false;
	}

	public int hashCode() {
		return id.hashCode();
	}

	public String toString() {
		return this.id;
	}

	public int compareTo(SipActivityHandle o) {
		if (o == null)
			return 1;
		else
			return this.id.compareTo(o.id);
	}

	public String getID() {
		return id;
	}

}
