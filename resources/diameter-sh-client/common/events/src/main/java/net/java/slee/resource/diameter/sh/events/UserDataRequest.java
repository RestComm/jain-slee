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

package net.java.slee.resource.diameter.sh.events;

import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.sh.events.avp.CurrentLocationType;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.IdentitySetType;
import net.java.slee.resource.diameter.sh.events.avp.RequestedDomainType;
import net.java.slee.resource.diameter.sh.events.avp.SupportedFeaturesAvp;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

/**
 * Defines an interface representing the User-Data-Request command.
 * 
 * From the Diameter Sh Reference Point Protocol Details (3GPP TS 29.329 V7.1.0)
 * specification:
 * 
 * <pre>
 * 6.1.1        User-Data-Request (UDR) Command
 * 
 * The User-Data-Request (UDR) command, indicated by the Command-Code field set 
 * to 306 and the 'R' bit set in the Command Flags field, is sent by a Diameter
 * client to a Diameter server in order to request user data.
 * 
 * Message Format
 * &lt; User-Data-Request &gt; ::=   &lt; Diameter Header: 306, REQ, PXY, 16777217 &gt;
 *                             &lt; Session-Id &gt;
 *                             { Vendor-Specific-Application-Id }
 *                             { Auth-Session-State }
 *                             { Origin-Host }
 *                             { Origin-Realm }
 *                             [ Destination-Host ]
 *                             { Destination-Realm }
 *                             *[ Supported-Features ]
 *                             { User-Identity }
 *                             [ Server-Name ]
 *                             *[ Service-Indication ]
 *                             *{ Data-Reference }
 *                             [ Identity-Set ]
 *                             [ Requested-Domain ]
 *                             [ Current-Location ]
 *                             *[ AVP ]
 *                             *[ Proxy-Info ]
 *                             *[ Route-Record ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface UserDataRequest extends DiameterShMessage {

  static final int commandCode = 306;



  /**
   * Returns true if the User-Identity AVP is present in the message.
   */
  boolean hasUserIdentity();

  /**
   * Returns the value of the User-Identity AVP, of type Grouped.
   * 
   * @return the value of the User-Identity AVP or null if it has not been set
   *         on this message
   */
  UserIdentityAvp getUserIdentity();

  /**
   * Sets the value of the User-Identity AVP, of type Grouped.
   * 
   * @throws IllegalStateException
   *             if setUserIdentity has already been called
   */
  void setUserIdentity(UserIdentityAvp userIdentity);

  /**
   * Returns true if the Server-Name AVP is present in the message.
   */
  boolean hasServerName();

  /**
   * Returns the value of the Server-Name AVP, of type UTF8String.
   * 
   * @return the value of the Server-Name AVP or null if it has not been set
   *         on this message
   */
  String getServerName();

  /**
   * Sets the value of the Server-Name AVP, of type UTF8String.
   * 
   * @throws IllegalStateException
   *             if setServerName has already been called
   */
  void setServerName(String serverName);

  /**
   * Returns the set of Service-Indication AVPs. The returned array contains
   * the AVPs in the order they appear in the message. A return value of null
   * implies that no Service-Indication AVPs have been set. The elements in
   * the given array are byte[] objects.
   */
  byte[][] getServiceIndications();

  /**
   * Sets a single Service-Indication AVP in the message, of type OctetString.
   * 
   * @throws IllegalStateException
   *             if setServiceIndication or setServiceIndications has already
   *             been called
   */
  void setServiceIndication(byte[] serviceIndication);

  /**
   * Sets the set of Service-Indication AVPs, with all the values in the given
   * array. The AVPs will be added to message in the order in which they
   * appear in the array.
   * 
   * Note: the array must not be altered by the caller following this call,
   * and getServiceIndications() is not guaranteed to return the same array
   * instance, e.g. an "==" check would fail.
   * 
   * @throws IllegalStateException
   *             if setServiceIndication or setServiceIndications has already
   *             been called
   */
  void setServiceIndications(byte[][] serviceIndications);

  /**
   * Returns the set of Data-Reference AVPs. The returned array contains the
   * AVPs in the order they appear in the message. A return value of null
   * implies that no Data-Reference AVPs have been set. The elements in the
   * given array are DataReference objects.
   */
  DataReferenceType[] getDataReferences();

  /**
   * Sets a single Data-Reference AVP in the message, of type Enumerated.
   * 
   * @throws IllegalStateException
   *             if setDataReference or setDataReferences has already been
   *             called
   */
  void setDataReference(DataReferenceType dataReference);

  /**
   * Sets the set of Data-Reference AVPs, with all the values in the given
   * array. The AVPs will be added to message in the order in which they
   * appear in the array.
   * 
   * Note: the array must not be altered by the caller following this call,
   * and getDataReferences() is not guaranteed to return the same array
   * instance, e.g. an "==" check would fail.
   * 
   * @throws IllegalStateException
   *             if setDataReference or setDataReferences has already been
   *             called
   */
  void setDataReferences(DataReferenceType[] dataReferences);

  /**
   * Returns true if the Identity-Set AVP is present in the message.
   */
  boolean hasIdentitySet();

  /**
   * Returns the value of the Identity-Set AVP, of type Enumerated.
   * 
   * @return the value of the Identity-Set AVP or null if it has not been set
   *         on this message
   */
  IdentitySetType getIdentitySet();

  /**
   * Sets the value of the Identity-Set AVP, of type Enumerated.
   * 
   * @throws IllegalStateException
   *             if setIdentitySet has already been called
   */
  void setIdentitySet(IdentitySetType identitySet);

  /**
   * Returns true if the Requested-Domain AVP is present in the message.
   */
  boolean hasRequestedDomain();

  /**
   * Returns the value of the Requested-Domain AVP, of type Enumerated.
   * 
   * @return the value of the Requested-Domain AVP or null if it has not been
   *         set on this message
   */
  RequestedDomainType getRequestedDomain();

  /**
   * Sets the value of the Requested-Domain AVP, of type Enumerated.
   * 
   * @throws IllegalStateException
   *             if setRequestedDomain has already been called
   */
  void setRequestedDomain(RequestedDomainType requestedDomain);

  /**
   * Returns true if the Current-Location AVP is present in the message.
   */
  boolean hasCurrentLocation();

  /**
   * Returns the value of the Current-Location AVP, of type Enumerated.
   * 
   * @return the value of the Current-Location AVP or null if it has not been
   *         set on this message
   */
  CurrentLocationType getCurrentLocation();

  /**
   * Sets the value of the Current-Location AVP, of type Enumerated.
   * 
   * @throws IllegalStateException
   *             if setCurrentLocation has already been called
   */
  void setCurrentLocation(CurrentLocationType currentLocation);



}
