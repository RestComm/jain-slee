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

package net.java.slee.resource.diameter.s6a.events.avp;

/**
 * Diameter S6a AVP Codes
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class DiameterS6aAvpCodes {

  public static final long S6A_VENDOR_ID = 10415L;
  public static final long S6A_AUTH_APP_ID = 16777251L;

  // S6a/S6d Specific
  public static final int SUBSCRIPTION_DATA = 1400;
  public static final int TERMINAL_INFORMATION = 1401;
  public static final int IMEI = 1402;
  public static final int SOFTWARE_VERSION = 1403;
  public static final int QOS_SUBSCRIBED = 1404;
  public static final int ULR_FLAGS = 1405;
  public static final int ULA_FLAGS = 1406;
  public static final int VISITED_PLMN_ID = 1407;
  public static final int REQUESTED_EUTRAN_AUTHENTICATION_INFO = 1408;
  public static final int REQUESTED_UTRAN_GERAN_AUTHENTICATION_INFO = 1409;
  public static final int NUMBER_OF_REQUESTED_VECTORS = 1410;
  public static final int RE_SYNCHRONIZATION_INFO = 1411;
  public static final int IMMEDIATE_RESPONSE_PREFERRED = 1412;
  public static final int AUTHENTICATION_INFO = 1413;
  public static final int E_UTRAN_VECTOR = 1414;
  public static final int UTRAN_VECTOR = 1415;
  public static final int GERAN_VECTOR = 1416;
  public static final int NETWORK_ACCESS_MODE = 1417;
  public static final int HPLMN_ODB = 1418;
  public static final int ITEM_NUMBER = 1419;
  public static final int CANCELLATION_TYPE = 1420;
  public static final int DSR_FLAGS = 1421;
  public static final int DSA_FLAGS = 1422;
  public static final int CONTEXT_IDENTIFIER = 1423;
  public static final int SUBSCRIBER_STATUS = 1424;
  public static final int OPERATOR_DETERMINED_BARRING = 1425;
  public static final int ACCESS_RESTRICTION_DATA = 1426;
  public static final int APN_OI_REPLACEMENT = 1427;
  public static final int ALL_APN_CONFIGURATIONS_INCLUDED_INDICATOR = 1428;
  public static final int APN_CONFIGURATION_PROFILE = 1429;
  public static final int APN_CONFIGURATION = 1430;
  public static final int EPS_SUBSCRIBED_QOS_PROFILE = 1431;
  public static final int VPLMN_DYNAMIC_ADDRESS_ALLOWED = 1432;
  public static final int STN_SR = 1433;
  public static final int ALERT_REASON = 1434;
  public static final int AMBR = 1435;
  public static final int CSG_SUBSCRIPTION_DATA = 1436;
  public static final int CSG_ID = 1437;
  public static final int PDN_GW_ALLOCATION_TYPE = 1438;
  public static final int EXPIRATION_DATE = 1439;
  public static final int RAT_FREQUENCY_SELECTION_PRIORITY_ID = 1440;
  public static final int IDA_FLAGS = 1441;
  public static final int PUA_FLAGS = 1442;
  public static final int NOR_FLAGS = 1443;
  public static final int USER_ID = 1444;
  public static final int EQUIPMENT_STATUS = 1445;
  public static final int REGIONAL_SUBSCRIPTION_ZONE_CODE = 1446;
  public static final int RAND = 1447;
  public static final int XRES = 1448;
  public static final int AUTN = 1449;
  public static final int KASME = 1450;
  public static final int TRACE_COLLECTION_ENTITY = 1452;
  public static final int KC = 1453;
  public static final int SRES = 1454;
  public static final int PDN_TYPE = 1456;
  public static final int ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE = 1457;
  public static final int TRACE_DATA = 1458;
  public static final int TRACE_REFERENCE = 1459;
  public static final int TRACE_DEPTH = 1462;
  public static final int TRACE_NE_TYPE_LIST = 1463;
  public static final int TRACE_INTERFACE_LIST = 1464;
  public static final int TRACE_EVENT_LIST = 1465;
  public static final int OMC_ID = 1466;
  public static final int GPRS_SUBSCRIPTION_DATA = 1467;
  public static final int COMPLETE_DATA_LIST_INCLUDED_INDICATOR = 1468;
  public static final int PDP_CONTEXT = 1469;
  public static final int PDP_TYPE = 1470;
  public static final int TGPP2_MEID = 1471;
  public static final int SPECIFIC_APN_INFO = 1472;
  public static final int LCS_INFO = 1473;
  public static final int GMLC_NUMBER = 1474;
  public static final int LCS_PRIVACYEXCEPTION = 1475;
  public static final int SS_CODE = 1476;
  public static final int SS_STATUS = 1477;
  public static final int NOTIFICATION_TO_UE_USER = 1478;
  public static final int EXTERNAL_CLIENT = 1479;
  public static final int CLIENT_IDENTITY = 1480;
  public static final int GMLC_RESTRICTION = 1481;
  public static final int PLMN_CLIENT = 1482;
  public static final int SERVICE_TYPE = 1483;
  public static final int SERVICETYPEIDENTITY = 1484;
  public static final int MO_LR = 1485;
  public static final int TELESERVICE_LIST = 1486;
  public static final int TS_CODE = 1487;
  public static final int CALL_BARRING_INFOR_LIST = 1488;
  public static final int SGSN_NUMBER = 1489;
  public static final int IDR_FLAGS = 1490;
  public static final int ICS_INDICATOR = 1491;
  public static final int IMS_VOICE_OVER_PS_SESSIONS_SUPPORTED = 1492;
  public static final int HOMOGENEOUS_SUPPORT_OF_IMS_VOICE_OVER_PS_SESSIONS = 1493;
  public static final int LAST_UE_ACTIVITY_TIME = 1494;
  public static final int EPS_USER_STATE = 1495;
  public static final int EPS_LOCATION_INFORMATION = 1496;
  public static final int MME_USER_STATE = 1497;
  public static final int SGSN_USER_STATE = 1498;
  public static final int USER_STATE = 1499;
  public static final int MME_LOCATION_INFORMATION = 1600;
  public static final int SGSN_LOCATION_INFORMATION = 1601;
  public static final int E_UTRAN_CELL_GLOBAL_IDENTITY = 1602;
  public static final int TRACKING_AREA_IDENTITY = 1603;
  public static final int CELL_GLOBAL_IDENTITY = 1604;
  public static final int ROUTING_AREA_IDENTITY = 1605;
  public static final int LOCATION_AREA_IDENTITY = 1606;
  public static final int SERVICE_AREA_IDENTITY = 1607;
  public static final int GEOGRAPHICAL_INFORMATION = 1608;
  public static final int GEODETIC_INFORMATION = 1609;
  public static final int CURRENT_LOCATION_RETRIEVED = 1610;
  public static final int AGE_OF_LOCATION_INFORMATION = 1611;
  public static final int ACTIVE_APN = 1612;
  public static final int ERROR_DIAGNOSTIC = 1614;
  public static final int EXT_PDP_TYPE = 1620;
  public static final int EXT_PDP_ADDRESS = 1621;

  // Reused
  public static final int TGPP_CHARGING_CHARACTERISTICS = 13;
  public static final int MIP6_HOME_LINK_PREFIX = 125;
  public static final int DESTINATION_REALM = 283;
  public static final int DESTINATION_HOST = 293;
  public static final int MIP_HOME_AGENT_ADDRESS = 334;
  public static final int MIP_HOME_AGENT_HOST = 348;
  public static final int MIP6_AGENT_INFO = 486;
  public static final int SERVICE_SELECTION = 493;
  public static final int MAX_REQUESTED_BANDWIDTH_DL = 515;
  public static final int MAX_REQUESTED_BANDWIDTH_UL = 516;
  public static final int VISITED_NETWORK_IDENTIFIER = 600;
  public static final int SUPPORTED_FEATURES = 628;
  public static final int FEATURE_LIST_ID = 629;
  public static final int FEATURE_LIST = 630;
  public static final int MSISDN = 701;
  public static final int QOS_CLASS_IDENTIFIER = 1028;
  public static final int RAT_TYPE = 1032;
  public static final int ALLOCATION_RETENTION_POLICY = 1034;
  public static final int PRIORITY_LEVEL = 1046;
  public static final int PRE_EMPTION_CAPABILITY = 1047;
  public static final int PRE_EMPTION_VULNERABILITY = 1048;
  public static final int RESYNCHRONIZATION_INFO = 1411;
  public static final int EUTRAN_VECTOR = 1414;
  public static final int PDN_GATEWAY_ALLOCATION_TYPE =1438;
  public static final int GMLC_ADDRESS = 2405;

  private DiameterS6aAvpCodes() {
  }
}
