/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import javax.slee.SbbID;

/**
 * @author martins
 * 
 */
public interface SbbRefDescriptor {

	/**
	 * 
	 * @return
	 */
	public SbbID getComponentID();

	/**
	 * 
	 * @return
	 */
	public String getSbbAlias();

}
