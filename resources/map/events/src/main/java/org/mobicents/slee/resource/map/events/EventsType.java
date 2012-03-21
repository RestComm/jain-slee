/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.map.events;

/**
 * @author abhayani
 * 
 */
public interface EventsType {

	// USSD
	public static final String PROCESS_UNSTRUCTURED_SS_REQUEST = "ss7.map.service.suplementary.PROCESS_UNSTRUCTURED_SS_REQUEST";
	public static final String PROCESS_UNSTRUCTURED_SS_RESPONSE = "ss7.map.service.suplementary.PROCESS_UNSTRUCTURED_SS_RESPONSE";
	public static final String UNSTRUCTURED_SS_NOTIFY_REQUEST = "ss7.map.service.suplementary.UNSTRUCTURED_SS_NOTIFY_REQUEST";
	public static final String UNSTRUCTURED_SS_NOTIFY_RESPONSE = "ss7.map.service.suplementary.UNSTRUCTURED_SS_NOTIFY_RESPONSE";
	public static final String UNSTRUCTURED_SS_REQUEST = "ss7.map.service.suplementary.UNSTRUCTURED_SS_REQUEST";
	public static final String UNSTRUCTURED_SS_RESPONSE = "ss7.map.service.suplementary.UNSTRUCTURED_SS_RESPONSE";

	// LSM
	public static final String PROVIDE_SUBSCRIBER_LOCATION_REQUEST = "ss7.map.service.lsm.PROVIDE_SUBSCRIBER_LOCATION_REQUEST";
	public static final String PROVIDE_SUBSCRIBER_LOCATION_RESPONSE = "ss7.map.service.lsm.PROVIDE_SUBSCRIBER_LOCATION_RESPONSE";
	public static final String SEND_ROUTING_INFO_FOR_LCS_REQUEST = "ss7.map.service.lsm.SEND_ROUTING_INFO_FOR_LCS_REQUEST";
	public static final String SEND_ROUTING_INFO_FOR_LCS_RESPONSE = "ss7.map.service.lsm.SEND_ROUTING_INFO_FOR_LCS_RESPONSE";
	public static final String SUBSCRIBER_LOCATION_REPORT_REQUEST = "ss7.map.service.lsm.SUBSCRIBER_LOCATION_REPORT_REQUEST";
	public static final String SUBSCRIBER_LOCATION_REPORT_RESPONSE = "ss7.map.service.lsm.SUBSCRIBER_LOCATION_REPORT_RESPONSE";

	// SMS
	public static final String ALERT_SERVICE_CENTER_REQUEST = "ss7.map.service.sms.ALERT_SERVICE_CENTER_REQUEST";
	public static final String ALERT_SERVICE_CENTER_RESPONSE = "ss7.map.service.sms.ALERT_SERVICE_CENTER_RESPONSE";
	public static final String FORWARD_SHORT_MESSAGE_REQUEST = "ss7.map.service.sms.FORWARD_SHORT_MESSAGE_REQUEST";
	public static final String FORWARD_SHORT_MESSAGE_RESPONSE = "ss7.map.service.sms.FORWARD_SHORT_MESSAGE_RESPONSE";
	public static final String INFORM_SERVICE_CENTER_REQUEST = "ss7.map.service.sms.INFORM_SERVICE_CENTER_REQUEST";
	public static final String MO_FORWARD_SHORT_MESSAGE_REQUEST = "ss7.map.service.sms.MO_FORWARD_SHORT_MESSAGE_REQUEST";
	public static final String MO_FORWARD_SHORT_MESSAGE_RESPONSE = "ss7.map.service.sms.MO_FORWARD_SHORT_MESSAGE_RESPONSE";
	public static final String MT_FORWARD_SHORT_MESSAGE_REQUEST = "ss7.map.service.sms.MT_FORWARD_SHORT_MESSAGE_REQUEST";
	public static final String MT_FORWARD_SHORT_MESSAGE_RESPONSE = "ss7.map.service.sms.MT_FORWARD_SHORT_MESSAGE_RESPONSE";
	public static final String REPORT_SM_DELIVERY_STATUS_REQUEST = "ss7.map.service.sms.REPORT_SM_DELIVERY_STATUS_REQUEST";
	public static final String REPORT_SM_DELIVERY_STATUS_RESPONSE = "ss7.map.service.sms.REPORT_SM_DELIVERY_STATUS_RESPONSE";
	public static final String SEND_ROUTING_INFO_FOR_SM_REQUEST = "ss7.map.service.sms.SEND_ROUTING_INFO_FOR_SM_REQUEST";
	public static final String SEND_ROUTING_INFO_FOR_SM_RESPONSE = "ss7.map.service.sms.SEND_ROUTING_INFO_FOR_SM_RESPONSE";
	
	//SUBSCRIBER INFORMATION
	public static final String ANY_TIME_INTERROGATION_REQUEST = "ss7.map.service.subscriberinfo.ANY_TIME_INTERROGATION_REQUEST";
	public static final String ANY_TIME_INTERROGATION_RESPONSE = "ss7.map.service.subscriberinfo.ANY_TIME_INTERROGATION_RESPONSE";
	
}
