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

package net.java.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the MMS-Information grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.65 MMS-Information AVP 
 * The MMS-Information AVP (AVP code 877) is of type Grouped. Its purpose is to allow the transmission of additional 
 * MMS service specific information elements. 
 * 
 * It has the following ABNF grammar: (Note: the *[ AVP ] is not part of 3GPP TS 32.299, it was added to allow for more
 * flexibility for extensions to Diameter Ro.) 
 *  MMS-Information ::= AVP Header: 877 
 *      [ Originator-Address ] 
 *    * [ Recipient-Address ] 
 *      [ Submission-Time ] 
 *      [ MM-Content-Type ] 
 *      [ Priority ] 
 *      [ Message-ID ] 
 *      [ Message-Type ] 
 *      [ Message-Size ] 
 *      [ Message-Class ] 
 *      [ Delivery-Report-Requested ] 
 *      [ Read-Reply-Report-Requested ] 
 *      [ MMBox-Storage-Information ] #exclude 
 *      [ Applic-ID ] 
 *      [ Reply-Applic-ID ] 
 *      [ Aux-Applic-Info ] 
 *      [ Content-Class ] 
 *      [ DRM-Content ] 
 *      [ Adaptations ] 
 *      [ VASP-Id ] 
 *      [ VAS-Id ] 
 *     *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface MmsInformation extends GroupedAvp {

  /**
   * Returns the value of the Adaptations AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract Adaptations getAdaptations();

  /**
   * Returns the value of the Applic-ID AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getApplicId();

  /**
   * Returns the value of the Aux-Applic-Info AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getAuxApplicInfo();

  /**
   * Returns the value of the Content-Class AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract ContentClass getContentClass();

  /**
   * Returns the value of the Delivery-Report-Requested AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract DeliveryReportRequested getDeliveryReportRequested();

  /**
   * Returns the value of the DRM-Content AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract DrmContent getDrmContent();

  /**
   * Returns the set of extension AVPs. The returned array contains the extension AVPs in the order they appear in the message. A return value of null implies that no extensions AVPs have been set.
   */
  abstract DiameterAvp[] getExtensionAvps();

  /**
   * Returns the value of the Message-Class AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract MessageClass getMessageClass();

  /**
   * Returns the value of the Message-ID AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getMessageId();

  /**
   * Returns the value of the Message-Size AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getMessageSize();

  /**
   * Returns the value of the Message-Type AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract MessageType getMessageType();

  /**
   * Returns the value of the MM-Content-Type AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract MmContentType getMmContentType();

  /**
   * Returns the value of the Originator-Address AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract OriginatorAddress getOriginatorAddress();

  /**
   * Returns the value of the Priority AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract Priority getPriority();

  /**
   * Returns the value of the Read-Reply-Report-Requested AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract ReadReplyReportRequested getReadReplyReportRequested();

  /**
   * Returns the set of Recipient-Address AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no Recipient-Address AVPs have been set. The elements in the given array are RecipientAddress objects.
   */
  abstract RecipientAddress[] getRecipientAddresses();

  /**
   * Returns the value of the Reply-Applic-ID AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getReplyApplicId();

  /**
   * Returns the value of the Submission-Time AVP, of type Time. A return value of null implies that the AVP has not been set.
   */
  abstract java.util.Date getSubmissionTime();

  /**
   * Returns the value of the VAS-Id AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getVasId();

  /**
   * Returns the value of the VASP-Id AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getVaspId();

  /**
   * Returns true if the Adaptations AVP is present in the message.
   */
  abstract boolean hasAdaptations();

  /**
   * Returns true if the Applic-ID AVP is present in the message.
   */
  abstract boolean hasApplicId();

  /**
   * Returns true if the Aux-Applic-Info AVP is present in the message.
   */
  abstract boolean hasAuxApplicInfo();

  /**
   * Returns true if the Content-Class AVP is present in the message.
   */
  abstract boolean hasContentClass();

  /**
   * Returns true if the Delivery-Report-Requested AVP is present in the message.
   */
  abstract boolean hasDeliveryReportRequested();

  /**
   * Returns true if the DRM-Content AVP is present in the message.
   */
  abstract boolean hasDrmContent();

  /**
   * Returns true if the Message-Class AVP is present in the message.
   */
  abstract boolean hasMessageClass();

  /**
   * Returns true if the Message-ID AVP is present in the message.
   */
  abstract boolean hasMessageId();

  /**
   * Returns true if the Message-Size AVP is present in the message.
   */
  abstract boolean hasMessageSize();

  /**
   * Returns true if the Message-Type AVP is present in the message.
   */
  abstract boolean hasMessageType();

  /**
   * Returns true if the MM-Content-Type AVP is present in the message.
   */
  abstract boolean hasMmContentType();

  /**
   * Returns true if the Originator-Address AVP is present in the message.
   */
  abstract boolean hasOriginatorAddress();

  /**
   * Returns true if the Priority AVP is present in the message.
   */
  abstract boolean hasPriority();

  /**
   * Returns true if the Read-Reply-Report-Requested AVP is present in the message.
   */
  abstract boolean hasReadReplyReportRequested();

  /**
   * Returns true if the Reply-Applic-ID AVP is present in the message.
   */
  abstract boolean hasReplyApplicId();

  /**
   * Returns true if the Submission-Time AVP is present in the message.
   */
  abstract boolean hasSubmissionTime();

  /**
   * Returns true if the VAS-Id AVP is present in the message.
   */
  abstract boolean hasVasId();

  /**
   * Returns true if the VASP-Id AVP is present in the message.
   */
  abstract boolean hasVaspId();

  /**
   * Sets the value of the Adaptations AVP, of type Enumerated.
   */
  abstract void setAdaptations(Adaptations adaptations);

  /**
   * Sets the value of the Applic-ID AVP, of type UTF8String.
   */
  abstract void setApplicId(String applicId);

  /**
   * Sets the value of the Aux-Applic-Info AVP, of type UTF8String.
   */
  abstract void setAuxApplicInfo(String auxApplicInfo);

  /**
   * Sets the value of the Content-Class AVP, of type Enumerated.
   */
  abstract void setContentClass(ContentClass contentClass);

  /**
   * Sets the value of the Delivery-Report-Requested AVP, of type Enumerated.
   */
  abstract void setDeliveryReportRequested(DeliveryReportRequested deliveryReportRequested);

  /**
   * Sets the value of the DRM-Content AVP, of type Enumerated.
   */
  abstract void setDrmContent(DrmContent drmContent);

  /**
   * Sets the set of extension AVPs with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getExtensionAvps() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setExtensionAvps(DiameterAvp[] avps) throws AvpNotAllowedException;

  /**
   * Sets the value of the Message-Class AVP, of type Grouped.
   */
  abstract void setMessageClass(MessageClass messageClass);

  /**
   * Sets the value of the Message-ID AVP, of type UTF8String.
   */
  abstract void setMessageId(String messageId);

  /**
   * Sets the value of the Message-Size AVP, of type Unsigned32.
   */
  abstract void setMessageSize(long messageSize);

  /**
   * Sets the value of the Message-Type AVP, of type Enumerated.
   */
  abstract void setMessageType(MessageType messageType);

  /**
   * Sets the value of the MM-Content-Type AVP, of type Grouped.
   */
  abstract void setMmContentType(MmContentType mmContentType);

  /**
   * Sets the value of the Originator-Address AVP, of type Grouped.
   */
  abstract void setOriginatorAddress(OriginatorAddress originatorAddress);

  /**
   * Sets the value of the Priority AVP, of type Enumerated.
   */
  abstract void setPriority(Priority priority);

  /**
   * Sets the value of the Read-Reply-Report-Requested AVP, of type Enumerated.
   */
  abstract void setReadReplyReportRequested(ReadReplyReportRequested readReplyReportRequested);

  /**
   * Sets a single Recipient-Address AVP in the message, of type Grouped.
   */
  abstract void setRecipientAddress(RecipientAddress recipientAddress);

  /**
   * Sets the set of Recipient-Address AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getRecipientAddresses() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setRecipientAddresses(RecipientAddress[] recipientAddresses);

  /**
   * Sets the value of the Reply-Applic-ID AVP, of type UTF8String.
   */
  abstract void setReplyApplicId(String replyApplicId);

  /**
   * Sets the value of the Submission-Time AVP, of type Time.
   */
  abstract void setSubmissionTime(java.util.Date submissionTime);

  /**
   * Sets the value of the VAS-Id AVP, of type UTF8String.
   */
  abstract void setVasId(String vasId);

  /**
   * Sets the value of the VASP-Id AVP, of type UTF8String.
   */
  abstract void setVaspId(String vaspId);

}
