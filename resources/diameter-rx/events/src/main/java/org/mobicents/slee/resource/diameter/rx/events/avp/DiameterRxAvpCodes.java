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

package org.mobicents.slee.resource.diameter.rx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpType;

/**
 * 3GPP IMS Rx Reference Point AVP Codes
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class DiameterRxAvpCodes {

  public static final long TGPP_VENDOR_ID = 10415L;
  public static final long ETSI_VENDOR_ID = 13019L;
  public static final long RX_APPLICATION_ID = 16777236L;

  private DiameterRxAvpCodes() {
  }

  /**
   * RX Re-Used AVPs
   */
  public static final int TGPP_GPRS_NEGOTIATED_QOS_PROFILE = 5;
  public static final DiameterAvpType TGPP_GPRS_NEGOTIATED_QOS_PROFILE_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int TGPP_SGSN_ADDRESS = 6;
  public static final DiameterAvpType TGPP_SGSN_ADDRESS_AVP_TYPE = DiameterAvpType.fromString("UTF8String");

  public static final int FRAMED_IP_ADDRESS = 8;
  public static final DiameterAvpType FRAMED_IP_ADDRESS_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int TGPP_SGSN_IPv6_ADDRESS = 15;
  public static final DiameterAvpType TGPP_SGSN_IPv6_ADDRESS_AVP_TYPE = DiameterAvpType.fromString("UTF8String");

  public static final int TGPP_SGSN_MCC_MNC = 18;
  public static final DiameterAvpType TGPP_SGSN_MCC_MNC_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int TGPP_RAT_TYPE = 21;
  public static final DiameterAvpType TGPP_RAT_TYPE_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int CALLED_STATION_ID = 30;
  public static final DiameterAvpType CALLED_STATION_ID_AVP_TYPE = DiameterAvpType.fromString("UTF8String");

  public static final int FRAMED_IPV6_PREFIX = 97;
  public static final DiameterAvpType FRAMED_IPV6_PREFIX_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int ETSI_RESERVATION_PRIORITY = 458;
  public static final DiameterAvpType ETSI_RESERVATION_PRIORITY_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int ABORT_CAUSE = 500;
  public static final DiameterAvpType ABORT_CAUSE_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int ACCESS_NETWORK_CHARGING_ADDRESS = 501;
  public static final DiameterAvpType ACCESS_NETWORK_CHARGING_ADDRESS_AVP_TYPE = DiameterAvpType.fromString("Address");

  public static final int ACCESS_NETWORK_CHARGING_IDENTIFIER = 502;
  public static final DiameterAvpType ACCESS_NETWORK_CHARGING_IDENTIFIER_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int ACCESS_NETWORK_CHARGING_IDENTIFIER_VALUE = 503;
  public static final DiameterAvpType ACCESS_NETWORK_CHARGING_IDENTIFIER_VALUE_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int AF_APPLICATION_IDENTIFIER = 504;
  public static final DiameterAvpType AF_APPLICATION_IDENTIFIER_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int AF_CHARGING_IDENTIFIER = 505;
  public static final DiameterAvpType AF_CHARGING_IDENTIFIER_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int FLOW_DESCRIPTION = 507;
  public static final DiameterAvpType FLOW_DESCRIPTION_AVP_TYPE = DiameterAvpType.fromString("IPFilterRule");

  public static final int FLOW_NUMBER   = 509;
  public static final DiameterAvpType FLOW_NUMBER_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int FLOWS = 510;
  public static final DiameterAvpType FLOWS_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int FLOW_STATUS = 511;
  public static final DiameterAvpType FLOW_STATUS_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int FLOW_USAGE = 512;
  public static final DiameterAvpType FLOW_USAGE_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int SPECIFIC_ACTION = 513;
  public static final DiameterAvpType SPECIFIC_ACTION_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int MAX_REQUESTED_BANDWIDTH_DL = 515;
  public static final DiameterAvpType MAX_REQUESTED_BANDWIDTH_DL_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int MAX_REQUESTED_BANDWIDTH_UL = 516;
  public static final DiameterAvpType MAX_REQUESTED_BANDWIDTH_UL_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int MEDIA_COMPONENT_DESCRIPTION = 517;
  public static final DiameterAvpType MEDIA_COMPONENT_DESCRIPTION_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int MEDIA_COMPONENT_NUMBER   = 518;
  public static final DiameterAvpType MEDIA_COMPONENT_NUMBER_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int MEDIA_SUBCOMPONENT = 519;
  public static final DiameterAvpType MEDIA_SUBCOMPONENT_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int MEDIA_TYPE = 520;
  public static final DiameterAvpType MEDIA_TYPE_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int RR_BANDWIDTH = 521;
  public static final DiameterAvpType RR_BANDWIDTH_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int RS_BANDWIDTH = 522;
  public static final DiameterAvpType RS_BANDWIDTH_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int SIP_FORKING_INDICATION = 523;
  public static final DiameterAvpType SIP_FORKING_INDICATION_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int CODEC_DATA = 524;
  public static final DiameterAvpType CODEC_DATA_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int SERVICE_URN = 525;
  public static final DiameterAvpType SERVICE_URN_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int ACCEPTABLE_SERVICE_INFO = 526;
  public static final DiameterAvpType ACCEPTABLE_SERVICE_INFO_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int SERVICE_INFO_STATUS = 527;
  public static final DiameterAvpType SERVICE_INFO_STATUS_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int MPS_IDENTIFIER = 528;
  public static final DiameterAvpType MPS_IDENTIFIER_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int AF_SIGNALLING_PROTOCOL = 529;
  public static final DiameterAvpType AF_SIGNALLING_PROTOCOL_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int SPONSORED_CONNECTIVITY_DATA = 530;
  public static final DiameterAvpType SPONSORED_CONNECTIVITY_DATA_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int SPONSOR_IDENTITY = 531;
  public static final DiameterAvpType SPONSOR_IDENTITY_AVP_TYPE = DiameterAvpType.fromString("UTF8String");

  public static final int APPLICATION_SERVICE_PROVIDER_IDENTITY = 532;
  public static final DiameterAvpType APPLICATION_SERVICE_PROVIDER_IDENTITY_AVP_TYPE = DiameterAvpType.fromString("UTF8String");

  public static final int RX_REQUEST_TYPE = 532;
  public static final DiameterAvpType RX_REQUEST_TYPE_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int CHARGING_INFORMATION = 618;
  public static final DiameterAvpType CHARGING_INFORMATION_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int PRIMARY_EVENT_CHARGING_FUNCTION_NAME = 619;
  public static final DiameterAvpType PRIMARY_EVENT_CHARGING_FUNCTION_NAME_AVP_TYPE = DiameterAvpType.fromString("DiameterURI");

  public static final int SECONDARY_EVENT_CHARGING_FUNCTION_NAME = 620;
  public static final DiameterAvpType SECONDARY_EVENT_CHARGING_FUNCTION_NAME_AVP_TYPE = DiameterAvpType.fromString("DiameterURI");

  public static final int PRIMARY_CHARGING_COLLECTION_FUNCTION_NAME = 621;
  public static final DiameterAvpType PRIMARY_CHARGING_COLLECTION_FUNCTION_NAME_AVP_TYPE = DiameterAvpType.fromString("DiameterURI");

  public static final int SECONDARY_CHARGING_COLLECTION_FUNCTION_NAME = 622;
  public static final DiameterAvpType SECONDARY_CHARGING_COLLECTION_FUNCTION_NAME_AVP_TYPE = DiameterAvpType.fromString("DiameterURI");

  public static final int SUPPORTED_FEATURES = 628;
  public static final DiameterAvpType SUPPORTED_FEATURES_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int FEATURE_LIST_ID = 629;
  public static final DiameterAvpType FEATURE_LIST_ID_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int FEATURE_LIST = 630;
  public static final DiameterAvpType FEATURE_LIST_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int BEARER_USAGE = 1000;
  public static final DiameterAvpType BEARER_USAGE_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int CHARGING_RULE_INSTALL = 1001;
  public static final DiameterAvpType CHARGING_RULE_INSTALL_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int CHARGING_RULE_REMOVE = 1002;
  public static final DiameterAvpType CHARGING_RULE_REMOVE_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int CHARGING_RULE_DEFINITION = 1003;
  public static final DiameterAvpType CHARGING_RULE_DEFINITION_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int CHARGING_RULE_BASE_NAME = 1004;
  public static final DiameterAvpType CHARGING_RULE_BASE_NAME_AVP_TYPE = DiameterAvpType.fromString("UTF8String");

  public static final int CHARGING_RULE_NAME = 1005;
  public static final DiameterAvpType CHARGING_RULE_NAME_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  public static final int EVENT_TRIGGER = 1006;
  public static final DiameterAvpType EVENT_TRIGGER_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int METERING_METHOD = 1007;
  public static final DiameterAvpType METERING_METHOD_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int OFFLINE = 1008;
  public static final DiameterAvpType OFFLINE_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int ONLINE = 1009;
  public static final DiameterAvpType ONLINE_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int PRECEDENCE = 1010;
  public static final DiameterAvpType PRECEDENCE_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  public static final int REPORTING_LEVEL = 1011;
  public static final DiameterAvpType REPORTING_LEVEL_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int PDP_SESSION_OPERATION = 1015;
  public static final DiameterAvpType PDP_SESSION_OPERATION_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int TFT_FILTER = 1012;
  public static final DiameterAvpType TFT_FILTER_AVP_TYPE = DiameterAvpType.fromString("IPFileterRule");

  public static final int TFT_PACKET_FILTER_INFORMATION = 1013;
  public static final DiameterAvpType TFT_PACKET_FILTER_INFORMATION_AVP_TYPE = DiameterAvpType.fromString("Grouped");

  public static final int ToS_TRAFFIC_CLASS = 1014;
  public static final DiameterAvpType ToS_TRAFFIC_CLASS_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  //Comes from TS 29.212

  public static final int IP_CAN_TYPE = 1027;
  public static final DiameterAvpType IP_CAN_TYPE_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  public static final int RAT_TYPE = 1032;
  public static final DiameterAvpType RAT_TYPE_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

}
