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

package net.java.slee.resource.diameter.sh.server;

import java.io.IOException;

import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateAnswer;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer;
import net.java.slee.resource.diameter.sh.events.UserDataAnswer;

/**
 * Activity used by a Diameter Sh Server. The following request messages can be
 * fired as events:
 * <UL>
 * <LI>UserDataRequest
 * <LI>ProfileUpdateRequest
 * <LI>SubscribeNotificationsRequest
 * </UL>
 * <p/>
 * The following answers can be sent:
 * <UL>
 * <LI>UserDataAnswer
 * <LI>ProfileUpdateAnswer
 * <LI>SubscribeNotificationsAnswer
 * </UL>
 * 
 * These are stateless requests so the activity ends when the answer is sent.
 * 
 */
public interface ShServerActivity extends DiameterActivity {

  /**
   * Create a UserDataAnswer using the given parameter to populate the
   * User-Data AVP. The Result-Code AVP is automatically set to
   * {@link org.mobicents.slee.resource.diameter.base.DiameterResultCode#DIAMETER_SUCCESS}
   * .If there is no request of this type received, this method returns null;
   * 
   * @return a UserDataAnswer object that can be sent using
   *         {@link ShServerActivity#sendUserDataAnswer(net.java.slee.resource.diameter.sh.types.UserDataAnswer)}
   */
  UserDataAnswer createUserDataAnswer(byte[] userData);

  /**
   * Create a UserDataAnswer containing a Result-Code or Experimental-Result
   * AVP populated with the given value. If
   * <code>isExperimentalResultCode</code> is <code>true</code>, the
   * <code>resultCode</code> parameter will be set in a
   * {@link org.mobicents.slee.resource.diameter.base.types.ExperimentalResultAvp}
   * AVP, if it is <code>false</code> the result code will be set in a
   * Result-Code AVP.If there is no request of this type received, this method
   * returns null;
   * 
   * @return a UserDataAnswer object that can be sent using
   *         {@link ShServerActivity#sendUserDataAnswer(net.java.slee.resource.diameter.sh.types.UserDataAnswer)}
   */
  UserDataAnswer createUserDataAnswer(long resultCode, boolean isExperimentalResult);

  /**
   * Create an empty UserDataAnswer that will need to have AVPs set on it
   * before being sent.If there is no request of this type received, this
   * method returns null;
   * 
   * @return a UserDataAnswer object that can be sent using
   *         {@link ShServerActivity#sendUserDataAnswer(net.java.slee.resource.diameter.sh.types.UserDataAnswer)}
   */
  UserDataAnswer createUserDataAnswer();

  /**
   * Create a ProfileUpdateAnswer containing a Result-Code or
   * Experimental-Result AVP populated with the given value. If
   * <code>isExperimentalResultCode</code> is <code>true</code>, the
   * <code>resultCode</code> parameter will be set in a
   * {@link org.mobicents.slee.resource.diameter.base.types.ExperimentalResultAvp}
   * AVP, if it is <code>false</code> the result code will be set in a
   * Result-Code AVP.If there is no request of this type received, this method
   * returns null;
   * 
   * @return a ProfileUpdateAnswer object that can be sent using
   *         {@link ShServerActivity#sendProfileUpdateAnswer(net.java.slee.resource.diameter.sh.types.ProfileUpdateAnswer)}
   */
  ProfileUpdateAnswer createProfileUpdateAnswer(long resultCode, boolean isExperimentalResult);

  /**
   * Create an empty ProfileUpdateAnswer that will need to have AVPs set on it
   * before being sent.If there is no request of this type received, this
   * method returns null;
   * 
   * @return a ProfileUpdateAnswer object that can be sent using
   *         {@link ShServerActivity#sendProfileUpdateAnswer(net.java.slee.resource.diameter.sh.types.ProfileUpdateAnswer)}
   */
  ProfileUpdateAnswer createProfileUpdateAnswer();

  /**
   * Create an empty SubscribeNotificationsAnswer that will need to have AVPs
   * set on it before being sent.If there is no request of this type received,
   * this method returns null;
   * 
   * @return a SubscribeNotificationsAnswer object that can be sent using
   *         {@link ShServerActivity#sendSubscribeNotificationsAnswer(SubscribeNotificationsAnswer)}
   */
  SubscribeNotificationsAnswer createSubscribeNotificationsAnswer();

  /**
   * Create a SubscribeNotificationsAnswer containing a Result-Code or
   * Experimental-Result AVP populated with the given value. If there is no
   * request of this type received, this method returns null;
   * 
   * @param resultCode
   * @param isExperimentalResult
   * @return
   */
  SubscribeNotificationsAnswer createSubscribeNotificationsAnswer(long resultCode, boolean isExperimentalResult);

  /**
   * Send the UserDataAnswer to the peer that sent the UserDataRequest.
   */
  void sendUserDataAnswer(UserDataAnswer message) throws IOException;

  /**
   * Send the ProfileUpdateAnswer to the peer that sent the
   * ProfileUpdateRequest.
   */
  void sendProfileUpdateAnswer(ProfileUpdateAnswer message) throws IOException;

  /**
   * Send the SubscribeNotificationsAnswer to the peer that sent the
   * SubscribeNotificationsRequest.
   */
  void sendSubscribeNotificationsAnswer(SubscribeNotificationsAnswer message) throws IOException;

  /**
   * Get a message factory to manually create Sh Server Messages.
   * @return
   */
  ShServerMessageFactory getServerMessageFactory();

  /**
   * Get avp factory.
   * 
   * @return
   */
  DiameterShAvpFactory getServerAvpFactory();

}
