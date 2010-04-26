/**
 * 
 */
package org.mobicents.slee.container.component.profile;

/**
 * @author martins
 *
 */
public interface ProfileIndexDescriptor {

	/**
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 
	 * @return
	 */
	public boolean getUnique();

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj);
}
