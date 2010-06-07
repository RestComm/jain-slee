/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.demo.ivr.sip;

import jain.protocol.ip.mgcp.message.ModifyConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import org.mobicents.slee.demo.ivr.media.ConnectionState;

/**
 *
 * @author kulikov
 */
public abstract class ConnectionModifiedSbb extends BaseSbb {

    public void onModified(ModifyConnectionResponse response, ActivityContextInterface aci) {
        ReturnCode returnCode = response.getReturnCode();
        switch (returnCode.getValue()) {
            case ReturnCode.TRANSACTION_BEING_EXECUTED :
                break;
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY :
                setState(ConnectionState.CONNECTION_CONNECTED);
                tracer.info(String.format("CallID = %s, State=%s ", getCallID(), getState()));                
                break;
            default :
                setState(ConnectionState.CONNECTION_FAILED);
                tracer.info(String.format("CallID = %s, State=%s ", getCallID(), getState()));                
                
//                Response cancel = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, getRequest());
//                sipProvider.sendResponse(cancel);
        }
    }
    
}
