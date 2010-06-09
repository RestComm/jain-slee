package org.mobicents.slee.demo.ivr.isup;

import jain.protocol.ip.mgcp.message.ModifyConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import javax.slee.ActivityContextInterface;


/**
 *
 * @author amit bhayani
 */
public abstract class IsupConnectionModifiedSbb extends BaseSbb {

    public void onModified(ModifyConnectionResponse response, ActivityContextInterface aci) {
        ReturnCode returnCode = response.getReturnCode();
        switch (returnCode.getValue()) {
            case ReturnCode.TRANSACTION_BEING_EXECUTED :
                break;
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY :
                setState(IsupConnectionState.CONNECTION_CONNECTED);
                tracer.info(String.format("CallID = %s, State=%s ", getCallID(), getState()));                
                break;
            default :
                setState(IsupConnectionState.CONNECTION_FAILED);
                tracer.info(String.format("CallID = %s, State=%s ", getCallID(), getState()));                
                
//                Response cancel = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, getRequest());
//                sipProvider.sendResponse(cancel);
        }
    }
    
}
