/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.mediacontrol;

import java.rmi.server.UID;

import javax.slee.resource.ActivityHandle;

/**
 * @author baranowb
 *
 */
public class MsActivityHandle implements ActivityHandle {
	
	private final String uid;
	private final int hash;

	

	public MsActivityHandle(MsActivity activity) {
		super();
		this.uid = new UID().toString()+"_"+activity.toString();
		this.hash = createHash();
	}
	/**
	 * @param uid
	 */
	public MsActivityHandle(String uid) {
		super();
		this.uid = uid;
		this.hash = createHash();
	}

	private int createHash()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return  result;
	}
	
	
	public int hashCode() {
		return hash;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true; 
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MsActivityHandle other = (MsActivityHandle) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}
	
	public String toString() {
		return "MsActivityHandle [uid=" + uid + ", hash=" + hash + ", hashCode()=" + hashCode() + "]";
	}
	
	
	
	
}
