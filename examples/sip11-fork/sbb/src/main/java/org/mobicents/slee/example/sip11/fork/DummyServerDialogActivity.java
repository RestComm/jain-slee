package org.mobicents.slee.example.sip11.fork;

import java.util.Iterator;

import javax.sip.ClientTransaction;
import javax.sip.DialogDoesNotExistException;
import javax.sip.DialogState;
import javax.sip.InvalidArgumentException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.TransactionDoesNotExistException;
import javax.sip.address.Address;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import net.java.slee.resource.sip.DialogActivity;

public class DummyServerDialogActivity implements DialogActivity {

	private final CallIdHeader callId;
	private final Address from;
	private final Address to;
	
	public DummyServerDialogActivity(CallIdHeader callId, Address from, Address to) {
		this.callId = callId;
		this.from = from;
		this.to = to;
	}

	public void associateServerTransaction(ClientTransaction arg0,
			ServerTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	public Request createRequest(Request arg0) throws SipException {
		// TODO Auto-generated method stub
		return null;
	}

	public Response createResponse(ServerTransaction arg0, Response arg1)
			throws SipException {
		// TODO Auto-generated method stub
		return null;
	}

	public ServerTransaction getAssociatedServerTransaction(
			ClientTransaction arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public ClientTransaction sendCancel() throws SipException {
		// TODO Auto-generated method stub
		return null;
	}

	public ClientTransaction sendRequest(Request arg0) throws SipException {
		// TODO Auto-generated method stub
		return null;
	}

	public Request createAck(long arg0) throws InvalidArgumentException,
			SipException {
		// TODO Auto-generated method stub
		return null;
	}

	public Request createPrack(Response arg0)
			throws DialogDoesNotExistException, SipException {
		// TODO Auto-generated method stub
		return null;
	}

	public Response createReliableProvisionalResponse(int arg0)
			throws InvalidArgumentException, SipException {
		// TODO Auto-generated method stub
		return null;
	}

	public Request createRequest(String arg0) throws SipException {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete() {
		// TODO Auto-generated method stub
		
	}

	public Object getApplicationData() {
		// TODO Auto-generated method stub
		return null;
	}

	public CallIdHeader getCallId() {
		return callId;
	}

	public String getDialogId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Transaction getFirstTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	public Address getLocalParty() {
		return from;
	}

	public long getLocalSeqNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getLocalSequenceNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLocalTag() {
		// TODO Auto-generated method stub
		return null;
	}

	public Address getRemoteParty() {
		return to;
	}

	public long getRemoteSeqNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getRemoteSequenceNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getRemoteTag() {
		// TODO Auto-generated method stub
		return null;
	}

	public Address getRemoteTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator getRouteSet() {
		// TODO Auto-generated method stub
		return null;
	}

	public DialogState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	public void incrementLocalSequenceNumber() {
		// TODO Auto-generated method stub
		
	}

	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isServer() {
		return true;
	}

	public void sendAck(Request arg0) throws SipException {
		// TODO Auto-generated method stub
		
	}

	public void sendReliableProvisionalResponse(Response arg0)
			throws SipException {
		// TODO Auto-generated method stub
		
	}

	public void sendRequest(ClientTransaction arg0)
			throws TransactionDoesNotExistException, SipException {
		// TODO Auto-generated method stub
		
	}

	public void setApplicationData(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	public void terminateOnBye(boolean arg0) throws SipException {
		// TODO Auto-generated method stub
		
	}
	
	
}
