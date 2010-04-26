/**
 * 
 */
package org.mobicents.slee.container.component.security;

import java.security.Policy;

/**
 * @author martins
 *
 */
public abstract class PolicyFile extends Policy {

	/**
	 * @param ph
	 * @param b
	 * @return
	 */
	public abstract boolean addPermissionHolder(PermissionHolder ph, boolean b);

	/**
	 * 
	 */
	public abstract void refresh();

	/**
	 * @param ph
	 * @param b
	 * @return
	 */
	public abstract boolean removePermissionHolder(PermissionHolder ph, boolean b);

	/**
	 * @return
	 */
	public abstract String getCodeSources();

	/**
	 * @return
	 */
	public abstract String getPolicyFilesURL();

}
