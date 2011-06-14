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

package org.mobicents.slee.resource.diameter.gq.events.avp;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpType;

/**
 * Diameter Gq Avp Codes
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class DiameterGqAvpCodes {

  public static final long TGPP_VENDOR_ID = 10415L;
  public static final long ETSI_VENDOR_ID = 13019L;

  // defined in table 7.3.1 - ETSI
  public static final int ETSI_BINDING_INFORMATION = 450;
  public static final DiameterAvpType ETSI_BINDING_INFORMATION_AVP_TYPE = DiameterAvpType.fromString("Grouped");
  public static final int ETSI_BINDING_INPUT_LIST = 451;
  public static final DiameterAvpType ETSI_BINDING_INPUT_LIST_AVP_TYPE = DiameterAvpType.fromString("Grouped");
  public static final int ETSI_BINDING_OUTPUT_LIST = 452;
  public static final DiameterAvpType ETSI_BINDING_OUTPUT_LIST_AVP_TYPE = DiameterAvpType.fromString("Grouped");
  public static final int ETSI_V6_TRANSPORT_ADDRESS = 453;
  public static final DiameterAvpType ETSI_V6_TRANSPORT_ADDRESS_AVP_TYPE = DiameterAvpType.fromString("Grouped");
  public static final int ETSI_V4_TRANSPORT_ADDRESS = 454;
  public static final DiameterAvpType ETSI_V4_TRANSPORT_ADDRESS_AVP_TYPE = DiameterAvpType.fromString("Grouped");
  public static final int ETSI_PORT_NUMBER = 455;
  public static final DiameterAvpType ETSI_PORT_NUMBER_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");
  public static final int ETSI_RESERVATION_CLASS = 456;
  public static final DiameterAvpType ETSI_RESERVATION_CLASS_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");
  public static final int ETSI_LATCHING_INDICATION = 457;
  public static final DiameterAvpType ETSI_LATCHING_INDICATION_AVP_TYPE = DiameterAvpType.fromString("Enumerated");
  public static final int ETSI_RESERVATION_PRIORITY = 458;
  public static final DiameterAvpType ETSI_RESERVATION_PRIORITY_AVP_TYPE = DiameterAvpType.fromString("Enumerated");
  public static final int ETSI_SERVICE_CLASS = 459;
  public static final DiameterAvpType ETSI_SERVICE_CLASS_AVP_TYPE = DiameterAvpType.fromString("UTF8String");
  public static final int ETSI_OVERBOOKING_INDICATOR = 460;
  public static final DiameterAvpType ETSI_OVERBOOKING_INDICATOR_AVP_TYPE = DiameterAvpType.fromString("Enumerated");
  public static final int ETSI_AUTHORIZATION_PACKAGE_ID = 461;
  public static final DiameterAvpType ETSI_AUTHORIZATION_PACKAGE_ID_AVP_TYPE = DiameterAvpType.fromString("UTF8String");
  public static final int ETSI_MEDIA_AUTHORIZATION_CONTEXT_ID = 462;
  public static final DiameterAvpType ETSI_MEDIA_AUTHORIZATION_CONTEXT_ID_AVP_TYPE = DiameterAvpType.fromString("UTF8String");

  // defined in table 7.3.2 - ETSI
  public static final int ETSI_GLOBALLY_UNIQUE_ADDRESS = 300;
  public static final DiameterAvpType ETSI_GLOBALLY_UNIQUE_ADDRESS_AVP_TYPE = DiameterAvpType.fromString("Grouped");
  public static final int ETSI_ADRESS_REALM = 301;
  public static final DiameterAvpType ETSI_ADRESS_REALM_AVP_TYPE = DiameterAvpType.fromString("OctetString");
  public static final int ETSI_LOGICAL_ACCESS_ID = 302;
  public static final DiameterAvpType ETSI_LOGICAL_ACCESS_ID_AVP_TYPE = DiameterAvpType.fromString("OctetString");
  public static final int ETSI_TRANSPORT_CLASS = 311;
  public static final DiameterAvpType ETSI_TRANSPORT_CLASS_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");

  // defined in table 7.3.3 - 3GPP
  public static final int TGPP_ABORT_CAUSE = 500;
  public static final DiameterAvpType TGPP_ABORT_CAUSE_AVP_TYPE = DiameterAvpType.fromString("Enumerated");
  public static final int TGPP_AF_APPLICATION_IDENTIFIER = 504;
  public static final DiameterAvpType TGPP_AF_APPLICATION_IDENTIFIER_AVP_TYPE = DiameterAvpType.fromString("OctetString");
  public static final int TGPP_AF_CHARGING_IDENTIFIER = 505;
  public static final DiameterAvpType TGPP_AF_CHARGING_AVP_TYPE = DiameterAvpType.fromString("OctetString");
  public static final int TGPP_FLOW_DESCRIPTION = 507;
  public static final DiameterAvpType TGPP_FLOW_DESCRIPTION_AVP_TYPE = DiameterAvpType.fromString("IPFilterRule");
  public static final int TGPP_FLOW_GROUPING = 508;
  public static final DiameterAvpType TGPP_FLOW_GROUPING_AVP_TYPE = DiameterAvpType.fromString("Grouped");
  public static final int TGPP_FLOW_NUMBER = 509;
  public static final DiameterAvpType TGPP_FLOW_NUMBER_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");
  public static final int TGPP_FLOWS = 510;
  public static final DiameterAvpType TGPP_FLOWS_AVP_TYPE = DiameterAvpType.fromString("Grouped");
  public static final int TGPP_FLOW_STATUS = 511;
  public static final DiameterAvpType TGPP_FLOW_STATUS_AVP_TYPE = DiameterAvpType.fromString("Enumerated");
  public static final int TGPP_FLOW_USAGE = 512;
  public static final DiameterAvpType TGPP_FLOW_USAGE_AVP_TYPE = DiameterAvpType.fromString("Enumerated");
  public static final int TGPP_SPECIFIC_ACTION = 513;
  public static final DiameterAvpType TGPP_SPECIFIC_ACTION_AVP_TYPE = DiameterAvpType.fromString("Enumerated");
  public static final int TGPP_MAX_REQUESTED_BANDWIDTH_DL = 515;
  public static final DiameterAvpType TGPP_MAX_REQUESTED_BANDWIDTH_DL_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");
  public static final int TGPP_MAX_REQUESTED_BANDWIDTH_UL = 516;
  public static final DiameterAvpType TGPP_MAX_REQUESTED_BANDWIDTH_UL_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");
  public static final int TGPP_MEDIA_COMPONENT_DESCRIPTION = 517;
  public static final DiameterAvpType TGPP_MEDIA_COMPONENT_DESCRIPTION_TYPE = DiameterAvpType.fromString("Grouped");
  public static final int TGPP_MEDIA_COMPONENT_NUMBER = 518;
  public static final DiameterAvpType TGPP_MEDIA_COMPONENT_NUMBER_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");
  public static final int TGPP_MEDIA_SUB_COMPONENT = 519;
  public static final DiameterAvpType TGPP_MEDIA_SUB_COMPONENT_AVP_TYPE = DiameterAvpType.fromString("Grouped");
  public static final int TGPP_MEDIA_TYPE = 520;
  public static final DiameterAvpType TGPP_MEDIA_TYPE_AVP_TYPE = DiameterAvpType.fromString("Enumerated");
  public static final int TGPP_RR_BANDWIDTH = 521;
  public static final DiameterAvpType TGPP_RR_BANDWIDTH_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");
  public static final int TGPP_RS_BANDWIDTH = 522;
  public static final DiameterAvpType TGPP_RS_BANDWIDTH_AVP_TYPE = DiameterAvpType.fromString("Unsigned32");
  public static final int TGPP_SIP_FORKING_INDICATION = 523;
  public static final DiameterAvpType TGPP_SIP_FORKING_INDICATION_AVP_TYPE = DiameterAvpType.fromString("Enumerated");

  // defined in table 7.3.4 - defined for the NASREQ,No Vendor-Id
  public static final int FRAMED_IP_ADDRESS = 8;
  public static final DiameterAvpType FRAMED_IP_ADDRESS_AVP_TYPE = DiameterAvpType.fromString("OctetString");
  public static final int FRAMED_IPV6_PREFIX = 97;
  public static final DiameterAvpType FRAMED_IPV6_PREFIX_AVP_TYPE = DiameterAvpType.fromString("OctetString");

  // defined in table 7.3.5 - 3GPP
  public static final int TGPP_CODEC_DATA = 524;
  public static final DiameterAvpType TGPP_CODEC_DATA_AVP_TYPE = DiameterAvpType.fromString("OctetString");
}
