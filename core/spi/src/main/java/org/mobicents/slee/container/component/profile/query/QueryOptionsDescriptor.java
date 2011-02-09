/**
 * 
 */
package org.mobicents.slee.container.component.profile.query;

/**
 * @author martins
 *
 */
public interface QueryOptionsDescriptor {

	/**
	 * 
	 * @return
	 */
	public long getMaxMatches();

	/**
	 * 
	 * @return
	 */
	public boolean isReadOnly();
}
