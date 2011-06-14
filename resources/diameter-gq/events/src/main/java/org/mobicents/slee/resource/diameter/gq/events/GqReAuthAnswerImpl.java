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
import net.java.slee.resource.diameter.gq.events.GqReAuthAnswer;
import net.java.slee.resource.diameter.gq.events.avp.FlowGrouping;
import net.java.slee.resource.diameter.gq.events.avp.MediaComponentDescription;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.DiameterGqAvpCodes;
import org.mobicents.slee.resource.diameter.gq.events.avp.FlowGroupingImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.MediaComponentDescriptionImpl;




/**
 * <pre>
 * <b>7.1.4 Re-Auth-Answer(RAA) command</b>
 * The RAA command, indicated by the Command-Code field set to 258 and the "R" bit cleared in the Command Flags
 * field, is sent by the AF to the SPDF in response to the RAR command.
 * Message Format:
 * &lt;Re-Auth-Answer&gt; ::= < Diameter Header: 258, PXY >
 *                      < Session-Id >
 *                      { Origin-Host }
 *                      { Origin-Realm }
 *                      [ Result-Code ]
 *                      [ Experimental-Result ]
 *                     *[ Media-Component-Description ]
 *                     *[ Flow-Grouping ]
 *                      [ Origin-State-Id ]
 *                      [ Error-Message ]
 *                      [ Error-Reporting-Host ]
 *                      [ Failed-AVP ]
 *                      [ Proxy-Info ]
 *                      [ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class GqReAuthAnswerImpl extends DiameterMessageImpl implements GqReAuthAnswer {

  public GqReAuthAnswerImpl(Message message) {
    super(message);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthAnswer#hasExperimentalResult
   */
  public boolean hasExperimentalResult() {
    return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer#getExperimentalResult
   */
  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) getAvpAsCustom(DiameterAvpCodes.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer#setExperimentalResult
   */
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) throws IllegalStateException {
    addAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer#getMediaComponentDescriptions
   */
  public MediaComponentDescription[] getMediaComponentDescriptions() {
    return (MediaComponentDescription[]) getAvpsAsCustom(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_DESCRIPTION,
        DiameterGqAvpCodes.TGPP_VENDOR_ID, MediaComponentDescriptionImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer#setMediaComponentDescription
   */
  public void setMediaComponentDescription(MediaComponentDescription mediaComponentDescription) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_DESCRIPTION, DiameterGqAvpCodes.TGPP_VENDOR_ID,
        mediaComponentDescription.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer#setMediaComponentDescriptions
   */
  public void setMediaComponentDescriptions(MediaComponentDescription[] mediaComponentDescriptions) throws IllegalStateException {
    for (MediaComponentDescription mediaComponentDescription : mediaComponentDescriptions) {
      setMediaComponentDescription(mediaComponentDescription);
    }
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer#getFlowGrouping
   */
  public FlowGrouping[] getFlowGroupings() {
    return (FlowGrouping[]) getAvpsAsCustom(DiameterGqAvpCodes.TGPP_FLOW_GROUPING, DiameterGqAvpCodes.TGPP_VENDOR_ID,
        FlowGroupingImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer#setFlowGrouping
   */
  public void setFlowGrouping(FlowGrouping flowGrouping) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.TGPP_FLOW_GROUPING, DiameterGqAvpCodes.TGPP_VENDOR_ID, flowGrouping.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer#setFlowGroupings
   */
  public void setFlowGroupings(FlowGrouping[] flowGroupings) throws IllegalStateException {
    for (FlowGrouping flowGrouping : flowGroupings) {
      setFlowGrouping(flowGrouping);
    }
  }

  @Override
  public String getLongName() {
    return "Re-Auth-Answer";
  }

  @Override
  public String getShortName() {
    return "RAA";
  }
}
