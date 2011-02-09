/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

/**
 * @author martins
 *
 */
public interface EjbRefDescriptor {

	/**
	 * 
	 * @return
	 */
	public String getEjbLink();

	/**
	 * 
	 * @return
	 */
	public String getEjbRefName();

	/**
	 * 
	 * @return
	 */
	public String getEjbRefType();

	/**
	 * 
	 * @return
	 */
	public String getHome();

	/**
	 * 
	 * @return
	 */
	public String getRemote();

}
