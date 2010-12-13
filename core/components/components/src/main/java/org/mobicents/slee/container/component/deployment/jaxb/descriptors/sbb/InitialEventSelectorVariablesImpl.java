package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables;

/**
 * 
 * @author martins
 * 
 */
public class InitialEventSelectorVariablesImpl implements InitialEventSelectorVariables {

	private boolean activityContextOnlySelected;
	private boolean activityContextSelected;
	private boolean addressProfileSelected;
	private boolean addressSelected;
	private boolean eventSelected;
	private boolean eventTypeSelected;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public InitialEventSelectorVariablesImpl clone() {
		final InitialEventSelectorVariablesImpl clone = new InitialEventSelectorVariablesImpl();
		clone.activityContextSelected = this.activityContextSelected;
		clone.activityContextOnlySelected = this.activityContextOnlySelected;
		clone.addressProfileSelected = this.addressProfileSelected;
		clone.addressSelected = this.addressSelected;
		clone.eventTypeSelected = this.eventTypeSelected;
		clone.eventSelected = this.eventSelected;
		return clone;
	}

	

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#isActivityContextOnlySelected()
	 */
	public boolean isActivityContextOnlySelected() {
		return activityContextOnlySelected;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#isActivityContextSelected()
	 */
	public boolean isActivityContextSelected() {
		return activityContextSelected;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#isAddressProfileSelected()
	 */
	public boolean isAddressProfileSelected() {
		return addressProfileSelected;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#isAddressSelected()
	 */
	public boolean isAddressSelected() {
		return addressSelected;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#isEventSelected()
	 */
	public boolean isEventSelected() {
		return eventSelected;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#isEventTypeSelected()
	 */
	public boolean isEventTypeSelected() {
		return eventTypeSelected;
	}

	/*
	 * 
	 */
	private void resetActivityContextOnlySelected() {
		activityContextOnlySelected = activityContextSelected && !addressProfileSelected && !addressSelected && !eventSelected && !eventTypeSelected;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#setActivityContextSelected(boolean)
	 */
	public void setActivityContextSelected(boolean activityContextSelected) {
		this.activityContextSelected = activityContextSelected;
		resetActivityContextOnlySelected();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#setAddressProfileSelected(boolean)
	 */
	public void setAddressProfileSelected(boolean addressProfileSelected) {
		this.addressProfileSelected = addressProfileSelected;
		resetActivityContextOnlySelected();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#setAddressSelected(boolean)
	 */
	public void setAddressSelected(boolean addressSelected) {
		this.addressSelected = addressSelected;
		resetActivityContextOnlySelected();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#setEventSelected(boolean)
	 */
	public void setEventSelected(boolean eventSelected) {
		this.eventSelected = eventSelected;
		resetActivityContextOnlySelected();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables#setEventTypeSelected(boolean)
	 */
	public void setEventTypeSelected(boolean eventTypeSelected) {
		this.eventTypeSelected = eventTypeSelected;
		resetActivityContextOnlySelected();
	}

	/**
	 * 
	 * @param variable
	 */
	protected void setVariable(InitialEventSelectVariable variable) {
		switch (variable) {
		case ActivityContext:
			setActivityContextSelected(true);
			break;
		case Address:
			setAddressSelected(true);
			break;
		case AddressProfile:
			setAddressProfileSelected(true);
			break;
		case Event:
			setEventSelected(true);
			break;
		case EventType:
			setEventTypeSelected(true);
			break;
		default:
			throw new IllegalArgumentException("unknown variable type");
		}
	}

}
