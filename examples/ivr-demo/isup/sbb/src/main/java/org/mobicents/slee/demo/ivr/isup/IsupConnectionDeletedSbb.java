package org.mobicents.slee.demo.ivr.isup;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.DeleteConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import javax.slee.ActivityContextInterface;

import net.java.slee.resource.mgcp.MgcpEndpointActivity;

import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;

/**
 * 
 * @author amit bhayani
 */
public abstract class IsupConnectionDeletedSbb extends BaseSbb {

	public void onRelease(ReleaseMessage event, ActivityContextInterface aci) {
		tracer.info(String.format("CallID = %s, State=%s, Deleting connection ", getCallID(), getState()));
		this.setState(IsupConnectionState.DS0_CONNECTION_DELETED);
		this.deleteDS0Endpoint();
	}

	public void onCreateConnectionResponse(CreateConnectionResponse response, ActivityContextInterface aci) {
		ReturnCode returnCode = response.getReturnCode();
		switch (returnCode.getValue()) {
		case ReturnCode.TRANSACTION_BEING_EXECUTED:
			// provisional response received, awaitng for final response
			break;
		case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:
			this.setState(IsupConnectionState.DS0_CONNECTION_DELETED);
			this.deleteDS0Endpoint();
			break;
		default:
			// ignore response
		}
	}

	public void onConnectionDeleted(DeleteConnectionResponse response, ActivityContextInterface aci) {
		tracer.info(String
				.format("CallID = %s, State=%s, Receive Delete connection response ", getCallID(), getState()));

		ReturnCode returnCode = response.getReturnCode();
		switch (returnCode.getValue()) {
		case ReturnCode.TRANSACTION_BEING_EXECUTED:
			// provisional response
			break;
		case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:
		case ReturnCode.CONNECTION_WAS_DELETED:
			switch (getState()) {
			case DS0_CONNECTION_DELETED:

				// Delete the IVR Endpoint Connection
				String endpointName = this.getEndpointID();
				EndpointIdentifier endpointID = new EndpointIdentifier(endpointName, JBOSS_BIND_ADDRESS + ":"
						+ MGCP_PEER_PORT);

				MgcpEndpointActivity mgcpEndpointActivity = this.mgcpProvider.getEndpointActivity(endpointID);
				ActivityContextInterface activityContextInterface = this.mgcpAcif
						.getActivityContextInterface(mgcpEndpointActivity);

				activityContextInterface.attach(this.sbbContext.getSbbLocalObject());

				DeleteConnection deleteConnection = new DeleteConnection(this, endpointID);
				deleteConnection.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
				
				this.setState(IsupConnectionState.IVR_CONNECTION_DELETED);
				
				mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { deleteConnection });

				break;
			case IVR_CONNECTION_DELETED:
				this.tracer.info("IVR also deleted everything ok");
				break;
			default:
				break;

			}
			tracer.info(String.format("CallID = %s, State=%s ", getCallID(), getState()));

			break;
		case ReturnCode.ENDPOINT_UNKNOWN:
		case ReturnCode.UNKNOWN_CALL_ID:
		case ReturnCode.INCORRECT_CONNECTION_ID:
			break;
		default:
			// TODO Send error
		}
	}

}
