/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import java.util.List;
import java.util.Map;

/**
 * @author martins
 * 
 */
public interface SbbAbstractClassDescriptor {

	/**
	 * Retrieves the Map between child relation method names and
	 * {@link MGetChildRelationMethod}s
	 * 
	 * @return
	 */
	public Map<String, GetChildRelationMethodDescriptor> getChildRelationMethods();

	/**
	 * 
	 * @return
	 */
	public List<CMPFieldDescriptor> getCmpFields();

	/**
	 * Retrieves the map between profile CMP method names and
	 * {@link MGetProfileCMPMethod}s
	 * 
	 * @return
	 */
	public Map<String, GetProfileCMPMethodDescriptor> getProfileCMPMethods();

	/**
	 * 
	 * @return
	 */
	public String getSbbAbstractClassName();

	/**
	 * 
	 * @return
	 */
	public boolean isReentrant();
}
