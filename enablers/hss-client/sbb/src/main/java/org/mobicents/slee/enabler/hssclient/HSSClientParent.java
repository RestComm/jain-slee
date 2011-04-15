/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.enabler.hssclient;

/**
 * 
 * @author <a href=mailto:brainslog@gmail.com> Alexandre Mendonca </a>
 */
public interface HSSClientParent {

  // Sh-Pull Operations Callbacks ---------------------------------------------

  /**
   * Callback method to deliver the Repository Data data requested in getRepositoryData(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param serviceIndications the Service-Indication(s) value(s) from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   * @param data a String containing the XML for the received data
   */
  public void deliverRepositoryData(String publicIdentity, byte[][] serviceIndications, long resultCode, String data);

  /**
   * Callback method to deliver the IMS Public Identity data requested in getIMSPublicIdentity(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request (if any)
   * @param msisdn the MSISDN value from the original request (if any)
   * @param identitySet the Identity-Set value, identifying which identities to retrieve, from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   * @param data a String containing the XML for the received data
   */
  public void deliverIMSPublicIdentity(String publicIdentity, byte[] msisdn, int identitySet, long resultCode, String data);

  /**
   * Callback method to deliver the IMS User State data requested in getIMSUserState(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   * @param data a String containing the XML for the received data
   */
  public void deliverIMSUserState(String publicIdentity, long resultCode, String data);

  /**
   * Callback method to deliver the S-CSCF Name data requested in getSCSCFName(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   * @param data a String containing the XML for the received data
   */
  public void deliverSCSCFName(String publicIdentity, long resultCode, String data);

  /**
   * Callback method to deliver the Initial Filter Criteria data requested in getInitialFilterCriteria(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param serverName the Server-Name value, a SIP URI identifying the AS, from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   * @param data a String containing the XML for the received data
   */
  public void deliverInitialFilterCriteria(String publicIdentity, String serverName, long resultCode, String data);

  /**
   * Callback method to deliver the Location Information data requested in getLocationInformation(..)
   * 
   * @param msisdn the MSISDN value from the original request
   * @param requestedDomain the Requested-Domain value, indicating the access domain for which the data was requested, from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   * @param data a String containing the XML for the received data
   */
  public void deliverLocationInformation(byte[] msisdn, int requestedDomain, long resultCode, String data);

  /**
   * Callback method to deliver the User State data requested in getUserState(..)
   * 
   * @param msisdn the MSISDN value from the original request
   * @param requestedDomain the Requested-Domain value, indicating the access domain for which the data was requested, from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   * @param data a String containing the XML for the received data
   */
  public void deliverUserState(byte[] msisdn, int requestedDomain, long resultCode, String data);

  /**
   * Callback method to deliver the Charging Information data requested in getChargingInformation(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request (if any)
   * @param msisdn the MSISDN value from the original request (if any)
   * @param resultCode the Diameter Result-Code received in the answer
   * @param data a String containing the XML for the received data
   */
  public void deliverChargingInformation(String publicIdentity, byte[] msisdn, long resultCode, String data);

  /**
   * Callback method to deliver the MSISDN data requested in getMSISDN(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request (if any)
   * @param msisdn the MSISDN value from the original request (if any)
   * @param resultCode the Diameter Result-Code received in the answer
   * @param data a String containing the XML for the received data
   */
  public void deliverMSISDN(String publicIdentity, byte[] msisdn, long resultCode, String data);

  /**
   * Callback method to deliver the PSI Activation data requested in getPSIActivation(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   * @param data a String containing the XML for the received data
   */
  public void deliverPSIActivation(String publicIdentity, long resultCode, String data);

  // Sh-Update Operations Callbacks -------------------------------------------

  /**
   * Callback method to deliver the result of the profile update for Repository Data made through updateRepositoryData(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   */
  public void updateRepositoryDataResponse(String publicIdentity, long resultCode);

  /**
   * Callback method to deliver the result of the profile update for PSI Activation made through updatePSIActivation(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   */
  public void updatePSIActivationResponse(String publicIdentity, long resultCode);

  // Sh-Subscribe Operations Callbacks ----------------------------------------

  /**
   * Callback method to deliver the result of the profile subscription for Repository Data made through subscribeRepositoryData(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param serviceIndications the Service-Indication(s) value(s) from the original request
   * @param subscriptionRequestType the Subscription-Request-Type value, indicating whether to SUBSCRIBE (0) or UNSUBSCRIBE (1) to profile, from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   */
  public void subscribeRepositoryDataResponse(String publicIdentity, byte[][] serviceIndications, int subscriptionRequestType, long resultCode);

  /**
   * Callback method to deliver the result of the profile subscription for IMS User State made through subscribeIMSUserState(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param subscriptionRequestType the Subscription-Request-Type value, indicating whether to SUBSCRIBE (0) or UNSUBSCRIBE (1) to profile, from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   */
  public void subscribeIMSUserStateResponse(String publicIdentity, int subscriptionRequestType, long resultCode);

  /**
   * Callback method to deliver the result of the profile subscription for S-CSCF Name made through subscribeSCSCFName(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param subscriptionRequestType the Subscription-Request-Type value, indicating whether to SUBSCRIBE (0) or UNSUBSCRIBE (1) to profile, from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   */
  public void subscribeSCSCFNameResponse(String publicIdentity, int subscriptionRequestType, long resultCode);

  /**
   * Callback method to deliver the result of the profile subscription for Initial Filter Criteria made through subscribeInitialFilterCriteria(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param serverName the Server-Name value, a SIP URI identifying the AS, from the original request
   * @param subscriptionRequestType the Subscription-Request-Type value, indicating whether to SUBSCRIBE (0) or UNSUBSCRIBE (1) to profile, from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   */
  public void subscribeInitialFilterCriteriaResponse(String publicIdentity, String serverName, int subscriptionRequestType, long resultCode);

  /**
   * Callback method to deliver the result of the profile subscription for PSI Activation made through subscribePSIActivation(..)
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value from the original request
   * @param subscriptionRequestType the Subscription-Request-Type value, indicating whether to SUBSCRIBE (0) or UNSUBSCRIBE (1) to profile, from the original request
   * @param resultCode the Diameter Result-Code received in the answer
   */
  public void subscribePSIActivationResponse(String publicIdentity, int subscriptionRequestType, long resultCode);

  // Sh-Notify Operations Callbacks -------------------------------------------

  /**
   * Callback method to deliver a received update of a profile subscription
   * 
   * @param userIdentity the IMS Public User Identity or Public Service Identity value for the received update (if any)
   * @param msisdn the MSISDN value for the received update (if any)
   * @param data a String containing the XML for the updated data
   * @param originRealm the Diameter realm from where this update was originated
   * @param originHost the Diameter host from where this update was originated
   */
  public void receivedProfileUpdate(String userIdentity, byte[] msisdn, String data, String originRealm, String originHost);

}
