package org.mobicents.slee.training.example9.profile;

/**
 * EventControlProfileCMP would be used to for all the CMP's of profile getter
 * and setter
 * 
 * @author MoBitE info@mobite.co.in
 * 
 */
public interface EventControlProfileCMP {

	public abstract void setActivityId(String value);

	public abstract String getActivityId();

	public abstract void setInit(boolean value);

	public abstract boolean getInit();

	public abstract void setAny(boolean value);

	public abstract boolean getAny();

}
