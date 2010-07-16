package org.mobicents.slee.resources.smpp.pdu;

import java.util.ArrayList;
import java.util.List;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.pdu.SmppTooManyValuesException;
import net.java.slee.resources.smpp.pdu.SubmitMulti;

/**
 * 
 * @author amit bhayani
 *
 */
public class SubmitMultiImpl extends SubmitMulti {

	private static final int MAX_DEST_ADDRESS_ALLOWED = 254;

	private List<Address> destAddresses = new ArrayList<Address>();
	private List<String> distrListNames = new ArrayList<String>();

	private int destAddressCounter = 0;

	private long commandId = SmppRequest.SUBMIT_MULTI;
	private long commandStatus = 0;
	private long sequenceNumber = -1;

	private int dataCoding = 0;

	private int esmClass = 0;

	private Address srcAdd = null;

	private byte[] message;

	private int priority;

	private int protocolId;

	private int registered;

	private int replace;

	private String deliveryTime;

	private String serviceType;

	private int defaultMsgId;

	private String validityPeriod;

	public SubmitMultiImpl(long sequenceNumber) {
		super();
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public void addDestAddress(Address address) throws SmppTooManyValuesException {
		if (this.destAddressCounter == MAX_DEST_ADDRESS_ALLOWED) {
			throw new SmppTooManyValuesException("Maximum allowed destinations are 254");
		}
		destAddressCounter++;
		destAddresses.add(address);
	}

	@Override
	public void addDistrListName(String distributionListName) throws SmppTooManyValuesException {
		if (this.destAddressCounter == MAX_DEST_ADDRESS_ALLOWED) {
			throw new SmppTooManyValuesException("Maximum allowed destinations are 254");
		}
		destAddressCounter++;
		distrListNames.add(distributionListName);
	}

	@Override
	public int getDataCoding() {
		return this.dataCoding;
	}

	@Override
	public List<Address> getDestAddresses() {
		List<Address> tempDestAddresses = new ArrayList<Address>();
		tempDestAddresses.addAll(this.destAddresses);
		return tempDestAddresses;
	}

	@Override
	public List<String> getDistrListNames() {
		List<String> tmpDistrListNames = new ArrayList<String>();
		tmpDistrListNames.addAll(this.distrListNames);
		return tmpDistrListNames;
	}

	@Override
	public int getEsmClass() {
		return this.esmClass;
	}

	@Override
	public Address getSourceAddress() {
		return this.srcAdd;
	}

	@Override
	public byte[] getMessage() {
		return this.message;
	}

	@Override
	public int getPriority() {
		return this.priority;
	}

	@Override
	public int getProtocolID() {
		return this.protocolId;
	}

	@Override
	public int getRegisteredDelivery() {
		return this.registered;
	}

	@Override
	public int getReplaceIfPresentFlag() {
		return this.replace;
	}

	@Override
	public String getScheduleDeliveryTime() {
		return this.deliveryTime;
	}

	@Override
	public String getServiceType() {
		return this.serviceType;
	}

	@Override
	public int getSmDefaultMsgID() {
		return this.defaultMsgId;
	}

	@Override
	public String getValidityPeriod() {
		return this.validityPeriod;
	}

	@Override
	public boolean removeDestAddress(Address address) {
		boolean result = this.destAddresses.remove(address);
		if (result) {
			this.destAddressCounter--;
		}
		return result;
	}

	@Override
	public boolean removeDistrListName(String distributionListName) {
		boolean result = this.distrListNames.remove(distributionListName);
		if (result) {
			this.destAddressCounter--;
		}
		return result;
	}

	@Override
	public void setDataCoding(int dataCoding) {
		this.dataCoding = dataCoding;
	}

	@Override
	public void setEsmClass(int esmClass) {
		this.esmClass = esmClass;

	}

	@Override
	public void setSourceAddress(Address address) {
		this.srcAdd = address;
	}

	@Override
	public void setMessage(byte[] message) {
		this.message = message;
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public void setProtocolID(int protocolID) {
		this.protocolId = protocolID;
	}

	@Override
	public void setRegisteredDelivery(int registeredDelivery) {
		this.registered = registeredDelivery;
	}

	@Override
	public void setReplaceIfPresentFlag(int replaceIfPresentFlag) {
		this.replace = replaceIfPresentFlag;
	}

	@Override
	public void setScheduleDeliveryTime(String time) {
		this.deliveryTime = time;
	}

	@Override
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public void setSmDefaultMsgID(int smDefaultMsgID) {
		this.defaultMsgId = smDefaultMsgID;
	}

	@Override
	public void setValidityPeriod(String period) {
		this.validityPeriod = period;
	}

	public SmppResponse createSmppResponseEvent(int status) {
		SubmitMultiRespImpl submitMultiRespImpl = new SubmitMultiRespImpl(status);
		submitMultiRespImpl.setSequenceNum(this.getSequenceNum());
		return submitMultiRespImpl;
	}

	public long getCommandId() {
		return this.commandId;
	}

	public long getCommandStatus() {
		return this.commandStatus;
	}

	public long getSequenceNum() {
		return this.sequenceNumber;
	}

	public void setSequenceNum(long sequenceNum) {
		this.sequenceNumber = sequenceNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (commandId ^ (commandId >>> 32));
		result = prime * result + dataCoding;
		result = prime * result + defaultMsgId;
		result = prime * result + ((deliveryTime == null) ? 0 : deliveryTime.hashCode());
		result = prime * result + destAddressCounter;
		result = prime * result + esmClass;
		result = prime * result + priority;
		result = prime * result + protocolId;
		result = prime * result + registered;
		result = prime * result + replace;
		result = prime * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		result = prime * result + ((srcAdd == null) ? 0 : srcAdd.hashCode());
		result = prime * result + ((validityPeriod == null) ? 0 : validityPeriod.hashCode());
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
		final SubmitMultiImpl other = (SubmitMultiImpl) obj;
		if (commandId != other.commandId)
			return false;
		if (dataCoding != other.dataCoding)
			return false;
		if (defaultMsgId != other.defaultMsgId)
			return false;
		if (deliveryTime == null) {
			if (other.deliveryTime != null)
				return false;
		} else if (!deliveryTime.equals(other.deliveryTime))
			return false;
		if (destAddressCounter != other.destAddressCounter)
			return false;
		if (esmClass != other.esmClass)
			return false;
		if (priority != other.priority)
			return false;
		if (protocolId != other.protocolId)
			return false;
		if (registered != other.registered)
			return false;
		if (replace != other.replace)
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		if (serviceType == null) {
			if (other.serviceType != null)
				return false;
		} else if (!serviceType.equals(other.serviceType))
			return false;
		if (srcAdd == null) {
			if (other.srcAdd != null)
				return false;
		} else if (!srcAdd.equals(other.srcAdd))
			return false;
		if (validityPeriod == null) {
			if (other.validityPeriod != null)
				return false;
		} else if (!validityPeriod.equals(other.validityPeriod))
			return false;
		return true;
	}

}
