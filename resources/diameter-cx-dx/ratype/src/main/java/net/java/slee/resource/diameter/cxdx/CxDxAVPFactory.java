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

package net.java.slee.resource.diameter.cxdx;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;

import net.java.slee.resource.diameter.cxdx.events.avp.AssociatedIdentities;
import net.java.slee.resource.diameter.cxdx.events.avp.AssociatedRegisteredIdentities;
import net.java.slee.resource.diameter.cxdx.events.avp.ChargingInformation;
import net.java.slee.resource.diameter.cxdx.events.avp.DeregistrationReason;
import net.java.slee.resource.diameter.cxdx.events.avp.ReasonCode;
import net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo;
import net.java.slee.resource.diameter.cxdx.events.avp.SCSCFRestorationInfo;
import net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem;
import net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate;
import net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo;
import net.java.slee.resource.diameter.cxdx.events.avp.ServerCapabilities;

/**
 * Factory to support the creation of Grouped AVP instances for Cx/Dx.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface CxDxAVPFactory extends DiameterAvpFactory{

  public DiameterAvpFactory  getBaseFactory();
  
  /**
   * Create an empty AssociatedIdentities instance.
   * 
   * @return a new and empty AssociatedIdentities instance
   */
  AssociatedIdentities createAssociatedIdentities();

  /**
   * Create an empty AssociatedRegisteredIdentities instance.
   * 
   * @return a new and empty AssociatedRegisteredIdentities instance
   */
  AssociatedRegisteredIdentities createAssociatedRegisteredIdentities();

  /**
   * Create an empty ChargingInformation instance.
   * 
   * @return a new and empty ChargingInformation instance
   */
  ChargingInformation createChargingInformation();

  /**
   * Create an empty ServerCapabilities instance.
   * 
   * @return a new and empty ServerCapabilities instance
   */
  ServerCapabilities createServerCapabilities();
  
  /**
   * Create an empty DeregistrationReason instance.
   * 
   * @return a new and empty DeregistrationReason instance
   */
  DeregistrationReason createDeregistrationReason();

  /**
   * Create an DeregistrationReason instance with mandatory AVPs filled.
   * 
   * @param reasonCode the Reason-Code AVP value
   * @return a new DeregistrationReason instance
   */
  DeregistrationReason createDeregistrationReason(ReasonCode reasonCode);

  /**
   * Create an empty RestorationInfo instance.
   * 
   * @return a new and empty RestorationInfo instance
   */
  RestorationInfo createRestorationInfo();

  /**
   * Create an RestorationInfo instance with mandatory AVPs filled.
   * 
   * @param path the Path AVP value
   * @param contact the Contact AVP value
   * @return a new RestorationInfo instance
   */
  RestorationInfo createRestorationInfo(byte[] path, byte[] contact);

  /**
   * Create an empty SCSCFRestorationInfo instance.
   * 
   * @return a new and empty SCSCFRestorationInfo instance
   */
  SCSCFRestorationInfo createSCSCFRestorationInfo();

  /**
   * Create an SCSCFRestorationInfo instance with mandatory AVPs filled.
   * 
   * @param userName the User-Name AVP value
   * @param restorationInfos the Restoration-Info AVP values
   * @return a new SCSCFRestorationInfo instance
   */
  SCSCFRestorationInfo createSCSCFRestorationInfo(String userName, RestorationInfo[] restorationInfos);

  /**
   * Create an empty SIPAuthDataItem instance.
   * 
   * @return a new and empty SIPAuthDataItem instance
   */
  SIPAuthDataItem createSIPAuthDataItem();

  /**
   * Create an empty SIPDigestAuthenticate instance.
   * 
   * @return a new and empty SIPDigestAuthenticate instance
   */
  SIPDigestAuthenticate createSIPDigestAuthenticate();

  /**
   * Create an SIPDigestAuthenticate instance with mandatory AVPs filled.
   * 
   * @param digestRealm the Digest-Realm AVP value
   * @param digestQoP the Digest-QoP AVP value
   * @param digestHA1 the Digest-HA1 AVP value
   * @return a new SIPDigestAuthenticate instance
   */
  SIPDigestAuthenticate createSIPDigestAuthenticate(String digestRealm, String digestQoP, byte[] digestHA1);

  /**
   * Create an empty SubscriptionInfo instance.
   * 
   * @return a new and empty SubscriptionInfo instance
   */
  SubscriptionInfo createSubscriptionInfo();

  /**
   * Create an SubscriptionInfo instance with mandatory AVPs filled.
   * 
   * @param callIDSIPHeader the Call-ID-SIP-Header AVP value
   * @param fromSIPHeader the From-SIP-Header AVP value
   * @param toSIPHeader the To-SIP-Header AVP value
   * @param recordRoute the Record-Route AVP value
   * @param contact the Contact AVP value
   * @return a new SubscriptionInfo instance
   */
  SubscriptionInfo createSubscriptionInfo(byte[] callIDSIPHeader, byte[] fromSIPHeader, byte[] toSIPHeader, byte[] recordRoute, byte[] contact);

}
