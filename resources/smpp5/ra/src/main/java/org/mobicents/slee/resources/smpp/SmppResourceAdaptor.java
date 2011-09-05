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

package org.mobicents.slee.resources.smpp;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.SLEEException;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.AlarmLevel;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.resource.StartActivityException;

import net.java.slee.resources.smpp.SmppTransaction;

import org.mobicents.protocols.smpp.BadCommandIDException;
import org.mobicents.protocols.smpp.NotBoundException;
import org.mobicents.protocols.smpp.Session;
import org.mobicents.protocols.smpp.message.Bind;
import org.mobicents.protocols.smpp.message.BindReceiver;
import org.mobicents.protocols.smpp.message.BindTransceiver;
import org.mobicents.protocols.smpp.message.BindTransmitter;
import org.mobicents.protocols.smpp.message.CommandId;
import org.mobicents.protocols.smpp.message.EnquireLink;
import org.mobicents.protocols.smpp.version.SMPPVersion;
import org.mobicents.protocols.smpp.version.VersionException;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;
import org.mobicents.slee.resource.cluster.MemberAddress;
import org.mobicents.slee.resources.smpp.pdu.AlertNotificationImpl;
import org.mobicents.slee.resources.smpp.pdu.BroadcastSMImpl;
import org.mobicents.slee.resources.smpp.pdu.BroadcastSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.CancelBroadcastSMImpl;
import org.mobicents.slee.resources.smpp.pdu.CancelBroadcastSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.CancelSMImpl;
import org.mobicents.slee.resources.smpp.pdu.CancelSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.DataSMImpl;
import org.mobicents.slee.resources.smpp.pdu.DataSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.DeliverSMImpl;
import org.mobicents.slee.resources.smpp.pdu.DeliverSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.GenericNackImpl;
import org.mobicents.slee.resources.smpp.pdu.QueryBroadcastSMImpl;
import org.mobicents.slee.resources.smpp.pdu.QueryBroadcastSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.QuerySMImpl;
import org.mobicents.slee.resources.smpp.pdu.QuerySMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.ReplaceSMImpl;
import org.mobicents.slee.resources.smpp.pdu.ReplaceSMRespImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitMultiImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitMultiRespImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitSMImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitSMRespImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
@SuppressWarnings("rawtypes")
public class SmppResourceAdaptor implements FaultTolerantResourceAdaptor, org.mobicents.protocols.smpp.event.SessionObserver {

	private transient Tracer tracer;
	private transient ResourceAdaptorContext raContext;
	private transient SleeEndpoint sleeEndpoint = null;
	private transient EventLookupFacility eventLookup = null;
	private transient SmppSessionImpl smppSession = null;
	private transient FireableEventTypeCache eventTypeCache;
	private transient FireableEventTypeFilter eventTypeFilter;

	private transient FaultTolerantResourceAdaptorContext ftRAContext;
	private SmppResourceAdaptorUsageParameters usageParams; 
	
	private Utils utils;

	private Thread linkMonitorThread;

	public String smscAlarm;

	/**
	 * Configuration Properties
	 */
	private String host = "localhost";
	private int port = 2727;
	private String systemID = "1";
	private String systemType = "ESME";
	private String password = "1";
	private int addressTON = 0;
	private int addressNPI = 1;
	private String addressRange = "50";
	private int enquireLinkTimeout = 1000 * 30;
	private String bindType = BindType.TRANSMITTER.toString();
	private boolean statsOn;
	private int smppResponseReceivedTimeout = 5000;
	private int smppResponseSentTimeout = 5000;

	/**
	 * SMPP API related variables
	 */
	protected org.mobicents.protocols.smpp.Session protoSmppSession = null;
	protected org.mobicents.protocols.smpp.util.SequenceNumberScheme seq = null;;
	private Semaphore semaphore = new Semaphore(0);
	private int bindStatus;

	private volatile boolean isRAStoped = false;

	private Bind bind = null;
	
	private long lastMessagesExchanged = 0l;
	private long lastStatsUpdatedTime = 0l;

	/**
	 * ActivityHandle holder
	 */
	private ConcurrentHashMap<SmppTransactionHandle, SmppTransactionImpl> handleVsActivityMap = new ConcurrentHashMap<SmppTransactionHandle, SmppTransactionImpl>();

	public SmppResourceAdaptor() {
		// TODO Auto-generated constructor stub
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public void setPort(Integer port) {
		this.port = port.intValue();
	}

	public Integer getPort() {
		return new Integer(port);
	}

	public String getSystemId() {
		return systemID;
	}

	public void setSystemId(String systemID) {
		this.systemID = systemID;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAddressTon() {
		return addressTON;
	}

	public void setAddressTon(Integer addressTON) {
		this.addressTON = addressTON.intValue();
	}

	public Integer getAddressNpi() {
		return new Integer(addressNPI);
	}

	public void setAddressNpi(Integer addressNPI) {
		this.addressNPI = addressNPI.intValue();
	}

	public String getAddressRange() {
		return addressRange;
	}

	public void setAddressRange(String addressRange) {
		this.addressRange = addressRange;
	}

	public Integer getEnquireLinkTimeout() {
		return this.enquireLinkTimeout;
	}

	public void setEnquireLinkTimeout(Integer enquireLinkTimeout) {
		this.enquireLinkTimeout = enquireLinkTimeout.intValue() * 1000;
	}

	public String getBindType() {
		return bindType;
	}

	public void setBindType(String bindTypeArg) {
		bindType = bindTypeArg;
	}

	public int getSmppResponseReceivedTimeout() {
		return smppResponseReceivedTimeout;
	}

	public void setSmppResponseReceivedTimeout(int smppResponseReceivedTimeout) {
		if (smppResponseReceivedTimeout < 100) {
			smppResponseReceivedTimeout = 5000;
		}
		this.smppResponseReceivedTimeout = smppResponseReceivedTimeout;
	}

	public int getSmppResponseSentTimeout() {
		return smppResponseSentTimeout;
	}

	public void setSmppResponseSentTimeout(int smppResponseSentTimeout) {
		if (smppResponseSentTimeout < 100) {
			smppResponseSentTimeout = 5000;
		}
		this.smppResponseSentTimeout = smppResponseSentTimeout;
	}

	public void dataRemoved(Serializable arg0) {
		// TODO Auto-generated method stub

	}

	public void failOver(Serializable arg0) {
		if (this.tracer.isInfoEnabled()) {
			this.tracer.info("Failed over the SMPP. Available memebers now ");
			for (MemberAddress memAdd : this.ftRAContext.getMembers()) {
				this.tracer.info(memAdd.toString());
			}
		}
	}

	public void setFaultTolerantResourceAdaptorContext(FaultTolerantResourceAdaptorContext arg0) {
		tracer.info("setFaultTolerantResourceAdaptorContext(FaultTolerantResourceAdaptorContext arg0)");
		this.ftRAContext = arg0;
	}

	public void unsetFaultTolerantResourceAdaptorContext() {
		tracer.info("unsetFaultTolerantResourceAdaptorContext()");
		this.ftRAContext = null;
	}

	public void activityEnded(ActivityHandle activityHandle) {
		this.handleVsActivityMap.remove(activityHandle);
	}

	public void activityUnreferenced(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void administrativeRemove(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5,
			FailureReason arg6) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingSuccessful(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public Object getActivity(ActivityHandle activityHandle) {
		return this.handleVsActivityMap.get(activityHandle);
	}

	public ActivityHandle getActivityHandle(Object obj) {
		return ((SmppTransactionImpl) obj).getHandle();
	}

	public Marshaler getMarshaler() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getResourceAdaptorInterface(String arg0) {
		return this.smppSession;
	}

	public void queryLiveness(ActivityHandle activityHandle) {
		if (getActivity(activityHandle) == null) {
			this.tracer.warning("The queryLiveness failed for activity " + activityHandle + " Ending this activity");
			this.sleeEndpoint.endActivity(activityHandle);
		}
	}

	public void raActive() {
		if (this.tracer.isInfoEnabled()) {
			tracer.info("Activation RA " + this.raContext.getEntityName());
		}
		
		this.lastMessagesExchanged = 0;
		this.lastStatsUpdatedTime = System.currentTimeMillis();
		
		try {
			bindSMSC();

		} catch (IOException e) {
			this.tracer.severe("Binding to SMSC Failed ", e);
		}
		
		this.isRAStoped = false;
		
		// Start the ENQUIRE Link Thread in any case
		linkMonitorThread = new Thread(new LinkMonitor());
		linkMonitorThread.start();
	}

	public void raConfigurationUpdate(ConfigProperties properties) {
		raConfigure(properties);
	}

	public void raConfigure(ConfigProperties properties) {
		if (tracer.isFineEnabled()) {
			tracer.fine("Configuring RA Entity " + this.raContext.getEntityName());
		}

		setAddressNpi((Integer) properties.getProperty("addressNpi").getValue());
		setAddressRange((String) properties.getProperty("addressRange").getValue());
		setAddressTon((Integer) properties.getProperty("addressTon").getValue());
		setEnquireLinkTimeout((Integer) properties.getProperty("enquireLinkTimeout").getValue());
		setHost((String) properties.getProperty("host").getValue());
		setPassword((String) properties.getProperty("password").getValue());
		setPort((Integer) properties.getProperty("port").getValue());
		setSystemId((String) properties.getProperty("systemId").getValue());
		setSystemType((String) properties.getProperty("systemType").getValue());
		setBindType((String) properties.getProperty("bindType").getValue());
		setSmppResponseReceivedTimeout((Integer) properties.getProperty("smppResponseReceivedTimeout").getValue());
		setSmppResponseSentTimeout((Integer) properties.getProperty("smppResponseSentTimeout").getValue());
		statsOn = (Boolean) properties.getProperty("statsOn").getValue();
		if (tracer.isFineEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("addressNpi = ").append(getAddressNpi()).append("\n").append("addressTon = ").append(getAddressTon()).append("\n")
					.append("addressRange = ").append(getAddressRange()).append("\n").append("host = ").append(getHost()).append("\n").append("port = ")
					.append(getPort()).append("\n").append("systemId = ").append(getSystemId()).append("\n").append("systemType = ").append(getSystemType())
					.append("\n").append("password = ").append(getPassword()).append("\n").append("bindType = ").append(getBindType()).append("\n");
			tracer.fine(sb.toString());
		}

	}

	public void raInactive() {
		this.unbindSMSC();
	}

	public void raStopping() {
		isRAStoped = true;
		this.smppSession.setIsAlive(false);

		if (linkMonitorThread != null) {
			linkMonitorThread.interrupt();
		}
	}

	public void raUnconfigure() {
		setAddressNpi(-1);
		setAddressRange(null);
		setAddressTon(-1);
		setEnquireLinkTimeout(-1);
		setHost(null);
		setPassword(null);
		setPort(-1);
		setSystemId(null);
		setSystemType(null);
		setBindType(null);
	}

	public void raVerifyConfiguration(ConfigProperties properties) throws InvalidConfigurationException {
		String addressRange = null;
		String host = null;
		String password = null;
		String systemId = null;
		String systemType = null;
		BindType binidType = null;
		try {

			addressRange = (String) properties.getProperty("addressRange").getValue();
			host = (String) properties.getProperty("host").getValue();
			password = (String) properties.getProperty("password").getValue();
			systemId = (String) properties.getProperty("systemId").getValue();
			systemType = (String) properties.getProperty("systemType").getValue();
			binidType = BindType.getBindType((String) properties.getProperty("bindType").getValue());
		} catch (Throwable e) {
			throw new InvalidConfigurationException(e.getMessage(), e);
		}

		if (addressRange == null) {
			throw new InvalidConfigurationException("Address Range cannot be null");
		} else if (host == null) {
			throw new InvalidConfigurationException("Host (of SMSC) cannot be null");
		} else if (password == null) {
			throw new InvalidConfigurationException("Password cannot be null");
		} else if (systemId == null) {
			throw new InvalidConfigurationException("System ID cannot be null");
		} else if (systemType == null) {
			throw new InvalidConfigurationException("System Type cannot be null");
		} else if (binidType == null) {
			throw new InvalidConfigurationException("Bind Type cannot be null");
		}

	}

	public void serviceActive(ReceivableService receivableService) {
		this.eventTypeFilter.serviceActive(receivableService);

	}

	public void serviceInactive(ReceivableService receivableService) {
		this.eventTypeFilter.serviceInactive(receivableService);
	}

	public void serviceStopping(ReceivableService receivableService) {
		this.eventTypeFilter.serviceStopping(receivableService);
	}

	public void setResourceAdaptorContext(ResourceAdaptorContext raContext) {
		this.tracer = raContext.getTracer(getClass().getSimpleName());
		this.raContext = raContext;
		this.sleeEndpoint = raContext.getSleeEndpoint();
		this.eventLookup = raContext.getEventLookupFacility();
		this.smppSession = new SmppSessionImpl(this);
		this.eventTypeCache = new FireableEventTypeCache(tracer);
		this.eventTypeFilter = new FireableEventTypeFilter();
		this.usageParams = (SmppResourceAdaptorUsageParameters) raContext.getDefaultUsageParameterSet();
		this.utils = new Utils(this);
	}

	public void unsetResourceAdaptorContext() {
		this.raContext = null;
		this.sleeEndpoint = null;
		this.eventTypeCache = null;
		this.eventTypeFilter = null;
		this.usageParams = null;
	}

	/**
	 * ConnectionObserver methods
	 */
	public void packetReceived(Session source, org.mobicents.protocols.smpp.message.SMPPPacket packet) {
		String entityName = raContext.getEntityName();
		
		//Update usage params
		incrementUsageParams(packet.getCommandId());
		
		switch (packet.getCommandId()) {
		// A connected ESME has requested to bind as an ESME Transceiver
		// (by issuing a bind_transceiver PDU) and has received a response from
		// the SMSC authorising its Bind request. An ESME bound as a Transceiver
		// supports the complete set of operations supported by a Transmitter
		// ESME and a Receiver ESME. Thus an ESME bound as a transceiver may
		// send short messages to an SMSC for onward delivery to a Mobile
		// Station
		// or to another ESME. The ESME may also receive short messages from an
		// SMSC which may be originated by a mobile station, by another ESME or
		// by the SMSC itself (for example an SMSC delivery receipt).
		case CommandId.BIND_TRANSCEIVER_RESP:
			bindStatus = packet.getCommandStatus();
			if (tracer.isFineEnabled()) {
				tracer.fine(entityName + " receive bind_transaceiver_resp. Statu = " + bindStatus + " Message = " + this.utils.statusMessage(bindStatus));
			}
			semaphore.release();
			break;

		case CommandId.BIND_RECEIVER_RESP:
			bindStatus = packet.getCommandStatus();
			if (tracer.isFineEnabled()) {
				tracer.fine(entityName + " receive bind_receiver_resp. Statu = " + bindStatus + " Message = " + this.utils.statusMessage(bindStatus));
			}
			semaphore.release();

			break;

		case CommandId.BIND_TRANSMITTER_RESP:
			bindStatus = packet.getCommandStatus();
			if (tracer.isFineEnabled()) {
				tracer.fine(entityName + " receive bind_transmitter_resp. Statu = " + bindStatus + " Message = " + this.utils.statusMessage(bindStatus));
			}
			semaphore.release();
			break;

		// An ESME has unbound from the SMSC and has closed the network
		// connection. The SMSC may also unbind from the ESME.
		case CommandId.UNBIND_RESP: {
			if (tracer.isFineEnabled()) {
				tracer.fine(raContext.getEntityName() + " unbound successfuly");
			}
			semaphore.release();
			break;
		}

		// This message can be sent by either the ESME or SMSC and is used
		// to provide a confidence-check of the communication path between
		// an ESME and an SMSC. On receipt of this request the receiving
		// party should respond with an enquire_link_resp, thus verifying
		// that the application level connection between the SMSC and the
		// ESME is functioning. The ESME may also respond by sending any
		// valid SMPP primitive.
		case CommandId.ENQUIRE_LINK:
			// TODO reply with enquire_link_resp
			if (tracer.isFineEnabled()) {
				tracer.fine("Enquire link packet received");
			}
			break;

		case CommandId.ENQUIRE_LINK_RESP:
			if (tracer.isFineEnabled()) {
				tracer.fine("Enquire link response packet received");
			}
			break;

		case CommandId.DELIVER_SM: {
			DeliverSMImpl deliverSMImpl = new DeliverSMImpl((org.mobicents.protocols.smpp.message.DeliverSM) packet);

			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(deliverSMImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				if (isEsmClassUserMessage(deliverSMImpl.getEsmClass())) {
					fireEvent(Utils.DELIVER_SM, txImpl, deliverSMImpl);
				} else {
					fireEvent(Utils.DELIVERY_REPORT, txImpl, deliverSMImpl);
				}
			}
			break;
		}
		// The command acknowledges deliver_sm message.
		case CommandId.DELIVER_SM_RESP: {
			// TODO : Take care of Delivery Acknowledgement
			DeliverSMRespImpl deliverSMRespImpl = new DeliverSMRespImpl((org.mobicents.protocols.smpp.message.DeliverSMResp) packet);
			try {
				SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(deliverSMRespImpl, false, SmppTransactionType.OUTGOING);
				txImpl.cancelResponseNotReceivedTimeout();
				fireEvent(Utils.DELIVER_SM_RESP, txImpl, deliverSMRespImpl);
				this.endActivity(txImpl);
			} catch (Exception e) {
				this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
			}
			break;
		}

		case CommandId.DATA_SM: {
			DataSMImpl dataSMImpl = new DataSMImpl((org.mobicents.protocols.smpp.message.DataSM) packet);
			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(dataSMImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				fireEvent(Utils.DATA_SM, txImpl, dataSMImpl);
			}
			break;
		}
		case CommandId.DATA_SM_RESP: {
			DataSMRespImpl dataSMRespImpl = new DataSMRespImpl((org.mobicents.protocols.smpp.message.DataSMResp) packet);
			try {
				SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(dataSMRespImpl, false, SmppTransactionType.OUTGOING);
				txImpl.cancelResponseNotReceivedTimeout();
				fireEvent(Utils.DATA_SM_RESP, txImpl, dataSMRespImpl);
				this.endActivity(txImpl);
			} catch (Exception e) {
				this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
			}

			break;
		}

		case CommandId.SUBMIT_SM: {
			SubmitSMImpl submitSMImpl = new SubmitSMImpl((org.mobicents.protocols.smpp.message.SubmitSM) packet);
			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(submitSMImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				fireEvent(Utils.SUBMIT_SM, txImpl, submitSMImpl);
			}
			break;
		}

		case CommandId.SUBMIT_SM_RESP: {
			SubmitSMRespImpl submitSMRespImpl = new SubmitSMRespImpl((org.mobicents.protocols.smpp.message.SubmitSMResp) packet);
			try {
				SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(submitSMRespImpl, false, SmppTransactionType.OUTGOING);
				txImpl.cancelResponseNotReceivedTimeout();
				fireEvent(Utils.SUBMIT_SM_RESP, txImpl, submitSMRespImpl);
				this.endActivity(txImpl);
			} catch (Exception e) {
				this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
			}

			break;
		}

		case CommandId.SUBMIT_MULTI: {
			SubmitMultiImpl submitMultiImpl = new SubmitMultiImpl((org.mobicents.protocols.smpp.message.SubmitMulti) packet);
			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(submitMultiImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				fireEvent(Utils.SUBMIT_MULTI, txImpl, submitMultiImpl);
			}
			break;
		}

		case CommandId.SUBMIT_MULTI_RESP: {
			SubmitMultiRespImpl submitMultiRespImpl = new SubmitMultiRespImpl((org.mobicents.protocols.smpp.message.SubmitMultiResp) packet);
			try {
				SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(submitMultiRespImpl, false, SmppTransactionType.OUTGOING);
				txImpl.cancelResponseNotReceivedTimeout();
				fireEvent(Utils.SUBMIT_MULTI_RESP, txImpl, submitMultiRespImpl);
				this.endActivity(txImpl);
			} catch (Exception e) {
				this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
			}

			break;
		}

		case CommandId.QUERY_SM: {
			QuerySMImpl querySMImpl = new QuerySMImpl((org.mobicents.protocols.smpp.message.QuerySM) packet);
			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(querySMImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				fireEvent(Utils.QUERY_SM, txImpl, querySMImpl);
			}
			break;
		}

		case CommandId.QUERY_SM_RESP: {
			QuerySMRespImpl querySMRespImpl = new QuerySMRespImpl((org.mobicents.protocols.smpp.message.QuerySMResp) packet);
			try {
				SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(querySMRespImpl, false, SmppTransactionType.OUTGOING);
				txImpl.cancelResponseNotReceivedTimeout();
				fireEvent(Utils.QUERY_SM_RESP, txImpl, querySMRespImpl);
				this.endActivity(txImpl);
			} catch (Exception e) {
				this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
			}

			break;
		}

		case CommandId.REPLACE_SM: {
			ReplaceSMImpl replaceSMImpl = new ReplaceSMImpl((org.mobicents.protocols.smpp.message.ReplaceSM) packet);
			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(replaceSMImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				fireEvent(Utils.REPLACE_SM, txImpl, replaceSMImpl);
			}
			break;
		}

		case CommandId.REPLACE_SM_RESP: {
			ReplaceSMRespImpl replaceSMRespImpl = new ReplaceSMRespImpl((org.mobicents.protocols.smpp.message.ReplaceSMResp) packet);
			try {
				SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(replaceSMRespImpl, false, SmppTransactionType.OUTGOING);
				txImpl.cancelResponseNotReceivedTimeout();
				fireEvent(Utils.REPLACE_SM_RESP, txImpl, replaceSMRespImpl);
				this.endActivity(txImpl);
			} catch (Exception e) {
				this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
			}

			break;
		}

		case CommandId.CANCEL_SM: {
			CancelSMImpl cancelSMImpl = new CancelSMImpl((org.mobicents.protocols.smpp.message.CancelSM) packet);
			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(cancelSMImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				fireEvent(Utils.CANCEL_SM, txImpl, cancelSMImpl);
			}
			break;
		}

		case CommandId.CANCEL_SM_RESP: {
			CancelSMRespImpl cancelSMRespImpl = new CancelSMRespImpl((org.mobicents.protocols.smpp.message.CancelSMResp) packet);
			try {
				SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(cancelSMRespImpl, false, SmppTransactionType.OUTGOING);
				txImpl.cancelResponseNotReceivedTimeout();
				fireEvent(Utils.CANCEL_SM_RESP, txImpl, cancelSMRespImpl);
				this.endActivity(txImpl);
			} catch (Exception e) {
				this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
			}

			break;
		}

		case CommandId.BROADCAST_SM: {
			BroadcastSMImpl broadcastSMImpl = new BroadcastSMImpl((org.mobicents.protocols.smpp.message.BroadcastSM) packet);
			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(broadcastSMImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				fireEvent(Utils.BROADCAST_SM, txImpl, broadcastSMImpl);
			}
			break;
		}

		case CommandId.BROADCAST_SM_RESP: {
			BroadcastSMRespImpl broadcastSMRespImpl = new BroadcastSMRespImpl((org.mobicents.protocols.smpp.message.BroadcastSMResp) packet);
			try {
				SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(broadcastSMRespImpl, false, SmppTransactionType.OUTGOING);
				txImpl.cancelResponseNotReceivedTimeout();
				fireEvent(Utils.BROADCAST_SM_RESP, txImpl, broadcastSMRespImpl);
				this.endActivity(txImpl);
			} catch (Exception e) {
				this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
			}

			break;
		}

		case CommandId.CANCEL_BROADCAST_SM: {
			CancelBroadcastSMImpl cancelBroadcastSMImpl = new CancelBroadcastSMImpl((org.mobicents.protocols.smpp.message.CancelBroadcastSM) packet);
			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(cancelBroadcastSMImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				fireEvent(Utils.CANCEL_BROADCAST_SM, txImpl, cancelBroadcastSMImpl);
			}
			break;
		}

		case CommandId.CANCEL_BROADCAST_SM_RESP: {
			CancelBroadcastSMRespImpl cancelBroadcastSMRespImpl = new CancelBroadcastSMRespImpl(
					(org.mobicents.protocols.smpp.message.CancelBroadcastSMResp) packet);
			try {
				SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(cancelBroadcastSMRespImpl, false, SmppTransactionType.OUTGOING);
				txImpl.cancelResponseNotReceivedTimeout();
				fireEvent(Utils.CANCEL_BROADCAST_SM_RESP, txImpl, cancelBroadcastSMRespImpl);
				this.endActivity(txImpl);
			} catch (Exception e) {
				this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
			}

			break;
		}

		case CommandId.QUERY_BROADCAST_SM: {
			QueryBroadcastSMImpl queryBroadcastSMImpl = new QueryBroadcastSMImpl((org.mobicents.protocols.smpp.message.QueryBroadcastSM) packet);
			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(queryBroadcastSMImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				fireEvent(Utils.QUERY_BROADCAST_SM, txImpl, queryBroadcastSMImpl);
			}
			break;
		}

		case CommandId.QUERY_BROADCAST_SM_RESP: {
			QueryBroadcastSMRespImpl queryBroadcastSMRespImpl = new QueryBroadcastSMRespImpl((org.mobicents.protocols.smpp.message.QueryBroadcastSMResp) packet);
			try {
				SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(queryBroadcastSMRespImpl, false, SmppTransactionType.OUTGOING);
				txImpl.cancelResponseNotReceivedTimeout();
				fireEvent(Utils.QUERY_BROADCAST_SM_RESP, txImpl, queryBroadcastSMRespImpl);
				this.endActivity(txImpl);
			} catch (Exception e) {
				this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
			}
			break;
		}

		case CommandId.GENERIC_NACK: {
			GenericNackImpl genericNackImpl = new GenericNackImpl((org.mobicents.protocols.smpp.message.GenericNack) packet);
			if (genericNackImpl.getCommandStatus() == SmppTransaction.ESME_ROK) {
				if (this.tracer.isFineEnabled()) {
					this.tracer.fine("Receievd GENERIC_NACK acknowledgemet " + genericNackImpl);
				}
			} else {
				// This is Error Response to one of the Request sent

				try {
					SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(genericNackImpl, false, SmppTransactionType.OUTGOING);
					txImpl.cancelResponseNotReceivedTimeout();
					fireEvent(Utils.GENERIC_NACK, txImpl, genericNackImpl);
					this.endActivity(txImpl);
				} catch (Exception e) {
					this.tracer.warning("Activity not found for received SMPP Response " + packet, e);
				}

			}
			break;
		}

		case CommandId.ALERT_NOTIFICATION: {
			AlertNotificationImpl alertNotiImpl = new AlertNotificationImpl((org.mobicents.protocols.smpp.message.AlertNotification) packet);

			SmppTransactionImpl txImpl = this.smppSession.getSmppTransactionImpl(alertNotiImpl, true, SmppTransactionType.INCOMING);
			if (txImpl != null) {
				fireEvent(Utils.ALERT_NOTIFICATION, txImpl, alertNotiImpl);
			}

			this.endActivity(txImpl);
			break;
		}
		default:
			tracer.warning("Unexpected packet received! Id = 0x" + Integer.toHexString(packet.getCommandId()));
		}
	}

	public void update(Session source, org.mobicents.protocols.smpp.event.SMPPEvent smppEvent) {
		switch (smppEvent.getType()) {
		case org.mobicents.protocols.smpp.event.SMPPEvent.RECEIVER_START:
			if (this.tracer.isFineEnabled()) {
				this.tracer.fine("Receiver Thread started for SMPP RA " + this.raContext.getEntityName());
			}
			break;
		case org.mobicents.protocols.smpp.event.SMPPEvent.RECEIVER_EXCEPTION:
			if (this.tracer.isWarningEnabled()) {
				org.mobicents.protocols.smpp.event.ReceiverExceptionEvent recExcepEvent = (org.mobicents.protocols.smpp.event.ReceiverExceptionEvent) smppEvent;
				this.tracer.warning("The Receiver Thread for SMPP RA " + this.raContext.getEntityName() + " throws recovrable Exception ",
						recExcepEvent.getException());
			}
			//This is recovrable error. Don't reconnect!
			//this.reconnect();
			break;
		case org.mobicents.protocols.smpp.event.SMPPEvent.RECEIVER_EXIT:
			org.mobicents.protocols.smpp.event.ReceiverExitEvent recExitEvent = (org.mobicents.protocols.smpp.event.ReceiverExitEvent) smppEvent;

			switch (recExitEvent.getReason()) {
			case org.mobicents.protocols.smpp.event.ReceiverExitEvent.EXCEPTION:
				this.tracer.severe("The Receiver Thread for SMPP RA " + this.raContext.getEntityName() + " exited with error ", recExitEvent.getException());
				this.reconnect();
				break;
			case org.mobicents.protocols.smpp.event.ReceiverExitEvent.BIND_TIMEOUT:
				if (this.tracer.isFineEnabled()) {
					this.tracer.fine("The Receiver Thread for SMPP RA " + this.raContext.getEntityName() + " exited with BIND_TIMEOUT");
				}
				break;
			case org.mobicents.protocols.smpp.event.ReceiverExitEvent.UNKNOWN:
				//This is graceful shutdown. No need to reconnect here
				if (this.tracer.isFineEnabled()) {
					this.tracer.fine("The Receiver Thread for SMPP RA " + this.raContext.getEntityName() + " exited normally");
				}
				break;
			}
			break;
		}

	}

	/**
	 * Private Methods
	 */
	private void bindSMSC() throws UnknownHostException, IOException {
		
		this.bindStatus = -1;
		
		protoSmppSession = new org.mobicents.protocols.smpp.Session(host, port);
		seq = protoSmppSession.getSequenceNumberScheme();
		protoSmppSession.addObserver(this);

		protoSmppSession.setVersion(SMPPVersion.VERSION_5_0);
		// smscConnection.autoAckLink(true);
		// smscConnection.autoAckMessages(true);

		IOException ioException = null;
		// Bind
		try {
			this.createBind();
			protoSmppSession.bind(bind);
			
			//Update usage params
			incrementUsageParams(bind.getCommandId());
			
			semaphore.drainPermits();
			semaphore.tryAcquire(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			this.tracer.severe("Binding to SMS failed", e);
			this.bindStatus = -1;
		} catch (IOException e) {
			this.bindStatus = -1;
			ioException = e;
		}

		if (this.bindStatus != SmppTransaction.ESME_ROK) {
			AlarmFacility alarmFacility = raContext.getAlarmFacility();
			smscAlarm = alarmFacility.raiseAlarm(raContext.getEntityName(), "SMSCALARM", AlarmLevel.CRITICAL,
					"SMSCALARM: bind status " + this.utils.statusMessage(bindStatus));
			if (ioException != null) {
				throw ioException;
			} else {
				throw new IOException("Could not bind to SMSC. The reason is " + this.utils.statusMessage(bindStatus));
			}
		}
		tracer.info("Successfully bound to SMSC. ");
		this.smppSession.setIsAlive(true);

		if (smscAlarm != null) {
			AlarmFacility alarmFacility = raContext.getAlarmFacility();
			alarmFacility.clearAlarm(smscAlarm);
		}

	}

	private Bind createBind() {
		BindType bindType = BindType.getBindType(this.getBindType());

		switch (bindType) {
		case TRANSMITTER:
			bind = new BindTransmitter();
			break;
		case RECEIVER:
			bind = new BindReceiver();
			break;
		case TRANSCEIVER:
			bind = new BindTransceiver();
			break;
		}
		bind.setAddressTon(this.getAddressTon());
		bind.setAddressNpi(this.getAddressNpi());
		bind.setAddressRange(this.getAddressRange());
		bind.setSystemType(this.getSystemType());
		bind.setSystemId(this.getSystemId());
		bind.setPassword(this.getPassword());
		return bind;
	}

	/**
	 * reconnect() is called from LinkMonitor when connectivity between RA and
	 * SMSC fails. lets set isBound to true so while loop of LinkMonitor keeps
	 * trying to establish link again
	 */
	private void reconnect() {
		try {
			unbindSMSC();

			this.protoSmppSession.closeLink();
		} catch (Exception e) {
			// ignore all errors during unbinding
		}
		try {

			bindSMSC();
		} catch (Exception e) {
			if (tracer.isSevereEnabled())
				tracer.severe("Reconnect error: " + e.getMessage());
		}
	}

	private void unbindSMSC() {
		this.smppSession.setIsAlive(false);

		try {
			protoSmppSession.unbind();

			semaphore.drainPermits();
			semaphore.tryAcquire(5, TimeUnit.SECONDS);

			if (tracer.isInfoEnabled())
				tracer.info(raContext.getEntityName() + ": unbinding from SMSC");
		} catch (Exception e) {
			if (tracer.isSevereEnabled())
				tracer.severe("There was an error unbinding. ", e);
		}
	}

	/**
	 * Checking if the EsmClass is user message or is at a notification.
	 * 
	 * @param esmClassArg
	 *            - esm_class value
	 * @return true if it is a user message, otherwise false
	 */
	private static boolean isEsmClassUserMessage(int esmClassArg) {
		return (esmClassArg & 0X3C) == 0;
	}
	
	private void updateStats(){
		if(statsOn) {
			return;
		}
		long now = System.currentTimeMillis();
		long elpasedNumberOfMessagesCounter = usageParams.getMessagesExchanged() - this.lastMessagesExchanged;
		
		if(elpasedNumberOfMessagesCounter > 0){
			double elpasedTimeInSec = (now - this.lastStatsUpdatedTime) / 1000;
			double avgMessagesExchangedPerSec = elpasedNumberOfMessagesCounter / elpasedTimeInSec;
			usageParams.sampleMessagesExchangedPerSec((long)avgMessagesExchangedPerSec);
		}
		
		this.lastMessagesExchanged = usageParams.getMessagesExchanged();
		this.lastStatsUpdatedTime = now;
	}

	private void incrementUsageParams(int commandId) {

		if(!statsOn) {
			return;
		}
		
		usageParams.incrementMessagesExchanged(1);
		
		switch (commandId) {
		case CommandId.SUBMIT_SM:
			usageParams.incrementReqSubmitSm(1);
			break;
		case CommandId.SUBMIT_SM_RESP:
			usageParams.incrementResSubmitSm(1);
			break;
		case CommandId.DELIVER_SM:
			usageParams.incrementReqDeliverSm(1);
			break;
		case CommandId.DELIVER_SM_RESP:
			usageParams.incrementResDeliverSm(1);
			break;
		case CommandId.GENERIC_NACK:
			usageParams.incrementGenericNack(1);
			break;
		case CommandId.BIND_RECEIVER:
			usageParams.incrementReqBindReceiver(1);
			break;
		case CommandId.BIND_RECEIVER_RESP:
			usageParams.incrementResBindReceiver(1);
			break;
		case CommandId.BIND_TRANSMITTER:
			usageParams.incrementReqBindTransmitter(1);
			break;
		case CommandId.BIND_TRANSMITTER_RESP:
			usageParams.incrementResBindTransmitter(1);
			break;
		case CommandId.QUERY_SM:
			usageParams.incrementReqQuerySm(1);
			break;
		case CommandId.QUERY_SM_RESP:
			usageParams.incrementResQuerySm(1);
			break;
		case CommandId.UNBIND:
			usageParams.incrementReqUnbind(1);
			break;
		case CommandId.UNBIND_RESP:
			usageParams.incrementResUnbind(1);
			break;
		case CommandId.REPLACE_SM:
			usageParams.incrementReqReplaceSm(1);
			break;
		case CommandId.REPLACE_SM_RESP:
			usageParams.incrementResReplaceSm(1);
			break;
		case CommandId.CANCEL_SM:
			usageParams.incrementReqCancelSm(1);
			break;
		case CommandId.CANCEL_SM_RESP:
			usageParams.incrementResCancelSm(1);
			break;
		case CommandId.BIND_TRANSCEIVER:
			usageParams.incrementReqBindTransceiver(1);
			break;
		case CommandId.BIND_TRANSCEIVER_RESP:
			usageParams.incrementResBindTransceiver(1);
			break;
		case CommandId.OUTBIND:
			//TODO Supported?
			break;
		case CommandId.ENQUIRE_LINK:
			usageParams.incrementReqEnquireLink(1);
			break;
		case CommandId.ENQUIRE_LINK_RESP:
			usageParams.incrementResEnquireLink(1);
			break;
		case CommandId.SUBMIT_MULTI:
			usageParams.incrementReqSubmitMulti(1);
			break;
		case CommandId.SUBMIT_MULTI_RESP:
			usageParams.incrementResSubmitMulti(1);
			break;
		case CommandId.PARAM_RETRIEVE:
			usageParams.incrementReqParamRetrieve(1);
			break;
		case CommandId.PARAM_RETRIEVE_RESP:
			usageParams.incrementResParamRetrieve(1);
			break;
		case CommandId.QUERY_LAST_MSGS:
			usageParams.incrementReqQueryLastMessages(1);
			break;
		case CommandId.QUERY_LAST_MSGS_RESP:
			usageParams.incrementResQueryLastMessages(1);
			break;
		case CommandId.QUERY_MSG_DETAILS:
			usageParams.incrementReqQueryMessageDetails(1);
			break;
		case CommandId.QUERY_MSG_DETAILS_RESP:
			usageParams.incrementResQueryMessageDetails(1);
			break;
		case CommandId.ALERT_NOTIFICATION:
			usageParams.incrementReqAlertNotification(1);
			break;
		case CommandId.DATA_SM:
			usageParams.incrementReqDataSm(1);
			break;
		case CommandId.DATA_SM_RESP:
			usageParams.incrementResDataSm(1);
			break;
		case CommandId.BROADCAST_SM:
			usageParams.incrementReqBroadcastSm(1);
			break;
		case CommandId.BROADCAST_SM_RESP:
			usageParams.incrementResBroadcastSm(1);
			break;
		case CommandId.QUERY_BROADCAST_SM:
			usageParams.incrementReqQuerySm(1);
			break;
		case CommandId.QUERY_BROADCAST_SM_RESP:
			usageParams.incrementResQuerySm(1);
			break;
		case CommandId.CANCEL_BROADCAST_SM:
			usageParams.incrementReqBroadcastSm(1);
			break;
		case CommandId.CANCEL_BROADCAST_SM_RESP:
			usageParams.incrementResBroadcastSm(1);
			break;
		default:
			//Do nothing?
			//tracer.warning("Unexpected packet received! Id = 0x" + Integer.toHexString(commandId));

		}
	}

	/**
	 * Protected Methods
	 * 
	 * @throws StartActivityException
	 * @throws SLEEException
	 * @throws IllegalStateException
	 * @throws NullPointerException
	 * @throws ActivityAlreadyExistsException
	 */
	protected void startNewSmppTransactionActivity(SmppTransactionImpl txImpl) throws ActivityAlreadyExistsException, NullPointerException,
			IllegalStateException, SLEEException, StartActivityException {
		sleeEndpoint.startActivity(txImpl.getHandle(), txImpl, ActivityFlags.REQUEST_ENDED_CALLBACK);
		this.handleVsActivityMap.put(txImpl.getHandle(), txImpl);
	}

	protected void startNewSmppTransactionSuspendedActivity(SmppTransactionImpl txImpl) throws ActivityAlreadyExistsException, NullPointerException,
			IllegalStateException, SLEEException, StartActivityException {
		sleeEndpoint.startActivitySuspended(txImpl.getHandle(), txImpl, ActivityFlags.REQUEST_ENDED_CALLBACK);
		this.handleVsActivityMap.put(txImpl.getHandle(), txImpl);
	}

	protected void sendResponse(ExtSmppResponse response) throws IOException {
		this.protoSmppSession.sendPacket(response.getSMPPPacket());
		
		//Update usage params
		incrementUsageParams(response.getCommandId());
	}

	protected void sendRequest(ExtSmppRequest request) throws IOException {
		this.protoSmppSession.sendPacket(request.getSMPPPacket());
		
		//Update usage params
		incrementUsageParams(request.getCommandId());
	}

	protected void fireEvent(String eventName, SmppTransactionImpl activity, Object event) {
		final ActivityHandle handle = this.getActivityHandle(activity);

		if (handle == null) {
			this.tracer.warning("Firing event " + eventName + " for null ActivityHandle Activity=" + activity);
			return;
		}

		final FireableEventType eventType = eventTypeCache.getEventType(eventLookup, eventName);
		if (eventTypeFilter.filterEvent(eventType)) {
			if (tracer.isFineEnabled()) {
				tracer.fine("event " + eventName + " filtered");
			}
			return;
		}

		final String addressStr = this.utils.getAddressForEventFire(activity.getSmppRequest());
		final Address address = new Address(AddressPlan.E164_MOBILE, addressStr);
		try {
			sleeEndpoint.fireEvent(handle, eventType, event, address, null);
			if (tracer.isInfoEnabled()) {
				tracer.info("Fired event: " + eventName);
			}
		} catch (Throwable e) {
			tracer.severe("Failed to fire event", e);
		}
	}

	protected void endActivity(SmppTransactionImpl activity) {
		try {
			this.sleeEndpoint.endActivity(activity.getHandle());
		} catch (Exception e) {
			this.tracer.severe("Error while Ending Activity " + activity, e);
		}
	}

	/**
	 * Thread for sending ENQUIRE_LINK
	 */

	private class LinkMonitor implements Runnable {

		public void run() {
			if (tracer.isFineEnabled())
				tracer.fine("In LinkMonitor, isRAStoped = " + isRAStoped);
			
			// We keep trying only of we are bound atleast once and we are Head
			// Member
			while (!isRAStoped) {
				// long currentTime = System.currentTimeMillis();

				try {
					Thread.sleep(enquireLinkTimeout);
					
					updateStats();

					try {
	
						EnquireLink sm = new EnquireLink();
						protoSmppSession.sendPacket(sm);
						
						//Update usage params
						incrementUsageParams(sm.getCommandId());
						
						if (tracer.isFineEnabled()) {
							tracer.fine("Sent enquire link for " + raContext.getEntityName());
						}
	
					} catch (NotBoundException nbe) {
						if (tracer.isWarningEnabled())
							tracer.warning("Connection lost! for RA " + raContext.getEntityName() + "Reconnecting...", nbe);
						reconnect();
					} catch (IOException ie) {
						if (tracer.isSevereEnabled())
							tracer.severe("Connection lost! for RA " + raContext.getEntityName() + " 	Communication failed", ie);
						reconnect();
					} catch (BadCommandIDException ex) {
						tracer.severe("BadCommandIDException. Failed to enquire link ", ex);
					} catch (VersionException ex) {
						if (tracer.isSevereEnabled())
							tracer.severe("Failed to enquire link due to wrong Version ", ex);
					}
				

				} catch (InterruptedException e) {
					if (tracer.isInfoEnabled())
						tracer.info("Terminate link monitor: " + raContext.getEntityName());
				}
			}// while loop
		}
	}

	protected ResourceAdaptorContext getRAContext() {
		return this.raContext;
	}

	public ConcurrentHashMap<SmppTransactionHandle, SmppTransactionImpl> getHandleVsActivityMap() {
		return handleVsActivityMap;
	}
}
