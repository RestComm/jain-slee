package org.mobicents.slee.runtime.sbbentity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.slee.ServiceID;

import org.mobicents.slee.container.sbbentity.SbbEntityID;

public class NonRootSbbEntityID implements SbbEntityID {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SbbEntityID parentSbbEntityID;
	private String parentChildRelation;
	private String childID;
	
	/**
	 * not to be used, needed due to externalizable
	 */
	public NonRootSbbEntityID() {
		
	}
	
	public NonRootSbbEntityID(SbbEntityID parentSbbEntityID, String parentChildRelation, String childID) {
		this.parentSbbEntityID = parentSbbEntityID;
		this.parentChildRelation = parentChildRelation;
		this.childID = childID;
	}
	
	public String getChildID() {
		return childID;
	}
	
	@Override
	public SbbEntityID getParentSBBEntityID() {
		return parentSbbEntityID;
	}

	@Override
	public String getParentChildRelation() {
		return parentChildRelation;
	}

	@Override
	public ServiceID getServiceID() {
		return parentSbbEntityID.getServiceID();
	}

	@Override
	public String getServiceConvergenceName() {
		return parentSbbEntityID.getServiceConvergenceName();
	}

	@Override
	public boolean isRootSbbEntity() {
		return false;
	}

	@Override
	public SbbEntityID getRootSBBEntityID() {
		return parentSbbEntityID.getRootSBBEntityID();
	}
	
	@Override
	public int hashCode() {		
		int result = childID.hashCode();
		//result = result*31 + parentChildRelation.hashCode();
		//result = result*31 + parentSbbEntityID.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NonRootSbbEntityID other = (NonRootSbbEntityID) obj;
		if (!childID.equals(other.childID))
			return false;
		/*if (!parentChildRelation.equals(other.parentChildRelation))
			return false;
		if (!parentSbbEntityID.equals(other.parentSbbEntityID))
			return false;*/
		return true;
	}

	private String toString = null;
	
	@Override
	public String toString() {
		if (toString == null) {
			toString = new StringBuilder(parentSbbEntityID.toString()).append("/").append(parentChildRelation).append("/").append(childID).toString();
		}
		return toString;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		parentSbbEntityID = (SbbEntityID) in.readObject();
		parentChildRelation = in.readUTF();
		childID = in.readUTF();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(parentSbbEntityID);
		out.writeUTF(parentChildRelation);
		out.writeUTF(childID);
	}
}
