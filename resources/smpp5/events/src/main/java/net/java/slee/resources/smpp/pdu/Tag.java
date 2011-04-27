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

package net.java.slee.resources.smpp.pdu;

/**
 * <p>
 * TLV fields may be optionally included in a SMPP message. For a particular SMPP PDU, the ESME or MC may include some,
 * all or none of the defined TLVs as required for the particular application context. For example a paging system may
 * in a SMPP submit_sm operation, include only the “call-back number” related TLVs.
 * </p>
 * <p>
 * For further details look at Section 4.8 PDU TLV Definitions from SMPP Specs v 5.0
 * </p>
 * <p>
 * This is the Tag definitions
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public final class Tag {

	/**
	 * The additional_status_info_text parameter gives an ASCII textual description of the meaning of a response PDU. It
	 * is to be used by an implementation to allow easy diagnosis of problems.
	 */
	public static final Tag ADDITIONAL_STATUS_INFO_TEXT = new Tag(0x001D);

	/**
	 * The alert_on_message_delivery parameter is set to instruct a SMSC to alert the user (in a MS implementation
	 * specific manner) when the short message arrives at the MS.
	 */
	public static final Tag ALERT_ON_MESSAGE_DELIVERY = new Tag(0x130C);

	/**
	 * Billing information
	 */
	public static final Tag BILLING_IDENTIFICATION = new Tag(0x060B);

	/**
	 * The broadcast_area_identifier defines the Broadcast Area in terms of a geographical descriptor.
	 */
	public static final Tag BROADCAST_AREA_IDENTIFIER = new Tag(0x0606);

	/**
	 * The broadcast_area_success parameter is a success rate indicator, defined as the ratio of the number of BTSs who
	 * accepted the message and the total number of BTSs who should accept the message, for a particular
	 * broadcast_area_identifier.
	 */
	public static final Tag BROADCAST_AREA_SUCCESS = new Tag(0x0608);

	/**
	 * The broadcast_content_type_info parameter contains additional information specific to the broadcast_content_type.
	 * 
	 */
	public static final Tag BROADCAST_CONTENT_TYPE_INFO = new Tag(0x0602);

	/**
	 * The broadcast_channel_indicator parameter specifies the Cell Broadcast channel that should be used for
	 * broadcasting the message.
	 */
	public static final Tag BROADCAST_CHANNEL_INDICATOR = new Tag(0x0600);

	/**
	 * The broadcast_content_type parameter specifies the content_type of the message content.
	 */
	public static final Tag BROADCAST_CONTENT_TYPE = new Tag(0x0601);

	/**
	 * The broadcast_end_time parameter indicates the date and time at which the broadcasting state of this message was
	 * set to terminated in the Message Centre.
	 */
	public static final Tag BROADCAST_END_TIME = new Tag(0x0609);

	/**
	 * The broadcast_error_status parameter specifies the nature of the failure associated with a particular
	 * broadcast_area_identifier specified in a broadcast request.
	 */
	public static final Tag BROADCAST_ERROR_STATUS = new Tag(0x0607);

	/**
	 * The broadcast_frequency_interval parameter specifies the frequency interval at which the broadcasts of a message
	 * should be repeated.
	 */
	public static final Tag BROADCAST_FREQUENCY_INTERVAL = new Tag(0x0605);

	/**
	 * The broadcast_message_class parameter is used to route messages when received by a mobile station to user-defined
	 * destinations or to Terminal Equipment.
	 */
	public static final Tag BROADCAST_MESSAGE_CLASS = new Tag(0x0603);

	/**
	 * This field indicates the number of repeated broadcasts requested by the Submitter.
	 */
	public static final Tag BROADCAST_REP_NUM = new Tag(0x0604);

	/**
	 * The broadcast_service_group parameter is used to specify special target groups for broadcast information.
	 */
	public static final Tag BROADCAST_SERVICE_GROUP = new Tag(0x060A);

	/**
	 * The callback_num parameter associates a call back number with the message. In TDMA networks, it is possible to
	 * send and receive multiple call-back numbers to/from TDMA mobile stations.
	 */
	public static final Tag CALLBACK_NUM = new Tag(0x0381);

	/**
	 * The callback_num_atag parameter associates an alphanumeric display with the call back number.
	 */
	public static final Tag CALLBACK_NUM_ATAG = new Tag(0x0303);

	/**
	 * This parameter controls the presentation indication and screening of the CallBackNumber at the mobile station.
	 */
	public static final Tag CALLBACK_NUM_PRES_IND = new Tag(0x0302);

	/**
	 * The congestion_state parameter is used to pass congestion status information between ESME and SMSC as a means of
	 * providing flow control and congestion avoidance capabilities to the sending peer. The TLV can be used in any SMPP
	 * operation response PDU as a means of passing congestion status from one peer to another. Typical uses of this
	 * would be in submit_sm/submit_sm_resp sequences where an ESME would drive a batch of submissions at a high rate
	 * and use continual tracking of the returned congestion_state values as a means of gauging the congestion. Reaction
	 * to a variation in congestion_state would involve increasing/decreasing the rate as required to maintain the
	 * balance in the Optimum range.
	 */
	public static final Tag CONGESTION_STATE = new Tag(0x0428);

	/**
	 * The delivery_failure_reason parameter is used in the data_sm_resp operation to indicate the outcome of the
	 * message delivery attempt (only applicable for transaction message mode). If a delivery failure due to a network
	 * error is indicated, the ESME may check the network_error_code parameter (if present) for the actual network error
	 * code.
	 */
	public static final Tag DELIVERY_FAILURE_REASON = new Tag(0x0425);

	/**
	 * The dest_addr_np_country TLV is used to carry E.164 information relating to the operator country code.
	 */
	public static final Tag DEST_ADDR_NP_COUNTRY = new Tag(0x0613);

	/**
	 * The dest_addr_np_information TLV is used to carry number portability information.
	 */
	public static final Tag DEST_ADDR_NP_INFORMATION = new Tag(0x0612);

	/**
	 * The dest_addr_np_resolution TLV is used to pass an indicator relating to a number portability query. If this TLV
	 * is omitted, the default value is assumed.
	 */
	public static final Tag DEST_ADDR_NP_RESOLUTION = new Tag(0x0611);

	/**
	 * The dest_addr_subunit parameter is used to route messages when received by a mobile station, for example to a
	 * smart card in the mobile station or to an external device connected to the mobile station.
	 */
	public static final Tag DEST_ADDR_SUBUNIT = new Tag(0x0005);

	/**
	 * The dest_bearer_type parameter is used to request the desired bearer for delivery of the message to the
	 * destination address. In the case that the receiving system (e.g. MC) does not support the indicated bearer type,
	 * it may treat this a failure and return a response PDU reporting a failure.
	 */
	public static final Tag DEST_BEARER_TYPE = new Tag(0x0007);

	/**
	 * The dest_network_id assigned to a wireless network operator or ESME operator is a unique address that may be
	 * derived and assigned by the node owner without establishing a central assignment and management authority. When
	 * this TLV is specified, it must be accompanied with a dest_node_id TLV
	 */
	public static final Tag DEST_NETWORK_ID = new Tag(0x060E);

	/**
	 * The dest_network_type parameter is used to indicate a network type associated with the destination address of a
	 * message. In the case that the receiving system (e.g. MC) does not support the indicated network type, it may
	 * treat this a failure and return a response PDU reporting a failure.
	 */
	public static final Tag DEST_NETWORK_TYPE = new Tag(0x0006);

	/**
	 * The dest_node_id is a unique number assigned within a single ESME or SMSC network and must uniquely identify a
	 * destination node within the context of the MC or ESME. The content of a dest_node_id is comprised of decimal
	 * digits and is at the discretion of the owning ESME or MC.
	 */
	public static final Tag DEST_NODE_ID = new Tag(0x0610);

	/**
	 * The dest_subaddress parameter specifies a subaddress associated with the destination of the message.
	 */
	public static final Tag DEST_SUBADDRESS = new Tag(0x0203);

	/**
	 * This parameter defines the telematic interworking to be used by the delivering system for the destination
	 * address. This is only useful when a specific dest_bearer_type parameter has also been specified, as the value is
	 * bearer dependent. In the case that the receiving system (e.g. SMSC) does not support the indicated telematic
	 * interworking, it may treat this a failure and return a response PDU reporting a failure.
	 */
	public static final Tag DEST_TELEMATICS_ID = new Tag(0x0008);

	/**
	 * The dest_port parameter is used to indicate the application port number associated with the destination address
	 * of the message.
	 */
	public static final Tag DEST_PORT = new Tag(0x020B);

	/**
	 * The display_time parameter is used to associate a display time of the short message on the MS.
	 */
	public static final Tag DISPLAY_TIME = new Tag(0x1201);

	/**
	 * <p>
	 * The dpf_result parameter is used to indicate if delivery pending flag (DPF) was set for a delivery failure of a
	 * short message.
	 * </p>
	 * <p>
	 * When used in conjunction with transaction mode, dpf_result can be returned in a submit_sm_resp or data_sm_resp
	 * PDU. Where store and forward or datagram modes are used in the original submission, dpf_result may be returned as
	 * part of a delivery receipt in the form of a deliver_sm or data_sm PDU.
	 * </p>
	 * <p>
	 * If the dpf_result parameter is not returned, then the ESME should assume that DPF is not set.
	 * </p>
	 * 
	 */
	public static final Tag DPF_RESULT = new Tag(0x0420);

	/**
	 * The its_reply_type parameter is a required parameter for the CDMA Interactive Teleservice as defined by the
	 * Korean PCS carriers [KORITS]. It indicates and controls the MS user’s reply method to an SMS delivery message
	 * received from the ESME.
	 */
	public static final Tag ITS_REPLY_TYPE = new Tag(0x1380);

	/**
	 * The its_session_info parameter is a required parameter for the CDMA Interactive Teleservice as defined by the
	 * Korean PCS carriers [KORITS]. It contains control information for the interactive session between an MS and an
	 * ESME.
	 */
	public static final Tag ITS_SESSION_INFO = new Tag(0x1383);

	/**
	 * The language_indicator parameter is used to indicate the language of the short message.
	 */
	public static final Tag LANGUAGE_INDICATOR = new Tag(0x020D);

	/**
	 * <p>
	 * The message_payload parameter contains the user data. Its function is to provide an alternative means of carrying
	 * text lengths above the 255 octet limit of the short_message field.
	 * </p>
	 * <p>
	 * Applications, which need to send messages longer than 255 octets, should use the message_payload TLV. When used
	 * in the context of a submit_sm PDU, the sm_length field should be set to zero.
	 * </p>
	 * 
	 */
	public static final Tag MESSAGE_PAYLOAD = new Tag(0x0424);

	/**
	 * The message_state TLV is used by the MC in the deliver_sm and data_sm PDUs to indicate to the ESME the final
	 * message state for a MC Delivery Receipt. The message_state TLV is also returned by the MC to the ESME as part of
	 * the query_broadcast_sm_resp PDU.
	 */
	public static final Tag MESSAGE_STATE = new Tag(0x0427);

	/**
	 * The more_messages_to_send parameter is used by the ESME in the submit_sm and data_sm operations to indicate to
	 * the MC that there are further messages for the same destination SME. The MC may use this setting for network
	 * resource optimisation.
	 */
	public static final Tag MORE_MESSAGES_TO_SEND = new Tag(0x0426);

	/**
	 * <p>
	 * The ms_availability_status parameter is used in the alert_notification operation to indicate the availability
	 * state of the MS to the ESME.
	 * </p>
	 * <p>
	 * If the SMSC does not include the parameter in the alert_notification operation, the ESME should assume that the
	 * MS is in an “available” state.
	 * </p>
	 */
	public static final Tag MS_AVAILABILITY_STATUS = new Tag(0x0422);

	/**
	 * <p>
	 * The ms_msg_wait_facilities parameter allows an indication to be provided to an MS that there are messages waiting
	 * for the subscriber on systems on the PLMN. The indication can be an icon on the MS screen or other MMI
	 * indication.
	 * </p>
	 * <p>
	 * The ms_msg_wait_facilities can also specify the type of message associated with the message waiting indication.
	 * </p>
	 */
	public static final Tag MS_MSG_WAIT_FACILITIES = new Tag(0x0030);

	/**
	 * The ms_validity parameter is used to provide an MS with validity information associated with the received short
	 * message.
	 */
	public static final Tag MS_VALIDITY = new Tag(0x1204);

	/**
	 * The network_error_code parameter is used to indicate the actual network error code for a delivery failure. The
	 * network error code is technology specific.
	 */
	public static final Tag NETWORK_ERROR_CODE = new Tag(0x0423);

	/**
	 * The number_of_messages parameter is used to indicate the number of messages stored in a mailbox.
	 */
	public static final Tag NUMBER_OF_MESSAGES = new Tag(0x0304);

	/**
	 * The payload_type parameter defines the higher layer PDU type contained in the message payload.
	 */
	public static final Tag PAYLOAD_TYPE = new Tag(0x0019);

	/**
	 * The privacy_indicator indicates the privacy level of the message.
	 */
	public static final Tag PRIVACY_INDICATOR = new Tag(0x0201);

	/**
	 * This parameter defines the number of seconds which the sender requests the MC to keep the message if undelivered
	 * before it is deemed expired. If the parameter is not present, the MC may apply a default value
	 */
	public static final Tag QOS_TIME_TO_LIVE = new Tag(0x0017);

	public static final Tag RECEIPTED_MESSAGE_ID = new Tag(0x001E);

	public static final Tag SAR_MSG_REF_NUM = new Tag(0x020C);

	public static final Tag SAR_SEGMENT_SEQNUM = new Tag(0x020F);

	public static final Tag SAR_TOTAL_SEGMENTS = new Tag(0x020E);

	public static final Tag SC_INTERFACE_VERSION = new Tag(0x0210);

	public static final Tag SET_DPF = new Tag(0x0421);

	public static final Tag SMS_SIGNAL = new Tag(0x1203);

	public static final Tag SOURCE_ADDR_SUBUNIT = new Tag(0x000D);

	public static final Tag SOURCE_BEARER_TYPE = new Tag(0x000F);

	public static final Tag SOURCE_NETWORK_ID = new Tag(0x060D);

	public static final Tag SOURCE_NETWORK_TYPE = new Tag(0x000E);

	public static final Tag SOURCE_NODE_ID = new Tag(0x060F);

	public static final Tag SOURCE_PORT = new Tag(0x020A);

	public static final Tag SOURCE_SUBADDRESS = new Tag(0x0202);

	public static final Tag SOURCE_TELEMATICS_ID = new Tag(0x0010);

	public static final Tag USER_MESSAGE_REFERENCE = new Tag(0x0204);

	public static final Tag USER_RESPONSE_CODE = new Tag(0x0205);

	public static final Tag USSD_SERVICE_OP = new Tag(0x0501);

	private int tag;

	public Tag(int tag) {
		this.tag = tag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tag;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Tag other = (Tag) obj;
		if (tag != other.tag)
			return false;
		return true;
	}
	
	public int getTag() {
		return tag;
	}

}
