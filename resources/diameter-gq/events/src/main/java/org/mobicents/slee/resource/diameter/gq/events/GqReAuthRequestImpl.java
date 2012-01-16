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

import net.java.slee.resource.diameter.gq.events.GqReAuthRequest;
import net.java.slee.resource.diameter.gq.events.avp.AbortCause;
import net.java.slee.resource.diameter.gq.events.avp.Flows;
import net.java.slee.resource.diameter.gq.events.avp.SpecificAction;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.DiameterGqAvpCodes;
import org.mobicents.slee.resource.diameter.gq.events.avp.FlowsImpl;




/**
 * <pre>
 * <b>7.1.3  Re-Auth-Request (RAR) command</b>
 * The RAR command, indicated by the Command-Code field set to 258 and the 'R' bit set in
 * the Command Flags field, is sent by the SPDF to the AF in order to indicate a specific
 * action.
 * 
 * However, application-specific authentication and/or authorization messages are not 
 * mandated for the Gq application in response to an RAR command.
 * 
 * The values INDICATION_OF_RELEASE_OF_BEARER, INDICATION_OF_SUBSCRIBER_DETACHMENT, 
 * INDICATION_OF_RESERVATION_EXPIRATION and INDICATION_OF_LOSS_OF_BEARER, 
 * INDICATION_OF_RECOVERY_OF_BEARER and INDICATION_OF_RELEASE_OF_BEARER of the 
 * Specific-Action AVP shall not be combined with each other in an Re-Auth-Request.
 * 
 * Message Format:
 * 
 * &lt;RA-Request&gt; ::= < Diameter Header: 258, REQ, PXY > 
 *                  < Session-Id >
 *                  { Origin-Host } 
 *                  { Origin-Realm } 
 *                  { Destination-Realm } 
 *                  { Destination-Host } 
 *                  { Auth-Application-Id }
 *                 *{ Specific-Action } 
 *                 *[ Flow-Description ]
 *                  [ Globally-Unique-Address ]
 *                  [ Logical-Access-Id ] 
 *                 *[ Flows ]
 *                  [ Abort-Cause ]
 *                  [ Origin-State-Id ] 
 *                 *[ Proxy-Info ] 
 *                 *[ Route-Record ] 
 *                 *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class GqReAuthRequestImpl extends DiameterMessageImpl implements GqReAuthRequest {

  public GqReAuthRequestImpl(Message message) {
    super(message);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#getSpecificAction
   */
  public SpecificAction[] getSpecificActions() {
    return (SpecificAction[]) getAvpsAsEnumerated(DiameterGqAvpCodes.TGPP_SPECIFIC_ACTION, DiameterGqAvpCodes.TGPP_VENDOR_ID,
        SpecificAction.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#setSpecificAction
   */
  public void setSpecificAction(SpecificAction specificAction) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.TGPP_SPECIFIC_ACTION, DiameterGqAvpCodes.TGPP_VENDOR_ID, specificAction.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#setSpecificActions
   */
  public void setSpecificActions(SpecificAction[] specificActions) throws IllegalStateException {
    for (SpecificAction specificAction : specificActions) {
      setSpecificAction(specificAction);
    }
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#getFlows
   */
  public Flows[] getFlows() {
    return (Flows[]) getAvpsAsCustom(DiameterGqAvpCodes.TGPP_FLOWS, DiameterGqAvpCodes.TGPP_VENDOR_ID, FlowsImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#setFlows
   */
  public void setFlows(Flows flow) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.TGPP_FLOWS, DiameterGqAvpCodes.TGPP_VENDOR_ID, flow.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#setFlows
   */
  public void setFlows(Flows[] flows) throws IllegalStateException {
    for (Flows flow : flows) {
      setFlows(flow);
    }
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#hasAbortCause
   */
  public boolean hasAbortCause() {
    return hasAvp(DiameterGqAvpCodes.TGPP_ABORT_CAUSE, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#getAbortCause
   */
  public AbortCause getAbortCause() {
    return (AbortCause) getAvpAsEnumerated(DiameterGqAvpCodes.TGPP_ABORT_CAUSE, DiameterGqAvpCodes.TGPP_VENDOR_ID, AbortCause.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#setAbortCause
   */
  public void setAbortCause(AbortCause abortCause) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.TGPP_ABORT_CAUSE, DiameterGqAvpCodes.TGPP_VENDOR_ID, abortCause.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#hasLogicalAccessId
   */
  public boolean hasLogicalAccessId() {
    return hasAvp(DiameterGqAvpCodes.ETSI_LOGICAL_ACCESS_ID, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#getLogicalAccessId
   */
  public byte[] getLogicalAccessId() {
    return getAvpAsOctetString(DiameterGqAvpCodes.ETSI_LOGICAL_ACCESS_ID, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqReAuthRequest#setLogicalAccessId
   */
  public void setLogicalAccessId(byte[] logicalAccessId) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.ETSI_LOGICAL_ACCESS_ID, DiameterGqAvpCodes.ETSI_VENDOR_ID, logicalAccessId);
  }

  @Override
  public String getLongName() {
    return "Re-Auth-Request";
  }

  @Override
  public String getShortName() {
    return "RAR";
  }
}
