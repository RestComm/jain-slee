/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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

import java.io.Serializable;

import net.java.slee.resource.diameter.sh.events.DiameterShMessage;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.PushNotificationRequest;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.UserDataRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.IdentitySetType;
import net.java.slee.resource.diameter.sh.events.avp.RequestedDomainType;
import net.java.slee.resource.diameter.sh.events.avp.SubsReqType;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

/**
 * Data container for request and answer matching
 * 
 * @author <a href=mailto:brainslog@gmail.com> Alexandre Mendonca </a>
 */
public class MessageData implements Serializable {

  private static final long serialVersionUID = 1L;

  private String sessionId = null;
  private String publicIdentity = null;
  private String msisdn = null;

  private DataReferenceType dataReference;
  private DataReferenceType[] dataReferences;

  private byte[][] serviceIndications;

  private IdentitySetType identitySet;
  private String serverName;
  private RequestedDomainType requestedDomain;

  private SubsReqType subsReqType;

  public MessageData(DiameterShMessage message) {

    this.sessionId = message.getSessionId();

    if(message instanceof UserDataRequest) {
      UserDataRequest udr = (UserDataRequest) message;

      // retrieve public identity or msisdn
      UserIdentityAvp uIdAvp = udr.getUserIdentity();
      this.publicIdentity = uIdAvp.getPublicIdentity();
      this.msisdn = uIdAvp.getMsisdn();

      this.dataReferences = udr.getDataReferences();
      this.serviceIndications = udr.getServiceIndications();
      if(udr.hasIdentitySet()) {
        this.identitySet = udr.getIdentitySet();        
      }
      if(udr.hasServerName()) {
        this.serverName = udr.getServerName();
      }
      if(udr.hasRequestedDomain()) {
        this.requestedDomain = udr.getRequestedDomain();
      }
    }
    else if(message instanceof ProfileUpdateRequest) {
      ProfileUpdateRequest pur = (ProfileUpdateRequest) message;

      // retrieve public identity or msisdn
      UserIdentityAvp uIdAvp = pur.getUserIdentity();
      this.publicIdentity = uIdAvp.getPublicIdentity();
      this.msisdn = uIdAvp.getMsisdn();

      this.dataReference = pur.getDataReference();
    }
    else if(message instanceof SubscribeNotificationsRequest) {
      SubscribeNotificationsRequest snr = (SubscribeNotificationsRequest) message;

      // retrieve public identity or msisdn
      UserIdentityAvp uIdAvp = snr.getUserIdentity();
      this.publicIdentity = uIdAvp.getPublicIdentity();
      this.msisdn = uIdAvp.getMsisdn();

      if(snr.hasSubsReqType()) {
        this.subsReqType = snr.getSubsReqType();
      }
      this.dataReferences = snr.getDataReferences();

      this.serviceIndications = snr.getServiceIndications();

      if(snr.hasServerName()) {
        this.serverName = snr.getServerName();
      }
    }
    else if(message instanceof PushNotificationRequest) {
      PushNotificationRequest pnr = (PushNotificationRequest) message;

      // retrieve public identity or msisdn
      UserIdentityAvp uIdAvp = pnr.getUserIdentity();
      this.publicIdentity = uIdAvp.getPublicIdentity();
      this.msisdn = uIdAvp.getMsisdn();
    }
  }

  /**
   * @return the sessionId
   */
  public String getSessionId() {
    return sessionId;
  }

  /**
   * @return the publicIdentity
   */
  public String getPublicIdentity() {
    return publicIdentity;
  }

  /**
   * @return the msisdn
   */
  public byte[] getMsisdn() {
    return msisdn != null ? msisdn.getBytes() : null;
  }

  /**
   * @return the dataReference
   */
  public DataReferenceType getDataReference() {
    return dataReference;
  }

  /**
   * @return the dataReferences
   */
  public DataReferenceType[] getDataReferences() {
    return dataReferences;
  }

  /**
   * @return the serviceIndications
   */
  public byte[][] getServiceIndications() {
    return serviceIndications;
  }

  /**
   * @return the identitySet
   */
  public IdentitySetType getIdentitySet() {
    return identitySet;
  }

  /**
   * @return the serverName
   */
  public String getServerName() {
    return serverName;
  }

  /**
   * @return the requestedDomain
   */
  public RequestedDomainType getRequestedDomain() {
    return requestedDomain;
  }

  /**
   * @return the subsReqType
   */
  public SubsReqType getSubsReqType() {
    return subsReqType;
  }

}
