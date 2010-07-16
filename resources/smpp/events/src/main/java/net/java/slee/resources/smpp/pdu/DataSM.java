package net.java.slee.resources.smpp.pdu;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class DataSM implements SmppRequest {

	private Map<Tag, Object> tlvs = new HashMap<Tag, Object>();

	public abstract String getServiceType();

	public abstract void setServiceType(String serviceType);

	public abstract Address getDestAddress();

	public abstract void setDestAddress(Address address);

	public abstract Address getSourceAddress();

	public abstract void setSourceAddress(Address address);

	public abstract int getEsmClass();

	public abstract void setEsmClass(int esmClass);

	public abstract int getRegisteredDelivery();

	public abstract void setRegisteredDelivery(int registeredDelivery);

	public abstract int getDataCoding();

	public abstract void setDataCoding(int dataCoding);

	public void addTLV(Tag tag, Object value) throws TLVNotPermittedException {
		if (isTLVPermitted(tag)) {
			this.tlvs.put(tag, value);
		} else {
			throw new TLVNotPermittedException(tag);
		}
	}

	public Object getValue(Tag tag) {
		return this.tlvs.get(tag);
	}

	public Object removeTLV(Tag tag) {
		return this.tlvs.remove(tag);
	}

	public boolean hasTLV(Tag tag) {
		return this.tlvs.containsKey(tag);
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.SOURCE_PORT) || tag.equals(Tag.SOURCE_ADDR_SUBUNIT)
				|| tag.equals(Tag.SOURCE_NETWORK_TYPE) || tag.equals(Tag.SOURCE_BEARER_TYPE)
				|| tag.equals(Tag.SOURCE_TELEMATICS_ID) || tag.equals(Tag.DEST_PORT)
				|| tag.equals(Tag.DEST_ADDR_SUBUNIT) || tag.equals(Tag.DEST_NETWORK_TYPE)
				|| tag.equals(Tag.DEST_BEARER_TYPE) || tag.equals(Tag.DEST_TELEMATICS_ID)
				|| tag.equals(Tag.SAR_MSG_REF_NUM) || tag.equals(Tag.SAR_TOTAL_SEGMENTS)
				|| tag.equals(Tag.SAR_SEGMENT_SEQNUM) || tag.equals(Tag.MORE_MESSAGES_TO_SEND)
				|| tag.equals(Tag.QOS_TIME_TO_LIVE) || tag.equals(Tag.PAYLOAD_TYPE) || tag.equals(Tag.MESSAGE_PAYLOAD)
				|| tag.equals(Tag.SET_DPF) || tag.equals(Tag.RECEIPTED_MESSAGE_ID) || tag.equals(Tag.MESSAGE_STATE)
				|| tag.equals(Tag.NETWORK_ERROR_CODE) || tag.equals(Tag.USER_MESSAGE_REFERENCE)
				|| tag.equals(Tag.PRIVACY_INDICATOR) || tag.equals(Tag.CALLBACK_NUM)
				|| tag.equals(Tag.CALLBACK_NUM_PRES_IND) || tag.equals(Tag.CALLBACK_NUM_ATAG)
				|| tag.equals(Tag.SOURCE_SUBADDRESS) || tag.equals(Tag.DEST_SUBADDRESS)
				|| tag.equals(Tag.USER_RESPONSE_CODE) || tag.equals(Tag.DISPLAY_TIME) || tag.equals(Tag.SMS_SIGNAL)
				|| tag.equals(Tag.MS_VALIDITY) || tag.equals(Tag.MS_MSG_WAIT_FACILITIES)
				|| tag.equals(Tag.NUMBER_OF_MESSAGES) || tag.equals(Tag.ALERT_ON_MESSAGE_DELIVERY)
				|| tag.equals(Tag.LANGUAGE_INDICATOR) || tag.equals(Tag.ITS_REPLY_TYPE) || tag
				.equals(Tag.ITS_SESSION_INFO));
	}

	public Map<Tag, Object> getAllTLVs() {
		return this.tlvs;
	}

}
