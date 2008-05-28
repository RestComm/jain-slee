/*
 * Diameter Sh Resource Adaptor Type
 *
 * Copyright (C) 2006 Open Cloud Ltd.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of version 2.1 of the GNU Lesser 
 * General Public License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301  USA, or see the FSF site: http://www.fsf.org.
 */
package net.java.slee.resource.diameter.sh.client;

import java.io.IOException;

import net.java.slee.resource.diameter.sh.server.events.PushNotificationAnswer;
import net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest;

/**
 * Activity used by a Diameter Sh client to receive Push-Notification-Request messages and send Push-Notification-Answer 
 * messages.
 * <p/>
 * The PushNotificationRequest is an initial event that will cause this activity to be created, and the
 * activity will end when the answer is sent. 
 * <p/>
 * A future version of the RA type will allow for this activity to be created when the Subscription request is sent, 
 * and will end only when the subscription request is cancelled by sending a SubscribeNotificationRequest message 
 * containing the SubsReq.UNSUBSCRIBE AVP, or when the subscription times out.
 * 
 * @author Open Cloud
 */
public interface ShClientNotificationActivity {

    /**
     * Get a message factory to create a PushNotificationAnswer and AVPs.
     */
    ShClientMessageFactory getClientMessageFactory();
    
    /**
     * Create a PushNotificationAnswer containing a Result-Code or Experimental-Result AVP populated with the given value.
     * If <code>isExperimentalResultCode</code> is <code>true</code>, the <code>resultCode</code> parameter will be set
     * in a {@link org.mobicents.slee.resource.diameter.base.types.ExperimentalResultAvp} AVP, if it is <code>false</code> it 
     * will be sent as a standard Result-Code AVP. 
     * @return a PushNotificationAnswer object that can be sent using {@link ShClientNotificationActivity#sendPushNotificationAnswer(net.java.slee.resource.diameter.sh.types.PushNotificationAnswer)} 
     */
    PushNotificationAnswer createPushNotificationAnswer(long resultCode, boolean isExperimentalResultCode);

    /**
     * Create an empty PushNotificationAnswer that will need to have AVPs set on it before being sent.
     * @return a PushNotificationAnswer object that can be sent using {@link ShClientNotificationActivity#sendPushNotificationAnswer(net.java.slee.resource.diameter.sh.types.PushNotificationAnswer)}
     */
    PushNotificationAnswer createPushNotificationAnswer();

    /**
     * Send the PushNotificationAnswer to the peer that sent the PushNotificationRequest.
     */
    void sendPushNotificationAnswer(PushNotificationAnswer message) throws IOException;

    /**
     * For sending an unsubscribe request ONLY.  
     * 
     * @throws IllegalArgumentException if SubsReqType is not set to 
     * {@link net.java.slee.resource.diameter.sh.types.SubsReqType#UNSUBSCRIBE}.
     */
    void sendSubscribeNotificationsRequest(SubscribeNotificationsRequest message) throws IOException;
}
