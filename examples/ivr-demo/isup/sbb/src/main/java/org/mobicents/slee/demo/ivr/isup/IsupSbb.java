package org.mobicents.slee.demo.ivr.isup;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;

import java.io.IOException;

import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.SbbLocalObject;

import net.java.slee.resource.mgcp.MgcpConnectionActivity;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;

/**
 * 
 * @author amit bhayani
 */
public abstract class IsupSbb extends BaseSbb {

	// ISUP Event Handlers

	public void onInitialAddressMessage(InitialAddressMessage event, ActivityContextInterface aci) {

		// generate call identifier
		callIdentifier = Integer.toHexString(CALL_ID_GEN++);

		// setState(IsupConnectionState.NULL);
		tracer.info("CallID=" + callIdentifier + ", Receive incoming call");

		try {
			// Send ACM
			AddressCompleteMessage acm = isupMessageFactory.createACM();
			// TODO fill ACM parameters

			this.isupProvider.sendMessage(acm);

			// Create B Channel Endpoint name from CIC
			EndpointIdentifier bChhanEndpointID = new EndpointIdentifier(B_CHANN_ENDPOINT_NAME
					+ event.getCircuitIdentificationCode().getCIC(), JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);

			// now we are going to create local connection between B Channel Endpoint and IVR Endpoint

			// preparing MGCP create connection request

			CallIdentifier callID = new CallIdentifier(callIdentifier);
			EndpointIdentifier ivrEndpointID = new EndpointIdentifier(IVR_ENDPOINT_NAME, JBOSS_BIND_ADDRESS + ":2427");

			CreateConnection crcx = new CreateConnection(this, callID, bChhanEndpointID, ConnectionMode.SendRecv);
			crcx.setSecondEndpointIdentifier(ivrEndpointID);

			int txID = GEN++;
			crcx.setTransactionHandle(txID);

			// creating media connection activity context interface
			ActivityContextInterface activityContextInterface = null;

			MgcpConnectionActivity connectionActivity = mgcpProvider.getConnectionActivity(txID, bChhanEndpointID);
			activityContextInterface = mgcpAcif.getActivityContextInterface(connectionActivity);

			asSbbActivityContextInterface(activityContextInterface).setRequest(event);

			// attaching sbb to the connection activity
			activityContextInterface.attach(sbbContext.getSbbLocalObject());

			this.setCallID(callIdentifier);

			// update response handler and state
			this.setCrcxResponseHandler(this.getSbbLocalObject("ConnectionCreated"));
			setState(IsupConnectionState.CREATING_CONNECTION);

			// sending create connection request
			tracer.info(String.format("CallID = %s, State=%s, Sending request to %s", callIdentifier, getState(),
					bChhanEndpointID));
			mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { crcx });
		} catch (Exception ex) {
			setState(IsupConnectionState.CONNECTION_FAILED);
			tracer.severe(String.format("CallID=%s, State=%, Unexpected internal error", callIdentifier, getState()),
					ex);

			// Send REL
			this.sendREL();

			return;
		}

	}

	public void onReleaseComplete(ReleaseCompleteMessage event, ActivityContextInterface aci) {
		tracer.info(String.format("CallID = %s, State=%s, Receive Cancel request ", getCallID(), getState()));
		// setState(ConnectionState.CALL_CANCELED);
		//
		// crcxResponseHandler = this.getSbbLocalObject("ConnectionDeleted");
		tracer.info(String.format("CallID = %s, State=%s", getCallID(), getState()));
	}

	public void onRelease(ReleaseMessage event, ActivityContextInterface aci) {
		tracer.info(String.format("CallID = %s, State=%s, Receive Release request ", getCallID(), getState()));

		// Send RLC
		ReleaseCompleteMessage rlc = isupMessageFactory.createRLC();
		// TODO fill RLC parameters
		try {
			this.isupProvider.sendMessage(rlc);
		} catch (ParameterRangeInvalidException e) {
			tracer
					.severe(String.format("CallID=%s, State=%, Unexpected internal error", callIdentifier, getState()),
							e);
		} catch (IOException e) {
			tracer
					.severe(String.format("CallID=%s, State=%, Unexpected internal error", callIdentifier, getState()),
							e);
		}

		// Clean Media Endpoints
		SbbLocalObject handler = this.getSbbLocalObject("ConnectionDeleted");
		this.setCrcxResponseHandler(handler);

		switch (this.getState()) {
		case CREATING_CONNECTION:

			break;
		case CONNECTION_CONNECTED:
			// IsupConnectionDeletedSbb should take care
			attach(handler);
			break;

		default:
			tracer.severe("Expected state CREATING_CONNECTION or CONNECTION_CONNECTED and is " + getState());
			break;
		}

		setState(IsupConnectionState.CALL_RELEASED);
	}

	// MGCP Event Handlers
	public void onCreateConnectionResponse(CreateConnectionResponse response, ActivityContextInterface aci) {
		tracer.info(String.format("CallID = %s, State=%s, Receive CRCX response", getCallID(), getState()));
		System.out.println("handler=" + this.getCrcxResponseHandler());
		attach(this.getCrcxResponseHandler());
	}

	private void attach(SbbLocalObject sbbLocalObject) {
		this.getISUPServerTxActivity().attach(sbbLocalObject);
		this.getConnectionActivity().attach(sbbLocalObject);
	}

	private SbbLocalObject getSbbLocalObject(String name) {
		ChildRelation relation = null;
		if (name.equals("ConnectionCreated")) {
			relation = this.getIsupConnectionCreatedSbb();
		} else if (name.equals("ConnectionDeleted")) {
			relation = this.getIsupConnectionDeletedSbb();
		}
		try {
			return relation.create();
		} catch (Exception e) {
			this.tracer.severe("Error while creating child", e);
			return null;
		}
	}

	public abstract ChildRelation getIsupConnectionCreatedSbb();

	public abstract ChildRelation getIsupConnectionDeletedSbb();

	public abstract void setCrcxResponseHandler(SbbLocalObject crcxResponseHandler);

	public abstract SbbLocalObject getCrcxResponseHandler();

}
