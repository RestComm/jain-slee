/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.slee.resource.diameter.sh.events;

import java.util.Date;

import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.SendDataIndicationType;
import net.java.slee.resource.diameter.sh.events.avp.SubsReqType;
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
    addAvp(DiameterShAvpCodes.DATA_REFERENCE, DiameterShAvpCodes.SH_VENDOR_ID, (long)dataReference.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setDataReferences(net.java.slee.resource.diameter.sh.events.avp.DataReferenceType[])
   */
  public void setDataReferences(DataReferenceType[] dataReferences)
  {
    super.message.getAvps().removeAvp(DiameterShAvpCodes.DATA_REFERENCE);

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
  public String[] getServiceIndications() {
    return getAvpsAsOctetString(DiameterShAvpCodes.SERVICE_INDICATION, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setServiceIndication(java.lang.String)
   */
  public void setServiceIndication(String serviceIndication) {
    addAvp(DiameterShAvpCodes.SERVICE_INDICATION, DiameterShAvpCodes.SH_VENDOR_ID, serviceIndication);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest#setServiceIndications(java.lang.String[])
   */
  public void setServiceIndications(String[] serviceIndications) {
    for(String serviceIndication : serviceIndications) {
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
    addAvp(DiameterShAvpCodes.SUBS_REQ_TYPE, DiameterShAvpCodes.SH_VENDOR_ID, (long)subsReqType.getValue());
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

}
