/*
 * BulkEventHandlerImpl.java
 * 
 * Created on Jan 19, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
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

package org.mobicents.slee.test.suite;

import com.opencloud.sleetck.lib.infra.eventbridge.BulkEventHandler;
import com.opencloud.sleetck.lib.infra.jmx.MBeanFacadeImpl;

/**
 *
 */
public class BulkEventHandlerImpl implements BulkEventHandler {
    
    // private NotificationListener notificationListener;
    private MBeanFacadeImpl mbeanFacade;
    public BulkEventHandlerImpl ( ) {
        
    }
    
  /*  public void setNotificationListener ( NotificationListener notificationListener ) {
        this.notificationListener = notificationListener;
    }
    */
    public void setMBeanFacade( MBeanFacadeImpl mbeanFacade){
       this.mbeanFacade = mbeanFacade;
    }

    /* (non-Javadoc)
     * @see com.opencloud.sleetck.lib.infra.eventbridge.BulkEventHandler#handleEvents(java.lang.Object[], java.lang.Object[])
     */
    public void handleEvents(Object[] events, Object[] channelIDs) {
        for (int i = 0 ; i < events.length; i++) {
            handleEvent(events[i], channelIDs[i]);
        }

    }

    /* (non-Javadoc)
     * @see com.opencloud.sleetck.lib.infra.eventbridge.TCKEventListener#handleEvent(java.lang.Object, java.lang.Object)
     */
    public void handleEvent(Object event, Object channelID) {
       if( this.mbeanFacade != null ) this.mbeanFacade.handleEvent( event, channelID);
       else { 
           
           System.out.println("Notification Listener not registered!");
           System.out.println("event = " + event + " Object ID = " + channelID);
       }
      
    }

}

