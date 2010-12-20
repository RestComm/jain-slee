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
package net.java.slee.resource.diameter.gx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.DiameterURI;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the ChargingInformation grouped AVP type.<br>
 * <br>
 * From the Diameter Cx and Dx interfaces based  Reference Point Protocol Details (3GPP TS 32.229 V6.11.0) specification:
 * <pre>
 *   6.3.19 Charging-Information AVP
 *   The Charging-Information is of type Grouped, and contains the addresses of the charging functions.
 *   AVP format
 *       Charging-Information :: = < AVP Header : 618 10415 >
 *                       [ Primary-Event-Charging-Function-Name ]
 *                       [ Secondary-Event-Charging-Function-Name ]
 *                       [ Primary-Charging-Collection-Function-Name ]
 *                       [ Secondary-Charging-Collection-Function-Name ]
 *                       *[ AVP]
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
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
   */
  void setSecondaryChargingCollectionFunctionName(DiameterURI secondaryChargingCollectionFunctionName);

}

