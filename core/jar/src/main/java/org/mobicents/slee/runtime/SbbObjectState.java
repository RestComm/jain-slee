/*
 * Created on Dec 3, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain. It can be
 * used in any project or product without prior permission, license or royalty
 * payments. There is no claim of correctness and NO WARRANTY OF ANY KIND
 * provided with this code.
 */
package org.mobicents.slee.runtime;

public final class SbbObjectState {

    /**
     * 
     * Constructor for the activityContextState
     * 
     * 
     * 
     * @param activityContextState
     *            The integer value for the activityContextState
     *  
     */

    private SbbObjectState(int sbbObjectState) {

        this.sbbObjectState = sbbObjectState;

        sbbObjectStateArray[sbbObjectState] = this;

    }

    /**
     * 
     * This method returns the object value of the activityContextState
     * 
     * 
     * 
     * @return The activityContextState Object
     * 
     * @param activityContextState
     *            The integer value of the activityContextState
     *  
     */

    public static SbbObjectState getObject(int sbbObjectState) {

        if (sbbObjectState >= 0 && sbbObjectState < m_size) {

            return sbbObjectStateArray[sbbObjectState];

        } else {

            throw new IllegalArgumentException(
                    "Invalid activityContextState value");

        }

    }

    /**
     * 
     * This method returns the integer value of the activityContextState
     * 
     * 
     * 
     * @return The integer value of the activityContextState
     *  
     */

    public int getValue() {

        return sbbObjectState;

    }

    /**
     * 
     * 
     * This method returns a string version of this class.
     * 
     * @return The string version of the activityContextState
     *  
     */

    public String toString() {

        String text = "";

        switch (sbbObjectState) {

        case _POOLED:

            text = "Pooled Sbb Object State";

            break;

        case _READY:

            text = "Ready Sbb Object State";

            break;
            
       case _DOES_NOT_EXIST:
           
           text = "Does not Exist Object State";

           break;

        default:

            text = "Error while printing SbbObject State";

            break;

        }

        return text;

    }

    // internal variables
    private int sbbObjectState;

    private static int m_size = 3;

    private static SbbObjectState[] sbbObjectStateArray = new SbbObjectState[m_size];

    private static final int _POOLED = 0;

    public final static SbbObjectState POOLED = new SbbObjectState(_POOLED);

    private static final int _READY = 1;

    public final static SbbObjectState READY = new SbbObjectState(_READY);
    
    private static final int _DOES_NOT_EXIST = 2;
    
    public final static SbbObjectState DOES_NOT_EXIST = new SbbObjectState(_DOES_NOT_EXIST);

}

