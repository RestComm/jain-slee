package org.mobicents.slee.demo.ivr.isup;

import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import javax.slee.ActivityContextInterface;

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
			String endpoint = response.getSpecificEndpointIdentifier().getLocalEndpointName();

			ActivityContextInterface isupSerTxActivity = getISUPServerTxActivity();
			asSbbActivityContextInterface(isupSerTxActivity).setEndpoint(endpoint);

			// TODO Send ACM message

			break;
		default:
			setState(IsupConnectionState.CONNECTION_FAILED);
			// TODO Send REL
		}
	}

}
