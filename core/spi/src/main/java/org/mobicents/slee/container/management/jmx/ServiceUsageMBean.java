/**
 * 
 */
package org.mobicents.slee.container.management.jmx;

import javax.management.ObjectName;

/**
 * @author martins
 *
 */
public interface ServiceUsageMBean extends javax.slee.management.ServiceUsageMBean {

	/**
	 * Retrieves the object name of this mbean
	 * 
	 * @return
	 */
	public ObjectName getObjectName();
	
	/**
	 * Closes and unregisters the mbean.
	 * 
	 */
	public void remove();
	
}
