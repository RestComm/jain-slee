/**
 * 
 */
package org.mobicents.slee.resource.smpp.ra;

/**
 * @author martins
 *
 */
public class SmppActivityImpl {

	/**
	 * 
	 */
	private SmppActivityHandle activityHandle;
	
	/**
	 * @param activityHandle the activityHandle to set
	 */
	public void setActivityHandle(SmppActivityHandle activityHandle) {
		this.activityHandle = activityHandle;
	}
	
	/**
	 * 
	 * @return
	 */
	public SmppActivityHandle getActivityHandle() {
		return activityHandle;
	}

}
