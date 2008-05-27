/*
 * Created on Oct 21, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.resource;

import java.io.Serializable;



/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class ResourceAdaptorState implements Serializable {
    
    /**

     * Constructor for the resourceAdaptorState

     *

     * @param resourceAdaptorState  The integer value for the resourceAdaptorState

     */

    private ResourceAdaptorState (int resourceAdaptorState) {

        this.resourceAdaptorState = resourceAdaptorState;

       resourceAdaptorStateArray[resourceAdaptorState] = this;

    }



    /**

     * This method returns the object value of the resourceAdaptorState

     *

     * @return  The resourceAdaptorState Object

     * @param resourceAdaptorState The integer value of the resourceAdaptorState

     */

    public static ResourceAdaptorState getObject(int resourceAdaptorState){

        if (resourceAdaptorState >= 0 && resourceAdaptorState < m_size) {

            return resourceAdaptorStateArray[resourceAdaptorState];

        } else {

            throw new IllegalArgumentException("Invalid resourceAdaptorState value");

        }

    }



    /**

     * This method returns the integer value of the resourceAdaptorState

     *

     * @return The integer value of the resourceAdaptorState

     */

    public int getValue() {

        return resourceAdaptorState;

    }



   



    /**

    
     * This method returns a string version of this class.
     * 
     * @return The string version of the resourceAdaptorState

     */

    public String toString() {

        String text = "";

        switch (resourceAdaptorState) {

            case _UNCONFIGURED:

                text = "Unconfigured Resource Adaptor";

                break;

            case _CONFIGURED:

                text = "Configured Resource Adaptor";

                break;                

            case _ACTIVE:

                text = "Active Resource Adaptor";

                break;
                
            case _STOPPING:

                text = "Stopping Resource Adaptor";

                break;
            

            default:

                text = "Error while printing Activity Context State";

                break;

        }

        return text;

    }

    
    // internal variables
    private int resourceAdaptorState;
  
    private static int m_size = 4;
    
    private static ResourceAdaptorState[] resourceAdaptorStateArray = new ResourceAdaptorState[m_size];    

    public static final int _UNCONFIGURED = 0;
       
    public final static ResourceAdaptorState UNCONFIGURED = new ResourceAdaptorState(_UNCONFIGURED);     

       
    public static final int _CONFIGURED = 1;
       
    public final static ResourceAdaptorState CONFIGURED = new ResourceAdaptorState(_CONFIGURED);   

    
    public static final int _ACTIVE = 2;
       
    public final static ResourceAdaptorState ACTIVE = new ResourceAdaptorState(_ACTIVE);    

    public static final int _STOPPING = 3;
    
    public final static ResourceAdaptorState STOPPING = new ResourceAdaptorState(_STOPPING);    

}
