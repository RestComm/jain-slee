package org.mobicents.slee.resources.smpp.pdu;

import java.util.HashMap;
import java.util.Map;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.ErrorAddress;
import net.java.slee.resources.smpp.pdu.PDU;
import net.java.slee.resources.smpp.pdu.TLVNotPermittedException;
import net.java.slee.resources.smpp.pdu.Tag;
import net.java.slee.resources.smpp.util.SMPPDate;

import org.mobicents.slee.resources.smpp.util.AbsoluteSMPPDateImpl;
import org.mobicents.slee.resources.smpp.util.RelativeSMPPDateImpl;

public abstract class PDUImpl implements PDU {

	protected org.mobicents.protocols.smpp.message.SMPPPacket smppPacket;

	public int getCommandId() {
		return smppPacket.getCommandId();
	}

	public int getCommandStatus() {
		return smppPacket.getCommandStatus();
	}

	public long getSequenceNum() {
		return smppPacket.getSequenceNum();
	}

	public void setSequenceNum(long sequenceNum) {
		this.smppPacket.setSequenceNum(sequenceNum);
	}

	public void addTLV(Tag tag, Object value) throws TLVNotPermittedException {
		if (isTLVPermitted(tag)) {
			this.smppPacket.getTLVTable().put(org.mobicents.protocols.smpp.message.tlv.Tag.getTag(tag.getTag()), value);
		} else {
			throw new TLVNotPermittedException(tag);
		}
	}

	public Map<Tag, Object> getAllTLVs() {
		HashMap<Tag, Object> tlvs = new HashMap<Tag, Object>();
		for (Map.Entry<org.mobicents.protocols.smpp.message.tlv.Tag, Object> entry : this.smppPacket.getTLVTable()
				.entrySet()) {
			tlvs.put(new Tag(entry.getKey().intValue()), entry.getValue());
		}
		return tlvs;
	}

	public Object getValue(Tag tag) {
		return this.smppPacket.getTLVTable().get(tag.getTag());
	}

	public boolean hasTLV(Tag tag) {
		return this.smppPacket.getTLVTable().containsKey(
				org.mobicents.protocols.smpp.message.tlv.Tag.getTag(tag.getTag()));
	}

	public Object removeTLV(Tag tag) {
		return this.smppPacket.getTLVTable().remove(org.mobicents.protocols.smpp.message.tlv.Tag.getTag(tag.getTag()));
	}

	@Override
	public int hashCode() {
		return this.smppPacket.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.smppPacket.equals(obj);
	}

	protected SMPPDate convertProtoDate(org.mobicents.protocols.smpp.util.SMPPDate protoSmppDate) {
		if (protoSmppDate == null) {
			return null;
		} else if (protoSmppDate.isAbsolute()) {
			return new AbsoluteSMPPDateImpl(protoSmppDate.getCalendar(), protoSmppDate.hasTimezone());
		} else {
			return new RelativeSMPPDateImpl(protoSmppDate.getYear(), protoSmppDate.getMonth(), protoSmppDate.getDay(),
					protoSmppDate.getHour(), protoSmppDate.getMinute(), protoSmppDate.getSecond());
		}

	}

	protected Address convertProtoAddress(org.mobicents.protocols.smpp.Address address) {
		if (address == null) {
			return null;
		} else {
			return new AddressImpl(address);
		}
	}

	protected ErrorAddress convertProtoErrorAddress(org.mobicents.protocols.smpp.ErrorAddress errorAddress) {
		if (errorAddress == null) {
			return null;
		} else {
			return new ErrorAddressImpl(errorAddress);
		}
	}

}
