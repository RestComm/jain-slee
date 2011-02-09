/**
 * 
 */
package org.mobicents.slee.runtime.usage.local;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.mobicents.slee.runtime.usage.UsageParameter;

/**
 * @author martins
 *
 */
public class UsageParamSetLocalData {

	private final ConcurrentHashMap<String, UsageParameter> usageParams = new ConcurrentHashMap<String, UsageParameter>();
	
	private final String usageParamSetName;

	/**
	 * @param usageParamSetName
	 */
	public UsageParamSetLocalData(String usageParamSetName) {
		this.usageParamSetName = usageParamSetName;
	}

	/**
	 * Retrieves 
	 * @return the usageParams
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(usageParams.keySet());
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public UsageParameter getUsageParam(String name) {
		return usageParams.get(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param usageParam
	 */
	public void setUsageParam(String name, UsageParameter usageParam) {
		usageParams.put(name, usageParam);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return usageParamSetName.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((UsageParamSetLocalData)obj).usageParamSetName.equals(this.usageParamSetName);
		}
		else {
			return false;
		}
	}		
}
