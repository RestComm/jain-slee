package org.mobicents.slee.resources.smpp;

import java.io.IOException;
import java.util.TimerTask;

import javax.slee.facilities.Tracer;

import net.java.slee.resources.smpp.SmppSession;
import net.java.slee.resources.smpp.SmppTransaction;
import net.java.slee.resources.smpp.pdu.SmppError;
import net.java.slee.resources.smpp.pdu.SmppRequest;

import org.mobicents.slee.resources.smpp.pdu.GenericNackImpl;
import org.mobicents.slee.resources.smpp.pdu.SmppErrorImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
public class SmppTransactionImpl implements SmppTransaction {

	private Tracer tracer;

	private SmppResourceAdaptor smppRA;
	private SmppSessionImpl smppSess;

	private long sequenceNumber;
	private SmppRequest requestPDU;

	private ResponseNotSent responseNotSent;
	private ResponseNotReceived responseNotReceived;

	protected SmppTransactionImpl(SmppRequest requestPDU, SmppResourceAdaptor smppRA, SmppSessionImpl smppSess) {
		this.requestPDU = requestPDU;
		this.smppRA = smppRA;
		this.tracer = this.smppRA.getRAContext().getTracer(SmppTransactionImpl.class.getSimpleName());
		this.smppSess = smppSess;
		this.sequenceNumber = this.requestPDU.getSequenceNum();
	}

	public long getId() {
		return this.sequenceNumber;
	}

	public SmppSession getSmppSession() {
		return smppSess;
	}

	protected SmppRequest getSmppRequest() {
		return this.requestPDU;
	}

	/**
	 * Reset the Timers
	 */
	protected void setResponseNotSentTimeout() {
		responseNotSent = new ResponseNotSent();
		this.smppSess.timer.schedule(responseNotSent, this.smppRA.getSmppResponseSentTimeout());
	}

	protected void setResponseNotReceivedTimeout() {
		responseNotReceived = new ResponseNotReceived();
		this.smppSess.timer.schedule(responseNotReceived, this.smppRA.getSmppResponseReceivedTimeout());
	}

	/**
	 * Cancel the timers
	 */
	protected void cancelResponseNotSentTimeout() {
		if (responseNotSent != null) {
			responseNotSent.cancel();
		}
	}

	protected void cancelResponseNotReceivedTimeout() {
		if (responseNotReceived != null) {
			responseNotReceived.cancel();
		}
	}

	protected void sendGenericNack() {
		GenericNackImpl genericNak = new GenericNackImpl(SmppTransaction.ESME_RUNKNOWNERR);
		genericNak.setSequenceNum(this.requestPDU.getSequenceNum());
		try {
			this.smppRA.sendResponse(genericNak);
		} catch (IOException e) {
			this.tracer.severe("IOException while sending GenericNack SMPP Response", e);
		}
	}

	private void fireSmppTORespNotSent() {
		SmppErrorImpl error = new SmppErrorImpl(SmppError.SMPP_TIMEOUT_RESPONSE_SENT, this.requestPDU);
		this.smppRA.fireEvent(Utils.SMPP_TIMEOUT_RESPONSE_SENT, this, error);
		this.smppRA.endActivity(this);

	}

	private void fireSmppRespNotReceived() {
		SmppErrorImpl error = new SmppErrorImpl(SmppError.SMPP_TIMEOUT_RESPONSE_RECEIVED, this.requestPDU);
		this.smppRA.fireEvent(Utils.SMPP_TIMEOUT_RESPONSE_RECEIVED, this, error);
		this.smppRA.endActivity(this);
	}

	private class ResponseNotSent extends TimerTask {

		public void run() {
			// Clean the transactions MAP
			smppSess.transactions.remove(this);

			// Send GENERIC_NACK to back SMSC
			sendGenericNack();

			// Fire SMPP_TIMEOUT_RESPONSE_SENT back to application
			fireSmppTORespNotSent();
		}
	}

	private class ResponseNotReceived extends TimerTask {

		public void run() {
			// Clean the transactions MAP
			smppSess.transactions.remove(this);

			// Fire SMPP_TIMEOUT_RESPONSE_RECEIVED back to application
			fireSmppRespNotReceived();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestPDU == null) ? 0 : requestPDU.hashCode());
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
		final SmppTransactionImpl other = (SmppTransactionImpl) obj;
		if (requestPDU == null) {
			if (other.requestPDU != null)
				return false;
		} else if (!requestPDU.equals(other.requestPDU))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TransactionImpl[SequenceNumber = ").append(sequenceNumber).append(", Hash = ").append(
				this.hashCode()).append("]");
		return sb.toString();
	}

}
