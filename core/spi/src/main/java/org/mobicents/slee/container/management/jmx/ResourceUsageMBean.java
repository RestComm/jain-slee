/**
 * 
 */
package org.mobicents.slee.container.management.jmx;

import javax.management.ObjectName;

/**
 * @author martins
 * 
 */
public interface ResourceUsageMBean extends
		javax.slee.management.ResourceUsageMBean {

	/**
	 * Retrieves the default usage param set.
	 * 
	 * @return
	 */
	public Object getDefaultInstalledUsageParameterSet();

	/**
	 * Retrieves the usage param set for the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public Object getInstalledUsageParameterSet(String name);

	/**
	 * Retrieves the object name of this mbean
	 * 
	 * @return
	 */
	public ObjectName getObjectName();

	/**
	 * Opens the bean if it is closed, does nothing otherwise.
	 */
	public void open();

	/**
	 * Removes the mbean
	 * 
	 */
	public void remove();

}
