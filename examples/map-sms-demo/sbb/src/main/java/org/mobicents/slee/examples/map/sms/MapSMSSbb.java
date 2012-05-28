package org.mobicents.slee.examples.map.sms;

import java.util.ArrayList;

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
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageUnknownSubscriber;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationSetListImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationTripletImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.TripletListImpl;
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
import org.mobicents.slee.resource.map.events.ErrorComponent;
import org.mobicents.slee.resource.map.events.InvokeTimeout;
import org.mobicents.slee.resource.map.events.ProviderErrorComponent;
import org.mobicents.slee.resource.map.events.RejectComponent;

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
	private static final String SC_ADDRESS = "923330053058";
	private static final String HLR_GT = "553496629960";

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
		logger.warning("SMS Service activated");
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

		// Lets prepare the timer for next iteration
		this.setTimer(aci);
	}

	/**
	 * Components Events
	 */

	public void onInvokeTimeout(InvokeTimeout evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("Rx :  onInvokeTimeout" + evt);
		}
	}

	public void onErrorComponent(ErrorComponent event, ActivityContextInterface aci) {

		MAPErrorMessage mapErrorMessage = event.getMAPErrorMessage();
		this.logger.severe("Rx :  onErrorComponent MAPErrorMessage.isEmAbsentSubscriber=" + mapErrorMessage.isEmAbsentSubscriber());
		if (mapErrorMessage.isEmAbsentSubscriberSM()) {
			this.sendReportSMDeliveryStatusRequest(SMDeliveryOutcome.absentSubscriber);
		}
	}

	public void onProviderErrorComponent(ProviderErrorComponent event, ActivityContextInterface aci) {
		this.logger.severe("Rx :  onProviderErrorComponent" + event);
	}

	public void onRejectComponent(RejectComponent event, ActivityContextInterface aci) {
		this.logger.severe("Rx :  onRejectComponent" + event);
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
	public void onForwardShortMessageRequest(ForwardShortMessageRequest evt, ActivityContextInterface aci) {
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
	public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse evt, ActivityContextInterface aci) {
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

	public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest event, ActivityContextInterface aci) {

		// TODO : We should never receive request rather we should send request.
		// This is error condition

		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received REPORT_SM_DELIVERY_STATUS_REQUEST = " + event);
		}

		try {
			SMDeliveryOutcome smDeliveryOutcome = event.getSMDeliveryOutcome();

			if (this.logger.isInfoEnabled()) {
				this.logger.info("smDeliveryOutcome = " + smDeliveryOutcome);
			}

			MAPDialogSms mapDialogSms = event.getMAPDialog();
			mapDialogSms.addReportSMDeliveryStatusResponse(event.getInvokeId(), null, null);
			mapDialogSms.close(true);
		} catch (Exception e) {
			logger.severe("Error while trying to send ReportSMDeliveryStatusResponse", e);
		}

	}

	public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse event, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received REPORT_SM_DELIVERY_STATUS_RESPONSE = " + event);
		}
	}

	public void onAlertServiceCentreRequest(AlertServiceCentreRequest event, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received ALERT_SERVICE_CENTER_REQUEST = " + event);
		}

		try {

			MAPDialogSms mapDialogSMS = event.getMAPDialog();
			mapDialogSMS.addAlertServiceCentreResponse(event.getInvokeId());
			mapDialogSMS.close(true);
		} catch (Exception e) {
			logger.severe("Error while trying to send AlertServiceCentreResponse", e);
		}

		// TODO : Send the SMS from persistent store for this MSISDN

	}

	/**
	 * Received SRI request. But this is error, we should never receive this
	 * request
	 * 
	 * @param evt
	 * @param aci
	 */
	public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest evt, ActivityContextInterface aci) {
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
	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received MO_FORWARD_SHORT_MESSAGE_REQUEST = " + evt);
		}

		SmsSignalInfo smsSignalInfo = evt.getSM_RP_UI();
		SM_RP_OA smRPOA = evt.getSM_RP_OA();

		AddressString callingPartyAddress = smRPOA.getMsisdn();
		if (callingPartyAddress == null) {
			callingPartyAddress = smRPOA.getServiceCentreAddressOA();
		}

		this.setCallingPartyAddressString(callingPartyAddress);

		SmsTpdu smsTpdu = null;
		AddressField destinationAddress = null;
		try {
			smsTpdu = smsSignalInfo.decodeTpdu(true);

			switch (smsTpdu.getSmsTpduType()) {
			case SMS_SUBMIT:
				SmsSubmitTpdu smsSubmitTpdu = (SmsSubmitTpdu) smsTpdu;
				if (this.logger.isInfoEnabled()) {
					this.logger.info("Received SMS_SUBMIT = " + smsSubmitTpdu);
				}

				destinationAddress = smsSubmitTpdu.getDestinationAddress();

				UserData userData = smsSubmitTpdu.getUserData();
				userData.decode();
				String decodedMessage = userData.getDecodedMessage();
				this.setSmsMessage(decodedMessage);
				if (this.logger.isInfoEnabled()) {
					this.logger.info("decodedMessage SMS_SUBMIT = " + decodedMessage);
				}

				break;
			case SMS_DELIVER:
				SmsDeliverTpdu smsDeliverTpdu = (SmsDeliverTpdu) smsTpdu;
				if (this.logger.isInfoEnabled()) {
					this.logger.info("Received SMS_DELIVER = " + smsDeliverTpdu);
				}
				break;
			default:
				if (this.logger.isInfoEnabled()) {
					this.logger.info("Received DEFAULT = " + smsTpdu);
				}
				break;
			}
		} catch (MAPException e1) {
			logger.severe("Error while decoding SmsSignalInfo ", e1);
		}

		MAPDialogSms dialog = evt.getMAPDialog();

		try {
			dialog.addMoForwardShortMessageResponse(evt.getInvokeId(), null, null);
			dialog.close(false);
		} catch (MAPException e) {
			logger.severe("Error while sending ForwardShortMessageResponse ", e);
		}

		// TODO : Send the PENDING Status

		if (destinationAddress != null) {
			ISDNAddressString calledPartyISDNAddressString = this.getCalledPartyISDNAddressString(destinationAddress);
			this.setCalledPartyISDNAddressString(calledPartyISDNAddressString);
			this.sendSRI(destinationAddress);
		} else {
			logger.severe("Cannot send SRI as destinationAddress is null. Check errors above");
		}
	}

	/**
	 * Received Ack for MO SMS. But this is error we should never receive this
	 * 
	 * @param evt
	 * @param aci
	 */
	public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse evt, ActivityContextInterface aci) {
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
	public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest evt, ActivityContextInterface aci) {
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
	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received MT_FORWARD_SHORT_MESSAGE_RESPONSE = " + evt);
		}
	}

	/**************************************
	 * Mobility Events
	 **************************************/
	public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received SEND_AUTHENTICATION_INFO_REQUEST = " + evt);
		}

		IMSI imsi = evt.getImsi();
		MAPDialogMobility mapDialogMobility = evt.getMAPDialog();

		if ("724348899999950".equals(imsi.getData())) {
			// Good IMSI
			ArrayList<AuthenticationTriplet> ats = new ArrayList<AuthenticationTriplet>();

			byte[] randData = new byte[] { 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128 };
			byte[] sresData = new byte[] { -32, 82, -17, -14 };
			byte[] kcData = new byte[] { 31, 72, -93, 97, 78, -17, -52, 0 };

			AuthenticationTripletImpl at = new AuthenticationTripletImpl(randData, sresData, kcData);

			ats.add(at);
			TripletListImpl tl = new TripletListImpl(ats);
			AuthenticationSetListImpl asl = new AuthenticationSetListImpl(tl, 3);

			try {
				mapDialogMobility.addSendAuthenticationInfoResponse(evt.getInvokeId(), 3, asl, null, null);
				mapDialogMobility.close(false);
			} catch (MAPException e) {
				logger.severe("Error while sending SendAuthenticationInfoResponse ", e);
			}

		} else {
			// we don't know about this IMSI
			MAPErrorMessageUnknownSubscriber mapErrorMessageUnknownSubscriber = mapProvider.getMAPErrorMessageFactory().createMAPErrorMessageUnknownSubscriber(null, null);
			try {
				mapDialogMobility.sendErrorComponent(evt.getInvokeId(), mapErrorMessageUnknownSubscriber);
			} catch (MAPException e) {
				logger.severe("Error while sending SendAuthenticationInfoResponse MAPErrorMessageUnknownSubscriber", e);
			}
		}
	}

	public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse evt, ActivityContextInterface aci) {
		this.logger.severe("Received SEND_AUTHENTICATION_INFO_RESPONSE = " + evt);
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

	public abstract void setSmsMessage(String message);

	public abstract String getSmsMessage();

	public abstract void setCalledPartyISDNAddressString(ISDNAddressString calledPartyISDNAddressString);

	public abstract ISDNAddressString getCalledPartyISDNAddressString();

	public abstract void setCallingPartyAddressString(AddressString callingPartyAddressString);

	public abstract AddressString getCallingPartyAddressString();

	/**
	 * Private methods
	 */

	private AddressField getSmsTpduOriginatingAddress() {
		// if (this.smsTpduOriginatingAddress == null) {
		// this.smsTpduOriginatingAddress = new
		// AddressFieldImpl(TypeOfNumber.InternationalNumber,
		// NumberingPlanIdentification.ISDNTelephoneNumberingPlan,
		// SC_ADDRESS);
		// }
		// return this.smsTpduOriginatingAddress;

		return new AddressFieldImpl(TypeOfNumber.InternationalNumber, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, this
				.getCallingPartyAddressString().getAddress());
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
					MAPApplicationContextVersion.version3);
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
			GT0100 gt = new GT0100(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, HLR_GT);
			return this.hlrSCCPAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 6);
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
	private ISDNAddressString getCalledPartyISDNAddressString(AddressField destinationAddress) {
		String address = changeNationalToInternational(destinationAddress);
		return this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
				org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.ISDN, address);
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

	private MAPDialogSms setupRoutingInfoForSMRequestIndication(AddressField destinationAddress) throws MAPException {
		// this.mapParameterFactory.creat

		SccpAddress destinationReference = this.convertAddressFieldToSCCPAddress(destinationAddress);

		MAPDialogSms mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(this.getSRIMAPApplicationContext(), this.getServiceCenterSccpAddress(),
				null, destinationReference, null);

		mapDialogSms.addSendRoutingInfoForSMRequest(this.getCalledPartyISDNAddressString(destinationAddress), true, this.getServiceCenterAddressString(), null,
				false, null, null);

		return mapDialogSms;
	}

	private MAPDialogSms setupMtForwardShortMessageRequestIndication(SendRoutingInfoForSMResponse evt) throws MAPException {
		// this.mapParameterFactory.creat

		MAPDialogSms mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(this.getMtFoSMSMAPApplicationContext(),
				this.getServiceCenterSccpAddress(), null, this.getMSCSccpAddress(evt.getLocationInfoWithLMSI().getNetworkNodeNumber()), null);

		SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(evt.getIMSI());

		SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_ServiceCentreAddressOA(this.getServiceCenterAddressString());

		UserDataImpl ud = new UserDataImpl(this.getSmsMessage(), new DataCodingSchemeImpl(0), null, null);

		AbsoluteTimeStampImpl serviceCentreTimeStamp = new AbsoluteTimeStampImpl(12, 2, 1, 15, 1, 11, 12);

		SmsDeliverTpduImpl smsDeliverTpduImpl = new SmsDeliverTpduImpl(false, false, false, true, this.getSmsTpduOriginatingAddress(), pi,
				serviceCentreTimeStamp, ud);

		SmsSignalInfoImpl SmsSignalInfoImpl = new SmsSignalInfoImpl(smsDeliverTpduImpl, null);

		mapDialogSms.addMtForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, SmsSignalInfoImpl, false, null);

		return mapDialogSms;
	}

	private void sendSRI(AddressField destinationAddress) {
		// Send out SMS
		MAPDialogSms mapDialogSms = null;
		try {
			// 1. Create Dialog first and add the SRI request to it
			mapDialogSms = this.setupRoutingInfoForSMRequestIndication(destinationAddress);

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
	}

	private SccpAddress convertAddressFieldToSCCPAddress(AddressField destinationAddress) {

		String address = changeNationalToInternational(destinationAddress);
		GT0100 gt = new GT0100(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, address);
		return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 6);
	}

	private String changeNationalToInternational(AddressField destinationAddress) {
		String address = destinationAddress.getAddressValue();
		if (destinationAddress.getTypeOfNumber() != TypeOfNumber.InternationalNumber) {
			// TODO : Add logic to know if received address is international or
			// national or local and make necessary changes to make it
			// international format
			address = address.substring(1);
			address = "92" + address;
			logger.info("Converted address " + address);
		}
		return address;
	}

	private void sendReportSMDeliveryStatusRequest(SMDeliveryOutcome smDeliveryOutcome) {
		// Send out SMS
		MAPDialogSms mapDialogSms = null;
		try {
			// 1. Create Dialog first and add the SRI request to it
			mapDialogSms = this.setupReportSMDeliveryStatusRequest(smDeliveryOutcome);

			// 2. Create the ACI and attach this SBB
			ActivityContextInterface sriDialogACI = this.mapAcif.getActivityContextInterface(mapDialogSms);
			sriDialogACI.attach(this.sbbContext.getSbbLocalObject());

			// 3. Finally send the request
			mapDialogSms.send();
		} catch (MAPException e) {
			logger.severe("Error while trying to send ReportSMDeliveryStatusRequest", e);
			// something horrible, release MAPDialog and free resources
			mapDialogSms.release();
		}
	}

	private MAPDialogSms setupReportSMDeliveryStatusRequest(SMDeliveryOutcome smDeliveryOutcome) throws MAPException {
		// this.mapParameterFactory.creat

		MAPDialogSms mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(this.getSRIMAPApplicationContext(), this.getServiceCenterSccpAddress(),
				null, this.getHLRSccpAddress(), null);

		mapDialogSms.addReportSMDeliveryStatusRequest(this.getCalledPartyISDNAddressString(), this.getServiceCenterAddressString(), smDeliveryOutcome, null,
				null, false, false, null, null);

		return mapDialogSms;
	}
}