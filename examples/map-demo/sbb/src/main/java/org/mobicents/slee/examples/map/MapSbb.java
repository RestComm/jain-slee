package org.mobicents.slee.examples.map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.MAPCloseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPOpenInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;
import org.mobicents.slee.resource.map.MAPContextInterfaceFactory;

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
	private MapServiceFactory mapServiceFactory;

	/** Creates a new instance of CallSbb */
	public MapSbb() {
	}

	public void setSbbContext(SbbContext sbbContext) {

		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			this.sbbContext = sbbContext;

			mapAcif = (MAPContextInterfaceFactory) ctx.lookup("slee/resources/map/2.0/acifactory");

			mapProvider = (MAPProvider) ctx.lookup("slee/resources/map/2.0/provider");

			this.mapServiceFactory = this.mapProvider.getMapServiceFactory();

			this.logger = sbbContext.getTracer("MapTest");
		} catch (Exception ne) {
			logger.severe("Could not set SBB context:", ne);
		}

	}

	public void onOpenInfo(MAPOpenInfo evt, ActivityContextInterface aci) {
		if (logger.isInfoEnabled()) {
			this.logger.info("New MAP Dialog. Received event MAPOpenInfo " + evt);
		}

	}

	public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSIndication evt, ActivityContextInterface aci) {
		USSDString ussdStrObj = evt.getUSSDString();
		String ussdStr = ussdStrObj.getString();

		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received PROCESS_UNSTRUCTURED_SS_REQUEST_INDICATION event USSDString = " + ussdStr);
		}

		ussdStrObj = this.mapServiceFactory
				.createUSSDString("USSD String : Hello World <CR> 1. Balance <CR> 2. Texts Remaining");
		byte ussdDataCodingScheme = (byte) 0x0F;
		MAPDialog dialog = evt.getMAPDialog();

		try {
			// FIXME: msisdn ?
			dialog.addUnstructuredSSRequest(ussdDataCodingScheme, ussdStrObj);
			dialog.send();
		} catch (MAPException e) {
			logger.severe("Error while sending UnstructuredSSRequest ", e);
		}

	}

	public void onUnstructuredSSRequest(UnstructuredSSIndication evt, ActivityContextInterface aci) {
		USSDString ussdStrObj = evt.getUSSDString();
		String ussdStr = ussdStrObj.getString();

		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received UNSTRUCTURED_SS_REQUEST_INDICATION event USSDString = " + ussdStr);
		}

		ussdStrObj = this.mapServiceFactory.createUSSDString("Your balance = 350");
		byte ussdDataCodingScheme = (byte) 0x0F;
		MAPDialog dialog = evt.getMAPDialog();

		try {
			// FIXME: msisdn ?
			dialog.addUnstructuredSSResponse(evt.getInvokeId(), true, ussdDataCodingScheme, ussdStrObj);
			dialog.send();
		} catch (MAPException e) {
			logger.severe("Error while sending UnstructuredSSRequest ", e);
		}

	}

	/**
	 * MAP Dialog Event Handlers
	 */

	public void onCloseInfo(MAPCloseInfo evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received MAP CLOSE_INFO event for MAP Dialog Id " + evt.getMAPDialog().getDialogId());
		}
	}

	public void onRefuseInfo(MAPRefuseInfo evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received MAP REFUSE_INFO event for MAP Dialog Id " + evt.getMAPDialog().getDialogId());
		}
	}

	public void onUserAbortInfo(MAPUserAbortInfo evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger
					.info("Received MAP USER_ABORT_INFO event for MAP Dialog Id " + evt.getMAPDialog().getDialogId());
		}
	}

	public void onProviderAbortInfo(MAPProviderAbortInfo evt, ActivityContextInterface aci) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Received MAP PROVIDER_ABORT_INFO event for MAP Dialog Id "
					+ evt.getMAPDialog().getDialogId());
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
}
