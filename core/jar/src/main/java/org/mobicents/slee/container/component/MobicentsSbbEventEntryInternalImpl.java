package org.mobicents.slee.container.component;

public class MobicentsSbbEventEntryInternalImpl implements MobicentsSbbEventEntryInternal , SbbEventEntry{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8300218186663094718L;

		private int eventDirection;

	    private String typeName = null;

	    private String initialEventSelectorMethod = null;

	    private String eventName = null;

	    private String resourceOption = null;

	    private ComponentKey eventTypeRefKey = null;

	    private boolean maskedEvent;

	    private int[] initialEventSelectors;

	    /**
	     * Create an EventTypeIDImpl method with the specified typeName and
	     * direction
	     */
	    public MobicentsSbbEventEntryInternalImpl(ComponentKey eventTypeRefKey, int direction) {
	    	if(eventTypeRefKey != null) {
	    		this.eventTypeRefKey = eventTypeRefKey;
	    		this.eventDirection = direction;
	    	}
	    	else {
	    		throw new IllegalArgumentException("eventTypeRefKey is null");
	    	}
	        
	    }

	    /**
	     * Returns the int value that is used to represent the specified value of
	     * the Variable attribute of the initial-event-select xml node.
	     * 
	     * @param eventSelectVar
	     *            the value of the initial event select variable attribute.
	     * @return the int value that represents the specified eventSelectVar value.
	     */
	    public static int eventSelectVarToInt(String eventSelectVar) {
	        if (eventSelectVar == null) {
	            throw new NullPointerException(
	                    "<null> is not a legal event select variable.");
	        }
	        if (eventSelectVar.equals("ActivityContext")) {
	            return ACTIVITY_CONTEXT_INITIAL_EVENT_SELECT;
	        }
	        if (eventSelectVar.equals("AddressProfile")) {
	            return ADDRESS_PROFILE_INITIAL_EVENT_SELECT;
	        }
	        if (eventSelectVar.equals("Address")) {
	            return ADDRESS_INITIAL_EVENT_SELECT;
	        }
	        if (eventSelectVar.equals("EventType")) {
	            return EVENT_TYPE_INITIAL_EVENT_SELECT;
	        }
	        if (eventSelectVar.equals("Event")) {
	            return EVENT_INITIAL_EVENT_SELECT;
	        } else {
	            throw new IllegalArgumentException(eventSelectVar
	                    + " is not a legal event select variable.");
	        }
	    }

	    
	    public String toString() {
	        return this.eventName + " Event Direction = " + this.eventDirection;
	    }
	    
	    
	    /**
	     * Returns the type name of this event.
	     * 
	     * @return the type name of this event.
	     */
	    public String getTypeName() {
	        return this.typeName;
	    }

	    /**
	     * Determines whether or not the event is initial.
	     * 
	     * @return boolean tru if the event is initial and false otherwise
	     */
	    public boolean isInitial() {
	        return (this.eventDirection & INITIAL) != 0;
	    }

	    /**
	     * Determines whether or not the event that this EvenTypeIDImpl represents
	     * has a "Receive" Direction.
	     * 
	     * @return true if the event is to be received and false otherwise.
	     */
	    public boolean isReceived() {
	        return (eventDirection & RECEIVED) != 0;
	    }

	    /**
	     * Determines whether or not the event that this EventTypeIDImple represents
	     * has a "Fire" direction.
	     * 
	     * @return true if the event is to be fired and false otherwise.
	     */
	    public boolean isFired() {
	        return (eventDirection & FIRED) != 0;
	    }

	    /**
	     * Determines whether this is a bidirectional event.
	     * 
	     * @return true if both isFired() and isReceived() return true and false
	     *         otherwise.
	     */
	    public boolean isFiredAndReceived() {
	        return (eventDirection & FIRED_AND_RECEIVED) != 0;
	    }

	    /**
	     * Get the event direction.
	     * 
	     * @return eventDirection.
	     */
	    public int getEventDirection() {
	        return this.eventDirection;
	    }

	    /**
	     * Is this event masked.
	     * 
	     * @return true if event is masked.
	     */
	    public boolean isMasked() {
	        return this.maskedEvent;
	    }

	    /**
	     * Mask this event. set the event mask to true.
	     */
	    public void maskEvent() {
	        this.maskedEvent = true;
	    }

	    /**
	     * Set the INITIAL flag.
	     * 
	     * @param flag
	     */
	    public void setInitial(boolean flag) {
	        if (flag)
	            this.eventDirection |= INITIAL;
	        else
	            this.eventDirection &= ~INITIAL;
	    }

	    /**
	     * Set the RECEIVED flag.
	     * 
	     * @param flag --
	     *            true to set bit false to clear.
	     */
	    public void setReceived(boolean flag) {
	        if (flag)
	            this.eventDirection |= RECEIVED;
	        else
	            this.eventDirection &= ~RECEIVED;
	    }

	    /**
	     * Set the FIRED flag.
	     * 
	     * @param flag --
	     *            true to set false to clear.
	     */
	    public void setFired(boolean flag) {
	        if (flag)
	            this.eventDirection |= FIRED;
	        else
	            this.eventDirection &= ~FIRED;
	    }

	    
	    /**
	     * Equality check override.
	     * 
	     * @return true of two typeIDs compare for equality. This is important
	     *         because they are kept in a hash set.
	     */
	    public boolean equals(Object obj) {
	    	if (obj != null && obj.getClass() == this.getClass()) {
	    		MobicentsSbbEventEntryInternalImpl other = (MobicentsSbbEventEntryInternalImpl) obj;
	    		return other.eventTypeRefKey.equals(this.eventTypeRefKey)
                	&& other.eventDirection == this.eventDirection;
	    	}
	    	else {
	    		return false;
	    	}         
	    }

	    public int hashCode() {
	    	return eventTypeRefKey.hashCode()*31+eventDirection;
	    }
	    
	    /**
	     * Sets an array of ints that represent initial-event-select values for this
	     * EventTypeIDImpl. Valid int values are among the static variables
	     * <code>ACTIVITY_CONTEXT_INITIAL_EVENT_SELECT, ADDRESS_PROFILE_INITIAL_EVENT_SELECT,
	     * ADDRESS_INITIAL_EVENT_SELECT, EVENT_TYPE_INITIAL_EVENT_SELECT,
	     * EVENT_INITIAL_EVENT_SELECT</code>
	     * param initialEventSelectors an int array of initial event selectors.
	     */
	    public void setInitialEventSelectors(int[] initialEventSelectors) {
	        this.initialEventSelectors = initialEventSelectors;
	    }

	    /**
	     * Returns an int array representing the set of initial event selectors.
	     * 
	     * @return int[]
	     */
	    public int[] getInitialEventSelectors() {
	        return this.initialEventSelectors;
	    }

	    /**
	     * Sets any additional event handling options that the SLEE forwards to
	     * resource adaptor entities that emit events of the event type represented
	     * by this EventTypeIDImpl
	     * 
	     * @param resourceOption
	     *            String
	     */
	    public void setResourceOption(String resourceOption) {
	        this.resourceOption = resourceOption;
	    }

	    /**
	     * Returns a String object containing any additional event handling options
	     * that the SLEE will forward to the resource adaptor entities that emit
	     * events of the event type represented by this EventTypeIDImpl
	     * 
	     * @return a String containing parameters for the relevant RA.
	     */
	    public String getResourceOption() {
	        return resourceOption;
	    }

	    /**
	     * Sets the component key that uniquely represents this EventTypeIDImpl.
	     * 
	     * @param eventTypeRefKey
	     *            the ComponentKey that uniquely represents this EventTypeIDImpl
	     */
	    public void setEventTypeRefKey(ComponentKey eventTypeRefKey) {
	        this.eventTypeRefKey = eventTypeRefKey;
	    }

	    /**
	     * Returns the ComponentKey that identifies this EventTypeIDImpl
	     * 
	     * @return the ComponentKey the uniquely identifies this instance of the
	     *         EventTypeIDImpl
	     */
	    public ComponentKey getEventTypeRefKey() {
	        return this.eventTypeRefKey;
	    }

	    /**
	     * Sets the name of this event;
	     * 
	     * @param eventName
	     *            the name of this event
	     */
	    public void setEventName(String eventName) {
	        this.eventName = eventName;
	    }

	    /**
	     * Returns the name of this event.
	     * 
	     * @return a String reference to the name of this event.
	     */
	    public String getEventName() {
	        return eventName;
	    }

	    /**
	     * Returns the name of the method that lhe SLEE invokes to determine if an
	     * event of a given event type is an initial event.
	     * 
	     * @return a String containing the name of the method that determines
	     *         whether or not events of this type are initial.
	     */
	    public String getInitialEventSelectorMethod() {
	        return initialEventSelectorMethod;
	    }

	    /**
	     * Sets the name of the method that the SLEE would invoke to determine
	     * whether or not event instances are initial.
	     * 
	     * @param initialEventSelectorMethod
	     *            the name of the initialEventSelectorMethod
	     */
	    public void setInitialEventSelectorMethod(
	            String initialEventSelectorMethod) {
	        this.initialEventSelectorMethod = initialEventSelectorMethod;
	    }

	
	
}
