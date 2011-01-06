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
package org.mobicents.slee.enabler.ims.userprofile;

import java.io.IOException;

import net.java.slee.resource.diameter.sh.events.avp.userdata.ShData;
import net.java.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory;


/**
 * 
 * @author <a href=mailto:brainslog@gmail.com> Alexandre Mendonca </a>
 */
public interface IMSUserProfileChild {

  /**
   * Sets the parent, which will be used by the client to provide async results.
   * 
   * @param parent
   */
  public void setParentSbb(IMSUserProfileParentSbbLocalObject parentSbb);

  public UserDataObjectFactory getUserDataObjectFactory();

  /**
   * Data Ref. - XML tag - Defined in - Access key
   * Operations
   * 
   * 0 - RepositoryData - 7.6.1 - IMS Public User Identity or Public Service Identity + Data-Reference + Service-Indication
   * Sh-Pull, Sh-Update, Sh-Subs-Notif
   * 
   * 10 - IMSPublicIdentity - 7.6.2 - IMS Public User Identity or Public Service Identity or MSISDN + Data-Reference + Identity-Set
   * Sh-Pull
   * 
   * 11 - IMSUserState - 7.6.3 - IMS Public User Identity + Data-Reference
   * Sh-Pull, Sh-Subs-Notif
   * 
   * 12 - S-CSCFName - 7.6.4 - IMS Public User Identity or Public Service Identity + Data-Reference
   * Sh-Pull, Sh-Subs-Notif
   * 
   * 13 - InitialFilterCriteria - 7.6.5 - IMS Public User Identity or Public Service Identity + Data-Reference + Server-Name
   * Sh-Pull, Sh-Subs-Notif
   * 
   * 14 - LocationInformation - 7.6.6 - MSISDN + Data-Reference+ Requested-Domain
   * Sh-Pull
   * 
   * 15 - UserState - 7.6.7 - MSISDN + Data-Reference+ Requested-Domain
   * Sh-Pull
   * 
   * 16 - Charging information - 7.6.8 - IMS Public User Identity or Public Service Identity or MSISDN + Data-Reference
   * Sh-Pull
   * 
   * 17 - MSISDN - 7.6.9 - IMS Public User Identity or MSISDN + Data-Reference
   * Sh-Pull
   * 
   * 18 - PSIActivation - 7.6.10 - Public Service Identity + Data-Reference
   * Sh-Pull, Sh-Update, Sh-Subs-Notif
   */

  // Sh-Pull Operations -------------------------------------------------------

  public String getRepositoryData(String publicIdentity, byte[][] serviceIndication) throws IOException;

  public String getIMSPublicIdentity(String publicIdentity, byte[] msisdn, int identitySet) throws IOException;

  public String getIMSUserState(String publicIdentity) throws IOException;

  public String getSCSCFName(String publicIdentity) throws IOException;

  public String getInitialFilterCriteria(String publicIdentity, String serverName) throws IOException;

  public String getLocationInformation(byte[] msisdn, int requestedDomain) throws IOException;

  public String getUserState(byte[] msisdn, int requestedDomain) throws IOException;

  public String getChargingInformation(String publicIdentity, byte[] msisdn) throws IOException;

  public String getMSISDN(String publicIdentity, byte[] msisdn) throws IOException;

  public String getPSIActivation(String publicIdentity) throws IOException;

  // Sh-Update Operations -----------------------------------------------------

  public String updateRepositoryData(String publicIdentity, ShData data) throws IOException;

  public String updatePSIActivation(String publicIdentity, ShData data) throws IOException;
  
  // Sh-Subscribe Operations --------------------------------------------------

  public String subscribeRepositoryData(String publicIdentity, byte[][] serviceIndication, int subscriptionRequestType) throws IOException;

  public String subscribeIMSUserState(String publicIdentity, int subscriptionRequestType) throws IOException;

  public String subscribeSCSCFName(String publicIdentity, int subscriptionRequestType) throws IOException;

  public String subscribeInitialFilterCriteria(String publicIdentity, String serverName, int subscriptionRequestType) throws IOException;

  public String subscribePSIActivation(String publicIdentity, int subscriptionRequestType) throws IOException;

}
