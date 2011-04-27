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

package net.java.slee.resource.diameter.ro.events.avp;

import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the MBMS-2G-3G-Indicator enumerated type.
 * <pre>
 * 17.7.17  MBMS-Counting-Information AVP
 * The MBMS-Counting-Information AVP (AVP code 914) is of type Enumerated, and contains explicit information about whether the MBMS Counting procedures are applicable for the MBMS Service that is about to start. See 3GPP TS 25.346 [72].
 * This AVP is only valid for UTRAN access type.
 * 
 * The following values are supported:
 * COUNTING-NOT-APPLICABLE (0)
 *   The MBMS Session Start Procedure signalled by the BM-SC is for a MBMS Service where MBMS Counting procedures are not applicable.
 * COUNTING-APPLICABLE (1)
 *   The MBMS Session Start Procedure signalled by the BM-SC is for a MBMS Service where MBMS Counting procedures are applicable.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public enum MbmsCountingInformation implements Enumerated {

  COUNTING_NOT_APPLICABLE(0), 
  COUNTING_APPLICABLE(1);

  private int value = -1;

  private MbmsCountingInformation(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public static MbmsCountingInformation fromInt(int type) throws IllegalArgumentException {
    switch (type) {
    case 0:
      return COUNTING_NOT_APPLICABLE;
    case 1:
      return COUNTING_APPLICABLE;

    default:
      throw new IllegalArgumentException();
    }
  }

}
