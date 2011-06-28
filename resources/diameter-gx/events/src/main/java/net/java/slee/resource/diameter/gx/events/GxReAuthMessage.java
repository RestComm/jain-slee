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

package net.java.slee.resource.diameter.gx.events;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;


/**
 * Base interface for RoReAuthMessage
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public interface GxReAuthMessage extends DiameterMessage {

  /**
   * Returns the value of the Auth-Application-Id AVP, of type Unsigned32.
   *
   * @return
   */
  long getAuthApplicationId();

  /**
   * Sets the value of the Auth-Application-Id AVP, of type Unsigned32.
   *
   * @param authApplicationId
   * @throws IllegalStateException
   */
  void setAuthApplicationId(long authApplicationId) throws IllegalStateException;

  /**
   * Returns true if the Auth-Application-Id AVP is present in the message.
   *
   * @return
   */
  boolean hasAuthApplicationId();

  /**
   * Returns the value of the CC-Request-Number AVP, of type Unsigned32.
   *
   * @return
   */
  
  /**
   * Returns the set of Route-Record AVPs.
   *
   * @return
   */
  DiameterIdentity[] getRouteRecords();

  /**
   * Sets a single Route-Record AVP in the message, of type DiameterIdentity.
   *
   * @param routeRecord
   * @throws IllegalStateException
   */
  void setRouteRecord(DiameterIdentity routeRecord) throws IllegalStateException;

  /**
   * Sets the set of Route-Record AVPs, with all the values in the given
   * array.
   *
   * @param routeRecords
   * @throws IllegalStateException
   */
  void setRouteRecords(DiameterIdentity[] routeRecords) throws IllegalStateException;
}
