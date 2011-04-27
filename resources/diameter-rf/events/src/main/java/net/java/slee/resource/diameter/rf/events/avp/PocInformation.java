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

package net.java.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the PoC-Information grouped AVP type.<br>
 * <br>
 * From the Diameter Rf Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.78 PoC-Information AVP
 * The PoC-Information AVP (AVP code 879) is of type Grouped. Its purpose is to allow the transmission of additional
 * PoC service specific information elements. 
 * 
 * It has the following ABNF grammar: 
 *  PoC-Information ::= AVP Header: 879 
 *      [ PoC-Server-Role ] 
 *      [ PoC-Session-Type ] 
 *      [ Number-Of-Participants ] 
 *    * [ Participants-Involved ] 
 *    * [ Talk-Burst-Exchange ] 
 *      [ PoC-Controlling-Address ] 
 *      [ PoC-Group-Name ] 
 *      [ PoC-Session-Id ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface PocInformation extends GroupedAvp {

  /**
   * Returns the value of the Number-Of-Participants AVP, of type Integer32. A return value of null implies that the AVP has not been set.
   */
  abstract int getNumberOfParticipants();

  /**
   * Returns the set of Participants-Involved AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no Participants-Involved AVPs have been set. The elements in the given array are String objects.
   */
  abstract String[] getParticipantsInvolveds();

  /**
   * Returns the value of the PoC-Controlling-Address AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getPocControllingAddress();

  /**
   * Returns the value of the PoC-Group-Name AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getPocGroupName();

  /**
   * Returns the value of the PoC-Server-Role AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract PocServerRole getPocServerRole();

  /**
   * Returns the value of the PoC-Session-Id AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getPocSessionId();

  /**
   * Returns the value of the PoC-Session-Type AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract PocSessionType getPocSessionType();

  /**
   * Returns the set of Talk-Burst-Exchange AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no Talk-Burst-Exchange AVPs have been set. The elements in the given array are TalkBurstExchange objects.
   */
  abstract TalkBurstExchange[] getTalkBurstExchanges();

  /**
   * Returns true if the Number-Of-Participants AVP is present in the message.
   */
  abstract boolean hasNumberOfParticipants();

  /**
   * Returns true if the PoC-Controlling-Address AVP is present in the message.
   */
  abstract boolean hasPocControllingAddress();

  /**
   * Returns true if the PoC-Group-Name AVP is present in the message.
   */
  abstract boolean hasPocGroupName();

  /**
   * Returns true if the PoC-Server-Role AVP is present in the message.
   */
  abstract boolean hasPocServerRole();

  /**
   * Returns true if the PoC-Session-Id AVP is present in the message.
   */
  abstract boolean hasPocSessionId();

  /**
   * Returns true if the PoC-Session-Type AVP is present in the message.
   */
  abstract boolean hasPocSessionType();

  /**
   * Sets the value of the Number-Of-Participants AVP, of type Integer32.
   */
  abstract void setNumberOfParticipants(int numberOfParticipants);

  /**
   * Sets a single Participants-Involved AVP in the message, of type UTF8String.
   */
  abstract void setParticipantsInvolved(String participantsInvolved);

  /**
   * Sets the set of Participants-Involved AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getParticipantsInvolveds() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setParticipantsInvolveds(String[] participantsInvolveds);

  /**
   * Sets the value of the PoC-Controlling-Address AVP, of type UTF8String.
   */
  abstract void setPocControllingAddress(String pocControllingAddress);

  /**
   * Sets the value of the PoC-Group-Name AVP, of type UTF8String.
   */
  abstract void setPocGroupName(String pocGroupName);

  /**
   * Sets the value of the PoC-Server-Role AVP, of type Enumerated.
   */
  abstract void setPocServerRole(PocServerRole pocServerRole);

  /**
   * Sets the value of the PoC-Session-Id AVP, of type UTF8String.
   */
  abstract void setPocSessionId(String pocSessionId);

  /**
   * Sets the value of the PoC-Session-Type AVP, of type Enumerated.
   */
  abstract void setPocSessionType(PocSessionType pocSessionType);

  /**
   * Sets a single Talk-Burst-Exchange AVP in the message, of type Grouped.
   */
  abstract void setTalkBurstExchange(TalkBurstExchange talkBurstExchange);

  /**
   * Sets the set of Talk-Burst-Exchange AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getTalkBurstExchanges() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setTalkBurstExchanges(TalkBurstExchange[] talkBurstExchanges);

}
