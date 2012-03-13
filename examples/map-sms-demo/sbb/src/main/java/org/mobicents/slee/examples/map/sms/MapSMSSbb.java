package org.mobicents.slee.examples.map.sms;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.facilities.Tracer;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.mobicents.protocols.ss7.map.smstpdu.AbsoluteTimeStampImpl;
import org.mobicents.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.mobicents.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.mobicents.protocols.ss7.map.smstpdu.SmsDeliverTpduImpl;
import org.mobicents.protocols.ss7.map.smstpdu.UserDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.resource.map.MAPContextInterfaceFactory;
import org.mobicents.slee.resource.map.events.DialogAccept;
import org.mobicents.slee.resource.map.events.DialogClose;
import org.mobicents.slee.resource.map.events.DialogDelimiter;
import org.mobicents.slee.resource.map.events.DialogNotice;
import org.mobicents.slee.resource.map.events.DialogProviderAbort;
import org.mobicents.slee.resource.map.events.DialogReject;
import org.mobicents.slee.resource.map.events.DialogRequest;
import org.mobicents.slee.resource.map.events.DialogTimeout;
import org.mobicents.slee.resource.map.events.DialogUserAbort;
import org.mobicents.slee.resource.map.events.InvokeTimeout;

/**
 * <p>
 * A simple example that listens for incoming MAP(SMS) Message.
 * </p>
 * <p>
 * This example also tries to send SMS every 30 secs
 * </p>
 * 
 * @author amit bhayani
 */
public abstract class MapSMSSbb implements Sbb {

	private final ProtocolIdentifierImpl pi = new ProtocolIdentifierImpl(0);

	/**
	 * NOTE : Replace below XXXXXXXXXXXX, YYYYYYYYYYYY by your service center
	 * MSISDN and called party MSISDN
	 */
	private static final String SC_ADDRESS = "XXXXXXXXXXXX";
	private static final String CALLED_PARTY_MSISDN = "YYYYYYYYYYYY";

	private SbbContextExt sbbContext;

	private Tracer logger;

	private MAPContextInterfaceFactory mapAcif;
	private MAPProvider mapProvider;
	private MAPParameterFactory mapParameterFactory;

	private static TimerOptions timerOptions;
	private TimerFacility timerFacility;

	private ISDNAddressString calledPartyAddress;
	private AddressString serviceCenterAddress;

	private MAPApplicationContext sriMAPApplicationContext = null;
	private MAPApplicationContext mtFoSMSMAPApplicationContext = null;

	private SccpAddress serviceCenterSCCPAddress = null;
	private SccpAddress hlrSCCPAddress = null;

	private AddressField smsTpduOriginatingAddress = null;

	/** Creates a new instance of CallSbb */
	public MapSMSSbb() {
	}

	/**
	 * Service started event
	 * 
	 * @param event
	 * @param aci
	 */
	public void onStartServiceEvent(javax.slee.serviceactivity.ServiceStartedEvent event, ActivityContextInterface aci) {
		logger.warning("Service activated, SMS will be sent out every 30 seconds");
		this.setTimer(aci);
	}

	/**
	 * Timer event
	 * 
	 * @param event
	 * @param aci
	 */
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			logger.info("Sending out SRI");
		}

		// Send out SMS
		MAPDialogSms mapDialogSms = null;
		try {
			// 1. Create Dialog first and add the SRI request to it
			mapDialogSms = this.setupRoutingInfoForSMRequestIndication();

			// 2. Create the ACI and attach this SBB
			ActivityContextInterface sriDialogACI = this.mapAcif.getActivityContextInterface(mapDialogSms);
			sriDialogACI.attach(this.sbbContext.getSbbLocalObject());

			// 3. Finally send the request
			mapDialogSms.send();
		} catch (MAPException e) {
			logger.severe("Error while trying to send RoutingInfoForSMRequestIndication", e);
			// something horrible, release MAPDialog and free resources
			mapDialogSms.release();
		}

		// Lets prepare the timer for next iteration
		this.setTimer(aci);
	}

	/**
	 * Dialog Events
	 */

	public void onDialogDelimiter(DialogDelimiter evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onDialogDelimiter" + evt);
		}
	}

	public void onDialogAccept(DialogAccept evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onDialogAccept" + evt);
		}
	}

	public void onDialogReject(DialogReject evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onDialogReject" + evt);
		}
	}

	public void onDialogUserAbort(DialogUserAbort evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onDialogUserAbort" + evt);
		}
	}

	public void onDialogProviderAbort(DialogProviderAbort evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onDialogProviderAbort" + evt);
		}
	}

	public void onDialogClose(DialogClose evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onDialogClose" + evt);
		}
	}

	public void onDialogNotice(DialogNotice evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onDialogNotice" + evt);
		}
	}

	public void onDialogTimeout(DialogTimeout evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onDialogTimeout" + evt);
		}
	}

	public void onInvokeTimeout(InvokeTimeout evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onInvokeTimeout" + evt);
		}
	}

	public void onDialogRequest(DialogRequest evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onDialogRequest" + evt);
		}
	}

	/**************************************
	 * SMS Events
	 **************************************/

	/**
	 * We received a SMS. Send back ACK
	 * 
	 * @param evt
	 * @param aci
	 */
	public void onForwardShortMessageRequest(ForwardShortMessageRequestIndication evt, ActivityContextInterface aci) {
		ISDNAddressString isdn = evt.getSM_RP_OA().getMsisdn();
		SmsSignalInfo smsSignalInfo = evt.getSM_RP_UI();

		if (this.logger.isInfoEnabled()) {
			this.logger.info(String.format("Received FORWARD_SHORT_MESSAGE_REQUEST event=%s", evt.toString()));
		}

		MAPDialogSms dialog = evt.getMAPDialog();

		try {
			dialog.addForwardShortMessageResponse(evt.getInvokeId());
			dialog.close(false);
		} catch (MAPException e) {
			logger.severe("Error while sending ForwardShortMessageResponse ", e);
		}
	}

	/**
	 * Received response for SRI sent earlier
	 * 
	 * @param evt
	 * @param aci
	 */
	public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponseIndication evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received SEND_ROUTING_INFO_FOR_SM_RESPONSE = " + evt);
		}

		// Now send SMS

		MAPDialogSms mapDialogSms = null;
		try {
			// 1. Create MT Forward SMS Dialog and add message to it
			mapDialogSms = this.setupMtForwardShortMessageRequestIndication(evt);

			// 2. Create the ACI and attach this SBB
			ActivityContextInterface mtFOSmsDialogACI = this.mapAcif.getActivityContextInterface(mapDialogSms);
			mtFOSmsDialogACI.attach(this.sbbContext.getSbbLocalObject());

			// 3. Finaly send SMS
			mapDialogSms.send();
		} catch (MAPException e) {
			logger.severe("Error while trying to send MtForwardShortMessageRequestIndication", e);
			// something horrible, release MAPDialog and free resources
			mapDialogSms.release();
		}
	}

	/**
	 * Received SRI request. But this is error, we should never receive this
	 * request
	 * 
	 * @param evt
	 * @param aci
	 */
	public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequestIndication evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received SEND_ROUTING_INFO_FOR_SM_REQUEST = " + evt);
		}

	}

	/**
	 * Received incoming SMS for ACN v3. Send back ack
	 * 
	 * @param evt
	 * @param aci
	 */
	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequestIndication evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received MO_FORWARD_SHORT_MESSAGE_REQUEST = " + evt);
		}

		MAPDialogSms dialog = evt.getMAPDialog();

		try {
			dialog.addMtForwardShortMessageResponse(evt.getInvokeId(), null, null);
			dialog.close(false);
		} catch (MAPException e) {
			logger.severe("Error while sending ForwardShortMessageResponse ", e);
		}
	}

	/**
	 * Received Ack for MO SMS. But this is error we should never receive this
	 * 
	 * @param evt
	 * @param aci
	 */
	public void onMoForwardShortMessageResponse(MoForwardShortMessageResponseIndication evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received MO_FORWARD_SHORT_MESSAGE_RESPONSE = " + evt);
		}
	}

	/**
	 * Received MT SMS. This is error we should never receive this
	 * 
	 * @param evt
	 * @param aci
	 */
	public void onMtForwardShortMessageRequest(MtForwardShortMessageRequestIndication evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received MT_FORWARD_SHORT_MESSAGE_REQUEST = " + evt);
		}
	}

	/**
	 * Received ACK for MT Forward SMS sent earlier
	 * 
	 * @param evt
	 * @param aci
	 */
	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponseIndication evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received MT_FORWARD_SHORT_MESSAGE_RESPONSE = " + evt);
		}
	}

	/**
	 * Life cycle methods
	 */
	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = (SbbContextExt) sbbContext;

		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			this.timerFacility = this.sbbContext.getTimerFacility();

			mapAcif = (MAPContextInterfaceFactory) ctx.lookup("slee/resources/map/2.0/acifactory");

			mapProvider = (MAPProvider) ctx.lookup("slee/resources/map/2.0/provider");

			this.mapParameterFactory = this.mapProvider.getMAPParameterFactory();

			this.logger = this.sbbContext.getTracer(getClass().getSimpleName());
		} catch (Exception ne) {
			logger.severe("Could not set SBB context:", ne);
		}

	}

	public void unsetSbbContext() {
		this.sbbContext = null;
		this.logger = null;
	}

	public void sbbCreate() throws CreateException {
	}

	public void sbbPostCreate() throws CreateException {

	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbRemove() {
	}

	public void sbbExceptionThrown(Exception exception, Object object, ActivityContextInterface activityContextInterface) {
	}

	public void sbbRolledBack(RolledBackContext rolledBackContext) {
	}

	/**
	 * CMP's
	 */
	public abstract void setInvokeId(long invokeId);

	public abstract long getInvokeId();

	/**
	 * Private methods
	 */

	private AddressField getSmsTpduOriginatingAddress() {
		if (this.smsTpduOriginatingAddress == null) {
			this.smsTpduOriginatingAddress = new AddressFieldImpl(TypeOfNumber.InternationalNumber, NumberingPlanIdentification.ISDNTelephoneNumberingPlan,
					SC_ADDRESS);
		}
		return this.smsTpduOriginatingAddress;
	}

	private TimerOptions getTimerOptions() {
		if (timerOptions == null) {
			timerOptions = new TimerOptions();
			timerOptions.setPreserveMissed(TimerPreserveMissed.ALL);
		}
		return timerOptions;
	}

	private MAPApplicationContext getMtFoSMSMAPApplicationContext() {
		if (this.mtFoSMSMAPApplicationContext == null) {
			this.mtFoSMSMAPApplicationContext = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMTRelayContext,
					MAPApplicationContextVersion.version3);
		}
		return this.mtFoSMSMAPApplicationContext;
	}

	private MAPApplicationContext getSRIMAPApplicationContext() {
		if (this.sriMAPApplicationContext == null) {
			this.sriMAPApplicationContext = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
					MAPApplicationContextVersion.version2);
		}
		return this.sriMAPApplicationContext;
	}

	/**
	 * This is our (Service Center) SCCP Address for GT
	 * 
	 * @return
	 */
	private SccpAddress getServiceCenterSccpAddress() {
		if (this.serviceCenterSCCPAddress == null) {
			GT0100 gt = new GT0100(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, SC_ADDRESS);
			this.serviceCenterSCCPAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 8);
		}
		return this.serviceCenterSCCPAddress;
	}

	/**
	 * This is MSC SCCP Address for GT
	 * 
	 * @return
	 */
	private SccpAddress getHLRSccpAddress() {
		if (this.hlrSCCPAddress == null) {
			GT0100 gt = new GT0100(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, CALLED_PARTY_MSISDN);
			this.hlrSCCPAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 6);
		}
		return this.hlrSCCPAddress;
	}

	private SccpAddress getMSCSccpAddress(ISDNAddressString networkNodeNumber) {

		// TODO : use the networkNodeNumber also to derive if its
		// International / ISDN?
		GT0100 gt = new GT0100(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, networkNodeNumber.getAddress());
		return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 8);
	}

	/**
	 * This is end user mobile number to whom SMS should reach.
	 * 
	 * @return
	 */
	private ISDNAddressString getCalledPartyISDNAddressString() {
		if (this.calledPartyAddress == null) {
			this.calledPartyAddress = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
					org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.ISDN, CALLED_PARTY_MSISDN);
		}
		return this.calledPartyAddress;
	}

	/**
	 * This is our own number. We are Service Center.
	 * 
	 * @return
	 */
	private AddressString getServiceCenterAddressString() {

		if (this.serviceCenterAddress == null) {
			this.serviceCenterAddress = this.mapParameterFactory.createAddressString(AddressNature.international_number,
					org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.ISDN, SC_ADDRESS);
		}
		return this.serviceCenterAddress;
	}

	private void setTimer(ActivityContextInterface aci) {
		// set timer of 60 secs on the dialog aci
		timerFacility.setTimer(aci, null, System.currentTimeMillis() + 30000L, getTimerOptions());
	}

	private MAPDialogSms setupRoutingInfoForSMRequestIndication() throws MAPException {
		// this.mapParameterFactory.creat

		MAPDialogSms mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(this.getSRIMAPApplicationContext(), this.getServiceCenterSccpAddress(),
				null, this.getHLRSccpAddress(), null);

		mapDialogSms
				.addSendRoutingInfoForSMRequest(this.getCalledPartyISDNAddressString(), true, this.getServiceCenterAddressString(), null, false, null, null);

		return mapDialogSms;
	}

	private MAPDialogSms setupMtForwardShortMessageRequestIndication(SendRoutingInfoForSMResponseIndication evt) throws MAPException {
		// this.mapParameterFactory.creat

		MAPDialogSms mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(this.getMtFoSMSMAPApplicationContext(),
				this.getServiceCenterSccpAddress(), null, this.getMSCSccpAddress(evt.getLocationInfoWithLMSI().getNetworkNodeNumber()), null);

		SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(evt.getIMSI());

		SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_ServiceCentreAddressOA(this.getServiceCenterAddressString());

		UserDataImpl ud = new UserDataImpl("Hello, mobicents world !!!", new DataCodingSchemeImpl(0), null, null);

		AbsoluteTimeStampImpl serviceCentreTimeStamp = new AbsoluteTimeStampImpl(12, 2, 1, 15, 1, 11, 12);

		SmsDeliverTpduImpl smsDeliverTpduImpl = new SmsDeliverTpduImpl(false, false, false, false, this.getSmsTpduOriginatingAddress(), pi,
				serviceCentreTimeStamp, ud);

		SmsSignalInfoImpl SmsSignalInfoImpl = new SmsSignalInfoImpl(smsDeliverTpduImpl, null);

		mapDialogSms.addMtForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, SmsSignalInfoImpl, false, null);

		return mapDialogSms;
	}
}
