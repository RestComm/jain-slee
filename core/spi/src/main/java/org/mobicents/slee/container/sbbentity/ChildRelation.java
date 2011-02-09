/**
 * 
 */
package org.mobicents.slee.container.sbbentity;

import java.util.Set;

/**
 * @author martins
 *
 */
public interface ChildRelation extends javax.slee.ChildRelation {

	/**
	 * 
	 * @return
	 */
	public Set<SbbEntityID> getSbbEntitySet();
	
}
