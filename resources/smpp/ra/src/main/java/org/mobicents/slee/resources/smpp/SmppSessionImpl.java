package org.mobicents.slee.resources.smpp;

import ie.omk.smpp.AlreadyBoundException;
import ie.omk.smpp.BadCommandIDException;
import ie.omk.smpp.UnsupportedOperationException;
import ie.omk.smpp.message.SMPPProtocolException;
import ie.omk.smpp.version.VersionException;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.java.slee.resources.smpp.SmppSession;
import net.java.slee.resources.smpp.SmppTransaction;
import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.PDU;
import net.java.slee.resources.smpp.pdu.SmppDateFormatException;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;

import org.mobicents.slee.resources.smpp.pdu.AddressImpl;
import org.mobicents.slee.resources.smpp.pdu.AlertNotificationImpl;
import org.mobicents.slee.resources.smpp.pdu.CancelSMImpl;
import org.mobicents.slee.resources.smpp.pdu.DataSMImpl;
import org.mobicents.slee.resources.smpp.pdu.DeliverSMImpl;
import org.mobicents.slee.resources.smpp.pdu.QuerySMImpl;
import org.mobicents.slee.resources.smpp.pdu.ReplaceSMImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitMultiImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitSMImpl;

/**
 * 
 * @author amit bhayani
 *
 */
public class SmppSessionImpl implements SmppSession {

	protected ConcurrentMap<Long, SmppTransactionImpl> transactions = new ConcurrentHashMap<Long, SmppTransactionImpl>();

	private String sessionId;
	private SmppResourceAdaptor smppResourceAdaptor = null;
	private boolean isAlive = false;

	protected Timer timer = new Timer();

	public SmppSessionImpl(SmppResourceAdaptor smppResourceAdaptor) {
		this.smppResourceAdaptor = smppResourceAdaptor;
		StringBuffer sb = new StringBuffer();
		sb.append("SmppSession[SMSHost=").append(this.smppResourceAdaptor.getHost()).append(", SMSCPort=").append(
				this.smppResourceAdaptor.getPort()).append(", SystemId").append(this.smppResourceAdaptor.getSystemId());

		this.sessionId = sb.toString();

	}

	public Address createAddress(int addTon, int addNpi, String address) {
		return new AddressImpl(addTon, addNpi, address);
	}

	public SmppRequest createSmppRequest(long commandId) {
		SmppRequest request = null;
		if (commandId == SmppRequest.ALERT_NOTIFICATION) {
			request = new AlertNotificationImpl(this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.CANCEL_SM) {
			request = new CancelSMImpl(this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.DATA_SM) {
			request = new DataSMImpl(this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.DELIVER_SM) {
			request = new DeliverSMImpl(this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.QUERY_SM) {
			request = new QuerySMImpl(this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.REPLACE_SM) {
			request = new ReplaceSMImpl(this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.SUBMIT_MULTI) {
			request = new SubmitMultiImpl(this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.SUBMIT_SM) {
			request = new SubmitSMImpl(this.smppResourceAdaptor.seq.nextNumber());
		}

		return request;
	}

	public String getSMSCHost() {
		return this.smppResourceAdaptor.getHost();
	}

	public int getSMSPort() {
		return this.smppResourceAdaptor.getPort();
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	public SmppTransaction sendRequest(SmppRequest request) throws IllegalStateException, NullPointerException,
			IOException {

		if (!this.isAlive()) {
			throw new IllegalStateException("The ESME is not connected to SMSC");
		}

		if (request == null) {
			throw new NullPointerException("SMPP Request cannot be null");
		}

		SmppTransactionImpl smppTxImpl = this.getSmppTransactionImpl(request, true, SmppTransactionType.OUTGOING);
		if (smppTxImpl != null) {
			try {
				this.smppResourceAdaptor.sendRequest(request);
			} catch (AlreadyBoundException e) {
				this.smppResourceAdaptor.tracer.severe("AlreadyBoundException while sending SMPP Request", e);
				throw new IOException(e.getMessage());
			} catch (VersionException e) {
				this.smppResourceAdaptor.tracer.severe("VersionException while sending SMPP Request", e);
				throw new IOException(e.getMessage());
			} catch (SMPPProtocolException e) {
				this.smppResourceAdaptor.tracer.severe("SMPPProtocolException while sending SMPP Request", e);
				throw new IOException(e.getMessage());
			} catch (UnsupportedOperationException e) {
				this.smppResourceAdaptor.tracer.severe("UnsupportedOperationException while sending SMPP Request", e);
				throw new IOException(e.getMessage());
			} catch (BadCommandIDException e) {
				this.smppResourceAdaptor.tracer.severe("BadCommandIDException while sending SMPP Request", e);
				throw new IOException(e.getMessage());
			} catch (SmppDateFormatException e) {
				this.smppResourceAdaptor.tracer.severe("SmppDateFormatException while sending SMPP Request", e);
				throw new IOException(e.getMessage());
			}
		}
		return smppTxImpl;
	}

	public void sendResponse(SmppTransaction txn, SmppResponse response) throws IllegalStateException,
			NullPointerException, IOException {

		if (!this.isAlive()) {
			throw new IllegalStateException("The ESME is not connected to SMSC");
		}

		if (response == null) {
			throw new NullPointerException("SMPP Response cannot be null");
		}

		SmppTransactionImpl smppTxImpl = this.getSmppTransactionImpl(response, false, null);

		smppTxImpl.cancelResponseNotSentTimeout();

		try {
			this.smppResourceAdaptor.sendResponse(response);
		} catch (VersionException e) {
			this.smppResourceAdaptor.tracer.severe("VersionException while sending SMPP Response", e);
			throw new IOException(e.getMessage());
		} catch (BadCommandIDException e) {
			this.smppResourceAdaptor.tracer.severe("BadCommandIDException while sending SMPP Response", e);
			throw new IOException(e.getMessage());
		} catch (SmppDateFormatException e) {
			this.smppResourceAdaptor.tracer.severe("SmppDateFormatException while sending SMPP Response", e);
			throw new IOException(e.getMessage());
		}

	}

	protected void setIsAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	/**
	 * This method will return the existing SmppTransaction (Activity) if already exist else create new if
	 * creayeActivity is true. If already exist we remove it from transactions Map as this Tx life is only till response
	 * is received back
	 * 
	 * @param pdu
	 * @param createActivity
	 * @param requestReceived
	 * @return
	 */
	protected SmppTransactionImpl getSmppTransactionImpl(PDU pdu, boolean createActivity, SmppTransactionType txType) {
		SmppTransactionImpl txImpl = this.transactions.remove(pdu.getSequenceNum());
		if (txImpl != null) {
			this.smppResourceAdaptor.tracer.fine("Got the SmppTransaction " + txImpl);
			return txImpl;
		}

		this.smppResourceAdaptor.tracer.fine("Didnt get the SmppTransaction and createActivity = " + createActivity
				+ " and SmppTransactionType = " + txType + " For PDU " + pdu + " Seq No = " + pdu.getSequenceNum());
		// New Activity only created when either new SMPP Request arrives or Service sending new SMPP Request
		if (createActivity) {
			txImpl = new SmppTransactionImpl((SmppRequest) pdu, this.smppResourceAdaptor, this);

			try {
				this.transactions.put(txImpl.getId(), txImpl);

				switch (txType) {
				case INCOMING:
					// Try to start the Activity
					this.smppResourceAdaptor.startNewSmppTransactionActivity(txImpl);
					txImpl.setResponseNotSentTimeout();
					break;
				case OUTGOING:
					// Try to start the Activity in Suspended Mode
					this.smppResourceAdaptor.startNewSmppTransactionSuspendedActivity(txImpl);
					txImpl.setResponseNotReceivedTimeout();
					break;
				}

				return txImpl;
			} catch (Exception e) {
				this.smppResourceAdaptor.tracer.severe("Failed to start the Activity. SmppTransaction " + txImpl, e);

				try {
					this.transactions.remove(txImpl.getId());

					switch (txType) {
					case INCOMING:
						txImpl.cancelResponseNotSentTimeout();
						txImpl.sendGenericNack();
						return null;
					case OUTGOING:
						txImpl.cancelResponseNotReceivedTimeout();
						throw new IllegalStateException("Error while trying to create Activity");
					}
				} finally {
					// Last part clean Activity if it exist
					this.smppResourceAdaptor.endActivity(txImpl);
				}
			}
		} // if (createActivity)

		this.smppResourceAdaptor.tracer.fine("Now we will throw exception. But befire that lets just iterate");
		for (Long e : this.transactions.keySet()) {

			this.smppResourceAdaptor.tracer.fine("Seq = " + e);
		}
		throw new IllegalStateException("No Activity found for PDU " + pdu);
	}

	@Override
	public int hashCode() {
		final int prime = 19;
		int result = 1;
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
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
		final SmppSessionImpl other = (SmppSessionImpl) obj;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}

}
