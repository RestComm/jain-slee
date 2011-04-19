/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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

import javax.slee.Address;

import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables;
import org.mobicents.slee.container.event.EventContext;

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
    	
    private final EventContext eventContext;
    private final EventEntryDescriptor eventEntryDescriptor;
    private final InitialEventSelectorVariables iesVariables;
    
    private String customName = null;
    private Address address = null;
    private boolean isInitialEvent = true;
    
    /**
     * 
     * @param eventContext
     * @param eventEntryDescriptor
     */
    public InitialEventSelectorImpl(EventContext eventContext,
			EventEntryDescriptor eventEntryDescriptor) {
		this.eventContext = eventContext;
		this.eventEntryDescriptor = eventEntryDescriptor;
		this.address = eventContext.getAddress();
		this.iesVariables = eventEntryDescriptor.getInitialEventSelectVariables().clone();
	}

    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#getActivity()
     */
    public Object getActivity() {
        return eventContext.getActivityContextHandle().getActivityObject();
    }
	
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#getAddress()
     */
    public Address getAddress() {        
        return address;
    }
    
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#getCustomName()
     */
    public String getCustomName() {
        return customName;
    }
    
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#getEvent()
     */
    public Object getEvent() {
        return eventContext.getEvent();
    }
    
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#getEventName()
     */
    public String getEventName() {
        return eventEntryDescriptor.getEventName();
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#isActivityContextSelected()
     */
    public boolean isActivityContextSelected() {
        return iesVariables.isActivityContextSelected();
    }
     
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#isAddressProfileSelected()
     */
    public boolean isAddressProfileSelected() {
    	return iesVariables.isAddressProfileSelected();
    }
       
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#isAddressSelected()
     */
    public boolean isAddressSelected() {
    	return iesVariables.isAddressSelected();
    }
        
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#isEventSelected()
     */
    public boolean isEventSelected() {
    	return iesVariables.isEventSelected();
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#isEventTypeSelected()
     */
    public boolean isEventTypeSelected() {
    	return iesVariables.isEventTypeSelected();
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#isInitialEvent()
     */
    public boolean isInitialEvent() {
        return isInitialEvent;
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#setActivityContextSelected(boolean)
     */
    public void setActivityContextSelected(boolean isActivityContextSelected) {
    	iesVariables.setActivityContextSelected(isActivityContextSelected);
    }
    
    /* (non-Javadoc)
     * @see javax.slee.InitialEventSelector#setAddress(javax.slee.Address)
     */
    public void setAddress(Address address) {        
        this.address = address;
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#setAddressProfileSelected(boolean)
     */
    public void setAddressProfileSelected(boolean isAddressProfileSelected) {
    	iesVariables.setAddressProfileSelected(isAddressProfileSelected);
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#setAddressSelected(boolean)
     */
    public void setAddressSelected(boolean isAddressSelected) {
    	iesVariables.setAddressSelected(isAddressSelected);
    }

    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#setCustomName(java.lang.String)
     */
    public void setCustomName(String customName) {
        this.customName = customName;
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#setEventSelected(boolean)
     */
    public void setEventSelected(boolean isEventSelected) {
    	iesVariables.setEventSelected(isEventSelected);
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#setEventTypeSelected(boolean)
     */
    public void setEventTypeSelected(boolean isEventTypeSelected) {
    	iesVariables.setEventTypeSelected(isEventTypeSelected);
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.InitialEventSelector#setInitialEvent(boolean)
     */
    public void setInitialEvent(boolean isInitialEvent) {
        this.isInitialEvent = isInitialEvent;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return "InitialEventSelector = {\n eventName " + getEventName()
        	+ " event " + getEvent() 
        	+ " ach " + eventContext.getActivityContextHandle()
        	+ " address " + getAddress()
        	+ "\n isActivityContextSelected = "  + isActivityContextSelected()
        	+ "\n isAddressSelected " + isAddressSelected()
        	+ "\n isAddressProfileSelected " + isAddressProfileSelected()
        	+ "\n isEventSelected " + isEventSelected() 
        	+ "\n customName " + customName
        	+ "\n selectMethodName " + eventEntryDescriptor.getInitialEventSelectorMethod()
        	+ "\n}";
    }
}
