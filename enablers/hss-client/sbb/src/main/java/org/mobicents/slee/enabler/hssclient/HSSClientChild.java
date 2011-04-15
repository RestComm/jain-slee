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

import java.io.IOException;

/**
 * 
 * @author <a href=mailto:brainslog@gmail.com> Alexandre Mendonca </a>
 */
public interface HSSClientChild {

  // Sh-Pull Operations -------------------------------------------------------

  /**
   * Sends an User-Data-Request for Repository Data, using the following access key:
   * IMS Public User Identity or Public Service Identity + Data-Reference + Service-Indication
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param serviceIndications the Service-Indication(s) value(s)
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String getRepositoryData(String publicIdentity, byte[][] serviceIndications, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends an User-Data-Request for IMS Public Identity, using the following access key:
   * IMS Public User Identity or Public Service Identity or MSISDN + Data-Reference + Identity-Set
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value (if any)
   * @param msisdn the MSISDN value (if any)
   * @param identitySet the Identity-Set value identifying which identities to retrieve, from the following values:
   *                    ALL_IDENTITIES (0), REGISTERED_IDENTITIES (1), IMPLICIT_IDENTITIES (2)
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String getIMSPublicIdentity(String publicIdentity, byte[] msisdn, int identitySet, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends an User-Data-Request for IMS User State, using the following access key:
   * IMS Public User Identity + Data-Reference
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String getIMSUserState(String publicIdentity, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends an User-Data-Request for S-CSCFName, using the following access key:
   * IMS Public User Identity or Public Service Identity + Data-Reference
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String getSCSCFName(String publicIdentity, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends an User-Data-Request for Initial Filter Criteria, using the following access key:
   * IMS Public User Identity or Public Service Identity + Data-Reference + Server-Name
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param serverName the Server-Name value, a SIP URI identifying the AS
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String getInitialFilterCriteria(String publicIdentity, String serverName, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends an User-Data-Request for Location Information, using the following access key:
   * MSISDN + Data-Reference+ Requested-Domain
   * 
   * @param msisdn the MSISDN value
   * @param requestedDomain the Requested-Domain value indicating the access domain for which the data is requested: CS-Domain (0), PS-Domain (1)
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String getLocationInformation(byte[] msisdn, int requestedDomain, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends an User-Data-Request for User State, using the following access key:
   * MSISDN + Data-Reference+ Requested-Domain
   * 
   * @param msisdn the MSISDN value
   * @param requestedDomain the Requested-Domain value indicating the access domain for which the data is requested: CS-Domain (0), PS-Domain (1)
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String getUserState(byte[] msisdn, int requestedDomain, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends an User-Data-Request for Charging Information, using the following access key:
   * IMS Public User Identity or Public Service Identity or MSISDN + Data-Reference
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value (if any)
   * @param msisdn the MSISDN value (if any)
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String getChargingInformation(String publicIdentity, byte[] msisdn, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends an User-Data-Request for MSISDN, using the following access key:
   * IMS Public User Identity or MSISDN + Data-Reference
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value (if any)
   * @param msisdn the MSISDN value (if any)
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String getMSISDN(String publicIdentity, byte[] msisdn, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends an User-Data-Request for PSI Activation, using the following access key:
   * Public Service Identity + Data-Reference
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String getPSIActivation(String publicIdentity, String destinationRealm, String destinationHost) throws IOException;

  // Sh-Update Operations -----------------------------------------------------

  /**
   * Sends a Profile-Update-Request for Repository Data, using the following access key:
   * IMS Public User Identity or Public Service Identity + Data-Reference
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param data a String with the XML for the data to be updated 
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String updateRepositoryData(String publicIdentity, String data, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends a Profile-Update-Request for PSI Activation, using the following access key:
   * IMS Public User Identity or Public Service Identity + Data-Reference
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param data a String with the XML for the data to be updated
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String updatePSIActivation(String publicIdentity, String data, String destinationRealm, String destinationHost) throws IOException;
  
  // Sh-Subscribe Operations --------------------------------------------------

  /**
   * Sends a Subscribe-Notifications-Request for Repository Data, using the following access key:
   * IMS Public User Identity or Public Service Identity + Data-Reference + Service-Indication
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param serviceIndications the Service-Indication(s) value(s)
   * @param subscriptionRequestType the Subscription-Request-Type value indicating whether to SUBSCRIBE (0) or UNSUBSCRIBE (1) to profile
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String subscribeRepositoryData(String publicIdentity, byte[][] serviceIndications, int subscriptionRequestType, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends a Subscribe-Notifications-Request for Repository Data, using the following access key:
   * IMS Public User Identity + Data-Reference
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param subscriptionRequestType the Subscription-Request-Type value indicating whether to SUBSCRIBE (0) or UNSUBSCRIBE (1) to profile
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String subscribeIMSUserState(String publicIdentity, int subscriptionRequestType, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends a Subscribe-Notifications-Request for S-CSCF Name, using the following access key:
   * IMS Public User Identity or Public Service Identity + Data-Reference
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param subscriptionRequestType the Subscription-Request-Type value indicating whether to SUBSCRIBE (0) or UNSUBSCRIBE (1) to profile
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String subscribeSCSCFName(String publicIdentity, int subscriptionRequestType, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends a Subscribe-Notifications-Request for Initial Filter Criteria, using the following access key:
   * IMS Public User Identity or Public Service Identity + Data-Reference + Server-Name
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param serverName the Server-Name value, a SIP URI identifying the AS
   * @param subscriptionRequestType the Subscription-Request-Type value indicating whether to SUBSCRIBE (0) or UNSUBSCRIBE (1) to profile
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String subscribeInitialFilterCriteria(String publicIdentity, String serverName, int subscriptionRequestType, String destinationRealm, String destinationHost) throws IOException;

  /**
   * Sends a Subscribe-Notifications-Request for PSI Activation, using the following access key:
   * Public Service Identity + Data-Reference
   * 
   * @param publicIdentity the IMS Public User Identity or Public Service Identity value
   * @param subscriptionRequestType the Subscription-Request-Type value indicating whether to SUBSCRIBE (0) or UNSUBSCRIBE (1) to profile
   * @param destinationRealm the Diameter realm to which the request is to be sent to
   * @param destinationHost (optional parameter) the Diameter host (in the specified realm) to which the request is to be sent to
   * @return a String representing the sent request Session-Id for possible matching
   * @throws IOException
   */
  public String subscribePSIActivation(String publicIdentity, int subscriptionRequestType, String destinationRealm, String destinationHost) throws IOException;

}
