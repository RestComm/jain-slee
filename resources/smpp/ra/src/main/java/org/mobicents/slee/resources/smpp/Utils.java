package org.mobicents.slee.resources.smpp;

import ie.omk.smpp.BadCommandIDException;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.util.SMPPDate;
import ie.omk.smpp.version.VersionException;

import java.util.Date;

import net.java.slee.resources.smpp.SmppTransaction;
import net.java.slee.resources.smpp.pdu.SmppDateFormatException;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;

import org.mobicents.slee.resources.smpp.pdu.AlertNotificationImpl;
import org.mobicents.slee.resources.smpp.pdu.CancelSMImpl;
import org.mobicents.slee.resources.smpp.pdu.CancelSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.DataSMImpl;
import org.mobicents.slee.resources.smpp.pdu.DataSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.DeliverSMImpl;
import org.mobicents.slee.resources.smpp.pdu.DeliverSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.GenericNackImpl;
import org.mobicents.slee.resources.smpp.pdu.QuerySMImpl;
import org.mobicents.slee.resources.smpp.pdu.QuerySMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.ReplaceSMImpl;
import org.mobicents.slee.resources.smpp.pdu.ReplaceSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitMultiImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitSMImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitSMRespImpl;

/**
 * 
 * @author amit bhayani
 *
 */
public class Utils {
	
	/**
	 * Define Events
	 */
	protected static final String ALERT_NOTIFICATION = "net.java.slee.resources.smpp.ALERT_NOTIFICATION";
	protected static final String GENERIC_NACK = "net.java.slee.resources.smpp.GENERIC_NACK";
	protected static final String DELIVER_SM = "net.java.slee.resources.smpp.DELIVER_SM";
	protected static final String DELIVER_SM_RESP = "net.java.slee.resources.smpp.DELIVER_SM_RESP";
	protected static final String DELIVERY_REPORT = "net.java.slee.resources.smpp.DELIVERY_REPORT";
	protected static final String SUBMIT_SM = "net.java.slee.resources.smpp.SUBMIT_SM";
	protected static final String SUBMIT_SM_RESP = "net.java.slee.resources.smpp.SUBMIT_SM_RESP";
	protected static final String DATA_SM = "net.java.slee.resources.smpp.DATA_SM";
	protected static final String DATA_SM_RESP = "net.java.slee.resources.smpp.DATA_SM_RESP";
	protected static final String SUBMIT_MULTI = "net.java.slee.resources.smpp.SUBMIT_MULTI";
	protected static final String SUBMIT_MULTI_RESP = "net.java.slee.resources.smpp.SUBMIT_MULTI_RESP";
	protected static final String QUERY_SM = "net.java.slee.resources.smpp.QUERY_SM";
	protected static final String QUERY_SM_RESP = "net.java.slee.resources.smpp.QUERY_SM_RESP";
	protected static final String CANCEL_SM = "net.java.slee.resources.smpp.CANCEL_SM";
	protected static final String CANCEL_SM_RESP = "net.java.slee.resources.smpp.CANCEL_SM_RESP";
	protected static final String REPLACE_SM = "net.java.slee.resources.smpp.REPLACE_SM";
	protected static final String REPLACE_SM_RESP = "net.java.slee.resources.smpp.REPLACE_SM_RESP";
	protected static final String SMPP_TIMEOUT_RESPONSE_SENT = "net.java.slee.resources.smpp.SMPP_TIMEOUT_RESPONSE_SENT";
	protected static final String SMPP_TIMEOUT_RESPONSE_RECEIVED = "net.java.slee.resources.smpp.SMPP_TIMEOUT_RESPONSE_RECEIVED";

	private SmppResourceAdaptor smppResourceAdaptor;

	protected Utils(SmppResourceAdaptor smppResourceAdaptor) {
		this.smppResourceAdaptor = smppResourceAdaptor;
	}

	protected String getAddressForEventFire(SmppRequest smppRequest) {
		long commandId = smppRequest.getCommandId();
		if (commandId == SmppRequest.ALERT_NOTIFICATION) {
			return ((AlertNotificationImpl) smppRequest).getEsmeAddress().getAddress();
		} else if (commandId == SmppRequest.CANCEL_SM) {
			return ((CancelSMImpl) smppRequest).getSourceAddress().getAddress();
		} else if (commandId == SmppRequest.DATA_SM) {
			return ((DataSMImpl) smppRequest).getSourceAddress().getAddress();
		} else if (commandId == SmppRequest.DELIVER_SM) {
			// For Deliver_SM, the address would be destination Address
			return ((DeliverSMImpl) smppRequest).getDestAddress().getAddress();
		} else if (commandId == SmppRequest.QUERY_SM) {
			return ((QuerySMImpl) smppRequest).getSourceAddress().getAddress();
		} else if (commandId == SmppRequest.REPLACE_SM) {
			return ((ReplaceSMImpl) smppRequest).getSourceAddress().getAddress();
		} else if (commandId == SmppRequest.SUBMIT_MULTI) {
			return ((SubmitMultiImpl) smppRequest).getSourceAddress().getAddress();
		} else if (commandId == SmppRequest.SUBMIT_SM) {
			return ((SubmitSMImpl) smppRequest).getSourceAddress().getAddress();
		}
		return null;
	}

	/*******************************************************************************************************************
	 * Convert org.mobicents messages to ie.omk messages
	 * 
	 * @throws BadCommandIDException
	 * @throws VersionException
	 * @throws SmppDateFormatException
	 ******************************************************************************************************************/

	protected ie.omk.smpp.message.SMPPRequest convertRequest(SmppRequest request) throws VersionException,
			BadCommandIDException, SmppDateFormatException {

		long commandId = request.getCommandId();

		if (commandId == SmppRequest.ALERT_NOTIFICATION) {

		} else if (commandId == SmppRequest.CANCEL_SM) {
			return this.getCancelSM((CancelSMImpl) request);
		} else if (commandId == SmppRequest.DATA_SM) {
			return this.getDataSM((DataSMImpl) request);
		} else if (commandId == SmppRequest.DELIVER_SM) {
			return this.getDeliverSM((DeliverSMImpl) request);
		} else if (commandId == SmppRequest.QUERY_SM) {
			return this.getQuerySM((QuerySMImpl) request);
		} else if (commandId == SmppRequest.REPLACE_SM) {
			return this.getReplaceSM((ReplaceSMImpl) request);
		} else if (commandId == SmppRequest.SUBMIT_MULTI) {
			// return this.getSubmitMultiSM((SubmitMultiImpl)request);

		} else if (commandId == SmppRequest.SUBMIT_SM) {
			return this.getSubmitSM((SubmitSMImpl) request);
		}

		return null;
	}

	protected ie.omk.smpp.message.SMPPResponse convertResponse(SmppResponse response) throws VersionException,
			BadCommandIDException, SmppDateFormatException {

		long commandId = response.getCommandId();

		if (commandId == SmppRequest.GENERIC_NACK) {
			return this.getGenericNack((GenericNackImpl) response);
		} else if (commandId == SmppRequest.CANCEL_SM_RESP) {
			return this.getCancelSMResp((CancelSMRespImpl) response);
		} else if (commandId == SmppRequest.DATA_SM_RESP) {
			return this.getDataSMResp((DataSMRespImpl) response);
		} else if (commandId == SmppRequest.DELIVER_SM_RESP) {
			return this.getDeliverSMResp((DeliverSMRespImpl) response);
		} else if (commandId == SmppRequest.QUERY_SM_RESP) {
			return this.getQuerySMResp((QuerySMRespImpl) response);
		} else if (commandId == SmppRequest.REPLACE_SM) {
			return this.getReplaceSMResp((ReplaceSMRespImpl) response);
		} else if (commandId == SmppRequest.SUBMIT_MULTI_RESP) {
			// return this.getSubmitMultiSM((SubmitMultiImpl)request);
			// TODO
		} else if (commandId == SmppRequest.SUBMIT_SM_RESP) {
			return this.getSubmitSMResp((SubmitSMRespImpl) response);
		}

		return null;
	}

	/**
	 * 
	 * @param genericNackImpl
	 * @return
	 */
	protected ie.omk.smpp.message.GenericNack getGenericNack(GenericNackImpl genericNackImpl) {
		ie.omk.smpp.message.GenericNack genericNack = new ie.omk.smpp.message.GenericNack();
		this.processSMPPPacket(genericNack);
		
		genericNack.setCommandStatus((int) genericNackImpl.getCommandStatus());
		genericNack.setSequenceNum((int) genericNackImpl.getSequenceNum());
		return genericNack;
	}

	protected ie.omk.smpp.message.DeliverSM getDeliverSM(DeliverSMImpl deliverSM) throws VersionException,
			BadCommandIDException {
		ie.omk.smpp.message.DeliverSM smppDeliverSm = new ie.omk.smpp.message.DeliverSM();
		this.processSMPPPacket(smppDeliverSm);

		smppDeliverSm.setSequenceNum((int) deliverSM.getSequenceNum());
		smppDeliverSm.setServiceType(deliverSM.getServiceType());
		smppDeliverSm.setSource(this.convertJavaNetAddress(deliverSM.getSourceAddress()));
		smppDeliverSm.setDestination(this.convertJavaNetAddress(deliverSM.getDestAddress()));
		smppDeliverSm.setEsmClass(deliverSM.getEsmClass());
		smppDeliverSm.setProtocolID(deliverSM.getProtocolID());
		smppDeliverSm.setPriority(deliverSM.getPriority());

		// Not used in DELIVER_SM
		// smppDeliverSm.setDeliveryTime(null);

		// Not used in DELIVER_SM
		// smppDeliverSm.setExpiryTime(null);
		smppDeliverSm.setRegistered(deliverSM.getRegisteredDelivery());

		// Not used in DELIVER_SM
		smppDeliverSm.setReplaceIfPresent(deliverSM.getReplaceIfPresentFlag());

		// Not used in DELIVER_SM
		smppDeliverSm.setDefaultMsg(deliverSM.getSmDefaultMsgID());

		smppDeliverSm.setMessage(deliverSM.getMessage());

		// Let the setting of DataCoding be last due to bug in stack
		smppDeliverSm.setDataCoding(deliverSM.getDataCoding());

		// TODO take care of TLV's

		return smppDeliverSm;
	}

	protected ie.omk.smpp.message.DeliverSMResp getDeliverSMResp(DeliverSMRespImpl deliverSMResp)
			throws VersionException, BadCommandIDException {
		ie.omk.smpp.message.DeliverSMResp smppdeliverSMResp = new ie.omk.smpp.message.DeliverSMResp();
		this.processSMPPPacket(smppdeliverSMResp);
		smppdeliverSMResp.setSequenceNum((int) deliverSMResp.getSequenceNum());

		// Unused and set to Null
		// smppdeliverSMResp.setMessageID(smppDeliverSMResp.getMessageId());

		return smppdeliverSMResp;
	}

	protected ie.omk.smpp.message.DataSM getDataSM(DataSMImpl dataSMImpl) throws VersionException,
			BadCommandIDException {
		ie.omk.smpp.message.DataSM smppdataSM = new ie.omk.smpp.message.DataSM();
		this.processSMPPPacket(smppdataSM);
		smppdataSM.setSequenceNum((int)dataSMImpl.getSequenceNum());
		smppdataSM.setServiceType(dataSMImpl.getServiceType());
		smppdataSM.setSource(this.convertJavaNetAddress(dataSMImpl.getSourceAddress()));
		smppdataSM.setDestination(this.convertJavaNetAddress(dataSMImpl.getDestAddress()));
		smppdataSM.setEsmClass(dataSMImpl.getEsmClass());
		smppdataSM.setRegistered(dataSMImpl.getRegisteredDelivery());

		smppdataSM.setDataCoding(dataSMImpl.getDataCoding());

		// TODO TLV
		return smppdataSM;
	}

	protected ie.omk.smpp.message.DataSMResp getDataSMResp(DataSMRespImpl dataSMRespImpl) throws VersionException,
			BadCommandIDException {
		ie.omk.smpp.message.DataSMResp smppdataSM = new ie.omk.smpp.message.DataSMResp();		
		this.processSMPPPacket(smppdataSM);
		smppdataSM.setSequenceNum((int) dataSMRespImpl.getSequenceNum());
		smppdataSM.setMessageId(dataSMRespImpl.getMessageID());

		// TODO : TLV
		return smppdataSM;
	}

	protected ie.omk.smpp.message.SubmitSM getSubmitSM(SubmitSMImpl smppSubmitSM) throws VersionException,
			BadCommandIDException, SmppDateFormatException {
		ie.omk.smpp.message.SubmitSM submitSMImpl = new ie.omk.smpp.message.SubmitSM();
		this.processSMPPPacket(submitSMImpl);
		
		submitSMImpl.setSequenceNum((int)smppSubmitSM.getSequenceNum());
		submitSMImpl.setServiceType(smppSubmitSM.getServiceType());
		submitSMImpl.setSource(this.convertJavaNetAddress(smppSubmitSM.getSourceAddress()));
		submitSMImpl.setDestination(this.convertJavaNetAddress(smppSubmitSM.getDestAddress()));
		submitSMImpl.setEsmClass(smppSubmitSM.getEsmClass());
		submitSMImpl.setProtocolID(smppSubmitSM.getProtocolID());
		submitSMImpl.setPriority(smppSubmitSM.getPriority());

		submitSMImpl.setDeliveryTime(this.getTime(smppSubmitSM.getScheduleDeliveryTime()));

		submitSMImpl.setExpiryTime(this.getTime(smppSubmitSM.getValidityPeriod()));
		submitSMImpl.setRegistered(smppSubmitSM.getRegisteredDelivery());

		submitSMImpl.setReplaceIfPresent(smppSubmitSM.getReplaceIfPresentFlag());
		submitSMImpl.setDefaultMsg(smppSubmitSM.getSmDefaultMsgID());

		submitSMImpl.setMessage(smppSubmitSM.getMessage());

		// Datacoding in last
		submitSMImpl.setDataCoding(smppSubmitSM.getDataCoding());
		// TODO take care of TLV's

		return submitSMImpl;
	}

	protected ie.omk.smpp.message.SubmitSMResp getSubmitSMResp(SubmitSMRespImpl smppSubmitSMResp)
			throws VersionException, BadCommandIDException {
		ie.omk.smpp.message.SubmitSMResp submitSMRespImpl = new ie.omk.smpp.message.SubmitSMResp();
		this.processSMPPPacket(submitSMRespImpl);
		submitSMRespImpl.setSequenceNum((int) smppSubmitSMResp.getSequenceNum());
		submitSMRespImpl.setMessageId(smppSubmitSMResp.getMessageID());
		return submitSMRespImpl;
	}

	protected ie.omk.smpp.message.QuerySM getQuerySM(QuerySMImpl smppQuerySM) throws VersionException,
			BadCommandIDException {
		ie.omk.smpp.message.QuerySM querySMImpl = new ie.omk.smpp.message.QuerySM();
		this.processSMPPPacket(querySMImpl);

		querySMImpl.setSequenceNum((int)smppQuerySM.getSequenceNum());
		querySMImpl.setMessageId(smppQuerySM.getMessageID());
		querySMImpl.setSource(this.convertJavaNetAddress(smppQuerySM.getSourceAddress()));
		return querySMImpl;
	}

	protected ie.omk.smpp.message.QuerySMResp getQuerySMResp(QuerySMRespImpl smppQuerySMResp) throws VersionException,
			BadCommandIDException, SmppDateFormatException {
		ie.omk.smpp.message.QuerySMResp querySMRespImpl = new ie.omk.smpp.message.QuerySMResp();
		this.processSMPPPacket(querySMRespImpl);

		querySMRespImpl.setSequenceNum((int) smppQuerySMResp.getSequenceNum());
		querySMRespImpl.setMessageId(smppQuerySMResp.getMessageID());
		querySMRespImpl.setFinalDate(this.getTime(smppQuerySMResp.getFinalDate()));
		querySMRespImpl.setMessageStatus(smppQuerySMResp.getMessageState());
		querySMRespImpl.setErrorCode(smppQuerySMResp.getErrorCode());

		return querySMRespImpl;
	}

	protected ie.omk.smpp.message.ReplaceSM getReplaceSM(ReplaceSMImpl smppReplaceSM) throws VersionException,
			BadCommandIDException, SmppDateFormatException {
		ie.omk.smpp.message.ReplaceSM replaceSMImpl = new ie.omk.smpp.message.ReplaceSM();
		this.processSMPPPacket(replaceSMImpl);
		replaceSMImpl.setSequenceNum((int)smppReplaceSM.getSequenceNum());
		replaceSMImpl.setMessageId(smppReplaceSM.getMessageID());
		replaceSMImpl.setSource(this.convertJavaNetAddress(smppReplaceSM.getSourceAddress()));
		replaceSMImpl.setDeliveryTime(this.getTime(smppReplaceSM.getScheduleDeliveryTime()));
		replaceSMImpl.setExpiryTime(this.getTime(smppReplaceSM.getValidityPeriod()));
		replaceSMImpl.setRegistered(smppReplaceSM.getRegisteredDelivery());
		replaceSMImpl.setDefaultMsg(smppReplaceSM.getSmDefaultMsgID());
		replaceSMImpl.setMessage(smppReplaceSM.getMessage());

		return replaceSMImpl;
	}

	protected ie.omk.smpp.message.ReplaceSMResp getReplaceSMResp(ReplaceSMRespImpl smppReplaceSMResp)
			throws VersionException, BadCommandIDException {
		ie.omk.smpp.message.ReplaceSMResp replaceSMRespImpl = new ie.omk.smpp.message.ReplaceSMResp();
		this.processSMPPPacket(replaceSMRespImpl);

		replaceSMRespImpl.setSequenceNum((int) smppReplaceSMResp.getSequenceNum());

		return replaceSMRespImpl;
	}

	protected ie.omk.smpp.message.CancelSM getCancelSM(CancelSMImpl smppCancelSM) throws VersionException,
			BadCommandIDException {
		ie.omk.smpp.message.CancelSM cancelSMImpl = new ie.omk.smpp.message.CancelSM();
		this.processSMPPPacket(cancelSMImpl);
		
		cancelSMImpl.setSequenceNum((int)smppCancelSM.getSequenceNum());
		cancelSMImpl.setServiceType(smppCancelSM.getServiceType());
		cancelSMImpl.setMessageId(smppCancelSM.getMessageID());
		cancelSMImpl.setSource(this.convertJavaNetAddress(smppCancelSM.getSourceAddress()));
		cancelSMImpl.setDestination(this.convertJavaNetAddress(smppCancelSM.getDestAddress()));

		return cancelSMImpl;
	}

	protected ie.omk.smpp.message.CancelSMResp getCancelSMResp(CancelSMRespImpl smppCancelSMResp)
			throws VersionException, BadCommandIDException {
		ie.omk.smpp.message.CancelSMResp cancelSMRespImpl = new ie.omk.smpp.message.CancelSMResp();
		this.processSMPPPacket(cancelSMRespImpl);

		cancelSMRespImpl.setSequenceNum((int) smppCancelSMResp.getSequenceNum());

		return cancelSMRespImpl;
	}

	/*******************************************************************************************************************
	 * Convert ie.omk messages to org.mobicents messages
	 ******************************************************************************************************************/
	protected  GenericNackImpl getGenericNack(ie.omk.smpp.message.GenericNack genericNackImpl) {
		GenericNackImpl genericNack = new GenericNackImpl(genericNackImpl.getCommandStatus());		
		genericNack.setSequenceNum((int) genericNackImpl.getSequenceNum());
		return genericNack;
	}
	
	protected DeliverSMImpl getDeliverSM(ie.omk.smpp.message.DeliverSM smppDeliverSM) {

		DeliverSMImpl deliverSm = new DeliverSMImpl(smppDeliverSM.getSequenceNum());

		deliverSm.setServiceType(smppDeliverSM.getServiceType());
		deliverSm.setSourceAddress(this.convertSmppApiAddress(smppDeliverSM.getSource()));
		deliverSm.setDestAddress(this.convertSmppApiAddress(smppDeliverSM.getDestination()));
		deliverSm.setEsmClass(smppDeliverSM.getEsmClass());
		deliverSm.setProtocolID(smppDeliverSM.getProtocolID());
		deliverSm.setPriority(smppDeliverSM.getPriority());

		// Not used in DELIVER_SM
		deliverSm.setScheduleDeliveryTime(null);

		// Not used in DELIVER_SM
		deliverSm.setValidityPeriod(null);
		deliverSm.setRegisteredDelivery(smppDeliverSM.getRegistered());

		// Not used in DELIVER_SM
		deliverSm.setReplaceIfPresentFlag(smppDeliverSM.getReplaceIfPresent());
		deliverSm.setDataCoding(smppDeliverSM.getDataCoding());

		// Not used in DELIVER_SM
		deliverSm.setSmDefaultMsgID(smppDeliverSM.getDefaultMsg());

		deliverSm.setMessage(smppDeliverSM.getMessage());

		// TODO take care of TLV's

		return deliverSm;
	}

	protected DeliverSMRespImpl getDeliverSMResp(ie.omk.smpp.message.DeliverSMResp smppDeliverSMResp) {
		DeliverSMRespImpl deliverSMResp = new DeliverSMRespImpl(smppDeliverSMResp.getCommandStatus());
		deliverSMResp.setSequenceNum(smppDeliverSMResp.getSequenceNum());

		// Unused and set to Null
		deliverSMResp.setMessageID(smppDeliverSMResp.getMessageId());

		return deliverSMResp;
	}

	protected DataSMImpl getDataSM(ie.omk.smpp.message.DataSM smppDataSM) {
		DataSMImpl dataSMImpl = new DataSMImpl(smppDataSM.getSequenceNum());
		dataSMImpl.setServiceType(smppDataSM.getServiceType());
		dataSMImpl.setSourceAddress(this.convertSmppApiAddress(smppDataSM.getSource()));
		dataSMImpl.setDestAddress(this.convertSmppApiAddress(smppDataSM.getDestination()));
		dataSMImpl.setEsmClass(smppDataSM.getEsmClass());
		dataSMImpl.setRegisteredDelivery(smppDataSM.getRegistered());
		dataSMImpl.setDataCoding(smppDataSM.getDataCoding());

		// TODO TLV
		return dataSMImpl;
	}

	protected DataSMRespImpl getDataSMResp(ie.omk.smpp.message.DataSMResp smppDataSMResp) {
		DataSMRespImpl dataSMRespImpl = new DataSMRespImpl(smppDataSMResp.getCommandStatus());
		dataSMRespImpl.setSequenceNum(smppDataSMResp.getSequenceNum());
		dataSMRespImpl.setMessageID(smppDataSMResp.getMessageId());

		// TODO : TLV
		return dataSMRespImpl;
	}

	protected SubmitSMImpl getSubmitSM(ie.omk.smpp.message.SubmitSM smppSubmitSM) {
		SubmitSMImpl submitSMImpl = new SubmitSMImpl(smppSubmitSM.getSequenceNum());

		submitSMImpl.setServiceType(smppSubmitSM.getServiceType());
		submitSMImpl.setSourceAddress(this.convertSmppApiAddress(smppSubmitSM.getSource()));
		submitSMImpl.setDestAddress(this.convertSmppApiAddress(smppSubmitSM.getDestination()));
		submitSMImpl.setEsmClass(smppSubmitSM.getEsmClass());
		submitSMImpl.setProtocolID(smppSubmitSM.getProtocolID());
		submitSMImpl.setPriority(smppSubmitSM.getPriority());

		submitSMImpl.setScheduleDeliveryTime(this.getTime(smppSubmitSM.getDeliveryTime()));

		submitSMImpl.setValidityPeriod(this.getTime(smppSubmitSM.getExpiryTime()));
		submitSMImpl.setRegisteredDelivery(smppSubmitSM.getRegistered());

		submitSMImpl.setReplaceIfPresentFlag(smppSubmitSM.getReplaceIfPresent());
		submitSMImpl.setDataCoding(smppSubmitSM.getDataCoding());

		submitSMImpl.setSmDefaultMsgID(smppSubmitSM.getDefaultMsg());

		submitSMImpl.setMessage(smppSubmitSM.getMessage());

		// TODO take care of TLV's

		return submitSMImpl;
	}

	protected SubmitSMRespImpl getSubmitSMResp(ie.omk.smpp.message.SubmitSMResp smppSubmitSMResp) {
		SubmitSMRespImpl submitSMRespImpl = new SubmitSMRespImpl(smppSubmitSMResp.getCommandStatus());
		submitSMRespImpl.setSequenceNum(smppSubmitSMResp.getSequenceNum());
		submitSMRespImpl.setMessageID(smppSubmitSMResp.getMessageId());
		return submitSMRespImpl;
	}

	protected QuerySMImpl getQuerySM(ie.omk.smpp.message.QuerySM smppQuerySM) {
		QuerySMImpl querySMImpl = new QuerySMImpl(smppQuerySM.getSequenceNum());

		querySMImpl.setMessageID(smppQuerySM.getMessageId());
		querySMImpl.setSourceAddress(this.convertSmppApiAddress(smppQuerySM.getSource()));
		return querySMImpl;
	}

	protected QuerySMRespImpl getQuerySMResp(ie.omk.smpp.message.QuerySMResp smppQuerySMResp) {
		QuerySMRespImpl querySMRespImpl = new QuerySMRespImpl(smppQuerySMResp.getCommandStatus());

		querySMRespImpl.setSequenceNum(smppQuerySMResp.getSequenceNum());
		querySMRespImpl.setMessageID(smppQuerySMResp.getMessageId());
		querySMRespImpl.setFinalDate(this.getTime(smppQuerySMResp.getFinalDate()));
		querySMRespImpl.setMessageState(smppQuerySMResp.getMessageStatus());
		querySMRespImpl.setErrorCode(smppQuerySMResp.getErrorCode());

		return querySMRespImpl;
	}

	protected ReplaceSMImpl getReplaceSM(ie.omk.smpp.message.ReplaceSM smppReplaceSM) {
		ReplaceSMImpl replaceSMImpl = new ReplaceSMImpl(smppReplaceSM.getSequenceNum());

		replaceSMImpl.setMessageID(smppReplaceSM.getMessageId());
		replaceSMImpl.setSourceAddress(this.convertSmppApiAddress(smppReplaceSM.getSource()));
		replaceSMImpl.setScheduleDeliveryTime(this.getTime(smppReplaceSM.getDeliveryTime()));
		replaceSMImpl.setValidityPeriod(this.getTime(smppReplaceSM.getExpiryTime()));
		replaceSMImpl.setRegisteredDelivery(smppReplaceSM.getRegistered());
		replaceSMImpl.setSmDefaultMsgID(smppReplaceSM.getDefaultMsg());
		replaceSMImpl.setMessage(smppReplaceSM.getMessage());

		return replaceSMImpl;
	}

	protected ReplaceSMRespImpl getReplaceSMResp(ie.omk.smpp.message.ReplaceSMResp smppReplaceSMResp) {
		ReplaceSMRespImpl replaceSMRespImpl = new ReplaceSMRespImpl(smppReplaceSMResp.getCommandStatus());

		replaceSMRespImpl.setSequenceNum(smppReplaceSMResp.getSequenceNum());

		return replaceSMRespImpl;
	}

	protected CancelSMImpl getCancelSM(ie.omk.smpp.message.CancelSM smppCancelSM) {
		CancelSMImpl cancelSMImpl = new CancelSMImpl(smppCancelSM.getSequenceNum());

		cancelSMImpl.setServiceType(smppCancelSM.getServiceType());
		cancelSMImpl.setMessageID(smppCancelSM.getMessageId());
		cancelSMImpl.setSourceAddress(this.convertSmppApiAddress(smppCancelSM.getSource()));
		cancelSMImpl.setDestAddress(this.convertSmppApiAddress(smppCancelSM.getDestination()));

		return cancelSMImpl;
	}

	protected CancelSMRespImpl getCancelSMResp(ie.omk.smpp.message.CancelSMResp smppCancelSMResp) {
		CancelSMRespImpl cancelSMRespImpl = new CancelSMRespImpl(smppCancelSMResp.getCommandStatus());

		cancelSMRespImpl.setSequenceNum(smppCancelSMResp.getSequenceNum());

		return cancelSMRespImpl;
	}

	protected String statusMessage(int status) {
		switch (status) {
		case SmppTransaction.ESME_ROK:
			return "No Error";
		case SmppTransaction.ESME_RINVMSGLEN:
			return "Message Length is invalid";
		case SmppTransaction.ESME_RINVCMDLEN:
			return "Command Length is invalid";
		case SmppTransaction.ESME_RINVCMDID:
			return "Invalid Command ID";
		case SmppTransaction.ESME_RINVBNDSTS:
			return "Incorrect BIND Status for given command";
		case SmppTransaction.ESME_RALYBND:
			return "ESME Already in Bound State";
		case SmppTransaction.ESME_RINVPRTFLG:
			return "Invalid Priority Flag";
		case SmppTransaction.ESME_RINVREGDLVFLG:
			return "Invalid Registered Delivery Flag";
		case SmppTransaction.ESME_RSYSERR:
			return "System Error";
		case SmppTransaction.ESME_RINVSRCADR:
			return "Invalid Source Address";
		case SmppTransaction.ESME_RINVDSTADR:
			return "Invalid Dest Addr";
		case SmppTransaction.ESME_RINVMSGID:
			return "Message ID is invalid";
		case SmppTransaction.ESME_RBINDFAIL:
			return "Bind Failed";
		case SmppTransaction.ESME_RINVPASWD:
			return "Invalid Password";
		case SmppTransaction.ESME_RINVSYSID:
			return "Invalid System ID";
		case SmppTransaction.ESME_RCANCELFAIL:
			return "Cancel SM Failed";
		case SmppTransaction.ESME_RREPLACEFAIL:
			return "Replace SM Failed";
		case SmppTransaction.ESME_RMSGQFUL:
			return "Message Queue Full";
		case SmppTransaction.ESME_RINVSERTYP:
			return "Invalid Service Type";
		case SmppTransaction.ESME_RINVNUMDESTS:
			return "Invalid number of destinations";
		case SmppTransaction.ESME_RINVDLNAME:
			return "Invalid Distribution List name";
		case SmppTransaction.ESME_RINVDESTFLAG:
			return "Destination flag is invalid (submit_multi)";
		case SmppTransaction.ESME_RINVSUBREP:
			return "Invalid ‘submit with replace’ request (i.e. submit_sm with replace_if_present_flag set)";
		case SmppTransaction.ESME_RINVESMCLASS:
			return "Invalid esm_class field data";
		case SmppTransaction.ESME_RCNTSUBDL:
			return "Cannot Submit to Distribution List";
		case SmppTransaction.ESME_RSUBMITFAIL:
			return "submit_sm or submit_multi failed";
		case SmppTransaction.ESME_RINVSRCTON:
			return "Invalid Source address TON";
		case SmppTransaction.ESME_RINVSRCNPI:
			return "Invalid Source address NPI";
		case SmppTransaction.ESME_RINVDSTTON:
			return "Invalid Destination address TON";
		case SmppTransaction.ESME_RINVDSTNPI:
			return "Invalid Destination address NPI";
		case SmppTransaction.ESME_RINVSYSTYP:
			return "Invalid system_type field";
		case SmppTransaction.ESME_RINVREPFLAG:
			return "Invalid replace_if_present flag";
		case SmppTransaction.ESME_RINVNUMMSGS:
			return "Invalid number of messages";
		case SmppTransaction.ESME_RTHROTTLED:
			return "Throttling error (ESME has exceeded allowed message limits)";
		case SmppTransaction.ESME_RINVSCHED:
			return "Invalid Scheduled Delivery Time";
		case SmppTransaction.ESME_RINVEXPIRY:
			return "Invalid message validity period (Expiry time)";
		case SmppTransaction.ESME_RINVDFTMSGID:
			return "Predefined Message Invalid or Not Found";
		case SmppTransaction.ESME_RX_T_APPN:
			return "ESME Receiver Temporary App Error Code";
		case SmppTransaction.ESME_RX_P_APPN:
			return "ESME Receiver Permanent App Error Code";
		case SmppTransaction.ESME_RX_R_APPN:
			return "ESME Receiver Reject Message Error Code";
		case SmppTransaction.ESME_RQUERYFAIL:
			return "query_sm request failed";
		case SmppTransaction.ESME_RINVOPTPARSTREAM:
			return "Error in the optional part of the PDU Body.";
		case SmppTransaction.ESME_ROPTPARNOTALLWD:
			return "Optional Parameter not allowed";
		case SmppTransaction.ESME_RINVPARLEN:
			return "Invalid Parameter Length.";
		case SmppTransaction.ESME_RMISSINGOPTPARAM:
			return "Expected Optional Parameter missing";
		case SmppTransaction.ESME_RINVOPTPARAMVAL:
			return "Invalid Optional Parameter Value";
		case SmppTransaction.ESME_RDELIVERYFAILURE:
			return "Delivery   Failure (used for data_sm_resp)";
		case SmppTransaction.ESME_RUNKNOWNERR:
			return "Unknown Error";
		case -1:
			return "Some exception occured. Look at log files";
		}
		return "Unknonw ststus code " + status;
	}

	private net.java.slee.resources.smpp.pdu.Address convertSmppApiAddress(ie.omk.smpp.Address okiSmppaddress) {
		if (okiSmppaddress == null) {
			return null;
		}
		return new org.mobicents.slee.resources.smpp.pdu.AddressImpl(okiSmppaddress.getTON(), okiSmppaddress.getNPI(),
				okiSmppaddress.getAddress());
	}

	private ie.omk.smpp.Address convertJavaNetAddress(net.java.slee.resources.smpp.pdu.Address address) {
		if (address == null) {
			return null;
		}
		return new ie.omk.smpp.Address(address.getAddressTon(), address.getAddressNpi(), address.getAddress());
	}

	private String getTime(SMPPDate smppDate) {
		if (smppDate != null) {
			return smppDate.toString();
		}
		return null;
	}

	private SMPPDate getTime(String smppDate) throws SmppDateFormatException {
		if (smppDate != null) {
			try {
				Long absoluteTime = Long.parseLong(smppDate);
				return new SMPPDate(new Date(absoluteTime));
			} catch (NumberFormatException e) {
				throw new SmppDateFormatException("Conversion of String to Date failed", e);
			}
		}
		return null;
	}
	
	private void processSMPPPacket(SMPPPacket smppPacket){
		smppPacket.setVersion(this.smppResourceAdaptor.smscConnection.getVersion());
		smppPacket.setAlphabet(this.smppResourceAdaptor.smscConnection.getDefaultAlphabet(), 0);
	}

}
