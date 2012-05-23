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

package org.mobicents.slee.resource.diameter.rf.events.avp;

import java.util.Date;

import net.java.slee.resource.diameter.rf.events.avp.Adaptations;
import net.java.slee.resource.diameter.rf.events.avp.ContentClass;
import net.java.slee.resource.diameter.rf.events.avp.DeliveryReportRequested;
import net.java.slee.resource.diameter.rf.events.avp.DrmContent;
import net.java.slee.resource.diameter.rf.events.avp.MessageClass;
import net.java.slee.resource.diameter.rf.events.avp.MessageType;
import net.java.slee.resource.diameter.rf.events.avp.MmContentType;
import net.java.slee.resource.diameter.rf.events.avp.MmsInformation;
import net.java.slee.resource.diameter.rf.events.avp.OriginatorAddress;
import net.java.slee.resource.diameter.rf.events.avp.Priority;
import net.java.slee.resource.diameter.rf.events.avp.ReadReplyReportRequested;
import net.java.slee.resource.diameter.rf.events.avp.RecipientAddress;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * MmsInformationImpl.java
 *
 * <br>Project:  mobicents
 * <br>9:33:22 AM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MmsInformationImpl extends GroupedAvpImpl implements MmsInformation {

  public MmsInformationImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public MmsInformationImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getAdaptations()
   */
  public Adaptations getAdaptations() {
    return (Adaptations) getAvpAsEnumerated(DiameterRfAvpCodes.ADAPTATIONS, DiameterRfAvpCodes.TGPP_VENDOR_ID, Adaptations.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getApplicId()
   */
  public String getApplicId() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.APPLIC_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getAuxApplicInfo()
   */
  public String getAuxApplicInfo() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.AUX_APPLIC_INFO, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getContentClass()
   */
  public ContentClass getContentClass() {
    return (ContentClass) getAvpAsEnumerated(DiameterRfAvpCodes.CONTENT_CLASS, DiameterRfAvpCodes.TGPP_VENDOR_ID, ContentClass.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getDeliveryReportRequested()
   */
  public DeliveryReportRequested getDeliveryReportRequested() {
    return (DeliveryReportRequested) getAvpAsEnumerated(DiameterRfAvpCodes.DELIVERY_REPORT_REQUESTED, DiameterRfAvpCodes.TGPP_VENDOR_ID, DeliveryReportRequested.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getDrmContent()
   */
  public DrmContent getDrmContent() {
    return (DrmContent) getAvpAsEnumerated(DiameterRfAvpCodes.DRM_CONTENT, DiameterRfAvpCodes.TGPP_VENDOR_ID, DrmContent.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getMessageClass()
   */
  public MessageClass getMessageClass() {
    return (MessageClass) getAvpAsCustom(DiameterRfAvpCodes.MESSAGE_CLASS, DiameterRfAvpCodes.TGPP_VENDOR_ID, MessageClassImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getMessageId()
   */
  public String getMessageId() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.MESSAGE_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getMessageSize()
   */
  public long getMessageSize() {
    return getAvpAsUnsigned32(DiameterRfAvpCodes.MESSAGE_SIZE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getMessageType()
   */
  public MessageType getMessageType() {
    return (MessageType) getAvpAsEnumerated(DiameterRfAvpCodes.MESSAGE_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, MessageType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getMmContentType()
   */
  public MmContentType getMmContentType() {
    return (MmContentType) getAvpAsCustom(DiameterRfAvpCodes.MM_CONTENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, MmContentTypeImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getOriginatorAddress()
   */
  public OriginatorAddress getOriginatorAddress() {
    return (OriginatorAddress) getAvpAsCustom(DiameterRfAvpCodes.ORIGINATOR_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, OriginatorAddressImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getPriority()
   */
  public Priority getPriority() {
    return (Priority) getAvpAsEnumerated(DiameterRfAvpCodes.PRIORITY, DiameterRfAvpCodes.TGPP_VENDOR_ID, Priority.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getReadReplyReportRequested()
   */
  public ReadReplyReportRequested getReadReplyReportRequested() {
    return (ReadReplyReportRequested) getAvpAsEnumerated(DiameterRfAvpCodes.READ_REPLY_REPORT_REQUESTED, DiameterRfAvpCodes.TGPP_VENDOR_ID, ReadReplyReportRequested.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getRecipientAddresses()
   */
  public RecipientAddress[] getRecipientAddresses() {
    return (RecipientAddress[]) getAvpsAsCustom(DiameterRfAvpCodes.RECIPIENT_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, RecipientAddressImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getReplyApplicId()
   */
  public String getReplyApplicId() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.REPLY_APPLIC_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getSubmissionTime()
   */
  public Date getSubmissionTime() {
    return getAvpAsTime(DiameterRfAvpCodes.SUBMISSION_TIME, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getVasId()
   */
  public String getVasId() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.VAS_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#getVaspId()
   */
  public String getVaspId() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.VASP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasAdaptations()
   */
  public boolean hasAdaptations() {
    return hasAvp( DiameterRfAvpCodes.ADAPTATIONS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasApplicId()
   */
  public boolean hasApplicId() {
    return hasAvp( DiameterRfAvpCodes.APPLIC_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasAuxApplicInfo()
   */
  public boolean hasAuxApplicInfo() {
    return hasAvp( DiameterRfAvpCodes.AUX_APPLIC_INFO, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasContentClass()
   */
  public boolean hasContentClass() {
    return hasAvp( DiameterRfAvpCodes.CONTENT_CLASS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasDeliveryReportRequested()
   */
  public boolean hasDeliveryReportRequested() {
    return hasAvp( DiameterRfAvpCodes.DELIVERY_REPORT_REQUESTED, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasDrmContent()
   */
  public boolean hasDrmContent() {
    return hasAvp( DiameterRfAvpCodes.DRM_CONTENT, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasMessageClass()
   */
  public boolean hasMessageClass() {
    return hasAvp( DiameterRfAvpCodes.MESSAGE_CLASS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasMessageId()
   */
  public boolean hasMessageId() {
    return hasAvp( DiameterRfAvpCodes.MESSAGE_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasMessageSize()
   */
  public boolean hasMessageSize() {
    return hasAvp( DiameterRfAvpCodes.MESSAGE_SIZE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasMessageType()
   */
  public boolean hasMessageType() {
    return hasAvp( DiameterRfAvpCodes.MESSAGE_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasMmContentType()
   */
  public boolean hasMmContentType() {
    return hasAvp( DiameterRfAvpCodes.MM_CONTENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasOriginatorAddress()
   */
  public boolean hasOriginatorAddress() {
    return hasAvp( DiameterRfAvpCodes.ORIGINATOR_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasPriority()
   */
  public boolean hasPriority() {
    return hasAvp( DiameterRfAvpCodes.PRIORITY, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasReadReplyReportRequested()
   */
  public boolean hasReadReplyReportRequested() {
    return hasAvp( DiameterRfAvpCodes.READ_REPLY_REPORT_REQUESTED, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasReplyApplicId()
   */
  public boolean hasReplyApplicId() {
    return hasAvp( DiameterRfAvpCodes.REPLY_APPLIC_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasSubmissionTime()
   */
  public boolean hasSubmissionTime() {
    return hasAvp( DiameterRfAvpCodes.SUBMISSION_TIME, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasVasId()
   */
  public boolean hasVasId() {
    return hasAvp( DiameterRfAvpCodes.VAS_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#hasVaspId()
   */
  public boolean hasVaspId() {
    return hasAvp( DiameterRfAvpCodes.VASP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setAdaptations(net.java.slee.resource.diameter.rf.events.avp.Adaptations)
   */
  public void setAdaptations( Adaptations adaptations ) {
    addAvp(DiameterRfAvpCodes.ADAPTATIONS, DiameterRfAvpCodes.TGPP_VENDOR_ID, adaptations.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setApplicId(String)
   */
  public void setApplicId( String applicId ) {
    addAvp(DiameterRfAvpCodes.APPLIC_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, applicId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setAuxApplicInfo(String)
   */
  public void setAuxApplicInfo( String auxApplicInfo ) {
    addAvp(DiameterRfAvpCodes.AUX_APPLIC_INFO, DiameterRfAvpCodes.TGPP_VENDOR_ID, auxApplicInfo);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setContentClass(net.java.slee.resource.diameter.rf.events.avp.ContentClass)
   */
  public void setContentClass( ContentClass contentClass ) {
    addAvp(DiameterRfAvpCodes.CONTENT_CLASS, DiameterRfAvpCodes.TGPP_VENDOR_ID, contentClass.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setDeliveryReportRequested(net.java.slee.resource.diameter.rf.events.avp.DeliveryReportRequested)
   */
  public void setDeliveryReportRequested( DeliveryReportRequested deliveryReportRequested ) {
    addAvp(DiameterRfAvpCodes.DELIVERY_REPORT_REQUESTED, DiameterRfAvpCodes.TGPP_VENDOR_ID, deliveryReportRequested.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setDrmContent(net.java.slee.resource.diameter.rf.events.avp.DrmContent)
   */
  public void setDrmContent( DrmContent drmContent ) {
    addAvp(DiameterRfAvpCodes.DRM_CONTENT, DiameterRfAvpCodes.TGPP_VENDOR_ID, drmContent.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setMessageClass(net.java.slee.resource.diameter.rf.events.avp.MessageClass)
   */
  public void setMessageClass( MessageClass messageClass ) {
    addAvp(DiameterRfAvpCodes.MESSAGE_CLASS, DiameterRfAvpCodes.TGPP_VENDOR_ID, messageClass.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setMessageId(String)
   */
  public void setMessageId( String messageId ) {
    addAvp(DiameterRfAvpCodes.MESSAGE_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, messageId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setMessageSize(long)
   */
  public void setMessageSize( long messageSize ) {
    addAvp(DiameterRfAvpCodes.MESSAGE_SIZE, DiameterRfAvpCodes.TGPP_VENDOR_ID, messageSize);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setMessageType(net.java.slee.resource.diameter.rf.events.avp.MessageType)
   */
  public void setMessageType( MessageType messageType ) {
    addAvp(DiameterRfAvpCodes.MESSAGE_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, messageType.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setMmContentType(net.java.slee.resource.diameter.rf.events.avp.MmContentType)
   */
  public void setMmContentType( MmContentType mmContentType ) {
    addAvp(DiameterRfAvpCodes.MM_CONTENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, mmContentType.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setOriginatorAddress(net.java.slee.resource.diameter.rf.events.avp.OriginatorAddress)
   */
  public void setOriginatorAddress( OriginatorAddress originatorAddress ) {
    addAvp(DiameterRfAvpCodes.ORIGINATOR_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, originatorAddress.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setPriority(net.java.slee.resource.diameter.rf.events.avp.Priority)
   */
  public void setPriority( Priority priority ) {
    addAvp(DiameterRfAvpCodes.PRIORITY, DiameterRfAvpCodes.TGPP_VENDOR_ID, priority.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setReadReplyReportRequested(net.java.slee.resource.diameter.rf.events.avp.ReadReplyReportRequested)
   */
  public void setReadReplyReportRequested( ReadReplyReportRequested readReplyReportRequested ) {
    addAvp(DiameterRfAvpCodes.READ_REPLY_REPORT_REQUESTED, DiameterRfAvpCodes.TGPP_VENDOR_ID, readReplyReportRequested.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setRecipientAddress(net.java.slee.resource.diameter.rf.events.avp.RecipientAddress)
   */
  public void setRecipientAddress( RecipientAddress recipientAddress ) {
    addAvp(DiameterRfAvpCodes.RECIPIENT_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, recipientAddress.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setRecipientAddresses(net.java.slee.resource.diameter.rf.events.avp.RecipientAddress[])
   */
  public void setRecipientAddresses( RecipientAddress[] recipientAddresses ) {
    for(RecipientAddress recipientAddress : recipientAddresses) {
      setRecipientAddress(recipientAddress);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setReplyApplicId(String)
   */
  public void setReplyApplicId( String replyApplicId ) {
    addAvp(DiameterRfAvpCodes.REPLY_APPLIC_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, replyApplicId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setSubmissionTime(java.util.Date)
   */
  public void setSubmissionTime( Date submissionTime ) {
    addAvp(DiameterRfAvpCodes.SUBMISSION_TIME, DiameterRfAvpCodes.TGPP_VENDOR_ID, submissionTime);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setVasId(String)
   */
  public void setVasId( String vasId ) {
    addAvp(DiameterRfAvpCodes.VAS_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, vasId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmsInformation#setVaspId(String)
   */
  public void setVaspId( String vaspId ) {
    addAvp(DiameterRfAvpCodes.VASP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, vaspId);
  }

}
