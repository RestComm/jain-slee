package net.java.slee.resources.smpp.pdu;

import java.util.HashMap;
import java.util.Map;

public abstract class BroadcastSM implements SmppRequest {
	private Map<Tag, Object> tlvs = new HashMap<Tag, Object>();

	public abstract String getServiceType();

	public abstract void setServiceType(String serviceType);

	public abstract Address getEsmeAddress();

	public abstract void setEsmeAddress(Address address);

	public abstract String getMessageID();

	public abstract void setMessageID(String messageID);

	public abstract int getPriority();

	public abstract void setPriority(int priority);

	// TODO The specs says about absolute date and time or relative. Should we add the corresponding classes here to set
	// respective dates rather than String?
	public abstract void setScheduleDeliveryTime(String time);

	public abstract String getScheduleDeliveryTime();

	// TODO The specs says about absolute date and time or relative. Should we add the corresponding classes here to set
	// respective dates rather than String?
	public abstract String getValidityPeriod();

	public abstract void setValidityPeriod(String period);

	public abstract int getReplaceIfPresentFlag();

	public abstract void setReplaceIfPresentFlag(int replaceIfPresentFlag);

	public abstract int getDataCoding();

	public abstract void setDataCoding(int dataCoding);

	public abstract int getSmDefaultMsgID();

	public abstract void setSmDefaultMsgID(int smDefaultMsgID);

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
		return (tag.equals(Tag.BROADCAST_AREA_IDENTIFIER) || tag.equals(Tag.BROADCAST_CONTENT_TYPE)
				|| tag.equals(Tag.BROADCAST_REP_NUM) || tag.equals(Tag.BROADCAST_FREQUENCY_INTERVAL)
				|| tag.equals(Tag.ALERT_ON_MESSAGE_DELIVERY) || tag.equals(Tag.BROADCAST_CHANNEL_INDICATOR)
				|| tag.equals(Tag.BROADCAST_CONTENT_TYPE_INFO) || tag.equals(Tag.BROADCAST_MESSAGE_CLASS)
				|| tag.equals(Tag.BROADCAST_SERVICE_GROUP) || tag.equals(Tag.CALLBACK_NUM)
				|| tag.equals(Tag.CALLBACK_NUM_ATAG) || tag.equals(Tag.CALLBACK_NUM_PRES_IND)
				|| tag.equals(Tag.DEST_ADDR_SUBUNIT) || tag.equals(Tag.DEST_SUBADDRESS) || tag.equals(Tag.DEST_PORT)
				|| tag.equals(Tag.DISPLAY_TIME) || tag.equals(Tag.LANGUAGE_INDICATOR)
				|| tag.equals(Tag.MESSAGE_PAYLOAD) || tag.equals(Tag.MS_VALIDITY) || tag.equals(Tag.PAYLOAD_TYPE)
				|| tag.equals(Tag.PRIVACY_INDICATOR) || tag.equals(Tag.SMS_SIGNAL)
				|| tag.equals(Tag.SOURCE_ADDR_SUBUNIT) || tag.equals(Tag.SOURCE_PORT)
				|| tag.equals(Tag.SOURCE_SUBADDRESS) || tag.equals(Tag.USER_MESSAGE_REFERENCE));
	}

	public Map<Tag, Object> getAllTLVs() {
		return this.tlvs;
	}
}
