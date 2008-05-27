/*
* Created on Aug 5, 2004
*
*The Open SLEE project
* 
* The source code contained in this file is in in the public domain.          
* It can be used in any project or product without prior permission, 	      
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*
*/
package org.mobicents.slee.container.component;

import java.io.Serializable;

/**
 * Implementation of event type ID. This is filled in by the parser.
 * 
 * @author M. Ranganathan
 * @author Emil Ivov
 *  
 */
public interface SbbEventEntry extends Serializable{

    public static final int RECEIVED = 0x0001;

    public static final int FIRED = 0x0002;

    public static final int FIRED_AND_RECEIVED = 0x0003;

    public static final int INITIAL = 0x0004;

    
    
    public static final int ACTIVITY_CONTEXT_INITIAL_EVENT_SELECT = 1;

    public static final int ADDRESS_PROFILE_INITIAL_EVENT_SELECT = 2;

    public static final int ADDRESS_INITIAL_EVENT_SELECT = 3;

    public static final int EVENT_TYPE_INITIAL_EVENT_SELECT = 4;

    public static final int EVENT_INITIAL_EVENT_SELECT = 5;

   
        
    
    /**
     * Returns the type name of this event.
     * 
     * @return the type name of this event.
     */
    public String getTypeName() ;

    /**
     * Determines whether or not the event is initial.
     * 
     * @return boolean tru if the event is initial and false otherwise
     */
    public boolean isInitial() ;

    /**
     * Determines whether or not the event that this EvenTypeIDImpl represents
     * has a "Receive" Direction.
     * 
     * @return true if the event is to be received and false otherwise.
     */
    public boolean isReceived() ;
    /**
     * Determines whether or not the event that this EventTypeIDImple represents
     * has a "Fire" direction.
     * 
     * @return true if the event is to be fired and false otherwise.
     */
    public boolean isFired() ;

    /**
     * Determines whether this is a bidirectional event.
     * 
     * @return true if both isFired() and isReceived() return true and false
     *         otherwise.
     */
    public boolean isFiredAndReceived() ;

    /**
     * Get the event direction.
     * 
     * @return eventDirection.
     */
    public int getEventDirection() ;

    /**
     * Is this event masked.
     * 
     * @return true if event is masked.
     */
    public boolean isMasked() ;

    /**
     * Mask this event. set the event mask to true.
     */
    public void maskEvent() ;

    /**
     * Sets an array of ints that represent initial-event-select values for this
     * EventTypeIDImpl. Valid int values are among the static variables
     * <code>ACTIVITY_CONTEXT_INITIAL_EVENT_SELECT, ADDRESS_PROFILE_INITIAL_EVENT_SELECT,
     * ADDRESS_INITIAL_EVENT_SELECT, EVENT_TYPE_INITIAL_EVENT_SELECT,
     * EVENT_INITIAL_EVENT_SELECT</code>
     * param initialEventSelectors an int array of initial event selectors.
     */
    public void setInitialEventSelectors(int[] initialEventSelectors) ;

    /**
     * Returns an int array representing the set of initial event selectors.
     * 
     * @return int[]
     */
    public int[] getInitialEventSelectors() ;



    /**
     * Returns a String object containing any additional event handling options
     * that the SLEE will forward to the resource adaptor entities that emit
     * events of the event type represented by this EventTypeIDImpl
     * 
     * @return a String containing parameters for the relevant RA.
     */
    public String getResourceOption() ;



    /**
     * Returns the ComponentKey that identifies this EventTypeIDImpl
     * 
     * @return the ComponentKey the uniquely identifies this instance of the
     *         EventTypeIDImpl
     */
    public ComponentKey getEventTypeRefKey() ;

    /**
     * Sets the name of this event;
     * 
     * @param eventName
     *            the name of this event
     */
    public void setEventName(String eventName) ;

    /**
     * Returns the name of this event.
     * 
     * @return a String reference to the name of this event.
     */
    public String getEventName() ;

    /**
     * Returns the name of the method that lhe SLEE invokes to determine if an
     * event of a given event type is an initial event.
     * 
     * @return a String containing the name of the method that determines
     *         whether or not events of this type are initial.
     */
    public String getInitialEventSelectorMethod() ;


}
