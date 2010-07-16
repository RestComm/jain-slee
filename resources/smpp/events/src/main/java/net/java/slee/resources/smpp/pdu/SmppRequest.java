package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 *
 */
public interface SmppRequest extends PDU {

	public static final long BIND_RECEIVER = 0x00000001;
	
	public static final long BIND_TRANSMITTER = 0x00000002;
	public static final long QUERY_SM = 0x00000003;
	public static final long SUBMIT_SM = 0x00000004;
	public static final long DELIVER_SM = 0x00000005;
	public static final long UNBIND = 0x00000006;
	public static final long REPLACE_SM = 0x00000007;
	public static final long CANCEL_SM = 0x00000008;
	public static final long BIND_TRANSCEIVER = 0x00000009;
	public static final long OUTBIND = 0x0000000B;
	public static final long ENQUIRE_LINK = 0x00000015;
	public static final long SUBMIT_MULTI = 0x00000021;
	public static final long ALERT_NOTIFICATION = 0x00000102;
	public static final long DATA_SM = 0x00000103;
	//public static final long BROADCAST_SM = 0x00000111;
	//public static final long QUERY_BROADCAST_SM = 0x00000112;
	//public static final long CANCEL_BROADCAST_SM = 0x00000113;
	public static final long GENERIC_NACK = 0x80000000;
	public static final long BIND_RECEIVER_RESP = 0x80000001;
	public static final long BIND_TRANSMITTER_RESP = 0x80000002;
	public static final long QUERY_SM_RESP = 0x80000003;
	public static final long SUBMIT_SM_RESP = 0x80000004;
	public static final long DELIVER_SM_RESP = 0x80000005;
	public static final long UNBIND_RESP = 0x80000006;
	public static final long REPLACE_SM_RESP = 0x80000007;
	public static final long CANCEL_SM_RESP = 0x80000008;
	public static final long BIND_TRANSCEIVER_RESP = 0x80000009;
	public static final long ENQUIRY_LINK_RESP = 0x80000015;
	public static final long SUBMIT_MULTI_RESP = 0x80000021;
	public static final long DATA_SM_RESP = 0x80000103;
	//public static final long BORADCAST_SM_RESP = 0x80000111;
	//public static final long QUERY_BROADCAST_SM_RESP = 0x80000112;
	//public static final long CANCEL_BROADCAST_SM_RESP = 0x80000113;

	SmppResponse createSmppResponseEvent(int status);
}
