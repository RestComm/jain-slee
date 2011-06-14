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

package net.java.slee.resource.diameter.gq.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import net.java.slee.resource.diameter.base.events.avp.IPFilterRule;

/**
 * Defines an interface representing the requested QOS and filters grouped AVP type.<br>
 * <br>
 * From the Diameter Gq' Reference Point Protocol Details (ETSI TS 183.017 V1.4.0) specification:
 * 
 * <pre>
 * 7.3.28 Media-sub-component AVP
 * The Binding-input-list AVP (AVP Code 519) is of type Grouped AVP and contains requested QoS and filters for
 * the set of IP flows identified by their common Flow-Identifier 
 * 
 * It has the following ABNF grammar: 
 *  Media-sub-component ::= AVP Header: 519 13019
 *      [ Flow-Number ]
 *  0*2 [ Flow-Description ] UL and/or DL
 *      [ Flow-Status ]
 *      [ Flow-Usage ]
 *      [ Max-Request-Bandwidth-UL ]      
 *      [ Max-Request-Bandwidth-DL ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface MediaSubComponent extends GroupedAvp {

  /**
   * Returns the value of the Flow-Number AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getFlowNumber();

  /**
   * Returns the value of the Flow-Description UL AVP, of type IPFilterRule. A return value of null implies that the AVP has not been set.
   */
  abstract IPFilterRule[] getFlowDescriptions();

  /**
   * Returns the value of the Flow-Status DL AVP, of type Flow Status. A return value of null implies that the AVP has not been set.
   */
  abstract FlowStatus getFlowStatus();

  /**
   * Returns the value of the Flow-Usage DL AVP, of type Flow Usage. A return value of null implies that the AVP has not been set.
   */
  abstract FlowUsage getFlowUsage();

  /**
   * Returns the value of the Max-Requested-Bandwidth-UL AVP, of type Unsigned32. A return value of null implies that the AVP has not been
   * set.
   */
  abstract long getMaxRequestedBandwidthUL();

  /**
   * Returns the value of the Max-Requested-Bandwidth-DL AVP, of type Unsigned32. A return value of null implies that the AVP has not been
   * set.
   */
  abstract long getMaxRequestedBandwidthDL();

  /**
   * Returns true if the Flow-Number AVP is present in the message.
   */
  abstract boolean hasFlowNumber();

  /**
   * Returns true if the Flow-Status AVP is present in the message.
   */
  abstract boolean hasFlowStatus();

  /**
   * Returns true if the Flow-Usage AVP is present in the message.
   */
  abstract boolean hasFlowUsage();

  /**
   * Returns true if the Max-Requested-Bandwidth-UL AVP is present in the message.
   */
  abstract boolean hasMaxRequestedBandwidthUL();

  /**
   * Returns true if the Max-Requested-Bandwidth-DL AVP is present in the message.
   */
  abstract boolean hasMaxRequestedBandwidthDL();

  /**
   * Sets the value of the Flow-Number AVP, of type Unsigned32.
   */
  abstract void setFlowNumber(long flowNumber);

  /**
   * Sets the value of the Flow-Description AVP, of type IP Filter Rule.
   */
  abstract void setFlowDescriptions(IPFilterRule[] flowDescriptions);

  /**
   * Sets the value of the Flow-Description AVP, of type IP Filter Rule.
   */
  abstract void setFlowDescription(IPFilterRule flowDescription);

  /**
   * Sets the value of the Flow-Status AVP, of type Flow Status.
   */
  abstract void setFlowStatus(FlowStatus flowStatus);

  /**
   * Sets the value of the Flow-Usage AVP, of type Flow Usage.
   */
  abstract void setFlowUsage(FlowUsage flowUsage);

  /**
   * Sets the value of the Max-Requested-Bandwidth-UL AVP, of type Unsigned32.
   */
  abstract void setMaxRequestedBandwidthUL(long maxRequestedBandwidthUL);

  /**
   * Sets the value of the Max-Requested-Bandwidth-DL AVP, of type Unsigned32.
   */
  abstract void setMaxRequestedBandwidthDL(long maxRequestedBandwidthDL);
}
