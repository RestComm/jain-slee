/*
* Created on Jul 19, 2004
*
* The Open SLEE Project
*
* The source code contained in this file is in in the public domain.          
* It can be used in any project, or product without prior permission, 	      
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/
package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.util.EnumSet;

import javax.slee.Address;
import javax.slee.EventTypeID;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.component.sbb.InitialEventSelectVariable;

/**
 * 
 * Implements the InitialEventSelector interface
 * 
 * @author F.Moggia
 * @author E. Martins
 * 
 * 
 */
public class InitialEventSelectorImpl implements javax.slee.InitialEventSelector{
    	
	private EventTypeID eventTypeID;
    private String eventName;
    private Object event;
    private ActivityContextHandle ach;
    private Address address;
    private String customName;

    private boolean isSelectMethod = false;
    
    private final EnumSet<InitialEventSelectVariable> iesVariables;
    
    private boolean isInitialEvent= false;
    
    private String selectMethodName;
 
    public InitialEventSelectorImpl(EventTypeID type, Object event, ActivityContextHandle ach,  EnumSet<InitialEventSelectVariable> iesVariables, String methodName, Address address) {
        eventTypeID = type;
        this.event = event;
        this.address = address;
        this.ach = ach;
        
        this.iesVariables = iesVariables;
        
        if (methodName == null){
            isSelectMethod = false;
        } else {
            isSelectMethod = true;
            selectMethodName = methodName;
        }
    }
    
    public String toString() {
        return "InitialEventSelector = {\n eventTypeID " + eventTypeID 
        	+ " eventName " + eventName
        	+ " event " + event 
        	+ " ach " + ach
        	+ " address " + address
        	+ "\n activityContextSelected = "  + isActivityContextSelected()
        	+ "\n isAddressSelected " + isAddressSelected()
        	+ "\n isAddressProfileSelected " + isAddressProfileSelected()
        	+ "\n isEventSelected " + isEventSelected() 
        	+ "\n customName " + customName
        	+ "\n selectMethodName " + selectMethodName
        	+ "\n}";
    }
    
    /**
     * @return Returns the isActivityContextSelected.
     */
    public boolean isActivityContextSelected() {
        return iesVariables.contains(InitialEventSelectVariable.ActivityContext);
    }
    /**
     * @return Returns the isAddressProfileSelected.
     */
    public boolean isAddressProfileSelected() {
    	return iesVariables.contains(InitialEventSelectVariable.AddressProfile);
    }
    /**
     * @return Returns the isAddressSelected.
     */
    public boolean isAddressSelected() {
    	return iesVariables.contains(InitialEventSelectVariable.Address);
    }
    /**
     * @return Returns the isEventTypeSelected.
     */
    public boolean isEventTypeSelected() {
    	return iesVariables.contains(InitialEventSelectVariable.EventType);
    }
    /**
     * @return Returns the eventType.
     */
    public EventTypeID getEventTypeID() {
        return eventTypeID;
    }
    /**
     * @param eventType The eventType to set.
     */
    public void setEventType(EventTypeID eventTypeID) {
        this.eventTypeID = eventTypeID;
    }
    /**
     * @return Returns the isEventSelected.
     */
    public boolean isEventSelected() {
    	return iesVariables.contains(InitialEventSelectVariable.Event);
    }
    /**
     * @param isEventSelected The isEventSelected to set.
     */
    
    /**
     * @param isAddressProfileSelected The isAddressProfileSelected to set.
     */
    public void setAddressProfileSelected(boolean isAddressProfileSelected) {
        if (isAddressProfileSelected) {
        	iesVariables.add(InitialEventSelectVariable.AddressProfile);
        }
        else {
        	iesVariables.remove(InitialEventSelectVariable.AddressProfile);
        }
    }
    /**
     * @param isAddressSelected The isAddressSelected to set.
     */
    public void setAddressSelected(boolean isAddressSelected) {
    	if (isAddressSelected) {
        	iesVariables.add(InitialEventSelectVariable.Address);
        }
        else {
        	iesVariables.remove(InitialEventSelectVariable.Address);
        }
    }
    /**
     * @param isEventTypeSelected The isEventTypeSelected to set.
     */
    public void setEventTypeSelected(boolean isEventTypeSelected) {
    	if (isEventTypeSelected) {
        	iesVariables.add(InitialEventSelectVariable.EventType);
        }
        else {
        	iesVariables.remove(InitialEventSelectVariable.EventType);
        }
    }
    /**
     * @return Returns the isSelectMethod.
     */
    public boolean isSelectMethod() {
        return isSelectMethod;
    }
    /**
     * @param isSelectMethod The isSelectMethod to set.
     */
    public void setSelectMethod(boolean isSelectMethod) {
        this.isSelectMethod = isSelectMethod;
    }
    /**
     * @return Returns the selectMethodName.
     */
    public String getSelectMethodName() {
        return selectMethodName;
    }
    /**
     * @param selectMethodName The selectMethodName to set.
     */
    public void setSelectMethodName(String selectMethodName) {
        this.selectMethodName = selectMethodName;
    }
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#getEventName()
     */
    public String getEventName() {
        return eventName;
    }
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#getEvent()
     */
    public Object getEvent() {
        return event;
    }
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#getActivity()
     */
    public Object getActivity() {
        return ach.getActivityObject();
    }
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#getAddress()
     */
    public Address getAddress() {
        
        return address;
    }
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#setAddress(javax.slee.Address)
     */
    public void setAddress(Address address) {
        
        this.address = address;
    }
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#getCustomName()
     */
    public String getCustomName() {
        
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }
    
    public void setActivityContextSelected(boolean isActivityContextSelected) {
    	if (isActivityContextSelected) {
        	iesVariables.add(InitialEventSelectVariable.ActivityContext);
        }
        else {
        	iesVariables.remove(InitialEventSelectVariable.ActivityContext);
        }
    }
    
    public void setEventSelected(boolean isEventSelected) {
    	if (isEventSelected) {
        	iesVariables.add(InitialEventSelectVariable.Event);
        }
        else {
        	iesVariables.remove(InitialEventSelectVariable.Event);
        }
    }
    
    public boolean isInitialEvent() {
        return isInitialEvent;
    }
    
    public void setInitialEvent(boolean isInitialEvent) {
        this.isInitialEvent = isInitialEvent;
    }
}
