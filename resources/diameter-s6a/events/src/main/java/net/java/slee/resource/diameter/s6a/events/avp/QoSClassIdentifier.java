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

package net.java.slee.resource.diameter.s6a.events.avp;

import java.io.Serializable;
import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class representing the QoS-Class-Identifier enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 5.3.17  QoS-Class-Identifier AVP (All access types)
 * 
 * QoS-Class-Identifier AVP (AVP code 1028) is of type Enumerated, and it identifies a set of 
 * IP-CAN specific QoS parameters that define the authorized QoS, excluding the applicable bitrates
 * and ARP for the IP-CAN bearer or service flow. The allowed values for the nine standard QCIs are
 * defined in Table 6.1.7 of 3GPP TS 23.203 [7].
 * 
 * The following values are defined:
 * 
 * QCI_1 (1)
 *   This value shall be used to indicate standardized characteristics associated with standardized QCI value 1 from 3GPP TS 23.203 [7].
 * 
 * QCI_2 (2)
 *   This value shall be used to indicate standardized characteristics associated with standardized QCI value 2 from 3GPP TS 23.203 [7].
 * 
 * QCI_3 (3)
 *   This value shall be used to indicate standardized characteristics associated with standardized QCI value 3 from 3GPP TS 23.203 [7].
 * 
 * QCI_4 (4)
 *   This value shall be used to indicate standardized characteristics associated with standardized QCI value 4 from 3GPP TS 23.203 [7].
 * 
 * QCI_5 (5)
 *   This value shall be used to indicate standardized characteristics associated with standardized QCI value 5 from 3GPP TS 23.203 [7].
 * 
 * QCI_6 (6)
 *   This value shall be used to indicate standardized characteristics associated with standardized QCI value 6 from 3GPP TS 23.203 [7].
 * 
 * QCI_7 (7)
 *   This value shall be used to indicate standardized characteristics associated with standardized QCI value 7 from 3GPP TS 23.203 [7].
 * 
 * QCI_8 (8)
 *   This value shall be used to indicate standardized characteristics associated with standardized QCI value 8 from 3GPP TS 23.203 [7].
 * 
 * QCI_9 (9)
 *   This value shall be used to indicate standardized characteristics associated with standardized QCI value 9 from 3GPP TS 23.203 [7].
 * 
 * The QCI values 0, 10 Ð 255 are divided for usage as follows:
 *   0: Reserved
 *   10-127: Reserved
 *   128-254: Operator specific
 *   255: Reserved
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class QoSClassIdentifier implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _None = 0;
  public static final int _Conversational1 = 1;
  public static final int _Conversational2 = 2;
  public static final int _Streaming1 = 3;
  public static final int _Streaming2 = 4;
  public static final int _Interactive1 = 5;
  public static final int _Interactive2 = 6;
  public static final int _Interactive3 = 7;
  public static final int _Interactive4 = 8;
  public static final int _Background = 9;
  public static final int _OTHER = 0xFFFFFFFF;

  public static final QoSClassIdentifier None = new QoSClassIdentifier(_None);
  public static final QoSClassIdentifier Conversational1 = new QoSClassIdentifier(_Conversational1);
  public static final QoSClassIdentifier Conversational2 = new QoSClassIdentifier(_Conversational2);
  public static final QoSClassIdentifier Streaming1 = new QoSClassIdentifier(_Streaming1);
  public static final QoSClassIdentifier Streaming2 = new QoSClassIdentifier(_Streaming2);
  public static final QoSClassIdentifier Interactive1 = new QoSClassIdentifier(_Interactive1);
  public static final QoSClassIdentifier Interactive2 = new QoSClassIdentifier(_Interactive2);
  public static final QoSClassIdentifier Interactive3 = new QoSClassIdentifier(_Interactive3);
  public static final QoSClassIdentifier Interactive4 = new QoSClassIdentifier(_Interactive4);
  public static final QoSClassIdentifier Background = new QoSClassIdentifier(_Background);
  public static final QoSClassIdentifier OTHER = new QoSClassIdentifier(_OTHER);

  private int value = -1;

  private QoSClassIdentifier(int value) {
    this.value = value;
  }

  public static QoSClassIdentifier fromInt(int type) {
    switch (type) {
      case _None:
        return None;
      case _Conversational1:
        return Conversational1;
      case _Conversational2:
        return Conversational2;
      case _Streaming1:
        return Streaming1;
      case _Streaming2:
        return Streaming2;
      case _Interactive1:
        return Interactive1;
      case _Interactive2:
        return Interactive2;
      case _Interactive3:
        return Interactive3;
      case _Interactive4:
        return Interactive4;
      case _Background:
        return Background;
      case _OTHER:
        return OTHER;
      default:
        throw new IllegalArgumentException("Invalid value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    switch (value) {
      case _None:
        return "None";
      case _Conversational1:
        return "Conversational1";
      case _Conversational2:
        return "Conversational2";
      case _Streaming1:
        return "Streaming1";
      case _Streaming2:
        return "Streaming2";
      case _Interactive1:
        return "Interactive1";
      case _Interactive2:
        return "Interactive2";
      case _Interactive3:
        return "Interactive3";
      case _Interactive4:
        return "Interactive4";
      case _Background:
        return "Background";
      case _OTHER:
        return "OTHER";
      default:
        return "<Invalid Value>";
    }
  }
}