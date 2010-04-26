/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import javax.slee.SbbID;

/**
 * @author martins
 *
 */
public interface GetChildRelationMethodDescriptor {
		
	/**
	 * 
	 * @return
	 */
	public String getChildRelationMethodName();
	
	/**
	 * 
	 * @return
	 */
	public byte getDefaultPriority();
	
	/**
	 * 
	 * @return
	 */
	public SbbID getSbbID();
	
}
