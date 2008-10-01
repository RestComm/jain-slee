package org.mobicents.slee.services.sip.common;

import java.util.List;
import java.util.Map;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;
import javax.sip.message.Response;






public interface MessageHandlerInterface {

	void processRequest(ServerTransaction stx, Request req);
	void processResponse(ServerTransaction stx, ClientTransaction ctx,Response resp);
	
	
}
