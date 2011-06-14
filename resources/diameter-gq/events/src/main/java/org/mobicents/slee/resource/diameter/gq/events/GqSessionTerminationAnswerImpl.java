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
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationAnswer;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;


/**
 * <pre>
 * <b>7.1.6 Session-Termination-Answer(STA) command</b>
 * The STA command, indicated by the Command-Code field set to 275 and the "R" bit cleared in the Command Flags
 * field, is sent by the SPDF to the AF in response to the STR command.
 * Message Format:
 * &lt;Session-Termination-Answer&gt; ::= < Diameter Header: 275, PXY >
 *                                  < Session-Id >
 *                                  { Origin-Host }
 *                                  { Origin-Realm }
 *                                  [ Result-Code ]
 *                                  [ Experimental-Result ]
 *                                  [ Error-Message ]
 *                                  [ Error-Reporting-Host ]
 *                                 *[ Failed-AVP ]
 *                                  [ Origin-State-Id ]
 *                                 *[ Redirect-Host ]
 *                                  [ Redirect-Host-Usage ]
 *                                  [ Redirect-Max-Cache-Time ]
 *                                 *[ Proxy-Info ]
 *                                  [ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class GqSessionTerminationAnswerImpl extends DiameterMessageImpl implements GqSessionTerminationAnswer {

  public GqSessionTerminationAnswerImpl(Message message) {
    super(message);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqSessionTerminationAnswer#hasExperimentalResult
   */
  public boolean hasExperimentalResult() {
    return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqSessionTerminationAnswer#getExperimentalResult
   */
  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) getAvpAsCustom(DiameterAvpCodes.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqSessionTerminationAnswer#setExperimentalResult
   */
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) throws IllegalStateException {
    addAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
  }

  @Override
  public String getLongName() {
    return "Session-Termination-Answer";
  }

  @Override
  public String getShortName() {
    return "STA";
  }
}
