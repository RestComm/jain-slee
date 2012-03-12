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

package org.mobicents.slee.resource.diameter.sh.events;

import java.util.Date;

import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.SendDataIndicationType;
import net.java.slee.resource.diameter.sh.events.avp.SubsReqType;
import net.java.slee.resource.diameter.sh.events.avp.OneTimeNotificationType;
import net.java.slee.resource.diameter.sh.events.avp.IdentitySetType;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.sh.events.DiameterShMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.UserIdentityAvpImpl;

/**
 * 
 * Implementation of {@link SubscribeNotificationsRequest} interface.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SubscribeNotificationsRequestImpl extends DiameterShMessageImpl implements SubscribeNotificationsRequest {

  private static final long serialVersionUID = 3918656584341204489L;

  /**
   * @param msg
   */
  public SubscribeNotificationsRequestImpl(Message msg) {
    super(msg);

    msg.setRequest(true);

    super.longMessageName = "Subscribe-Notification-Request";
    super.shortMessageName = "SNR";
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getDataReferences()
   */
  public DataReferenceType[] getDataReferences() {
    return (DataReferenceType[]) getAvpsAsEnumerated(DiameterShAvpCodes.DATA_REFERENCE, DiameterShAvpCodes.SH_VENDOR_ID, DataReferenceType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setDataReference(net.java.slee.resource.diameter.sh.events.avp.DataReferenceType)
   */
  public void setDataReference(DataReferenceType dataReference)
  {
    addAvp(DiameterShAvpCodes.DATA_REFERENCE, DiameterShAvpCodes.SH_VENDOR_ID, dataReference.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setDataReferences(net.java.slee.resource.diameter.sh.events.avp.DataReferenceType[])
   */
  public void setDataReferences(DataReferenceType[] dataReferences)
  {
    super.message.getAvps().removeAvp(DiameterShAvpCodes.DATA_REFERENCE,DiameterShAvpCodes.SH_VENDOR_ID);

    for (DataReferenceType drt : dataReferences)
    {
      super.message.getAvps().addAvp(DiameterShAvpCodes.DATA_REFERENCE, drt.getValue(), DiameterShAvpCodes.SH_VENDOR_ID, true, false);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#hasSendDataIndication()
   */
  public boolean hasSendDataIndication()
  {
    return hasAvp(DiameterShAvpCodes.SEND_DATA_INDICATION, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getSendDataIndication()
   */
  public SendDataIndicationType getSendDataIndication() {
    return (SendDataIndicationType) getAvpAsEnumerated(DiameterShAvpCodes.SEND_DATA_INDICATION, DiameterShAvpCodes.SH_VENDOR_ID, SendDataIndicationType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setSendDataIndication(net.java.slee.resource.diameter.sh.events.avp.SendDataIndicationType)
   */
  public void setSendDataIndication(SendDataIndicationType sendDataIndication)
  {
    addAvp(DiameterShAvpCodes.SEND_DATA_INDICATION, DiameterShAvpCodes.SH_VENDOR_ID, sendDataIndication.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#hasServerName()
   */
  public boolean hasServerName()
  {
    return hasAvp(DiameterShAvpCodes.SERVER_NAME, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getServerName()
   */
  public String getServerName() {
    return getAvpAsUTF8String(DiameterShAvpCodes.SERVER_NAME, DiameterShAvpCodes.SH_VENDOR_ID);    
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setServerName(java.lang.String)
   */
  public void setServerName(String serverName)
  {
    addAvp(DiameterShAvpCodes.SERVER_NAME, DiameterShAvpCodes.SH_VENDOR_ID, serverName);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getServiceIndications()
   */
  public byte[][] getServiceIndications() {
    return getAvpsAsOctetString(DiameterShAvpCodes.SERVICE_INDICATION, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setServiceIndication(java.lang.String)
   */
  public void setServiceIndication(byte[] serviceIndication) {
    addAvp(DiameterShAvpCodes.SERVICE_INDICATION, DiameterShAvpCodes.SH_VENDOR_ID, serviceIndication);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setServiceIndications(java.lang.String[])
   */
  public void setServiceIndications(byte[][] serviceIndications) {
    for(byte[] serviceIndication : serviceIndications) {
      setServiceIndication(serviceIndication);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#hasSubsReqType()
   */
  public boolean hasSubsReqType() {
    return hasAvp(DiameterShAvpCodes.SUBS_REQ_TYPE, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getSubsReqType()
   */
  public SubsReqType getSubsReqType() {
    return (SubsReqType) getAvpAsEnumerated(DiameterShAvpCodes.SUBS_REQ_TYPE, DiameterShAvpCodes.SH_VENDOR_ID, SubsReqType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setSubsReqType(net.java.slee.resource.diameter.sh.events.avp.SubsReqType)
   */
  public void setSubsReqType(SubsReqType subsReqType) {
    addAvp(DiameterShAvpCodes.SUBS_REQ_TYPE, DiameterShAvpCodes.SH_VENDOR_ID, subsReqType.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#hasUserIdentity()
   */
  public boolean hasUserIdentity()
  {
    return hasAvp(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getUserIdentity()
   */
  public UserIdentityAvp getUserIdentity() {
    return (UserIdentityAvp) getAvpAsCustom(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, UserIdentityAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setUserIdentity(net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp)
   */
  public void setUserIdentity(UserIdentityAvp userIdentity) {
    addAvp(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, userIdentity.byteArrayValue() );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#hasExpiryTime()
   */
  public boolean hasExpiryTime() {
    return hasAvp(DiameterShAvpCodes.EXPIRY_TIME, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getExpiryTime()
   */
  public Date getExpiryTime()
  {
    return getAvpAsTime(DiameterShAvpCodes.EXPIRY_TIME, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setExpiryTime(java.util.Date)
   */
  public void setExpiryTime(Date expiryTime) {
    addAvp(DiameterShAvpCodes.EXPIRY_TIME, DiameterShAvpCodes.SH_VENDOR_ID, expiryTime);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#hasWildcardedPSI()
   */
  public boolean hasWildcardedPSI() {
    return hasAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getWildcardedPSI()
   */
  public String getWildcardedPSI() {
    return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setWildcardedPSI(String)
   */
  public void setWildcardedPSI(String wildcardedPSI) {
    addAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedPSI);
  }
  
  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#hasWildcardedIMPU()
   */
  public boolean hasWildcardedIMPU() {
    return hasAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getWildcardedIMPU()
   */
  public String getWildcardedIMPU() {
    return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setWildcardedIMPU(String)
   */
  public void setWildcardedIMPU(String wildcardedIMPU) {
    addAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedIMPU);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getIdentitySet()
   */
  public IdentitySetType[] getIdentitySets() {
    return (IdentitySetType[]) getAvpsAsEnumerated(DiameterShAvpCodes.IDENTITY_SET, DiameterShAvpCodes.SH_VENDOR_ID, IdentitySetType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setIdentitySet(IdentitySetType)
   */
  public void setIdentitySet(IdentitySetType identitySet)
  {
    addAvp(DiameterShAvpCodes.IDENTITY_SET, DiameterShAvpCodes.SH_VENDOR_ID, identitySet.getValue());
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setIdentitySets(IdentitySetType[])
   */
  public void setIdentitySets(IdentitySetType[] identitySets)
  {
	  for(IdentitySetType identitySet : identitySets) {
		  setIdentitySet(identitySet);
	    }
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#hasOneTimeNotification()
   */
  public boolean hasOneTimeNotification()
  {
    return hasAvp(DiameterShAvpCodes.ONE_TIME_NOTIFICATION, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getOneTimeNotification()
   */
  public OneTimeNotificationType getOneTimeNotification() {
    return (OneTimeNotificationType) getAvpAsEnumerated(DiameterShAvpCodes.ONE_TIME_NOTIFICATION, DiameterShAvpCodes.SH_VENDOR_ID, OneTimeNotificationType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setOneTimeNotification(OneTimeNotification)
   */
  public void setOneTimeNotification(OneTimeNotificationType oneTimeNotification)
  {
    addAvp(DiameterShAvpCodes.ONE_TIME_NOTIFICATION, DiameterShAvpCodes.SH_VENDOR_ID, oneTimeNotification.getValue());
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#getDSAITag()
   */
  public byte[][] getDSAITags() {
    return getAvpsAsRaw(DiameterShAvpCodes.DSAI_TAG, DiameterShAvpCodes.SH_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setDSAITag(byte[])
   */
  public void setDSAITag(byte[] dsaiTag) {
    addAvp(DiameterShAvpCodes.DSAI_TAG, DiameterShAvpCodes.SH_VENDOR_ID, dsaiTag);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setDSAITag(byte[])
   */
  public void setDSAITags(byte[][] dsaiTags) {
	  for(byte[] dsaiTag : dsaiTags) {
		  setDSAITag(dsaiTag);
	    }    
  }
}
