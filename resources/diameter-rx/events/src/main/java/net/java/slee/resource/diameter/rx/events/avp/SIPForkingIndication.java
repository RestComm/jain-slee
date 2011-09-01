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

package net.java.slee.resource.diameter.rx.events.avp;

import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * The SIP-Forking-Indication AVP (AVP code 523) is of type Enumerated, and
 * describes if several SIP dialogues are related to one Diameter session:
 * 
 * <pre>
 * SINGLE_DIALOGUE (0)
 *     This value is used to indicate that the Diameter session relates to a single SIP dialogue.
 *     This is the default value applicable if the AVP is omitted.
 * SEVERAL_DIALOGUES (1)
 *     This value is used to indicate that the Diameter session relates to several SIP dialogues.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum SIPForkingIndication implements Enumerated{

  /**
   * This value is used to indicate that the Diameter session relates to a single SIP dialogue.
   * This is the default value applicable if the AVP is omitted.
   */
  SINGLE_DIALOGUE(0),

  /**
   * This value is used to indicate that the Diameter session relates to several SIP dialogues.
   */
  SEVERAL_DIALOGUES(1);

  public static final int _SINGLE_DIALOGUE = SINGLE_DIALOGUE.getValue();
  public static final int _SEVERAL_DIALOGUES = SEVERAL_DIALOGUES.getValue();

  private int value = -1;

  private SIPForkingIndication(int v) {
    this.value=v;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public static SIPForkingIndication fromInt(int type) throws IllegalArgumentException {
    switch (type) {
      case 0:
        return SINGLE_DIALOGUE;
      case 1:
        return SEVERAL_DIALOGUES;

      default:
        throw new IllegalArgumentException();
    }
  }

  public int getValue() {
    return this.value;
  }

}
