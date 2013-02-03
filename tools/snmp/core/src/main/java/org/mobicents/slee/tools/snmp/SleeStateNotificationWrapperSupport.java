package org.mobicents.slee.tools.snmp;

import javax.management.Notification;
import javax.slee.management.SleeStateChangeNotification;

import org.jboss.jmx.adaptor.snmp.agent.NotificationWrapperSupport;

/**
 * Simple implementation of wrapper support. It fetches all data from SleeState notification and encodes it in a way it allows
 * SNMP adaptor to consume it.
 * 
 * @author baranowb
 * 
 */
public class SleeStateNotificationWrapperSupport extends NotificationWrapperSupport {

    // Agent properties
    public static final String STATE_NEW_TAG = "n:newState";
    public static final String STATE_OLD_TAG = "n:oldState";

    public SleeStateNotificationWrapperSupport() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void prime(Notification n) {
        if (!(n instanceof SleeStateChangeNotification)) {
            throw new IllegalArgumentException("Wrong type of notification!");
        }
        // trigger super, so it puts all the data...
        super.prime(n);
        // now put our specific data
        SleeStateChangeNotification sscn = (SleeStateChangeNotification) n;

        this.payload.put(STATE_OLD_TAG, sscn.getOldState());
        this.payload.put(STATE_NEW_TAG, sscn.getNewState());

    }
}
