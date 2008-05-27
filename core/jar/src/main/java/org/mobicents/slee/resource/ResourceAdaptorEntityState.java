/*
 * Created on Oct 26, 2004
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
 * Records the Resource adaptor State.
 * 
 * @author F.Moggia
 * @deprecated This class has been replaced by javax.slee.management.ResourceAdaptorEntityState
 */
public class ResourceAdaptorEntityState  implements Serializable {

    /**

     * Constructor for the resourceAdaptorEntityState

     *

     * @param resourceAdaptorEntityState  The integer value for the resourceAdaptorEntityState

     */
    private ResourceAdaptorEntityState (int resourceAdaptorEntityState) {

        this.resourceAdaptorEntityState = resourceAdaptorEntityState;

       resourceAdaptorEntityStateArray[resourceAdaptorEntityState] = this;

    }



    /**

     * This method returns the object value of the resourceAdaptorEntityState

     *

     * @return  The resourceAdaptorEntityState Object

     * @param resourceAdaptorEntityState The integer value of the resourceAdaptorEntityState

     */

    public static ResourceAdaptorEntityState getObject(int resourceAdaptorEntityState){

        if (resourceAdaptorEntityState >= 0 && resourceAdaptorEntityState < m_size) {

            return resourceAdaptorEntityStateArray[resourceAdaptorEntityState];

        } else {

            throw new IllegalArgumentException("Invalid resourceAdaptorEntityState value");

        }

    }



    /**

     * This method returns the integer value of the resourceAdaptorEntityState

     *

     * @return The integer value of the resourceAdaptorEntityState

     */

    public int getValue() {

        return resourceAdaptorEntityState;

    }



   



    /**

    
     * This method returns a string version of this class.
     * 
     * @return The string version of the resourceAdaptorEntityState

     */

    public String toString() {

        String text = "";

        switch (resourceAdaptorEntityState) {

            case _INACTIVE:

                text = "Inactive Resource Adaptor Entity";

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
    private int resourceAdaptorEntityState;
  
    private static int m_size = 3;
    
    private static ResourceAdaptorEntityState[] resourceAdaptorEntityStateArray = new ResourceAdaptorEntityState[m_size];    

    public static final int _INACTIVE = 0;
       
    public final static ResourceAdaptorEntityState INACTIVE = new ResourceAdaptorEntityState(_INACTIVE);     

       
    public static final int _ACTIVE = 1;
       
    public final static ResourceAdaptorEntityState ACTIVE = new ResourceAdaptorEntityState(_ACTIVE);   

    
    public static final int _STOPPING = 2;
    
    public final static ResourceAdaptorEntityState STOPPING = new ResourceAdaptorEntityState(_STOPPING);    

}
