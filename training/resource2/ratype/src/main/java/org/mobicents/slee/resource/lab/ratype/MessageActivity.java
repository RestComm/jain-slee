/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.resource.lab.ratype;

import java.io.Serializable;


/**
 * The RAFActivity object models a statemachine (activity) controlled by the resource 
 * adaptor. In the case of RAFrame resource adaptor the activity / statemachine is quite
 * simple. The RAFrame resource adaptor accepts a simple message based protocol. The 
 * various protocol messages are embedded into a dialog.
 * <br>
 * The message format / protocol contains an id and a command. 
 * Normally, the message format looks like:<br>
 * id command<br>
 * e.g.<br>
 * 100 INIT<br>
 * <br>
 * The message format is embedded in a "protocol".<br>
 * The very first message in a "dialog" is the "100 INIT" command. It initiates
 * the "dialog". Here, the "dialog" for id 100 is initiated.<br>
 * The next message in a valid "dialog" is any number of "100 ANY" commands. They
 * continue the "dialog". Here, the "dialog" for id 100 is continued.<br>
 * The terminating message is the "100 END" command. It terminates the "dialog". 
 * Here the "dialog" for id 100 ends. 
 * <br>
 * The statemachine for the resource adaptor looks like this:
 *
 *  -------------           ---------          -------
 * | NOTEXISTING |--INIT-->| ACTIVE  |--END-->| ENDED |
 *  -------------           ---------          -------
 *                          ^     |
 *                          |     |
 *                           -ANY-
 * Valid is: 
 *          State NOTEXISTING ACTIVE ENDED
 * Command  INIT       X        -     -
 *          ANY        -        X     -
 *          END        -        X     -
 *
 * Invalid protocol messages are simply skipped.
 * <br>
 * The RAFActivity is referenced in the deployment descriptor file 
 * "resource-adapter-type-jar.xml" in the tag <activity-type>, sub-tag 
 * <activity-type-name>: com.maretzke.raframe.RAFActivity.
 * <br>
 * For further information, please refer to JAIN SLEE Specification 1.0, Final 
 * Release Page 238 and Page 240.
 *
 * @author Michael Maretzke
 * @author amit bhayani
 */
public interface MessageActivity extends Serializable{
	
	/**
	 * @return Uniques Id for this activity
	 */
	public String getSessionId();

	
    /**
     * Checks if an incoming command is valid according to the 
     * defined statemachine. The statemachine of the resource adaptor
     * is described above.
     *
     * @param command the integer representation of the command
     * @return true: the command does not violate the state machine; 
     * false: the command does violate the state machine
     */
    public boolean isValid(int command);

    /**
     * This method is called to signal the statemachine abstracted by the 
     * activity that a message of the type Message.INIT has been received.
     */
    public void initReceived();
    
    /**
     * This method is called to signal the statemachine abstracted by the 
     * activity that a message of the type Message.ANY has been received.
     */
    public void anyReceived();
    
    /**
     * This method is called to signal the statemachine abstracted by the 
     * activity that a message of the type Message.END has been received.
     */
    public void endReceived();
    
    /**
     * Access the internal counter for Message.INIT messages
     */
    public int getInitCounter();

    /**
     * Access the internal counter for Message.ANY messages
     */
    public int getAnyCounter();
    
    /**
     * Access the internal counter for Message.END messages
     */
    public int getEndCounter();
    
    /**
     * Access the time of creation of this activity object
     */    
    public long getStartTime();
    
}

