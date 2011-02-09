package org.mobicents.slee.container.component.sbb;

import javax.slee.InitialEventSelector;

/**
 * The variables for an {@link InitialEventSelector}.
 * 
 * @author martins
 *
 */
public interface InitialEventSelectorVariables {

	/**
	 * 
	 * @return
	 */
	public InitialEventSelectorVariables clone();

	/**
	 * 
	 * @return
	 */
	public boolean isActivityContextOnlySelected();

	/**
	 * 
	 * @return
	 */
	public boolean isActivityContextSelected();

	/**
	 * 
	 * @return
	 */
	public boolean isAddressProfileSelected();

	/**
	 * 
	 * @return
	 */
	public boolean isAddressSelected();

	/**
	 * 
	 * @return
	 */
	public boolean isEventSelected();

	/**
	 * 
	 * @return
	 */
	public boolean isEventTypeSelected();

	/**
	 * 
	 * @param activityContextSelected
	 */
	public void setActivityContextSelected(boolean activityContextSelected);

	/**
	 * 
	 * @param addressProfileSelected
	 */
	public void setAddressProfileSelected(boolean addressProfileSelected);

	/**
	 * 
	 * @param addressSelected
	 */
	public void setAddressSelected(boolean addressSelected);

	/**
	 * 
	 * @param eventSelected
	 */
	public void setEventSelected(boolean eventSelected);

	/**
	 * 
	 * @param eventTypeSelected
	 */
	public void setEventTypeSelected(boolean eventTypeSelected);

}
