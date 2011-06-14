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

import net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest;
import net.java.slee.resource.diameter.gq.events.avp.AbortCause;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.DiameterGqAvpCodes;


/**
 * <pre>
 * <b>7.1.7 Abort-Session-Request(ASR) command</b>
 * The ASR command, indicated by the Command-Code field set to 274 and the "R" bit set in the Command Flags field, is
 * sent by the SPDF to inform the AF that all bearer resources for the authorized session have become unavailable.
 * Message Format:
 * &lt;Abort-Session-Request&gt; ::= < Diameter Header: 274, REQ, PXY >
 *                             < Session-Id >
 *                             { Origin-Host }
 *                             { Origin-Realm }
 *                             { Destination-Realm }
 *                             { Destination-Host }
 *                             { Auth-Application-Id }
 *                             { Abort-Cause }
 *                             [ Origin-State-Id ]
 *                            *[ Proxy-Info ]
 *                            *[ Route-Record ]
 *                             [ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class GqAbortSessionRequestImpl extends DiameterMessageImpl implements GqAbortSessionRequest {

  public GqAbortSessionRequestImpl(Message message) {
    super(message);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest#hasAbortCause
   */
  public boolean hasAbortCause() {
    return hasAvp(DiameterGqAvpCodes.TGPP_ABORT_CAUSE, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest#getAbortCause
   */
  public AbortCause getAbortCause() {
    return (AbortCause) getAvpAsEnumerated(DiameterGqAvpCodes.TGPP_ABORT_CAUSE, DiameterGqAvpCodes.TGPP_VENDOR_ID, AbortCause.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest#setAbortCause
   */
  public void setAbortCause(AbortCause abortCause) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.TGPP_ABORT_CAUSE, DiameterGqAvpCodes.TGPP_VENDOR_ID, abortCause.getValue());
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
