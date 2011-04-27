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

package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.PocInformation;
import net.java.slee.resource.diameter.ro.events.avp.PocServerRole;
import net.java.slee.resource.diameter.ro.events.avp.PocSessionType;
import net.java.slee.resource.diameter.ro.events.avp.TalkBurstExchange;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * PocInformationImpl.java
 *
 * <br>Project:  mobicents
 * <br>12:14:33 PM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class PocInformationImpl extends GroupedAvpImpl implements PocInformation {

  public PocInformationImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public PocInformationImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#getNumberOfParticipants()
   */
  public int getNumberOfParticipants() {
    return getAvpAsInteger32(DiameterRoAvpCodes.NUMBER_OF_PARTICIPANTS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#getParticipantsInvolveds()
   */
  public String[] getParticipantsInvolveds() {
    return getAvpsAsUTF8String(DiameterRoAvpCodes.PARTICIPANTS_INVOLVED, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#getPocControllingAddress()
   */
  public String getPocControllingAddress() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.POC_CONTROLLING_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#getPocGroupName()
   */
  public String getPocGroupName() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.POC_GROUP_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#getPocServerRole()
   */
  public PocServerRole getPocServerRole() {
    return (PocServerRole) getAvpAsEnumerated(DiameterRoAvpCodes.POC_SERVER_ROLE, DiameterRoAvpCodes.TGPP_VENDOR_ID, PocServerRole.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#getPocSessionId()
   */
  public String getPocSessionId() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.POC_SESSION_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#getPocSessionType()
   */
  public PocSessionType getPocSessionType() {
    return (PocSessionType) getAvpAsEnumerated(DiameterRoAvpCodes.POC_SESSION_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, PocSessionType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#getTalkBurstExchanges()
   */
  public TalkBurstExchange[] getTalkBurstExchanges() {
    return (TalkBurstExchange[]) getAvpsAsCustom(DiameterRoAvpCodes.TALK_BURST_EXCHANGE, DiameterRoAvpCodes.TGPP_VENDOR_ID, TalkBurstExchange.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#hasNumberOfParticipants()
   */
  public boolean hasNumberOfParticipants() {
    return hasAvp( DiameterRoAvpCodes.NUMBER_OF_PARTICIPANTS, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#hasPocControllingAddress()
   */
  public boolean hasPocControllingAddress() {
    return hasAvp( DiameterRoAvpCodes.POC_CONTROLLING_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#hasPocGroupName()
   */
  public boolean hasPocGroupName() {
    return hasAvp( DiameterRoAvpCodes.POC_GROUP_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#hasPocServerRole()
   */
  public boolean hasPocServerRole() {
    return hasAvp( DiameterRoAvpCodes.POC_SERVER_ROLE, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#hasPocSessionId()
   */
  public boolean hasPocSessionId() {
    return hasAvp( DiameterRoAvpCodes.POC_SESSION_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#hasPocSessionType()
   */
  public boolean hasPocSessionType() {
    return hasAvp( DiameterRoAvpCodes.POC_SESSION_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#setNumberOfParticipants(int)
   */
  public void setNumberOfParticipants( int numberOfParticipants ) {
    addAvp(DiameterRoAvpCodes.NUMBER_OF_PARTICIPANTS, DiameterRoAvpCodes.TGPP_VENDOR_ID, numberOfParticipants);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#setParticipantsInvolved(String)
   */
  public void setParticipantsInvolved( String participantsInvolved ) {
    addAvp(DiameterRoAvpCodes.PARTICIPANTS_INVOLVED, DiameterRoAvpCodes.TGPP_VENDOR_ID, participantsInvolved);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#setParticipantsInvolveds(String[])
   */
  public void setParticipantsInvolveds( String[] participantsInvolveds ) {
    for(String participantInvolved : participantsInvolveds) {
      setParticipantsInvolved(participantInvolved);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#setPocControllingAddress(String)
   */
  public void setPocControllingAddress( String pocControllingAddress ) {
    addAvp(DiameterRoAvpCodes.POC_CONTROLLING_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID, pocControllingAddress);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#setPocGroupName(String)
   */
  public void setPocGroupName( String pocGroupName ) {
    addAvp(DiameterRoAvpCodes.POC_GROUP_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID, pocGroupName);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#setPocServerRole(net.java.slee.resource.diameter.ro.events.avp.PocServerRole)
   */
  public void setPocServerRole( PocServerRole pocServerRole ) {
    addAvp(DiameterRoAvpCodes.POC_SERVER_ROLE, DiameterRoAvpCodes.TGPP_VENDOR_ID, pocServerRole.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#setPocSessionId(String)
   */
  public void setPocSessionId( String pocSessionId ) {
    addAvp(DiameterRoAvpCodes.POC_SESSION_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID, pocSessionId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#setPocSessionType(net.java.slee.resource.diameter.ro.events.avp.PocSessionType)
   */
  public void setPocSessionType( PocSessionType pocSessionType ) {
    addAvp(DiameterRoAvpCodes.POC_SESSION_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, pocSessionType.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#setTalkBurstExchange(net.java.slee.resource.diameter.ro.events.avp.TalkBurstExchange)
   */
  public void setTalkBurstExchange( TalkBurstExchange talkBurstExchange ) {
    addAvp(DiameterRoAvpCodes.TALK_BURST_EXCHANGE, DiameterRoAvpCodes.TGPP_VENDOR_ID, talkBurstExchange.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PocInformation#setTalkBurstExchanges(net.java.slee.resource.diameter.ro.events.avp.TalkBurstExchange[])
   */
  public void setTalkBurstExchanges( TalkBurstExchange[] talkBurstExchanges )
  {
    for(TalkBurstExchange talkBurstExchange : talkBurstExchanges) {
      setTalkBurstExchange(talkBurstExchange);
    }
  }

}
