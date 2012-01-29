package org.mobicents.slee.examples.map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
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
import org.mobicents.slee.resource.map.events.service.sms.ForwardShortMessageRequest;
import org.mobicents.slee.resource.map.events.service.suplementary.ProcessUnstructuredSSRequest;
import org.mobicents.slee.resource.map.events.service.suplementary.UnstructuredSSResponse;

/**
 * A simple example that listens for incoming MAP(USSD) Message and sends back
 * fix Menu for which it expects back response from user(mobile). Once the
 * response is received it sends back response again and terminates dialog.
 * 
 * @author amit bhayani
 * @author baranowb
 */
public abstract class MapSbb implements Sbb {

	private SbbContext sbbContext;

	private Tracer logger;

	private MAPContextInterfaceFactory mapAcif;
	private MAPProvider mapProvider;
	private MAPParameterFactory mapParameterFactory;

	/** Creates a new instance of CallSbb */
	public MapSbb() {
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

	public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest evt, ActivityContextInterface aci) {
		USSDString ussdStrObj = evt.getUSSDString();
		String ussdStr = ussdStrObj.getString();
		long invokeId = evt.getInvokeId();

		if (this.logger.isInfoEnabled()) {
			this.logger.info(String.format("Received PROCESS_UNSTRUCTURED_SS_REQUEST event USSDString=%s invokeId=%d", ussdStr, invokeId));
		}

		this.setInvokeId(invokeId);

		ussdStrObj = this.mapParameterFactory.createUSSDString("USSD String : Hello World <CR> 1. Balance <CR> 2. Texts Remaining");
		byte ussdDataCodingScheme = (byte) 0x0F;
		MAPDialogSupplementary dialog = evt.getMAPDialog();

		try {
			// FIXME: msisdn ?
			dialog.addUnstructuredSSRequest(ussdDataCodingScheme, ussdStrObj, null, null);
			dialog.send();
		} catch (MAPException e) {
			logger.severe("Error while sending UnstructuredSSRequest ", e);
		}

	}

	public void onUnstructuredSSResponse(UnstructuredSSResponse evt, ActivityContextInterface aci) {
		USSDString ussdStrObj = evt.getUSSDString();
		String ussdStr = ussdStrObj.getString();
		long invokeId = evt.getInvokeId();

		if (this.logger.isInfoEnabled()) {
			this.logger.info(String.format("Received UNSTRUCTURED_SS_RESPONSE event USSDString=%s invokeId=%d", ussdStr, invokeId));
		}

		ussdStrObj = this.mapParameterFactory.createUSSDString("Your balance = 350");
		byte ussdDataCodingScheme = (byte) 0x0F;
		MAPDialogSupplementary dialog = evt.getMAPDialog();

		try {
			// FIXME: msisdn ?
			dialog.addProcessUnstructuredSSResponse(this.getInvokeId(), ussdDataCodingScheme, ussdStrObj);
			dialog.close(false);
		} catch (MAPException e) {
			logger.severe("Error while sending UnstructuredSSRequest ", e);
		}

	}

	public void onForwardShortMessageRequest(ForwardShortMessageRequest evt, ActivityContextInterface aci) {
		ISDNAddressString isdn = evt.getSM_RP_OA().getMsisdn();
		byte[] sms = evt.getSM_RP_UI();

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
	 * Life cycle methods
	 */
	public void setSbbContext(SbbContext sbbContext) {

		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			this.sbbContext = sbbContext;

			mapAcif = (MAPContextInterfaceFactory) ctx.lookup("slee/resources/map/2.0/acifactory");

			mapProvider = (MAPProvider) ctx.lookup("slee/resources/map/2.0/provider");

			this.mapParameterFactory = this.mapProvider.getMAPParameterFactory();

			this.logger = sbbContext.getTracer("MapTest");
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
}
