package org.mobicents.mgcp;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;

import java.text.ParseException;


public class AuditEndpointHandler extends TransactionHandler {

	public AuditEndpointHandler(JainMgcpStackImpl stack) {
		super(stack);
	}
	
	@Override
	protected JainMgcpCommandEvent decodeCommand(String message)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JainMgcpResponseEvent decodeResponse(String message)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String encode(JainMgcpCommandEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String encode(JainMgcpResponseEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

}
