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

/**
 * Defines an interface representing the ip flows list grouped AVP type.<br>
 * <br>
 * From the Diameter Gq' Reference Point Protocol Details (ETSI TS 183.017 V1.4.0) specification:
 * 
 * <pre>
 * 7.3.18 Flow-Grouping AVP
 * The Binding-input-list AVP (AVP Code 508) is of type Grouped AVP and holds a list of IP flows 
 * 
 * It has the following ABNF grammar: 
 *  Flow-Grouping ::= AVP Header: 508 13019
 *      [ Flows ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface FlowGrouping extends GroupedAvp {

  /**
   * Returns the value of the V4-Transport-Address AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract Flows[] getFlows();

  /**
   * Sets the value of the Flows AVP, of type Grouped.
   */
  abstract void setFlow(Flows flow);

  /**
   * Sets the value of the Flows AVP, of type Grouped.
   */
  abstract void setFlows(Flows[] flows);
}
