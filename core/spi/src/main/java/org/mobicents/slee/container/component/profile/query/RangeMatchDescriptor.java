/**
 * 
 */
package org.mobicents.slee.container.component.profile.query;

/**
 * @author martins
 *
 */
public interface RangeMatchDescriptor {

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
	public String getFromParameter();

	/**
	 * 
	 * @return
	 */
	public String getFromValue();

	/**
	 * 
	 * @return
	 */
	public String getToParameter();

	/**
	 * 
	 * @return
	 */
	public String getToValue();

}
