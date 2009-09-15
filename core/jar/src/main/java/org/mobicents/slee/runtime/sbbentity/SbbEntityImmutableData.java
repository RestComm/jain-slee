/**
 * 
 */
package org.mobicents.slee.runtime.sbbentity;

import java.io.Serializable;

import javax.slee.SbbID;
import javax.slee.ServiceID;

/**
 * @author martins
 *
 */
public class SbbEntityImmutableData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final SbbID sbbID;
	private final ServiceID serviceID;
	private final String parentSbbEntityID;
	private final String parentChildRelationName;
	private final String rootSbbEntityID;
	private final String convergenceName;
	
	/**
	 * @param sbbID
	 * @param serviceID
	 * @param parentSbbEntityID
	 * @param parentChildRelationName
	 * @param rootSbbEntityID
	 * @param convergenceName
	 */
	public SbbEntityImmutableData(SbbID sbbID,
			ServiceID serviceID, String parentSbbEntityID,
			String parentChildRelationName, String rootSbbEntityID,
			String convergenceName) {
		this.sbbID = sbbID;
		this.serviceID = serviceID;
		this.parentSbbEntityID = parentSbbEntityID;
		this.parentChildRelationName = parentChildRelationName;
		this.rootSbbEntityID = rootSbbEntityID;
		this.convergenceName = convergenceName;
	}

	/**
	 *  
	 * @return the sbbID
	 */
	public SbbID getSbbID() {
		return sbbID;
	}

	/**
	 *  
	 * @return the serviceID
	 */
	public ServiceID getServiceID() {
		return serviceID;
	}

	/**
	 *  
	 * @return the parentSbbEntityID
	 */
	public String getParentSbbEntityID() {
		return parentSbbEntityID;
	}

	/**
	 *  
	 * @return the parentChildRelationName
	 */
	public String getParentChildRelationName() {
		return parentChildRelationName;
	}

	/**
	 *  
	 * @return the rootSbbEntityID
	 */
	public String getRootSbbEntityID() {
		return rootSbbEntityID;
	}

	/**
	 *  
	 * @return the convergenceName
	 */
	public String getConvergenceName() {
		return convergenceName;
	}
	
	
}
