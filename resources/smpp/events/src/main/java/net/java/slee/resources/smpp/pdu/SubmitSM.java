package net.java.slee.resources.smpp.pdu;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class SubmitSM implements SmppRequest {

	private Map<Tag, Object> tlvs = new HashMap<Tag, Object>();

	public abstract String getServiceType();

	public abstract void setServiceType(String serviceType);

	public abstract Address getDestAddress();

	public abstract void setDestAddress(Address address);

	public abstract Address getSourceAddress();

	public abstract void setSourceAddress(Address address);

	public abstract int getEsmClass();

	public abstract void setEsmClass(int esmClass);

	public abstract int getProtocolID();

	public abstract void setProtocolID(int protocolID);

	public abstract void setPriority(int priority);

	public abstract int getPriority();

	// TODO The specs says about absolute date and time or relative. Should we add the corresponding classes here to set
	// respective dates rather than String?
	public abstract void setScheduleDeliveryTime(String time);

	public abstract String getScheduleDeliveryTime();

	// TODO The specs says about absolute date and time or relative. Should we add the corresponding classes here to set
	// respective dates rather than String?
	public abstract String getValidityPeriod();

	public abstract void setValidityPeriod(String period);

	public abstract int getRegisteredDelivery();

	public abstract void setRegisteredDelivery(int registeredDelivery);

	public abstract int getReplaceIfPresentFlag();

	public abstract void setReplaceIfPresentFlag(int replaceIfPresentFlag);

	public abstract int getDataCoding();

	public abstract void setDataCoding(int dataCoding);

	public abstract int getSmDefaultMsgID();

	public abstract void setSmDefaultMsgID(int smDefaultMsgID);

	public abstract byte[] getMessage();

	public abstract void setMessage(byte[] message);

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
		return (tag.equals(Tag.USER_MESSAGE_REFERENCE) || tag.equals(Tag.SOURCE_PORT)
				|| tag.equals(Tag.SOURCE_ADDR_SUBUNIT) || tag.equals(Tag.DEST_PORT)
				|| tag.equals(Tag.DEST_ADDR_SUBUNIT) || tag.equals(Tag.SAR_MSG_REF_NUM)
				|| tag.equals(Tag.SAR_TOTAL_SEGMENTS) || tag.equals(Tag.SAR_SEGMENT_SEQNUM)
				|| tag.equals(Tag.MORE_MESSAGES_TO_SEND) || tag.equals(Tag.PAYLOAD_TYPE)
				|| tag.equals(Tag.MESSAGE_PAYLOAD) || tag.equals(Tag.PRIVACY_INDICATOR) || tag.equals(Tag.CALLBACK_NUM)
				|| tag.equals(Tag.CALLBACK_NUM_PRES_IND) || tag.equals(Tag.CALLBACK_NUM_ATAG)
				|| tag.equals(Tag.SOURCE_SUBADDRESS) || tag.equals(Tag.DEST_SUBADDRESS)
				|| tag.equals(Tag.USER_RESPONSE_CODE) || tag.equals(Tag.DISPLAY_TIME) || tag.equals(Tag.SMS_SIGNAL)
				|| tag.equals(Tag.MS_VALIDITY) || tag.equals(Tag.MS_MSG_WAIT_FACILITIES)
				|| tag.equals(Tag.NUMBER_OF_MESSAGES) || tag.equals(Tag.ALERT_ON_MESSAGE_DELIVERY)
				|| tag.equals(Tag.LANGUAGE_INDICATOR) || tag.equals(Tag.ITS_REPLY_TYPE) || tag
				.equals(Tag.USSD_SERVICE_OP));
	}

	public Map<Tag, Object> getAllTLVs() {
		return this.tlvs;
	}

}
