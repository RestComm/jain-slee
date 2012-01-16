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

package org.mobicents.slee.resource.diameter.gq.events;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;


/**
 * <pre>
 * <b>7.1.5 Session-Termination-Request(STR) command</b>
 * The STR command, indicated by the Command-Code field set to 275 and the "R" bit set in the Command Flags field, is
 * sent by the AF to inform the SPDF that an authorized session shall be terminated.
 * Message Format:
 * &lt;Session-Termination-Request&gt; ::= < Diameter Header: 275, REQ, PXY >
 *                                   < Session-Id >
 *                                   { Origin-Host }
 *                                   { Origin-Realm }
 *                                   { Destination-Realm }
 *                                   { Termination-Cause }
 *                                   { Auth-Application-Id }
 *                                   [ Destination-Host ]
 *                                  *[ Class ]
 *                                   [ Origin-State-Id ]
 *                                  *[ Proxy-Info ]
 *                                  *[ Route-Record ]
 *                                  *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class GqSessionTerminationRequestImpl extends DiameterMessageImpl implements GqSessionTerminationRequest {

  public GqSessionTerminationRequestImpl(Message message) {
    super(message);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest#hasTerminationCause
   */
  public boolean hasTerminationCause() {
    return hasAvp(DiameterAvpCodes.TERMINATION_CAUSE);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest#getTerminationCause
   */
  public TerminationCauseType getTerminationCause() {
    return (TerminationCauseType) getAvpAsEnumerated(DiameterAvpCodes.TERMINATION_CAUSE, TerminationCauseType.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest#setTerminationCause
   */
  public void setTerminationCause(TerminationCauseType terminationCause) throws IllegalStateException {
    addAvp(DiameterAvpCodes.TERMINATION_CAUSE, terminationCause.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest#getClasses
   */
  public byte[][] getClasses() {
    return getAvpsAsOctetString(DiameterAvpCodes.CLASS_AVP);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest#setClass
   */
  public void setClass(byte[] classValue) throws IllegalStateException {
    addAvp(DiameterAvpCodes.CLASS_AVP, classValue);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest#setClasses
   */
  public void setClasses(byte[][] classes) throws IllegalStateException {
    for (byte[] classValue : classes) {
      setClass(classValue);
    }
  }

  @Override
  public String getLongName() {
    return "Session-Termination-Request";
  }

  @Override
  public String getShortName() {
    return "STR";
  }
}
