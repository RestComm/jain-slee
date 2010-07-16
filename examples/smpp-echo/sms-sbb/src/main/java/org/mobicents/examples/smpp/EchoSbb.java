/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.examples.smpp;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;

import net.java.slee.resources.smpp.SmppSession;
import net.java.slee.resources.smpp.SmppTransaction;
import net.java.slee.resources.smpp.SmppTransactionACIFactory;
import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.DeliverSM;
import net.java.slee.resources.smpp.pdu.DeliverSMResp;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SubmitSM;
import net.java.slee.resources.smpp.pdu.SubmitSMResp;

/**
 * 
 * @author amit bhayani
 */
public abstract class EchoSbb implements Sbb {

	private SbbContext sbbContext;
	private SmppSession smppProvider;
	private SmppTransactionACIFactory smppAcif;

	private Tracer logger;

	public void onSmsMessage(DeliverSM event, ActivityContextInterface aci) {

		logger.info("Received DeliverSM ");

		DeliverSMResp deliverSMResp = (DeliverSMResp) event.createSmppResponseEvent(SmppTransaction.ESME_ROK);
		SmppTransaction txn = (SmppTransaction) aci.getActivity();
		try {
			txn.getSmppSession().sendResponse(txn, deliverSMResp);

			String message = new String(event.getMessage());
			logger.info("DeliverSM Message = " + message);

			Address user = event.getSourceAddress();

			reply(user, message);
		} catch (IllegalStateException e) {
			this.logger.severe("Error while sending DELIVER_SM_RESP", e);
		} catch (NullPointerException e) {
			this.logger.severe("Error while sending DELIVER_SM_RESP", e);
		} catch (IOException e) {
			this.logger.severe("Error while sending DELIVER_SM_RESP", e);
		}
	}

	private void reply(Address msidn, String message) {

		// Avoid setting Sequence Number as this is taken care by SMPP RA
		SubmitSM submitSM = (SubmitSM) this.smppProvider.createSmppRequest(SmppRequest.SUBMIT_SM);
		submitSM.setMessage(message.getBytes());

		Address source = this.smppProvider.createAddress(1, 0, "502");

		submitSM.setSourceAddress(source);
		submitSM.setDestAddress(msidn);

		submitSM.setRegisteredDelivery(1);

		SmppTransaction submitTxn;
		try {
			submitTxn = this.smppProvider.sendRequest(submitSM);

			// attach to the new activity so we get the response
			ActivityContextInterface newaci = smppAcif.getActivityContextInterface(submitTxn);
			newaci.attach(this.sbbContext.getSbbLocalObject());
		} catch (IllegalStateException e) {
			this.logger.severe("Error while sending SUBMIT_SM", e);
		} catch (NullPointerException e) {
			this.logger.severe("Error while sending SUBMIT_SM", e);
		} catch (IOException e) {
			this.logger.severe("Error while sending SUBMIT_SM", e);
		}

	}

	public void onSmsReport(SubmitSMResp event, ActivityContextInterface aci) {
		this.logger.info("Received SUBMIT_SM_RESP statu = " + event.getCommandStatus());
	}

	public void onDeliveryAck(DeliverSM event, ActivityContextInterface aci) {
		logger.info("Received DeliverSM for Acknowledgement. Sequence Number = " + event.getSequenceNum()
				+ " \n message = " + new String(event.getMessage()));

		//Send back Response
		DeliverSMResp deliverSMResp = (DeliverSMResp) event.createSmppResponseEvent(SmppTransaction.ESME_ROK);
		SmppTransaction txn = (SmppTransaction) aci.getActivity();
		try {
			txn.getSmppSession().sendResponse(txn, deliverSMResp);
		} catch (IllegalStateException e) {
			this.logger.severe("Error while sending DELIVER_SM_RESP", e);
		} catch (NullPointerException e) {
			this.logger.severe("Error while sending DELIVER_SM_RESP", e);
		} catch (IOException e) {
			this.logger.severe("Error while sending DELIVER_SM_RESP", e);
		}		
	}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		try {
			this.logger = sbbContext.getTracer(EchoSbb.class.getSimpleName());
			Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
			smppProvider = (SmppSession) myEnv.lookup("slee/resources/smpp/3.4/smppinterface");
			smppAcif = (SmppTransactionACIFactory) myEnv.lookup("slee/resources/smpp/3.4/factoryprovider");
		} catch (NamingException ne) {
			logger.severe("Could not set SBB context:" + ne.getMessage());
		}
	}

	public void unsetSbbContext() {
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

	public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) {
	}

	public void sbbRolledBack(RolledBackContext arg0) {
	}
}
