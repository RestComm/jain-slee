/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package net.java.slee.resource.diameter.sh.client;

import net.java.slee.resource.diameter.sh.MessageFactory;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.PushNotificationAnswer;
import net.java.slee.resource.diameter.sh.events.PushNotificationRequest;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.UserDataRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.SubsReqType;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

/**
 * The Sh client interface to the Diameter MessageFactory used by applications to create Diameter Sh messages.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ShClientMessageFactory extends MessageFactory {

  /**
   * Create a UserDataRequest using the given parameters to populate the User-Identity and Data-Reference AVPs.
   * @return a UserDataRequest object that can be sent using {@link ShClientActivity#sendUserDataRequest(net.java.slee.resource.diameter.sh.events.types.UserDataRequest)} 
   */
  UserDataRequest createUserDataRequest(UserIdentityAvp userIdentity, DataReferenceType reference);

  /**
   * Create an empty UserDataRequest that will need to have AVPs set on it before being sent.
   * @return a UserDataRequest object that can be sent using {@link ShClientActivity#sendUserDataRequest(net.java.slee.resource.diameter.sh.events.types.UserDataRequest)} 
   */
  UserDataRequest createUserDataRequest();

  /**
   * Create a ProfileUpdateRequest using the given parameters to populate the User-Identity, Data-Reference and User-Data AVPs.
   * @return a ProfileUpdateRequest object that can be sent using {@link ShClientActivity#sendProfileUpdateRequest(net.java.slee.resource.diameter.sh.events.types.ProfileUpdateRequest)} 
   */
  ProfileUpdateRequest createProfileUpdateRequest(UserIdentityAvp userIdentity, DataReferenceType reference, byte[] userData);

  /**
   * Create an empty ProfileUpdateRequest that will need to have AVPs set on it before being sent.
   * 
   * @return a ProfileUpdateRequest object that can be sent using {@link ShClientActivity#sendProfileUpdateRequest(net.java.slee.resource.diameter.sh.events.types.ProfileUpdateRequest)} 
   */
  ProfileUpdateRequest createProfileUpdateRequest();

  /**
   * Create a SubscribeNotificationsRequest using the given parameters to populate the User-Identity, Data-Reference and Subs-Req-Type AVPs.
   * 
   * @return a SubscribeNotificationsRequest object that can be sent using {@link ShClientActivity#sendSubscribeNotificationsRequest(net.java.slee.resource.diameter.sh.events.types.SubscribeNotificationsRequest)} 
   * or {@link ShClientNotificationActivity#sendSubscribeNotificationsRequest(net.java.slee.resource.diameter.sh.events.types.SubscribeNotificationsRequest)}
   */
  SubscribeNotificationsRequest createSubscribeNotificationsRequest(UserIdentityAvp userIdentity, DataReferenceType reference, SubsReqType subscriptionType);

  /**
   * Create an empty SubscribeNotificationsRequest that will need to have AVPs set on it before being sent.
   * 
   * @return a SubscribeNotificationsRequest object that can be sent using {@link ShClientActivity#sendSubscribeNotificationsRequest(net.java.slee.resource.diameter.sh.events.types.SubscribeNotificationsRequest)} 
   * or {@link ShClientNotificationActivity#sendSubscribeNotificationsRequest(net.java.slee.resource.diameter.sh.events.types.SubscribeNotificationsRequest)}
   */
  SubscribeNotificationsRequest createSubscribeNotificationsRequest();

  /**
   * Create a PushNotificationAnswer containing a Result-Code or Experimental-Result AVP populated with the given value.
   * If <code>isExperimentalResultCode</code> is <code>true</code>, the <code>resultCode</code> parameter will be set
   * in a {@link org.mobicents.slee.resource.diameter.base.types.ExperimentalResultAvp} AVP, if it is <code>false</code> it 
   * will be sent as a standard Result-Code AVP.
   * 
   * @return a PushNotificationAnswer object that can be sent using {@link ShClientNotificationActivity#sendPushNotificationAnswer(net.java.slee.resource.diameter.sh.events.types.PushNotificationAnswer)} 
   */
  PushNotificationAnswer createPushNotificationAnswer(PushNotificationRequest request, long resultCode, boolean isExperimentalResultCode);

  /**
   * Create an empty PushNotificationAnswer that will need to have AVPs set on it before being sent.
   * @return a PushNotificationAnswer object that can be sent using {@link ShClientNotificationActivity#sendPushNotificationAnswer(net.java.slee.resource.diameter.sh.events.types.PushNotificationAnswer)}
   */
  PushNotificationAnswer createPushNotificationAnswer(PushNotificationRequest request);
}
