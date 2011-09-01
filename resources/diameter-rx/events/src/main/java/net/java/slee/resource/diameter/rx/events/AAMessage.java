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

package net.java.slee.resource.diameter.rx.events;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.rx.events.avp.SupportedFeaturesAvp;

/**
 * Base interface for Rx AA Message
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface AAMessage extends DiameterMessage {

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

  public boolean hasSupportedFeaturesAvp();

  /**
   * Returns the set of Supported-Features AVPs. The returned array contains
   * the AVPs in the order they appear in the message. A return value of null
   * implies that no Supported-Features AVPs have been set. The elements in
   * the given array are SupportedFeatures objects.
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses();

  /**
   * Sets a single Supported-Features AVP in the message, of type Grouped.
   * 
   * @throws IllegalStateException
   *             if setSupportedFeatures or setSupportedFeatureses has already
   *             been called
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures);

  /**
   * Sets the set of Supported-Features AVPs, with all the values in the given
   * array. The AVPs will be added to message in the order in which they
   * appear in the array.
   * 
   * Note: the array must not be altered by the caller following this call,
   * and getSupportedFeatureses() is not guaranteed to return the same array
   * instance, e.g. an "==" check would fail.
   * 
   * @throws IllegalStateException
   *             if setSupportedFeatures or setSupportedFeatureses has already
   *             been called
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses);
}
