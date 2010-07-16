package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.DeliverSM;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class DeliverSMImpl extends DeliverSM {

	private long commandId = SmppRequest.DELIVER_SM;
	private long commandStatus = 0;
	private long sequenceNumber = -1;

	private Address destAdd = null;
	private Address srcAdd = null;

	private int dataCoding = 0;
	private int esmClass = 0;

	private byte[] message;

	private int priority;

	private int protocolId;

	private int registered;

	private int replace;

	private String deliveryTime;

	private String serviceType;

	private int defaultMsgId;

	private String validityPeriod;

	public DeliverSMImpl(long sequenceNumber) {
		super();
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public int getDataCoding() {
		return this.dataCoding;
	}

	@Override
	public int getEsmClass() {
		return this.esmClass;
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
	public Address getSourceAddress() {
		return this.srcAdd;
	}

	@Override
	public String getValidityPeriod() {
		return this.validityPeriod;
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
	public void setDestAddress(Address address) {
		this.destAdd = address;
	}

	@Override
	public Address getDestAddress() {
		return this.destAdd;
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
	public void setSourceAddress(Address address) {
		this.srcAdd = address;
	}

	@Override
	public void setValidityPeriod(String period) {
		this.validityPeriod = period;
	}

	public SmppResponse createSmppResponseEvent(int status) {
		DeliverSMRespImpl deliverSMRespImpl = new DeliverSMRespImpl(status);
		deliverSMRespImpl.setSequenceNum(this.getSequenceNum());
		return deliverSMRespImpl;
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
		result = prime * result + ((destAdd == null) ? 0 : destAdd.hashCode());
		result = prime * result + esmClass;
		//result = prime * result + Arrays.hashCode(message);
		result = prime * result + priority;
		result = prime * result + protocolId;
		result = prime * result + registered;
		result = prime * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		result = prime * result + ((srcAdd == null) ? 0 : srcAdd.hashCode());
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
		final DeliverSMImpl other = (DeliverSMImpl) obj;
		if (commandId != other.commandId)
			return false;
		if (dataCoding != other.dataCoding)
			return false;
		if (destAdd == null) {
			if (other.destAdd != null)
				return false;
		} else if (!destAdd.equals(other.destAdd))
			return false;
		if (esmClass != other.esmClass)
			return false;
//		if (!Arrays.equals(message, other.message))
//			return false;
		if (priority != other.priority)
			return false;
		if (protocolId != other.protocolId)
			return false;
		if (registered != other.registered)
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
		return true;
	}

}
