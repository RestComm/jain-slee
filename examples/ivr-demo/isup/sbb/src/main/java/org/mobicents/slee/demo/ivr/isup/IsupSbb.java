package org.mobicents.slee.demo.ivr.isup;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;

import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.InitialEventSelector;
import javax.slee.SbbLocalObject;

import net.java.slee.resource.mgcp.MgcpConnectionActivity;

import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;
import org.mobicents.slee.demo.ivr.CallConnectedEvent;

/**
 * 
 * @author amit bhayani
 */
public abstract class IsupSbb extends BaseSbb {

	private SbbLocalObject crcxResponseHandler;
	
	//ISUP Event Handlers

	public void onInitialAddressMessage(InitialAddressMessage event, ActivityContextInterface aci) {

		// generate call identifier
		callIdentifier = Integer.toHexString(CALL_ID_GEN++);
		this.setCallID(callIdentifier);

		setState(IsupConnectionState.NULL);
		tracer.info("CallID=" + callIdentifier + ", State=" + getState() + ", Receive incoming call");

		// now we are going to create connection.
		// preparing MGCP create connection request

		CallIdentifier callID = new CallIdentifier(callIdentifier);
		EndpointIdentifier endpointID = new EndpointIdentifier(ENDPOINT_NAME, JBOSS_BIND_ADDRESS + ":2427");

		CreateConnection crcx = new CreateConnection(this, callID, endpointID, ConnectionMode.SendRecv);

		int txID = GEN++;
		crcx.setTransactionHandle(txID);

		// creating media connection activity context interface
		ActivityContextInterface activityContextInterface = null;
		try {
			MgcpConnectionActivity connectionActivity = mgcpProvider.getConnectionActivity(txID, endpointID);
			activityContextInterface = mgcpAcif.getActivityContextInterface(connectionActivity);
		} catch (Exception ex) {
			setState(IsupConnectionState.CONNECTION_FAILED);
			tracer.severe(String.format("CallID=%s, State=%, Unexpected internal error", callIdentifier, getState()),
					ex);

			return;
		}

		asSbbActivityContextInterface(activityContextInterface).setRequest(event);
		// attaching sbb to the connection activity
		activityContextInterface.attach(sbbContext.getSbbLocalObject());

		// update response handler and state
		crcxResponseHandler = this.getSbbLocalObject("ConnectionCreated");
		setState(IsupConnectionState.CREATING_CONNECTION);

		// sending create connection request
		tracer.info(String.format("CallID = %s, State=%s, Sending request to %s", callIdentifier, getState(),
				endpointID));
		mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { crcx });
	}



	public void onReleaseComplete(ReleaseCompleteMessage event, ActivityContextInterface aci) {
		tracer.info(String.format("CallID = %s, State=%s, Receive Cancel request ", getCallID(), getState()));
//		setState(ConnectionState.CALL_CANCELED);
//
//		crcxResponseHandler = this.getSbbLocalObject("ConnectionDeleted");
		tracer.info(String.format("CallID = %s, State=%s", getCallID(), getState()));
	}

	public void onRelease(ReleaseMessage event, ActivityContextInterface aci) {
		SbbLocalObject handler = this.getSbbLocalObject("ConnectionDeleted");
		attach(handler);
	}
	
	//MGCP Event Handlers
	public void onCreateConnectionResponse(CreateConnectionResponse response, ActivityContextInterface aci) {
		tracer.info(String.format("CallID = %s, State=%s, Receive CRCX response", getCallID(), getState()));
		System.out.println("handler=" + crcxResponseHandler);
		attach(crcxResponseHandler);
	}

	private void attach(SbbLocalObject sbbLocalObject) {
		this.getISUPServerTxActivity().attach(sbbLocalObject);
		this.getConnectionActivity().attach(sbbLocalObject);
	}

	private SbbLocalObject getSbbLocalObject(String name) {
		ChildRelation relation = null;
		if (name.equals("ConnectionCreated")) {
			relation = this.getIsupConnectionCreatedSbb();
		} else if (name.equals("ModifiedConnection")) {
			relation = this.getIsupConnectionModifiedSbb();
		} else if (name.equals("ConnectionDeleted")) {
			relation = this.getIsupConnectionDeletedSbb();
		}
		try {
			return relation.create();
		} catch (Exception e) {
			return null;
		}
	}

	public abstract ChildRelation getIsupConnectionCreatedSbb();

	public abstract ChildRelation getIsupConnectionDeletedSbb();

	public abstract ChildRelation getIsupConnectionModifiedSbb();

	public abstract void fireCallConnected(CallConnectedEvent event, ActivityContextInterface aci,
			javax.slee.Address address);


}
