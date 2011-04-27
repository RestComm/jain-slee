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

package net.java.slee.resource.diameter.cxdx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * <pre>
 * <b>6.3.52  Restoration-Info AVP</b>
 * The Restoration-Info AVP is of type Grouped and it contains the information related to a 
 * specific registration required for an S-CSCF to handle the requests for a user. The Contact AVP
 * contains the Contact Address and Parameters in the Contact header of the registration request.
 * 
 * AVP format
 * Restoration-Info ::= < AVP Header: 649, 10415>
 *                  { Path }
 *                  { Contact }
 *                  [ Subscription-Info ]
 *                 *[ AVP ]
 *
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RestorationInfo extends GroupedAvp {

  /**
   * Returns true if the Path AVP is present in the message.
   */
  boolean hasPath();

  /**
   * Returns the value of the Path AVP, of type OctetString.
   * @return the value of the Path AVP or null if it has not been set on this message
   */
  String getPath();

  /**
   * Sets the value of the Path AVP, of type OctetString.
   * @throws IllegalStateException if setPath has already been called
   */
  void setPath(String path);

  /**
   * Returns true if the Contact AVP is present in the message.
   */
  boolean hasContact();

  /**
   * Returns the value of the Contact AVP, of type OctetString.
   * @return the value of the Contact AVP or null if it has not been set on this message
   */
  String getContact();

  /**
   * Sets the value of the Contact AVP, of type OctetString.
   * @throws IllegalStateException if setContact has already been called
   */
  void setContact(String contact);

  /**
   * Returns true if the Subscription-Info AVP is present in the message.
   */
  boolean hasSubscriptionInfo();

  /**
   * Returns the value of the Subscription-Info AVP, of type Grouped.
   * @return the value of the Subscription-Info AVP or null if it has not been set on this message
   */
  SubscriptionInfo getSubscriptionInfo();

  /**
   * Sets the value of the Subscription-Info AVP, of type Grouped.
   * @throws IllegalStateException if setSubscriptionInfo has already been called
   */
  void setSubscriptionInfo(SubscriptionInfo subscriptionInfo);

}
