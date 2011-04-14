/**
 * 
 */
package org.mobicents.slee.container.sbbentity;

import java.util.Set;

import org.mobicents.slee.ChildRelationExt;

/**
 * @author martins
 *
 */
public interface ChildRelation extends ChildRelationExt {

	/**
	 * 
	 * @return
	 */
	public Set<SbbEntityID> getSbbEntitySet();
	
}
