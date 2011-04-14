/**
 * 
 */
package org.mobicents.slee.container.sbb;

import org.mobicents.slee.SbbLocalObjectExt;
import org.mobicents.slee.container.sbbentity.SbbEntity;

/**
 * @author martins
 *
 */
public interface SbbLocalObject extends SbbLocalObjectExt {

	/**
	 * 
	 * @return
	 */
	public SbbEntity getSbbEntity();
}
