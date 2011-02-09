/**
 * 
 */
package org.mobicents.slee.container.component.profile.query;

/**
 * @author martins
 *
 */
public interface CollatorDescriptor {

	/**
	 * 
	 * @return
	 */
	public String getCollatorAlias();

	/**
	 * 
	 * @return
	 */
	public String getDecomposition();

	/**
	 * 
	 * @return
	 */
	public String getLocaleCountry();

	/**
	 * 
	 * @return
	 */
	public String getLocaleLanguage();

	/**
	 * 
	 * @return
	 */
	public String getLocaleVariant();

	/**
	 * 
	 * @return
	 */
	public String getStrength();

}
