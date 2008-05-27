package org.mobicents.slee.container.component;

public interface MobicentsSbbEventEntryInternal {

	
	
	/**
     * Sets the component key that uniquely represents this EventTypeIDImpl.
     * 
     * @param eventTypeRefKey
     *            the ComponentKey that uniquely represents this EventTypeIDImpl
     */
    public void setEventTypeRefKey(ComponentKey eventTypeRefKey) ;
	
    /**
     * Set the FIRED flag.
     * 
     * @param flag --
     *            true to set false to clear.
     */
    public void setFired(boolean flag) ;
    
    /**
     * Set the INITIAL flag.
     * 
     * @param flag
     */
    public void setInitial(boolean flag) ;
    
    /**
     * Sets the name of the method that the SLEE would invoke to determine
     * whether or not event instances are initial.
     * 
     * @param initialEventSelectorMethod
     *            the name of the initialEventSelectorMethod
     */
    public void setInitialEventSelectorMethod(
            String initialEventSelectorMethod) ;
    
    /**
     * Set the RECEIVED flag.
     * 
     * @param flag --
     *            true to set bit false to clear.
     */
    public void setReceived(boolean flag) ;
    
    /**
     * Sets any additional event handling options that the SLEE forwards to
     * resource adaptor entities that emit events of the event type represented
     * by this EventTypeIDImpl
     * 
     * @param resourceOption
     *            String
     */
    public void setResourceOption(String resourceOption) ;
}
