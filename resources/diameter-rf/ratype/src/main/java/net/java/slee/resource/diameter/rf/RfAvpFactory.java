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

package net.java.slee.resource.diameter.rf;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.rf.events.avp.*;

/**
 * Factory to support the creation of Grouped AVP instances.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface RfAvpFactory {

  public DiameterAvpFactory getBaseFactory();

  /**
   * Create an empty AdditionalContentInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public AdditionalContentInformation createAdditionalContentInformation();

  /**
   * Create an empty AddressDomain (Grouped AVP) instance.
   * 
   * @return
   */
  public AddressDomain createAddressDomain();

  /**
   * Create an empty ApplicationServerInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public ApplicationServerInformation createApplicationServerInformation();

  /**
   * Create an empty EventType (Grouped AVP) instance.
   * 
   * @return
   */
  public EventType createEventType();

  /**
   * Create an empty ImsInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public ImsInformation createImsInformation();

  /**
   * Create a ImsInformation (Grouped AVP) instance using required AVP values.
   * 
   * @param nodeFunctionality
   * @return
   */
  public ImsInformation createImsInformation(NodeFunctionality nodeFunctionality);

  /**
   * Create an empty InterOperatorIdentifier (Grouped AVP) instance.
   * 
   * @return
   */
  public InterOperatorIdentifier createInterOperatorIdentifier();

  /**
   * Create an empty LcsClientId (Grouped AVP) instance.
   * 
   * @return
   */
  public LcsClientId createLcsClientId();

  /**
   * Create an empty LcsClientName (Grouped AVP) instance.
   * 
   * @return
   */
  public LcsClientName createLcsClientName();

  /**
   * Create an empty LcsInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public LcsInformation createLcsInformation();

  /**
   * Create an empty LcsRequestorId (Grouped AVP) instance.
   * 
   * @return
   */
  public LcsRequestorId createLcsRequestorId();

  /**
   * Create an empty LocationType (Grouped AVP) instance.
   * 
   * @return
   */
  public LocationType createLocationType();

  /**
   * Create an empty MbmsInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public MbmsInformation createMbmsInformation();

  /**
   * Create a MbmsInformation (Grouped AVP) instance using required AVP
   * values.
   * 
   * @param tmgi
   * @param mbmsServiceType
   * @param mbmsUserServiceType
   * @return
   */
  public MbmsInformation createMbmsInformation(byte[]  tmgi, MbmsServiceType mbmsServiceType, MbmsUserServiceType mbmsUserServiceType);

  /**
   * Create an empty MessageBody (Grouped AVP) instance.
   * 
   * @return
   */
  public MessageBody createMessageBody();

  /**
   * Create an empty MessageClass (Grouped AVP) instance.
   * 
   * @return
   */
  public MessageClass createMessageClass();

  /**
   * Create an empty MmContentType (Grouped AVP) instance.
   * 
   * @return
   */
  public MmContentType createMmContentType();

  /**
   * Create an empty MmsInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public MmsInformation createMmsInformation();

  /**
   * Create an empty OriginatorAddress (Grouped AVP) instance.
   * 
   * @return
   */
  public OriginatorAddress createOriginatorAddress();

  /**
   * Create an empty PocInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public PocInformation createPocInformation();

  /**
   * Create an empty PsFurnishChargingInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public PsFurnishChargingInformation createPsFurnishChargingInformation();

  /**
   * Create a PsFurnishChargingInformation (Grouped AVP) instance using
   * required AVP values.
   * 
   * @param tgppChargingId
   * @param psFreeFormatData
   * @return
   */
  public PsFurnishChargingInformation createPsFurnishChargingInformation(byte[] tgppChargingId, byte[] psFreeFormatData);

  /**
   * Create an empty PsInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public PsInformation createPsInformation();

  /**
   * Create an empty RecipientAddress (Grouped AVP) instance.
   * 
   * @return
   */
  public RecipientAddress createRecipientAddress();

  /**
   * Create an empty SdpMediaComponent (Grouped AVP) instance.
   * 
   * @return
   */
  public SdpMediaComponent createSdpMediaComponent();

  /**
   * Create an empty ServiceInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public ServiceInformation createServiceInformation();

  /**
   * Create an empty TalkBurstExchange (Grouped AVP) instance.
   * 
   * @return
   */
  public TalkBurstExchange createTalkBurstExchange();

  /**
   * Create an empty TimeStamps (Grouped AVP) instance.
   * 
   * @return
   */
  public TimeStamps createTimeStamps();

  /**
   * Create an empty TrunkGroupId (Grouped AVP) instance.
   * 
   * @return
   */
  public TrunkGroupId createTrunkGroupId();

  /**
   * Create an empty WlanInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public WlanInformation createWlanInformation();

  /**
   * Create an empty WlanRadioContainer (Grouped AVP) instance.
   * 
   * @return
   */
  public WlanRadioContainer createWlanRadioContainer();

}