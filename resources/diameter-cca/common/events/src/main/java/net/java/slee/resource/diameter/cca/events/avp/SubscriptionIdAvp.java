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

package net.java.slee.resource.diameter.cca.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * <pre>
 *  &lt;b&gt;8.46. Subscription-Id AVP&lt;/b&gt;
 * 
 * 
 *   The Subscription-Id AVP (AVP Code 443) is used to identify the end
 *   user's subscription and is of type Grouped.  The Subscription-Id AVP
 *   includes a Subscription-Id-Data AVP that holds the identifier and a
 *   Subscription-Id-Type AVP that defines the identifier type.
 * 
 *   It is defined as follows (per the grouped-avp-def of RFC 3588
 *   [DIAMBASE]):
 * 
 *      Subscription-Id ::= &lt; AVP Header: 443 &gt;
 *                          { Subscription-Id-Type }
 *                          { Subscription-Id-Data }
 * </pre>
 *      
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface SubscriptionIdAvp extends GroupedAvp {

  /**
   * Sets the value of the Subscription-Id-Type AVP, of type Enumerated. <br>
   * See:{@link SubscriptionIdType}.
   * 
   * @param type
   */
  public void setSubscriptionIdType(SubscriptionIdType type);

  /**
   * Returns the value of the Subscription-Id-Type AVP, of type Enumerated. A
   * return value of null implies that the AVP has not been set.
   * 
   * @return
   */
  public SubscriptionIdType getSubscriptionIdType();

  public boolean hasSubscriptionIdType();

  /**
   * Sets the value of the Subscription-Id-Data AVP, of type UTF8String.
   * 
   * @param data
   */
  public void setSubscriptionIdData(String data);

  /**
   * Returns the value of the Subscription-Id-Data AVP, of type UTF8String. A
   * return value of null implies that the AVP has not been set.
   * 
   * @return
   */
  public String getSubscriptionIdData();

  /**
   * Returns true if the Subscription-Id-Data AVP is present in the message.
   * 
   * @return
   */
  public boolean hasSubscriptionIdData();
}
