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
package net.java.slee.resource.diameter.rf.events;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rf.events.avp.LocationType;
import net.java.slee.resource.diameter.rf.events.avp.ServiceInformation;

/**
 * Defines an interface representing the Accounting-Request command.
 * 
 * From the Diameter Ro/Rf Protocol (TS 32299-6c0) specification:
 * 
 * <pre>
 * Accounting-Request
 * The ACR messages, indicated by the Command-Code field set to 271 is sent by the CTF to the CDF
 * in order to sent charging information for the request bearer / subsystem /service. 
 * The ACR message format is defined according to the Diameter Base Protocol [401] as follows:
 *       &lt;ACR&gt; ::= &lt; Diameter Header: 271, REQ, PXY &gt;
 *                 &lt; Session-Id &gt;
 *                 { Origin-Host }
 *                 { Origin-Realm }
 *                 { Destination-Realm }
 *                 { Accounting-Record-Type }
 *                 { Accounting-Record-Number }
 *                 [ Acct-Application-Id ]
 *                 [ User-Name ]
 *                 [ Acct-Interim-Interval ]
 *                 [ Origin-State-Id ]
 *                 [ Event-Timestamp ]              
 *                *[ Proxy-Info ]
 *                *[ Route-Record ]
 *                 [ Service-Information ]
 *                *[ AVP ]
 * 
 * 
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RfAccountingRequest extends DiameterMessage, RfAccountingMessage {

  /**
   * Returns the set of Route-Record AVPs. The returned array contains the
   * AVPs in the order they appear in the message. A return value of null
   * implies that no Route-Record AVPs have been set. The elements in the
   * given array are DiameterIdentity objects.
   */
  DiameterIdentity[] getRouteRecords();

  /**
   * Sets a single Route-Record AVP in the message, of type DiameterIdentity.
   * 
   * @throws IllegalStateException
   *             if setRouteRecord or setRouteRecords has already been called
   */
  void setRouteRecord(DiameterIdentity routeRecord);

  /**
   * Sets the set of Route-Record AVPs, with all the values in the given
   * array. The AVPs will be added to message in the order in which they
   * appear in the array.
   * 
   * Note: the array must not be altered by the caller following this call,
   * and getRouteRecords() is not guaranteed to return the same array
   * instance, e.g. an "==" check would fail.
   * 
   * @throws IllegalStateException
   *             if setRouteRecord or setRouteRecords has already been called
   */
  void setRouteRecords(DiameterIdentity[] routeRecords);

  /**
   * Returns the value of the Service-Information AVP, of type Grouped.
   * 
   * @return the value of the Service-Information AVP or null if it has not
   *         been set on this message
   */
  ServiceInformation getServiceInformation();

  /**
   * Sets the value of the Service-Information AVP, of type Grouped.
   * 
   * @throws IllegalStateException
   *             if setServiceInformation has already been called
   */
  void setServiceInformation(ServiceInformation si);

  /**
   * Returns true if the Service-Information AVP is present in the message.
   */
  boolean hasServiceInformation();

  /**
   * Returns the value of the Called-Station-Id AVP, of type UTF8String.
   * 
   * @return the value of the Called-Station-Id AVP or null if it has not been set on this message
   */
  String getCalledStationId();

  /**
   * Sets the value of the Called-Station-Id AVP, of type UTF8String.
   * 
   * @throws IllegalStateException
   *             if setCalledStationId has already been called
   */
  void setCalledStationId(String si);

  /**
   * Returns true if the Called-Station-Id AVP is present in the
   * message.
   */
  boolean hasCalledStationId();

  /**
   * Returns the value of the Location-Type AVP, of type Grouped.
   * 
   * @return the value of the Location-Type AVP or null if it has not been set on this message
   */
  LocationType getLocationType();

  /**
   * Sets the value of the Location-Type AVP, of type Grouped.
   * 
   * @throws IllegalStateException 
   *             if setLocationType has already been called
   */
  void setLocationType(LocationType si);

  /**
   * Returns true if the Location-Type AVP is present in the message.
   */
  boolean hasLocationType();

}
