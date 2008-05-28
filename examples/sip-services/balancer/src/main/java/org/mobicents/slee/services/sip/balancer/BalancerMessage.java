package org.mobicents.slee.services.sip.balancer;

import java.io.Serializable;

public interface BalancerMessage extends Serializable{

	
	public BalancerMessageType getType();
	public Object getContent();
	
	
}
