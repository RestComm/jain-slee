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
 * The Service-Info-Status AVP (AVP code 527) is of type Enumerated, and
 * indicates the status of the service information that the AF is providing to
 * the PCRF. If the Service-Info-Status AVP is not provided in the AA request,
 * the value FINAL SERVICE INFORMATION shall be assumed.
 * <pre> 
 *   FINAL SERVICE INFORMATION (0) This value is used to indicate that the service has been
 *       fully negotiated between the two ends and service information provided is the
 *       result of that negotiation. 
 *   PRELIMINARY SERVICE INFORMATION (1) This value is used to indicate that the service 
 *       information that the AF has provided to the PCRF is preliminary and needs to be further 
 *       negotiated between the two ends (e.g. for IMS when the service information is sent 
 *       based on the SDP offer).
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum ServiceInfoStatus implements Enumerated{

  /**
   * This value is used to indicate that the service has been fully negotiated
   * between the two ends and service information provided is the result of
   * that negotiation.
   */
  FINAL_SERVICE_INFORMATION(0),

  /**
   * This value is used to indicate that the service information that the AF
   * has provided to the PCRF is preliminary and needs to be further
   * negotiated between the two ends (e.g. for IMS when the service
   * information is sent based on the SDP offer).
   */
  PRELIMINARY_SERVICE_INFORMATION(1);

  public static final int _PRELIMINARY_SERVICE_INFORMATION = PRELIMINARY_SERVICE_INFORMATION.getValue();
  public static final int _FINAL_SERVICE_INFORMATION = FINAL_SERVICE_INFORMATION.getValue();

  private int value = -1;

  private ServiceInfoStatus(int v) {
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

  public static ServiceInfoStatus fromInt(int type) throws IllegalArgumentException {
    switch (type) {
      case 0:
        return FINAL_SERVICE_INFORMATION;
      case 1:
        return PRELIMINARY_SERVICE_INFORMATION;

      default:
        throw new IllegalArgumentException("Value: "+type);
    }
  }

  public int getValue() {
    return this.value;
  }

}
