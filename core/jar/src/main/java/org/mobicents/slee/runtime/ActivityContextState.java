/*
 * ActivityContextState.java
 * 
 * Created on Sep 7, 2004
 * 
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.runtime;

import java.io.Serializable;

/**
 *
 * @author mranga
 * @author Ivelin Ivanov
 */

    public final class ActivityContextState implements Serializable {


	private static final long serialVersionUID = 4755296960431243435L;

		/**
         * Constructor for the activityContextState
         *
         * @param activityContextState  The integer value for the activityContextState
         */

        private ActivityContextState (int activityContextState) {

            this.activityContextState = activityContextState;

           activityContextStateArray[activityContextState] = this;

        }



        /**

         * This method returns the object value of the activityContextState

         *

         * @return  The activityContextState Object

         * @param activityContextState The integer value of the activityContextState

         */

        public static ActivityContextState getObject(int activityContextState){

            if (activityContextState >= 0 && activityContextState < m_size) {

                return activityContextStateArray[activityContextState];

            } else {

                throw new IllegalArgumentException("Invalid activityContextState value");

            }

        }



        /**

         * This method returns the integer value of the activityContextState

         *

         * @return The integer value of the activityContextState

         */

        public int getValue() {

            return activityContextState;

        }
       
        public boolean equals (ActivityContextState acState) {
            return (this.activityContextState == acState.activityContextState);
        }


        /**

        
         * This method returns a string version of this class.
         * 
         * @return The string version of the activityContextState

         */

        public String toString() {

            String text = "";

            switch (activityContextState) {

                case _ACTIVE:

                    text = "Active Activity Context";

                    break;

                case _ENDING:

                    text = "Ending Activity Context";

                    break;                

                case _INVALID:

                    text = "Invalid Activity Context";

                    break;

                

                default:

                    text = "Error while printing Activity Context State";

                    break;

            }

            return text;

        }

        
        // internal variables
        private int activityContextState;
      
        private static int m_size = 3;
        
        private static ActivityContextState[] activityContextStateArray = new ActivityContextState[m_size];    
    
        public static final int _ACTIVE = 0;
           
        public final static ActivityContextState ACTIVE = new ActivityContextState(_ACTIVE);     
           
        public static final int _ENDING = 1;
           
        public final static ActivityContextState ENDING = new ActivityContextState(_ENDING);   

        
        public static final int _INVALID = 2;
           
        public final static ActivityContextState INVALID = new ActivityContextState(_INVALID);    

        


    }

