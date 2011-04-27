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

import net.java.slee.resource.diameter.base.events.avp.DiameterURI;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * <pre>
 * <b>6.3.19  Charging-Information AVP</b>
 * The Charging-Information is of type Grouped, and contains the addresses of the charging 
 * functions.
 * 
 * AVP format
 * Charging-Information :: = < AVP Header : 618 10415 >
 *                      [ Primary-Event-Charging-Function-Name ]
 *                      [ Secondary-Event-Charging-Function-Name ]
 *                      [ Primary-Charging-Collection-Function-Name ]
 *                      [ Secondary-Charging-Collection-Function-Name ]
 *                     *[ AVP ]
 *  
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ChargingInformation extends GroupedAvp {

  /**
   * Returns true if the Primary-Event-Charging-Function-Name AVP is present in the message.
   */
  boolean hasPrimaryEventChargingFunctionName();

  /**
   * Returns the value of the Primary-Event-Charging-Function-Name AVP, of type DiameterURI.
   * A return value of null implies that the AVP has not been set.
   */
  DiameterURI getPrimaryEventChargingFunctionName();

  /**
   * Sets the value of the Primary-Event-Charging-Function-Name AVP, of type DiameterURI.
   * @throws IllegalStateException if setPrimaryEventChargingFunctionName has already been called
   */
  void setPrimaryEventChargingFunctionName(DiameterURI primaryEventChargingFunctionName);

  /**
   * Returns true if the Secondary-Event-Charging-Function-Name AVP is present in the message.
   */
  boolean hasSecondaryEventChargingFunctionName();

  /**
   * Returns the value of the Secondary-Event-Charging-Function-Name AVP, of type DiameterURI.
   * A return value of null implies that the AVP has not been set.
   */
  DiameterURI getSecondaryEventChargingFunctionName();

  /**
   * Sets the value of the Secondary-Event-Charging-Function-Name AVP, of type DiameterURI.
   * @throws IllegalStateException if setSecondaryEventChargingFunctionName has already been called
   */
  void setSecondaryEventChargingFunctionName(DiameterURI secondaryEventChargingFunctionName);

  /**
   * Returns true if the Primary-Charging-Collection-Function-Name AVP is present in the message.
   */
  boolean hasPrimaryChargingCollectionFunctionName();

  /**
   * Returns the value of the Primary-Charging-Collection-Function-Name AVP, of type DiameterURI.
   * A return value of null implies that the AVP has not been set.
   */
  DiameterURI getPrimaryChargingCollectionFunctionName();

  /**
   * Sets the value of the Primary-Charging-Collection-Function-Name AVP, of type DiameterURI.
   * @throws IllegalStateException if setPrimaryChargingCollectionFunctionName has already been called
   */
  void setPrimaryChargingCollectionFunctionName(DiameterURI primaryChargingCollectionFunctionName);

  /**
   * Returns true if the Secondary-Charging-Collection-Function-Name AVP is present in the message.
   */
  boolean hasSecondaryChargingCollectionFunctionName();

  /**
   * Returns the value of the Secondary-Charging-Collection-Function-Name AVP, of type DiameterURI.
   * A return value of null implies that the AVP has not been set.
   */
  DiameterURI getSecondaryChargingCollectionFunctionName();

  /**
   * Sets the value of the Secondary-Charging-Collection-Function-Name AVP, of type DiameterURI.
   * @throws IllegalStateException if setSecondaryChargingCollectionFunctionName has already been called
   */
  void setSecondaryChargingCollectionFunctionName(DiameterURI secondaryChargingCollectionFunctionName);

}
