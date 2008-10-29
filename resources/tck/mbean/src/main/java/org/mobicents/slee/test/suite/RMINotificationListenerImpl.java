/*
 * RMINotificationListenerImpl.java
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

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.management.Notification;
import javax.management.NotificationListener;

import org.jboss.jmx.adaptor.rmi.RMINotificationListener;

import com.opencloud.sleetck.lib.infra.jmx.NotificationListenerID;

public class RMINotificationListenerImpl extends UnicastRemoteObject implements RMINotificationListener {
   
    private NotificationListenerID notificationID;
    private static int counter;
    private NotificationListener notificationListener;

    public RMINotificationListenerImpl(
            NotificationListener notificationListener) throws RemoteException {
        this.notificationListener = notificationListener;
        this.notificationID = new NotificationListenerID(counter ++);
    }
    
    public boolean equals(Object other	) {
        if ( ! other.getClass().equals(this.getClass()))return false;
        RMINotificationListenerImpl rmiImpl = (RMINotificationListenerImpl) other;
        return this.notificationID.equals(rmiImpl.notificationID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jmx.adaptor.rmi.RMINotificationListener#handleNotification(javax.management.Notification,
     *      java.lang.Object)
     */
    public void handleNotification(Notification notification, Object handback)
            throws RemoteException {
        // System.out.println("RMINotificationListener got called back!");
        this.notificationListener.handleNotification(notification, handback);

    }

}


