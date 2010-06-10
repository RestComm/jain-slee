package org.mobicents.slee.demo.ivr.isup;

import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.AddressPlan;

import org.mobicents.slee.demo.ivr.CallConnectedEvent;

/**
 * 
 * @author amit bhayani
 */
public abstract class IsupConnectionCreatedSbb extends BaseSbb {

	public void onCreateConnectionResponse(CreateConnectionResponse response, ActivityContextInterface aci) {
		ReturnCode returnCode = response.getReturnCode();
		tracer.info(String.format("CallID = %s, State=%s, Receive ACK request ", getCallID(), getState()));
		switch (returnCode.getValue()) {
		case ReturnCode.TRANSACTION_BEING_EXECUTED:
			// provisional response received, awaitng for final response
			break;
		case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:

			// DS0 Endpoint
			String bChannEndpoint = response.getSpecificEndpointIdentifier().getLocalEndpointName();

			ActivityContextInterface isupSerTxActivity = getISUPServerTxActivity();
			asSbbActivityContextInterface(isupSerTxActivity).setBChannEndpoint(bChannEndpoint);

			// IVR Endpoint
			String ivrEndpoint = response.getSecondEndpointIdentifier().getLocalEndpointName();
			asSbbActivityContextInterface(isupSerTxActivity).setIVREndpoint(ivrEndpoint);

			setState(IsupConnectionState.CONNECTION_CONNECTED);
			tracer.info(String.format("CallID = %s, State=%s", getCallID(), getState()));

			CallConnectedEvent evt = new CallConnectedEvent("79023629581", this.getEndpointID(), response
					.getSecondConnectionIdentifier().toString());
			Address address = new Address(AddressPlan.UNDEFINED, "IVR");
			this.fireCallConnected(evt, this.getConnectionActivity(), address);

			break;
		default:
			setState(IsupConnectionState.CONNECTION_FAILED);
			// TODO Send REL
		}
	}

	public abstract void fireCallConnected(CallConnectedEvent event, ActivityContextInterface aci,
			javax.slee.Address address);

}
