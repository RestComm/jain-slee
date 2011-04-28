/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.services.sip.location;

import java.io.Serializable;

/**
 * TODO
 * @author martins
 *
 */
public abstract class RegistrationBinding implements Serializable {
	
	public abstract String getCallId();
    
    public abstract void setCallId(String callId);

    public abstract String getComment();
    
    public abstract void setComment(String comment);
       
    public abstract long getCSeq();
    
    public abstract void setCSeq(long cSq);
    
	public abstract long getExpires();

	public abstract void setExpires(long expires);
    
	public abstract float getQValue();
    
    public abstract void setQValue(float q);

    public abstract long getRegistrationDate();
    
    public abstract void setRegistrationDate(long registrationDate);
    
    public abstract String getContactAddress(); 
	
	public abstract String getSipAddress();
	
    // --- logic methods
    
    /**
     * Returns number of mseconds till this entry expires
     * May be 0 or -ve if already expired
     */
	public long getExpiresDelta() {
        return ((getExpires()-(System.currentTimeMillis()-getRegistrationDate())/1000));  
    }

	public String toString() {
		return "RegistrationBinding[sipAddress=" + getSipAddress() + ",contactAddress=" +getContactAddress()+ ",comment=" + getComment() + ",expires=" +
			getExpires() + ",qValue=" + getQValue() + ",callId=" + getCallId() + ",cSeq=" + getCSeq() + "]";
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getContactAddress() == null) ? 0 : getContactAddress().hashCode());
		result = prime * result
				+ ((getSipAddress() == null) ? 0 : getSipAddress().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			RegistrationBinding other = (RegistrationBinding) obj;
			return this.getSipAddress().equals(other.getSipAddress()) && this.getContactAddress().equals(other.getContactAddress()); 
		}
		else {
			return false;
		}
	}
}
