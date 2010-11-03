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
package net.java.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Trunk-Group-ID grouped AVP type.<br>
 * From the Diameter Rf Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.114 Trunk-Group-ID AVP 
 * The Trunk-Group-ID AVP (AVP code 851) is of type Grouped and identifies the incoming and outgoing PSTN legs.
 * 
 * It has the following ABNF grammar: 
 *  Trunk-Group-ID ::= AVP Header: 851 
 *      [ Incoming-Trunk-Group-ID ] 
 *      [ Outgoing-Trunk-Group-ID ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface TrunkGroupId extends GroupedAvp {

  /**
   * Returns the value of the Incoming-Trunk-Group-ID AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getIncomingTrunkGroupId();

  /**
   * Returns the value of the Outgoing-Trunk-Group-ID AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getOutgoingTrunkGroupId();

  /**
   * Returns true if the Incoming-Trunk-Group-ID AVP is present in the message.
   */
  abstract boolean hasIncomingTrunkGroupId();

  /**
   * Returns true if the Outgoing-Trunk-Group-ID AVP is present in the message.
   */
  abstract boolean hasOutgoingTrunkGroupId();

  /**
   * Sets the value of the Incoming-Trunk-Group-ID AVP, of type UTF8String.
   */
  abstract void setIncomingTrunkGroupId(String incomingTrunkGroupId);

  /**
   * Sets the value of the Outgoing-Trunk-Group-ID AVP, of type UTF8String.
   */
  abstract void setOutgoingTrunkGroupId(String outgoingTrunkGroupId);

}
