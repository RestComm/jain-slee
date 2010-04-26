/**
 * 
 */
package org.mobicents.slee.container.component.profile.query;

/**
 * @author martins
 *
 */
public interface CompareDescriptor {

	/**
	 * 
	 * @return
	 */
	public String getAttributeName();
	
	/**
	 * 
	 * @return
	 */
	public String getCollatorRef();
	
	/**
	 * 
	 * @return
	 */
	public String getOp();
	
	/**
	 * 
	 * @return
	 */
	public String getParameter();
	
	/**
	 * 
	 * @return
	 */
	public String getValue();

}
