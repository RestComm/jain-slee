/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import javax.slee.SbbID;

/**
 * @author martins
 *
 */
public interface CMPFieldDescriptor {

	/**
	 * Retrieves the cmp field name
	 * @return
	 */
	public String getCmpFieldName();

	/**
	 * Retrieves the id of the sbb referenced through the alias
	 * @return
	 */
	public SbbID getSbbRef();

}
